package UI.TmTest.AccessUI.Statistick;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
public class Access_783Test extends BaseTest {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    public boolean TallMO;
    public boolean AverageMO;
    public boolean lowMO;

    /**
     * Метод проверки, где есть нужная МО
     **/
    @Step("Смотрим где есть МО с просроченными сроками и переходим в первую попавшуюся = {0}")
    public void QuantityStackMethod(String NameMO) {
        for (int i = 1; i < 4; i++) {
            List<WebElement> Quantity = driver.findElements(By.xpath(
                    "//div[@class='rating-mo-grid']/div[@class='rating-mo-grid-info'][" + i + "]/div[@class='rating-mo-grid']/div//p[contains(.,'" + NameMO + "')]"));
            if (Quantity.size() > 0 && i == 1) {
                TallMO = true;
                System.out.println("Есть МО в Высокий");
            }
            if (Quantity.size() > 0 && i == 2) {
                AverageMO = true;
                System.out.println("Есть МО в Средний");
            }
            if (Quantity.size() > 0 && i == 3) {
                lowMO = true;
                System.out.println("Есть МО в Низкий");
            }
        }
        if (TallMO) {
            driver.findElement(By.xpath(
                    "//div[@class='rating-mo-grid']/div[@class='rating-mo-grid-info'][1]/div[@class='rating-mo-grid']/div//p[contains(.,'" + NameMO + "')]")).click();
        }
        if (AverageMO) {
            driver.findElement(By.xpath(
                    "//div[@class='rating-mo-grid']/div[@class='rating-mo-grid-info'][2]/div[@class='rating-mo-grid']/div//p[contains(.,'" + NameMO + "')]")).click();
        }
        if (lowMO) {
            driver.findElement(By.xpath(
                    "//div[@class='rating-mo-grid']/div[@class='rating-mo-grid-info'][3]/div[@class='rating-mo-grid']/div//p[contains(.,'" + NameMO + "')]")).click();
        }
    }

    @Test
    @Issue(value = "TEL-783")
    @Link(name = "ТМС-1464", url = "https://team-1okm.testit.software/projects/5/tests/1464?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение аналитики по этапам ОМП")
    @Description("Перейти в Статистика - Аналитика МО по ОМП. Проверить Отображение аналитики по этапам ОМП")
    public void Access_783() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        System.out.println("Авторизуемся и переходим в Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        WaitElement(analyticsMO.StateHeader);
        Thread.sleep(1500);
        System.out.println("Проверяем где есть маршруты, во всех блоках");
        WaitElement(analyticsMO.Tall);
        WaitElement(analyticsMO.Average);
        WaitElement(analyticsMO.low);
        if (KingNumber == 1) {
            QuantityStackMethod("БУ ХМАО-Югры \"Окружной клинический лечебно-реабилитационный центр\"");
        }
        if (KingNumber == 2) {
            QuantityStackMethod("БУ ХМАО-Югры \"Белоярская районная больница\"");
        }
        if (KingNumber == 4) {
            QuantityStackMethod("БУ ХМАО-Югры \"Белоярская районная больница\"");
        }
        Thread.sleep(1500);
        ClickElement(analyticsMO.FirstPatient);
        ClickElement(analyticsMO.RouteFirst);
        Thread.sleep(2000);

        /** Берём список всех запросов и определяем нужный **/
        String scriptToExecute = "var performance = window.performance || window.mozPerformance || window.msPerformance || window.webkitPerformance || {}; var network = performance.getEntries() || {}; return network;";
        String netData = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();
        Assertions.assertEquals(netData.contains("/vimis/pmc/patients/stages/route?snils"), true,
                                "Нет перехода на маршрут пациента");
    }
}
