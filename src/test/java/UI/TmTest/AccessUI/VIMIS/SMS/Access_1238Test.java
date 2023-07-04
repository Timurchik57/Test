package UI.TmTest.AccessUI.VIMIS.SMS;

import UI.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.SMS;
import api.BaseAPI;
import api.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис - СМС")
public class Access_1238Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    SMS sms;

    public void Access_1238Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String SMS, By District
    ) throws InterruptedException, IOException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        sms = new SMS(driver);
        System.out.println("Отправляем смс с id = 3");
        xml.ApiSmd(
                FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1
        );
        System.out.println("Проверяем, что значения msg_id и request_id пустые ");
        sql.StartConnection("select * from " + SMS + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("msg_id");
            Assertions.assertNull(sql.value, "Значение msg_id заполнено");
            sql.value = sql.resultSet.getString("request_id");
            Assertions.assertNull(sql.value, "Значение request_id заполнено");
        }
        System.out.println("Переходим в СМС, находим отправленный документ и отправляем принудительно");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(sms.SMSWait);
        ClickElement(District);
        ClickElement(sms.FilterWaitAdd);
        ClickElement(sms.Ident);
        Thread.sleep(1500);
        inputWord(driver.findElement(sms.localUid), "" + xml.uuid + " ");
        ClickElement(sms.SearchWait);
        Thread.sleep(1000);
        ClickElement(By.xpath("(//tr[1]/td[7][contains(.,'" + xml.uuid + "')]/preceding-sibling::td[6]//span)[1]"));
        Thread.sleep(1500);
        ClickElement(sms.Send);
        WaitElement(sms.Success);
        Thread.sleep(2000);
        System.out.println("Проверяем, что значения msg_id и request_id заполнены");
        sql.StartConnection("select * from " + SMS + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("msg_id");
            Assertions.assertNotEquals(sql.value, null, "Значение msg_id не заполнено");
            sql.value = sql.resultSet.getString("request_id");
            Assertions.assertNotEquals(sql.value, null, "Значение request_id не заполнено");
        }
    }

    @Test
    @Story("Проверка, что СМС принудительно отправляется в ВИМИС")
    @Issue(value = "TEL-1238")
    @Link(name = "ТМС-1484", url = "https://team-1okm.testit.software/projects/5/tests/1484?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка, что СМС принудительно отправляется в ВИМИС для Онкологии")
    @Description("Отправляем смс, принудительно отправляем в ВИМИС через удкон, проверяем, что добавились значения msg_id и request_id")
    public void Access_1238_1() throws SQLException, InterruptedException, IOException {
        sms = new SMS(driver);
        Access_1238Method("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", sms.OncologyWait);
    }

    @Test
    @Story("Проверка, что СМС принудительно отправляется в ВИМИС")
    @DisplayName("Проверка, что СМС принудительно отправляется в ВИМИС для Профилактики")
    public void Access_1238_2() throws SQLException, InterruptedException, IOException {
        sms = new SMS(driver);
        Access_1238Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", sms.PreventionWait);
    }

    @Test
    @Story("Проверка, что СМС принудительно отправляется в ВИМИС")
    @DisplayName("Проверка, что СМС принудительно отправляется в ВИМИС для Акушерство и неонатология")
    public void Access_1238_3() throws SQLException, InterruptedException, IOException {
        sms = new SMS(driver);
        Access_1238Method("SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", sms.AkineoWait);
    }

    @Test
    @Story("Проверка, что СМС принудительно отправляется в ВИМИС")
    @DisplayName("Проверка, что СМС принудительно отправляется в ВИМИС для Сердечно-сосудистые заболевания")
    public void Access_1238_4() throws SQLException, InterruptedException, IOException {
        sms = new SMS(driver);
        Access_1238Method("SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", sms.SSZWait);
    }
}
