package UI.TmTest.AccessUI.Directions;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.RRP;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class Access_1039Test extends BaseTest {
    MedOrganization medOrganization;
    AuthorizationObject authorizationObject;
    Users users;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    SQL sql;
    RRP rrp;

    @Issue(value = "TEL-1039")
    @Link(name = "ТМС-1381", url = "https://team-1okm.testit.software/projects/5/tests/1381?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Правки в интерфейс на странице создания пациента")
    @Description("Проверяем блок Данные о страховом полисе, Социальный статус, Место работы на обязательность в различных условиях")
    public void Access_1039() throws IOException, SQLException, InterruptedException {
        Calendar calendar = new GregorianCalendar();
        equipmentSchedule = new EquipmentSchedule(driver);
        medOrganization = new MedOrganization(driver);
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        sql = new SQL();
        rrp = new RRP(driver);
        Faker faker = new Faker();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            /** Генерация снилса для добавления пользователя */
            System.out.println("Генерация снилса для добавления пользователя");
            driver.get(GENERATE_SNILS);
            WaitElement(authorizationObject.ButtonNewNumberWait);
            authorizationObject.ButtonNewNumber.click();
            String text = authorizationObject.Snils.getText();
            System.out.println("Новый СНИЛС: " + text);
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
            WaitElement(directionsForQuotas.SnilsWait);
            directionsForQuotas.Snils.sendKeys(text);
            Thread.sleep(2000);
            ClickElement(directionsForQuotas.BigSearchWait);
            /** Создание пациента для консультации */
            System.out.println("Создание пациента для консультации");
            WaitElement(directionsForQuotas.CreatePatientWait);
            directionsForQuotas.CreatePatient.click();
            WaitElement(directionsForQuotas.RemoteConsultationForDiagnostics);
            directionsForQuotas.AddSnils.sendKeys(text);
            inputWord(directionsForQuotas.LastName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
            inputWord(directionsForQuotas.Name, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
            inputWord(directionsForQuotas.MiddleName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
            System.out.println("Проверка отображения Данные о страховом полисе");
            ClickElement(directionsForQuotas.Polis);
            WaitNotElement3(directionsForQuotas.PolisTypeError, 3);
            System.out.println("Проверка отображения - Тип полиса обязателен для заполнения");
            inputWord(directionsForQuotas.PolisENP, "12332145678945612");
            WaitElement(directionsForQuotas.PolisTypeError);
            System.out.println("Проверка значения Тип полиса с бд");
            ClickElement(directionsForQuotas.PolisTypeWait);
            Thread.sleep(1000);
            List<String> Type = new ArrayList<String>();
            List<WebElement> TypeWeb = driver.findElements(directionsForQuotas.StatusSelectT);
            for (int i = 0; i < TypeWeb.size(); i++) {
                Type.add(TypeWeb.get(i).getText());
            }
            List<String> TypeSql = new ArrayList<String>();
            sql.StartConnection("Select * from telmed.hst0065;");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("namepolicytype");
                TypeSql.add(sql.value);
            }
            Assertions.assertEquals(Type, TypeSql, "Не совпадает Тип полиса с значениями в БД");
            System.out.println("Указываем дату рождения с которой прошло менее 14 лет");
            System.out.println("Высчитываем нужный год");
            String Year = String.valueOf(calendar.get(Calendar.YEAR));
            String Day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            Integer IntYear = Integer.valueOf(Year) - 14;
            Integer IntDay = Integer.valueOf(Day) + 1;
            ClickElement(directionsForQuotas.DateWait);
            String YearNow = directionsForQuotas.Year.getText();
            while (!YearNow.contains(String.valueOf(IntYear))) {
                directionsForQuotas.BeforeYear.click();
                YearNow = directionsForQuotas.Year.getText();
            }
            if ((isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                if (isElementNotVisible(By.xpath(
                        "//table[@class='el-date-table']//tr/td[@class='next-month']//span[text()=" + IntDay + "]"))) {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
                } else {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]"));
                }
            }
            if (!(isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
            }
            System.out.println("Проверяем, что поле Гражданство не обязательно");
            WaitNotElement3(directionsForQuotas.CitizenshipError, 3);
            System.out.println("Проверяем, что поле Социальный статус обязательно");
            WaitElement(directionsForQuotas.StatusError);
            System.out.println("Указываем дату рождения с которой прошло ровно 14 лет");
            IntDay = Integer.valueOf(Day);
            ClickElement(directionsForQuotas.DateWait);
            if ((isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                if (isElementNotVisible(By.xpath(
                        "//table[@class='el-date-table']//tr/td[@class='next-month']//span[text()=" + IntDay + "]"))) {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
                } else {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]"));
                }
            }
            if (!(isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
            }
            System.out.println("Проверяем, что поле Гражданство обязательно");
            WaitElement(directionsForQuotas.CitizenshipError);
            System.out.println("Указываем дату рождения с которой прошло более 14 лет");
            IntDay = Integer.valueOf(Day) - 1;
            ClickElement(directionsForQuotas.DateWait);
            if ((isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                if (isElementNotVisible(By.xpath(
                        "//table[@class='el-date-table']//tr/td[@class='next-month']//span[text()=" + IntDay + "]"))) {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
                } else {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]"));
                }
            }
            if (!(isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
            }
            System.out.println("Проверяем, что поле Гражданство обязательно");
            WaitElement(directionsForQuotas.CitizenshipError);
            System.out.println("Указываем дату рождения с которой прошло менее 8 лет");
            IntYear = Integer.valueOf(Year) - 8;
            IntDay = Integer.valueOf(Day) + 1;
            ClickElement(directionsForQuotas.DateWait);
            YearNow = directionsForQuotas.Year.getText();
            while (!YearNow.contains(String.valueOf(IntYear))) {
                directionsForQuotas.AfterYear.click();
                YearNow = directionsForQuotas.Year.getText();
            }
            if ((isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                if (isElementNotVisible(By.xpath(
                        "//table[@class='el-date-table']//tr/td[@class='next-month']//span[text()=" + IntDay + "]"))) {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
                } else {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]"));
                }
            }
            if (!(isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
            }
            WaitNotElement3(directionsForQuotas.StatusError, 3);
            System.out.println("Указываем дату рождения с которой прошло ровно 8 лет");
            IntDay = Integer.valueOf(Day);
            ClickElement(directionsForQuotas.DateWait);
            if ((isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                if (isElementNotVisible(By.xpath(
                        "//table[@class='el-date-table']//tr/td[@class='next-month']//span[text()=" + IntDay + "]"))) {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
                } else {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]"));
                }
            }
            if (!(isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
            }
            WaitElement(directionsForQuotas.StatusError);
            System.out.println("Указываем дату рождения с которой прошло более 8 лет");
            IntDay = Integer.valueOf(Day) - 1;
            ClickElement(directionsForQuotas.DateWait);
            if ((isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                if (isElementNotVisible(By.xpath(
                        "//table[@class='el-date-table']//tr/td[@class='next-month']//span[text()=" + IntDay + "]"))) {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
                } else {
                    ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]"));
                }
            }
            if (!(isElementNotVisible(
                    By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[2]")))) {
                ClickElement(By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=" + IntDay + "])[1]"));
            }
            WaitElement(directionsForQuotas.StatusError);
            System.out.println("Указываем Социальный статус Работающий и проверяем обязательность места работы");
            ClickElement(directionsForQuotas.Jobinfo);
            WaitNotElement3(directionsForQuotas.JobError, 3);
            ClickElement(directionsForQuotas.Status);
            ClickElement(directionsForQuotas.StatusSelectJob);
            WaitNotElement3(directionsForQuotas.JobPost, 3);
            System.out.println("Заполняем данные о пациенте для консультации");
            ClickElement(directionsForQuotas.ManWait);
            SelectClickMethod(directionsForQuotas.TypeDocument, directionsForQuotas.SelectTypeDocument);
            inputWord(directionsForQuotas.Serial, "12344");
            inputWord(directionsForQuotas.Number, "1234566");
            actionElementAndClick(directionsForQuotas.Address);
            inputWord(directionsForQuotas.Address, "г Москва, ул Арбат, д 9АФ");
            ClickElement(directionsForQuotas.Select);
            actionElementAndClick(directionsForQuotas.AddressHome);
            ClickElement(directionsForQuotas.Select);
            inputWord(directionsForQuotas.Job, "KOMTEKK");
            inputWord(directionsForQuotas.PostJob, "TESTT");
            SelectClickMethod(directionsForQuotas.PolisTypeWait, directionsForQuotas.PolisTypeSelectEdin);
            actionElementAndClick(directionsForQuotas.NextPatient);
            System.out.println("Заполнение информации о направившем враче");
            directionsForQuotas.DoctorMethod();
            Thread.sleep(3000);
            System.out.println("Переходим в РРП и вводим снилс");
            driver.get("http://192.168.2.21:34142");
            WaitElement(rrp.LoginWait);
            rrp.Login.sendKeys("vimis");
            rrp.Password.sendKeys("ZIiW6O");
            rrp.Enter.click();
            WaitElement(rrp.SnilsWait);
            rrp.Snils.sendKeys(text);
            rrp.Search.click();
            System.out.println("Берём значение Снилс");
            WaitElement(rrp.SearchSnilsWait);
            System.out.println("Убираем пробелы из Снилс");
            String charsToRemove = " !";
            for (char c : charsToRemove.toCharArray()) {
                text = text.replace(String.valueOf(c), "");
            }
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
                    .queryParam("snils", text)
                    .header("Authorization", "Bearer " + value)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("http://192.168.2.21:34142/IEMKRegionalService/services/patient/search")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            assertThat(response.get("size"), equalTo(1));
            assertThat(response.get("patients[0].snils"), equalTo("" + text + ""));
        }
    }
}
