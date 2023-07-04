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

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Отправка смс с пациентом без снилс")
public class Access_1313Test extends BaseAPI {
    public String URLRemd;

    public void Access_1313Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd
    ) throws IOException, InterruptedException, SQLException {
        PatientGuid = "c89bfaeb-732e-4d0e-a519-559743166fca";
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем запрос смс3 с vmlc=" + vmcl + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            if (vmcl != 99) {
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
            } else {
                sql.StartConnection(
                        "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    System.out.println(sql.value);
                }
            }
            if (KingNumber == 1) {
                URLRemd = "http://192.168.2.126:1108/api/rremd/" + xml.uuid + "?CreationDateBegin=" + Date + "";
            }
            if (KingNumber == 2) {
                URLRemd = "http://192.168.2.126:1131/api/rremd/" + xml.uuid + "?CreationDateBegin=" + Date + "";
            }
            if (KingNumber == 4) {
                URLRemd = "http://192.168.2.21:34154/api/rremd/" + xml.uuid + "?CreationDateBegin=" + Date + "";
            }
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(URLRemd)
                    .body()
                    .jsonPath();
            if (SmdToMinio == 0) {
                Assertions.assertEquals(response.get("result[0].localUid"), "" + xml.uuid + "", "localUid не совпадет");
                Assertions.assertNull(response.get("result[0].patient.snils"), "snils не null");
            } else {
                Assertions.assertEquals(
                        response.get("result[0].documentDto.localUid"), "" + xml.uuid + "", "localUid не совпадет");
                Assertions.assertNull(response.get("result[0].documentDto.patient.snils"), "snils не null");
            }
        }
    }

    @Issue(value = "TEL-1313")
    @Link(name = "ТМС-1478", url = "https://team-1okm.testit.software/projects/5/tests/1478?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка смс с пациентом без снилс для vmcl = 1")
    @Description("Отправляем смс с пациентом без снилс и проверяем что в РЭМД идёт отправка без снилс")
    public void Access_1313Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1313Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @Test
    @DisplayName("Отправка смс с пациентом без снилс для vmcl = 2")
    public void Access_1113Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1313Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );
    }

    @Test
    @DisplayName("Отправка смс с пациентом без снилс для vmcl = 3")
    public void Access_1113Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1313Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Отправка смс с пациентом без снилс для vmcl = 4")
    public void Access_1113Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1313Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );
    }

    @Test
    @DisplayName("Отправка смс с пациентом без снилс для vmcl = 99")
    public void Access_1113Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1313Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");
    }
}
