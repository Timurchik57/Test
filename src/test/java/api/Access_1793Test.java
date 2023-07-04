package api;

import UI.TestListener;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
public class Access_1793Test extends BaseAPI {

    public String body;
    public String Body;
    public String content;
    public String encodedString;
    public String Time;
    public String URLKremd;
    public String URLCollback;
    public UUID Uuid;

    @Step("Отправляем смс = {1} с vmcl = {2}. Меняем значение effectiveTime ")
    public void Access_1786Method(String FileName, String DocType, Integer vmcl, Integer triggerPoint, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, Boolean time, String sms, String log, String remd, String info) throws IOException, InterruptedException, SQLException {

        Uuid = UUID.randomUUID();
        Body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
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
                "\t\t\t\txmlns=\"\">1</status>\n" +
                "\t\t\t<description\n" +
                "\t\t\t\txmlns=\"\">Проверяем 1793</description>\n" +
                "\t\t</sendResult>\n" +
                "\t</SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";

        Token = new String(Files.readAllBytes(Paths.get("AuthorizationToken.txt")));

        XML.Type = FileName;

        Thread.sleep(1000);
        ID = (int) Math.floor(timestamp.getTime() / 1000);
        SetID = (int) Math.floor(timestamp.getTime() / 1000) + 1;
        VN = (int) Math.floor(timestamp.getTime() / 1000) + 2;
        InputProp("src/test/resources/my.properties", "value_ID", String.valueOf(ID));
        InputProp("src/test/resources/my.properties", "value_SetID", String.valueOf(SetID));
        InputProp("src/test/resources/my.properties", "value_VN", String.valueOf(VN));

        xml.ReplaceWord(FileName, "${iD}", String.valueOf(ID));
        xml.ReplaceWord(FileName, "${setId}", String.valueOf(SetID));
        xml.ReplaceWord(FileName, "${versionNumber}", String.valueOf(VN));
        if (time == true) {
            xml.ReplaceWord(FileName, "<effectiveTime value=\"202101251600+0300\"/>",
                            "<effectiveTime nullFlavor=\"NA\"/>");
        }
        xml.ReplaceWord(FileName, "${depart}", Departmen);
        xml.ReplaceWord(FileName, "${mo}", MO);
        xml.ReplaceWord(FileName, "${guid}", PatientGuid);
        xml.ReplaceWord(FileName, "${namemo}", NameMO);
        xml.ReplaceWord(FileName, "${snils}", Snils);

        content = new String(Files.readAllBytes(Paths.get(FileName)));
        encodedString = Base64.getEncoder().encodeToString(content.getBytes());

        /** Подсчёт чек Суммы для XML */
        String sum = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                              "  \"base64String\": \"" + encodedString + "\"\n" +
                              "}")
                .when()
                .post("http://192.168.2.126:10117/CheckSum/CalculateCheckSum")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("checkSum");
        CheckSum = sum;

        /** Получение подписи */
        String Signat = given()
                .contentType(ContentType.JSON)
                .body("{\"requestId\": \"2\",\n" +
                              "            \"serialNumber\": \"008B55DE3674250EE3DE4983AF7C4D455A\",\n" +
                              "            \"message\": \"" + encodedString + "\",\n" +
                              "            \"isBase64\": true}")
                .when()
                .post("http://192.168.2.126:8082/sign/message")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("result.signedMessage");
        Signatures = Signat;

        /** Подсчёт чек Суммы для Подписи */
        String sum1 = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                              "  \"base64String\": \"" + Signatures + "\"\n" +
                              "}")
                .when()
                .post("http://192.168.2.126:10117/CheckSum/CalculateCheckSum")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("checkSum");
        CheckSumSign = sum1;
        xml.uuid = UUID.randomUUID();
        System.out.println(xml.uuid);

        if (vmcl != 99) {
            body = "{\n" +
                    "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                    "    \"DocType\":\"" + DocType + "\",\n" +
                    "    \"DocContent\":\n" +
                    "    {\n" +
                    "        \"Document\":\"" + encodedString + "\",\n" +
                    "        \"CheckSum\":" + CheckSum + "\n" +
                    "        },\n" +
                    "        \"LocalUid\":\"" + xml.uuid + "\",\n" +
                    "        \"Payment\":1,\n" +
                    "        \"ReasonForAbsenceIdcase\":\n" +
                    "        {\n" +
                    "            \"CodeSystemVersion\":\"1.1\",\n" +
                    "            \"Code\":10,\n" +
                    "            \"CodeSystem\":\"1.2.643.5.1.13.13.99.2.286\"\n" +
                    "        },\n" +
                    "            \"VMCL\":\n" +
                    "            [\n" +
                    "                {\n" +
                    "                    \"vmcl\": " + vmcl + ",\n" +
                    "                    \"triggerPoint\": " + triggerPoint + ",\n" +
                    "                    \"docTypeVersion\": " + docTypeVersion + "\n" +
                    "                    \n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"OrgSignature\":\n" +
                    "            {\n" +
                    "                \"Data\":\"" + Signatures + "\",\n" +
                    "                \"CheckSum\":" + CheckSumSign + "\n" +
                    "                },\n" +
                    "                \"PersonalSignatures\":\n" +
                    "                [\n" +
                    "                    {\n" +
                    "                        \"Signer\":\n" +
                    "                        {\n" +
                    "                            \"LocalId\":\"0001510378\",\n" +
                    "                            \"Role\":{\"$\":\"" + Role + "\",\"@version\":\"2.4\"},\n" +
                    "                            \"LastName\":\"Коситченков\",\n" +
                    "                            \"FirstName\":\"Андрей\",\n" +
                    "                            \"MiddleName\":\"Александрович\",\n" +
                    "                            \"Snils\":\"18259202174\",\n" +
                    "                            \"Position\":{\"$\":\"" + position + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                            \"Speciality\":{\"$\":" + speciality + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                            \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                        },\n" +
                    "                            \"Signature\":\n" +
                    "                            {\n" +
                    "                                \"Data\":\"" + Signatures + "\",\n" +
                    "                                  \"CheckSum\":" + CheckSumSign + "\n" +
                    "                            },\n" +
                    "                            \"Description\":\"Подпись сотрудника\"\n" +
                    "                            },\n" +
                    "                            {\n" +
                    "                                \"Signer\":\n" +
                    "                                {\n" +
                    "                                    \"LocalId\":\"0003948083\",\n" +
                    "                                    \"Role\":{\"$\":\"" + Role1 + "\",\"@version\":\"2.4\"},\n" +
                    "                                    \"LastName\":\"Стрекнев\",\n" +
                    "                                    \"FirstName\":\"Денис\",\n" +
                    "                                    \"MiddleName\":\"Юрьевич\",\n" +
                    "                                    \"Snils\":\"18287265004\",\n" +
                    "                                    \"Position\":{\"$\":\"" + position1 + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                                    \"Speciality\":{\"$\":" + speciality1 + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                                    \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                                },\n" +
                    "                                    \"Signature\":\n" +
                    "                                    {\n" +
                    "                                        \"Data\":\"" + Signatures + "\",\n" +
                    "                                       \"CheckSum\":" + CheckSumSign + "\n" +
                    "                                        },\n" +
                    "                                        \"Description\":\"Подпись сотрудника\"\n" +
                    "                                    }\n" +
                    "        ]\n" +
                    "    }";
        } else {
            body = "{\n" +
                    "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                    "    \"DocType\":\"" + DocType + "\",\n" +
                    "    \"DocContent\":\n" +
                    "    {\n" +
                    "        \"Document\":\"" + encodedString + "\",\n" +
                    "        \"CheckSum\":" + CheckSum + "\n" +
                    "        },\n" +
                    "        \"LocalUid\":\"" + xml.uuid + "\",\n" +
                    "        \"Payment\":1,\n" +
                    "        \"ReasonForAbsenceIdcase\":\n" +
                    "        {\n" +
                    "            \"CodeSystemVersion\":\"1.1\",\n" +
                    "            \"Code\":10,\n" +
                    "            \"CodeSystem\":\"1.2.643.5.1.13.13.99.2.286\"\n" +
                    "        },\n" +
                    "            \"VMCL\":\n" +
                    "            [\n" +
                    "                {\n" +
                    "                    \"vmcl\": " + vmcl + "\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"OrgSignature\":\n" +
                    "            {\n" +
                    "                \"Data\":\"" + Signatures + "\",\n" +
                    "                \"CheckSum\":" + CheckSumSign + "\n" +
                    "                },\n" +
                    "                \"PersonalSignatures\":\n" +
                    "                [\n" +
                    "                    {\n" +
                    "                        \"Signer\":\n" +
                    "                        {\n" +
                    "                            \"LocalId\":\"0001510378\",\n" +
                    "                            \"Role\":{\"$\":\"" + Role + "\",\"@version\":\"2.4\"},\n" +
                    "                            \"LastName\":\"Коситченков\",\n" +
                    "                            \"FirstName\":\"Андрей\",\n" +
                    "                            \"MiddleName\":\"Александрович\",\n" +
                    "                            \"Snils\":\"18259202174\",\n" +
                    "                            \"Position\":{\"$\":\"" + position + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                            \"Speciality\":{\"$\":" + speciality + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                            \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                        },\n" +
                    "                            \"Signature\":\n" +
                    "                            {\n" +
                    "                                \"Data\":\"" + Signatures + "\",\n" +
                    "                                  \"CheckSum\":" + CheckSumSign + "\n" +
                    "                            },\n" +
                    "                            \"Description\":\"Подпись сотрудника\"\n" +
                    "                            },\n" +
                    "                            {\n" +
                    "                                \"Signer\":\n" +
                    "                                {\n" +
                    "                                    \"LocalId\":\"0003948083\",\n" +
                    "                                    \"Role\":{\"$\":\"" + Role1 + "\",\"@version\":\"2.4\"},\n" +
                    "                                    \"LastName\":\"Стрекнев\",\n" +
                    "                                    \"FirstName\":\"Денис\",\n" +
                    "                                    \"MiddleName\":\"Юрьевич\",\n" +
                    "                                    \"Snils\":\"18287265004\",\n" +
                    "                                    \"Position\":{\"$\":\"" + position1 + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                                    \"Speciality\":{\"$\":" + speciality1 + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                                    \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                                },\n" +
                    "                                    \"Signature\":\n" +
                    "                                    {\n" +
                    "                                        \"Data\":\"" + Signatures + "\",\n" +
                    "                                       \"CheckSum\":" + CheckSumSign + "\n" +
                    "                                        },\n" +
                    "                                        \"Description\":\"Подпись сотрудника\"\n" +
                    "                                    }\n" +
                    "        ]\n" +
                    "    }";
        }

        System.out.println("Отправляем смс " + DocType + " с vmcl = " + vmcl + "");
        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .post(HostAddress + "/api/smd")
                .body()
                .jsonPath();
        Thread.sleep(3000);

        if (vmcl != 99) {
            sql.StartConnection(
                    "SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                System.out.println(sql.value);
                Assertions.assertNotEquals(sql.value, "NULL", "СМС не добавилось в таблицу " + sms + "");
                Time = sql.resultSet.getString("create_date");
            }

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
                    .body(Body)
                    .post(URLCollback)
                    .then()
                    .statusCode(200);

            System.out.println("Добавляем запись в таблицу " + remd + "");
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
                                                                "  \"emdrId\": \"Проверка 1793\",\n" +
                                                                "  \"status\": \"success\",\n" +
                                                                "  \"registrationDateTime\": \"2022-08-30\",\n" +
                                                                "  \"errors\": [\n" +
                                                                "    {\n" +
                                                                "      \"code\": \"string\",\n" +
                                                                "      \"message\": \"Проверка 1793\"\n" +
                                                                "    }\n" +
                                                                "  ]\n" +
                                                                "}")
                                                  .post(URLCollback)
                                                  .then()
                                                  .extract().jsonPath().getString("id"));
            Assertions.assertEquals(value, "" + xml.uuid + "", "Не сменился статус");
            Thread.sleep(1000);
        }

        System.out.println("Отправляем запрос /api/rremd/");
        if (KingNumber == 1) {
            URLKremd = "http://192.168.2.126:1108/api/rremd/";
        }
        if (KingNumber == 2) {
            URLKremd = "http://192.168.2.126:1131/api/rremd/";
        }
        if (KingNumber == 4) {
            URLKremd = "http://192.168.2.21:34154/api/rremd/";
        }
        JsonPath respons = given()
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .get(URLKremd + "" + xml.uuid + "")
                .body()
                .jsonPath();
        if (time == true) {
            String date = respons.get("result[0].documentDto.creationDateTime");
            Assertions.assertEquals(date.substring(0, date.length() - 19),
                                    "" + Date + "", "Дата не совпадает");
        } else {
            Assertions.assertEquals((respons.get("result[0].documentDto.creationDateTime")),
                                    "2021-01-25T18:00:00.000+05:00", "Дата не совпадает");
        }
    }

    @Order(1)
    @Issue(value = "TEL-1793")
    @Link(name = "ТМС-1789", url = "https://team-1okm.testit.software/projects/5/tests/1789?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправить СЭМД с effectiveTime/ без effectiveTime и проверить методом /api/rremd/, что берутся разные значения, но не передаётся null")
    @Test
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=1")
    public void Access_1793_1() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 1, 2, 3, 1, 9, 18, 1, 57, 21, true, "vimis.sms", "vimis.documentlogs",
                          "vimis.remd_onko_sent_result", "");
    }

    @Test
    @Order(2)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=2")
    public void Access_1793_2() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 2, 2, 3, 1, 9, 18, 1, 57, 21, true, "vimis.preventionsms",
                          "vimis.preventionlogs", "vimis.remd_prevention_sent_result", "");
    }

    @Test
    @Order(3)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=3")
    public void Access_1793_3() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 3, 2, 2, 1, 9, 18, 1, 57, 21, true, "vimis.akineosms",
                          "vimis.akineologs",
                          "vimis.remd_akineo_sent_result", "");
    }

    @Test
    @Order(4)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=4")
    public void Access_1793_4() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 4, 2, 2, 1, 9, 18, 1, 57, 21, true, "vimis.cvdsms", "vimis.cvdlogs",
                          "vimis.remd_cvd_sent_result", "");
    }

    @Test
    @Order(5)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=99")
    public void Access_1793_99() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 99, 2, 2, 1, 9, 18, 1, 57, 21, true, "", "",
                          "vimis.remd_sent_result", "");
    }

    @Test
    @Order(6)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Возвращаем время")
    public void Back_1() throws IOException, InterruptedException {
        xml.ReplaceWord("SMS/SMS3.xml", "<effectiveTime nullFlavor=\"NA\"/>",
                        "<effectiveTime value=\"202101251600+0300\"/>");
    }

    @Order(7)
    @Test
    @Story("Отправляем смс c <effectiveTime value=\"202101251600+0300\"/>")
    @DisplayName("Отправляем смс для vmcl=1")
    public void Access_1793_1_f() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 1, 2, 3, 1, 9, 18, 1, 57, 21, false, "vimis.sms", "vimis.documentlogs",
                          "vimis.remd_onko_sent_result", "");
    }

    @Test
    @Order(8)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=2")
    public void Access_1793_2_f() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 2, 2, 3, 1, 9, 18, 1, 57, 21, false, "vimis.preventionsms",
                          "vimis.preventionlogs", "vimis.remd_prevention_sent_result", "");
    }

    @Test
    @Order(9)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=3")
    public void Access_1793_3_f() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 3, 2, 2, 1, 9, 18, 1, 57, 21, false, "vimis.akineosms",
                          "vimis.akineologs",
                          "vimis.remd_akineo_sent_result", "");
    }

    @Test
    @Order(10)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=4")
    public void Access_1793_4_f() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 4, 2, 2, 1, 9, 18, 1, 57, 21, false, "vimis.cvdsms", "vimis.cvdlogs",
                          "vimis.remd_cvd_sent_result", "");
    }

    @Test
    @Order(11)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=99")
    public void Access_1793_99_f() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 99, 2, 2, 1, 9, 18, 1, 57, 21, false, "", "",
                          "vimis.remd_sent_result", "");
    }
}
