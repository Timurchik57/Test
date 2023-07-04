package api;

import UI.TestListener;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Сервис запроса метаданных КРЭМД")
public class Access_1437Test extends BaseAPI {
    public void Access_1473Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd
    ) throws IOException, InterruptedException, SQLException {
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            xml.ReplacementWordInFile(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
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
            String transferId = response.get("result[0].transferId");
            if (vmcl == 99) {
                sql.StartConnection(
                        "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    System.out.println(sql.value);
                }
            } else {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    System.out.println(sql.value);
                }
                System.out.println("Устанавливаем status = 1 в " + logs + "");
                sql.UpdateConnection(
                        "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + sql.value + "', '" + UUID.randomUUID() + "')");
                System.out.println("Устанавливаем status = 1 в " + remd + "");
                sql.UpdateConnection(
                        "insert into " + remd + " (sms_id, local_uid, status, created_datetime, fremd_status) values (" + sql.value + ", '" + xml.uuid + "', 'success', '" + Date + " 00:00:00.888 +0500', NULL);");
            }
            System.out.println("Отправляем запрос в КРЭМД");
            JsonPath respons = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddress + "/api/smd/meta-remd-document?transferId=" + transferId + "")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            Assertions.assertEquals(respons.get("result[0].localUid"), "" + xml.uuid + "", "localUid не совпадает");
            Assertions.assertEquals(
                    respons.get("result[0].documentNumber"), "" + ID + "", "Версия документа не совпадает");
            assertThat(respons.get("result[0].DocContent"), equalTo(null));
            assertThat(respons.get("result[0].PersonalSignarure"), equalTo(null));
            assertThat(respons.get("result[0].OrgSignature"), equalTo(null));
        }
    }

    @Issue(value = "TEL-1473")
    @Link(name = "ТМС-1537", url = "https://team-1okm.testit.software/projects/5/tests/1537?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @Description("Сервис запроса метаданных КРЭМД")
    @DisplayName("Сервис запроса метаданных КРЭМД vmcl=1")
    @Test
    public void Access_1437Vmcl_1() throws InterruptedException, SQLException, IOException {
        Access_1473Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );

    }

    @DisplayName("Сервис запроса метаданных КРЭМД vmcl=2")
    @Test
    public void Access_1437Vmcl_2() throws InterruptedException, SQLException, IOException {
        Access_1473Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );

    }

    @DisplayName("Сервис запроса метаданных КРЭМД vmcl=3")
    @Test
    public void Access_1437Vmcl_3() throws InterruptedException, SQLException, IOException {
        Access_1473Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );

    }

    @DisplayName("Сервис запроса метаданных КРЭМД vmcl=4")
    @Test
    public void Access_1437Vmcl_4() throws InterruptedException, SQLException, IOException {
        Access_1473Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );

    }

    @DisplayName("Сервис запроса метаданных КРЭМД vmcl=99")
    @Test
    public void Access_1437Vmcl_99() throws InterruptedException, SQLException, IOException {
        Access_1473Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");

    }
}
