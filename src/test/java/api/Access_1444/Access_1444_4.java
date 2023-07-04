package api.Access_1444;

import UI.TestListener;
import api.BaseAPI;
import api.TestListenerApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Проверка добавленных смс с SmdToMinio =1/0 методами /api/smd/document и /api/rremd/")
public class Access_1444_4 extends BaseAPI {
    public String URLRemd;
    public String minio_document_name;

    public void Access_1444_2Method(
            String FileName, Integer vmcl, String Local_Uid, Integer SmdToMinio, String sms, String remd
    ) throws InterruptedException, SQLException, IOException {
        FileInputStream in = new FileInputStream("src/test/java/api/Access_1444/1444.properties");
        props = new Properties();
        props.load(in);
        in.close();
        if (vmcl != 99) {
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + props.getProperty(
                            "" + Local_Uid + "") + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                minio_document_name = sql.resultSet.getString("minio_document_name");
            }
        }
        if (vmcl == 99) {
            sql.StartConnection(
                    "SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + props.getProperty(
                            "" + Local_Uid + "") + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                minio_document_name = sql.resultSet.getString("minio_document_name");
            }
        }
        Token = new String(Files.readAllBytes(Paths.get("AuthorizationToken.txt")));
        if (FileName != "SMS/id=101-vmcl=99.txt") {
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddress + "/api/smd/document?LocalUid=" + props.getProperty("" + Local_Uid + ""))
                    .body()
                    .jsonPath();
            Assertions.assertEquals(
                    response.get("result[0].localUid"), "" + props.getProperty("" + Local_Uid + "") + "",
                    "Local_uid не совпадает"
            );
            Assertions.assertNotNull(response.get("result[0].document"), "document отсутствует");
            Assertions.assertNull(
                    response.get("result[0].objectStorageDocumentName"), "objectStorageDocumentName отсутствует");
        }
        if (vmcl == 99 | FileName == "SMS/SMS3.xml" | FileName == "SMS/id=101-vmcl=99.txt") {
            URLRemd = "http://192.168.2.126:1131/api/rremd/" + props.getProperty("" + Local_Uid + "") + "";
            JsonPath respons = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(URLRemd)
                    .body()
                    .jsonPath();
            Assertions.assertEquals(
                    respons.get("result[0].localUid"), "" + props.getProperty("" + Local_Uid + "") + "",
                    "Local_uid не совпадает"
            );
            Assertions.assertNull(
                    respons.get("result[0].objectStorageDocumentName"), "objectStorageDocumentName присутствует");
        }
    }

    @DisplayName("Проверка смс с vmcl=1 в ВИМИС")
    @Test
    public void Access_1444_vmcl1Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS2-id=42.xml", 1, "SmsIdVimis1_2", 1, "vimis.sms", "");
    }

    @DisplayName("Проверка смс с vmcl=2 в ВИМИС")
    @Test
    public void Access_1444_vmcl2Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS2-id=42.xml", 2, "SmsIdVimis2_2", 1, "vimis.preventionsms", "");
    }

    @DisplayName("Проверка смс с vmcl=3 в ВИМИС")
    @Test
    public void Access_1444_vmcl3Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS2-id=42.xml", 3, "SmsIdVimis3_2", 1, "vimis.akineosms", "");
    }

    @DisplayName("Проверка смс с vmcl=4 в ВИМИС")
    @Test
    public void Access_1444_vmcl4Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS2-id=42.xml", 4, "SmsIdVimis4_2", 1, "vimis.cvdsms", "");
    }

    @DisplayName("Проверка смс с vmcl=1 в ВИМИС и Ремд")
    @Test
    public void Access_1444_vmcl1VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS3.xml", 1, "SmsIdVimisRemd1_2", 1, "vimis.sms", "");
    }

    @DisplayName("Проверка смс с vmcl=2 в ВИМИС и Ремд")
    @Test
    public void Access_1444_vmcl2VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS3.xml", 2, "SmsIdVimisRemd2_2", 1, "vimis.preventionsms", "");
    }

    @DisplayName("Проверка смс с vmcl=3 в ВИМИС и Ремд")
    @Test
    public void Access_1444_vmcl3VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS3.xml", 3, "SmsIdVimisRemd3_2", 1, "vimis.akineosms", "");
    }

    @DisplayName("Проверка смс с vmcl=4 в ВИМИС и Ремд")
    @Test
    public void Access_1444_vmcl4VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS3.xml", 4, "SmsIdVimisRemd4_2", 1, "vimis.cvdsms", "");
    }

    @DisplayName("Проверка смс с vmcl=99 в Ремд")
    @Test
    public void Access_1444_vmcl99Remd() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS3.xml", 99, "SmsIdRemd_2", 1, "", "vimis.remd_sent_result");
    }

    @DisplayName("Проверка смс с vmcl=99 в Ремд pdf")
    @Test
    public void Access_1444_vmcl99RemdPdf() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method(
                "SMS/id=101-vmcl=99.txt", 99, "SmsIdCertRemd_2", 1, "", "vimis.certificate_remd_sent_result");
    }

    @DisplayName("Проверка смс с vmcl=1 в ВИМИС")
    @Test
    public void Access_1444_vmcl1Vimis_2() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS2-id=42.xml", 1, "SmsIdVimis1", 0, "vimis.sms", "");
    }

    @DisplayName("Проверка смс с vmcl=2 в ВИМИС")
    @Test
    public void Access_1444_vmcl2Vimis_2() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS2-id=42.xml", 2, "SmsIdVimis2", 0, "vimis.preventionsms", "");
    }

    @DisplayName("Проверка смс с vmcl=3 в ВИМИС")
    @Test
    public void Access_1444_vmcl3Vimis_2() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS2-id=42.xml", 3, "SmsIdVimis3", 0, "vimis.akineosms", "");
    }

    @DisplayName("Проверка смс с vmcl=4 в ВИМИС")
    @Test
    public void Access_1444_vmcl4Vimis_2() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS2-id=42.xml", 4, "SmsIdVimis4", 0, "vimis.cvdsms", "");
    }

    @DisplayName("Проверка смс с vmcl=1 в ВИМИС и Ремд")
    @Test
    public void Access_1444_vmcl1VimisRemd_2() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS3.xml", 1, "SmsIdVimisRemd1", 0, "vimis.sms", "");
    }

    @DisplayName("Проверка смс с vmcl=2 в ВИМИС и Ремд")
    @Test
    public void Access_1444_vmcl2VimisRemd_2() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS3.xml", 2, "SmsIdVimisRemd2", 0, "vimis.preventionsms", "");
    }

    @DisplayName("Проверка смс с vmcl=3 в ВИМИС и Ремд")
    @Test
    public void Access_1444_vmcl3VimisRemd_2() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS3.xml", 3, "SmsIdVimisRemd3", 0, "vimis.akineosms", "");
    }

    @DisplayName("Проверка смс с vmcl=4 в ВИМИС и Ремд")
    @Test
    public void Access_1444_vmcl4VimisRemd_2() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS3.xml", 4, "SmsIdVimisRemd4", 0, "vimis.cvdsms", "");
    }

    @DisplayName("Проверка смс с vmcl=99 в Ремд")
    @Test
    public void Access_1444_vmcl99Remd_2() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/SMS3.xml", 99, "SmsIdRemd", 0, "", "vimis.remd_sent_result");
    }

    @DisplayName("Проверка смс с vmcl=99 в Ремд pdf")
    @Test
    public void Access_1444_vmcl99RemdPdf_2() throws InterruptedException, SQLException, IOException {
        Access_1444_2Method("SMS/id=101-vmcl=99.txt", 99, "SmsIdCertRemd", 1, "", "vimis.certificate_remd_sent_result");
    }
}
