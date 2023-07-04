package UI.TmTest.AccessUI.VIMIS.SMS;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.SMS;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис - СМС")
public class SMSTest extends BaseTest {
    private List<String> getResultSearchOnko;
    private List<String> getResultSearchPrev;
    private List<String> getResultSearchAkineo;
    private List<String> getResultSearchSSZ;
    private List<String> getResultSearchOther;
    AuthorizationObject authorizationObject;
    SMS sms;
    SQL sql;
    AcceessRoles acceessRoles;
    Date date = new Date();
    Date date1 = new Date();
    SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatForDateNow1 = new SimpleDateFormat("dd-MM-yyyy");
    String Date = formatForDateNow.format(date);
    String Date1 = formatForDateNow1.format(date1);

    public void SMSMethod(
            List ResultSearch, String SMS, String Name, Boolean EditMO
    ) throws InterruptedException, SQLException, NullPointerException {
        /** Результаты поиска */
        SelectClickMethod(sms.Page, sms.SelectPage);
        Thread.sleep(1000);
        ResultSearch = new ArrayList<String>();
        System.out.println("Берём все смс на 1 странице");
        if (KingNumber == 4) {
            Thread.sleep(10000);
        }
        if (!isElementNotVisible(sms.ResultSearch)) {
            System.out.println("Количество успешно загруженных в регион для " + Name + ": " + ResultSearch);
        } else {
            WaitElement(sms.ResultSearch);
            List<WebElement> list = driver.findElements(sms.ResultSearch);
            for (int i = 0; i < list.size(); i++) {
                ResultSearch.add(list.get(i).getText());
            }
            Collections.sort(ResultSearch);
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
                    Thread.sleep(8000);
                }
                if (!isElementNotVisible(sms.ResultSearch)) {
                    Thread.sleep(1000);
                    System.out.println("Количество успешно загруженных в регион для " + Name + ": " + ResultSearch);
                } else {
                    Thread.sleep(1000);
                    WaitElement(sms.ResultSearch);
                    List<WebElement> list = driver.findElements(sms.ResultSearch);
                    for (int i = 0; i < list.size(); i++) {
                        ResultSearch.add(list.get(i).getText());
                    }
                    Collections.sort(ResultSearch);
                }
            }
        }
        if (EditMO) {
            System.out.println("Запрос в БД для " + Name + "");
            List<String> getResultBd = new ArrayList<String>();
            if (Name != "Иных профилей") {
                sql.StartConnection(
                        "SELECT local_uid FROM " + SMS + " where doctype='SMSV5' and create_date > '" + Date + " 00:00:00.346 +0500' and medicalidmu = '1001';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("local_uid");
                    getResultBd.add(sql.value);
                }
                Collections.sort(getResultBd);
            } else {
                sql.StartConnection(
                        "SELECT local_uid FROM " + SMS + " where doctype='110' and created_datetime > '" + Date + " 00:00:00.346 +0500' and medicalidmu = '1001';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("local_uid");
                    getResultBd.add(sql.value);
                }
                Collections.sort(getResultBd);
            }
            System.out.println("Проверка на соответствие для " + Name + "");
            Assertions.assertEquals(ResultSearch, getResultBd);
            System.out.println(
                    "Значение на интерфейсе для " + Name + ": " + ResultSearch + " и значение в БД: " + getResultBd + " совпадают");
        } else {
            System.out.println("Проверка на отсутствие данных для " + Name + "");
            List<String> getResultBd = new ArrayList<String>();
            Assertions.assertEquals(ResultSearch, getResultBd, "Данные отображаются при отключенном доступе 19.2");
        }
    }

    @Issue(value = "TEL-1507")
    @Link(name = "ТМС-1660", url = "https://team-1okm.testit.software/projects/5/tests/1660?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отображения cведений в Структурированные медицинские сведения")
    @Description("Авторизация - переход в роли доступа выставление досткпа 19/2 и переход в Структурированные медицинские сведения. Проверяем корректное отображение, за выбранный период, проверяем соответствие с БД")
    void SMS() throws InterruptedException, SQLException, NullPointerException {
        authorizationObject = new AuthorizationObject(driver);
        sms = new SMS(driver);
        sql = new SQL();
        acceessRoles = new AcceessRoles(driver);
        AuthorizationMethod(authorizationObject.MIAC);
        ClickElement(acceessRoles.RolesWait1);
        System.out.println("Редактирование роли");
        WaitElement(acceessRoles.HeaderRoles);
        while (!isElementNotVisible(acceessRoles.RolePolny)) {
            acceessRoles.Next.click();
        }
        actionElementAndClick(acceessRoles.EditPolny);
        WaitElement(acceessRoles.EditRole);
        WaitElement(acceessRoles.InputWordWait);
        inputWord(acceessRoles.InputWord, "Доступ к структурированным медицинским сведениям по ");
        Thread.sleep(2500);
        if (!isElementNotVisible(acceessRoles.CheckBoxAccessMyMOActive)) {
            ClickElement(acceessRoles.CheckBoxAccessMyMO);
        }
        if (isElementNotVisible(acceessRoles.CheckBoxAccessMOActive)) {
            ClickElement(acceessRoles.CheckBoxAccessMOActive);
        }
        Thread.sleep(1500);
        ClickElement(acceessRoles.UpdateWait);
        Thread.sleep(2500);
        /** Выбор Направления  */
        sms.ChoosingDirection(sms.Oncology, sms.OncologyWait, "Онкология", false);
        SMSMethod(getResultSearchOnko, "vimis.sms", "Онкологии", false);
        /** Выбор Направления  */
        sms.ChoosingDirection(sms.Prevention, sms.PreventionWait, "Профилактика", false);
        SMSMethod(getResultSearchPrev, "vimis.preventionsms", "Профилактики", false);
        /** Выбор Направления  */
        sms.ChoosingDirection(sms.Akineo, sms.AkineoWait, "Акушерство и неонатология", false);
        SMSMethod(getResultSearchAkineo, "vimis.akineosms", "Акушерства и неонатологии", false);
        /** Выбор Направления  */
        sms.ChoosingDirection(sms.SSZ, sms.SSZWait, "Сердечно-сосудистые заболевания", false);
        SMSMethod(getResultSearchSSZ, "vimis.cvdsms", "Сердечно-сосудистых заболеваний", false);
        /** Выбор Направления  */
        sms.ChoosingDirection(sms.Other, sms.OtherWait, "Иные профили", false);
        SMSMethod(getResultSearchOther, "vimis.remd_sent_result", "Иных профилей", false);
        ClickElement(acceessRoles.RolesWait1);
        System.out.println("Редактирование роли");
        WaitElement(acceessRoles.HeaderRoles);
        while (!isElementNotVisible(acceessRoles.RolePolny)) {
            acceessRoles.Next.click();
        }
        actionElementAndClick(acceessRoles.EditPolny);
        WaitElement(acceessRoles.EditRole);
        WaitElement(acceessRoles.InputWordWait);
        inputWord(acceessRoles.InputWord, "Доступ к структурированным медицинским сведениям по ");
        Thread.sleep(2500);
        if (!isElementNotVisible(acceessRoles.CheckBoxAccessMyMOActive)) {
            ClickElement(acceessRoles.CheckBoxAccessMyMO);
        }
        if (!isElementNotVisible(acceessRoles.CheckBoxAccessMOActive)) {
            ClickElement(acceessRoles.CheckBoxAccessMO);
        }
        Thread.sleep(1500);
        ClickElement(acceessRoles.UpdateWait);
        Thread.sleep(1500);
        /** Выбор Направления  */
        sms.ChoosingDirection(sms.Oncology, sms.OncologyWait, "Онкология", true);
        SMSMethod(getResultSearchOnko, "vimis.sms", "Онкологии", true);
        /** Выбор Направления  */
        sms.ChoosingDirection(sms.Prevention, sms.PreventionWait, "Профилактика", true);
        SMSMethod(getResultSearchPrev, "vimis.preventionsms", "Профилактики", true);
        /** Выбор Направления  */
        sms.ChoosingDirection(sms.Akineo, sms.AkineoWait, "Акушерство и неонатология", true);
        SMSMethod(getResultSearchAkineo, "vimis.akineosms", "Акушерства и неонатологии", true);
        /** Выбор Направления  */
        sms.ChoosingDirection(sms.SSZ, sms.SSZWait, "Сердечно-сосудистые заболевания", true);
        SMSMethod(getResultSearchSSZ, "vimis.cvdsms", "Сердечно-сосудистых заболеваний", true);
        /** Выбор Направления  */
        sms.ChoosingDirection(sms.Other, sms.OtherWait, "Иные профили", true);
        SMSMethod(getResultSearchOther, "vimis.remd_sent_result", "Иных профилей", true);
    }
}
