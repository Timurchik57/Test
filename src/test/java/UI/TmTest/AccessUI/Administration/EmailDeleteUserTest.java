package UI.TmTest.AccessUI.Administration;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Ввод email удалённого пользователя ")
public class EmailDeleteUserTest extends BaseTest {
    AuthorizationObject authorizationObject;
    Users users;

    @Issue(value = "TEL-633")
    @Link(name = "ТМС-1181", url = "https://team-1okm.testit.software/projects/5/tests/1181")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Администрирование")
    @DisplayName("Авторизация и переход в роли доступа")
    @Description("Создаём пользователя с email, удаляем пользователя вчерашним числом. Создаём нового пользователя, проверяем, что тот же email можно использовать")
    public void EmailDeleteUser() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);

        System.out.println("Проверяем, что нет нужного mail в системе");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(users.UsersWait);
        WaitElement(users.EmailSearch);
        inputWord(driver.findElement(users.EmailSearch), "trewq@mail.ru ");
        ClickElement(users.SearchWait);
        Thread.sleep(1000);
        if (isElementNotVisible(users.FirstUser) == true) {
            ClickElement(users.EditUserWaitOne);
            WaitElement(users.SnilsInputWait);
            /** Проверка, что добавленный профиль отображается */
            WaitElement(users.OneWait);
            System.out.println("Изменение данных о месте работы");
            users.Edit.click();
            WaitElement(users.EditWait);
            SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);
            /** Обновляем */
            users.UpdateWork.click();
            WaitNotElement(users.EditWait);
            WaitElement(users.SnilsInputWait);
            actionElementAndClick(users.Update);
        }

        System.out.println("Добавление нового пользователя");
        /** Генерация снилса для добавления пользователя, Авторизация, переход в пользователи и ввод сгенерированного СНИЛС*/
        authorizationObject.GenerationSnilsAndAuthorizationMethod();
        /** Заполнение данных пользователя */
        System.out.println("Заполнение данных пользователя");
        AddUsersMethod(users.WorkYatskiv, users.DivisionChildPolyclinic, users.RoleUser5, "trewq@mail.ruu");
        /** Редактирование роли, профиля и добавление даты увольнения */
        System.out.println("Редактирование роли, профиля и добавление даты увольнения");
        WaitElement(users.AddUserWait);
        Thread.sleep(1000);
        users.AddUser.click();
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
        System.out.println("Редактирование существующего пользователя");
        /** Проверка, что добавленный профиль отображается */
        if (KingNumber == 4) {
            Thread.sleep(2000);
        }
        users.ButtonSearch.click();
        WaitElement(users.OneWait);
        /** Изменение данных о месте работы */
        System.out.println("Изменение данных о месте работы");
        users.Edit.click();
        WaitElement(users.EditWait);
        SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);
        /** Обновляем */
        users.UpdateWork.click();
        WaitNotElement(users.EditWait);
        WaitElement(users.SnilsInputWait);
        actionElementAndClick(users.Update);
        Thread.sleep(1500);
        /** Генерация снилса для добавления пользователя, Авторизация, переход в пользователи и ввод сгенерированного СНИЛС*/
        authorizationObject.GenerationSnilsAndAuthorizationMethod();
        /** Заполнение данных пользователя */
        System.out.println("Заполнение данных пользователя");
        AddUsersMethod(users.WorkYatskiv, users.DivisionChildPolyclinic, users.RoleUser5, "trewq@mail.ruu");
        System.out.println("Email удалённого пользователя можно использовать!");
        String Snils1 = authorizationObject.GenerationSnils;
        /** Проверка на ошибку ввода уже существующего email */
        authorizationObject.GenerationSnilsAndAuthorizationMethod();
        /** Заполнение данных пользователя с вводом существующего email проверкой появления ошибки - Данный email-адрес уже существует в системе  */
        WaitElement(users.EmailWait);
        inputWord(users.Email, "trewq@mail.ruu");
        WaitElement(users.Fail);
        actionElementAndClick(users.Close);
        /** Удаление созданного профиля, для возможности переиспользовать данный тест */
        WaitElement(users.HeaderUsersWait);
        users.AlertClose.click();
        Thread.sleep(1500);
        users.AlertClose.click();
        WaitElement(users.AddUserWait);
        users.AddUser.click();
        users.InputSnils.sendKeys(Snils1);
        users.ButtonSearch.click();
        WaitElement(users.SnilsInputWait);
        /** Проверка, что добавленный профиль отображается */
        WaitElement(users.OneWait);
        System.out.println("Изменение данных о месте работы");
        users.Edit.click();
        WaitElement(users.EditWait);
        SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);
        /** Обновляем */
        users.UpdateWork.click();
        WaitNotElement(users.EditWait);
        WaitElement(users.SnilsInputWait);
        actionElementAndClick(users.Update);
    }
}
