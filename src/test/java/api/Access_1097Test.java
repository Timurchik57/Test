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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Передача информации о снилс получателя в КРЭМД")
public class Access_1097Test extends BaseAPI {
    SQL sql;
    public String value1096;
    public String URLKremd;

    public void Access_1097Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String remd, String info, String logs
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
                    value1096 = sql.resultSet.getString("id");
                    System.out.println(value1096);
                }
            } else {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value1096 = sql.resultSet.getString("id");
                    System.out.println(value1096);
                }
            }
            Thread.sleep(1500);
            System.out.println("Проверяем добавление значений в таблице " + info + ".recipient_snils");
            sql.StartConnection("Select * from " + info + " where smsid = '" + value1096 + "';");
            while (sql.resultSet.next()) {
                String recipient_snils = sql.resultSet.getString("recipient_snils");
                String recipient_mo = sql.resultSet.getString("recipient_mo");
                if (FileName == "SMS/id=15-vmcl=99.xml") {
                    Assertions.assertEquals(recipient_snils, "19591054097", "Значение recipient_snils не совпадает");
                    Assertions.assertEquals(
                            recipient_mo, "1.2.643.5.1.13.13.12.1.77.87", "Значение recipient_mo не совпадает");
                }
                if (FileName == "SMS/SMSV19-id=34.xml" | FileName == "SMS/id=36-vmcl=99.xml") {
                    Assertions.assertEquals(recipient_snils, "15979025720", "Значение recipient_snils не совпадает");
                    Assertions.assertEquals(
                            recipient_mo, "1.2.643.5.1.13.13.12.1.77.87", "Значение recipient_mo не совпадает");
                }
                if (FileName == "SMS/SMS3.xml" | FileName == "SMS/id=33-vmcl=1.xml") {
                    Assertions.assertNull(recipient_snils, "Значение recipient_snils не совпадает");
                    Assertions.assertEquals(
                            recipient_mo, "1.2.643.5.1.13.13.12.1.77.87", "Значение recipient_mo не совпадает");
                }
            }
            if (vmcl != 99) {
                System.out.println("Устанавливаем status = 1 в " + logs + "");
                sql.UpdateConnection(
                        "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + value1096 + "', '" + UUID.randomUUID() + "')");
                System.out.println("Устанавливаем status = 1 в " + remd + "");
                sql.UpdateConnection(
                        "insert into " + remd + " (sms_id, local_uid, status, created_datetime, fremd_status) values (" + value1096 + ", '" + xml.uuid + "', 'success', '" + Date + " 00:00:00.888 +0500', NULL);");
            }
            if (KingNumber == 1) {
                URLKremd = "http://192.168.2.126:1108/api/rremd/";
            }
            if (KingNumber == 2) {
                URLKremd = "http://192.168.2.126:1131/api/rremd/";
            }
            if (KingNumber == 4) {
                URLKremd = "http://192.168.2.21:34154/api/rremd/";
            }
            Thread.sleep(1500);
            JsonPath response = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(URLKremd + "" + xml.uuid + "")
                    .body()
                    .jsonPath();
            if (SmdToMinio == 0) {
                if (FileName == "SMS/id=15-vmcl=99.xml" | FileName == "SMS/SMS3.xml") {
                    assertThat(response.get("result[0].recipient"), equalTo(null));
                }
                if (FileName == "SMS/id=33-vmcl=1.xml") {
                    assertThat(response.get("result[0].recipient.kind.code"), equalTo("MEDICAL_ORGANIZATION"));
                    assertThat(response.get("result[0].recipient.recipientKindPerson.snils"), equalTo(null));
                    assertThat(
                            response.get("result[0].recipient.recipientKindMedicalOrganization.organization.code"),
                            equalTo("1.2.643.5.1.13.13.12.1.77.87")
                    );
                }
                if (FileName == "SMS/SMSV19-id=34.xml" | FileName == "SMS/id=36-id=99.xml") {
                    assertThat(response.get("result[0].recipient.kind.code"), equalTo("PATIENT_REPRESENTATIVE"));
                    assertThat(response.get("result[0].recipient.recipientKindPerson.snils"), equalTo("15979025720"));
                }
            } else {
                if (FileName == "SMS/id=15-vmcl=99.xml" | FileName == "SMS/SMS3.xml") {
                    assertThat(response.get("result[0].documentDto.recipient"), equalTo(null));
                }
                if (FileName == "SMS/id=33-vmcl=1.xml") {
                    assertThat(
                            response.get("result[0].documentDto.recipient.kind.code"), equalTo("MEDICAL_ORGANIZATION"));
                    assertThat(
                            response.get("result[0].documentDto.recipient.recipientKindPerson.snils"), equalTo(null));
                    assertThat(
                            response.get(
                                    "result[0].documentDto.recipient.recipientKindMedicalOrganization.organization.code"),
                            equalTo("1.2.643.5.1.13.13.12.1.77.87")
                    );
                }
                if (FileName == "SMS/SMSV19-id=34.xml" | FileName == "SMS/id=36-id=99.xml") {
                    assertThat(
                            response.get("result[0].documentDto.recipient.kind.code"),
                            equalTo("PATIENT_REPRESENTATIVE")
                    );
                    assertThat(
                            response.get("result[0].documentDto.recipient.recipientKindPerson.snils"),
                            equalTo("15979025720")
                    );
                }
            }
        }
    }

    @Issue(value = "TEL-1097")
    @Issue(value = "TEL-1080")
    @Issue(value = "TEL-1120")
    @Issue(value = "TEL-1668")
    @Link(name = "ТМС-1376", url = "https://team-1okm.testit.software/projects/5/tests/1376?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Link(name = "ТМС-1731", url = "https://team-1okm.testit.software/projects/5/tests/1731?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Передача информации о снилс получателя в КРЭМД для vmcl = 1")
    @Description("Отправляем смс с блоком IRCP, после проверяем таблицу vimis.cvd/akineo/prevention/remd/additionalinfo.recipient_snils на заполненное поле. Для смс в ВИМИС устанавливаем статус 1 в logs и добавляем запись в remd, после используем запрос api/rremd и проверяем передалось ли значение snils. По 1120 заявке проверяем, что отображение блока recipient в запросе к КРЭМД будет только у смс с id=33/34/36")
    public void Access_1097Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/id=15-vmcl=99.xml", "15", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms",
                "vimis.remd_onko_sent_result", "vimis.additionalinfo", "vimis.documentlogs"
        );
    }

    @Test
    @DisplayName("Есть снилс, но поля recipient не будет для id = 15 vmcl = 99")
    public void Access_1097Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/id=15-vmcl=99.xml", "15", 99, 1, true, 1, 6, 4, 18, 1, 57, 21, "", "vimis.remd_sent_result",
                "vimis.remdadditionalinfo", ""
        );
    }

    @Test
    @DisplayName("Есть снилс, но поля recipient не будет для vmcl = 1 id = 3")
    public void Access_1097Vmcl_1Id_3() throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.remd_onko_sent_result",
                "vimis.additionalinfo", "vimis.documentlogs"
        );
    }

    @Test
    @DisplayName("Нет снилс, есть поле recipient для vmcl = 1 id = 33")
    public void Access_1097Vmcl_1Id_33() throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/id=33-vmcl=1.xml", "33", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms",
                "vimis.remd_onko_sent_result", "vimis.additionalinfo", "vimis.documentlogs"
        );
    }

    @Test
    @DisplayName("Нет снилс, есть поле recipient для vmcl = 99 id = 33")
    public void Access_1097Vmcl_99Id_33() throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/id=33-vmcl=1.xml", "33", 99, 1, true, 3, 6, 4, 18, 1, 57, 21, "", "vimis.remd_sent_result",
                "vimis.remdadditionalinfo", ""
        );
    }

    @Test
    @DisplayName("Есть снилс, есть поле recipient для vmcl = 99 id = 34")
    public void Access_1097Vmcl_99Id_34() throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/SMSV19-id=34.xml", "34", 99, 1, true, 3, 6, 4, 18, 1, 57, 21, "", "vimis.remd_sent_result",
                "vimis.remdadditionalinfo", ""
        );
    }

    @Test
    @DisplayName("Есть снилс, есть поле recipient для vmcl = 3 id = 34")
    public void Access_1097Vmcl_3Id_34() throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/SMSV19-id=34.xml", "34", 3, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.akineosms",
                "vimis.remd_akineo_sent_result", "vimis.akineoadditionalinfo", "vimis.akineologs"
        );
    }

    @Test
    @DisplayName("Передача информации о снилс получателя в КРЭМД для vmcl = 99 id = 36")
    public void Access_1097Vmcl_99Id_36() throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/id=36-vmcl=99.xml", "36", 99, 1, true, 3, 6, 4, 18, 1, 57, 21, "", "vimis.remd_sent_result",
                "vimis.remdadditionalinfo", ""
        );
    }
}
