package UI.TmTest.AccessUI.Directions;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.JavascriptExecutor;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class Access_1151Test extends BaseTest {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;

    @Test
    @Issue(value = "TEL-1151")
    @Link(name = "ТМС-1428", url = "https://team-1okm.testit.software/projects/5/tests/1428?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Некорректное отображение данных о пациенте при поиске по снилс в РРП")
    @Description("Перейти в Консультации - создать консультацию на диагностику, произвести поиск через Расширенный поиск и без Расширенного одного и такого же пользователя, данные должны совпадать")
    public void Access_1151() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        System.out.println("Авторизация и переход в создание консультации");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        System.out.println("Создать консультацию - добавить пациента");
        WaitElement(directionsForQuotas.Heading);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.DistrictDiagnosticWait);
        ClickElement(directionsForQuotas.NextWait);
        ClickElement(directionsForQuotas.BigSearchWait);
        System.out.println("Ищем пациента");
        WaitElement(directionsForQuotas.LastNameWait);
        inputWord(directionsForQuotas.LastName, "Ивановв");
        ClickElement(directionsForQuotas.SearchWait);
        System.out.println("Берём его снилс и ЕНП");
        WaitElement(directionsForQuotas.listPatientFirstnils);
        String snils = driver.findElement(directionsForQuotas.listPatientFirstnils).getText();
        ClickElement(directionsForQuotas.listPatientFirst);
        ClickElement(directionsForQuotas.Choose);
        System.out.println("Проверяем, что выбраны нужные пункты");
        WaitElement(directionsForQuotas.ManWaitTrue);
        if (isElementNotVisible(directionsForQuotas.VahtTrue)) {
            WaitElement(directionsForQuotas.VahtTrue);
        } else {
            WaitElement(directionsForQuotas.VahtFalse);
        }
        JavascriptExecutor js = driver;
        String Type = (String) js.executeScript(
                "return arguments[0].value", driver.findElement(directionsForQuotas.TypeDocument));
        System.out.println(Type);
        System.out.println("Сбрасываем форму и ищем пациента по снилсу без расширенного поиска");
        ClickElement(directionsForQuotas.ResetForm);
        directionsForQuotas.Snils.sendKeys(snils);
        System.out.println("Проверяем, что выбраны нужные пункты");
        WaitElement(directionsForQuotas.ManWaitTrue);
        if (isElementNotVisible(directionsForQuotas.VahtTrue)) {
            WaitElement(directionsForQuotas.VahtTrue);
        } else {
            WaitElement(directionsForQuotas.VahtFalse);
        }
        String Type2 = (String) js.executeScript(
                "return arguments[0].value", driver.findElement(directionsForQuotas.TypeDocument));
        System.out.println(Type);
        Assertions.assertEquals(Type, Type2, "Документы не совпадают");
    }
}
