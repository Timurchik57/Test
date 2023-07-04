package UI.TmTest.AccessUI.VIMIS.Report;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.Report;
import api.BaseAPI;
import api.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис - Отчёты")
public class Access_1193Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    SQL sql;
    Report report;

    /**
     * Метод для сверки селектов с бд
     */
    public void WebSqlMethod(
            String Name, By Select, String Sql, String SqlName, Integer vmcl
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        report = new Report(driver);
        authorizationObject = new AuthorizationObject(driver);
        if (Name == "Мед организации") {
            driver.findElement(report.MOWait).sendKeys(Keys.SPACE);
        } else {
            ClickElement(Select);
        }
        Thread.sleep(1500);
        List<String> Web = new ArrayList<>();
        List<WebElement> list = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < list.size(); i++) {
            Web.add(list.get(i).getAttribute("innerText"));
        }
        Collections.sort(Web);
        if (Name == "Мед организации" | Name == "Информационная система") {
            List<String> SQL = new ArrayList<>();
            sql.StartConnection("" + Sql + "");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("" + SqlName + "");
                SQL.add(sql.value);
            }
            Collections.sort(SQL);
            Assertions.assertEquals(Web, SQL, "" + Name + " не совпадает с БД");
        } else {
            if (vmcl == 99) {
                String NameSMS;
                List<String> SQL = new ArrayList<>();
                sql.StartConnection("" + Sql + "");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("" + SqlName + "");
                    NameSMS = sql.resultSet.getString("oid");
                    SQL.add(NameSMS + " - " + sql.value);
                }
                Collections.sort(SQL);
                Assertions.assertEquals(Web, SQL, "" + Name + " не совпадает с БД");
            } else {
                String NameSMS;
                List<String> SQL = new ArrayList<>();
                sql.StartConnection("" + Sql + "");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("" + SqlName + "");
                    NameSMS = sql.resultSet.getString("id");
                    SQL.add(NameSMS + " - " + sql.value);
                }
                Collections.sort(SQL);
                Assertions.assertEquals(Web, SQL, "" + Name + " не совпадает с БД");
            }
        }

    }

    /**
     * Метод для отправки смс с изменением статусов, с последующей проверкой изменений на UI
     */
    public void Access_1193Method(
            Integer vmcl, Integer docTypeVersion, By Directions, String sms, String logs
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        report = new Report(driver);
        authorizationObject = new AuthorizationObject(driver);
        System.out.println("Отправляем  смс c DocType 3 с vmlc=" + vmcl + "");
        xml.ApiSmd("SMS/SMS3.xml", "3", vmcl, 1, true, docTypeVersion, 1, 9, 18, 1, 57, 21);
        Thread.sleep(2000);
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
        System.out.println("Успешно загруженные в регион");
        WaitElement(report.SuccessfullyUploadedWait);
        String SuccessfullyUp = report.SuccessfullyUploaded.getText();
        System.out.println("Отправленные на федеральный уровень");
        String Federal = report.Federal.getText();
        System.out.println("Успешно приняты на федеральном уровне");
        String SuccessfullyFederal = report.SuccessfullyFederal.getText();
        System.out.println("Не прошли ФЛК");
        String FLK = report.FLK.getText();
        System.out.println("В очереди отправки");
        String Queue = report.Queue.getText();
        report.Search.click();
        Thread.sleep(1500);
        String New = report.SuccessfullyUploaded.getText();
        Assertions.assertEquals(
                Integer.valueOf(New), Integer.valueOf(SuccessfullyUp),
                "Значение не добавилось в Успешно загруженные в регион"
        );
        New = report.Queue.getText();
        Assertions.assertEquals(
                Integer.valueOf(New), Integer.valueOf(Queue), "Значение не добавилось в В очереди отправки");
        if (vmcl == 99) {
            System.out.println("Берём отправленную смс и меняем у неё status на null");
            sql.StartConnection(
                    "SELECT id, doctype, status, fremd_status,emd_id FROM " + sms + " WHERE created_datetime > '" + Date + " 00:00:00.888 +0500' AND doctype = '75' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
            }
            sql.UpdateConnection("update " + sms + " set status = NULL where id = '" + sql.value + "';");
            System.out.println(
                    "Обновляем страницу и смотрим, что увеличилось значение в В очереди отправки");
            report.Search.click();
            Thread.sleep(1500);
            New = report.SuccessfullyUploaded.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(SuccessfullyUp),
                    "Значение изменилось в Успешно загруженные в регион"
            );
            New = report.Federal.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Federal),
                    "Значение прибавилось в Отправленные на федеральный уровень"
            );
            New = report.Queue.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Queue) + 1, "Значение не прибавилось в В очереди отправки");

            System.out.println("Берём отправленную смс и меняем у неё status с null на success");
            sql.UpdateConnection("update " + sms + " set status = 'success' where id = '" + sql.value + "';");
            System.out.println(
                    "Обновляем страницу и смотрим, что уменьшилось значение В очереди отправки и увеличилось Отправленные на федеральный уровень");
            report.Search.click();
            Thread.sleep(1500);
            New = report.SuccessfullyUploaded.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(SuccessfullyUp),
                    "Значение изменилось в Успешно загруженные в регион"
            );
            New = report.Federal.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Federal) + 1,
                    "Значение не прибавилось в Отправленные на федеральный уровень"
            );
            New = report.Queue.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Queue), "Значение не убавилось в В очереди отправки");

            System.out.println("Берём эту же смс и меняем у неё fremd_status на 1");
            sql.UpdateConnection("update " + sms + " set fremd_status = '1' where id = '" + sql.value + "';");
            System.out.println(
                    "Обновляем страницу и смотрим, что увеличилось значение Успешно приняты на федеральном уровне");
            report.Search.click();
            Thread.sleep(1500);
            New = report.SuccessfullyUploaded.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(SuccessfullyUp),
                    "Значение изменилось в Успешно загруженные в регион"
            );
            New = report.Federal.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Federal) + 1,
                    "Значение изменилось в Отправленные на федеральный уровень"
            );
            New = report.SuccessfullyFederal.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(SuccessfullyFederal) + 1,
                    "Значение не прибавилось в Успешно приняты на федеральном уровне"
            );
            New = report.Queue.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Queue), "Значение не убавилось в В очереди отправки");

            System.out.println("Берём эту же смс и меняем у неё fremd_status на 0");
            sql.UpdateConnection("update " + sms + " set fremd_status = '0' where id = '" + sql.value + "';");
            System.out.println(
                    "Обновляем страницу и смотрим, что увеличилось значение Успешно приняты на федеральном уровне");
            report.Search.click();
            Thread.sleep(1500);
            New = report.SuccessfullyUploaded.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(SuccessfullyUp),
                    "Значение изменилось в Успешно загруженные в регион"
            );
            New = report.Federal.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Federal) + 1,
                    "Значение изменилось в Отправленные на федеральный уровень"
            );
            New = report.SuccessfullyFederal.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(SuccessfullyFederal),
                    "Значение не убавилось в Успешно приняты на федеральном уровне"
            );
            New = report.FLK.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(FLK) + 1, "Значение не добавилось в Не прошли ФЛК");
            New = report.Queue.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Queue), "Значение не убавилось в В очереди отправки");
        } else {
            System.out.println("Берём отправленную смс и меняем у неё status с false на true");
            sql.StartConnection(
                    "SELECT id FROM " + sms + " WHERE create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
            }
            sql.UpdateConnection("update " + sms + " set is_sent = 'true' where id = '" + sql.value + "';");
            System.out.println(
                    "Обновляем страницу и смотрим, что уменьшилось значение В очереди отправки и увеличилось Отправленные на федеральный уровень");
            report.Search.click();
            Thread.sleep(1500);
            New = report.SuccessfullyUploaded.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(SuccessfullyUp),
                    "Значение изменилось в Успешно загруженные в регион"
            );
            New = report.Federal.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Federal) + 1,
                    "Значение не прибавилось в Отправленные на федеральный уровень"
            );
            New = report.Queue.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Queue) - 1, "Значение не убавилось в В очереди отправки");
            System.out.println("Берём эту же смс и меняем у неё status в " + logs + " на 1");
            sql.UpdateConnection(
                    "insert into " + logs + "(create_time, status, description, sms_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', " + sql.value + ");");
            System.out.println(
                    "Обновляем страницу и смотрим, что увеличилось значение Успешно приняты на федеральном уровне");
            report.Search.click();
            Thread.sleep(1500);
            New = report.SuccessfullyUploaded.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(SuccessfullyUp),
                    "Значение изменилось в Успешно загруженные в регион"
            );
            New = report.Federal.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Federal) + 1,
                    "Значение изменилось в Отправленные на федеральный уровень"
            );
            New = report.SuccessfullyFederal.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(SuccessfullyFederal) + 1,
                    "Значение не прибавилось в Успешно приняты на федеральном уровне"
            );
            New = report.Queue.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Queue) - 1, "Значение не убавилось в В очереди отправки");
            System.out.println("Берём эту же смс и меняем у неё status в " + logs + " на 0");
            sql.UpdateConnection("update " + logs + " set status = '0' where sms_id = '" + sql.value + "';");
            System.out.println(
                    "Обновляем страницу и смотрим, что увеличилось значение Успешно приняты на федеральном уровне");
            report.Search.click();
            Thread.sleep(1500);
            New = report.SuccessfullyUploaded.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(SuccessfullyUp),
                    "Значение изменилось в Успешно загруженные в регион"
            );
            New = report.Federal.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Federal) + 1,
                    "Значение изменилось в Отправленные на федеральный уровень"
            );
            New = report.SuccessfullyFederal.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(SuccessfullyFederal),
                    "Значение не убавилось в Успешно приняты на федеральном уровне"
            );
            New = report.FLK.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(FLK) + 1, "Значение не добавилось в Не прошли ФЛК");
            New = report.Queue.getText();
            Assertions.assertEquals(
                    Integer.valueOf(New), Integer.valueOf(Queue) - 1, "Значение не убавилось в В очереди отправки");
        }
    }

    @Test
    @Issue(value = "TEL-1193")
    @Link(name = "ТМС-1444", url = "https://team-1okm.testit.software/projects/5/tests/1444?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @Description("Переходим в ВИМИС - Отчёты - Заполняем параметры поиска. Отправляем смс и смотрим у неё статусы в колонках Успешно загруженные в регион/Отправленные на федеральный уровень/Успешно приняты на федеральном уровне/Не прошли ФЛК/В очереди отправки. Меняем данные в бд и смотрим изменения в этих столбцах")
    public void Access_1193() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        report = new Report(driver);
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Переход в Отчёты");
            AuthorizationMethod(authorizationObject.MIAC);
            ClickElement(report.ReportWait);
            WaitElement(report.IntelligenceWait);
            System.out.println("Сравниваем Информационная система с БД");
            WebSqlMethod(
                    "Информационная система", report.IsWait,
                    "SELECT name FROM telmed.centralized_unloading_systems WHERE isunloading = true;", "name", 0
            );
            Access_1193Method(99, 1, report.Other, "vimis.remd_sent_result", "");
            Access_1193Method(1, 3, report.Oncology, "vimis.sms", "vimis.documentlogs");
            Access_1193Method(2, 3, report.Prevention, "vimis.preventionsms", "vimis.preventionlogs");
            Access_1193Method(3, 2, report.Akineo, "vimis.akineosms", "vimis.akineologs");
            Access_1193Method(4, 2, report.SSZ, "vimis.cvdsms", "vimis.cvdlogs");

        }
    }
}
