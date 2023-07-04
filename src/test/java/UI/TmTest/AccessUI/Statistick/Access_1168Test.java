package UI.TmTest.AccessUI.Statistick;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
public class Access_1168Test extends BaseTest {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;
    public String Code;
    public String CodeName;
    public String TokenRRP;

    @Test
    @Issue(value = "TEL-1168")
    @Link(name = "ТМС-1439", url = "https://team-1okm.testit.software/projects/5/tests/1439?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение данных по пациенту на странице Маршрутов - Пациент")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок где есть маршруты, открываем МО выбираем пациента и сверяем данные пациента с бд")
    public void Access_1168() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();

        if (KingNumber != 4) {
            System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
            AuthorizationMethod(authorizationObject.MIAC);
            ClickElement(analyticsMO.Analytics);
            System.out.println("Проверяем где есть маршруты, во всех блоках");
            Thread.sleep(3000);
            analyticsMO.QuantityStackMethod();
            System.out.println("Переходим в первый попавшийся блок, у которого есть мо с этапами");
            if (AnalyticsMO.TallMO) {
                ClickElement(analyticsMO.NameMOTallFirst);

            } else {
                if (AnalyticsMO.AverageMO) {
                    ClickElement(analyticsMO.NameMOAverageFirst);
                } else {
                    ClickElement(analyticsMO.NameMOlowFirst);
                }
            }
            System.out.println("Выбираем первого пациента и переходим к нему");
            ClickElement(analyticsMO.FirstPatient);
            WaitElement(analyticsMO.Snils);
            Thread.sleep(1500);
            System.out.println("Берём данные пациента");
            String FIO = driver.findElement(analyticsMO.FIO).getText();
            String Date = driver.findElement(analyticsMO.Date).getText();
            String Phone = null;
            if (isElementNotVisible(analyticsMO.Phone)) {
                Phone = driver.findElement(analyticsMO.Phone).getText();
            }
            String Snils = driver.findElement(analyticsMO.Snils).getText();
            System.out.println("Изменяем формат даты (в бд отличается)");
            String Date1 = StringUtils.substring(Date, 0, 10);
            String dd = Date1.substring(0, Date1.length() - 8);
            String mm = Date1.substring(0, Date1.length() - 5);
            String mm1 = mm.substring(3);
            String yy = Date1.substring(6);
            String DateNew = yy + "-" + mm1 + "-" + dd;
            System.out.println("Берём диагнозы и их названия");
            List<String> DiagnosisWeb = new ArrayList<>();
            List<WebElement> QuaDiagnosis = driver.findElements(analyticsMO.QuantityDiagnosis);
            for (int i = 1; i < QuaDiagnosis.size() + 1; i++) {
                Code = driver.findElement(By.xpath(
                        "//li/div[contains(.,'Диагноз')]/following-sibling::div//ul/li[" + i + "]//span")).getText();
                CodeName = driver.findElement(
                        By.xpath(
                                "//li/div[contains(.,'Диагноз')]/following-sibling::div//ul/li[" + i + "]//p")).getText();
                DiagnosisWeb.add(Code + " " + CodeName);
            }
            String PhoneSql = null;

            System.out.println("Авторизуемся в РРП и берём токен");
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .when()
                    .body("{\n" +
                                  "    \"UserName\": \"vimis\",\n" +
                                  "    \"Password\": \"ZIiW6O\"\n" +
                                  "}")
                    .post("http://192.168.2.21:34142/api/authenticate")
                    .body()
                    .jsonPath();
            TokenRRP = response.get("access_token");

            System.out.println("Ищем пациента в РРП");
            JsonPath respons = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + TokenRRP)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("http://192.168.2.21:34142/IEMKRegionalService/services/patient/search/?snils=" + Snils)
                    .body()
                    .jsonPath();
            String DateRRP = respons.get("patients[0].Policy.ChangeDate");
            String DateRRPNew = StringUtils.substring(DateRRP, 0, 10);
            String LastName = respons.get("patients[0].LastName");
            String FirstName = respons.get("patients[0].FirstName");
            String MiddleName = respons.get("patients[0].MiddleName");

            Assertions.assertEquals(FIO, LastName + " " + FirstName + " " + MiddleName, "ФИО не совпадает");
            Assertions.assertEquals(DateNew, DateRRPNew, "Дата не совпадает");

            System.out.println("Сравниваем Диагнозы с БД");
            List<String> DiagnosisSQL = new ArrayList<>();
            sql.StartConnection("select np.diagnosis, m.mkb_name  from vimis.nosological_patients np \n" +
                                        "join vimis.nosological_patients_details npd on np.patient_guid = npd.patient_guid\n" +
                                        "join dpc.mkb10 m on np.diagnosis = m.mkb_code \n" +
                                        "where npd.snils = '" + Snils + "'");
            while (sql.resultSet.next()) {
                String Code = sql.resultSet.getString("diagnosis");
                String CodeName = sql.resultSet.getString("mkb_name");
                DiagnosisSQL.add(Code + " " + CodeName);
            }
            Assertions.assertEquals(DiagnosisWeb, DiagnosisSQL, "Диагнозы не совпадают");
        }
    }
}


