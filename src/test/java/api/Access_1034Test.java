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

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Уведомления при передаче ПДФ справок в КРЭМД")
public class Access_1034Test extends BaseAPI {
    SQL sql;
    public String value_1034;
    public String URLKremd;

    public void Access1034Method(
            String File, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position,
            Integer speciality, Integer Role1, Integer position1, Integer speciality1, String remd
    ) throws SQLException, IOException, InterruptedException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
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
            sql.StartConnection(
                    "SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value_1034 = sql.resultSet.getString("id");
                Assertions.assertNotEquals(value_1034, "NULL", "СМС не добавилось в таблицу " + remd + "");
                System.out.println(value_1034);
            }
            if (KingNumber == 1) {
                URLKremd = "http://192.168.2.126:1108/kremd/callback";
            }
            if (KingNumber == 2) {
                URLKremd = "http://192.168.2.126:1131/kremd/callback";
            }
            if (KingNumber == 4) {
                URLKremd = "http://192.168.2.21:34154/kremd/callback";
            }
            String value = String.valueOf(given()
                                                  .filter(new AllureRestAssured())
                                                  .header("Authorization", "Bearer " + Token)
                                                  .contentType(ContentType.JSON)
                                                  .when()
                                                  .body("{\n" +
                                                                "  \"id\": \"" + xml.uuid + "\",\n" +
                                                                "  \"emdrId\": \"Проверка 1034\",\n" +
                                                                "  \"status\": \"success\",\n" +
                                                                "  \"registrationDateTime\": \"2022-08-30\",\n" +
                                                                "  \"errors\": [\n" +
                                                                "    {\n" +
                                                                "      \"code\": \"string\",\n" +
                                                                "      \"message\": \"Проверка 1034\"\n" +
                                                                "    }\n" +
                                                                "  ]\n" +
                                                                "}")
                                                  .post(URLKremd)
                                                  .then()
                                                  .statusCode(200)
                                                  .extract().jsonPath().getString("id"));
            Assertions.assertEquals(value, "" + xml.uuid + "", "Не сменился статус");
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
            System.out.println("Переходим в таблицу " + remd + " и проверяем, что статус сменился");
            sql.StartConnection("SELECT * FROM " + remd + "  where id = " + value_1034 + ";");
            while (sql.resultSet.next()) {
                String local_uid = sql.resultSet.getString("local_uid");
                String errors = sql.resultSet.getString("errors");
                String fremd_status = sql.resultSet.getString("fremd_status");
                String emd_id = sql.resultSet.getString("emd_id");
                Assertions.assertEquals(local_uid, "" + xml.uuid + "", "СМС не добавилось в таблицу " + remd + "");
                Assertions.assertEquals(
                        errors, "[{\"Code\": \"string\", \"Message\": \"Проверка 1034\"}]",
                        "СМС не добавилось в таблицу " + remd + " с сообщением - Проверка 1034"
                );
                Assertions.assertEquals(
                        fremd_status, "1", "СМС не добавилось в таблицу " + remd + " с fremd_status - 1");
                Assertions.assertEquals(
                        emd_id, "Проверка 1034", "СМС не добавилось в таблицу " + remd + " с emd_id - Проверка 1034");
            }
        }
    }

    @Issue(value = "TEL-1034")
    @Link(name = "ТМС-1372", url = "https://team-1okm.testit.software/projects/5/tests/1372?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Уведомления при передаче ПДФ справок в КРЭМД Id=101")
    @Description("Отправляем смс, проверяем, что добавилось в таблицу vimis.certificate_remd_sent_result. Используем метод /kremd/collback, после проверяем, что пришло уведомление, в таблице vimis.certificate_remd_sent_result проверяем поля error, fremd_status, emd_id")
    public void Access_1034Id_101() throws IOException, SQLException, InterruptedException {
        Access1034Method("SMS/SMS3.xml", "101", 99, 1, 1, 9, 18, 1, 57, 21, "vimis.certificate_remd_sent_result");
    }

    @Test
    @DisplayName("Уведомления при передаче ПДФ справок в КРЭМД Id=102")
    public void Access_1034Id_102() throws IOException, SQLException, InterruptedException {
        Access1034Method("SMS/SMS3.xml", "102", 99, 1, 1, 9, 18, 1, 57, 21, "vimis.certificate_remd_sent_result");
    }

    @Test
    @DisplayName("Уведомления при передаче ПДФ справок в КРЭМД Id=103")
    public void Access_1034Id_103() throws IOException, SQLException, InterruptedException {
        Access1034Method("SMS/SMS3.xml", "103", 99, 1, 1, 9, 18, 1, 57, 21, "vimis.certificate_remd_sent_result");
    }

    @Test
    @DisplayName("Уведомления при передаче ПДФ справок в КРЭМД Id=104")
    public void Access_1034Id_104() throws IOException, SQLException, InterruptedException {
        Access1034Method("SMS/SMS3.xml", "104", 99, 1, 1, 9, 18, 1, 57, 21, "vimis.certificate_remd_sent_result");
    }

    @Test
    @DisplayName("Уведомления при передаче ПДФ справок в КРЭМД Id=105")
    public void Access_1034Id_105() throws IOException, SQLException, InterruptedException {
        Access1034Method("SMS/SMS3.xml", "105", 99, 1, 1, 9, 18, 1, 57, 21, "vimis.certificate_remd_sent_result");
    }
}
