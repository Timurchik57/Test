package api;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Добавление данных из Soap запроса")
public class Access_1318Test extends BaseAPI {
    public String URLCollback;
    Document_667 doc;
    public String body;
    public String content;
    public String encodedString;
    public String id;
    public String local_uid;
    public UUID uuid;
    public String SignatureBody;
    public String HostSignatureSoap;
    public String HostSoap;
    public String SignatureValue;
    public String SignedInfo;
    public String BinSecurityToken;

    /**
     * Метод для создания подписи Soap запроса
     */
    public void Access1328SignatureMethod(
            String File, String SystemId, Integer vmcl, String doctype, Integer docTypeVersion
    ) throws IOException, InterruptedException {
        if (KingNumber == 2 | KingNumber == 5) {
            String file = File;
            FileInputStream in = new FileInputStream("src/test/resources/my.properties");
            props = new Properties();
            props.load(in);
            in.close();
            XML.Type = file;
            Thread.sleep(1000);
            ID = (int) Math.floor(timestamp.getTime() / 1000);
            SetID = (int) Math.floor(timestamp.getTime() / 1000) + 1;
            VN = (int) Math.floor(timestamp.getTime() / 1000) + 2;
            InputProp("src/test/resources/my.properties", "value_ID", String.valueOf(ID));
            InputProp("src/test/resources/my.properties", "value_SetID", String.valueOf(SetID));
            InputProp("src/test/resources/my.properties", "value_VN", String.valueOf(VN));
            xml.ReplaceWord(file, "${iD}", String.valueOf(ID));
            xml.ReplaceWord(file, "${setId}", String.valueOf(SetID));
            xml.ReplaceWord(file, "${versionNumber}", String.valueOf(VN));
            xml.ReplaceWord(file, "${depart}", Departmen);
            xml.ReplaceWord(file, "${mo}", MO);
            xml.ReplaceWord(file, "${guid}", PatientGuid);
            xml.ReplaceWord(file, "${namemo}", NameMO);
            xml.ReplaceWord(file, "${snils}", Snils);
            content = new String(Files.readAllBytes(Paths.get(file)));
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
            uuid = UUID.randomUUID();
            System.out.println(uuid);
            System.out.println("Отправляем запрос на подпись для Soap");
            SignatureBody = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                    "\t<s:Header>\n" +
                    "\t\t<a:Action wsu:Id=\"id-action\">sendDocument</a:Action>\n" +
                    "\t\t<transportHeader xmlns=\"http://egisz.rosminzdrav.ru\">\n" +
                    "\t\t\t<authInfo>\n" +
                    "\t\t\t\t<clientEntityId>" + SystemId + "</clientEntityId>\n" +
                    "\t\t\t</authInfo>\n" +
                    "\t\t</transportHeader>\n" +
                    "\t\t<a:MessageID>43afe69d-b21a-428b-a624-f113f95ec743</a:MessageID>\n" +
                    "\t\t<a:ReplyTo wsu:Id=\"id-replyto\">\n" +
                    "\t\t\t<a:Address>https://ips.rosminzdrav.ru/79f5771afb36b</a:Address>\n" +
                    "\t\t</a:ReplyTo>\n" +
                    "\t\t<a:FaultTo>\n" +
                    "\t\t\t<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\n" +
                    "\t\t</a:FaultTo>\n" +
                    "\t\t<a:To>https://ips.rosminzdrav.ru/8b02e1e4e03c7</a:To>\n" +
                    "\t</s:Header>\n" +
                    "\t<s:Body xmlns:d2p1=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" d2p1:Id=\"body\">\n" +
                    "\t\t<sendDocument xmlns=\"http://vimis.rosminzdrav.ru/\">\n" +
                    "\t\t\t<vmcl xmlns=\"\">" + vmcl + "</vmcl>\n" +
                    "\t\t\t<vmcl_array xmlns=\"\">" + vmcl + "</vmcl_array>\n" +
                    "\t\t\t<docType xmlns=\"\">" + doctype + "</docType>\n" +
                    "\t\t\t<docTypeVersion xmlns=\"\">" + docTypeVersion + "</docTypeVersion>\n" +
                    "\t\t\t<interimMsg xmlns=\"\">1</interimMsg>\n" +
                    "\t\t\t<document xmlns=\"\">" + encodedString + "</document>\n" +
                    "\t\t\t<triggerPoint xmlns=\"\">2</triggerPoint>\n" +
                    "\t\t</sendDocument>\n" +
                    "\t</s:Body>\n" +
                    "</s:Envelope>";
            String SignatureBodyBase64 = Base64.getEncoder().encodeToString(SignatureBody.getBytes());
            if (KingNumber == 2 | KingNumber == 5) {
                HostSignatureSoap = "http://192.168.2.126:8082/sign/xml";
            }
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .when()
                    .body("{\n" +
                                  "                    \"requestId\": \"1\",\n" +
                                  "                    \"signReferenceUri\": \"#body\",\n" +
                                  "                    \"document\": \"" + SignatureBodyBase64 + "\",\n" +
                                  "                    \"algorithm\": \"Gost3411_2012_256\",\n" +
                                  "                    \"systemId\": \"" + SystemId + "\"\n" +
                                  "  }")
                    .post(HostSignatureSoap)
                    .body()
                    .jsonPath();
            System.out.println("Декодируем SignatureValue и SignedInfo");
            String SignatureValueBase64 = response.get("result.signatureValue");
            byte[] decoded = Base64.getDecoder().decode(SignatureValueBase64);
            SignatureValue = new String(decoded, StandardCharsets.UTF_8);
            String SignedInfoBase64 = response.get("result.signedInfo");
            decoded = Base64.getDecoder().decode(SignedInfoBase64);
            SignedInfo = new String(decoded, StandardCharsets.UTF_8);
            BinSecurityToken = response.get("result.binSecurityToken");
        }
    }

    /**
     * Метод для отправки смс с другим телом запроса (soap), и проверка добавления значений в нужные таблицы
     */
    public void Access_1318BodyMethod(
            String DocType, Integer vmcl, Integer docTypeVersion, String SystemId, String sms, String info
    ) throws IOException, InterruptedException, SQLException {
        if (KingNumber == 2 | KingNumber == 5) {
            body = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                    "\t<s:Header>\n" +
                    "\t\t<a:Action wsu:Id=\"id-action\">sendDocument</a:Action>\n" +
                    "\t\t<transportHeader xmlns=\"http://egisz.rosminzdrav.ru\">\n" +
                    "\t\t\t<authInfo>\n" +
                    "\t\t\t\t<clientEntityId>" + SystemId + "</clientEntityId>\n" +
                    "\t\t\t</authInfo>\n" +
                    "\t\t</transportHeader>\n" +
                    "\t\t<a:MessageID>43afe69d-b21a-428b-a624-f113f95ec743</a:MessageID>\n" +
                    "\t\t<a:ReplyTo wsu:Id=\"id-replyto\">\n" +
                    "\t\t\t<a:Address>https://ips.rosminzdrav.ru/79f5771afb36b</a:Address>\n" +
                    "\t\t</a:ReplyTo>\n" +
                    "\t\t<a:FaultTo>\n" +
                    "\t\t\t<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\n" +
                    "\t\t</a:FaultTo>\n" +
                    "\t\t<a:To>https://ips.rosminzdrav.ru/8b02e1e4e03c7</a:To>\n" +
                    "\t\t<wsse:Security>\n" +
                    "\t\t\t<ds:Signature Id=\"SIG-" + UUID.randomUUID() + "\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                    "\t\t\t\t" + SignedInfo + "\n" +
                    "\t\t\t\t<SignatureValue xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" + SignatureValue + "</SignatureValue>\n" +
                    "\t\t\t\t<ds:KeyInfo Id=\"KI-d11574b2-8970-4767-9e19-d9377929227d\">\n" +
                    "\t\t\t\t\t<wsse:SecurityTokenReference Id=\"" + UUID.randomUUID() + "\">\n" +
                    "\t\t\t\t\t\t<wsse:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#" + UUID.randomUUID() + "\"/>\n" +
                    "\t\t\t\t\t</wsse:SecurityTokenReference>\n" +
                    "\t\t\t\t</ds:KeyInfo>\n" +
                    "\t\t\t</ds:Signature>\n" +
                    "\t\t\t<wsse:BinarySecurityToken EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" wsu:Id=\"a9671d73-c729-4327-959e-31928de90b2a\">" + BinSecurityToken + "</wsse:BinarySecurityToken>\n" +
                    "\t\t</wsse:Security>\n" +
                    "\t</s:Header>\n" +
                    "\t<s:Body xmlns:d2p1=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" d2p1:Id=\"body\">\n" +
                    "\t\t<sendDocument xmlns=\"http://vimis.rosminzdrav.ru/\">\n" +
                    "\t\t\t<vmcl xmlns=\"\">" + vmcl + "</vmcl>\n" +
                    "\t\t\t<vmcl_array xmlns=\"\">" + vmcl + "</vmcl_array>\n" +
                    "\t\t\t<docType xmlns=\"\">" + DocType + "</docType>\n" +
                    "\t\t\t<docTypeVersion xmlns=\"\">" + docTypeVersion + "</docTypeVersion>\n" +
                    "\t\t\t<interimMsg xmlns=\"\">1</interimMsg>\n" +
                    "\t\t\t<document xmlns=\"\">" + encodedString + "</document>\n" +
                    "\t\t\t<triggerPoint xmlns=\"\">2</triggerPoint>\n" +
                    "\t\t</sendDocument>\n" +
                    "\t</s:Body>\n" +
                    "</s:Envelope>";
            System.out.println("Отправляем Soap запрос");
            if (KingNumber == 2) {
                HostSoap = "http://192.168.2.126:1153/Vimis";
            }
            if (KingNumber == 5) {
                HostSoap = "http://192.168.2.7:31035/soap";
            }
            given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.XML)
                    .body(body)
                    .when()
                    .post(HostSoap)
                    .then()
                    .statusCode(200);
            Thread.sleep(2000);
            System.out.println("Находим id записи в таблице " + sms + "");
            sql.StartConnection(
                    "Select max(id) from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and centralized_unloading_system_id = '99' and open_telemetry_trace_id = '00000000000000000000000000000000'");
            while (sql.resultSet.next()) {
                id = sql.resultSet.getString("max");
                System.out.println(id);
            }
            System.out.println("Находим запись по id в таблице " + sms + " и сверяем значения");
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and id = '" + id + "'");
            while (sql.resultSet.next()) {
                String patient_guid = sql.resultSet.getString("patient_guid");
                Assertions.assertEquals(
                        patient_guid, "e3c3323e-1e05-4f59-b733-9abe7dfc88ce", "patient_guid не совпадает");
                local_uid = sql.resultSet.getString("local_uid");
                String transfer_id = sql.resultSet.getString("transfer_id");
                Assertions.assertEquals(transfer_id, local_uid, "transfer_id не совпадает");
                String patient_snils = sql.resultSet.getString("patient_snils");
                Assertions.assertEquals(patient_snils, "15979025720", "patient_snils не совпадает");
            }
            System.out.println("Находим запись по id в таблице " + info + "");
            sql.StartConnection("Select * from " + info + " where smsid = '" + id + "'");
            while (sql.resultSet.next()) {
                String authorfname = sql.resultSet.getString("authorfname");
                if (vmcl == 1 | vmcl == 2) {
                    Assertions.assertEquals(authorfname, "Александр", "authorfname не совпадает");
                }
                if (vmcl == 3) {
                    Assertions.assertEquals(authorfname, "Александра", "authorfname не совпадает");
                }
                String authorlname = sql.resultSet.getString("authorlname");
                if (vmcl == 1 | vmcl == 2) {
                    Assertions.assertEquals(authorlname, "Смирнов", "authorlname не совпадает");
                }
                if (vmcl == 3) {
                    Assertions.assertEquals(authorlname, "Иванова", "authorlname не совпадает");
                }
                String authorsnils = sql.resultSet.getString("authorsnils");
                if (vmcl == 1 | vmcl == 2) {
                    Assertions.assertEquals(authorsnils, "15979025720", "authorsnils не совпадает");
                }
                if (vmcl == 3) {
                    Assertions.assertEquals(authorsnils, "95491645759", "authorsnils не совпадает");
                }
                String patientbirthday = sql.resultSet.getString("patientbirthday");
                if (vmcl == 1) {
                    Assertions.assertEquals(patientbirthday, "1960-02-17", "patientbirthday не совпадает");
                }
                if (vmcl == 2) {
                    Assertions.assertEquals(patientbirthday, "1980-02-17", "patientbirthday не совпадает");
                }
                String patientfname = sql.resultSet.getString("patientfname");
                if (vmcl == 1) {
                    Assertions.assertEquals(patientfname, "Михаил", "patientfname не совпадает");
                }
                if (vmcl == 2) {
                    Assertions.assertEquals(patientfname, "Мария", "patientfname не совпадает");
                }
                String patientlname = sql.resultSet.getString("patientlname");
                if (vmcl == 1) {
                    Assertions.assertEquals(patientlname, "Сельченков", "patientlname не совпадает");
                }
                if (vmcl == 2) {
                    Assertions.assertEquals(patientlname, "Спасских", "patientlname не совпадает");
                }
                String patient_local_id = sql.resultSet.getString("patient_local_id");
                Assertions.assertEquals(patient_local_id, PatientGuid, "patient_local_id не совпадает");
                String department_oid = sql.resultSet.getString("department_oid");
                if (vmcl == 1 | vmcl == 2) {
                    Assertions.assertEquals(
                            department_oid, "1.2.643.5.1.13.13.12.2.86.8986.0.536268", "department_oid не совпадает");
                }
                if (vmcl == 3) {
                    Assertions.assertNull(department_oid, "department_oid не совпадает");
                }
            }
        }
    }

    /**
     * Метод для смены статуса отправленной смс (soap), и проверка отображения уведомления
     */
    public void Access1318CallBackMethod(
            String sms, String log, Integer vmcl
    ) throws SQLException, IOException, InterruptedException {
        doc = new Document_667();
        if (KingNumber == 2 | KingNumber == 5) {
            System.out.println("Переходим на сайт для перехвата сообщений");
            driver.get("https://12345.requestcatcher.com/");
            Thread.sleep(1500);
            System.out.println(
                    "В таблице " + sms + " устанавливаем нужный uuid, который будет в запросе на смену статуса");
            sql.UpdateConnection("update " + sms + " set request_id = '" + doc.uuid + "' where id = " + id + ";");
            System.out.println("Отправляем запрос на смену статуса");
            if (KingNumber == 1) {
                URLCollback = "http://192.168.2.126:1108/onko/callback";
            }
            if (KingNumber == 2) {
                URLCollback = "http://192.168.2.126:1131/onko/callback";
            }
            if (KingNumber == 5 && vmcl == 1) {
                URLCollback = "192.168.137.77:1108/onko/callback";
            }
            if (KingNumber == 5 && vmcl == 2) {
                URLCollback = "http://212.96.206.70:1109/prevention/callback";
            }
            if (KingNumber == 5 && vmcl == 3) {
                URLCollback = "192.168.137.77:1108/akineo/callback";
            }
            if (KingNumber == 5 && vmcl == 4) {
                URLCollback = "192.168.137.77:1108/ssz/callback";
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
            System.out.println(local_uid);
            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            ClickElement(By.xpath("//div[@id='selector']/div[1]"));
            WaitElement(By.xpath("//div[@class='request']/pre"));
            String text = driver.findElement(By.xpath("//div[@class='request']/pre")).getText();
            System.out.println(text);
            Assertions.assertTrue(
                    text.contains("Проверяем отправку уведомлений"),
                    "Оповещение для vmcl = " + vmcl + " не добавилось"
            );
            Assertions.assertTrue(
                    text.contains("" + doc.uuid + ""), "Оповещение для vmcl = " + vmcl + " не добавилось");
            sql.StartConnection("SELECT * FROM " + log + "  where sms_id = " + id + ";");
            while (sql.resultSet.next()) {
                String Value1 = sql.resultSet.getString("sms_id");
                String Value2 = sql.resultSet.getString("description");
                String Value3 = sql.resultSet.getString("msg_id");
                Assertions.assertEquals(Value1, id, "СМС не добавилось в таблицу " + log + "");
                Assertions.assertEquals(
                        Value2, "Проверяем отправку уведомлений",
                        "СМС не добавилось в таблицу " + log + " с сообщением - Проверяем отправку уведомлений"
                );
                Assertions.assertEquals(
                        Value3, "" + doc.uuid + "",
                        "СМС не добавилось в таблицу " + log + " с msg_id - " + doc.uuid + ""
                );
            }
        }
    }

    @Issue(value = "TEL-1318")
    @Issue(value = "TEL-1330")
    @Issue(value = "TEL-1352")
    @Link(name = "ТМС-1493", url = "https://team-1okm.testit.software/projects/5/tests/1493?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Link(name = "ТМС-1508", url = "https://team-1okm.testit.software/projects/5/tests/1508?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Добавление данных из Soap запроса vmcl = 1")
    @Description("Отправляем смс soap запросом, проверить добавление в таблицы. Сменить статус смс и проверить уведомление")
    public void Access_1318() throws IOException, SQLException, InterruptedException {
        if (KingNumber == 2) {
            Access1328SignatureMethod("SMS/SMS5.xml", "12571a02-6d0a-43d1-8297-005a9210ab8e", 1, "5", 3);
            Access_1318BodyMethod(
                    "5", 1, 3, "12571a02-6d0a-43d1-8297-005a9210ab8e", "vimis.sms", "vimis.additionalinfo");
            Access1318CallBackMethod("vimis.sms", "vimis.documentlogs", 1);
        }
        if (KingNumber == 5) {
            Access1328SignatureMethod("SMS/SMS5.xml", "12571a02-6d0a-43d1-8297-005a9210ab8e", 1, "5", 3);
            Access_1318BodyMethod(
                    "5", 1, 3, "12571a02-6d0a-43d1-8297-005a9210ab8e", "vimis.sms", "vimis.additionalinfo");
            Access1318CallBackMethod("vimis.sms", "vimis.documentlogs", 1);
        }
    }

    @Test
    @DisplayName("Добавление данных из Soap запроса vmcl = 2")
    public void Access_1318_2() throws IOException, SQLException, InterruptedException {
        Access1328SignatureMethod("SMS/SMS5-vmcl=2.xml", "12571a02-6d0a-43d1-8297-005a9210ab8e", 2, "5", 3);
        Access_1318BodyMethod(
                "5", 2, 3, "12571a02-6d0a-43d1-8297-005a9210ab8e", "vimis.preventionsms",
                "vimis.preventionadditionalinfo"
        );
        Access1318CallBackMethod("vimis.preventionsms", "vimis.preventionlogs", 2);
    }

    @Test
    @DisplayName("Добавление данных из Soap запроса vmcl = 3")
    public void Access_1318_3() throws IOException, SQLException, InterruptedException {
        Access1328SignatureMethod("SMS/SMS5-vmcl=3.xml", "12571a02-6d0a-43d1-8297-005a9210ab8e", 3, "5", 2);
        Access_1318BodyMethod(
                "5", 3, 2, "12571a02-6d0a-43d1-8297-005a9210ab8e", "vimis.akineosms", "vimis.akineoadditionalinfo");
        Access1318CallBackMethod("vimis.akineosms", "vimis.akineologs", 3);
    }

    @Test
    @DisplayName("Добавление данных из Soap запроса vmcl = 4")
    public void Access_1318_4() throws IOException, SQLException, InterruptedException {
        Access1328SignatureMethod("SMS/SMS6-vmcl=4.xml", "12571a02-6d0a-43d1-8297-005a9210ab8e", 4, "6", 2);
        Access_1318BodyMethod(
                "6", 4, 2, "12571a02-6d0a-43d1-8297-005a9210ab8e", "vimis.cvdsms", "vimis.cvdadditionalinfo");
        Access1318CallBackMethod("vimis.cvdsms", "vimis.cvdlogs", 4);

    }
}
