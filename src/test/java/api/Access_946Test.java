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

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Передача ПДФ справок в КРЭМД")
public class Access_946Test extends BaseAPI {
    public String URLRemd;
    public String LocalUid;
    public String transfer_id;
    public String Doctype;
    public String SystemName;

    public void Access_946Method(
            String FileName, String DocType, Integer Role, Integer position, Integer speciality, Integer Role1,
            Integer position1, Integer speciality1
    ) throws IOException, InterruptedException, SQLException {
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Проверка по заявке 946 с смс = " + DocType + "");
            xml.ApiSmd(FileName, DocType, 99, 1, true, 2, Role, position, speciality, Role1, position1, speciality1);
            System.out.println("Берём название смс для id = " + DocType + "");
            sql.StartConnection("SELECT * FROM dpc.emd_types_additional where id = '" + DocType + "';");
            while (sql.resultSet.next()) {
                SystemName = sql.resultSet.getString("name");
            }
            System.out.println("Проверяем добавление в таблицу vimis.certificate_remd_sent_result");
            sql.StartConnection(
                    "Select * from vimis.certificate_remd_sent_result where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                LocalUid = sql.resultSet.getString("local_uid");
                Doctype = sql.resultSet.getString("doctype");
                transfer_id = sql.resultSet.getString("transfer_id");
                System.out.println(sql.value);
                Assertions.assertEquals(LocalUid, "" + xml.uuid + "", "LocalUid не совпадает");
                Assertions.assertEquals(Doctype, DocType, "Doctype не совпадает");
            }
            System.out.println("Проверяем тело отправки в РЭМД");
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
                assertThat(response.get("result[0].localUid"), equalTo("" + xml.uuid + ""));
                assertThat(response.get("result[0].kind.displayName"), equalTo("" + SystemName + ""));
                assertThat(response.get("result[0].kind.code"), equalTo("" + DocType + ""));
                assertThat(response.get("result[0].kind.codeSystem"), equalTo("remd0001"));
            } else {
                assertThat(response.get("result[0].documentDto.localUid"), equalTo("" + xml.uuid + ""));
                assertThat(response.get("result[0].documentDto.kind.displayName"), equalTo("" + SystemName + ""));
                assertThat(response.get("result[0].documentDto.kind.code"), equalTo("" + DocType + ""));
                assertThat(response.get("result[0].documentDto.kind.codeSystem"), equalTo("remd0001"));
            }
            sql.NotSQL(
                    "Select count(*) from vimis.remd_sent_result  where  created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        }
    }

    @Issue(value = "TEL-946")
    @Link(name = "ТМС-1476", url = "https://team-1okm.testit.software/projects/5/tests/1476?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Передача ПДФ справок в КРЭМД id=101")
    @Description("Отправляем смс 101/102/103/104/105 - проверяем добавление в таблицу vimis.certificate_remd_sent_result - проверяем тело отправки в РЭМД")
    public void Access_946ID_101() throws IOException, SQLException, InterruptedException {
        Access_946Method("SMS/id=101-vmcl=99.txt", "101", 1, 57, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Передача ПДФ справок в КРЭМД id=102")
    public void Access_946ID_102() throws IOException, SQLException, InterruptedException {
        Access_946Method("SMS/id=101-vmcl=99.txt", "102", 1, 57, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Передача ПДФ справок в КРЭМД id=103")
    public void Access_946ID_103() throws IOException, SQLException, InterruptedException {
        Access_946Method("SMS/id=101-vmcl=99.txt", "103", 1, 57, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Передача ПДФ справок в КРЭМД id=104")
    public void Access_946ID_104() throws IOException, SQLException, InterruptedException {
        Access_946Method("SMS/id=101-vmcl=99.txt", "104", 1, 57, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Передача ПДФ справок в КРЭМД id=105")
    public void Access_946ID_105() throws IOException, SQLException, InterruptedException {
        Access_946Method("SMS/id=101-vmcl=99.txt", "105", 1, 57, 18, 1, 57, 21);
    }
}
