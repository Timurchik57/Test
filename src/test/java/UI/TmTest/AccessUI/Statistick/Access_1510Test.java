package UI.TmTest.AccessUI.Statistick;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
public class Access_1510Test extends BaseTest {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;

    /**
     * Метод для выбора направления
     */
    public void ChoiceDirections(
            By Research, Boolean Direction
    ) throws InterruptedException, SQLException, IOException {
        analyticsMO = new AnalyticsMO(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
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
        ClickElement(analyticsMO.Action);
        System.out.println("Выбираем создание направления на диагностику");
        ClickElement(Research);
        Thread.sleep(1500);
        if (Direction) {
            directionsForQuotas.CreateConsultationMethod("500", false, directionsForQuotas.SelectResearch, false);
            WaitElement(equipmentSchedule.WriteWait);
            if (KingNumber == 1 | KingNumber == 2) {
                WaitElement(equipmentSchedule.AddFileWait);
                File file = new File("src/test/resources/test.txt");
                equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
                Thread.sleep(1000);
                ClickElement(equipmentSchedule.WriteTwo);
            }
            if (KingNumber == 4) {
                Thread.sleep(3000);
                ClickElement(equipmentSchedule.WriteTwo);
            }
            Thread.sleep(2000);
            WaitElement(equipmentSchedule.AlertWait);
            equipmentSchedule.AlertClose.click();
        } else {
            directionsForQuotas.CreateConsul(false, directionsForQuotas.SelectDoctorSecond, false);
        }
    }

    @Test
    @Issue(value = "TEL-1510")
    @Link(name = "ТМС-1564", url = "https://team-1okm.testit.software/projects/5/tests/1564?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Создание направления на диагностику/консультацию через ЛК Врача")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок гдк есть маршруты, переходим к пациенту, нажимаем действия и создаём направление на диагностику, консультацию")
    public void Access_1510() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();
        directionsForQuotas = new DirectionsForQuotas(driver);
        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        ChoiceDirections(analyticsMO.ActionAddDiagnostic, true);
        ChoiceDirections(analyticsMO.ActionAddKonsul, false);
    }
}
