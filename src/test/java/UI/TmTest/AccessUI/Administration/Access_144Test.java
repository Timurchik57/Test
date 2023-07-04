package UI.TmTest.AccessUI.Administration;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("В ФИО специалиста убирать значения из списка")
public class Access_144Test extends BaseTest {
    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;

    @Test
    @Story("Администрирование")
    @Issue(value = "TEL-144")
    @Link(name = "ТМС-1529", url = "https://team-1okm.testit.software/projects/5/tests/1529?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("В ФИО специалиста убирать значения из списка")
    @Description("Переходим в администрирование - типы регистров - Добавить регистр - перейти в специалисты, выбрать специалиста и проверить, что больше не отображается")
    public void Access_144() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        AuthorizationMethod(authorizationObject.OKB);
        /** Переход в Типы регистров */
        WaitElement(typeRegistr.TypeRegistrWait);
        ClickElement(typeRegistr.TypeRegistrWait);
        /** Добавление регистра */
        System.out.println("Добавление регистра");
        ClickElement(typeRegistr.AddRegistrWait);
        WaitElement(typeRegistr.HeaderAddRegistrWait);
        inputWord(typeRegistr.InputNameRegistr, "ТЕСТТ");
        inputWord(typeRegistr.InputShortNameRegistr, "ТЕСТОВИЧЧ");
        ClickElement(typeRegistr.Specialists);
        ClickElement(typeRegistr.AddSpecialists);
        System.out.println("Ищем специалиста и добавляем его");
        inputWord(driver.findElement(typeRegistr.SearchAddSpecialists), "АБАБКОВ ");
        Thread.sleep(1000);
        if (KingNumber == 4) {
            Thread.sleep(4000);
        }
        actionElementAndClick(driver.findElement(typeRegistr.ABABKOV));
        ClickElement(typeRegistr.AddSpecialists);
        System.out.println("Ищем этого же специалиста и проверяем, что не отображается");
        inputWord(driver.findElement(typeRegistr.SearchAddSpecialists), "АБАБКОВ ");
        Thread.sleep(1000);
        if (KingNumber == 4) {
            Thread.sleep(4000);
        }
        WaitNotElement(typeRegistr.ABABKOVWait);
    }
}
