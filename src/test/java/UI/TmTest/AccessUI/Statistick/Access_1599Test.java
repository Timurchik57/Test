package UI.TmTest.AccessUI.Statistick;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
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
public class Access_1599Test extends BaseTest {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;

    @Test
    @Issue(value = "TEL-1599")
    @Link(name = "ТМС-1737", url = "https://team-1okm.testit.software/projects/5/tests/1737?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка скролла в ЛК Врача")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок где есть маршруты, переходим к пациенту и скроллм вниз в ЛК Врача. При прокручивании страницы блок с информацией о пациенте перемещается")
    public void Access_1599() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
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
        actions.moveToElement(driver.findElement(analyticsMO.Footer));
        actions.perform();
        WaitElement(analyticsMO.BlockFIO);
    }
}
