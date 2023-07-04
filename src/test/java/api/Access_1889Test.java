package api;

import UI.TestListener;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
public class Access_1889Test extends BaseAPI {

    public String body;
    public String content;
    public String encodedString;
    public String guestation_age_registated;
    public String pregnancy_registration_date;
    public String pregnancy_number;
    public String pregnancy_outcome;
    public String abortion;
    public String childbirth_term;

    @Step("Метод отправки смс: {0}, vmcl = {3}, и проверки данных в таблице vimis.akineoadditionalinfo ")
    public void Access_1889Method(String FileName, String DocType, Integer vmcl, Integer triggerPoint, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1,
            Integer position1, Integer speciality1) throws IOException, InterruptedException, SQLException {

        Token = new String(Files.readAllBytes(Paths.get("AuthorizationToken.txt")));

        XML.Type = FileName;

        Thread.sleep(1000);
        ID = (int) Math.floor(timestamp.getTime() / 1000);
        SetID = (int) Math.floor(timestamp.getTime() / 1000) + 1;
        VN = (int) Math.floor(timestamp.getTime() / 1000) + 2;
        InputProp("src/test/resources/my.properties", "value_ID", String.valueOf(ID));
        InputProp("src/test/resources/my.properties", "value_SetID", String.valueOf(SetID));
        InputProp("src/test/resources/my.properties", "value_VN", String.valueOf(VN));

        if (DocType == "24") {
            xml.ReplaceWord(FileName, "${ksomo}", "1.2.643.5.1.13.13.12.2.86.9006");
            xml.ReplaceWord(FileName, "${ksoyear}", "2021");
            xml.ReplaceWord(FileName, "${ksonumber}", "21-86-05295");
        }

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
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .post(HostAddress + "/api/smd")
                .prettyPeek()
                .body()
                .jsonPath();

        sql.StartConnection(
                "SELECT * FROM vimis.akineosms  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            Assertions.assertNotEquals(sql.value, "NULL", "СМС не добавилось в таблицу vimis.akineosms");
            System.out.println(sql.value);
        }

        sql.StartConnection(
                "SELECT * FROM vimis.akineoadditionalinfo  where smsid = '" + sql.value + "';");
        while (sql.resultSet.next()) {
            guestation_age_registated = sql.resultSet.getString("guestation_age_registated");
            pregnancy_registration_date = sql.resultSet.getString("pregnancy_registration_date");
            pregnancy_number = sql.resultSet.getString("pregnancy_number");
            pregnancy_outcome = sql.resultSet.getString("pregnancy_outcome");
            abortion = sql.resultSet.getString("abortion");
            childbirth_term = sql.resultSet.getString("childbirth_term");
        }

        if (DocType == "5") {
            Assertions.assertEquals(guestation_age_registated, "1",
                                    "guestation_age_registated не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_registration_date, "2020-05-25 17:30:00+05",
                                    "pregnancy_registration_date не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_number, "2", "pregnancy_number не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_outcome, "{8,8}", "pregnancy_outcome не совпадает с нужным значением");
            Assertions.assertEquals(abortion, "14", "abortion не совпадает с нужным значением");
            Assertions.assertEquals(childbirth_term, "37", "childbirth_term не совпадает с нужным значением");
        }
        if (DocType == "17") {
            Assertions.assertEquals(guestation_age_registated, "1",
                                    "guestation_age_registated не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_registration_date, "2020-05-25 17:30:00+05",
                                    "pregnancy_registration_date не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_number, "2", "pregnancy_number не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_outcome, "{8,8}", "pregnancy_outcome не совпадает с нужным значением");
            Assertions.assertEquals(abortion, null, "abortion не совпадает с нужным значением");
            Assertions.assertEquals(childbirth_term, "39", "childbirth_term не совпадает с нужным значением");
        }
        if (DocType == "8") {
            Assertions.assertEquals(guestation_age_registated, null,
                                    "guestation_age_registated не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_registration_date, null,
                                    "pregnancy_registration_date не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_number, "2", "pregnancy_number не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_outcome, "{}", "pregnancy_outcome не совпадает с нужным значением");
            Assertions.assertEquals(abortion, "14", "abortion не совпадает с нужным значением");
            Assertions.assertEquals(childbirth_term, "37", "childbirth_term не совпадает с нужным значением");
        }
        if (DocType == "24") {
            Assertions.assertEquals(guestation_age_registated, null,
                                    "guestation_age_registated не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_registration_date, null,
                                    "pregnancy_registration_date не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_number, null, "pregnancy_number не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_outcome, "{8,8}", "pregnancy_outcome не совпадает с нужным значением");
            Assertions.assertEquals(abortion, "14", "abortion не совпадает с нужным значением");
            Assertions.assertEquals(childbirth_term, "23", "childbirth_term не совпадает с нужным значением");
        }
        if (DocType == "112") {
            Assertions.assertEquals(guestation_age_registated, null,
                                    "guestation_age_registated не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_registration_date, null,
                                    "pregnancy_registration_date не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_number, null, "pregnancy_number не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_outcome, "{}", "pregnancy_outcome не совпадает с нужным значением");
            Assertions.assertEquals(abortion, null, "abortion не совпадает с нужным значением");
            Assertions.assertEquals(childbirth_term, null, "childbirth_term не совпадает с нужным значением");
        }
    }

    @Issue(value = "TEL-1889")
    @Link(name = "ТМС-", url = "https://team-1okm.testit.software/projects/5/tests/1790?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Добавление данных в бд по беременным")
    @DisplayName("Добавление данных в бд по беременным для смс 5")
    @Description("Отправить смс 5/17/32 c vmcl = 3, перейти в таблицу vimis.akineoadditionalinfo и проверить добавление данных по регистру беременных")
    public void Access_1889_5() throws IOException, SQLException, InterruptedException {
        Access_1889Method("SMS/SMS5-vmcl=3(v1.2).xml", "5", 3, 2, 2, 6, 4,
                          18, 1, 57, 21);
    }

    @Test
    @Story("Добавление данных в бд по беременным")
    @DisplayName("Добавление данных в бд по беременным для смс 17")
    public void Access_1889_17() throws IOException, SQLException, InterruptedException {
        Access_1889Method("SMS/SMS17-vmcl=3.xml", "19", 3, 2, 2, 6, 4,
                          18, 1, 57, 21);
    }

    @Test
    @Story("Добавление данных в бд по беременным")
    @DisplayName("Добавление данных в бд по беременным для смс 8")
    public void Access_1889_8() throws IOException, SQLException, InterruptedException {
        Access_1889Method("SMS/SMS8-vmcl=3(v1.2).xml", "8", 3, 1, 2, 6, 4,
                          18, 1, 57, 21);
    }

    @Test
    @Story("Добавление данных в бд по беременным")
    @DisplayName("Добавление данных в бд по беременным для смс 22")
    public void Access_1889_22() throws IOException, SQLException, InterruptedException {
        Access_1889Method("SMS/SMS22-vmcl=3.xml", "24", 3, 5, 3, 6, 4,
                          18, 1, 57, 21);
    }

    @Test
    @Story("Добавление данных в бд по беременным")
    @DisplayName("Добавление данных в бд по беременным для смс 30")
    public void Access_1889_30() throws IOException, SQLException, InterruptedException {
        Access_1889Method("SMS/SMS30-vmcl=3.xml", "112", 3, 2, 3, 6, 4,
                          18, 1, 57, 21);
    }
}
