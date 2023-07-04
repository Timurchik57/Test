package api;

import UI.SQL;
import UI.TestListener;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isOneOf;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Feature("Проверка валидных ошибок при одинаковых данных")
public class Access_370Test extends BaseAPI {
    SQL sql;
    XML xml;
    public String value388;

    @Issue(value = "TEL-370")
    @Link(name = "ТМС-901", url = "https://team-1okm.testit.software/projects/5/tests/901?isolatedSection=827ef86d-406f-4fec-a839-c7939a1a4497")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Order(1)
    @DisplayName("Валидный СЭМД с DocType = 3 vmcl= 1")
    @Description("Отправляем валидный СЭМД, после отправляем с одинаковым SetID, ID, VersionNumber и смотрим валидные ошибки")
    public void Access_370ID_3() throws IOException, SQLException, InterruptedException {
        sql = new SQL();
        xml = new XML();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем смс с Doctype = 3");
            xml.ApiSmd("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21);
            System.out.println(ID);
            System.out.println(SetID);
            System.out.println(VN);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Id, который уже есть СЭМД с DocType = 43 vmcl= 1")
    public void Access_370ID_5() throws IOException, SQLException, InterruptedException {
        sql = new SQL();
        xml = new XML();
        String file = "SMS/SMSV2-(id=2)-vmcl=1.xml";
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            VN = (int) Math.floor(timestamp.getTime() / 1000) + 4;
            InputProp("src/test/resources/my.properties", "value_VN", String.valueOf(VN));
            xml.ReplaceWord(file, "${iD}", props.getProperty("value_ID"));
            xml.ReplaceWord(file, "${setId}", props.getProperty("value_SetID"));
            xml.ReplaceWord(file, "${versionNumber}", String.valueOf(VN));
            xml.ReplaceWord(file, "${mo}", MO);
            xml.ReplaceWord(file, "${guid}", PatientGuid);
            xml.ReplaceWord(file, "${namemo}", NameMO);
            xml.ReplacementWordInFile(file, "2", 1, 0, true, 3, 1, 9, 18, 1, 57, 21);
            String value = String.valueOf(given()
                                                  .filter(new AllureRestAssured())
                                                  .header("Authorization", "Bearer " + Token)
                                                  .contentType(ContentType.JSON)
                                                  .when()
                                                  .body(xml.body)
                                                  .post(HostAddress + "/api/smd")
                                                  .prettyPeek()
                                                  .then()
                                                  .statusCode(200)
                                                  .extract().jsonPath().getString("result[0].errorMessage"));
            assertThat(
                    value,
                    isOneOf("Уникальный идентификатор набора версий документа совпадает с идентификатором уже загруженного документа (Тип ранее загруженного документа = \"3\")")
            );
        }
    }

    @Test
    @Order(3)
    @DisplayName("Валидный СЭМД 2 с DocType = 3 vmcl= 1")
    public void Access_370ID() throws IOException, SQLException, InterruptedException {
        sql = new SQL();
        xml = new XML();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем смс с Doctype = 3");
            xml.ApiSmd("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21);
            System.out.println(props.getProperty("value_ID"));
            System.out.println(props.getProperty("value_SetID"));
            System.out.println(props.getProperty("value_VN"));
        }
    }

    @Test
    @Order(4)
    @DisplayName("versionNumber, который уже есть СЭМД с DocType = 3 vmcl= 1")
    public void Access_370ID_5_SetID() throws IOException, SQLException, InterruptedException {
        sql = new SQL();
        xml = new XML();
        String file = "SMS/SMS3.xml";
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            ID = (int) Math.floor(timestamp.getTime() / 1000) + 4;
            InputProp("src/test/resources/my.properties", "value_ID", String.valueOf(ID));
            xml.ReplaceWord(file, "${iD}", String.valueOf(ID));
            xml.ReplaceWord(file, "${setId}", props.getProperty("value_SetID"));
            xml.ReplaceWord(file, "${versionNumber}", props.getProperty("value_VN"));
            xml.ReplaceWord(file, "${mo}", MO);
            xml.ReplaceWord(file, "${guid}", PatientGuid);
            xml.ReplaceWord(file, "${namemo}", NameMO);
            System.out.println(props.getProperty("value_ID"));
            System.out.println(props.getProperty("value_SetID"));
            System.out.println(props.getProperty("value_VN"));
            xml.ReplacementWordInFile(file, "3", 1, 0, true, 3, 1, 9, 18, 1, 57, 21);
            String value = String.valueOf(given()
                                                  .filter(new AllureRestAssured())
                                                  .header("Authorization", "Bearer " + Token)
                                                  .contentType(ContentType.JSON)
                                                  .when()
                                                  .body(xml.body)
                                                  .post(HostAddress + "/api/smd")
                                                  .prettyPeek()
                                                  .then()
                                                  .statusCode(200)
                                                  .extract().jsonPath().getString("result[0].errorMessage"));
            assertThat(
                    value,
                    isOneOf("Версия загружаемого документа с указанными реквизитами SetID совпадает (или меньше) с ранее загруженным документом")
            );
        }
    }
}
