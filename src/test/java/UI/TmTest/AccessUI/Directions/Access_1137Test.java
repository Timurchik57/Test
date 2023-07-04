package UI.TmTest.AccessUI.Directions;

import UI.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import api.BaseAPI;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class Access_1137Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    public String LastName;
    public String FirstName;
    public String MiddleName;
    public String SnilsOld;
    public String SnilsNew;

    @Issue(value = "TEL-1137")
    @Link(name = "ТМС-1443", url = "https://team-1okm.testit.software/projects/5/tests/1443?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Обновление данных в РРП при сочетании ФИО+Дата рождения")
    @Description("Перейти в Консультации - Создать направление - Направление на диагностику/консультацию - Создать пациента. Далее создаём ещё одного пациента но с ФИО + Дата рождения первого. В РРП должен сохраниться только второй СНИЛС")
    public void Access_1137() throws IOException, SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Генерация снилса для добавления пользователя");
            authorizationObject.GenerateSnilsMethod();
            SnilsOld = authorizationObject.GenerationSnils.replaceAll(" ", "");
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
            WaitElement(directionsForQuotas.CreatePatientWait);
            directionsForQuotas.CreatePatient.click();
            directionsForQuotas.CreatePatientMethod(SnilsOld);
            directionsForQuotas.DoctorMethod();
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
                    .queryParam("snils", SnilsOld)
                    .header("Authorization", "Bearer " + value)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("http://192.168.2.21:34142/IEMKRegionalService/services/patient/search")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            LastName = response.get("patients[0].LastName");
            FirstName = response.get("patients[0].FirstName");
            MiddleName = response.get("patients[0].MiddleName");
            System.out.println("Создаём нового пациента заново");
            authorizationObject.GenerateSnilsMethod();
            System.out.println("Авторизация и переход в пользователи");
            AuthorizationMethod(authorizationObject.OKB);
            WaitElement(directionsForQuotas.Unfinished);
            actionElementAndClick(directionsForQuotas.Consultation);
            SnilsNew = authorizationObject.GenerationSnils.replaceAll(" ", "");
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
            WaitElement(directionsForQuotas.CreatePatientWait);
            directionsForQuotas.CreatePatient.click();
            WaitElement(directionsForQuotas.SnilsWait);
            directionsForQuotas.Snils.sendKeys(SnilsNew);
            inputWord(directionsForQuotas.LastName, LastName);
            inputWord(directionsForQuotas.Name, FirstName);
            inputWord(directionsForQuotas.MiddleName, MiddleName);
            System.out.println("Заполняем данные о пациенте для консультации");
            SelectClickMethod(directionsForQuotas.DateWait, directionsForQuotas.SelectDate);
            ClickElement(directionsForQuotas.ManWait);
            SelectClickMethod(directionsForQuotas.TypeDocument, directionsForQuotas.SelectTypeDocument);
            inputWord(directionsForQuotas.Serial, "12344");
            inputWord(directionsForQuotas.Number, "1234566");
            actionElementAndClick(directionsForQuotas.Address);
            inputWord(directionsForQuotas.Address, "г Москва, ул Арбат, д 9АФ");
            ClickElement(directionsForQuotas.Select);
            actionElementAndClick(directionsForQuotas.AddressHome);
            ClickElement(directionsForQuotas.Select);
            actionElementAndClick(directionsForQuotas.NextPatient);
            directionsForQuotas.DoctorMethod();
            System.out.println("Ищем пациента в РРП через АПИ");
            response = given()
                    .filter(new AllureRestAssured())
                    .queryParam("snils", SnilsNew)
                    .header("Authorization", "Bearer " + value)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("http://192.168.2.21:34142/IEMKRegionalService/services/patient/search")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            Assertions.assertEquals(response.get("patients[0].snils"), SnilsNew, "Новый снилс не заменился");
            System.out.println("Ищем старого пациента");
            response = given()
                    .filter(new AllureRestAssured())
                    //.log().all()
                    .queryParam("snils", SnilsOld)
                    .header("Authorization", "Bearer " + value)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("http://192.168.2.21:34142/IEMKRegionalService/services/patient/search")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            Assertions.assertNotEquals(
                    response.get("ErrorText"), "По указанным параметрам пациенты не найдены.", "Старый снилс остался");
        }
    }
}
