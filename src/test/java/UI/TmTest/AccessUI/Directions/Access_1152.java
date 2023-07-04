package UI.TmTest.AccessUI.Directions;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class Access_1152 extends BaseTest {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    ConsultationUnfinished consultationUn;
    ConsultationOutgoingUnfinished consultationOut;
    SQL sql;

    @Issue(value = "TEL-1152")
    @Link(name = "ТМС-1450", url = "https://team-1okm.testit.software/projects/5/tests/1450?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Очное консультирование в удаленном консультировании")
    @Description("Перейти в Консультации - Создать направление - Направление на консультацию - Создаём направление, указывая цель консультации - Очное консультирование. В исходящих выделена синим. Переходим в МО, куда отправили - входящие, выделена синим, после назначения времени, также выделена синим")
    public void Access_1152() throws IOException, SQLException, InterruptedException {
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
        SelectClickMethod(directionsForQuotas.Goal, directionsForQuotas.SelectOchnoe);
        ClickElement(directionsForQuotas.NextConsul);
        ClickElement(directionsForQuotas.CreateConsul);
        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.AddFilesWait);
        Thread.sleep(1000);
        File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(500);
        ClickElement(directionsForQuotas.SendConsul);
    }
}
