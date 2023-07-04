package UI.TmTest.AccessUI.Administration;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.AuditUsers;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import static UI.TmTest.PageObject.Administration.AuditUsers.AssertAudit;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Добавление нового пользователя, редактирование и последущее увольнение")
public class AddUsersAndAuditUsersTest extends BaseTest {
    AuthorizationObject authorizationObject;
    Users users;
    AuditUsers auditUsers;

    @Owner(value = "Галиакберов Тимур")
    @Order(1)
    @DisplayName("Добавление нового пользователя")
    @Description("Генерация снилса для добавления пользователя. Добавление МО, роли и профиля. Проверка, что добавленный профиль отображается. Редактирование МО, роли и профиля с установкой даты увольнения.")
    @Test
    @Story("Администрирование")
    public void AddUser() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);
        System.out.println("Добавление нового пользователя");
        /** Генерация снилса для добавления пользователя */
        System.out.println("Генерация снилса для добавления пользователя");
        driver.get(GENERATE_SNILS);
        WaitElement(authorizationObject.ButtonNewNumberWait);
        authorizationObject.ButtonNewNumber.click();
        String text = authorizationObject.Snils.getText();
        System.out.println("Новый СНИЛС: " + text);
        /** Авторизация */
        AuthorizationMethod(authorizationObject.MIAC);
        ClickElement(users.UsersWait);
        WaitElement(users.HeaderUsersWait);
        users.AddUser.click();
        users.InputSnils.sendKeys(text);
        Thread.sleep(1500);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);
        AddUsersMethod(users.WorkYatskiv, users.Accounting, users.RoleUser5, "");
        System.out.println("Новый пользователь добавлен в ИЭМК");
        /** Редактирование роли, профиля и добавление даты увольнения */
        System.out.println("Редактирование роли, профиля и добавление даты увольнения");
        WaitElement(users.AddUserWait);
        Thread.sleep(1000);
        if (KingNumber == 4) {
            Thread.sleep(3000);
        }
        users.AddUser.click();
        users.InputSnils.sendKeys(text);
        System.out.println("Редактирование существующего пользователя");
        /** Проверка, что добавленный профиль отображается */
        Thread.sleep(1500);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);
        WaitElement(users.OneWait);
        users.Edit.click();
        /** Изменение данных о месте работы */
        System.out.println("Изменение данных о месте работы");
        WaitElement(users.EditWait);
        SelectClickMethod(users.SelectWork, users.WorkOkb);
        SelectClickMethod(users.SelectDivision, users.Division5);
        if (KingNumber == 1) {
            SelectClickMethod(users.SelectRoleUser, users.RoleUserTest213);
        }
        if (KingNumber == 2) {
            SelectClickMethod(users.SelectRoleUser, users.RoleUserTest2);
        }
        if (KingNumber == 4) {
            SelectClickMethod(users.SelectRoleUser, users.RoleUserTest999);
        }
        SelectClickMethod(users.DateDismiss, users.DateDismissToday);
        /** Обновляем */
        users.UpdateWork.click();
        WaitNotElement(users.EditWait);
        /** Изменение данных о профиле */
        users.Profile.click();
        WaitElement(users.HeaderProfileWait);
        users.EditProfile.click();
        SelectClickMethod(users.SelectMedProfile, users.MedProfile);
        users.EditProfileDate.click();
        WaitElement(users.BottomStartWait);
        WaitElement(users.EditProfileDateTodayWait);
        users.EditProfileDateToday.click();
        WaitNotElement(users.BottomStartWait);
        users.EditProfileApply.click();
        Thread.sleep(300);
        users.EditProfileClose.click();
        users.Update.click();
        Thread.sleep(1500);
        System.out.println("Данные пользователя отредактированы");
    }

    @Issue(value = "TEL-443")
    @Link(name = "ТМС-944", url = "https://team-1okm.testit.software/projects/5/tests/944")
    @Owner(value = "Галиакберов Тимур")
    @Order(2)
    @Test
    @Story("Администрирование")
    @Description("Переход в Аудит пользователя и проверка всех действий")
    @DisplayName("Проверка Аудита пользователя")
    public void AuditUser() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        auditUsers = new AuditUsers(driver);
        System.out.println("Проверка Аудита пользователя");
        AuthorizationMethod(authorizationObject.MIAC);
        /** Переход в Аудит пользователя */
        WaitElement(auditUsers.AuditUsersWait);
        actionElementAndClick(auditUsers.AuditUsers);
        WaitElement(auditUsers.HeaderAuditUsersWait);
        WaitElement(auditUsers.DataStartWait);
        auditUsers.DataStart.click();
        WaitElement(authorizationObject.BottomStart);
        String number = auditUsers.DataToday.getText();
        auditUsers.DataToday.click();
        driver.findElement(
                By.xpath("(//div[@x-placement='bottom-start']//td//span[contains(.,'" + number + "')])[1]")).click();
        WaitNotElement(authorizationObject.BottomStart);
        /** Проверка действий */
        AssertAudit(auditUsers.CreateUser, auditUsers.CreateUserWait, "Создание пользователя");
        AssertAudit(auditUsers.AddWork, auditUsers.AddWorkWait, "Добавление места работы пользователю");
        AssertAudit(auditUsers.AddRole, auditUsers.AddRoleWait, "Добавление роли пользователю");
        AssertAudit(auditUsers.AddProfile, auditUsers.AddProfileWait, "Добавление профиля пользователю");
        AssertAudit(auditUsers.DismissProfile, auditUsers.DismissProfileWait, "Увольнение пользователя по профилю");
        AssertAudit(auditUsers.DismissWork, auditUsers.DismissWorkWait, "Увольнение пользователя с места работы");
        AssertAudit(auditUsers.EditRole, auditUsers.EditRoleWait, "Редактирование роли пользователю");
        AssertAudit(auditUsers.EditProfile, auditUsers.EditProfileWait, "Редактирование профиля пользователю");
        AssertAudit(auditUsers.EditWork, auditUsers.EditWorkWait, "Редактирование места работы пользователю");
    }
}



