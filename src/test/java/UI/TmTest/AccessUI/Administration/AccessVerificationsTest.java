package UI.TmTest.AccessUI.Administration;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.ConsultationSchedule;
import UI.TmTest.PageObject.Directions.HospitalizationSchedule;
import UI.TmTest.PageObject.Statistics.Reports;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Проверка отображения доступа у пользователя")
public class AccessVerificationsTest extends BaseTest {
    AuthorizationObject authorizationObject;
    AcceessRoles acceessRoles;
    Users users;
    Reports statisticReport;
    ConsultationSchedule consultationSchedule;
    HospitalizationSchedule hospitalizationSchedule;
    String ACCESS = "Доступ к аналитической отчетности из системы BI";
    String ACCESS1 = "Расписание консультаций";
    String ACCESS2 = "Расписание госпитализаций";

    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Администрирование")
    @DisplayName("Проверка отображения доступа у пользователя")
    @Description("Предоставляем тестовому пользователю определённую доступ, далее авторизуемся через него и проверяемость наличие данного доступа. Убираем доступ и проверяем его отсутствие")
    public void AccessVerification() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        acceessRoles = new AcceessRoles(driver);
        users = new Users(driver);
        statisticReport = new Reports(driver);
        consultationSchedule = new ConsultationSchedule(driver);
        hospitalizationSchedule = new HospitalizationSchedule(driver);
        /** На ХМАО нет возможности авторизовываться с созданного аккаунта*/
        if (KingNumber == 1 | KingNumber == 2) {
            System.out.println("Проверка отображения доступа у пользователя");
            System.out.println("Генерируем снилс для нового пользователя");
            authorizationObject.GenerateSnilsMethod();
            /** Авторизация */
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(users.UsersWait);
            WaitElement(users.HeaderUsersWait);
            users.AddUser.click();
            users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
            Thread.sleep(2000);
            users.ButtonSearch.click();
            WaitElement(users.SnilsInputWait);
            if (KingNumber == 1) {
                AddUsersMethod(users.WorkOkb, users.Accounting, users.RoleUserTest213, "");
            }
            if (KingNumber == 2) {
                AddUsersMethod(users.WorkOkb, users.Accounting, users.RoleUserTest2, "");
            }
            if (KingNumber == 4) {
                AddUsersMethod(users.WorkOkb, users.Accounting, users.RoleUserTest999, "");
            }
            System.out.println("Новый пользователь добавлен в ИЭМК");
            System.out.println("Проверяемый СНИЛС: " + authorizationObject.GenerationSnils);
            /** Редактирование роли - включение доступа */
            System.out.println("Редактирование роли - включение доступа");
            /** Метод для Перехода в роли доступа и открытие редактирование для роли  */
            acceessRoles.OpenRole();
            /** Ввод значений */
            System.out.println("Ввод нужного доступа");
            if (KingNumber != 4) {
                inputWord(acceessRoles.InputWord, ACCESS);
                Thread.sleep(1000);
                if (isElementVisible(acceessRoles.AnalysisBi)) {
                    System.out.println("Значение найдено");
                }
                if (isElementNotVisible(acceessRoles.CheckBoxFalse)) {
                    acceessRoles.CheckBox.click();
                    WaitElement(acceessRoles.CheckBoxActive);
                }
            }
            inputWord(acceessRoles.InputWord, ACCESS1);
            Thread.sleep(1000);
            if (isElementVisible(acceessRoles.Schedule)) {
                System.out.println("Значение найдено");
            }
            if (isElementNotVisible(acceessRoles.CheckBoxScheduleFalse)) {
                acceessRoles.CheckBoxSchedule.click();
                WaitElement(acceessRoles.CheckBoxScheduleActive);
            }
            inputWord(acceessRoles.InputWord, ACCESS2);
            Thread.sleep(1000);
            if (isElementVisible(acceessRoles.ScheduleHospital)) {
                System.out.println("Значение найдено");
            }
            if (isElementNotVisible(acceessRoles.CheckBoxHospitalFalse)) {
                acceessRoles.CheckBoxHospital.click();
                WaitElement(acceessRoles.CheckBoxHospitalActive);
            }
            actionElementAndClick(acceessRoles.Update);
            Thread.sleep(1000);
            /** Очищаем Cookie */
            System.out.println("Очищаем Cookie");
            driver.manage().deleteAllCookies();
            /** Авторизуемся под тестовым пользователем */
            System.out.println("Авторизуемся под тестовым пользователем");
            if (KingNumber == 1) {
                driver.get(
                        "https://tm-test.pkzdrav.ru/auth/bysnils?snils=" + authorizationObject.GenerationSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            }
            if (KingNumber == 2) {
                driver.get(
                        "https://tm-dev.pkzdrav.ru/auth/bysnils?snils=" + authorizationObject.GenerationSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            }
            if (KingNumber == 4) {
                driver.get(
                        "https://remotecons-test.miacugra.ru/auth/bysnils?" + authorizationObject.GenerationSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            }
            Thread.sleep(2000);
            if (isElementNotVisible(authorizationObject.Selected)) {
                authorizationObject.Select.click();
                WaitElement(authorizationObject.SelectWait);
                Thread.sleep(2000);
                actionElementAndClick(authorizationObject.Okb);
                authorizationObject.Сhoose.click();
            }
            if (isElementNotVisible(authorizationObject.Alert)) {
                ClickElement(authorizationObject.OKWait);
            }
            /** Проверка 1*/
            if (KingNumber != 4) {
                WaitElement(statisticReport.ReportsWait);
                System.out.println("Ошибки нет - Доступ " + ACCESS + " присутствует");
            }
            /** Проверка 2*/
            WaitElement(consultationSchedule.Schedules);
            System.out.println("Ошибки нет - Доступ " + ACCESS1 + " присутствует");
            /** Проверка 3*/
            WaitElement(hospitalizationSchedule.Hospitalization);
            System.out.println("Ошибки нет - Доступ " + ACCESS2 + " присутствует");
            /** Авторизуемся под пользователем с полным доступом */
            System.out.println("Авторизуемся под пользователем с полным доступом");
            AuthorizationMethod(authorizationObject.MIAC);
            /** Редактирование роли - отключение доступа */
            System.out.println("Редактирование роли - отключение доступа");
            acceessRoles.OpenRole();
            /** Ввод значений */
            System.out.println("Ввод нужного доступа");
            if (KingNumber != 4) {
                inputWord(acceessRoles.InputWord, ACCESS);
                Thread.sleep(1000);
                if (isElementVisible(acceessRoles.AnalysisBi)) {
                    System.out.println("Значение найдено");
                }
                if (!isElementNotVisible(acceessRoles.CheckBoxFalse)) {
                    acceessRoles.CheckBox.click();
                    WaitElement(acceessRoles.CheckBoxFalse);
                }
            }
            inputWord(acceessRoles.InputWord, ACCESS1);
            Thread.sleep(1000);
            if (isElementVisible(acceessRoles.Schedule)) {
                System.out.println("Значение найдено");
            }
            if (!isElementNotVisible(acceessRoles.CheckBoxScheduleFalse)) {
                acceessRoles.CheckBoxSchedule.click();
                WaitElement(acceessRoles.CheckBoxScheduleFalse);
            }
            inputWord(acceessRoles.InputWord, ACCESS2);
            Thread.sleep(1000);
            if (isElementVisible(acceessRoles.ScheduleHospital)) {
                System.out.println("Значение найдено");
            }
            if (!isElementNotVisible(acceessRoles.CheckBoxHospitalFalse)) {
                acceessRoles.CheckBoxHospital.click();
                WaitElement(acceessRoles.CheckBoxHospitalFalse);
            }
            actionElementAndClick(acceessRoles.Update);
            Thread.sleep(1000);
            /** Очищаем Cookie */
            System.out.println("Очищаем Cookie");
            driver.manage().deleteAllCookies();
            /** Снова авторизуемся под тестовым пользователем */
            System.out.println("Снова авторизуемся под тестовым пользователем");
            if (KingNumber == 1) {
                driver.get(
                        "https://tm-test.pkzdrav.ru/auth/bysnils?snils=" + authorizationObject.GenerationSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            }
            if (KingNumber == 2) {
                driver.get(
                        "https://tm-dev.pkzdrav.ru/auth/bysnils?snils=" + authorizationObject.GenerationSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            }
            if (KingNumber == 4) {
                driver.get(
                        "https://remotecons-test.miacugra.ru/auth/bysnils?" + authorizationObject.GenerationSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            }
            Thread.sleep(2000);
            if (isElementNotVisible(authorizationObject.Selected)) {
                authorizationObject.Select.click();
                WaitElement(authorizationObject.SelectWait);
                Thread.sleep(2000);
                actionElementAndClick(authorizationObject.Okb);
                authorizationObject.Сhoose.click();
            }
            if (isElementNotVisible(authorizationObject.Alert)) {
                ClickElement(authorizationObject.OKWait);
            }
            /** Проверка 1*/
            if (KingNumber != 4) {
                WaitNotElement(statisticReport.ReportsWait);
                System.out.println("Ошибки нет - Доступ " + ACCESS + " отсутствует");
            }
            /** Проверка 2*/
            WaitNotElement(consultationSchedule.Schedules);
            System.out.println("Ошибки нет - Доступ " + ACCESS1 + " отсутствует");
            /** Проверка 3*/
            WaitNotElement(hospitalizationSchedule.Hospitalization);
            System.out.println("Ошибки нет - Доступ " + ACCESS2 + " отсутствует");
            System.out.println("Проверка отображения доступа у пользователя");
            System.out.println("Удаление созданного пользователя");
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(users.UsersWait);
            WaitElement(users.HeaderUsersWait);
            users.AddUser.click();
            users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
            users.ButtonSearch.click();
            WaitElement(users.SnilsInputWait);
            ClickElement(users.BRBEditWaitFirst);
            SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);
            users.UpdateWork.click();
            WaitNotElement(users.EditWait);
            WaitElement(users.SnilsInputWait);
            actionElementAndClick(users.Update);
            Thread.sleep(2000);
        }
    }
}
