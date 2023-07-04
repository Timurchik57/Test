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
@Feature("Проверка метода api/smd с параметром status")
public class Access_1595Test extends BaseAPI {
    public String URLRemd;
    public String TransferId;

    public void Access_1595Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd
    ) throws IOException, InterruptedException, SQLException {
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем запрос смс 3 с vmlc = " + vmcl + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            if (vmcl != 99) {
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

            } else {
                sql.StartConnection(
                        "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    TransferId = sql.resultSet.getString("transfer_id");
                    System.out.println(sql.value);
                }
                sql.UpdateConnection(
                        "update " + remd + " set fremd_status = '1' where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            }
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddress + "/api/smd/?transferId=" + TransferId + "&status=1")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            Assertions.assertEquals(response.get("result[0].localUid"), "" + xml.uuid + "", "localUid не совпадет");
            JsonPath respons = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddress + "/api/smd/?transferId=" + TransferId + "&status=0")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            Assertions.assertEquals(
                    respons.get("errorMessage"), "СМС с указанными параметрами не найдены",
                    "Не выходит ошибка 'СМС с указанными параметрами не найдены'"
            );
            if (vmcl != 99) {
                sql.UpdateConnection("update " + logs + " set status = '0' where sms_id  = '" + sql.value + "';");
            } else {
                sql.UpdateConnection(
                        "update " + remd + " set fremd_status = '0' where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            }
            response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddress + "/api/smd/?transferId=" + TransferId + "&status=1")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            Assertions.assertEquals(
                    response.get("errorMessage"), "СМС с указанными параметрами не найдены",
                    "Не выходит ошибка 'СМС с указанными параметрами не найдены'"
            );
            respons = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddress + "/api/smd/?transferId=" + TransferId + "&status=0")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            Assertions.assertEquals(respons.get("result[0].localUid"), "" + xml.uuid + "", "localUid не совпадет");
        }
    }

    @Issue(value = "TEL-1547")
    @Link(name = "ТМС-1568", url = "https://team-1okm.testit.software/projects/5/tests/1568?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка метода api/smd с параметром status для vmcl = 1")
    @Description("Отправляем смс для ВИМИС добавляем статус в таблицы logs, для РЭМД в таблицы remd, после ищем данные смс с дополнителным параметром status")
    public void Access_1547Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1595Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @Test
    @DisplayName("Проверка метода api/smd с параметром status для vmcl = 2")
    public void Access_1547Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1595Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );
    }

    @Test
    @DisplayName("Проверка метода api/smd с параметром status для vmcl = 3")
    public void Access_1547Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1595Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Проверка метода api/smd с параметром status для vmcl = 4")
    public void Access_1547Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1595Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );
    }

    @Test
    @DisplayName("Проверка метода api/smd с параметром status для vmcl = 99")
    public void Access_1547Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1595Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");
    }
}
