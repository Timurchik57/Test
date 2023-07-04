package UI.TmTest.AccessUI.Administration;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.NSI.Equipments;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Добавление расписания оборудованию")
public class AddEquipmentsTest extends BaseTest {
    AuthorizationObject authorizationObject;
    MedOrganization medOrganization;
    Equipments equipments;
    EquipmentSchedule equipmentSchedule;
    DirectionsForQuotas directionsForQuotas;

    @Order(1)
    @Description("Добавление организации, для которой будет доступно оборудование. Редактирование самого оборудования, установка максимального веса и выбор исследования")
    @DisplayName("Добавление оборудования")
    @Test
    @Story("Администрирование")
    public void AddEquipment() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        medOrganization = new MedOrganization(driver);
        equipments = new Equipments(driver);
        System.out.println("Добавление оборудования");
        AuthorizationMethod(authorizationObject.YATCKIV);
        /** Переход в добавление МО, получающей консультацию */
        System.out.println("Переход в добавление МО, получающей консультацию");
        WaitElement(medOrganization.OrganizationWait);
        actionElementAndClick(medOrganization.Organization);
        /** Выбор организации для редактирования */
        System.out.println("Выбор организации для редактирования");
        WaitElement(medOrganization.HeaderOrganizationWait);
        WaitElement(medOrganization.InputOrganizationWait);
        if (KingNumber == 1 | KingNumber == 2) {
            SelectClickMethod(medOrganization.InputOrganizationWait, medOrganization.SelectMO);
        }
        if (KingNumber == 4) {
            SelectClickMethod(medOrganization.InputOrganizationWait, medOrganization.SelectMOKon);
        }
        medOrganization.Search.click();
        /** Редактирование организации */
        System.out.println("Редактирование организации");
        ClickElement(medOrganization.Edit);
        WaitElement(medOrganization.HeaderMOWait);
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        ClickElement(medOrganization.RecordingWait);
        /** Добавление организации для которой будет доступно оборудование */
        System.out.println("Добавление организации для которой будет доступно оборудование");
        WaitElement(medOrganization.GammaCameraWait);
        AddEquipmentsMethod(medOrganization.GammaCameraWait, medOrganization.OKB, medOrganization.OKBTrue);
        WaitElement(medOrganization.KT);
        AddEquipmentsMethod(medOrganization.KT, medOrganization.OKB, medOrganization.OKBTrue);
        WaitElement(medOrganization.MRT);
        AddEquipmentsMethod(medOrganization.MRT, medOrganization.OKB, medOrganization.OKBTrue);
        Thread.sleep(1000);
        WaitElement(medOrganization.UpdateWait);
        actionElementAndClick(medOrganization.Update);
        Thread.sleep(2000);
        if (isElementNotVisible(medOrganization.HeaderMOWait)) {
            ClickElement(medOrganization.CloseWait);
        }
        System.out.println("МО получающая консультацию добавлена");
        if (KingNumber == 4 | KingNumber == 2) {
            Thread.sleep(10000);
        }
        ClickElement(equipments.EquipmentWaitt);
        /** Выбор организации для редактирования */
        WaitElement(equipments.EquipmentWait);
        WaitElement(equipments.HeaderEquipmentWait);
        if (KingNumber == 1 | KingNumber == 2) {
            SelectClickMethod(equipments.InputMoWait, equipments.SelectMO);
            inputWord(equipments.Name, "X-OMATT");
            ClickElement(equipments.CheckIs);
            inputWord(equipments.Description, "1233");
            equipments.Search.click();
            WaitElement(equipments.SearchNameWait);
        }
        if (KingNumber == 4) {
            SelectClickMethod(equipments.InputMoWait, equipments.SelectMOKon);
            ClickElement(equipments.CheckIs);
            inputWord(equipments.Description, "3213");
            equipments.Search.click();
            WaitElement(equipments.FirstLineSet);
        }
        /** Поиск нужного оборудования */
        System.out.println("Поиск нужного оборудования");
        ClickElement(equipments.FirstLineSet);
        WaitElement(equipments.HeaderEditWait);
        WaitElement(equipments.DivisionWait);
        if (KingNumber == 1 | KingNumber == 2) {
            SelectClickMethod(equipments.DivisionWait, equipments.SelectDivision);
            SelectClickMethod(equipments.TypeWait, equipments.SelectType);
            SelectClickMethod(equipments.ModelWait, equipments.SelectModel);
        }
        if (KingNumber == 4) {
            SelectClickMethod(equipments.DivisionWait, equipments.SelectDivisionBuh);
            SelectClickMethod(equipments.TypeWait, equipments.SelectType);
            SelectClickMethod(equipments.ModelWait, equipments.SelectModel);
        }
        inputWord(equipments.Weight, "500");
        Thread.sleep(3000);
        if (isElementNotVisible(equipments.CheckWait)) {
            equipments.Check.click();
        }
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
            Thread.sleep(1500);
        } else {
            equipments.Close.click();
        }
        System.out.println("Оборудование отредактировано");
    }

    @Order(2)
    @Test
    @Story("Администрирование")
    @Description("Добавление расписания оборудования. Создание расписания оборудования, которое пересекается с существующим, проверка отображения валидного уведомления")
    @DisplayName("Переход в расписание оборудования и составление расписания")
    public void EquipmentSchedule() throws InterruptedException {
        equipmentSchedule = new EquipmentSchedule(driver);
        authorizationObject = new AuthorizationObject(driver);
        System.out.println("Добавление расписания оборудования");
        if (KingNumber == 1 | KingNumber == 2) {
            AuthorizationMethod(authorizationObject.YATCKIV);
            equipmentSchedule.AddEquipmentSchedule(
                    equipmentSchedule.SelectEquipmentChoice, equipmentSchedule.Tomorrow, equipmentSchedule.NextMouth);
        }
        if (KingNumber == 4) {
            AuthorizationMethod(authorizationObject.Kondinsk);
            equipmentSchedule.AddEquipmentSchedule(
                    equipmentSchedule.SelectEquipmentCDR, equipmentSchedule.Tomorrow, equipmentSchedule.NextMouth);
        }
        if (isElementNotVisible(By.xpath("//div[@role='alert']//h2[contains(.,'Успешно')]"))) {
            System.out.println("Новое расписание добавлено");
        } else {
            System.out.println("Проверка уведомления о пересечении расписания");
        }
    }

    @Order(3)
    @Description("Создание консультации. Заполнение информации о направившем враче. Прикрепление файла. Запись на приём. Проверка на то, что не видно отображения Фамилии пациента. Проверка на то, что нельзя записать пациента на занятый слот")
    @DisplayName("Создание консультации на оборудование")
    @Test
    @Story("Администрирование")
    public void ExcessWeight() throws InterruptedException, IOException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        System.out.println("Создание консультации на оборудование");
        AuthorizationMethod(authorizationObject.OKB);
        /** Метод создания консультации на оборудование и заполнении информации */
        directionsForQuotas.CreateConsultationMethod("500", false, directionsForQuotas.SelectResearch, true);
        WaitElement(equipmentSchedule.WriteWait);
        if (KingNumber == 1 | KingNumber == 2) {
            WaitElement(equipmentSchedule.AddFileWait);
            File file = new File("src/test/resources/test.txt");
            equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
            Thread.sleep(1000);
            ClickElement(equipmentSchedule.WriteTwo);
        }
        if (KingNumber == 4) {
            Thread.sleep(3000);
            ClickElement(equipmentSchedule.WriteTwo);
        }
        Thread.sleep(2000);
        WaitElement(equipmentSchedule.AlertWait);
        equipmentSchedule.AlertClose.click();
        System.out.println("Запись на прием успешна создана!");
        /** Дополнительная проверка на то, что не видно отображения Фамилии пациента */
        System.out.println("Дополнительная проверка на то, что не видно отображения Фамилии пациента");
        Thread.sleep(1000);
        WaitNotElement(equipmentSchedule.FioWait);
        System.out.println("Фамилии пациента не отображается!");
        /** Дополнительная проверка на то, что нельзя записать пациента на занятый слот */
        System.out.println("Дополнительная проверка на то, что нельзя записать пациента на занятый слот");
        ClickElement(equipmentSchedule.OccupiedSlotWaitt);
        WaitElement(equipmentSchedule.OccupiedSlotWait);
        System.out.println("Пациент не записывается на занятый слот!");
    }

    @Order(4)
    @Description("Создание консультации. Заполнение информации о направившем враче. Ввод веса, который больше допустимого. Прикрепление файла. Запись на приём.")
    @DisplayName("Проверка уведомления о превышении веса")
    @Test
    @Story("Администрирование")
    public void PatientRegistrationEquipment() throws InterruptedException, SQLException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        System.out.println("Проверка уведомления о превышении веса");
        AuthorizationMethod(authorizationObject.OKB);
        /** Метод создания консультации на оборудование и заполнении информации */
        directionsForQuotas.CreateConsultationMethod("990", false, directionsForQuotas.SelectResearch, true);
        WaitElement(equipmentSchedule.WriteWait);
        if (KingNumber == 1 | KingNumber == 2) {
            WaitElement(equipmentSchedule.AddFileWait);
            File file = new File("src/test/resources/test.txt");
            equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
            Thread.sleep(1000);
            WaitElement(equipmentSchedule.AlertErrorWait);
        } else {
            WaitElement(equipmentSchedule.AlertErrorWait);
        }
        System.out.println("Предупреждение о превышении веса появилось");
    }
}
