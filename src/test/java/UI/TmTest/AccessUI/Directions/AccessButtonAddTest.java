package UI.TmTest.AccessUI.Directions;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class AccessButtonAddTest extends BaseTest {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;

    @Issue(value = "TEL-861")
    @Link(name = "ТМС-1250", url = "https://team-1okm.testit.software/projects/5/tests/1250?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отображения кнопок")
    @Description("Переходим в Создание консультации - удалённая консультация - добавить файл и в Консультации - Исходящие - незавершённые Выбрать только что созданную консультацию. Проверить отображение кнопки добавления файлов")
    public void AccessTextConfiguringQueue() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        AuthorizationMethod(authorizationObject.MIAC);
        /** Переходим в Консультации - Создать консультацию - Консультация на диагностику */
        System.out.println("Переходим в Консультации - Создать консультацию - Консультация на диагностику");
        ClickElement(directionsForQuotas.ConsultationWait);
        WaitElement(directionsForQuotas.Heading);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        directionsForQuotas.Next.click();
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        Thread.sleep(1000);
        ClickElement(directionsForQuotas.NextPatientWait);
        /** Создание направления на удаленную консультацию */
        System.out.println("Создание направления на удаленную консультацию");
        WaitElement(directionsForQuotas.MOWait);
        Thread.sleep(1500);
        SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMO);
        SelectClickMethod(directionsForQuotas.DoctorWait, directionsForQuotas.SelectDoctorFirst);
        Thread.sleep(1500);
        if (KingNumber != 4) {
            SelectClickMethod(directionsForQuotas.ProfileWait, directionsForQuotas.SelectProfileFirst);
        } else {
            ClickElement(directionsForQuotas.ProfileWait);
            Thread.sleep(2500);
            ClickElement(directionsForQuotas.SelectProfileFirst);
        }
        inputWord(directionsForQuotas.Diagnosis, "A011");
        Thread.sleep(1500);
        ClickElement(directionsForQuotas.SelectProfileFirst);
        ClickElement(directionsForQuotas.Plan);
        SelectClickMethod(directionsForQuotas.Goal, directionsForQuotas.SelectGoal);
        directionsForQuotas.NextPatient.click();
        /** Добавление Файла и проверка соответствия */
        System.out.println("Добавление Файла и проверка соответствия");
        ClickElement(directionsForQuotas.CreateConsul);
        WaitElement(directionsForQuotas.AddFilesWait);
        directionsForQuotas.AddFiles.getAttribute("class").equals(
                "el-button el-button--primary el-button--medium margin-bottom-25");
        File file = new File("src/test/resources/test.txt");
        directionsForQuotas.AddFilesTwo.sendKeys(file.getAbsolutePath());
        Thread.sleep(1000);
        ClickElement(directionsForQuotas.SendConsul);
        Thread.sleep(1000);
        /** Переходим в Консультации - Исходящие - Незавершённые */
        System.out.println("Переходим в Консультации - Исходящие - Незавершённые");
        ClickElement(consultationOU.Consultation);
        ClickElement(consultationOU.ConsultationFirst);
        WaitElement(consultationOU.AddLinkWait);
        consultationOU.AddLink.getAttribute("class").equals(consultationOU.Class);
        consultationOU.AddFile.getAttribute("class").equals(consultationOU.Class);
        consultationOU.Download.getAttribute("class").equals(consultationOU.Class);
        consultationOU.AddProt.getAttribute("class").equals(consultationOU.Class);
    }
}
