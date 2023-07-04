package api;

import UI.SQL;
import UI.TestListener;
import io.qameta.allure.*;
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
@Feature("Формирование запроса к КРЭМД")
public class Access_1009Test extends BaseAPI {
    SQL sql;
    public String value_1009;
    public String URLRemd;

    public void Access_1009VimisMethod(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd
    ) throws SQLException, IOException, InterruptedException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем запрос смс " + DocType + " с vmlc=" + vmcl + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            if (vmcl == 1 || vmcl == 2 || vmcl == 3 || vmcl == 4) {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value_1009 = sql.resultSet.getString("id");
                    System.out.println(value_1009);
                }
                System.out.println("Устанавливаем status = 1 в " + logs + "");
                sql.UpdateConnection(
                        "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + value_1009 + "', '" + UUID.randomUUID() + "')");
                System.out.println("Устанавливаем status = 1 в " + remd + "");
                sql.UpdateConnection(
                        "insert into " + remd + " (sms_id, local_uid, status, created_datetime, fremd_status) values (" + value_1009 + ", '" + xml.uuid + "', 'success', '" + Date + " 00:00:00.888 +0500', NULL);");
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
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(URLRemd)
                    .prettyPeek()
                    .body()
                    .jsonPath();
            if (vmcl == 99 | DocType == "34" | DocType == "35") {
                if (SmdToMinio == 1) {
                    Assertions.assertEquals(
                            response.getString("result[0].documentDto.patient.localId"),
                            "" + PatientGuid.toLowerCase() + ""
                    );
                } else {
                    Assertions.assertEquals(
                            response.getString("result[0].patient.localId"), "" + PatientGuid.toLowerCase() + "");
                }
            } else {
                if (SmdToMinio == 1) {
                    Assertions.assertEquals(
                            response.getString("result[0].documentDto.patient.localId"), "" + PatientGuid + "");
                } else {
                    Assertions.assertEquals(response.getString("result[0].patient.localId"), "" + PatientGuid + "");
                }
            }
            System.out.println("В localId отображается гуид пациента " + PatientGuid + " \n ");
        }
    }

    @Issue(value = "TEL-1009")
    @Link(name = "ТМС-1348", url = "https://team-1okm.testit.software/projects/5/tests/1348?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование запроса к КРЭМД vmcl=1")
    @Description("Отправить смс в ВИМИС и РЭМД, добавить статус 1 в logs, Добавить данную смс в таблицу vimis.remd_onko_sent_result, vimis.remd_cvd_sent_result, vimis.remd_akineo_sent_result, vimis.remd_prevention_sent_result (в зависимости он указанного направления ) и указать local_uid отправленной смс. Отправить запрос api/rremd")
    public void Access_1009Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД vmcl=2")
    public void Access_1009Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД vmcl=3")
    public void Access_1009Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД vmcl=4")
    public void Access_1009Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД смс32 vmcl=99")
    public void Access_1009Id_32Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/id=32-vmcl=99.xml", "32", 99, 1, true, 2, 6, 4, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД смс36 vmcl=99")
    public void Access_1009Id_36Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/id=36-vmcl=99.xml", "36", 99, 1, true, 2, 6, 4, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД с id=34 vmcl=99")
    public void Access_1009Id_34Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMSV19-id=34.xml", "34", 99, 1, true, 2, 6, 4, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД id=34 vmcl=3")
    public void Access_1009Id_34Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMSV19-id=34.xml", "34", 3, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД id=35 vmcl=3")
    public void Access_1009Id_35Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/id=35.xml", "35", 3, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД с id=35 vmcl=99")
    public void Access_1009Id_35Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/id=35.xml", "35", 99, 1, true, 2, 6, 4, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД с id=35 vmcl=99")
    public void Access_1009Id_15Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/id=15-vmcl=99.xml", "15", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }
}
