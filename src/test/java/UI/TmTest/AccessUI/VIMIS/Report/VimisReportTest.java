package UI.TmTest.AccessUI.VIMIS.Report;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.Report;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис - Отчёты")
public class VimisReportTest extends BaseTest {
    private static String onko;
    private static String onkoDoctype;
    private static String prev;
    private static String prevDoctype;
    private static String ssz;
    private static String sszDoctype;
    private static String akineo;
    private static String akineoDoctype;
    private static String other;
    private static String otherDoctype;
    private static String SuccessfullyUploaded;
    private static String SuccessfullyUploadedDoctype;
    private static String SuccessfullyUploadedPrev;
    private static String SuccessfullyUploadedPrevDoctype;
    private static String SuccessfullyUploadedAkineo;
    private static String SuccessfullyUploadedAkineoDoctype;
    private static String SuccessfullyUploadedSSZ;
    private static String SuccessfullyUploadedSSZDoctype;
    private static String SuccessfullyUploadedOther;
    private static String SuccessfullyUploadedOtherDoctype;
    AuthorizationObject authorizationObject;
    Report vimisReport;
    SQL sql;
    Date date = new Date();
    SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
    String Date = formatForDateNow.format(date);

    public void VimisReportethod(
            String Name, By directions, String Successfull, String SuccessfullDoctype, String sms
    ) throws InterruptedException, SQLException {
        /** Выбор Направления - Онкология */
        System.out.println("Выбор Направления - " + Name + "");
        ClickElement(vimisReport.Delete);
        WaitElement(vimisReport.IntelligenceWait);
        SelectClickMethod(vimisReport.Direction, directions);
        System.out.println("Выбор Периода");
        WaitElement(vimisReport.IntelligenceWait);
        vimisReport.Period.click();
        ClickElement(vimisReport.DateOneWait);
        Thread.sleep(1500);
        ClickElement(vimisReport.DateTwoWait);
        Thread.sleep(1000);
        /** Поиск */
        System.out.println("Поиск");
        vimisReport.Search.click();
        WaitElement(vimisReport.TableSearch);
        Thread.sleep(1500);
        if (isElementNotVisible(vimisReport.TableSearch)) {
            String is = driver.findElement(vimisReport.IS).getAttribute("innerText");
            String isNew = is.replaceAll("\"", "\\\\\"");
            inputWord(driver.findElement(vimisReport.IsWait), isNew + "1");
            ClickElement(By.xpath("//div[@x-placement]//ul"));
            //  SelectClickMethod(vimisReport.IsWait, By.xpath("//div[@x-placement]//ul/li/span[contains(.,'МИС \"Пациент\"')]"));
            vimisReport.Search.click();
            List<String> get = new ArrayList<String>();
            if (!isElementNotVisible(vimisReport.TableSearch)) {
                Successfull = "0";
                System.out.println("Колличество успешно загруженных в регион для " + Name + ": " + Successfull);
            } else {
                /** Взяли значение "Успешно загруженные в регион" */
                Thread.sleep(1000);
                Successfull = driver.findElement(vimisReport.TableSearch).getText();
                System.out.println("Колличество успешно загруженных в регион для " + Name + ": " + Successfull);
                /** Взять значения стобца "Тип СМС" */
                SuccessfullDoctype = driver.findElement(vimisReport.OneDoctype).getText();
                get.add(SuccessfullDoctype);
                List<WebElement> list = driver.findElements(vimisReport.Doctype);
                for (int i = 0; i < list.size(); i++) {
                    get.add(list.get(i).getText());
                }
                Collections.sort(get);
            }
            System.out.println("Запрос в БД для " + Name + "");
            String Quantity = null;
            sql.StartConnection("SELECT count(a.id) FROM " + sms + " a\n" +
                                        "join telmed.centralized_unloading_systems cus on a.centralized_unloading_system_id = cus.id \n" +
                                        "where create_date BETWEEN '" + Date + " 00:00:00.346 +0500' and '" + Date + " 23:59:59.346 +0500' and cus.name = '" + is + "';");
            while (sql.resultSet.next()) {
                Quantity = sql.resultSet.getString("count");
            }
            sql.StartConnection("SELECT doctype FROM " + sms + " a \n" +
                                        "join telmed.centralized_unloading_systems cus on a.centralized_unloading_system_id = cus.id \n" +
                                        "where create_date BETWEEN '" + Date + " 00:00:00.888 +0500' and '" + Date + " 23:59:59.346 +0500' and cus.name = '" + is + "'\n" +
                                        "group by doctype order by doctype");
            List<String> Doctype = new ArrayList<String>();
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("doctype");
                Doctype.add(sql.value);
            }
            Collections.sort(Doctype);
            Assertions.assertEquals(Successfull, Quantity);
            System.out.println(
                    "Значение на интерфейсе для Онкологии: " + Successfull + " и значение в БД: " + Quantity + " совпадают");
            Assertions.assertEquals(get, Doctype, "Не совпадают названия СМС");
            System.out.println(
                    "Значение \"Тип СМС \" на интерфейсе для Онкологии: " + get + " и значение в БД: " + Doctype + " совпадают");
            System.out.println();
        } else {
            System.out.println("Нет записей для " + Name + "");
        }


    }

    @Description("Авторизация и переход в Вимис отчёт. Проверяем корректное отображение, за выбранный период, проверяем соответствие с БД")
    @DisplayName("Проверка отображения cведений о количестве переданных запросов в ВИМИС")
    @Test
    void VimisReport() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        vimisReport = new Report(driver);
        sql = new SQL();
        /** Авторизация и переход в ВИМИС - Отчёты */
        System.out.println("Авторизация  и переход в ВИМИС - Отчёты");
        AuthorizationMethod(authorizationObject.MIAC);
        WaitElement(vimisReport.ReportWait);
        actionElementAndClick(vimisReport.Report);
        VimisReportethod(
                "Онкология", vimisReport.Oncology, SuccessfullyUploaded, SuccessfullyUploadedPrevDoctype, "vimis.sms");
        VimisReportethod(
                "Профилактика", vimisReport.Prevention, SuccessfullyUploaded, SuccessfullyUploadedPrevDoctype,
                "vimis.preventionsms"
        );
        VimisReportethod(
                "Акушерство и неонатология", vimisReport.Akineo, SuccessfullyUploaded, SuccessfullyUploadedPrevDoctype,
                "vimis.akineosms"
        );
        VimisReportethod(
                "Сердечно-сосудистые заболевания", vimisReport.SSZ, SuccessfullyUploaded,
                SuccessfullyUploadedPrevDoctype, "vimis.cvdsms"
        );
    }
}
