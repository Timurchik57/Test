package api.A;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.NSI.Equipments;
import api.BaseAPI;
import api.TestListenerApi;
import api.XML;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Авторизация и методы перед тестами")
public class Authorization extends BaseAPI {
    XML xml;
    String BODY;
    String id;
    String SerialNumber;
    String InventoryNumber;
    String MO;
    String Name;
    String Type;

    @Test
    @DisplayName("Очищаем файл FiledTests.sh для записи в него упавших тестов")
    public void ClearFile() throws IOException {
        new FileWriter("src/test/resources/FiledTests.sh", false).close();

        FileWriter writer = new FileWriter("src/test/resources/FiledTests.sh", true);
        BufferedWriter bufferWriter = new BufferedWriter(writer);
        bufferWriter.write("mvn test -Dtest=");
        bufferWriter.close();
    }

    @Test
    @DisplayName("Авторизация и получение токена авторизации")
    public void Authorizations() throws IOException {
        xml = new XML();
        Token = given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(BodyAuthorisation)
                .when()
                .post(HostAddress + "/auth.svc")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("Result.Value");
        xml.ReplaceStringInFile(0, Token, "AuthorizationToken.txt");
    }

    @Test
    @DisplayName("Настройка контура для тестов, установка нужных значений")
    public void BeforeData() throws IOException, SQLException, InterruptedException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        MedOrganization medOrganization = new MedOrganization(driver);
        Equipments equipments = new Equipments(driver);
        SQL sql = new SQL();
        if (KingNumber == 1 | KingNumber == 2) {
            System.out.println("Устанавливаем видимость ролей доступа в таблице telmed.accessroles");
            if (KingNumber == 1) {
                sql.UpdateConnection(
                        "update telmed.accessroles set isavailable = '1' where name = 'Тестовая роль 213'");
            }
            if (KingNumber == 2) {
                sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = 'Тестовая роль'");
                sql.UpdateConnection("update telmed.accessroles set isavailable = '0' where name = 'Тестовая роль 2'");
            }
            sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = 'Полный доступ'");
            sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = 'Консультант'");
            System.out.println("У нужного оборудования удаляем дату списания");
            if (KingNumber == 1) {
                BODY = "{\"Username\": \"1.2.643.5.1.13.13.12.2.86.9003\",\n" +
                        "                   \"SystemId\": 22,\n" +
                        "                    \"Password\": \"612D11DB39CE0E0C434CCA701855CDDC\"}";
            }
            if (KingNumber == 2) {
                BODY = "{\"Username\": \"1.2.643.5.1.13.13.12.2.86.9003\",\n" +
                        "                   \"SystemId\": 22,\n" +
                        "                    \"Password\": \"561D9DF2BBAD98CF327DE72BEB3FB33C\"}";
            }
            System.out.println(HostAddress + "/auth.svc");
            String Token = given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .body(BODY)
                    .when()
                    .post(HostAddress + "/auth.svc")
                    .prettyPeek()
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("Result.Value");
            if (KingNumber == 1) {
                id = "14037";
                SerialNumber = "313141";
                InventoryNumber = "1010404095";
                MO = "1.2.643.5.1.13.13.12.2.86.9003";
                Name = "X-OMAT";
                Type = "54";
            }
            if (KingNumber == 2) {
                id = "17478";
                SerialNumber = "313141";
                InventoryNumber = "1010404095";
                MO = "1.2.643.5.1.13.13.12.2.86.9003";
                Name = "X-OMAT";
                Type = "54";
            }
            String result = given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + Token)
                    .body("{\n" +
                                  "  \"Id\": " + id + ",\n" +
                                  "  \"Name\": \"" + Name + "\",\n" +
                                  "  \"Type\": " + Type + ",\n" +
                                  "  \"Modality\": 1,\n" +
                                  "  \"MedicalOid\": \"" + MO + "\",\n" +
                                  "  \"PatientMaxWeight\": 50.0,\n" +
                                  "  \"IsAvailableForOtherMo\": true,\n" +
                                  "  \"DateAnnulment\": \"\",\n" +
                                  "  \"SerialNumber\": \"" + SerialNumber + "\",\n" +
                                  "  \"InventoryNumber\": \"" + InventoryNumber + "\",\n" +
                                  "  \"Description\": \"123\"\n" +
                                  "}")
                    .when()
                    .put(HostAddress + "/api/equipment/update")
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("Result.Id");
            if (KingNumber == 1) {
                Assertions.assertEquals(result, "14037", "Дата списания не удалилась");
            }
            if (KingNumber == 2) {
                Assertions.assertEquals(result, "17478", "Дата списания не удалилась");
            }
            System.out.println("Устанавливаем исследование нужному оборудованию");
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(equipments.EquipmentWaitt);
            /** Выбор организации для редактирования */
            WaitElement(equipments.EquipmentWait);
            WaitElement(equipments.HeaderEquipmentWait);
            SelectClickMethod(equipments.InputMoWait, equipments.SelectMO);
            inputWord(equipments.Name, "X-OMATT");
            ClickElement(equipments.CheckIs);
            inputWord(equipments.Description, "1233");
            equipments.Search.click();
            WaitElement(equipments.NameColumnWait);
            System.out.println("Проверка цвета оборудования (зелёный)");
            String Color = equipments.FirstLine.getCssValue("background-color");
            Assertions.assertEquals(Color, "rgba(207, 255, 207, 1)", " Цвет не совпадает с зелёным");
            WaitElement(equipments.SearchNameWait);
            System.out.println("Редактировать 1 элемент из списка");
            ClickElement(equipments.FirstLineSet);
            WaitElement(equipments.HeaderEditWait);
            WaitElement(equipments.DivisionWait);
            /** Редактирование Исследования */
            System.out.println("Редактирование Исследования");
            equipments.Researches.click();
            WaitElement(equipments.InputWordWait);
            if (!isElementNotVisible(equipments.ResearchesTrue)) {
                inputWord(equipments.InputWord, "HMH");
                WaitElement(equipments.BottomStartWait);
                WaitElement(equipments.HMP01Wait);
                actionElementAndClick(equipments.HMP01);
                wait.until(invisibilityOfElementLocated(equipments.BottomStartWait));
                equipments.Add.click();
                Thread.sleep(1000);
                equipments.Update.click();
            } else {
                equipments.Close.click();
            }
            Thread.sleep(2000);
            System.out.println("Оборудование отредактировано");
        }
        if (KingNumber == 4) {
            System.out.println("Устанавливаем видимость ролей доступа в таблице telmed.accessroles");
            sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = 'Тестовая 999'");
            sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = 'Полный доступ'");
            sql.UpdateConnection(
                    "update telmed.accessroles set isavailable = '1' where name = 'ВИМИС для разработчика МИС'");
            if (KingNumber == 4) {
                BODY = "{\"Username\": \"1.2.643.5.1.13.13.12.2.86.8902\",\n" +
                        "                   \"SystemId\": 13,\n" +
                        "                    \"Password\": \"8487D0E97267061375210E5D4031C567\"}";
            }
            String Token = given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .body(BODY)
                    .when()
                    .post(HostAddress + "/auth.svc")
                    .prettyPeek()
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("Result.Value");
            if (KingNumber == 4) {
                id = "50019";
                SerialNumber = "3CA14Y2040";
                InventoryNumber = "101240000953";
                MO = "1.2.643.5.1.13.13.12.2.86.8902";
                Name = "Aquilon LB";
                Type = "35";
            }
            String result = given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + Token)
                    .body("{\n" +
                                  "  \"Id\": " + id + ",\n" +
                                  "  \"Name\": \"" + Name + "\",\n" +
                                  "  \"Type\": " + Type + ",\n" +
                                  "  \"Modality\": 1,\n" +
                                  "  \"MedicalOid\": \"" + MO + "\",\n" +
                                  "  \"PatientMaxWeight\": 50.0,\n" +
                                  "  \"IsAvailableForOtherMo\": true,\n" +
                                  "  \"DateAnnulment\": \"\",\n" +
                                  "  \"SerialNumber\": \"" + SerialNumber + "\",\n" +
                                  "  \"InventoryNumber\": \"" + InventoryNumber + "\",\n" +
                                  "  \"Description\": \"123\"\n" +
                                  "}")
                    .when()
                    .put(HostAddress + "/api/equipment/update")
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("Result.Id");
            if (KingNumber == 4) {
                Assertions.assertEquals(result, "50019", "Дата списания не удалилась");
            }
            System.out.println("Устанавливаем исследование нужному оборудованию ");
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(equipments.EquipmentWaitt);
            /** Выбор организации для редактирования */
            WaitElement(equipments.EquipmentWait);
            WaitElement(equipments.HeaderEquipmentWait);
            SelectClickMethod(equipments.InputMoWait, equipments.SelectMOKon);
            ClickElement(equipments.CheckIs);
            inputWord(equipments.Description, "3211");
            Thread.sleep(2000);
            equipments.Search.click();
            WaitElement(equipments.SearchNameWaitFirst);
            System.out.println("Проверка цвета оборудования (зелёный)");
            String Color = equipments.FirstLine.getCssValue("background-color");
            Assertions.assertEquals(Color, "rgba(207, 255, 207, 1)", " Цвет не совпадает с зелёным");
            System.out.println("Поиск нужного оборудования");
            ClickElement(equipments.FirstLineSet);
            WaitElement(equipments.HeaderEditWait);
            WaitElement(equipments.DivisionWait);
            /** Редактирование Исследования */
            System.out.println("Редактирование Исследования");
            equipments.Researches.click();
            WaitElement(equipments.InputWordWait);
            if (!isElementNotVisible(equipments.ResearchesTrue)) {
                inputWord(equipments.InputWord, "HMH");
                WaitElement(equipments.BottomStartWait);
                WaitElement(equipments.HMP01Wait);
                actionElementAndClick(equipments.HMP01);
                wait.until(invisibilityOfElementLocated(equipments.BottomStartWait));
                equipments.Add.click();
                Thread.sleep(1000);
                equipments.Update.click();
            } else {
                equipments.Close.click();
            }
            Thread.sleep(2000);
            System.out.println("Оборудование отредактировано");
        }
    }
}
