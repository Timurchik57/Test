package UI.TmTest.AccessUI.Administration;

import UI.TestListener;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import api.Access_1786Test;
import api.BaseAPI;
import api.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Отображение данных из регистра Профилактика")
public class Access_1791Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;
    RegisterDispensaryPatients registerDispensaryPatients;
    public Integer number;
    public Integer patients;
    public Integer patients2;

    @Issue(value = "TEL-1791")
    @Link(name = "ТМС-1772", url = "https://team-1okm.testit.software/projects/5/tests/1772?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @Description("Создаём новый регистр с Профилактикой - переходим в данный регистр и проверяем добавленную запись из таблицы prevention_sms_v5_register")
    @Test
    @Story("Администрирование")
    @DisplayName("Отображение данных из регистра Профилактика")
    public void Access_1791() throws SQLException, IOException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        Access_1786Test access_1786Test = new Access_1786Test();
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        number = 1;

        System.out.println("Переходим в Типы регистров");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(typeRegistr.TypeRegistrWait);

        System.out.println("Проверяем создан ли нужный регистр");
        while (!isElementNotVisible(By.xpath("//table//tbody//tr//span[contains(.,'Регистр Профилактики')]"))) {
            if (isElementNotVisible(typeRegistr.NextDisabled) == true) {
                number = 0;
                break;
            } else {
                typeRegistr.Next.click();
            }
        }
        if (number == 0) {
            System.out.println("Добавление регистра");
            ClickElement(typeRegistr.AddRegistrWait);
            WaitElement(typeRegistr.HeaderAddRegistrWait);
            inputWord(typeRegistr.InputNameRegistr, "Регистр Профилактикии");
            inputWord(typeRegistr.InputShortNameRegistr, "Регистр Профилактикии");
            SelectClickMethod(typeRegistr.SourceData, typeRegistr.SelectSourceDataPrev);
            typeRegistr.AddDiagnosis.click();
            WaitElement(typeRegistr.SelectAddDiagnosisWait);
            inputWord(typeRegistr.CodRegistr, "A000");
            ClickElement(typeRegistr.FirstCodRegistrWait);
            ClickElement(typeRegistr.AddCodRegistrWait);
            Thread.sleep(1500);
        }
        ClickElement(registerDispensaryPatients.RegistrPrev);
        WaitElement(registerDispensaryPatients.FIO);
        List<WebElement> WebList = driver.findElements(registerDispensaryPatients.Patients);
        patients = WebList.size();
        System.out.println(patients);

        InputProp("src/test/resources/my.properties", "SizePatients_1791", String.valueOf(patients));

        System.out.println("Отправляем нужную смс");
        access_1786Test.Access_1786Method("SMS/SMS5-vmcl=2.xml", "5", 2, 6, 3, 6, 4, 18, 1, 57, 21,
                                          "vimis.preventionsms", "vimis.preventionlogs");
    }

    @Step("Переходим к Регистр Профилактики и проверяем, что запись добавились")
    public void Access_1791After(String NameProp) throws IOException, InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        number = 1;

        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        System.out.println("Переходим в Регистр Профилактики");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(registerDispensaryPatients.RegistrPrev);
        WaitElement(registerDispensaryPatients.FIO);
        List<WebElement> WebList = driver.findElements(registerDispensaryPatients.Patients);
        patients2 = WebList.size();

        Assertions.assertNotEquals(Integer.valueOf("" + props.getProperty("" + NameProp + "") + ""), patients2,
                                   "Количество пациентов не увеличилось");
    }
}
