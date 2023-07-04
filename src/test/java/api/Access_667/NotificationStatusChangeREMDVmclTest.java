package api.Access_667;

import UI.SQL;
import UI.TestListener;
import api.BaseAPI;
import api.TestListenerApi;
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
@Feature("Оповещение о смене статуса СЭМД")
public class NotificationStatusChangeREMDVmclTest extends BaseAPI {
    Document_667 doc;
    SQL sql;
    String Value;
    String URLCollback;

    public void NotificationStatusChangeREMDVmclMethod(
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
            } else {
                driver.get("http://192.168.2.126:10227");
            }
            Thread.sleep(1500);
            System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
            xml.ApiSmd(
                    File, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            if (vmcl != 99) {
                sql.StartConnection(
                        "SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    Value = sql.resultSet.getString("id");
                    System.out.println(Value);
                    Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + sms + "");
                }
                System.out.println(
                        "В таблице " + sms + " устанавливаем нужный uuid, который будет в запросе на смену статуса");
                sql.UpdateConnection(
                        "update " + sms + " set request_id = '" + doc.uuid + "' where id = " + Value + ";");
                System.out.println("Отправляем запрос на смену статуса");
                if (KingNumber == 1) {
                    URLCollback = "http://192.168.2.126:1108/onko/callback";
                }
                if (KingNumber == 2) {
                    URLCollback = "http://192.168.2.126:1131/onko/callback";
                }
                if (KingNumber == 4 && vmcl == 1) {
                    URLCollback = "http://212.96.206.70:1109/CallBackSoapBinding.svc";
                }
                if (KingNumber == 4 && vmcl == 2) {
                    URLCollback = "http://212.96.206.70:1109/prevention/callback";
                }
                if (KingNumber == 4 && vmcl == 3) {
                    URLCollback = "http://212.96.206.70:1109/akineo/callback.svc";
                }
                if (KingNumber == 4 && vmcl == 4) {
                    URLCollback = "http://212.96.206.70:1109/ssz/callbacksoapbinding.svc";
                }
                given()
                        .filter(new AllureRestAssured())
                        .header("Authorization", "Bearer " + Token)
                        .contentType(ContentType.JSON)
                        .when()
                        .body(doc.body)
                        .post(URLCollback)
                        .then()
                        .statusCode(200);
                Thread.sleep(2000);
            } else {
                Thread.sleep(4000);
                sql.StartConnection(
                        "SELECT * FROM " + sms + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    Value = sql.resultSet.getString("id");
                    Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + sms + "");
                    System.out.println(Value);
                }
                if (KingNumber == 1) {
                    URLCollback = "http://192.168.2.126:1108/kremd/callback";
                }
                if (KingNumber == 2) {
                    URLCollback = "http://192.168.2.126:1131/kremd/callback";
                }
                if (KingNumber == 4) {
                    URLCollback = "http://192.168.2.21:34154/kremd/callback";
                }
                String value = String.valueOf(given()
                                                      .filter(new AllureRestAssured())
                                                      .header("Authorization", "Bearer " + Token)
                                                      .contentType(ContentType.JSON)
                                                      .when()
                                                      .body("{\n" +
                                                                    "  \"id\": \"" + xml.uuid + "\",\n" +
                                                                    "  \"emdrId\": \"Проверка Уведомлений по РЭМД (667)\",\n" +
                                                                    "  \"status\": \"success\",\n" +
                                                                    "  \"registrationDateTime\": \"2022-08-30\",\n" +
                                                                    "  \"errors\": [\n" +
                                                                    "    {\n" +
                                                                    "      \"code\": \"string\",\n" +
                                                                    "      \"message\": \"Проверка Уведомлений по РЭМД (667)\"\n" +
                                                                    "    }\n" +
                                                                    "  ]\n" +
                                                                    "}")
                                                      .post(URLCollback)
                                                      .prettyPeek()
                                                      .then()
                                                      .statusCode(200)
                                                      .extract().jsonPath().getString("id"));
                Assertions.assertEquals(value, "" + xml.uuid + "", "Не сменился статус");
            }
            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
            String text = driver.findElement(By.xpath("//div[@class='text-center']/div[1]/span")).getText();
            System.out.println(text);
            System.out.println(xml.uuid);
            Assertions.assertTrue(text.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                                  "Оповещение для vmcl = " + vmcl + " не добавилось");
            Assertions.assertTrue(text.contains("\"EmdId\":\"Проверка Уведомлений по РЭМД (667)\""),
                                  "Оповещение для vmcl = " + vmcl + " не добавилось");
            if (vmcl != 99) {
                sql.StartConnection("SELECT * FROM " + log + "  where sms_id = " + Value + ";");
                while (sql.resultSet.next()) {
                    String Value1 = sql.resultSet.getString("sms_id");
                    String Value2 = sql.resultSet.getString("description");
                    String Value3 = sql.resultSet.getString("msg_id");
                    Assertions.assertEquals(Value1, Value, "СМС не добавилось в таблицу " + log + "");
                    Assertions.assertEquals(
                            Value2, "Проверяем отправку уведомлений",
                            "СМС не добавилось в таблицу " + log + " с сообщением - Проверяем отправку по заявке 667"
                    );
                    Assertions.assertEquals(
                            Value3, "" + doc.uuid + "",
                            "СМС не добавилось в таблицу " + log + " с msg_id - 6cb41323-d454-4378-9538-b329ca33b535"
                    );
                }
            } else {
                sql.StartConnection("SELECT * FROM " + sms + "  where id = " + Value + ";");
                while (sql.resultSet.next()) {
                    String local_uid = sql.resultSet.getString("local_uid");
                    String errors = sql.resultSet.getString("errors");
                    String fremd_status = sql.resultSet.getString("fremd_status");
                    String emd_id = sql.resultSet.getString("emd_id");
                    Assertions.assertEquals(local_uid, "" + xml.uuid + "", "СМС не добавилось в таблицу " + sms + "");
                    Assertions.assertEquals(
                            errors, "[{\"Code\": \"string\", \"Message\": \"Проверка Уведомлений по РЭМД (667)\"}]",
                            "СМС не добавилось в таблицу " + sms + " с сообщением - Проверка 1034"
                    );
                    Assertions.assertEquals(
                            fremd_status, "1", "СМС не добавилось в таблицу " + sms + " с fremd_status - 1");
                    Assertions.assertEquals(
                            emd_id, "Проверка Уведомлений по РЭМД (667)",
                            "СМС не добавилось в таблицу " + sms + " с emd_id - Проверка 1034"
                    );
                }
            }
        }
    }

    @Issue(value = "TEL-667")
    @Link(name = "ТМС-1185", url = "https://team-1okm.testit.software/projects/5/tests/1185?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Оповещение о смене статуса СЭМД, vmcl=1")
    @Description("Отправить СЭМД, сменить статус, проверить, что приходит оповещение")
    public void NotificationStatusChangeREMDVmcl_1() throws IOException, SQLException, InterruptedException {
        NotificationStatusChangeREMDVmclMethod(
                "SMS/SMS3.xml", "3", 1, "vimis.sms", "vimis.documentlogs", 3, 1, 9, 18, 1, 57, 21);

    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД, vmcl=2")
    public void NotificationStatusChangeREMDVmcl_2() throws IOException, SQLException, InterruptedException {
        NotificationStatusChangeREMDVmclMethod(
                "SMS/SMS3.xml", "3", 2, "vimis.preventionsms", "vimis.preventionlogs", 3, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД, vmcl=3")
    public void NotificationStatusChangeREMDVmcl_3() throws IOException, SQLException, InterruptedException {
        NotificationStatusChangeREMDVmclMethod(
                "SMS/SMS3.xml", "3", 3, "vimis.akineosms", "vimis.akineologs", 2, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД, vmcl=4")
    public void NotificationStatusChangeREMDVmcl_4() throws IOException, SQLException, InterruptedException {
        NotificationStatusChangeREMDVmclMethod(
                "SMS/SMS3.xml", "3", 4, "vimis.cvdsms", "vimis.cvdlogs", 2, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД, vmcl=99")
    public void NotificationStatusChangeREMDVmcl_99() throws IOException, SQLException, InterruptedException {
        NotificationStatusChangeREMDVmclMethod(
                "SMS/SMS3.xml", "3", 99, "vimis.remd_sent_result", "", 2, 1, 9, 18, 1, 57, 21);
    }
}
