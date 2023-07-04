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

import static io.restassured.RestAssured.given;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Хранение серии и номера документа из СЭМД")
public class Access_1071Test extends BaseAPI {
    SQL sql;
    public String value1071;
    public String URLKremd;

    public void Access_1071Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String remd, String info
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем смс с Doctype = " + DocType + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            sql.StartConnection(
                    "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value1071 = sql.resultSet.getString("id");
                System.out.println(value1071);
            }
            System.out.println("Проверяем добавление значений в таблице " + info + "");
            sql.StartConnection("Select * from " + info + " where smsid = '" + value1071 + "';");
            while (sql.resultSet.next()) {
                String document_series = sql.resultSet.getString("document_series");
                String document_number = sql.resultSet.getString("document_number");
                if (FileName == "SMS/id=id=32-vmcl=99.xml") {
                    Assertions.assertEquals(document_series, "77АА", "Значение document_series не совпадает");
                    Assertions.assertEquals(document_number, "1234567891", "Значение document_number не совпадает");
                }
                if (FileName == "SMS/id=36-vmcl=99.xml") {
                    Assertions.assertEquals(document_series, "77АА", "Значение document_series не совпадает");
                    Assertions.assertEquals(document_number, "2234567891", "Значение document_number не совпадает");
                }
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
            if (FileName == "SMS/id=32-vmcl=99.xml") {
                if (SmdToMinio == 1) {
                    Assertions.assertEquals(
                            response.getString("result[0].documentDto.documentNumber"), "77АА 1234567891",
                            "Не добавилось значение documentNumber в запросе к КРЭМД"
                    );
                } else {
                    Assertions.assertEquals(
                            response.getString("result[0].documentNumber"), "77АА 1234567891",
                            "Не добавилось значение documentNumber в запросе к КРЭМД"
                    );
                }
            }
            if (FileName == "SMS/id=36-vmcl=99.xml") {
                if (SmdToMinio == 1) {
                    Assertions.assertEquals(
                            response.getString("result[0].documentDto.documentNumber"), "77АА 2234567891",
                            "Не добавилось значение documentNumber в запросе к КРЭМД"
                    );
                } else {
                    Assertions.assertEquals(
                            response.getString("result[0].documentNumber"), "77АА 2234567891",
                            "Не добавилось значение documentNumber в запросе к КРЭМД"
                    );
                }
            }
        }
    }

    @Issue(value = "TEL-1071")
    @Issue(value = "TEL-1080")
    @Link(name = "ТМС-1365", url = "https://team-1okm.testit.software/projects/5/tests/1365?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Хранение серии и номера документа из СЭМД для id=32 vmcl=99")
    @Description("Отправляем смс с document_series и documentNumber, после проверить методом /api/rremd отправку этих значений")
    public void Access_1071ID_32() throws IOException, SQLException, InterruptedException {
        Access_1071Method(
                "SMS/id=32-vmcl=99.xml", "32", 99, 1, true, 1, 6, 4, 18, 1, 57, 21, "vimis.remd_sent_result",
                "vimis.remdadditionalinfo"
        );
    }

    @Test
    @Issue(value = "TEL-1080")
    @DisplayName("Хранение серии и номера документа из СЭМД для id=36 vmcl=99")
    @Description("Отправляем смс со статусом position_control = не строгий/ не проверять. После отправляем со статусом = проверять")
    public void Access_1071ID_36() throws IOException, SQLException, InterruptedException {
        Access_1071Method(
                "SMS/id=36-vmcl=99.xml", "36", 99, 1, true, 1, 6, 4, 18, 1, 57, 21, "vimis.remd_sent_result",
                "vimis.remdadditionalinfo"
        );
    }
}
