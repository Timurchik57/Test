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

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Заполнение блока подразделения в запросе к РРЭМД")
public class Access_603Test extends BaseAPI {
    SQL sql;
    public String value603;
    public String URLKremd;

    public void Access_603Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            if (vmcl == 99) {
                sql.StartConnection(
                        "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value603 = sql.resultSet.getString("id");
                    System.out.println(value603);
                }
            } else {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value603 = sql.resultSet.getString("id");
                    System.out.println(value603);
                }
                System.out.println("Устанавливаем status = 1 в " + logs + "");
                sql.UpdateConnection(
                        "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + value603 + "', '" + UUID.randomUUID() + "')");
                System.out.println("Устанавливаем status = 1 в " + remd + "");
                sql.UpdateConnection(
                        "insert into " + remd + " (sms_id, local_uid, status, created_datetime, fremd_status) values (" + value603 + ", '" + xml.uuid + "', 'success', '" + Date + " 00:00:00.888 +0500', NULL);");
            }
            System.out.println("Отправляем запрос в КРЭМД");
            if (KingNumber == 1) {
                URLKremd = "http://192.168.2.126:1108/api/rremd/";
            }
            if (KingNumber == 2) {
                URLKremd = "http://192.168.2.126:1131/api/rremd/";
            }
            if (KingNumber == 4) {
                URLKremd = "http://192.168.2.21:34154/api/rremd/";
            }
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(URLKremd + "" + xml.uuid + "")
                    .then()
                    .statusCode(200)
                    .extract().jsonPath();
            if (SmdToMinio == 0) {
                Assertions.assertEquals(
                        response.getString("result[0].department.localId.code"),
                        "1.2.643.5.1.13.13.12.2.86.8986.0.536268",
                        "Signer.Department в смс не совпадает с department.localId.code из КРЕМД"
                );
            } else {
                Assertions.assertEquals(
                        response.getString("result[0].documentDto.department.localId.code"),
                        "1.2.643.5.1.13.13.12.2.86.8986.0.536268",
                        "Signer.Department в смс не совпадает с department.localId.code из КРЕМД"
                );
            }
        }
    }

    @Issue(value = "TEL-603")
    @Link(name = "ТМС-1184", url = "https://team-1okm.testit.software/projects/5/tests/1184?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправка смс в РЭМД ")
    @DisplayName("Заполнение блока подразделения в запросе к РРЭМД vmcl=1")
    @Test
    public void Access_603Vmcl_1() throws InterruptedException, SQLException, IOException {
        Access_603Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );

    }

    @DisplayName("Заполнение блока подразделения в запросе к РРЭМД vmcl=2")
    @Test
    public void Access_603Vmcl_2() throws InterruptedException, SQLException, IOException {
        Access_603Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );

    }

    @DisplayName("Заполнение блока подразделения в запросе к РРЭМД vmcl=3")
    @Test
    public void Access_603Vmcl_3() throws InterruptedException, SQLException, IOException {
        Access_603Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );

    }

    @DisplayName("Заполнение блока подразделения в запросе к РРЭМД vmcl=4")
    @Test
    public void Access_603Vmcl_4() throws InterruptedException, SQLException, IOException {
        Access_603Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );

    }

    @DisplayName("Заполнение блока подразделения в запросе к РРЭМД vmcl=99")
    @Test
    public void Access_603Vmcl_99() throws InterruptedException, SQLException, IOException {
        Access_603Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");

    }
}
