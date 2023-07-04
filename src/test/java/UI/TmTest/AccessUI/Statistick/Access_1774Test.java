package UI.TmTest.AccessUI.Statistick;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
public class Access_1774Test extends BaseTest {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    DirectionsForQuotas directionsForQuotas;

    @Test
    @Issue(value = "TEL-1774")
    @Link(name = "ТМС-1768", url = "https://team-1okm.testit.software/projects/5/tests/1768?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Скачивание документа из ЛК Врача")
    @Description("Переходим в Лк Врача и скачиваем любой документ")
    public void Access_1774() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

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
        System.out.println("Выбираем нужного пациента и переходим к нему");
        if (KingNumber == 1) {
            ClickElement(analyticsMO.SecondPatient);
        } else {
            ClickElement(analyticsMO.FirstPatient);
        }
        WaitElement(analyticsMO.Snils);
        WaitNotElement3(analyticsMO.LoadingDocs, 20);
        WaitNotElement3(analyticsMO.LoadingDocs, 20);
        ClickElement(analyticsMO.Download);
        Thread.sleep(1500);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
    }
}
