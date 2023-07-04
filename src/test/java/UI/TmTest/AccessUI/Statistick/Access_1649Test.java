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
import org.openqa.selenium.JavascriptExecutor;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
public class Access_1649Test extends BaseTest {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    public String Host;

    @Test
    @Issue(value = "TEL-1649")
    @Issue(value = "TEL-1639")
    @Issue(value = "TEL-1749")
    @Link(name = "ТМС-1728", url = "https://team-1okm.testit.software/projects/5/tests/1728?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Link(name = "ТМС-1759", url = "https://team-1okm.testit.software/projects/5/tests/1759?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Link(name = "ТМС-1761", url = "https://team-1okm.testit.software/projects/5/tests/1761?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение маршрута у всех диагнозов пациента")
    @Description("Авторизумся и переходим в Лк врача пациента, далее переходим в любой диагноз и проверяем срабатываение метода маршрута - hostaddres/vimis/pmc/patients/stages/route?snils=10071266094&mkb=C64")
    public void Access_1649() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        System.out.println("Проверяем где есть маршруты, во всех блоках");
        WaitElement(analyticsMO.Tall);
        WaitElement(analyticsMO.Average);
        WaitElement(analyticsMO.low);
        analyticsMO.QuantityStackMethod();
        System.out.println(AnalyticsMO.TallMO);
        System.out.println(AnalyticsMO.AverageMO);
        System.out.println(AnalyticsMO.lowMO);
        System.out.println("Переходим в первый попавшийся блок, у которого есть мо с этапами");
        if (AnalyticsMO.TallMO) {
            ClickElement(analyticsMO.NameMOTallFirst);
        } else {
            if (AnalyticsMO.AverageMO) {
                ClickElement(analyticsMO.NameMOAverageFirst);
            } else {
                ClickElement(analyticsMO.NameMOlowFirst);
            }
        }
        System.out.println("Выбираем первого пациента и переходим к нему");
        ClickElement(analyticsMO.FirstPatient);
        WaitElement(analyticsMO.Snils);
        String Snils = driver.findElement(analyticsMO.Snils).getText();
        if (KingNumber == 1) {
            Host = "https://tm-test.pkzdrav.ru";
        }
        if (KingNumber == 2) {
            Host = "https://tm-dev.pkzdrav.ru";
        }
        if (KingNumber == 4) {
            Host = "https://remotecons-test.miacugra.ru";
        }

        Thread.sleep(2500);
        /** Берём список всех запросов и определяем нужный - проверка из заявки 1639 **/
        String scriptToExecute = "var performance = window.performance || window.mozPerformance || window.msPerformance || window.webkitPerformance || {}; var network = performance.getEntries() || {}; return network;";
        String netData = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();
        Assertions.assertTrue(netData.contains("" + Host + "/vimis/nosologicalpatients/"));

        /** Берём список всех запросов и определяем нужный - проверка из заявки 1749 **/
        Assertions.assertTrue(netData.contains("" + Host + "/vimis/sms/patient?patientGuid="));
        Assertions.assertTrue(netData.contains("" + Host + "&patientSnils=" + Snils + ""));

        System.out.println("Переходим к первому диагнозу");
        WaitElement(analyticsMO.FirstDiagnosis);
        String NameDiagnosis = driver.findElement(analyticsMO.FirstDiagnosis).getText();
        ClickElement(analyticsMO.FirstDiagnosis);
        Thread.sleep(3000);

        /** Берём список всех запросов и определяем нужный - проверка из заявки 1649 **/
        netData = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();
        Assertions.assertTrue(netData.contains(
                                      "" + Host + "/vimis/pmc/patients/stages/route?snils=" + Snils + "&mkb=" + NameDiagnosis + ""),
                              "Нет нужного запроса к маршруту пациента");
    }
}
