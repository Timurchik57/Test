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
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Оповещение о смене статуса СЭМД для статуса = 6/7")
public class Access_1473Test extends BaseAPI {
    Document_667 doc;
    SQL sql;
    String Value;
    String URLCollback;

    public void Access_1473REMDVmclMethod(
            String File, String DocType, Integer vmcl, Integer number, String sms, String log, String remd,
            Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1,
            Integer position1, Integer speciality1, String StatusRemd
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        doc = new Document_667();
        UUID uuidId = UUID.randomUUID();
        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<SOAP-ENV:Envelope\n" +
                "\txmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "\t<SOAP-ENV:Header>\n" +
                "\t\t<To\n" +
                "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-to\"\n" +
                "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">https://ips-test.rosminzdrav.ru/b6789b2b47581\n" +
                "\t\t</To>\n" +
                "\t\t<RelatesTo\n" +
                "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\">" + uuidId + "\n" +
                "\t\t</RelatesTo>\n" +
                "\t\t<Action\n" +
                "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-action\"\n" +
                "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">sendResult\n" +
                "\t\t</Action>\n" +
                "\t\t<MessageID\n" +
                "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-messageid\"\n" +
                "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">9dd49f16-3aa2-45e7-b786-1333f341fbd2\n" +
                "\t\t</MessageID>\n" +
                "\t\t<ReplyTo\n" +
                "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-replyto\"\n" +
                "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "\t\t\t<Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</Address>\n" +
                "\t\t</ReplyTo>\n" +
                "\t\t<transportHeader\n" +
                "\t\t\txmlns=\"http://egisz.rosminzdrav.ru\">\n" +
                "\t\t\t<authInfo>\n" +
                "\t\t\t\t<clientEntityId>e5daa377-b705-64e2-52c4-3ddebea35d7b</clientEntityId>\n" +
                "\t\t\t</authInfo>\n" +
                "\t\t</transportHeader>\n" +
                "\t\t<wsse:Security\n" +
                "\t\t\txmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
                "\t\t\t<wsse:BinarySecurityToken wsu:Id=\"id-x509\" EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\"\n" +
                "\t\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">MIIDazCCAlOgAwIBAgIUEch0BCakzmHyN1a7EoVrgAlaA5kwDQYJKoZIhvcNAQELBQAwRTELMAkGA1UEBhMCQVUxEzARBgNVBAgMClNvbWUtU3RhdGUxITAfBgNVBAoMGEludGVybmV0IFdpZGdpdHMgUHR5IEx0ZDAeFw0yMDA0MTAxODQ4NTlaFw0yMTA0MTAxODQ4NTlaMEUxCzAJBgNVBAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBXaWRnaXRzIFB0eSBMdGQwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCrJmNObcyjysnS5dV/Hvp6RzHZyoN+n4fxX0QhiMMU4tBPh1zjaMDSe8Cw4i+3B91P33I2vyI0vfvX8EqWumDiDdV0eWc9h+3EeaG9uozqfpMjY/gnyPntwReTqsWZjPy2yyZgRN+IHdvpCfuC+tv8G0H9/UK31RbIUI7P1zeMCZ68v9LVgbG7r5oa0QZId4p/LQ4xPtjvuWiObEwFAUfW6b/vbDfGXSZ4Tq63UwyD9DzXZcs3f7w5EjHmEU22LU8pEzRqjsv/VraQd7T2OrYKbnOP4EAzZIQwuk3OcIVJFtNBjj0+F5gJ+BOmCY0UYcH4oaGjVA5ez147E46oM5AhAgMBAAGjUzBRMB0GA1UdDgQWBBTiuUZkT5Q9j0wfM566jk5aHldSYTAfBgNVHSMEGDAWgBTiuUZkT5Q9j0wfM566jk5aHldSYTAPBgNVHRMBAf8EBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQAM5pQpQMcuE2unZTMLzlfXW5xY3J8mfdgHVZGnBC6fLKfwmlpyoleqnTuNik8X/REe2hN2heO/irHllBAGt+TUB8fdRJpaGbdi6H5FuzrCoG7rozJVZp70B3ij9XuBqkjBmZvac4QcWyXmBYyuWG0TqxKpRwwtgZNr5ZApk2PB79sTlaI7+sLyp/G3ntA4wwQ/4785Sx/8F90/Mrg5fwVi5sQz+Id6ykRV5IZnrE/Z47KdcJ/U4rePGHV01XECn1uR2X98e3npPzR1MX4hoo/f4iZuJ1KWYZ66u1z0edW1E8BFUAsuEe5Z5dytncpodvRsIaICCcXE6do2dYQqpJi4\n" +
                "\t\t\t</wsse:BinarySecurityToken>\n" +
                "\t\t\t<Signature\n" +
                "\t\t\t\txmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                "\t\t\t\t<SignedInfo>\n" +
                "\t\t\t\t\t<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t<SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\" />\n" +
                "\t\t\t\t\t<Reference URI=\"#body\">\n" +
                "\t\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
                "\t\t\t\t\t\t<DigestValue>YEOGiS0cujt2sVo0moV4VS7mBVuS0Pl8sQeaHkDrqa0=</DigestValue>\n" +
                "\t\t\t\t\t</Reference>\n" +
                "\t\t\t\t\t<Reference URI=\"#id-messageid\">\n" +
                "\t\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
                "\t\t\t\t\t\t<DigestValue>j8myuFMZfVE+B7gUZ0jf51ymcwKUgV2qwVN0BAw+INA=</DigestValue>\n" +
                "\t\t\t\t\t</Reference>\n" +
                "\t\t\t\t\t<Reference URI=\"#id-replyto\">\n" +
                "\t\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
                "\t\t\t\t\t\t<DigestValue>tpA2zvq1rD20AP46VFS6MUOE9Noe/s3zfGq3XeC9vPc=</DigestValue>\n" +
                "\t\t\t\t\t</Reference>\n" +
                "\t\t\t\t\t<Reference URI=\"#id-to\">\n" +
                "\t\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
                "\t\t\t\t\t\t<DigestValue>5qMzs7IC+8NoRSv4m3hf+XXJW3Wy7BkaQwFsPpL9744=</DigestValue>\n" +
                "\t\t\t\t\t</Reference>\n" +
                "\t\t\t\t\t<Reference URI=\"#id-action\">\n" +
                "\t\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
                "\t\t\t\t\t\t<DigestValue>4x0l+FI91mxd0FRM/0pitJuXGT+JtVgHi1xmLKuJDzk=</DigestValue>\n" +
                "\t\t\t\t\t</Reference>\n" +
                "\t\t\t\t</SignedInfo>\n" +
                "\t\t\t\t<SignatureValue>YZ2C7Q1DbmTgm+9BSd73WEThN/er2wd/TQY/qnlKG3+MuztFAJIUhv0htOJte9imifNMtrqE60eJ4Mq7dq0p0tda0cEpMYG6YyIUMkEwRwkrYejiwyGtORoeK9GwRCV1JiUwqP2tucRDbqrHG8cg6/xCGSaMU4rXTiKIj5x+aKvlsVhPpuSNMs/tf8VJ09XtJgd5kyTlxwPxjxH1PIFUanLAJwSEv/4OoWzmqxl1KdEA0ihgdK8x/V5MBBPvdiSkTYdp6LmsdC1005VnFwG29VwW+0X1wxAvVE0ac6kyVQjYGgJWhYR+VsFHdYBV+drY0+rJWM6q0ehgymiLqoT5yw==</SignatureValue>\n" +
                "\t\t\t\t<KeyInfo>\n" +
                "\t\t\t\t\t<wsse:SecurityTokenReference>\n" +
                "\t\t\t\t\t\t<wsse:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#id-x509\" />\n" +
                "\t\t\t\t\t</wsse:SecurityTokenReference>\n" +
                "\t\t\t\t</KeyInfo>\n" +
                "\t\t\t</Signature>\n" +
                "\t\t</wsse:Security>\n" +
                "\t</SOAP-ENV:Header>\n" +
                "\t<SOAP-ENV:Body wsu:Id=\"body\"\n" +
                "\t\txmlns:p2=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"\n" +
                "\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "\t\t<sendResult\n" +
                "\t\t\txmlns=\"http://callback.mis.vimis.rosminzdrav.ru/\">\n" +
                "\t\t\t<msg_id\n" +
                "\t\t\t\txmlns=\"\">" + uuidId + "\n" +
                "\t\t\t</msg_id>\n" +
                "\t\t\t<status\n" +
                "\t\t\t\txmlns=\"\">1</status>\n" +
                "\t\t\t<description\n" +
                "\t\t\t\txmlns=\"\">Проверяем отправку уведомлений</description>\n" +
                "\t\t</sendResult>\n" +
                "\t</SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
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
                    File, DocType, vmcl, number, true, docTypeVersion, Role, position, speciality, Role1, position1,
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
                sql.UpdateConnection("update " + sms + " set request_id = '" + uuidId + "' where id = " + Value + ";");
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
                        .body(body)
                        .post(URLCollback)
                        .then()
                        .statusCode(200);
                Thread.sleep(2000);
                System.out.println("В таблице " + remd + " Создаём запись, чтобы получить статус от крэмд");
                sql.UpdateConnection(
                        "insert into " + remd + "(sms_id, local_uid, created_datetime) values ('" + Value + "', '" + xml.uuid + "', '" + Date + " 00:00:00.888 +0500');");
            } else {
                sql.StartConnection(
                        "SELECT * FROM " + sms + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    Value = sql.resultSet.getString("id");
                    Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + sms + "");
                    System.out.println(Value);
                }
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
                                                  .log().all()
                                                  .header("Authorization", "Bearer " + Token)
                                                  .contentType(ContentType.JSON)
                                                  .when()
                                                  .body("{\n" +
                                                                "  \"id\": \"" + xml.uuid + "\",\n" +
                                                                "  \"emdrId\": \"Проверка Уведомлений по РЭМД (1473)\",\n" +
                                                                "  \"status\": \"" + StatusRemd + "\",\n" +
                                                                "  \"registrationDateTime\": \"2022-08-30\",\n" +
                                                                "  \"errors\": [\n" +
                                                                "    {\n" +
                                                                "      \"code\": \"string\",\n" +
                                                                "      \"message\": \"Проверка Уведомлений по РЭМД (1473)\"\n" +
                                                                "    }\n" +
                                                                "  ]\n" +
                                                                "}")
                                                  .post(URLCollback)
                                                  .prettyPeek()
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
            if (vmcl != 99 && StatusRemd == "sent") {
                Assertions.assertTrue(
                        text.contains("\"StatusREMD\":6"),
                        "Оповещение для vmcl = " + vmcl + " не добавилось"
                );
                Assertions.assertTrue(
                        text.contains("\"ResultDescription\":\"Документ передан на федеральный уровень ФРЭМД\""),
                        "Оповещение для vmcl = " + vmcl + " не добавилось"
                );
            }
            if (vmcl != 99 && StatusRemd == "resending") {
                Assertions.assertTrue(
                        text.contains("\"StatusREMD\":7"),
                        "Оповещение для vmcl = " + vmcl + " не добавилось"
                );
                Assertions.assertTrue(
                        text.contains(
                                "\"ResultDescription\":\"Документ переотправлен на федеральный уровень РЭМД (повторная отправка в связи с ошибками вызванные сбоем в федеральном сервисе)\""),
                        "Оповещение для vmcl = " + vmcl + " не добавилось"
                );
            }
            if (vmcl == 99 && StatusRemd == "resending") {
                Assertions.assertTrue(
                        text.contains(
                                "\"ResultDescription\":\"Документ переотправлен на федеральный уровень РЭМД (повторная отправка в связи с ошибками вызванные сбоем в федеральном сервисе)\""),
                        "Оповещение для vmcl = " + vmcl + " не добавилось"
                );
                Assertions.assertTrue(
                        text.contains("\"StatusREMD\":7"),
                        "Оповещение для vmcl = " + vmcl + " не добавилось"
                );
            }
            if (vmcl == 99 && StatusRemd == "sent") {
                Assertions.assertTrue(
                        text.contains("\"ResultDescription\":\"Документ передан на федеральный уровень ФРЭМД\""),
                        "Оповещение для vmcl = " + vmcl + " не добавилось"
                );
                Assertions.assertTrue(
                        text.contains("\"StatusREMD\":6"),
                        "Оповещение для vmcl = " + vmcl + " не добавилось"
                );
            }
            if (vmcl != 99) {
                sql.StartConnection("SELECT * FROM " + log + "  where sms_id = " + Value + ";");
                while (sql.resultSet.next()) {
                    String Value1 = sql.resultSet.getString("sms_id");
                    String Value2 = sql.resultSet.getString("description");
                    String Value3 = sql.resultSet.getString("msg_id");
                    String status = sql.resultSet.getString("status");
                    Assertions.assertEquals(Value1, Value, "СМС не добавилось в таблицу " + log + "");
                    Assertions.assertEquals(
                            Value2, "Проверяем отправку уведомлений",
                            "СМС не добавилось в таблицу " + log + " с сообщением - Проверяем отправку по заявке 1473"
                    );
                    Assertions.assertEquals(
                            Value3, "" + uuidId + "",
                            "СМС не добавилось в таблицу " + log + " с msg_id - " + uuidId + ""
                    );
                    Assertions.assertEquals(status, "1", "СМС не добавилось в таблицу " + sms + " с status - 1");
                }
                sql.StartConnection("SELECT * FROM " + remd + "  where sms_id = " + Value + ";");
                while (sql.resultSet.next()) {
                    String status = sql.resultSet.getString("status");
                    if (StatusRemd == "Sent") {
                        Assertions.assertEquals(status, "sent", "СМС не добавилось в таблицу " + remd + "");
                    }
                    if (StatusRemd == "resending") {
                        Assertions.assertEquals(status, "resending", "СМС не добавилось в таблицу " + remd + "");
                    }

                }

            } else {
                sql.StartConnection("SELECT * FROM " + sms + "  where id = " + Value + ";");
                while (sql.resultSet.next()) {
                    String local_uid = sql.resultSet.getString("local_uid");
                    String errors = sql.resultSet.getString("errors");
                    String fremd_status = sql.resultSet.getString("fremd_status");
                    String status = sql.resultSet.getString("status");
                    Assertions.assertEquals(local_uid, "" + xml.uuid + "", "СМС не добавилось в таблицу " + sms + "");
                    Assertions.assertNull(
                            fremd_status, "СМС не добавилось в таблицу " + sms + " с fremd_status - null");
                    Assertions.assertNotNull(errors, "СМС не добавилось в таблицу " + sms + " с errors - null");
                    if (StatusRemd == "sent") {
                        Assertions.assertEquals(
                                status, "sent", "СМС не добавилось в таблицу " + sms + " с fremd_status - sent");
                    }
                    if (StatusRemd == "resending") {
                        Assertions.assertEquals(
                                status, "resending",
                                "СМС не добавилось в таблицу " + sms + " с fremd_status - resending"
                        );
                    }
                }
            }
        }
    }

    @Issue(value = "TEL-1473")
    @Issue(value = "TEL-1590")
    @Link(name = "ТМС-1552", url = "https://team-1okm.testit.software/projects/5/tests/1552?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Оповещение о смене статуса СЭМД для статуса = 6/7, vmcl=1")
    @Description("Отправить СЭМД, сменить статус, проверить, что приходит оповещение")
    public void Access_1473Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 1, 1, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result", 3, 1, 9,
                18, 1, 57, 21, "sent"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(3000);
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 1, 2, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result", 3, 1, 9,
                18, 1, 57, 21, "resending"
        );

    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД для статуса = 6/7, vmcl=2")
    public void Access_1473Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 2, 1, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 1, 9, 18, 1, 57, 21, "sent"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 2, 2, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 1, 9, 18, 1, 57, 21, "resending"
        );
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД для статуса = 6/7, vmcl=3")
    public void Access_1473Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 3, 1, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result", 2, 1,
                9, 18, 1, 57, 21, "sent"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 3, 2, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result", 2, 1,
                9, 18, 1, 57, 21, "resending"
        );
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД для статуса = 6/7, vmcl=4")
    public void Access_1473Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 4, 1, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 1, 9, 18,
                1, 57, 21, "sent"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 4, 2, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 1, 9, 18,
                1, 57, 21, "resending"
        );
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД для статуса = 6/7, vmcl=99")
    public void Access_1473Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 99, 1, "vimis.remd_sent_result", "", "", 2, 1, 9, 18, 1, 57, 21, "sent");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 99, 2, "vimis.remd_sent_result", "", "", 2, 1, 9, 18, 1, 57, 21, "resending");
    }
}
