package UI.TmTest.AccessUI.Regisrty;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Регистры")
public class RegisterDispensariesTest extends BaseTest {
    AuthorizationObject authorizationObject;
    RegisterDispensaryPatients registerDB;
    TypeRegistr typeRegistr;

    @Issue(value = "TEL-614")
    @Link(name = "ТМС-1183", url = "https://team-1okm.testit.software/projects/5/tests/1183?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование регистра диспансерных больных")
    @Description("Формирование регистра диспансерных больных, выбор любого пациента и его диагноза. Переходим в Типы регистров и убираем данный диагноз. Проверяем, что при формировании регистра диспансерных больных, диагноз не отображается")
    public void RegisterDispensaries() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        registerDB = new RegisterDispensaryPatients(driver);
        typeRegistr = new TypeRegistr(driver);
        if (KingNumber == 1) {
            AuthorizationMethod(authorizationObject.OKB);
            /**  Переход Регистр Диспансерных больных - ССЗ **/
            System.out.println("Переход Регистр Диспансерных больных - ССЗ");
            if (KingNumber != 4) {
                ClickElement(registerDB.RegistrDBSSZWait);
            } else {
                ClickElement(registerDB.RegistrDBSSZWait);
            }
            /** Выбор диагноза из списка **/
            System.out.println("Выбор диагноза из списка");
            WaitElement(registerDB.HeaderSSZ);
            WaitElement(registerDB.RegistrDBDiagnosisSSZ);
            List<String> GetDiagnosis = new ArrayList<String>();
            List<WebElement> Diagnosis = driver.findElements(registerDB.RegistrDBDiagnosisSSZ);
            for (int i = 0; i < Diagnosis.size(); i++) {
                GetDiagnosis.add(Diagnosis.get(i).getText());
            }
            System.out.println("Выбираем диагноз " + GetDiagnosis.get(0));
            typeRegistr.NameDiagnosis = GetDiagnosis.get(0);
            System.out.println(typeRegistr.NameDiagnosis);
            /** Переход в Типы регистров **/
            System.out.println("Переход в Типы регистров");
            WaitElement(typeRegistr.TypeRegistrWait);
            actionElementAndClick(typeRegistr.TypeRegistr);
            WaitElement(typeRegistr.Header);
            System.out.println("Выбираем ССЗ");
            if (KingNumber != 4) {
                while (!isElementNotVisible(typeRegistr.SSZ)) {
                    ClickElement(typeRegistr.NextWait);
                }
                WaitElement(typeRegistr.SSZ);
                ClickElement(typeRegistr.EditSSZWait);
            } else {
                while (!isElementNotVisible(typeRegistr.ONKO)) {
                    ClickElement(typeRegistr.NextWait);
                }
                WaitElement(typeRegistr.ONKO);
                ClickElement(typeRegistr.EditONKO);
            }
            WaitElement(typeRegistr.HeaderUpdate);
            WaitElement(typeRegistr.FirstListDiagnosis);
            /** Ищем нужное название диагноза и удаляем **/
            System.out.println("Ищем нужное название диагноза и удаляем");
            List<WebElement> ListRegistr = driver.findElements(typeRegistr.ListDiagnosis);
            for (int i = 0; i < ListRegistr.size(); i++) {
                if ((typeRegistr.NameDiagnosis.contains(ListRegistr.get(i).getText()))) {
                    typeRegistr.TrueNumber = i;
                    typeRegistr.SelectedDiagnosis = ListRegistr.get(i - 1).getText();
                }
            }
            /** Удаляем нужный диагноз **/
            System.out.println("Удаляем нужный диагноз");
            driver.findElement(By.xpath(
                    "((//div[@class='margin-bottom-3']/strong/span)[" + typeRegistr.TrueNumber + "]/following::span)[2]")).click();
            WaitElement(typeRegistr.AlertDeleteDiagnosisWait);
            typeRegistr.AlertDeleteDiagnosis.click();
            typeRegistr.Update.click();
            Thread.sleep(1500);
            /**  Переход Регистр Диспансерных больных - ССЗ **/
            System.out.println("Переход Регистр Диспансерных больных - ССЗ");
            if (KingNumber != 4) {
                ClickElement(registerDB.RegistrDBSSZWait);
            } else {
                ClickElement(registerDB.RegistrDBSSZWait);
            }
            /** Проверяем, что нет удалённого диагноза **/
            System.out.println("Проверяем, что нет удалённого диагноза");
            Thread.sleep(1500);
            List<WebElement> DiagnosisUpdate = driver.findElements(registerDB.RegistrDBDiagnosisSSZ);
            for (int i = 0; i < DiagnosisUpdate.size(); i++) {
                Assertions.assertNotEquals(DiagnosisUpdate.get(i).getText(), typeRegistr.NameDiagnosis);
            }
            /** Переход в Типы регистров для обратного добавления удалённого диагноза **/
            System.out.println("Переход в Типы регистров для обратного добавления удалённого диагноза");
            WaitElement(typeRegistr.TypeRegistrWait);
            actionElementAndClick(typeRegistr.TypeRegistr);
            WaitElement(typeRegistr.Header);
            /** Выбираем ССЗ **/
            System.out.println("Выбираем ССЗ");
            if (KingNumber != 4) {
                while (!isElementNotVisible(typeRegistr.SSZ)) {
                    ClickElement(typeRegistr.NextWait);
                }
                WaitElement(typeRegistr.SSZ);
                ClickElement(typeRegistr.EditSSZWait);
            } else {
                while (!isElementNotVisible(typeRegistr.ONKO)) {
                    ClickElement(typeRegistr.NextWait);
                }
                WaitElement(typeRegistr.ONKO);
                ClickElement(typeRegistr.EditONKO);
            }
            WaitElement(typeRegistr.HeaderUpdate);
            /** Добавляем диагноз **/
            System.out.println("Добавляем диагноз ");
            typeRegistr.AddDiagnosis.click();
            WaitElement(typeRegistr.SelectAddDiagnosisWait);
            inputWord(typeRegistr.CodRegistr, typeRegistr.SelectedDiagnosis + "1");
            Thread.sleep(500);
            typeRegistr.FirstCodRegistr.click();
            WaitNotElement(typeRegistr.SelectAddDiagnosisWait);
            typeRegistr.Update.click();
            Thread.sleep(1500);
        }
    }
}
