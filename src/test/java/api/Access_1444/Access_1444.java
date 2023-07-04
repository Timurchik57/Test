package api.Access_1444;

import UI.TestListener;
import api.BaseAPI;
import api.TestListenerApi;
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
@Feature("Сохранение файлов в minio при отправке смс")
public class Access_1444 extends BaseAPI {
    public String URLRemd;
    public String minio_document_name;

    /**
     * Отправка vmcl = 1/2/3/4 в ВИМИС
     */
    public void Access_1444Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd, Integer SmdToMinio
    ) throws IOException, InterruptedException, SQLException {
        if (KingNumber == 2) {
            System.out.println("Отправляем запрос смс " + DocType + " с vmlc=" + vmcl + "");
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
                    String Local_Uid = sql.resultSet.getString("local_uid");
                    String document = sql.resultSet.getString("document");
                    minio_document_name = sql.resultSet.getString("minio_document_name");
                    if (SmdToMinio == 1) {
                        if (vmcl == 1 & FileName != "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis1", Local_Uid);
                        }
                        if (vmcl == 2 & FileName != "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis2", Local_Uid);
                        }
                        if (vmcl == 3 & FileName != "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis3", Local_Uid);
                        }
                        if (vmcl == 4 & FileName != "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis4", Local_Uid);
                        }
                        if (vmcl == 1 & FileName == "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd1", Local_Uid);
                        }
                        if (vmcl == 2 & FileName == "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd2", Local_Uid);
                        }
                        if (vmcl == 3 & FileName == "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd3", Local_Uid);
                        }
                        if (vmcl == 4 & FileName == "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd4", Local_Uid);
                        }
                        Assertions.assertNull(document, "При SmdToMinio = 1, поле document заполнено");
                        Assertions.assertNotEquals(
                                minio_document_name, null, "При SmdToMinio = 1, поле minio_document_name не заполнено");
                    }
                    if (SmdToMinio == 0) {
                        if (vmcl == 1 & FileName != "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis1_2", Local_Uid);
                        }
                        if (vmcl == 2 & FileName != "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis2_2", Local_Uid);
                        }
                        if (vmcl == 3 & FileName != "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis3_2", Local_Uid);
                        }
                        if (vmcl == 4 & FileName != "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis4_2", Local_Uid);
                        }
                        if (vmcl == 1 & FileName == "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd1_2", Local_Uid);
                        }
                        if (vmcl == 2 & FileName == "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd2_2", Local_Uid);
                        }
                        if (vmcl == 3 & FileName == "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd3_2", Local_Uid);
                        }
                        if (vmcl == 4 & FileName == "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd4_2", Local_Uid);
                        }
                        Assertions.assertNotEquals(document, null, "При SmdToMinio = 1, поле document заполнено");
                        Assertions.assertNull(
                                minio_document_name,
                                "При SmdToMinio = 1, поле minio_document_name не заполнено"
                        );
                    }

                }
                if (FileName == "SMS/SMS3.xml") {
                    System.out.println("Устанавливаем status = 1 в " + logs + "");
                    sql.UpdateConnection(
                            "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + sql.value + "', '" + UUID.randomUUID() + "')");
                    System.out.println("Устанавливаем status = 1 в " + remd + "");
                    sql.UpdateConnection(
                            "insert into " + remd + " (sms_id, local_uid, status, created_datetime, fremd_status) values (" + sql.value + ", '" + xml.uuid + "', 'success', '" + Date + " 00:00:00.888 +0500', NULL);");

                }
            }
            if (vmcl == 99) {
                sql.StartConnection(
                        "SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    String Local_Uid = sql.resultSet.getString("local_uid");
                    String document = sql.resultSet.getString("document");
                    minio_document_name = sql.resultSet.getString("minio_document_name");
                    System.out.println(sql.value);
                    if (SmdToMinio == 1) {
                        if (FileName == "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdRemd", Local_Uid);
                        }
                        if (FileName == "SMS/id=101-vmcl=99.txt") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdCertRemd", Local_Uid);
                        }
                        Assertions.assertNull(document, "При SmdToMinio = 1, поле document заполнено");
                        Assertions.assertNotEquals(
                                minio_document_name, null, "При SmdToMinio = 1, поле minio_document_name не заполнено");
                    }
                    if (SmdToMinio == 0) {
                        if (FileName == "SMS/SMS3.xml") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdRemd_2", Local_Uid);
                        }
                        if (FileName == "SMS/id=101-vmcl=99.txt") {
                            InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdCertRemd_2", Local_Uid);
                        }
                        Assertions.assertNotEquals(document, null, "При SmdToMinio = 0, поле document не заполнено");
                        Assertions.assertNull(
                                minio_document_name,
                                "При SmdToMinio = 0, поле minio_document_name заполнено"
                        );
                    }
                }
            }
            if (FileName != "SMS/id=101-vmcl=99.txt") {
                Thread.sleep(5000);
                JsonPath response = given()
                        .filter(new AllureRestAssured())
                        .header("Authorization", "Bearer " + Token)
                        .contentType(ContentType.JSON)
                        .when()
                        .get(HostAddress + "/api/smd/document?LocalUid=" + xml.uuid)
                        .body()
                        .jsonPath();
                Assertions.assertEquals(
                        response.get("result[0].localUid"), "" + xml.uuid + "", "Local_uid не совпадает");
                Assertions.assertNotNull(response.get("result[0].document"), "document отсутствует");
                Assertions.assertNull(
                        response.get("result[0].objectStorageDocumentName"), "ObjectStorageDocumentName отсутствует");
            }
            if (vmcl == 99 | FileName == "SMS/SMS3.xml") {
                Thread.sleep(5000);
                URLRemd = "http://192.168.2.126:1131/api/rremd/" + xml.uuid + "?CreationDateBegin=" + Date + "";
                JsonPath respons = given()
                        .header("Authorization", "Bearer " + Token)
                        .contentType(ContentType.JSON)
                        .when()
                        .get(URLRemd)
                        .body()
                        .jsonPath();
                if (SmdToMinio == 1) {
                    Assertions.assertEquals(
                            respons.get("result[0].documentDto.localUid"), "" + xml.uuid + "",
                            "Local_uid не совпадает"
                    );
                    Assertions.assertEquals(
                            respons.get("result[0].objectStorageDocumentName"), "" + minio_document_name + "",
                            "minio_document_name не совпадает"
                    );
                }
                if (SmdToMinio == 0) {
                    Assertions.assertEquals(
                            respons.get("result[0].localUid"), "" + xml.uuid + "", "Local_uid не совпадает");
                    Assertions.assertNull(
                            respons.get("result[0].objectStorageDocumentName"),
                            "objectStorageDocumentName присутствует"
                    );
                }
            }
        }
    }

    @Issue(value = "TEL-1444")
    @Issue(value = "TEL-1443")
    @Issue(value = "TEL-1457")
    @Link(name = "ТМС-1553", url = "https://team-1okm.testit.software/projects/5/tests/1553?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Description("Сохранение файлов в minio при отправке смс")
    @DisplayName("Отправка смс с vmcl=1 в ВИМИС")
    @Test
    public void Access_1444_vmcl1234Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "", "", 1);
    }

    @DisplayName("Отправка смс с vmcl=2 в ВИМИС")
    @Test
    public void Access_1444_vmcl2Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS2-id=42.xml", "42", 2, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms", "", "", 1);
    }

    @DisplayName("Отправка смс с vmcl=3 в ВИМИС")
    @Test
    public void Access_1444_vmcl3Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 3, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "", "", 1);
    }

    @DisplayName("Отправка смс с vmcl=4 в ВИМИС")
    @Test
    public void Access_1444_vmcl4Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 4, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.cvdsms", "", "", 1);
    }

    @DisplayName("Отправка смс с vmcl=99 ")
    @Test
    public void Access_1444_vmcl99() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 99, 1, true, 3, 1, 9, 18, 1, 57, 21, "", "", "vimis.remd_sent_result", 1);
    }

    @DisplayName("Отправка смс с vmcl=99 pdf")
    @Test
    public void Access_1444_vmcl99Pdf() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/id=101-vmcl=99.txt", "101", 99, 1, true, 3, 1, 57, 18, 1, 57, 21, "", "",
                "vimis.certificate_remd_sent_result", 1
        );
    }

    @DisplayName("Отправка смс с vmcl=1 в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl1VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result", 1
        );

    }

    @DisplayName("Отправка смс с vmcl=2 в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl2VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 1
        );
    }

    @DisplayName("Отправка смс с vmcl=3 в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl3VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result", 1
        );

    }

    @DisplayName("Отправка смс с vmcl=4 в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl4VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result", 1
        );

    }

    @DisplayName("Отправка смс с vmcl=1 в ВИМИС")
    @Test
    public void Access_1444_vmcl1234Vimis0() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "", "", 0);
    }

    @DisplayName("Отправка смс с vmcl=2 в ВИМИС")
    @Test
    public void Access_1444_vmcl2Vimis0() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS2-id=42.xml", "42", 2, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms", "", "", 0);
    }

    @DisplayName("Отправка смс с vmcl=3 в ВИМИС")
    @Test
    public void Access_1444_vmcl3Vimis0() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 3, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "", "", 0);
    }

    @DisplayName("Отправка смс с vmcl=4 в ВИМИС")
    @Test
    public void Access_1444_vmcl4Vimis0() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 4, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.cvdsms", "", "", 0);
    }

    @DisplayName("Отправка смс с vmcl=99 ")
    @Test
    public void Access_1444_vmcl990() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 99, 1, true, 3, 1, 9, 18, 1, 57, 21, "", "", "vimis.remd_sent_result", 0);
    }

    @DisplayName("Отправка смс с vmcl=99 pdf")
    @Test
    public void Access_1444_vmcl990Pdf() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/id=101-vmcl=99.txt", "101", 99, 1, true, 3, 1, 57, 18, 1, 57, 21, "", "",
                "vimis.certificate_remd_sent_result", 0
        );
    }

    @DisplayName("Отправка смс с vmcl=1 в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl1VimisRemd0() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result", 0
        );

    }

    @DisplayName("Отправка смс с vmcl=2 в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl2VimisRemd0() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 0
        );
    }

    @DisplayName("Отправка смс с vmcl=3 в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl3VimisRemd0() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result", 0
        );

    }

    @DisplayName("Отправка смс с vmcl=4 в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl4VimisRemd0() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result", 0
        );
    }
}
