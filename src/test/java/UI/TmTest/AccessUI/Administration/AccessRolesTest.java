package UI.TmTest.AccessUI.Administration;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Регистронезависимость Роли Доступа")
public class AccessRolesTest extends BaseTest {
    AuthorizationObject authorizationObject;
    AcceessRoles acceessRoles;

    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Администрирование")
    @DisplayName("Авторизация и переход в роли доступа")
    @Description("Пробуем писать в поиске ролей доступа различными регистрами")
    public void AccessRole() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        acceessRoles = new AcceessRoles(driver);

        AuthorizationMethod(authorizationObject.MIAC);

        /** Переход в Роли Доступа */
        WaitElement(acceessRoles.RolesWait);
        actionElementAndClick(acceessRoles.Roles);

        /** Редактирование роли */
        System.out.println("Редактирование роли");
        WaitElement(acceessRoles.HeaderRoles);
        while (!isElementNotVisible(acceessRoles.RoleAdministrator)) {
            acceessRoles.Next.click();
        }
        actionElementAndClick(acceessRoles.Edit);
        WaitElement(acceessRoles.EditRole);
        WaitElement(acceessRoles.InputWordWait);
        /** Проверка */
        System.out.println("Ввод прописных букв");
        if (KingNumber == 4) {
            inputWord(acceessRoles.InputWord, "Типы регистров");
            Thread.sleep(2500);
            String element = acceessRoles.TypeRegisters.getText();
            Assertions.assertEquals("15. Доступ к разделу \"Типы регистров\"", element);
        } else {
            inputWord(acceessRoles.InputWord, "процедурныйй");
            String element = acceessRoles.ProcedureRoom.getText();
            Assertions.assertEquals("16. Процедурный кабинет", element);
        }
        inputWord(acceessRoles.InputWord, "реестрыы");
        Thread.sleep(1000);
        System.out.println("Ввод заглавных букв");
        if (KingNumber == 4) {
            inputWord(acceessRoles.InputWord, "ТИПЫ РЕГИСТРОВ");
            Thread.sleep(2500);
            String element = acceessRoles.TypeRegisters.getText();
            Assertions.assertEquals("15. Доступ к разделу \"Типы регистров\"", element);
        } else {
            inputWord(acceessRoles.InputWord, "ПРОЦЕДУРНЫЙЙ");
            String element = acceessRoles.ProcedureRoom.getText();
            Assertions.assertEquals("16. Процедурный кабинет", element);
        }
        inputWord(acceessRoles.InputWord, "реестрыы");
        Thread.sleep(1000);
        System.out.println("Ввод заглавных и прописных букв");
        if (KingNumber == 4) {
            inputWord(acceessRoles.InputWord, "ТиПы РЕГиСТРоВ");
            Thread.sleep(2500);
            String element = acceessRoles.TypeRegisters.getText();
            Assertions.assertEquals("15. Доступ к разделу \"Типы регистров\"", element);
        } else {
            inputWord(acceessRoles.InputWord, "ПрОцЕдУрНыЙй");
            String element = acceessRoles.ProcedureRoom.getText();
            Assertions.assertEquals("16. Процедурный кабинет", element);
        }
        Thread.sleep(1000);
        System.out.println("Поиск работает корректно!");
    }
}
