package api;

import UI.SQL;
import UI.TestListener;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.StringUtils;
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
@Feature("Поиск смс по параметрам api/smd")
public class Access_1727Test extends BaseAPI {
    public String LocallUid;
    public UUID UuidEmd_id;
    public UUID UuidRequestId;
    public String TransferId;

    @Step("Отправляем запрос методом api/smd")
    public void ApiSmdMethod(String request, String sms, String local_uid, String info) throws SQLException {
        JsonPath response = given()
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .get(HostAddress + "/api/smd/?" + request)
                .prettyPeek()
                .body()
                .jsonPath();
        Assertions.assertEquals(response.get("result[0].localUid"), LocallUid,
                                "Смс не найдена по пареметру " + request + "");

        sql.StartConnection("Select * from " + sms + " where local_uid = '" + local_uid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        sql.StartConnection("Select * from " + info + " where smsid = " + sql.value + ";");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("effectivetime");
        }
        String str = response.get("result[0].createDate");
        Assertions.assertEquals(StringUtils.substring(str, 0, 10), StringUtils.substring(sql.value, 0, 10),
                                "Не совпадает createDate");
    }

    @Step("Отправляем смс с Doctype = {1} и устанавливаем в бд нужные значения")
    public void Access_1727Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd, String Info) throws IOException, InterruptedException, SQLException {
        sql = new SQL();

        System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                   position1,
                   speciality1);
        LocallUid = String.valueOf(xml.uuid);
        if (vmcl == 99) {
            sql.StartConnection(
                    "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                TransferId = sql.resultSet.getString("transfer_id");
                System.out.println(sql.value);
            }
            UuidEmd_id = UUID.randomUUID();
            sql.UpdateConnection(
                    "update " + remd + " set emd_id = '" + UuidEmd_id + "' where id = '" + sql.value + "';");

        } else {
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                TransferId = sql.resultSet.getString("transfer_id");
                System.out.println(sql.value);
            }
            System.out.println("Устанавливаем status = 1 в " + logs + "");
            sql.UpdateConnection(
                    "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + sql.value + "', '" + UUID.randomUUID() + "')");
            System.out.println("Устанавливаем status = 1 в " + remd + "");
            sql.UpdateConnection(
                    "insert into " + remd + " (sms_id, local_uid, status, created_datetime, fremd_status) values (" + sql.value + ", '" + xml.uuid + "', 'success', '" + Date + " 00:00:00.888 +0500', NULL);");
            UuidEmd_id = UUID.randomUUID();
            sql.UpdateConnection(
                    "update " + remd + " set emd_id = '" + UuidEmd_id + "' where sms_id = '" + sql.value + "';");
            UuidRequestId = UUID.randomUUID();
            sql.UpdateConnection(
                    "update " + sms + " set request_id = '" + UuidRequestId + "' where id = '" + sql.value + "';");
        }
        System.out.println("Ищем по localUid");
        ApiSmdMethod("localUid=" + xml.uuid + "", sms, LocallUid, Info);
        System.out.println("Ищем по emdId");
        ApiSmdMethod("emdId=" + UuidEmd_id + "", sms, LocallUid, Info);
        if (vmcl != 99) {
            System.out.println("Ищем по requestId");
            ApiSmdMethod("requestId=" + UuidRequestId + "", sms, LocallUid, Info);
        }
        System.out.println("Ищем по TransferId");
        ApiSmdMethod("TransferId=" + TransferId + "", sms, LocallUid, Info);
        ApiSmdMethod("TransferId=" + TransferId + "", sms, LocallUid, Info);
    }

    @Issue(value = "TEL-1727")
    @Issue(value = "TEL-1720")
    @Issue(value = "TEL-1713")
    @Link(name = "ТМС-1754", url = "https://team-1okm.testit.software/projects/5/tests/1754?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Поиск смс по параметрам api/smd vmcl = 1")
    @Description("Отправляем смс по необходимости добовляем нужные параметры в бд - осеществляем поиск смс по этому пареметру")
    public void Access_1727Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1727Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result", "vimis.additionalinfo");
    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=2")
    @Test
    public void Access_1727Vmcl_2() throws InterruptedException, SQLException, IOException {
        Access_1727Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", "vimis.preventionadditionalinfo");

    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=3")
    @Test
    public void Access_1727Vmcl_3() throws InterruptedException, SQLException, IOException {
        Access_1727Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result", "vimis.akineoadditionalinfo");

    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=4")
    @Test
    public void Access_1727Vmcl_4() throws InterruptedException, SQLException, IOException {
        Access_1727Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result", "vimis.cvdadditionalinfo");

    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=99")
    @Test
    public void Access_1727Vmcl_99() throws InterruptedException, SQLException, IOException {
        Access_1727Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.remd_sent_result", "",
                          "vimis.remd_sent_result",
                          "vimis.remdadditionalinfo");
    }
}
