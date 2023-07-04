package UI.TmTest.AccessUI.Regisrty;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import UI.TmTest.PageObject.VIMIS.SMS;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Регистры")
public class Access_1629Test extends BaseTest {
    AuthorizationObject authorizationObject;
    RegisterDispensaryPatients registerDB;
    TypeRegistr typeRegistr;
    AnalyticsMO analyticsMO;
    SMS sms;

    @Step("Выбираем направление")
    public void Access_1629Method(By District) throws InterruptedException {
        sms = new SMS(driver);
        ClickElement(sms.SMSWait);
        ClickElement(District);
        Thread.sleep(1000);
        System.out.println("Выбор Фильтров");
        sms.Filter.click();
        WaitElement(sms.FilterWait);
        System.out.println("Ввод PatientGuid");
        ClickElement(sms.Ident);
        WaitElement(sms.PatientGuid);
        if (KingNumber == 1) {
            inputWord(driver.findElement(sms.PatientGuid), "4743e15e-488a-44c6-af50-dff0778dd01a ");
        }
        if (KingNumber == 2) {
            inputWord(driver.findElement(sms.PatientGuid), "3791bfa4-c234-43d4-a83c-8d495bca5a55 ");
        }
        if (KingNumber == 4) {
            inputWord(driver.findElement(sms.PatientGuid), "3bd01e31-7a8e-41ef-901e-317a8ec1eff5 ");
        }
        System.out.println("Поиск");
        sms.Search.click();
        Thread.sleep(1500);
        if (KingNumber == 4) {
            WaitNotElement3(sms.Loading, 20);
        }
        if (isElementNotVisible(sms.NotResultSearch)) {
            System.out.println("Нет данных");
        } else {
            ClickElement(sms.FirstLine);
            String link = driver.findElement(sms.FirstLineLKLink).getAttribute("href");
            driver.get(link);
            WaitElement(analyticsMO.FirstDocs);
            WaitElement(analyticsMO.Snils);
        }
    }

    @Issue(value = "TEL-1629")
    @Link(name = "ТМС-1716", url = "https://team-1okm.testit.software/projects/5/tests/1716?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Перенести новый личный кабинет на ссылку старого ЛК врача")
    @Description("Переходим в ЛК Врача из регистров, Структурированных медицинских сведений")
    public void Access_1629() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        registerDB = new RegisterDispensaryPatients(driver);
        typeRegistr = new TypeRegistr(driver);
        analyticsMO = new AnalyticsMO(driver);
        sms = new SMS(driver);
        AuthorizationMethod(authorizationObject.OKB);
        System.out.println("Переход Регистр Диспансерных больных");
        if (KingNumber == 1) {
            ClickElement(registerDB.RegistrDBSSZWait);
            ClickElement(registerDB.FirstPatientLK);
        }
        if (KingNumber == 2) {
            ClickElement(registerDB.RegistrDBZNO);
            ClickElement(registerDB.FirstPatientLKDev);
        }
        if (KingNumber == 4) {
            ClickElement(registerDB.RegistrDBOnkoXmao);
            ClickElement(registerDB.FirstPatientLK);
        }
        WaitElement(analyticsMO.Docs);
        WaitElement(analyticsMO.Snils);
        System.out.println("Переход Вимис - СМС");
        Access_1629Method(sms.OncologyWait);
        Access_1629Method(sms.PreventionWait);
        Access_1629Method(sms.AkineoWait);
        Access_1629Method(sms.SSZWait);
        Access_1629Method(sms.OtherWait);
    }
}
