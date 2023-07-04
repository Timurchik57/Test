package UI.TmTest.AccessUI.Directions;

import UI.TestListener;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.IncomingUnfinished;
import api.BaseAPI;
import api.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class Access_1111Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    EquipmentSchedule equipmentSchedule;
    DirectionsForQuotas directionsForQuotas;
    IncomingUnfinished incomingUnfinished;
    Users users;

    @Issue(value = "TEL-1111")
    @Link(name = "ТМС-1416", url = "https://team-1okm.testit.software/projects/5/tests/1416?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Запись на оборудование в МО создающее направление")
    @Description("Создаём направление в МО, через которое авторизовались, проверяя отображение направления при различных подразделениях у авторизованного пользователя")
    public void Access_1111() throws IOException, SQLException, InterruptedException {
        equipmentSchedule = new EquipmentSchedule(driver);
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        users = new Users(driver);
        /** На Хмао не включена фича на запись в собсвтенную МО*/
        if (KingNumber != 4) {
            System.out.println("Добавление расписания оборудования");
            AuthorizationMethod(authorizationObject.OKB);
            if (KingNumber != 4) {
                equipmentSchedule.AddEquipmentSchedule(
                        equipmentSchedule.SelectEquipmentChorus, equipmentSchedule.Tomorrow,
                        equipmentSchedule.NextMouth
                );
            } else {
                equipmentSchedule.AddEquipmentSchedule(
                        equipmentSchedule.SelectEquipmentChorus, equipmentSchedule.Tomorrow,
                        equipmentSchedule.NextMouth
                );
            }

            /** Метод создания консультации на оборудование и заполнении информации */
            if (KingNumber == 4) {
                directionsForQuotas.CreateConsultationMethod(
                        "500", true, directionsForQuotas.SelectResearch_HMP04, true);
            } else {
                directionsForQuotas.CreateConsultationMethod(
                        "500", true, directionsForQuotas.SelectResearch_HMP30, true);
            }
            WaitElement(equipmentSchedule.WriteWait);
            actionElementAndClick(equipmentSchedule.Write);
            Thread.sleep(5000);
            equipmentSchedule.AlertClose.click();
            System.out.println("Запись на прием успешна создана!");
            System.out.println(
                    "Переходим в Направления на квоты - Исходящие - Незавершённые и проверяем созданную консультацию");
            ClickElement(directionsForQuotas.ConsultationWait);
            WaitElement(directionsForQuotas.FirstLineMO);
            driver.findElement(directionsForQuotas.FirstLineMO).getText().contains(
                    "БУ ХМАО-Югры \"Окружная клиническая больница\"");
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
            Date = formatForDateNow.format(date);
            Assertions.assertEquals(
                    driver.findElement(directionsForQuotas.FirstLineMO).getText(),
                    "БУ ХМАО-Югры \"Окружная клиническая больница\""
            );
            Assertions.assertEquals(driver.findElement(directionsForQuotas.FirstLineDate).getText(), "" + Date + "");
            Assertions.assertEquals(
                    driver.findElement(directionsForQuotas.FirstLinePatient).getText(), "Тестировщик Тест Тестович, 1");
            Assertions.assertEquals(driver.findElement(directionsForQuotas.FirstLineSnils).getText(), "159-790-257 20");
            Assertions.assertEquals(driver.findElement(directionsForQuotas.FirstLineStatus).getText(), "Отправлен");
            System.out.println(
                    "Переходим в Направления на квоты - Входящие - Незавершённые и проверяем созданную консультацию");
            ClickElement(incomingUnfinished.ConsultationWait);
            WaitElement(incomingUnfinished.FirstLineMO);
            Assertions.assertEquals(
                    driver.findElement(incomingUnfinished.FirstLineMO).getText(),
                    "БУ ХМАО-Югры \"Окружная клиническая больница\""
            );
            Assertions.assertEquals(driver.findElement(incomingUnfinished.FirstLineDate).getText(), "" + Date + "");
            Assertions.assertEquals(
                    driver.findElement(incomingUnfinished.FirstLinePatient).getText(), "Тестировщик Тест Тестович, 1");
            Assertions.assertEquals(driver.findElement(incomingUnfinished.FirstLineSnils).getText(), "159-790-257 20");
            Assertions.assertEquals(driver.findElement(incomingUnfinished.FirstLineStatus).getText(), "Отправлен");
            System.out.println("Создание нового пользователя с другим подразделением");
            authorizationObject.GenerateSnilsMethod();
            /** Авторизация и переход в пользователи */
            System.out.println("Авторизация и переход в пользователи");
            AuthorizationMethod(authorizationObject.OKB);
            WaitElement(users.UsersWait);
            actionElementAndClick(users.Users);
            WaitElement(users.HeaderUsersWait);
            users.AddUser.click();
            users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
            users.ButtonSearch.click();
            WaitElement(users.SnilsInputWait);
            /** Условие, нужно ли заполнять данные о пользователе */
            if (!isElementNotVisible(users.NotHeaderIEMK)) {
                if (KingNumber == 1) {
                    AddUsersMethod(users.WorkOkb, users.Accounting, users.RoleUserTest213, "");
                }
                if (KingNumber == 2) {
                    AddUsersMethod(users.WorkOkb, users.Accounting, users.RoleUserTest2, "");
                }
                System.out.println("Новый пользователь добавлен в ИЭМК");
            }
            System.out.println("Авторизуемся под тестовым пользователем");
            if (KingNumber == 1) {
                driver.get(
                        "https://tm-test.pkzdrav.ru/auth/bysnils?snils=" + authorizationObject.GenerationSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            }
            if (KingNumber == 2) {
                driver.get(
                        "https://tm-dev.pkzdrav.ru/auth/bysnils?snils=" + authorizationObject.GenerationSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            }
            Thread.sleep(2000);
            if (isElementNotVisible(authorizationObject.Selected)) {
                authorizationObject.Select.click();
                WaitElement(authorizationObject.SelectWait);
                Thread.sleep(2000);
                ClickElement(authorizationObject.OKB);
                authorizationObject.Сhoose.click();
            }
            if (isElementNotVisible(authorizationObject.Alert)) {
                ClickElement(authorizationObject.OKWait);
            }
            System.out.println(
                    "Переходим в Направления на квоты - Исходящие - Незавершённые и проверяем созданную консультацию");
            ClickElement(directionsForQuotas.ConsultationWait);
            WaitElement(directionsForQuotas.FirstLineMO);
            Assertions.assertNotEquals(
                    driver.findElement(directionsForQuotas.FirstLineMO).getText(),
                    "БУ ХМАО-Югры \"Окружная клиническая больница\""
            );
            System.out.println(
                    "Переходим в Направления на квоты - Входящие - Незавершённые и проверяем созданную консультацию");
            ClickElement(incomingUnfinished.ConsultationWait);
            WaitElement(incomingUnfinished.FirstLineMO);
            ClickElement(incomingUnfinished.TypeDate);
            ClickElement(incomingUnfinished.SelectDate);
            ClickElement(incomingUnfinished.FirstDate);
            ClickElement(incomingUnfinished.ToDay);
            if (isElementNotVisible(By.xpath("//td[@class='available today in-range start-date end-date']"))) {
                ClickElement(incomingUnfinished.ToDay1);
            } else {
                ClickElement(incomingUnfinished.ToDay2);
            }
            driver.findElement(incomingUnfinished.Snils).sendKeys("15979025720");
            ClickElement(incomingUnfinished.Search);
            Thread.sleep(2000);
            WaitElement(incomingUnfinished.NotBad);
            /** Удаление созданного профиля, для возможности переиспользовать данный тест */
            AuthorizationMethod(authorizationObject.OKB);
            WaitElement(users.UsersWait);
            actionElementAndClick(users.Users);
            WaitElement(users.HeaderUsersWait);
            WaitElement(users.AddUserWait);
            users.AddUser.click();
            users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
            users.ButtonSearch.click();
            WaitElement(users.SnilsInputWait);
            /** Проверка, что добавленный профиль отображается */
            WaitElement(users.OneWait);
            /** Увольнение с 1 работы */
            System.out.println("Изменение данных о месте работы");
            users.Edit1.click();
            WaitElement(users.EditWait);
            SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);
            users.UpdateWork.click();
            WaitNotElement(users.EditWait);
            WaitElement(users.SnilsInputWait);
            actionElementAndClick(users.Update);
        }
    }
}
