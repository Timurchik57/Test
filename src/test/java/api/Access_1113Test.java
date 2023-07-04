package api;

import UI.SQL;
import UI.TestListener;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Получение данных об смс переданных в КРЭМД с рандомным local_uid в remd")
public class Access_1113Test extends BaseAPI {
    SQL sql;
    public String value1113;
    public String URLRemd;
    public String rundom;

    public void Access_1113Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем запрос смс3 с vmlc=" + vmcl + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            if (vmcl == 1 || vmcl == 2 || vmcl == 3 || vmcl == 4) {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value1113 = sql.resultSet.getString("id");
                    System.out.println(value1113);
                }
                System.out.println("Устанавливаем status = 1 в " + logs + "");
                sql.UpdateConnection(
                        "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + value1113 + "', '" + UUID.randomUUID() + "')");
                rundom = String.valueOf(UUID.randomUUID());
                System.out.println("Устанавливаем status = 1 в " + remd + "");
                sql.UpdateConnection(
                        "insert into " + remd + " (sms_id, local_uid, status, created_datetime, fremd_status) values (" + value1113 + ", '" + rundom + "', 'success', '" + Date + " 00:00:00.888 +0500', NULL);");
            }
            if (KingNumber == 1) {
                URLRemd = "http://192.168.2.126:1108/api/rremd/" + rundom + "?CreationDateBegin=" + Date + "";
            }
            if (KingNumber == 2) {
                URLRemd = "http://192.168.2.126:1131/api/rremd/" + rundom + "?CreationDateBegin=" + Date + "";
            }
            if (KingNumber == 4) {
                URLRemd = "http://192.168.2.21:34154/api/rremd/" + rundom + "?CreationDateBegin=" + Date + "";
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
                assertThat(response.get("result[0].localUid"), equalTo("" + xml.uuid + ""));
            } else {
                assertThat(response.get("result[0].documentDto.localUid"), equalTo("" + xml.uuid + ""));
            }

        }
    }

    @Issue(value = "TEL-1113")
    @Link(name = "ТМС-1376", url = "https://team-1okm.testit.software/projects/5/tests/1376?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Получение данных об смс переданных в КРЭМД с рандомным local_uid в remd для смс с vmcl = 1 id = 3")
    @Description("Отправить смс в ВИМИС и РЭМД, добавить статус 1 в logs, Добавить данную смс в таблицу vimis.remd_onko_sent_result, vimis.remd_cvd_sent_result, vimis.remd_akineo_sent_result, vimis.remd_prevention_sent_result (в зависимости он указанного направления ) и указать другой local_uid. Отправить запрос api/rremd с другим local_uid и проверить, что в ответе отображается local_uid отправленной смс")
    public void Access_1113Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1113Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @Test
    @DisplayName("Получение данных об смс переданных в КРЭМД с рандомным local_uid в remd для смс с vmcl = 2 id = 3")
    public void Access_1113Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1113Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );
    }

    @Test
    @DisplayName("Получение данных об смс переданных в КРЭМД с рандомным local_uid в remd для смс с vmcl = 3 id = 3")
    public void Access_1113Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1113Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Получение данных об смс переданных в КРЭМД с рандомным local_uid в remd для смс с vmcl = 4 id = 3")
    public void Access_1113Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1113Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );
    }
}
