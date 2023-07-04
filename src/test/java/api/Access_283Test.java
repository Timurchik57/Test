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

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Добавление СЭМД с необязательным блоком replace")
public class Access_283Test extends BaseAPI {
    SQL sql;
    public String value_283;
    public static String random;
    public String body;
    public String content;
    public String encodedString;
    public UUID uuid;
    public String EmdId = "1";

    public String AddBody_283(
            String FileName, String DocType, Integer vmcl, Integer number, Integer docTypeVersion, Integer Role,
            Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1
    ) throws IOException, InterruptedException {
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        Token = new String(Files.readAllBytes(Paths.get("AuthorizationToken.txt")));
        XML.Type = FileName;
        if (number == 1) {
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
            xml.ReplaceWord(FileName, "${mo}", MO);
            xml.ReplaceWord(FileName, "${guid}", PatientGuid);
            xml.ReplaceWord(FileName, "${namemo}", NameMO);
            xml.ReplaceWord(FileName, "${snils}", Snils);

        }
        if (number == 2) {
            Thread.sleep(1000);
            ID = Long.parseLong(props.getProperty("value_ID")) + 5;
            SetID = Long.parseLong(props.getProperty("value_SetID")) + 5;
            VN = Long.parseLong(props.getProperty("value_VN")) + 5;
            InputProp("src/test/resources/my.properties", "value_ID", String.valueOf(ID));
            InputProp("src/test/resources/my.properties", "value_SetID", String.valueOf(SetID));
            InputProp("src/test/resources/my.properties", "value_VN", String.valueOf(VN));
            xml.ReplaceWord(FileName, "${iD}", String.valueOf(ID));
            xml.ReplaceWord(FileName, "${setId}", String.valueOf(SetID));
            xml.ReplaceWord(FileName, "${versionNumber}", String.valueOf(VN));
            xml.ReplaceWord(FileName, "${mo}", MO);
            xml.ReplaceWord(FileName, "${guid}", PatientGuid);
            xml.ReplaceWord(FileName, "${namemo}", NameMO);
            xml.ReplaceWord(FileName, "${snils}", Snils);
        }
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
        uuid = UUID.randomUUID();
        System.out.println(uuid);
        if (vmcl == 99) {
            body = "{\n" +
                    "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                    "    \"DocType\":\"" + DocType + "\",\n" +
                    "    \"DocContent\":\n" +
                    "    {\n" +
                    "        \"Document\":\"" + encodedString + "\",\n" +
                    "        \"CheckSum\":" + CheckSum + "\n" +
                    "        },\n" +
                    "        \"LocalUid\":\"" + uuid + "\",\n" +
                    "        \"replace\" : { \n" +
                    "        \"emdId\": \"" + EmdId + "\", \n" +
                    "        \"replaced_version\":  1 \n" +
                    "        }, \n" +
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
                    "                                \"CheckSum\":" + CheckSumSign + "\n" +
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
                    "                                        \"CheckSum\":" + CheckSumSign + "\n" +
                    "                                        },\n" +
                    "                                        \"Description\":\"Подпись сотрудника\"\n" +
                    "                                    }\n" +
                    "        ]\n" +
                    "    }";

        }
        if (vmcl != 99) {
            body = "{\n" +
                    "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                    "    \"DocType\":\"" + DocType + "\",\n" +
                    "    \"DocContent\":\n" +
                    "    {\n" +
                    "        \"Document\":\"" + encodedString + "\",\n" +
                    "        \"CheckSum\":" + CheckSum + "\n" +
                    "        },\n" +
                    "        \"LocalUid\":\"" + uuid + "\",\n" +
                    "        \"emdId\": \"" + EmdId + "\", \n" +
                    "        \"replaced_version\":  1 \n" +
                    "        }, \n" +
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
                    "                    \"triggerPoint\": 2,\n" +
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
        }
        return body;

    }

    public void Access_283Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        AddBody_283(
                FileName, DocType, vmcl, 1, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            random = String.valueOf((int) Math.floor(Math.random() * 1001));
            System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            JsonPath respons = given()
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
                    "Select * from vimis.remd_sent_result where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + uuid + "';");
            while (sql.resultSet.next()) {
                value_283 = sql.resultSet.getString("id");
                System.out.println(value_283);
            }
            System.out.println("Добавляем смс рандомное значение emd_id = " + random + "");
            sql.UpdateConnection(
                    "update vimis.remd_sent_result set emd_id = " + random + " where local_uid = '" + uuid + "';");
            System.out.println("Повторно отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            EmdId = random;
            xml.ReplacementWordInFileBack(FileName);
            AddBody_283(
                    FileName, DocType, vmcl, 2, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            JsonPath respons2 = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            System.out.println("Проверяем, что добавилась новая запись с emd_id = " + random + " и  version = 1");
            sql.StartConnection(
                    "Select * from vimis.remd_sent_result where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + uuid + "';");
            while (sql.resultSet.next()) {
                String emd_id = sql.resultSet.getString("emd_id");
                String version = sql.resultSet.getString("version");
                Assertions.assertEquals(emd_id, "" + random + "", "Не добавилась новая смс с emd_id = " + random + "");
                Assertions.assertEquals(version, "1", "Не добавилась новая смс с  version = 1");
            }
            EmdId = "9876879";
            xml.ReplacementWordInFileBack(FileName);
            AddBody_283(
                    FileName, DocType, vmcl, 2, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            JsonPath respons3 = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            Assertions.assertEquals(
                    respons3.get("result[0].errorMessage"),
                    "EmdId: Документ с указанный идентификатором \"emdId\" не может быть заменен, так как не передавался ранее через интеграционный шлюз"
            );
        }


    }

    @Issue(value = "TEL-283")
    @Link(name = "ТМС-1110", url = "https://team-1okm.testit.software/projects/5/tests/1110?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Добавление СЭМД с необязательным блоком replace id = 15")
    @Description("Отправляем смс только для vmcl=99, добавляем значение в vimis.remd_sent_result.emdid, после отправляем тот же СЭМД с указанием добавленного emdid и version")
    public void Access_283Id_15() throws IOException, SQLException, InterruptedException {
        Access_283Method("SMS/id=15-vmcl=99.xml", "15", 99, 1, true, 1, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Добавление СЭМД с необязательным блоком replace id = 3")
    public void Access_283Id_3() throws IOException, SQLException, InterruptedException {
        Access_283Method("SMS/SMS3.xml", "3", 99, 1, true, 1, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Добавление СЭМД с необязательным блоком replace id = 36")
    public void Access_283Id_36() throws IOException, SQLException, InterruptedException {
        Access_283Method("SMS/id=36-vmcl=99.xml", "36", 99, 1, true, 1, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Добавление СЭМД с необязательным блоком replace id = 32")
    public void Access_283Id_32() throws IOException, SQLException, InterruptedException {
        Access_283Method("SMS/id=32-vmcl=99.xml", "32", 99, 1, true, 1, 6, 4, 18, 1, 57, 21);
    }
}
