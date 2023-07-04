package UI.TmTest.AccessUI.Directions;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class Access_1138Test extends BaseTest {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    ConsultationUnfinished consultationUn;
    SQL sql;

    @Issue(value = "TEL-1138")
    @Link(name = "ТМС-1431", url = "https://team-1okm.testit.software/projects/5/tests/1431?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Номер телефона сотрудника запросивший консультацию")
    @Description("Перейти в Консультации - Создать направление - Направление на консультацию - Создаём направление, ищем номер пользователя, которого указали врачом, сверяем с UI")
    public void Access_1138() throws IOException, SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationUn = new ConsultationUnfinished(driver);
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
        SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.SelectFirst);
        inputWord(directionsForQuotas.Diagnos, "AA");
        Thread.sleep(1000);
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
        System.out.println("Авторизуемся под МО в которую направили консультацию");
        if (KingNumber == 1 | KingNumber == 2) {
            AuthorizationMethod(authorizationObject.YATCKIV);
        }
        if (KingNumber == 4) {
            AuthorizationMethod(authorizationObject.Kondinsk);
        }
        ClickElement(consultationUn.UnfinishedWait);
        ClickElement(consultationUn.FirstWait);
        WaitElement(consultationUn.Phone);
        String Phone = driver.findElement(consultationUn.Phone).getText();
        sql.StartConnection("SELECT phone FROM telmed.users \n" +
                                    "WHERE sname = 'Хакимова'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("phone");
        }
        System.out.println("Сверяем номера телефонов");
        Assertions.assertEquals(Phone, sql.value, "Номер телефона не совпадает");
    }
}
