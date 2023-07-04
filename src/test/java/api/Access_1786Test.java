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
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Добавление данных диспансерных больных в таблицу по смс5")
public class Access_1786Test extends BaseAPI {

    public String body;
    public String content;
    public String encodedString;

    @Step("Отправляем смс = {1} и добавляем статус 1 в таблицу {11}")
    public void Access_1786Method(String FileName, String DocType, Integer vmcl, Integer triggerPoint, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String log) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        xml = new XML();
        date = new Date();

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Date = formatForDateNow.format(date);

        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

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

        body = "{\n" +
                "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                "    \"DocType\":\"" + DocType + "\",\n" +
                "    \"DocContent\":\n" +
                "    {\n" +
                "        \"Document\":\"" + encodedString + "\",\n" +
                "        \"CheckSum\":" + CheckSum + "\n" +
                "        },\n" +
                "        \"LocalUid\":\"" + xml.uuid + "\",\n" +
                "        \"association\" : [{ \n" +
                "        \"target\": \"тест 1484\", \n" +
                "        \"type\":  \"тест 1484\" \n" +
                "        },{" +
                "        \"target\": \"тест 1786\", \n" +
                "        \"type\":  \"тест 1786\" \n" +
                "          } ], \n" +
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

        sql.StartConnection(
                "SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            System.out.println(sql.value);
            Assertions.assertNotEquals(sql.value, "NULL", "СМС не добавилось в таблицу " + sms + "");
        }

        System.out.println("Устанавливаем status = 1 в " + log + "");
        sql.UpdateConnection(
                "insert into " + log + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + sql.value + "', '" + UUID.randomUUID() + "')");

        if (triggerPoint == 6) {
            InputProp("src/test/resources/my.properties", "Local_uid_1786_tg6", String.valueOf(xml.uuid));
            InputProp("src/test/resources/my.properties", "value_1786_Vmcl_2_tg6", sql.value);
        } else {
            InputProp("src/test/resources/my.properties", "Local_uid_1786_tg12", String.valueOf(xml.uuid));
            InputProp("src/test/resources/my.properties", "value_1786_Vmcl_2_tg12", sql.value);
        }
    }

    @Step("Проверка добавления отправленной смс поле всех тестов в таблице = {1}")
    public void Access_1786After(String NameProp, String remd) throws IOException, SQLException {
        sql = new SQL();
        xml = new XML();

        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {

            System.out.println("Проверка добавления значения по заявке 1786");
            System.out.println(props.getProperty("" + NameProp + ""));
            sql.SQL("Select count(*) from " + remd + " where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
            sql.StartConnection(
                    "Select * from " + remd + " where sms_id = '" + props.getProperty("" + NameProp + "") + "';");
            while (sql.resultSet.next()) {
                String mo_oid = sql.resultSet.getString("mo_oid");
                String patient_guid = sql.resultSet.getString("patient_guid");
                String diagnosis = sql.resultSet.getString("diagnosis");
                String dispensary_reason_removal_id = sql.resultSet.getString("dispensary_reason_removal_id");
                String local_uid = sql.resultSet.getString("local_uid");

                Assertions.assertEquals(mo_oid, MO, "mo_oid не совпадает");
                Assertions.assertEquals(patient_guid, PatientGuid, "patient_guid не совпадает");
                Assertions.assertEquals(diagnosis, "A00", "diagnosis не совпадает");
                Assertions.assertEquals(dispensary_reason_removal_id, "31",
                                        "dispensary_reason_removal_id не совпадает");
                if (NameProp == "value_1786_Vmcl_2_tg6") {
                    Assertions.assertEquals(local_uid, "" + props.getProperty("Local_uid_1786_tg6") + "",
                                            "local_uid не совпадает");
                } else {
                    Assertions.assertEquals(local_uid, "" + props.getProperty("Local_uid_1786_tg12") + "",
                                            "local_uid не совпадает");
                }
            }
            System.out.println("Значение created_datetime по заявке 1786 добавились в таблицу " + remd + "");
        }
    }

    @Issue(value = "TEL-1786")
    @Issue(value = "TEL-1800")
    @Link(name = "ТМС-1767", url = "https://team-1okm.testit.software/projects/5/tests/1767?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправить СЭМД смс5 с triggerPoint = 6, установить статус 1 в таблицу vimis.preventionlogs, проверить заполение теблицы vimis.prevention_sms_v5_register")
    @Test
    @Story("Отправляем смс5")
    @DisplayName("Добавление данных диспансерных больных в таблицу по смс5 c triggerPoint = 6")
    public void Access_1786_6() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS5-vmcl=2.xml", "5", 2, 6, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms",
                          "vimis.preventionlogs");
    }

    @Test
    @Story("Отправляем смс5")
    @DisplayName("Добавление данных диспансерных больных в таблицу по смс5 c triggerPoint = 12")
    public void Access_1786_12() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS5-vmcl=2.xml", "5", 2, 12, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms",
                          "vimis.preventionlogs");
    }
}
