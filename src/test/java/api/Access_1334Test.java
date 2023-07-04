package api;

import UI.TestListener;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
@Feature("Запрос статуса Soap запроса")
public class Access_1334Test extends BaseAPI {
    Access_1318Test access_1318Test;
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
    public String SystemID;

    /**
     * Метод для создания подписи Soap запроса
     */
    public void Access1328SignatureMethod(
            String File, String SystemId, Integer vmcl, String doctype, Integer docTypeVersion
    ) throws IOException, InterruptedException {
        if (KingNumber == 2) {
            SystemID = SystemId;
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
            if (KingNumber == 2) {
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
    public void Access_1334BodyMethod(
            String DocType, Integer vmcl, Integer docTypeVersion, String sms
    ) throws IOException, InterruptedException, SQLException {
        if (KingNumber == 2) {
            body = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                    "\t<s:Header>\n" +
                    "\t\t<a:Action wsu:Id=\"id-action\">sendDocument</a:Action>\n" +
                    "\t\t<transportHeader xmlns=\"http://egisz.rosminzdrav.ru\">\n" +
                    "\t\t\t<authInfo>\n" +
                    "\t\t\t\t<clientEntityId>" + SystemID + "</clientEntityId>\n" +
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
        }
    }

    @Issue(value = "TEL-1334")
    @Link(name = "ТМС-1496", url = "https://team-1okm.testit.software/projects/5/tests/1496?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Запрос статуса Soap запроса")
    @Description("Отправляем смс soap запросом, проверить добавление в таблицы. Отправить запрос статуса, проверить ответ")
    public void Access_1334() throws IOException, SQLException, InterruptedException {
        access_1318Test = new Access_1318Test();
        if (KingNumber == 2) {
            System.out.println("Создаём подпись для Soap запроса");
            Access1328SignatureMethod("SMS/SMS5.xml", "12571a02-6d0a-43d1-8297-005a9210ab8e", 1, "5", 3);
            System.out.println("Отправляем Soap запрос");
            Access_1334BodyMethod("5", 1, 3, "vimis.sms");
            System.out.println("Устанавливаем status = 1 в vimis.documentlogs");
            sql.UpdateConnection(
                    "insert into vimis.documentlogs (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'Статус сменился по заявке 1334', '" + id + "', '" + UUID.randomUUID() + "')");
            System.out.println("Отправляем запрос статуса");
            String body = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                    "    <s:Header>\n" +
                    "        <a:Action wsu:Id=\"id-action\">checkStatus</a:Action>\n" +
                    "        <transportHeader xmlns=\"http://egisz.rosminzdrav.ru\">\n" +
                    "            <authInfo>\n" +
                    "                <clientEntityId>19723f16-2f55-a5d0-9a9d-593199a22ef3</clientEntityId>\n" +
                    "            </authInfo>\n" +
                    "        </transportHeader>\n" +
                    "        <a:MessageID>43afe69d-b21a-428b-a624-f113f95ec743</a:MessageID>\n" +
                    "\t\t<a:ReplyTo wsu:Id=\"id-replyto\">\n" +
                    "\t\t\t<a:Address>https://ips.rosminzdrav.ru/79f5771afb36b</a:Address>\n" +
                    "\t\t</a:ReplyTo>\n" +
                    "\t\t<a:FaultTo>\n" +
                    "\t\t\t<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\n" +
                    "\t\t</a:FaultTo>\n" +
                    "\t\t<a:To>https://ips.rosminzdrav.ru/8b02e1e4e03c7</a:To>\n" +
                    "        <wsse:Security>\n" +
                    "            <ds:Signature Id=\"SIG-a450f89e-67f4-497a-8f28-c11fad05e0d2\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                    "                <SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                    "                    <CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                    "                    <SignatureMethod Algorithm=\"urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102012-gostr34112012-256\" />\n" +
                    "                    <Reference URI=\"#body\">\n" +
                    "                        <Transforms>\n" +
                    "                            <Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" />\n" +
                    "                            <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                    "                        </Transforms>\n" +
                    "                        <DigestMethod Algorithm=\"urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34112012-256\" />\n" +
                    "                        <DigestValue>ITBagGDt7tSxjhgxGOng+Sh7Lhu4pwpKvwrZ800jjek=</DigestValue>\n" +
                    "                    </Reference>\n" +
                    "                </SignedInfo>\n" +
                    "                <SignatureValue xmlns=\"http://www.w3.org/2000/09/xmldsig#\">tW+AJcFAaeq8vJ8YtFu//jZq9sUVKDCem3GC6VFwuhtX1W9BI++/o8b0It7zfWTOmcDS3z+KGYg17bs07f5JgA==</SignatureValue>\n" +
                    "                <ds:KeyInfo Id=\"KI-88624081-8512-4a77-9526-040f5b8252db\">\n" +
                    "                    <wsse:SecurityTokenReference Id=\"STR-721f13eb-32f4-42ca-961b-bc888b76519c\">\n" +
                    "                        <wsse:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#565bbb1d-dd7f-41fa-bb94-591631ce1fb9\" />\n" +
                    "                    </wsse:SecurityTokenReference>\n" +
                    "                </ds:KeyInfo>\n" +
                    "            </ds:Signature>\n" +
                    "            <wsse:BinarySecurityToken EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" wsu:Id=\"565bbb1d-dd7f-41fa-bb94-591631ce1fb9\">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</wsse:BinarySecurityToken>\n" +
                    "        </wsse:Security>\n" +
                    "    </s:Header>\n" +
                    "    <s:Body xmlns:d2p1=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" d2p1:Id=\"body\">\n" +
                    "        <checkStatus xmlns=\"http://vimis.rosminzdrav.ru/\">\n" +
                    "            <msg_id xmlns=\"\">" + local_uid + "</msg_id>\n" +
                    "        </checkStatus>\n" +
                    "    </s:Body>\n" +
                    "</s:Envelope>";
            Response response = given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.XML)
                    .body(body)
                    .when()
                    .post("http://192.168.2.126:1153/Vimis")
                    .then()
                    .extract().response();
            System.out.println(response.asString());
            Assertions.assertTrue(
                    response.asString().contains("<status xmlns=\"\">1</status>"),
                    "Статус не сменился на 1"
            );
            Assertions.assertTrue(
                    response.asString().contains(
                            "<description xmlns=\"\">Статус сменился по заявке 1334</description>"),
                    "Статус не сменился на - Статус сменился по заявке 1334"
            );
        }
    }
}
