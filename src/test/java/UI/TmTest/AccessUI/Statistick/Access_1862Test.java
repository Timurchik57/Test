package UI.TmTest.AccessUI.Statistick;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
public class Access_1862Test extends BaseTest {

    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    DirectionsForQuotas directionsForQuotas;

    public boolean Result(Integer integer) {
        if (integer > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Test
    @Issue(value = "TEL-1862")
    @Link(name = "ТМС-1787", url = "https://team-1okm.testit.software/projects/5/tests/1787?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Поиск по документам в ЛК Врача")
    @Description("Переходим в Лк Врача и вбиваем в поиск и смотрим фильтрацию по запросу")
    public void Access_1862() throws SQLException, InterruptedException, IOException {
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
        Thread.sleep(3000);

        List<WebElement> list = driver.findElements(analyticsMO.Docs);
        System.out.println(list.size());
        inputWord(driver.findElement(analyticsMO.Search), "редакцияя");
        Thread.sleep(3000);

        List<WebElement> list2 = driver.findElements(analyticsMO.Docs);
        System.out.println(list2.size());

        Integer number = list.size() - list2.size();
        System.out.println(number);
        Assertions.assertEquals(Result(number), true, "Поиск не работает");
    }
}
