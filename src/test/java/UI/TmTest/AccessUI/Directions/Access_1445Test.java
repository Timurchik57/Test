package UI.TmTest.AccessUI.Directions;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Onkology.Analytics;
import UI.TmTest.PageObject.Onkology.Screenings;
import UI.TmTest.PageObject.Statistics.StatisticsConsultation;
import UI.TmTest.PageObject.VIMIS.Report;
import UI.TmTest.PageObject.VIMIS.SMS;
import api.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class Access_1445Test extends BaseTest {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    ConsultationOutgoingUnfinished consultationOutgoingUn;
    Analytics analytics;
    Screenings screenings;
    SMS sms;
    Report report;
    StatisticsConsultation statisticsConsultation;

    /**
     * Метод для создания направления на диагностику с записью на приём и прикреплением файлов
     */
    public void AddConsulMethod(String Mass, Boolean MyMO, By Research) throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        /** Переход в создание консультации на оборудование */
        ClickElement(directionsForQuotas.ConsultationWait);
        /** Создание консультации */
        System.out.println("Создание консультации");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.CreateWait);
        directionsForQuotas.Create.click();
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.DistrictDiagnostic.click();
        WaitElement(directionsForQuotas.DistrictDiagnosticWait);
        directionsForQuotas.Next.click();
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);
        /** Заполнение информации о направившем враче */
        System.out.println("Заполнение информации о направившем враче");
        WaitElement(directionsForQuotas.InfoDoctorWait);
        WaitElement(directionsForQuotas.SubmittingDoctorWait);
        Thread.sleep(2000);
        SelectClickMethod(directionsForQuotas.SubmittingDoctorWait, directionsForQuotas.FIO);
        if (KingNumber == 4) {
            Thread.sleep(1500);
        }
        SelectClickMethod(directionsForQuotas.Division, directionsForQuotas.SelectDivision);
        if (KingNumber == 4 | KingNumber == 2) {
            Thread.sleep(5000);
        }
        SelectClickMethod(directionsForQuotas.DepartmentWait, directionsForQuotas.SelectDepartment);
        if (KingNumber == 4 | KingNumber == 2) {
            Thread.sleep(2000);
        }
        SelectClickMethod(directionsForQuotas.Post, directionsForQuotas.SelectPost);
        if (KingNumber == 4) {
            Thread.sleep(1500);
        }
        SelectClickMethod(directionsForQuotas.Specialization, directionsForQuotas.SelectSpecializationFirst);
        if (KingNumber == 4) {
            Thread.sleep(1500);
        }
        SelectClickMethod(directionsForQuotas.AnatomicalAreas, directionsForQuotas.SelectAnatomicalAreas);
        if (MyMO) {
            ClickElement(directionsForQuotas.MyMODirection);
            Thread.sleep(2000);
            SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);
        }
        if (!MyMO) {
            if (KingNumber == 1 | KingNumber == 2) {
                SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirection);
            }
            if (KingNumber == 4) {
                SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirectionKon);
            }
        }
        directionsForQuotas.Diagnosis.sendKeys(Keys.SPACE);
        WaitElement(authorizationObject.BottomStart);
        Thread.sleep(1000);
        ClickElement(directionsForQuotas.SelectDiagnosisWait);
        SelectClickMethod(directionsForQuotas.Research, Research);
        SelectClickMethod(directionsForQuotas.BenefitCode, directionsForQuotas.SelectBenefitCode);
        WaitElement(directionsForQuotas.MassWait);
        inputWord(directionsForQuotas.Mass, Mass);
        System.out.println("Вес пациента " + Mass);
        inputWord(directionsForQuotas.NamePatient, "Тестовыйй");
        inputWord(directionsForQuotas.LastNamePatient, "Тестт");
        inputWord(directionsForQuotas.MiddleNamePatient, "Тестовичч");
        actionElementAndClick(directionsForQuotas.NextPatient);
        /** Окно направления на диагностику*/
        WaitElement(directionsForQuotas.Header);
        WaitElement(directionsForQuotas.CreateDirectionWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.CreateDirection);
        /** Прикрепление файла */
        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.FileWait);
        WaitElement(directionsForQuotas.AddFileWait);
        Thread.sleep(1000);
        java.io.File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(500);
        WaitElement(directionsForQuotas.Download);
        WaitElement(directionsForQuotas.ReceptionWait);
        directionsForQuotas.Reception.click();
        Thread.sleep(1500);
        System.out.println("Запись на приём");
        WaitElement(equipmentSchedule.HeaderEquipmentSchedulesWait);
        WaitElement(equipmentSchedule.NextPageWait);
        Thread.sleep(3000);
        if (KingNumber == 4) {
            Thread.sleep(4000);
        }
        if (!isElementNotVisible(equipmentSchedule.SlotsTrue)) {
            ClickElement(equipmentSchedule.NextPageWait);
            Thread.sleep(500);
        }
        while (!isElementNotVisible(equipmentSchedule.SlotsFreeWait)) {
            ClickElement(equipmentSchedule.NextPageWait);
            Thread.sleep(500);
            if (!isElementNotVisible(equipmentSchedule.SlotsTrue)) {
                System.out.println("Добавляем расписание с завтрашнего дня");
                if (KingNumber == 4) {
                    AuthorizationMethod(authorizationObject.Kondinsk);
                } else {
                    AuthorizationMethod(authorizationObject.YATCKIV);
                }
                equipmentSchedule.AddEquipmentSchedule(
                        equipmentSchedule.SelectEquipmentChoice, equipmentSchedule.Tomorrow,
                        equipmentSchedule.NextMouth
                );
                if (isElementNotVisible(By.xpath("//div[@role='alert']//h2[contains(.,'Успешно')]"))) {
                    System.out.println("Новое расписание добавлено");
                } else {
                    System.out.println("Проверка уведомления о пересечении расписания");
                    WaitElement(equipmentSchedule.Alert);
                }
                AuthorizationMethod(authorizationObject.OKB);
                ClickElement(directionsForQuotas.ConsultationWait);
                ClickElement(directionsForQuotas.FirstLine);
                ClickElement(directionsForQuotas.AddReception);
                Thread.sleep(1000);
            }
        }
        ClickElement(equipmentSchedule.SlotsFreeWait);
        System.out.println("Выбрали свободный слот");
        WaitElement(equipmentSchedule.WriteWait);
        if (KingNumber == 1 | KingNumber == 2) {
            WaitElement(equipmentSchedule.AddFileWait);
            file = new File("src/test/resources/test.txt");
            equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            equipmentSchedule.AddFile2.sendKeys(file.getAbsolutePath());
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            equipmentSchedule.AddFile3.sendKeys(file.getAbsolutePath());
            WaitNotElement3(directionsForQuotas.AlertError, 1);
            WaitElement(equipmentSchedule.FileWait1);
            WaitElement(equipmentSchedule.FileWait2);
            WaitElement(equipmentSchedule.FileWait3);
            ClickElement(equipmentSchedule.WriteTwo);
        }
        if (KingNumber == 4) {
            Thread.sleep(4000);
            ClickElement(equipmentSchedule.WriteTwo);
        }
        Thread.sleep(2000);
        WaitElement(equipmentSchedule.AlertWait);
        equipmentSchedule.AlertClose.click();
    }

    /**
     * Метод для создания удалённой консультации
     */
    public void AddConsultationMethod(Boolean MyMO) throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        /** Переход в создание консультации на оборудование */
        ClickElement(directionsForQuotas.ConsultationWait);
        /** Создание консультации */
        System.out.println("Создание консультации");
        ClickElement(directionsForQuotas.CreateWait);
        System.out.println("Выбираем консультацию");
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        Thread.sleep(1500);
        ClickElement(directionsForQuotas.NextWait);
        System.out.println("Заполняем данные");
        SelectClickMethod(directionsForQuotas.DoctorWait, directionsForQuotas.SelectDoctorSecond);
        if (!MyMO) {
            if (KingNumber == 1 | KingNumber == 2) {
                SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMO);
            }
            if (KingNumber == 4) {
                SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMOKon);
            }
        } else {
            ClickElement(directionsForQuotas.MyMO);
            SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);

        }
        SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.SelectFirst);
        inputWord(directionsForQuotas.Diagnos, "AA");
        Thread.sleep(1000);
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.Plan);
        SelectClickMethod(directionsForQuotas.Goal, directionsForQuotas.SelectCovid);
        ClickElement(directionsForQuotas.NextConsul);
        ClickElement(directionsForQuotas.CreateConsul);
        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.AddFilesWait);
        Thread.sleep(1000);
        File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());
        WaitElement(directionsForQuotas.Download);
        Thread.sleep(500);
        ClickElement(directionsForQuotas.SendConsul);
        Thread.sleep(2000);
    }

    @Issue(value = "TEL-1445")
    @Issue(value = "TEL-1425")
    @Link(name = "ТМС-1536", url = "https://team-1okm.testit.software/projects/5/tests/1536?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Переходим в различные части Удкон, проверяем загрузку и скачивание файлов")
    @Description("Добавляем файл при создании консультации на диагностику, при записи на приём, после записи на приём в направлении формируем направление и протокол. ")
    public void Access_1445() throws IOException, SQLException, InterruptedException, AWTException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);
        analytics = new Analytics(driver);
        screenings = new Screenings(driver);
        sms = new SMS(driver);
        report = new Report(driver);
        statisticsConsultation = new StatisticsConsultation(driver);
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("1 Проверка - Проверяем добавление файла при создании консультации на диагностику");
        AddConsulMethod("500", false, directionsForQuotas.SelectResearch);
        System.out.println(
                "Переходим в Исходящие - Незавершённые - Созданное направление и проверяем скачивание/загрузку файлов");
        ClickElement(directionsForQuotas.ConsultationWait);
        if (KingNumber == 4) {
            Thread.sleep(4000);
        }
        ClickElement(directionsForQuotas.FirstLine);
        System.out.println("Формируем направление и скачиваем его");
        ClickElement(directionsForQuotas.CreateDirectionLow);
        WaitElement(directionsForQuotas.CreateDirectionAfter);
        ClickElement(directionsForQuotas.DownloadCreateDirection);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        System.out.println("Скачиваем архивом");
        ClickElement(directionsForQuotas.DownloadArhiv);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        System.out.println("Сформировать протокол \n");
        ClickElement(directionsForQuotas.CreateProtocol);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        Thread.sleep(1500);

        System.out.println("2 Проверка - Проверяем добавление файла при создании удалённой консультации");
        AddConsultationMethod(false);
        System.out.println(
                "Переходим в Исходящие - Незавершённые - Созданное направление и проверяем скачивание/загрузку файлов");
        ClickElement(consultationOutgoingUn.Consultation);
        ClickElement(consultationOutgoingUn.ConsultationFirst);
        System.out.println("Добавляем файлы");
        ClickElement(consultationOutgoingUn.AddFileWait);
        Thread.sleep(1000);
        File file = new File("src/test/resources/test.txt");
        consultationOutgoingUn.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(1500);
        WaitElement(consultationOutgoingUn.AddFileDownload2);
        ClickElement(consultationOutgoingUn.AddFileClose);
        System.out.println("Скачиваем архив");
        ClickElement(consultationOutgoingUn.DownloadWait);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        System.out.println("Сформировать протокол \n");
        ClickElement(consultationOutgoingUn.AddProtWait);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        WaitElement(consultationOutgoingUn.CreateDirectionAfter);

        System.out.println("3 Проверка - Проверяем скачивание Excel в Онкология - Аналитика\n");
        ClickElement(analytics.AnalyticsWait);
        ClickElement(analytics.AddExcel);
        WaitNotElement3(directionsForQuotas.AlertError, 1);

        System.out.println("4 Проверка - Проверяем скачивание Excel в Онкология - Скрининнги\n");
        ClickElement(screenings.ScreeningWait);
        ClickElement(screenings.AddExcel);
        WaitNotElement3(directionsForQuotas.AlertError, 1);

        System.out.println("5 Проверка - Проверяем скачивание Excel в Вимис - СМС\n");
        ClickElement(sms.SMSWait);

        ClickElement(sms.OncologyWait);
        Thread.sleep(2000);
        WaitNotElement3(sms.Loading, 20);
        ClickElement(sms.AddExcel);
        WaitNotElement3(sms.ExcelLoading, 20);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        Thread.sleep(2000);
        WaitNotElement3(sms.Loading, 20);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        Thread.sleep(1500);
        ClickElement(sms.Back);

        ClickElement(sms.PreventionWait);
        Thread.sleep(2000);
        WaitNotElement3(sms.Loading, 20);
        ClickElement(sms.AddExcel);
        WaitNotElement3(sms.ExcelLoading, 20);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        WaitNotElement3(sms.Loading, 20);
        Thread.sleep(2000);
        ClickElement(sms.Back);

        ClickElement(sms.AkineoWait);
        Thread.sleep(2000);
        WaitNotElement3(sms.Loading, 20);
        ClickElement(sms.AddExcel);
        WaitNotElement3(sms.ExcelLoading, 20);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        WaitNotElement3(sms.Loading, 20);
        Thread.sleep(2000);
        ClickElement(sms.Back);

        ClickElement(sms.SSZWait);
        Thread.sleep(2000);
        WaitNotElement3(sms.Loading, 20);
        ClickElement(sms.AddExcel);
        WaitNotElement3(sms.ExcelLoading, 20);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        Thread.sleep(2000);
        WaitNotElement3(sms.Loading, 20);

        System.out.println("6 Проверка - Проверяем скачивание Excel в Вимис - Отчёты - СМС\n");
        ClickElement(report.ReportWait);
        SelectClickMethod(report.Direction, report.Other);
        ClickElement(report.SearchWait);
        Thread.sleep(2000);
        ClickElement(report.AddExcel);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        SelectClickMethod(report.Direction, report.Oncology);
        ClickElement(report.SearchWait);
        Thread.sleep(2000);
        ClickElement(report.AddExcel);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        SelectClickMethod(report.Direction, report.Prevention);
        ClickElement(report.SearchWait);
        Thread.sleep(2000);
        ClickElement(report.AddExcel);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        SelectClickMethod(report.Direction, report.Akineo);
        ClickElement(report.SearchWait);
        Thread.sleep(2000);
        ClickElement(report.AddExcel);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        SelectClickMethod(report.Direction, report.SSZ);
        ClickElement(report.SearchWait);
        Thread.sleep(2000);
        ClickElement(report.AddExcel);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        System.out.println("7 Проверка - Проверяем скачивание Excel в Вимис - Отчёты - Отчёт по статусам\n");
        ClickElement(report.ReportWait);
        ClickElement(report.ReportInStatusWait);
        SelectClickMethod(report.StatusDirection, report.Onko);
        ClickElement(report.StatusSearchWait);
        Thread.sleep(2000);
        ClickElement(report.AddExcel2);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        SelectClickMethod(report.StatusDirection, report.Prev);
        ClickElement(report.StatusSearchWait);
        Thread.sleep(2000);
        ClickElement(report.AddExcel2);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        SelectClickMethod(report.StatusDirection, report.akineo);
        ClickElement(report.StatusSearchWait);
        Thread.sleep(2000);
        ClickElement(report.AddExcel2);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        SelectClickMethod(report.StatusDirection, report.ssz);
        ClickElement(report.StatusSearchWait);
        Thread.sleep(2000);
        ClickElement(report.AddExcel2);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        System.out.println("8 Проверка - Проверяем скачивание Excel в Статистика - Статистика по консультациям \n");
        ClickElement(statisticsConsultation.ConsultationWait);
        ClickElement(statisticsConsultation.SearchWait1);
        ClickElement(statisticsConsultation.AddExcel);
        ClickElement(statisticsConsultation.AddExcelYes);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
        ClickElement(statisticsConsultation.HeaderReportWait);
        ClickElement(statisticsConsultation.DateStartWait);
        ClickElement(statisticsConsultation.FirstMonth);
        ClickElement(statisticsConsultation.ToDay);
        ClickElement(statisticsConsultation.SearchWait);
        ClickElement(statisticsConsultation.AddExcel2);
        WaitNotElement3(directionsForQuotas.AlertError, 1);
    }
}
