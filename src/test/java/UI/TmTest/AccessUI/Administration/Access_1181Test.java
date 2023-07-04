package UI.TmTest.AccessUI.Administration;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Задвоенные записи")
public class Access_1181Test extends BaseTest {
    AuthorizationObject authorizationObject;
    MedOrganization medOrganization;

    @Issue(value = "TEL-1181")
    @Link(name = "ТМС-1448", url = "https://team-1okm.testit.software/projects/5/tests/1448?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Администрирование")
    @DisplayName("Задвоенные записи")
    @Description("Перейти в Администрирование - Мед организации и проверить, что МО не задваиваются")
    public void Access_1181() throws IOException, SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        medOrganization = new MedOrganization(driver);
        System.out.println("Авторизуемся и переходим в Администрирование - Мед организации");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(medOrganization.OrganizationWait);
        inputWord(
                driver.findElement(medOrganization.InputOrganizationWait),
                "БУ ХМАО-Югры \"Белоярская районная больница\""
        );
        WaitElement(medOrganization.SelectMOOne);
        WaitNotElement(medOrganization.SelectMOTwo);

    }
}
