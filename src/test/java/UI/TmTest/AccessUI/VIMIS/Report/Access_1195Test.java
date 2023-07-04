package UI.TmTest.AccessUI.VIMIS.Report;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.Report;
import api.BaseAPI;
import api.TestListenerApi;
import io.qameta.allure.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.UUID;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис - Отчёты")
public class Access_1195Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    SQL sql;
    Report report;
    String MOWeb;
    String DateWeb;
    String LocalWeb;
    String IdentWeb;
    String IdenSMStWeb;
    String DateTwo;

    /**
     * Метод для отправки смс с изменением статусов, с последующим открытием данной смс на ui
     */
    public void Access_1195Method(
            Integer vmcl, Integer docTypeVersion, By Directions, String sms, String logs
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        report = new Report(driver);
        authorizationObject = new AuthorizationObject(driver);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        DateTwo = formatForDateNow.format(date);
        System.out.println("Отправляем  смс c DocType 3 с vmlc=" + vmcl + "");
        xml.ApiSmd("SMS/SMS3.xml", "3", vmcl, 1, true, docTypeVersion, 1, 9, 18, 1, 57, 21);
        if (vmcl == 99) {
            System.out.println("Берём отправленную смс и меняем у неё status с null на success и добавляем emd_id");
            sql.UpdateConnection(
                    "update " + sms + " set status = 'success', emd_id = 'Проверка 1195', fremd_status = '1' where local_uid = '" + xml.uuid + "';");
            sql.StartConnection(
                    "SELECT r.id, r.status, r.fremd_status, r.emd_id ,  r.created_datetime,r.local_uid, msm.namemu  FROM " + sms + " r\n" +
                            "join dpc.mis_sp_mu msm on r.medicalidmu = msm.medicalidmu\n" +
                            "where created_datetime > '" + Date + " 00:00:00.888 +0500' AND doctype = '75' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                MOWeb = sql.resultSet.getString("namemu");
                DateWeb = sql.resultSet.getString("created_datetime");
                LocalWeb = sql.resultSet.getString("local_uid");
                IdentWeb = sql.resultSet.getString("emd_id");
            }

        } else {
            System.out.println(
                    "Берём отправленную смс и меняем у неё в " + sms + " status = true  msg_id какое либо значение, request_id какое либо значение");
            sql.UpdateConnection(
                    "update " + sms + " set is_sent = 'true', msg_id = '" + UUID.randomUUID() + "', request_id = '" + UUID.randomUUID() + "' where local_uid = '" + xml.uuid + "';");
            sql.StartConnection("select * from " + sms + " where local_uid = '" + xml.uuid + "'");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
            }
            System.out.println("Берём отправленную смс и устанавливаем у неё в " + logs + " status = 1");
            sql.UpdateConnection(
                    "insert into " + logs + "(create_time, status, description, sms_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', " + sql.value + ");");
            sql.StartConnection(
                    "select s.id, s.local_uid, s.is_sent, s.create_date, s.msg_id, s.request_id, msm.namemu  from " + sms + " s \n" +
                            "join " + logs + " d on s.id = d.sms_id\n" +
                            "join dpc.mis_sp_mu msm on s.medicalidmu = msm.medicalidmu where s.id = '" + sql.value + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                MOWeb = sql.resultSet.getString("namemu");
                DateWeb = sql.resultSet.getString("create_date");
                LocalWeb = sql.resultSet.getString("local_uid");
                IdentWeb = sql.resultSet.getString("msg_id");
                IdenSMStWeb = sql.resultSet.getString("request_id");
            }
            System.out.println(IdentWeb);
            System.out.println(IdenSMStWeb);

        }
        System.out.println("Выбираем направление");
        SelectClickMethod(report.Direction, Directions);
        if (vmcl == 99) {
            System.out.println("Выбираем Период");
            ClickElement(report.PeriodWait);
            ClickElement(report.DateOneWait);
            Thread.sleep(1500);
            ClickElement(report.DateTwoWait);
        } else {
            System.out.println("Выбираем Период");
            ClickElement(report.PeriodWait);
            if (isElementNotVisible(report.DateOneWait)) {
                ClickElement(report.DateOneWait);
                Thread.sleep(1500);
                ClickElement(report.DateTwoWait);
            } else {
                ClickElement(report.DateTwoWait);
                Thread.sleep(1500);
                ClickElement(report.DateTwoWait);
            }
        }
        System.out.println("Выбираем Тип документа");
        if (vmcl == 99) {
            inputWord(driver.findElement(report.SMS), "755");
            ClickElement(report.SelectSms75);
        } else {
            ClickElement(report.SMS);
            ClickElement(report.SelectSms);
        }
        report.Search.click();
        Thread.sleep(1500);
        System.out.println("Нажимаем на количество Отправленные на федеральный уровень");
        WaitElement(report.SuccessfullyFederalMO);
        ClickElement(report.SuccessfullyFederalMO);
        WaitElement(report.DetalMOWait);
        Thread.sleep(1500);
        System.out.println("Берём название МО");
        String DetalMO = report.DetalMO.getText();
        Thread.sleep(2500);
        System.out.println("Берём Дату");
        String DetalNewDate = driver.findElement(
                By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/preceding-sibling::td//span")).getText();
        String DetalDate = StringUtils.substring(DetalNewDate, 0, 10);
        System.out.println("Берём Идентификатор");
        String DetalIdent;
        if (vmcl == 99) {
            DetalIdent = driver.findElement(
                    By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/following-sibling::td[1]//span")).getText();
        } else {
            DetalIdent = driver.findElement(
                    By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/following-sibling::td[2]//span")).getText();
        }
        String DetalSMSIdent = null;
        if (vmcl != 99) {
            System.out.println("Берём Идентификатор СМС");
            DetalSMSIdent = driver.findElement(
                    By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/following-sibling::td[1]//span")).getText();
        }
        Assertions.assertEquals(DetalMO, MOWeb + " (МИС \"Пациент\")", "Название МО не совпадает");
        Assertions.assertEquals(DetalDate, "" + DateTwo + "", "Дата не совпадает");
        Assertions.assertEquals(DetalIdent, IdentWeb, "Идентификатор не совпадает");
        if (vmcl != 99) {
            Assertions.assertEquals(DetalSMSIdent, IdenSMStWeb, "Идентификатор  СМС не совпадает");
        }
        System.out.println("Открываем документ");
        if (vmcl == 99) {
            driver.findElement(
                    By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/following-sibling::td[2]/div")).click();
        } else {
            driver.findElement(
                    By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/following-sibling::td[3]/div")).click();
        }
        Thread.sleep(1500);
        WaitElement(report.DocumentWait);
        Thread.sleep(1000);
        if (KingNumber == 1 | KingNumber == 4) {
            ClickElement(report.CloseTm);
        }
        if (KingNumber == 2) {
            ClickElement(report.Close);
        }
        ClickElement(report.DetalMOWait);

    }

    @Test
    @Issue(value = "TEL-1195")
    @Link(name = "ТМС-1447", url = "https://team-1okm.testit.software/projects/5/tests/1447?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Детализация отчета по РЭМД")
    @Description("Переходим в ВИМИС - Отчёты - Заполняем параметры поиска. Отправляем смс и смотрим у неё статусы в колонках Успешно загруженные в регион/Отправленные на федеральный уровень/Успешно приняты на федеральном уровне/Не прошли ФЛК/В очереди отправки. Открываем Отправленные на федеральный уровень сравниваем значения с БД, после открываем документ")
    public void Access_1195() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        report = new Report(driver);
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Переход в Отчёты");
            AuthorizationMethod(authorizationObject.MIAC);
            ClickElement(report.ReportWait);
            WaitElement(report.IntelligenceWait);
            Access_1195Method(99, 1, report.Other, "vimis.remd_sent_result", "");
            Access_1195Method(1, 3, report.Oncology, "vimis.sms", "vimis.documentlogs");
            Access_1195Method(2, 3, report.Prevention, "vimis.preventionsms", "vimis.preventionlogs");
            Access_1195Method(3, 2, report.Akineo, "vimis.akineosms", "vimis.akineologs");
            Access_1195Method(4, 2, report.SSZ, "vimis.cvdsms", "vimis.cvdlogs");

        }
    }
}
