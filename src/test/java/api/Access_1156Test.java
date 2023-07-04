package api;

import UI.SQL;
import UI.TestListener;
import api.Access_667.Document_667;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
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
@Feature("Оповещения по свидетельствам о смерти")
public class Access_1156Test extends BaseAPI {
    Document_667 doc;
    public String body;
    public UUID Uuid;
    String Value;
    String URLCollback;
    String TransferId;

    public void Access_1156Method(
            String File, String DocType, Integer vmcl, String sms, String logs, String remd, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String statusVimis, String statusRemd
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        doc = new Document_667();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            Uuid = UUID.randomUUID();
            body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<SOAP-ENV:Envelope\n" +
                    "\txmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "\t<SOAP-ENV:Header>\n" +
                    "\t\t<To\n" +
                    "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-to\"\n" +
                    "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">https://ips-test.rosminzdrav.ru/b6789b2b47581\n" +
                    "\t\t</To>\n" +
                    "\t\t<RelatesTo\n" +
                    "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\">" + Uuid + "\n" +
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
                    "\t\t\t\txmlns=\"\">" + Uuid + "\n" +
                    "\t\t\t</msg_id>\n" +
                    "\t\t\t<status\n" +
                    "\t\t\t\txmlns=\"\">" + statusVimis + "</status>\n" +
                    "\t\t\t<description\n" +
                    "\t\t\t\txmlns=\"\">Проверяем отправку уведомлений 1156, 1525</description>\n" +
                    "\t\t</sendResult>\n" +
                    "\t</SOAP-ENV:Body>\n" +
                    "</SOAP-ENV:Envelope>";
            System.out.println("Переходим на сайт для перехвата сообщений");
            if (KingNumber == 4) {
                driver.get("http://192.168.2.21:34329");
            }
            if (KingNumber == 1 | KingNumber == 2) {
                driver.get("http://192.168.2.126:10227");
            }
            Thread.sleep(1500);
            System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
            xml.ReplacementWordInFile(
                    File, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(xml.body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            TransferId = response.get("result[0].transferId");
            if (DocType != "13" & DocType != "14" & DocType != "21") {
                Thread.sleep(3000);
            }
            if (vmcl != 99) {
                System.out.println(
                        "В таблице " + sms + " устанавливаем нужный uuid, который будет в запросе на смену статуса");
                sql.UpdateConnection(
                        "update " + sms + " set request_id = '" + Uuid + "' where local_uid = '" + xml.uuid + "';");
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
                        .contentType(ContentType.JSON)
                        .when()
                        .body(body)
                        .post(URLCollback)
                        .then()
                        .statusCode(200);
                sql.StartConnection(
                        "SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    Value = sql.resultSet.getString("id");
                    System.out.println(Value);
                    Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + sms + "");
                }
                if (DocType == "15" | DocType == "33") {
                    System.out.println("Устанавливаем status = " + statusVimis + " в " + remd + "");
                    sql.UpdateConnection(
                            "update " + remd + " set local_uid = '" + xml.uuid + "', status = '" + statusRemd + "' where sms_id = '" + Value + "';");
                }
            }
            if (vmcl == 99) {
                sql.StartConnection(
                        "SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    Value = sql.resultSet.getString("id");
                    Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + remd + "");
                    System.out.println(Value);
                }
            }
            if (DocType == "15" | DocType == "32" | DocType == "33" | DocType == "34" | DocType == "36") {
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
                                                                    "  \"emdrId\": \"Проверка Уведомлений по РЭМД (1156, 1525)\",\n" +
                                                                    "  \"status\": \"" + statusRemd + "\",\n" +
                                                                    "  \"registrationDateTime\": \"2022-08-30\",\n" +
                                                                    "  \"errors\": [\n" +
                                                                    "    {\n" +
                                                                    "      \"code\": \"string\",\n" +
                                                                    "      \"message\": \"Проверка Уведомлений по РЭМД (1156, 1525)\"\n" +
                                                                    "    }\n" +
                                                                    "  ]\n" +
                                                                    "}")
                                                      .post(URLCollback)
                                                      .then()
                                                      .extract().jsonPath().getString("id"));
                Assertions.assertEquals(value, "" + xml.uuid + "", "Не сменился статус");
                Thread.sleep(1000);
            }
            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
            String text = driver.findElement(By.xpath("//div[@class='text-center']/div[1]/span")).getText();
            System.out.println(text);
            System.out.println(xml.uuid);
            if (statusVimis == "1" & statusRemd == "success") {
                System.out.println("Проверка для типа опевещинй 9");
                if (vmcl != 99) {
                    Assertions.assertTrue(
                            text.contains("\"TransferId\":\"" + TransferId + "\""),
                            "TransferId для vmcl = " + vmcl + " отсутствует"
                    );
                    Assertions.assertTrue(
                            text.contains("\"RequestId\":\"" + Uuid + "\""),
                            "RequestId для vmcl = " + vmcl + " отсутствует"
                    );
                    Assertions.assertTrue(
                            text.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                            "PatientGuid для vmcl = " + vmcl + " отсутствует"
                    );
                    if (DocType != "21") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"SMSV13\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    } else {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"SMSV19\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                } else {
                    Assertions.assertTrue(
                            text.contains("\"TransferId\":\"" + TransferId + "\""),
                            "TransferId для vmcl = " + vmcl + " отсутствует"
                    );
                    Assertions.assertTrue(
                            text.contains("\"RequestId\":null"),
                            "RequestId для vmcl = " + vmcl + " отсутствует"
                    );
                    Assertions.assertTrue(
                            text.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                            "PatientGuid для vmcl = " + vmcl + " отсутствует"
                    );
                    if (DocType == "15") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"58\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                    if (DocType == "32") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"76\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                    if (DocType == "33") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"113\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                    if (DocType == "34") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"114\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                    if (DocType == "36") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"118\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                }
            } else {
                System.out.println("Проверка для типа опевещинй 2");
                if (vmcl != 99) {
                    Assertions.assertTrue(
                            text.contains("\"TransferId\":\"" + TransferId + "\""),
                            "TransferId для vmcl = " + vmcl + " отсутствует"
                    );
                    Assertions.assertTrue(
                            text.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                            "LocalUid для vmcl = " + vmcl + " отсутствует"
                    );
                    Assertions.assertTrue(
                            text.contains("\"RequestId\":\"" + Uuid + "\""),
                            "RequestId для vmcl = " + vmcl + " отсутствует"
                    );
                    Assertions.assertFalse(
                            text.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                            "PatientGuid для vmcl = " + vmcl + " присутствует"
                    );
                    if (DocType != "21") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"SMSV13\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    } else {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"SMSV19\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                } else {
                    Assertions.assertTrue(
                            text.contains("\"EmdId\":\"Проверка Уведомлений по РЭМД (1156, 1525)\""),
                            "TransferId для vmcl = " + vmcl + " отсутствует"
                    );
                    Assertions.assertTrue(
                            text.contains("\"RequestId\":null"),
                            "RequestId для vmcl = " + vmcl + " отсутствует"
                    );
                    Assertions.assertFalse(
                            text.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                            "PatientGuid для vmcl = " + vmcl + " отсутствует"
                    );
                    if (DocType == "15") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"58\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                    if (DocType == "32") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"76\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                    if (DocType == "33") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"113\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                    if (DocType == "34") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"114\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                    if (DocType == "36") {
                        Assertions.assertTrue(
                                text.contains("\"DocType\":\"118\""),
                                "DocType для vmcl = " + vmcl + " отсутствует"
                        );
                    }
                }
            }
            if (vmcl != 99) {
                sql.StartConnection("SELECT * FROM " + logs + "  where sms_id = " + Value + ";");
                while (sql.resultSet.next()) {
                    String Value1 = sql.resultSet.getString("sms_id");
                    String Value2 = sql.resultSet.getString("description");
                    String Value3 = sql.resultSet.getString("msg_id");
                    Assertions.assertEquals(Value1, Value, "СМС не добавилось в таблицу " + logs + "");
                    Assertions.assertEquals(
                            Value2, "Проверяем отправку уведомлений 1156, 1525",
                            "СМС не добавилось в таблицу " + logs + " с сообщением - Проверяем отправку уведомлений 1156, 1525"
                    );
                    Assertions.assertEquals(
                            Value3, "" + Uuid + "", "СМС не добавилось в таблицу " + logs + " с msg_id - " + Uuid + "");
                }
            } else {
                sql.StartConnection("SELECT * FROM " + remd + "  where id = " + Value + ";");
                while (sql.resultSet.next()) {
                    String local_uid = sql.resultSet.getString("local_uid");
                    String errors = sql.resultSet.getString("errors");
                    String fremd_status = sql.resultSet.getString("fremd_status");
                    String emd_id = sql.resultSet.getString("emd_id");
                    Assertions.assertEquals(local_uid, "" + xml.uuid + "", "СМС не добавилось в таблицу " + remd + "");
                    Assertions.assertEquals(
                            errors,
                            "[{\"Code\": \"string\", \"Message\": \"Проверка Уведомлений по РЭМД (1156, 1525)\"}]",
                            "СМС не добавилось в таблицу " + remd + " с сообщением - Проверка 1156"
                    );
                    Assertions.assertEquals(
                            fremd_status, "" + statusVimis + "",
                            "СМС не добавилось в таблицу " + remd + " с fremd_status - " + statusVimis + ""
                    );
                    Assertions.assertEquals(
                            emd_id, "Проверка Уведомлений по РЭМД (1156, 1525)",
                            "СМС не добавилось в таблицу " + remd + " с emd_id - Проверка 1156"
                    );
                }
            }
        }
    }

    @Issue(value = "TEL-1156")
    @Issue(value = "TEL-1525")
    @Link(name = "ТМС-1185", url = "https://team-1okm.testit.software/projects/5/tests/1185?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Link(name = "ТМС-1738", url = "https://team-1okm.testit.software/projects/5/tests/1738?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 13 vmcl = 1")
    @Description("Отправить СЭМД, сменить статус, проверить, что приходит оповещение")
    public void Access_1156_13_1() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/SMS13-id=13.xml", "13", 1, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result", 3, 6,
                4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 13 vmcl = 2")
    public void Access_1156_13_2() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/SMS13-id=13.xml", "13", 2, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 6, 4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 13 vmcl = 3")
    public void Access_1156_13_3() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/SMS13-id=13.xml", "13", 3, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result",
                2, 6, 4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 13 vmcl = 4")
    public void Access_1156_13_4() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/SMS13-id=13.xml", "13", 4, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 6, 4,
                18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 14 vmcl = 1")
    public void Access_1156_14_1() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/SMS13-id=14.xml", "14", 1, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result", 3, 6,
                4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 14 vmcl = 2")
    public void Access_1156_14_2() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/SMS13-id=14.xml", "14", 2, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 6, 4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 14 vmcl = 3")
    public void Access_1156_14_3() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/SMS13-id=14.xml", "14", 3, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result",
                2, 6, 4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 14 vmcl = 4")
    public void Access_1156_14_4() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/SMS13-id=14.xml", "14", 4, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 6, 4,
                18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 1")
    public void Access_1156_15_1() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 1, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result", 3,
                6, 4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 2")
    public void Access_1156_15_2() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 2, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 6, 4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 3")
    public void Access_1156_15_3() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 3, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result", 2, 6, 4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 4")
    public void Access_1156_15_4() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 4, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 6,
                4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 99")
    public void Access_1156_15_99() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 21 vmcl = 3")
    public void Access_1156_21_3() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/SMS19-id=21.xml", "21", 3, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result",
                3, 6, 4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 32 vmcl = 99")
    public void Access_1156_32_99() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=32-vmcl=99.xml", "32", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 33 vmcl = 1")
    public void Access_1156_33_1() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=33-vmcl=1.xml", "33", 1, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result", 3, 6,
                4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 33 vmcl = 2")
    public void Access_1156_33_2() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=33-vmcl=1.xml", "33", 2, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 6, 4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 33 vmcl = 3")
    public void Access_1156_33_3() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=33-vmcl=1.xml", "33", 3, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result",
                2, 6, 4, 18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 33 vmcl = 4")
    public void Access_1156_33_4() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=33-vmcl=1.xml", "33", 4, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 6, 4,
                18, 1, 57, 21, "1", "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 33 vmcl = 99")
    public void Access_1156_33_99() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=33-vmcl=1.xml", "33", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 34 vmcl = 99")
    public void Access_1156_34_99() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/SMSV19-id=34.xml", "34", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 36 vmcl = 99")
    public void Access_1156_36_99() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=36-vmcl=99.xml", "36", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "success"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 36 vmcl = 99")
    public void Access_1156_36_99_() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=36-vmcl=99.xml", "36", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "0",
                "error"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 13 vmcl = 2")
    public void Access_1156_13_2_() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/SMS13-id=13.xml", "13", 2, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 6, 4, 18, 1, 57, 21, "0", "error"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 4")
    public void Access_1156_15_4_() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 4, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 6,
                4, 18, 1, 57, 21, "0", "error"
        );

    }

    @Test
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 99 statusRemd = error")
    public void Access_1156_15_99_() throws IOException, SQLException, InterruptedException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "0",
                "error"
        );

    }
}
