package UI.TmTest.AccessUI.Directions;

import UI.TestListener;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.RRP;
import api.BaseAPI;
import api.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class Access_1069Test extends BaseAPI {
    MedOrganization medOrganization;
    AuthorizationObject authorizationObject;
    Users users;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    RRP rrp;

    @Issue(value = "TEL-1069")
    @Link(name = "ТМС-1386", url = "https://team-1okm.testit.software/projects/5/tests/1386?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отображение данных по пациенту при создании консультации только из РРП")
    @Description("Проверяем пациента из Удкон в РРП и наоборот, таке проверяем пациента в API РРП. Создаём пациента в Удкон проверяем, проверяем добавление его в РРП")
    public void Access_1069() throws IOException, SQLException, InterruptedException {
        equipmentSchedule = new EquipmentSchedule(driver);
        medOrganization = new MedOrganization(driver);
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        rrp = new RRP(driver);
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            /** Авторизация и переход в создание консультации */
            System.out.println("Авторизация и переход в пользователи");
            AuthorizationMethod(authorizationObject.OKB);
            WaitElement(directionsForQuotas.Unfinished);
            actionElementAndClick(directionsForQuotas.Consultation);
            /** Переход в создание консультации  */
            WaitElement(directionsForQuotas.Unfinished);
            actionElementAndClick(directionsForQuotas.Consultation);
            /** Создать консультацию - добавить пациента */
            System.out.println("Создать консультацию - добавить пациента");
            WaitElement(directionsForQuotas.Heading);
            WaitElement(directionsForQuotas.CreateWait);
            directionsForQuotas.Create.click();
            WaitElement(directionsForQuotas.TypeConsultationWait);
            directionsForQuotas.DistrictDiagnostic.click();
            directionsForQuotas.Next.click();
            ClickElement(directionsForQuotas.BigSearchWait);
            System.out.println("Ищем пациента");
            WaitElement(directionsForQuotas.LastNameWait);
            inputWord(directionsForQuotas.LastName, "Ивановв");
            ClickElement(directionsForQuotas.SearchWait);
            WaitElement(directionsForQuotas.listPatientSnilsWait);
            System.out.println("Берём его снилс и ЕНП");
            String snils = directionsForQuotas.listPatientSnils.getText();
            String ENP = directionsForQuotas.listPatientENP.getText();
            System.out.println("Переходим в РРП и вводим снилс");
            driver.get("http://192.168.2.21:34142");
            WaitElement(rrp.LoginWait);
            rrp.Login.sendKeys("vimis");
            rrp.Password.sendKeys("ZIiW6O");
            rrp.Enter.click();
            WaitElement(rrp.SnilsWait);
            rrp.Snils.sendKeys(snils);
            rrp.Search.click();
            System.out.println("Берём значение Снилс и ЕНП");
            WaitElement(rrp.SearchSnilsWait);
            String SearchSnils = rrp.SearchSnils.getText();
            String SearchENP = rrp.SearchEnp.getText();
            Assertions(snils, SearchSnils);
            Assertions(ENP, SearchENP);
            System.out.println("Авторизуемся в РРП через АПИ");
            String value = String.valueOf(given()
                                                  .filter(new AllureRestAssured())
                                                  .contentType(ContentType.JSON)
                                                  .when()
                                                  .body("{\n" +
                                                                "    \"UserName\": \"vimis\",\n" +
                                                                "    \"Password\": \"ZIiW6O\"\n" +
                                                                "}")
                                                  .post("http://192.168.2.21:34142/api/authenticate")
                                                  .then()
                                                  .statusCode(200)
                                                  .extract().jsonPath().getString("access_token"));
            System.out.println("Ищем пациента в РРП через АПИ");
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .queryParam("snils", snils)
                    .header("Authorization", "Bearer " + value)
                    .contentType(ContentType.JSON).when()
                    .get("http://192.168.2.21:34142/IEMKRegionalService/services/patient/search")
                    .body()
                    .jsonPath();
            assertThat(response.get("size"), equalTo(1));
            assertThat(response.get("patients[0].snils"), equalTo("" + snils + ""));
            assertThat(response.get("patients[0].LastName"), equalTo("ИВАНОВ"));
            assertThat(response.get("patients[0].Policy.ENP"), equalTo("" + ENP + ""));
            System.out.println("Генерируем новый снилс");
            authorizationObject.GenerateSnilsMethod();
            /** Авторизация и переход в создание консультации */
            System.out.println("Авторизация и переход в пользователи");
            AuthorizationMethod(authorizationObject.OKB);
            WaitElement(directionsForQuotas.Unfinished);
            actionElementAndClick(directionsForQuotas.Consultation);
            /** Переход в создание консультации  */
            WaitElement(directionsForQuotas.Unfinished);
            actionElementAndClick(directionsForQuotas.Consultation);
            /** Создать консультацию - добавить пациента */
            System.out.println("Создать консультацию - добавить пациента");
            WaitElement(directionsForQuotas.Heading);
            WaitElement(directionsForQuotas.CreateWait);
            directionsForQuotas.Create.click();
            WaitElement(directionsForQuotas.TypeConsultationWait);
            directionsForQuotas.DistrictDiagnostic.click();
            directionsForQuotas.Next.click();
            ClickElement(directionsForQuotas.BigSearchWait);
            ClickElement(directionsForQuotas.CreatePatientWait);
            directionsForQuotas.CreatePatientMethod(authorizationObject.GenerationSnils);
            System.out.println("Заполнение информации о направившем враче");
            directionsForQuotas.DoctorMethod();
            System.out.println("Переходим в РРП и вводим снилс");
            driver.get("http://192.168.2.21:34142");
            WaitElement(rrp.SnilsWait);
            rrp.Snils.sendKeys(authorizationObject.GenerationSnils);
            rrp.Search.click();
            System.out.println("Берём значение Снилс");
            WaitElement(rrp.SearchSnilsWait);
            SearchSnils = rrp.SearchSnils.getText();
            System.out.println("Убираем пробелы из Снилс");
            String charsToRemove = " !";
            for (char c : charsToRemove.toCharArray()) {
                authorizationObject.GenerationSnils = authorizationObject.GenerationSnils.replace(String.valueOf(c),
                                                                                                  "");
            }
            System.out.println("Ищем пациента в РРП через АПИ");
            response = given()
                    .filter(new AllureRestAssured())
                    .queryParam("snils", authorizationObject.GenerationSnils)
                    .header("Authorization", "Bearer " + value)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("http://192.168.2.21:34142/IEMKRegionalService/services/patient/search")
                    .body()
                    .jsonPath();
            assertThat(response.get("size"), equalTo(1));
            assertThat(response.get("patients[0].snils"), equalTo("" + authorizationObject.GenerationSnils + ""));
        }
    }
}
