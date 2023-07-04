package api;

import UI.SQL;
import UI.TestListener;
import api.Access_667.Document_667;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Оповещение о смене статуса с типом 4 для vmcl=3")
public class Access_1076Test extends BaseAPI {
    Document_667 doc;
    SQL sql;
    String Value;
    String URLCollback;
    String BODY;

    public void Access_1076Method(
            String File, String DocType, Integer vmcl, String sms, String log, Integer docTypeVersion, Integer Role,
            Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        doc = new Document_667();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            /** Отправляем смс с id = 1 и vmcl = 1 */
            System.out.println("Переходим на сайт для перехвата сообщений");
            if (KingNumber == 4) {
                driver.get("http://192.168.2.21:34329");

            }
            if (KingNumber == 1 | KingNumber == 2) {
                driver.get("http://192.168.2.126:10227");
            }
            System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
            xml.ReplacementWordInFile(
                    File, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            String value = String.valueOf(given()
                                                  .filter(new AllureRestAssured())
                                                  .header("Authorization", "Bearer " + Token)
                                                  .contentType(ContentType.JSON)
                                                  .when()
                                                  .body(xml.body)
                                                  .post(HostAddress + "/api/smd")
                                                  .prettyPeek()
                                                  .then()
                                                  .statusCode(200)
                                                  .extract().jsonPath().getString("result[0].message"));
            Assertions.assertEquals(value, "СМС по направлению \"АкиНео\" успешно опубликован в ЦУ РС ЕГИСЗ.");
            sql.StartConnection(
                    "SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                Value = sql.resultSet.getString("id");
                System.out.println(Value);
                Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + sms + "");
            }
            System.out.println(
                    "В таблице " + sms + " устанавливаем нужный uuid, который будет в запросе на смену статуса");
            sql.UpdateConnection("update " + sms + " set request_id = '" + doc.uuid + "' where id = " + Value + ";");
            System.out.println("Отправляем запрос на смену статуса");
            if (KingNumber == 1) {
                URLCollback = "http://192.168.2.126:1108/onko/callback";
            }
            if (KingNumber == 2) {
                URLCollback = "http://192.168.2.126:1131/onko/callback";
            }
            if (KingNumber == 4) {
                URLCollback = "http://212.96.206.70:1109/akineo/callback.svc";
            }
            given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .when()
                    .body(doc.body)
                    .post(URLCollback)
                    .then()
                    .statusCode(200);
            Thread.sleep(2000);
            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
            String text = driver.findElement(By.xpath("//div[@class='text-center']/div[1]/span")).getText();
            System.out.println(text);
            System.out.println(xml.uuid);
            Assertions.assertTrue(
                    text.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось"
            );
            Assertions.assertTrue(
                    text.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось"
            );
            sql.StartConnection("SELECT * FROM " + log + "  where sms_id = " + Value + ";");
            while (sql.resultSet.next()) {
                String Value1 = sql.resultSet.getString("sms_id");
                String Value2 = sql.resultSet.getString("description");
                String Value3 = sql.resultSet.getString("msg_id");
                Assertions.assertEquals(Value1, Value, "СМС не добавилось в таблицу " + log + "");
                Assertions.assertEquals(
                        Value2, "Проверяем отправку уведомлений",
                        "СМС не добавилось в таблицу " + log + " с сообщением - Проверяем отправку уведомлений"
                );
                Assertions.assertEquals(
                        Value3, "" + doc.uuid + "",
                        "СМС не добавилось в таблицу " + log + " с msg_id - " + doc.uuid + ""
                );
            }
            Thread.sleep(10000);
        }
    }

    @Issue(value = "TEL-1076")
    @Link(name = "ТМС-1418", url = "https://team-1okm.testit.software/projects/5/tests/1418?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Оповещение о смене статуса с типом 4 для vmcl=3")
    @Description("Авторизоваться под МО с ИС=15, Отправить СЭМД с токеном этой авторизации и проверить изменённое оповещение для типа 4")
    public void Access_1076() throws IOException, SQLException, InterruptedException {
        Access_1076Method("SMS/SMS3.xml", "3", 3, "vimis.akineosms", "vimis.akineologs", 2, 1, 9, 18, 1, 57, 21);
    }
}
