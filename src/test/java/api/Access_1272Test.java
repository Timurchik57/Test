package api;

import UI.SQL;
import UI.TestListener;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileInputStream;
import java.io.IOException;
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
@Feature("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких")
public class Access_1272Test extends BaseAPI {
    public String URLRemd;
    public String body;
    public String content;
    public String encodedString;
    public UUID uuid;
    public static String value_1272_vmcl_1;
    public static String value_1272_vmcl_2;
    public static String value_1272_vmcl_3;
    public static String value_1272_vmcl_4;

    /**
     * Метод для отправки смс с другим телом запроса, где есть все 4 направления
     */
    public void Access_1272Method(
            Integer v1, Integer d1, Integer v2, Integer d2, Integer v3, Integer d3, Integer v4, Integer d4
    ) throws IOException, InterruptedException {
        String file = "SMS/SMS3.xml";
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        Token = new String(Files.readAllBytes(Paths.get("AuthorizationToken.txt")));
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
        body = "{\n" +
                "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                "    \"DocType\":\"3\",\n" +
                "    \"DocContent\":\n" +
                "    {\n" +
                "        \"Document\":\"" + encodedString + "\",\n" +
                "        \"CheckSum\":" + CheckSum + "\n" +
                "        },\n" +
                "        \"LocalUid\":\"" + uuid + "\",\n" +
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
                "                    \"vmcl\": " + v1 + ",\n" +
                "                    \"triggerPoint\": 2,\n" +
                "                    \"docTypeVersion\": " + d1 + "\n" +
                "                    \n" +
                "                },\n" +
                "                {\n" +
                "                    \"vmcl\": " + v2 + ",\n" +
                "                    \"triggerPoint\": 2,\n" +
                "                    \"docTypeVersion\": " + d2 + "\n" +
                "                    \n" +
                "                },\n" +
                "                {\n" +
                "                    \"vmcl\": " + v3 + ",\n" +
                "                    \"triggerPoint\": 2,\n" +
                "                    \"docTypeVersion\": " + d3 + "\n" +
                "                    \n" +
                "                },\n" +
                "                {\n" +
                "                    \"vmcl\": " + v4 + ",\n" +
                "                    \"triggerPoint\": 2,\n" +
                "                    \"docTypeVersion\": " + d4 + "\n" +
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
                "                            \"Role\":{\"$\":\"1\",\"@version\":\"2.4\"},\n" +
                "                            \"LastName\":\"Коситченков\",\n" +
                "                            \"FirstName\":\"Андрей\",\n" +
                "                            \"MiddleName\":\"Александрович\",\n" +
                "                            \"Snils\":\"18259202174\",\n" +
                "                            \"Position\":{\"$\":\"9\",\"@version\":\"" + Position + "\"},\n" +
                "                            \"Speciality\":{\"$\":18,\"@version\":\"" + Speciality + "\"},\n" +
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
                "                                    \"Role\":{\"$\":\"1\",\"@version\":\"2.4\"},\n" +
                "                                    \"LastName\":\"Стрекнев\",\n" +
                "                                    \"FirstName\":\"Денис\",\n" +
                "                                    \"MiddleName\":\"Юрьевич\",\n" +
                "                                    \"Snils\":\"18287265004\",\n" +
                "                                    \"Position\":{\"$\":\"57\",\"@version\":\"" + Position + "\"},\n" +
                "                                    \"Speciality\":{\"$\":21,\"@version\":\"" + Speciality + "\"},\n" +
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
        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .post(HostAddress + "/api/smd")
                .prettyPeek()
                .body()
                .jsonPath();
    }

    /**
     * Метод для проверки for_send и установки status = 1 в logs
     */
    public void InsertSMS(Integer vmcl, String value, String NameProp) throws SQLException, IOException {
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Проверяем смс3 с vmcl=" + vmcl + "");
            String sms = "";
            String logs = "";
            Integer number = 0;
            for (int i = 1; i < 5; i++) {
                if (i == 1) {
                    sms = "vimis.sms";
                    logs = "vimis.documentlogs";
                }
                if (i == 2) {
                    sms = "vimis.preventionsms";
                    logs = "vimis.preventionlogs";
                }
                if (i == 3) {
                    sms = "vimis.akineosms";
                    logs = "vimis.akineologs";
                }
                if (i == 4) {
                    sms = "vimis.cvdsms";
                    logs = "vimis.cvdlogs";
                }
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + uuid + "';");
                while (sql.resultSet.next()) {
                    value = sql.resultSet.getString("id");
                    sql.value = sql.resultSet.getString("for_send");
                    System.out.println(value);
                }
                if (i == vmcl) {
                    Assertions.assertEquals(sql.value, "t", "У первого vmcl статус не true");
                    InputProp("src/test/resources/my.properties", NameProp, value);
                } else {
                    Assertions.assertEquals(sql.value, "f", "У vmcl кроме первого статус не false");
                }
                System.out.println("Устанавливаем status = 1 в " + logs + "");
                sql.UpdateConnection(
                        "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + value + "', '" + UUID.randomUUID() + "')");
            }
        }
    }

    /**
     * Метод для проверки, что в remd добавилась только нужная смс
     */
    public void BeforeAccess_1272(String sms, String value) throws SQLException, IOException {
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        sql = new SQL();
        System.out.println("Проверяю в таблице " + sms + " с id = " + props.getProperty("" + value + "") + "");
        sql.SQL("Select count(*) from " + sms + " where sms_id = " + props.getProperty("" + value + "") + ";");

    }

    @Issue(value = "TEL-1272")
    @Link(name = "ТМС-1475", url = "https://team-1okm.testit.software/projects/5/tests/1475?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких vmcl = 1")
    @Description("Отправляем смс по нескольким направлениям и проверяем, что for_send = true только у первого стоящего vmcl, и отправка в РЭМД идёт только у него")
    public void Access_1272_Vmcl1() throws IOException, SQLException, InterruptedException {
        Access_1272Method(1, 3, 2, 3, 3, 2, 4, 2);
        InsertSMS(1, value_1272_vmcl_1, "value_1272_vmcl_1");
    }

    @Test
    @DisplayName("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких vmcl = 2")
    public void Access_1272_Vmcl2() throws IOException, SQLException, InterruptedException {
        Access_1272Method(2, 3, 1, 3, 3, 2, 4, 2);
        InsertSMS(2, value_1272_vmcl_2, "value_1272_vmcl_2");
    }

    @Test
    @DisplayName("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких vmcl = 3")
    public void Access_1272_Vmcl3() throws IOException, SQLException, InterruptedException {
        Access_1272Method(3, 2, 1, 3, 2, 3, 4, 2);
        InsertSMS(3, value_1272_vmcl_3, "value_1272_vmcl_3");
    }

    @Test
    @DisplayName("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких vmcl = 4")
    public void Access_1272_Vmcl4() throws IOException, SQLException, InterruptedException {
        Access_1272Method(4, 2, 1, 3, 2, 3, 3, 2);
        InsertSMS(4, value_1272_vmcl_4, "value_1272_vmcl_4");
    }
}
