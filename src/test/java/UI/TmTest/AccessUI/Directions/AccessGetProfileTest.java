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

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class AccessGetProfileTest extends BaseTest {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    String word;

    @Issue(value = "TEL-540")
    @Link(name = "ТМС-1210", url = "https://team-1okm.testit.software/projects/5/tests/1210?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отображения одного запроса при выборе МО в создании консультации")
    @Description("Переходим в Направления - Консультации - Незавершённые - Создание Консультации, удалённая консультация. Выбор МО и проверка, что отображается один запрос в Network")
    public void AccessGetProfile() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        System.out.println("Проверка отображения одного запроса при выборе МО в создании консультации");
        /**  Переход в Направления - Консультации - Незавершённые - Создание Консультации **/
        System.out.println("Переход в Направления - Консультации - Незавершённые - Создание Консультации");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(directionsForQuotas.ConsultationWait);
        actionElementAndClick(directionsForQuotas.Consultation);
        WaitElement(directionsForQuotas.CreateWait);
        directionsForQuotas.Create.click();
        /**  Выбор Удалённой консультации **/
        System.out.println("Выбор Удалённой консультации");
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.RemoteConsultation.click();
        directionsForQuotas.Next.click();
        /**  Ввод снилса существующего пользователя **/
        System.out.println("Ввод снилса существующего пользователя");
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);
        /**  Выбор МО, куда направлен **/
        System.out.println("Выбор МО, куда направлен");
        WaitElement(directionsForQuotas.MOWait);
        Thread.sleep(1000);
        SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMO);
        SelectClickMethod(directionsForQuotas.ProfileWait, directionsForQuotas.SelectProfileFirst);
        /** Берём список всех запросов и определяем нужный **/
        String scriptToExecute = "var performance = window.performance || window.mozPerformance || window.msPerformance || window.webkitPerformance || {}; var network = performance.getEntries() || {}; return network;";
        String netData = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();

        if (KingNumber == 1) {
            word = "name=https://tm-test.pkzdrav.ru/nsi/profiledirectory/search?moId=110";
        }
        if (KingNumber == 2) {
            word = "name=https://tm-dev.pkzdrav.ru/nsi/profiledirectory/search?moId=110";
        }
        if (KingNumber == 4) {
            word = "name=https://remotecons-test.miacugra.ru/nsi/profiledirectory/search?moId=57";
        }

        /** Проверяем, что нужный запрос отправляется один раз **/
        System.out.println("Проверяем, что нужный запрос отправляется один раз");
        netData.contains(word);
        directionsForQuotas.searchWord(netData, word);
        System.out.println(directionsForQuotas.CountAsk);
        Assertions.assertEquals(directionsForQuotas.CountAsk, 1);
    }
}
