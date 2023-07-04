package UI.TmTest.AccessUI.Statistick;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
public class Access_1115 extends BaseTest {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;

    @Test
    @Description("Авторизация и переход в Детальный отчёт по консультациям. Проверяем корректное отображение, за выбранный период")
    @DisplayName("Проверка отображения детального отчёта по консультации")
    public void ConsultationStatistic() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();
        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.MIAC);
        ClickElement(analyticsMO.Analytics);
        System.out.println("Берём все МО");
        ClickElement(analyticsMO.ChooseMO);
        Thread.sleep(1000);
        List<String> WebMO = new ArrayList<>();
        List<WebElement> MO = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < MO.size(); i++) {
            WebMO.add(MO.get(i).getAttribute("innerText"));
        }
        Collections.sort(WebMO);
        ClickElement(analyticsMO.ChooseMO);
        System.out.println("Берём все Диагнозы");
        ClickElement(analyticsMO.ChooseDiagnosis);
        Thread.sleep(1000);
        List<String> WebDiagnosis = new ArrayList<>();
        List<WebElement> Diagnosis = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < Diagnosis.size(); i++) {
            WebDiagnosis.add(Diagnosis.get(i).getAttribute("innerText"));
        }
        Collections.sort(WebDiagnosis);
        System.out.println("Сверяем МО с БД");
        List<String> SQLMO = new ArrayList<>();
        sql.StartConnection("Select * from dpc.mis_sp_mu where oid is not null and destroydate is null;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("namemu");
            SQLMO.add(sql.value);
        }
        Collections.sort(SQLMO);
        System.out.println("Сверяем Диагнозы с БД");
        List<String> SQLDiagnosis = new ArrayList<>();
        sql.StartConnection("Select * from dpc.mkb10;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("mkb_code");
            String name = sql.resultSet.getString("mkb_name");
            SQLDiagnosis.add(sql.value + " " + name);
        }
        Collections.sort(SQLDiagnosis);
        Assertions.assertEquals(WebMO, SQLMO, "МО не совпадают");
    }
}
