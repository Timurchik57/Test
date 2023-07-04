package UI.TmTest.AccessUI.Administration;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Удаление типа Регистра")
public class DeleteTypeRegistersTest extends BaseTest {
    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;

    @Description("Создание нового регистра. Редактирование. Выбор диагноза, который уже есть в списке. Добавление регистра с уже существующим наименованием. Удаление регистра.")
    @DisplayName("Удаление типа Регистра")
    @Test
    @Story("Администрирование")
    public void DeleteTypeRegister() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);

        System.out.println("Удаление типа Регистра");
        AuthorizationMethod(authorizationObject.OKB);
        /** Переход в Типы регистров */
        ClickElement(typeRegistr.TypeRegistrWait);

        /** Удаление регистра (если есть) */
        System.out.println("Удаление регистра");
        if (isElementNotVisible(typeRegistr.TrueRegistr) == true) {
            actionElementAndClick(typeRegistr.Delete);
            WaitElement(typeRegistr.AlertDelete);
            actionElementAndClick(typeRegistr.DeleteButton);
            WaitElement(typeRegistr.DeleteSuccess);
            System.out.println("Данные успешно удалены");
        } else {
            typeRegistr.Next.click();
            if (isElementNotVisible(typeRegistr.TrueRegistr) == true) {
                actionElementAndClick(typeRegistr.Delete);
                WaitElement(typeRegistr.AlertDelete);
                actionElementAndClick(typeRegistr.DeleteButton);
                WaitElement(typeRegistr.DeleteSuccess);
                System.out.println("Данные успешно удалены");
            }
        }
        /** Добавление регистра */
        System.out.println("Добавление регистра");
        WaitElement(typeRegistr.Header);
        WaitElement(typeRegistr.AddRegistrWait);
        typeRegistr.AddRegistr.click();
        WaitElement(typeRegistr.HeaderAddRegistrWait);
        inputWord(typeRegistr.InputNameRegistr, "ТЕСТТ");
        inputWord(typeRegistr.InputShortNameRegistr, "ТЕСТОВИЧЧ");
        SelectClickMethod(typeRegistr.SourceData, typeRegistr.SelectSourceDataOnko);
        typeRegistr.AddDiagnosis.click();
        WaitElement(typeRegistr.SelectAddDiagnosisWait);
        inputWord(typeRegistr.CodRegistr, "A000");
        Thread.sleep(2500);
        actionElementAndClick(typeRegistr.FirstCodRegistr);
        actionElementAndClick(typeRegistr.AddCodRegistr);
        WaitElement(typeRegistr.AlertSuccess);
        System.out.println("Регистр добавлен");
        typeRegistr.AlertSuccessClose.click();
        /** Редактирование регистра */
        System.out.println("Редактирование регистра");
        while (!isElementNotVisible(typeRegistr.TrueRegistr)) {
            typeRegistr.Next.click();
        }
        actionElementAndClick(typeRegistr.Edit);
        WaitElement(typeRegistr.HeaderUpdate);
        SelectClickMethod(typeRegistr.SourceData, typeRegistr.SelectSourceDataPrev);
        /** Выбор диагноза, который уже есть в списке */
        System.out.println("Выбор диагноза, который уже есть в списке");
        typeRegistr.AddDiagnosis.click();
        WaitElement(typeRegistr.SelectAddDiagnosisWait);
        inputWord(typeRegistr.CodRegistr, "A000");
        Thread.sleep(500);
        WaitNotElement(typeRegistr.CodRegistrA00);
        /** Выбор промежутка диагнозов */
        System.out.println("Выбор промежутка диагнозов");
        actionElementAndClick(typeRegistr.SecondCodRegistr);
        actionElementAndClick(typeRegistr.LastCodRegistr);
        actionElementAndClick(typeRegistr.Update);
        WaitElement(typeRegistr.UpdateSuccessTrue);
        System.out.println("Регистр отредактирован");
        typeRegistr.AlertSuccessClose.click();
        /** Добавление регистра с уже существующим наименованием */
        System.out.println("Добавление регистра с уже существующим наименованием");
        WaitElement(typeRegistr.Header);
        WaitElement(typeRegistr.AddRegistrWait);
        typeRegistr.AddRegistr.click();
        WaitElement(typeRegistr.HeaderAddRegistrWait);
        inputWord(typeRegistr.InputNameRegistr, "ТЕСТТ");
        WaitElement(typeRegistr.AlertNameRegistr);
        System.out.println("Регистр с таким наименованием уже существует");
        actionElementAndClick(typeRegistr.Close);

        /** Удаление регистра */
        System.out.println("Удаление регистра");
        while (!isElementNotVisible(typeRegistr.TrueRegistr)) {
            typeRegistr.Next.click();
        }
        actionElementAndClick(typeRegistr.Delete);
        WaitElement(typeRegistr.AlertDelete);
        actionElementAndClick(typeRegistr.DeleteButton);
        WaitElement(typeRegistr.DeleteSuccess);
        System.out.println("Данные успешно удалены");
    }
}
