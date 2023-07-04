package UI.TmTest.AccessUI.VIMIS.Report;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.Report;
import UI.TmTest.PageObject.VIMIS.SMS;
import api.BaseAPI;
import api.TestListenerApi;
import api.XML;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис - Отчёты")
public class VimisReportFilterTest extends BaseAPI {
    private static String onko;
    private static String onkoDoctype;
    private static String SuccessfullyUploadedOnko;
    private static String SuccessfullyUploadedOnkoDoctype;
    private String Value;
    AuthorizationObject authorizationObject;
    Report vimisReport;
    SQL sql;
    Date date = new Date();
    Date date1 = new Date();
    SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatForDateNow1 = new SimpleDateFormat("dd-MM-yyyy");
    String Date = formatForDateNow.format(date);
    String Date1 = formatForDateNow1.format(date1);
    SMS sms;
    XML xml;

    private void VimisReportAccepteMethod(
            Integer vmcl, String sms, String logs, Integer doctype
    ) throws IOException, InterruptedException, SQLException {
        xml = new XML();
        System.out.println("Отправляем смс3 для vmcl = " + vmcl + "");
        xml.ApiSmd("SMS/SMS3.xml", "3", vmcl, 1, true, doctype, 1, 9, 18, 1, 57, 21);
        sql.StartConnection(
                "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            Value = sql.resultSet.getString("id");
            System.out.println(Value);
        }
        sql.UpdateConnection(
                "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + Value + "', '" + UUID.randomUUID() + "')");

    }

    private void VimisReportAccepteAddMethod(
            Integer vmcl, String Sms, String logs, String Name, Integer doctype
    ) throws SQLException, IOException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        vimisReport = new Report(driver);
        sql = new SQL();
        sms = new SMS(driver);
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            VimisReportAccepteMethod(vmcl, Sms, logs, doctype);
            AuthorizationMethod(authorizationObject.MIAC);
            WaitElement(sms.SMSWait);
            actionElementAndClick(sms.SMS);
            /** Выбор Направления  */
            System.out.println("Выбор Направления - " + Name + "");
            WaitElement(sms.DistrictWait);
            if (vmcl == 1) {
                ClickElement(sms.OncologyWait);
                Thread.sleep(1000);
            }
            if (vmcl == 2) {
                ClickElement(sms.PreventionWait);
                Thread.sleep(1000);
            }
            if (vmcl == 3) {
                ClickElement(sms.AkineoWait);
                Thread.sleep(1000);
            }
            if (vmcl == 4) {
                ClickElement(sms.SSZWait);
                Thread.sleep(1000);
            }
            /** Выбор Фильтров */
            System.out.println("Выбор Фильтров");
            sms.Filter.click();
            WaitElement(sms.FilterWait);
            /** Ввод периода */
            System.out.println("Ввод периода");
            sms.BeforeTime.sendKeys(Date1 + "0000");
            sms.AfterTime.sendKeys(Date1 + "2359");
            System.out.println("Принятые");
            ClickElement(sms.Accepted);
            /** Поиск */
            System.out.println("Поиск");
            sms.Search.click();
            Thread.sleep(1000);
            if (KingNumber == 4) {
                Thread.sleep(12000);
            }
            /** Результаты поиска */
            SelectClickMethod(sms.Page, sms.SelectPage);
            if (KingNumber == 4) {
                WaitNotElement3(sms.Loading, 20);
            }
            Thread.sleep(1000);
            List<String> getResultSearch = new ArrayList<String>();
            System.out.println("Берём все смс на 1 странице");
            if (!isElementNotVisible(sms.ResultSearch)) {
                System.out.println("Количество успешно загруженных в регион для " + Name + ": " + getResultSearch);
            } else {
                WaitElement(sms.ResultSearch);
                List<WebElement> list = driver.findElements(sms.ResultSearch);
                for (int i = 0; i < list.size(); i++) {
                    getResultSearch.add(list.get(i).getText());
                }
                Collections.sort(getResultSearch);
            }
            System.out.println("Считаем сколько страниц");
            List<WebElement> Quantity = driver.findElements(sms.QuantityPage);
            Integer Count = 0;
            for (int i = 0; i < Quantity.size(); i++) {
                Count += 1;
            }
            System.out.println(Count);
            if (Count > 1) {
                while ((isElementNotVisible(sms.NextPage))) {
                    ClickElement(sms.NextPage);
                    if (KingNumber == 4) {
                        WaitNotElement3(sms.Loading, 20);
                    }
                    if (!isElementNotVisible(sms.ResultSearch)) {
                        Thread.sleep(1000);
                        System.out.println(
                                "Количество успешно загруженных в регион для " + Name + ": " + getResultSearch);
                    } else {
                        Thread.sleep(1000);
                        WaitElement(sms.ResultSearch);
                        List<WebElement> list = driver.findElements(sms.ResultSearch);
                        for (int i = 0; i < list.size(); i++) {
                            getResultSearch.add(list.get(i).getText());
                        }
                        Collections.sort(getResultSearch);
                    }
                }
            }
            /** Запрос в БД */
            System.out.println("Запрос в БД для " + Name + "");
            sql.StartConnection("select p.id, p.local_uid  from " + Sms + " p \n" +
                                        "join " + logs + " p2 on p.id = p2.sms_id \n" +
                                        "where p2.status = '1' and p.create_date  > '" + Date + " 00:00:00.888 +0500' group by p.id;");
            List<String> getResultBd = new ArrayList<String>();
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("local_uid");
                getResultBd.add(sql.value);
            }
            Collections.sort(getResultBd);
            /** Проверка на соответствие*/
            System.out.println("Проверка на соответствие для " + Name + "");
            Assertions.assertEquals(getResultSearch, getResultBd);
            System.out.println(
                    "Значение на интерфейсе для " + Name + ": " + getResultSearch + " и значение в БД: " + getResultBd + " совпадают");
        }
    }

    @Description("Авторизация и переход в Вимис отчёт. Проверяем корректное отображение, за выбранный период, применяем фильтр, проверяем соответствие с БД")
    @DisplayName("Проверка отображения cведений о количестве переданных запросов в ВИМИС c использованием фильтра")
    @Test
    @Story("Вимис - Отчёты")
    public void VimisReport() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        vimisReport = new Report(driver);
        sql = new SQL();
        /** Авторизация  и переход в ВИМИС - Отчёты */
        System.out.println("Авторизация  и переход в ВИМИС - Отчёты");
        AuthorizationMethod(authorizationObject.MIAC);
        WaitElement(vimisReport.ReportWait);
        actionElementAndClick(vimisReport.Report);
        /** Выбор Направления - Онкология */
        System.out.println("Выбор Направления - Онкология");
        WaitElement(vimisReport.IntelligenceWait);
        SelectClickMethod(vimisReport.Direction, vimisReport.Oncology);
        /** Выбор Периода */
        System.out.println("Выбор Периода");
        vimisReport.Period.click();
        WaitElement(vimisReport.DateWait);
        WaitElement(vimisReport.DateOneWait);
        vimisReport.DateOne.click();
        Thread.sleep(2000);
        vimisReport.DateTwo.click();
        Thread.sleep(1000);
        /** Выбор Мед. организации */
        System.out.println("Выбор Мед. организации");
        vimisReport.MO.sendKeys("БУ ХМАО-Югры \"Белоярская районная больница\"");
        WaitElement(By.xpath("//div[@x-placement='bottom-start']"));
        WaitElement(vimisReport.OrganizationWait);
        actionElementAndClick(vimisReport.Organization);
        /** Выбор Типа СМС */
        System.out.println("Выбор Типа СМС");
        SelectClickMethod(vimisReport.SMS, vimisReport.SelectSms);
        /** Поиск */
        System.out.println("Поиск");
        vimisReport.Search.click();
        Thread.sleep(1000);
        if (!isElementNotVisible(vimisReport.TableSearch)) {
            SuccessfullyUploadedOnko = "0";
            System.out.println("Колличество успешно загруженных в регион для Онкологии: " + SuccessfullyUploadedOnko);
        } else {
            /** Взяли значение "Успешно загруженные в регион" */
            Thread.sleep(1000);
            SuccessfullyUploadedOnko = driver.findElement(vimisReport.TableSearch).getText();
            System.out.println("Колличество успешно загруженных в регион для Онкологии: " + SuccessfullyUploadedOnko);
            /** Взять значения стобца "Тип СМС" */
            SuccessfullyUploadedOnkoDoctype = driver.findElement(vimisReport.OneDoctype).getText();
        }
        /** Запрос в БД для Онкологии*/
        sql.StartConnection(
                "SELECT count(id) FROM vimis.sms where create_date BETWEEN '" + Date + " 00:00:00.346 +0500' and '" + Date + " 23:59:59.346 +0500' and doctype = 'SMSV3';");
        while (sql.resultSet.next()) {
            onko = sql.resultSet.getString("count");
        }
        sql.StartConnection(
                "SELECT doctype FROM vimis.sms where create_date BETWEEN '" + Date + " 00:00:00.346 +0500' and '" + Date + " 23:59:59.346 +0500' and doctype = 'SMSV3' group by doctype order by doctype;");
        while (sql.resultSet.next()) {
            onkoDoctype = sql.resultSet.getString("doctype");
        }
        Assertions.assertEquals(SuccessfullyUploadedOnko, onko);
        System.out.println(
                "Значение на интерфейсе для Онкологии: " + SuccessfullyUploadedOnko + " и значение в БД: " + onko + " совпадают");
        Assertions.assertEquals(SuccessfullyUploadedOnkoDoctype, onkoDoctype);
        System.out.println(
                "Значение \"Тип СМС \" на интерфейсе для Онкологии: " + SuccessfullyUploadedOnkoDoctype + " и значение в БД: " + onkoDoctype + " совпадают");
        System.out.println();

    }

    @Issue(value = "TEL-833")
    @Link(name = "ТМС-1265", url = "https://team-1okm.testit.software/projects/5/tests/1265?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Description("Авторизация и переход в Вимис отчёт. Проверяем корректное отображение, за выбранный период, применяем фильтр по принятым, проверяем соответствие с БД")
    @DisplayName("Проверка отображения принятых документов в соответствии с бд для vmcl =1")
    @Test
    @Story("Проверка отображения принятых документов в соответствии с бд")
    public void VimisReportAcceptedVmcl_1() throws InterruptedException, SQLException, IOException {
        VimisReportAccepteAddMethod(1, "vimis.sms", "vimis.documentlogs", "Онкология", 3);

    }

    @DisplayName("Проверка отображения принятых документов в соответствии с бд для vmcl =2")
    @Test
    @Story("Проверка отображения принятых документов в соответствии с бд")
    public void VimisReportAcceptedVmcl_2() throws InterruptedException, SQLException, IOException {
        VimisReportAccepteAddMethod(2, "vimis.preventionsms", "vimis.preventionlogs", "Профилактика", 3);
    }

    @DisplayName("Проверка отображения принятых документов в соответствии с бд для vmcl =3")
    @Test
    @Story("Проверка отображения принятых документов в соответствии с бд")
    public void VimisReportAcceptedVmcl_3() throws InterruptedException, SQLException, IOException {
        VimisReportAccepteAddMethod(3, "vimis.akineosms", "vimis.akineologs", "Акинео", 2);
    }

    @DisplayName("Проверка отображения принятых документов в соответствии с бд для vmcl =4")
    @Test
    @Story("Проверка отображения принятых документов в соответствии с бд")
    public void VimisReportAcceptedVmcl_4() throws InterruptedException, SQLException, IOException {
        VimisReportAccepteAddMethod(4, "vimis.cvdsms", "vimis.cvdlogs", "ССЗ", 2);
    }
}
