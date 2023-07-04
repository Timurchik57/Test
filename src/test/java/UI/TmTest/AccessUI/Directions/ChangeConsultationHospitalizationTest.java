package UI.TmTest.AccessUI.Directions;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class ChangeConsultationHospitalizationTest extends BaseTest {
    AuthorizationObject authorizationObject;
    ConsultationUnfinished consultationUn;
    DirectionsForQuotas directionsForQuotas;
    SQL sql;

    @Test
    @Issue(value = "TEL-495")
    @Link(name = "ТМС-1272", url = "https://team-1okm.testit.software/projects/5/tests/1272?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Смена цели консультации на госпитализацию")
    @Description("Переходим в Направления - Консультации - Незавершённые - Выбираем консультацию со статусом отправлен/уточнить и нажимаем Требуется госпитализация. После проверяем в бд, что у данной консультации статус = 19(В очереди)")
    public void ChangeConsultationHospitalization() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        consultationUn = new ConsultationUnfinished(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        sql = new SQL();
        System.out.println("Авторизуемся и переходим в создание удалённой консультации");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        ClickElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        System.out.println("Заполняем данные");
        SelectClickMethod(directionsForQuotas.DoctorWait, directionsForQuotas.SelectDoctor);
        if (KingNumber == 1 | KingNumber == 2) {
            SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMO);
        }
        if (KingNumber == 4) {
            SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMOKon);
        }
        SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.SelectFirst);
        inputWord(directionsForQuotas.Diagnos, "AA");
        Thread.sleep(2000);
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.Plan);
        SelectClickMethod(directionsForQuotas.Goal, directionsForQuotas.SelectGoal);
        ClickElement(directionsForQuotas.NextConsul);
        ClickElement(directionsForQuotas.CreateConsul);
        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.AddFilesWait);
        Thread.sleep(1000);
        File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(500);
        ClickElement(directionsForQuotas.SendConsul);
        /**  Переход в Направления - Консультации - Незавершённые **/
        System.out.println("Переход в Направления - Консультации - Незавершённые ");
        if (KingNumber != 4) {
            AuthorizationMethod(authorizationObject.YATCKIV);
        } else {
            AuthorizationMethod(authorizationObject.Kondinsk);
        }
        ClickElement(consultationUn.UnfinishedWait);
        WaitElement(consultationUn.Header);
        WaitElement(consultationUn.FirstWait);
        /** Берём количество записей на странице с "Отправлено" и выбираем только те, которые "Плановые" **/
        System.out.println(
                "Берём количество записей на странице c \"Отправлено\" и выбираем только те, которые \"Плановые\"");
        List<Integer> QuantityNumbers = new ArrayList<Integer>();
        List<WebElement> quantity = driver.findElements(consultationUn.FormWait);
        for (int i = 1; i < quantity.size() + 1; i++) {
            String form = driver.findElement(By.xpath(
                    "(//tbody/tr/td/div/span[contains(.,'Отправлен')]/ancestor::td/following-sibling::td//span)[" + i + "]")).getText();
            if ((form.equals("плановая"))) {
                consultationUn.Number = i;
                QuantityNumbers.add(i);
            }
        }
        System.out.println(QuantityNumbers);
        /** Запоминаем id этой записи **/
        System.out.println("Запоминаем id этой записи");
        String id = driver.findElement(By.xpath(
                "(//tbody/tr/td/div/span[contains(.,'Отправлен')]/ancestor::td/following-sibling::td//span)[" + QuantityNumbers.get(
                        0) + "]/preceding::td[8]")).getText();
        System.out.println(id);
        /** Переходим и нажимаем Требуется госпитализация **/
        System.out.println("Переходим и нажимаем Требуется госпитализация");
        driver.findElement(By.xpath(
                "(//tbody/tr/td/div/span[contains(.,'Отправлен')]/ancestor::td/following-sibling::td//span)[" + QuantityNumbers.get(
                        0) + "]")).click();
        WaitElement(consultationUn.RequiredWait);
        actionElementAndClick(consultationUn.Required);
        WaitElement(consultationUn.YesWait);
        consultationUn.Yes.click();
        Thread.sleep(1500);
        String status = null;
        sql.StartConnection("Select d.status from telmed.directions d where id = '" + id + "'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("status");
            status = sql.value;
        }
        System.out.println(status);
        Assertions.assertEquals(status, "19");
    }
}
