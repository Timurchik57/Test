package api;

import UI.SQL;
import UI.TestListener;
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
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Оповещение с типом 8")
public class Access_847Test extends BaseAPI {
    public Integer value;
    public String URLKremd;
    public String TransferId;

    public void Access_847TestMethod(
            String FileName, String Doctype, Integer Vmlc, Integer docTypeVersion, Integer Role, Integer position,
            Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String remd,
            String logs
    ) throws IOException, SQLException, InterruptedException {
        sql = new SQL();
        xml = new XML();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Переходим на сайт для перехвата сообщений");
            if (KingNumber == 4) {
                driver.get("http://192.168.2.21:34329");
            } else {
                driver.get("http://192.168.2.126:10227");
            }
            Thread.sleep(1500);
            System.out.println("Отправляем смс с Doctype = " + Doctype + " и vmcl=" + Vmlc + "");
            xml.ApiSmd(
                    FileName, Doctype, Vmlc, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            if (Vmlc == 99) {
                sql.StartConnection(
                        "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value = Integer.valueOf(sql.resultSet.getString("id"));
                    TransferId = sql.resultSet.getString("transfer_id");
                    System.out.println(value);
                }
            } else {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value = Integer.valueOf(sql.resultSet.getString("id"));
                    TransferId = sql.resultSet.getString("transfer_id");
                    System.out.println(value);
                }
                sql.UpdateConnection(
                        "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + value + "', '" + UUID.randomUUID() + "')");
                sql.UpdateConnection(
                        "insert into " + remd + " (sms_id, local_uid, status, created_datetime, fremd_status) values (" + value + ", '" + xml.uuid + "', 'success', '" + Date + " 00:00:00.888 +0500', NULL);");
            }
            if (KingNumber == 1) {
                URLKremd = "http://192.168.2.126:1108/kremd/callback/mse";
            }
            if (KingNumber == 2) {
                URLKremd = "http://192.168.2.126:1131/kremd/callback/mse";
            }
            if (KingNumber == 4) {
                URLKremd = "http://212.96.206.70:1109/kremd/callback/mse";
            }
            String value1 = String.valueOf(given()
                                                   .filter(new AllureRestAssured())
                                                   .header("Authorization", "Bearer " + Token)
                                                   .contentType(ContentType.JSON)
                                                   .when()
                                                   .body("{\n" +
                                                                 "  \"associations\": [\n" +
                                                                 "    {\n" +
                                                                 "      \"target\": \"Тест заявки 847\"\n" +
                                                                 "    }\n" +
                                                                 "  ],\n" +
                                                                 "  \"id\": \"" + xml.uuid + "\", \n" +
                                                                 "  \"emdrId\": \"string\"\n" +
                                                                 "}")
                                                   .post(URLKremd)
                                                   .prettyPeek()
                                                   .then()
                                                   .statusCode(200)
                                                   .extract().jsonPath().getString("id"));
            Assertions.assertEquals(value1, "" + xml.uuid + "");
            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
            String text = driver.findElement(By.xpath("//div[@class='text-center']/div[1]/span")).getText();
            System.out.println(text);
            System.out.println(xml.uuid);
            Assertions.assertTrue(
                    text.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                    "Оповещение для vmcl = " + Vmlc + " не добавилось"
            );
            Assertions.assertTrue(
                    text.contains("\"TransferId\":\"" + TransferId + "\""),
                    "Оповещение для vmcl = " + Vmlc + " не добавилось"
            );
            if (Vmlc == 99) {
                sql.StartConnection("Select * from " + remd + " where id = " + value + "");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("target_emdid");
                    Assertions.assertEquals(sql.value, "Тест заявки 847", "fremd_status не сменился на 1");
                }
            } else {
                sql.StartConnection("Select * from " + remd + " where sms_id = " + value + "");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("target_emdid");
                    Assertions.assertEquals(sql.value, "Тест заявки 847", "fremd_status не сменился на 1");
                }
            }
        }
    }

    @Issue(value = "TEL-847")
    @Link(name = "ТМС-", url = "")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Оповещение с типом 8")
    @Description("Отправить смс с vmcl = 99, после использовать запрос /kremd/callback/mse для смены статуса. Далее переходим в сервис перехвата сообщений и проверяем, что оповещение пришло")
    public void Access_847_99() throws IOException, SQLException, InterruptedException {
        Access_847TestMethod(
                "SMS/SMS3.xml", "3", 99, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.remd_sent_result",
                "vimis.documentlogs"
        );
    }
}
