package UI.TmTest.AccessUI.Statistick;

import UI.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import api.BaseAPI;
import api.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
public class Access_1761Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;

    @Step("Сортируем документы по дате в обратном порядке")
    public void Access_1761Method() throws InterruptedException {
        analyticsMO = new AnalyticsMO(driver);

        WaitElement(analyticsMO.Snils);
        WaitNotElement3(analyticsMO.LoadingDocs, 20);
        Thread.sleep(1500);
        ClickElement(analyticsMO.Sort);
        ClickElement(analyticsMO.CreateDate);
        WaitNotElement3(analyticsMO.LoadingDocsFederal, 20);
        ClickElement(analyticsMO.SortUp);
        WaitNotElement3(analyticsMO.LoadingDocsFederal, 20);
    }

    @Test
    @Issue(value = "TEL-1761")
    @Link(name = "ТМС-1771", url = "https://team-1okm.testit.software/projects/5/tests/1771?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение статуса смс в ЛК Врача")
    @Description("Отправляем смс - добавляеям статус 1 в logs - добавляем статус 1 в remd. Авторизумся и переходим в Лк врача пациента и проверяем, что статус меняется на Опубликовано в ФВИМИС/Опубликовано в ФВИМИС и ФРЭМД")
    public void Access_1761() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Очищаем смс с нужной датой, чтобы не было ошибок, после падения теста");
        sql.UpdateConnection(
                "update vimis.additionalinfo set effectivetime = '2020-08-10 12:47:00.000 +0500' where effectivetime = '2100-08-10 12:47:00.000 +0500';");

        System.out.println("Отправляем смс в ВИМИС и РЭМД");
        if (KingNumber == 1) {
            PatientGuid = "a69b201e-4353-42a1-9b20-1e435322a11b";
        } else {
            PatientGuid = "40dad3c5-939d-4928-8f09-dded1a5cb940";
        }
        xml.ApiSmd("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21);

        System.out.println("Берём id отправленной смс и меняем у неё дату");
        sql.StartConnection(
                "Select * from vimis.sms where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            System.out.println(sql.value);
        }
        sql.UpdateConnection(
                "Update vimis.additionalinfo set effectivetime = '2100-08-10 12:47:00.000 +0500' where smsid = '" + sql.value + "';");

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        System.out.println("Проверяем где есть маршруты, во всех блоках");
        WaitElement(analyticsMO.Tall);
        WaitElement(analyticsMO.Average);
        WaitElement(analyticsMO.low);
        analyticsMO.QuantityStackMethod();
        System.out.println(AnalyticsMO.TallMO);
        System.out.println(AnalyticsMO.AverageMO);
        System.out.println(AnalyticsMO.lowMO);
        System.out.println("Переходим в первый попавшийся блок, у которого есть мо с этапами");
        if (AnalyticsMO.TallMO) {
            ClickElement(analyticsMO.NameMOTallFirst);
        } else {
            if (AnalyticsMO.AverageMO) {
                ClickElement(analyticsMO.NameMOAverageFirst);
            } else {
                ClickElement(analyticsMO.NameMOlowFirst);
            }
        }
        System.out.println("Выбираем нужного пациента и переходим к нему");
        if (KingNumber == 1) {
            ClickElement(analyticsMO.SecondPatient);
        } else {
            ClickElement(analyticsMO.FirstPatient);
        }
        Access_1761Method();

        System.out.println("Первая проверка - статуса у смс нет");
        WaitNotElement3(analyticsMO.FirstDocsFVimis, 1);

        System.out.println("Устанавливаем status = 1 в vimis.documentlogs");
        sql.UpdateConnection(
                "insert into vimis.documentlogs (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + sql.value + "', '" + UUID.randomUUID() + "')");
        driver.navigate().refresh();
        Access_1761Method();

        System.out.println("Вторая проверка - статус у смс Опубликовано в ФВИМИС");
        WaitElement(analyticsMO.FirstDocsFVimis);

        System.out.println("В таблице vimis.remd_onko_sent_result Создаём запись со статусом 1");
        sql.UpdateConnection(
                "insert into vimis.remd_onko_sent_result (sms_id, local_uid, status, created_datetime, fremd_status) values (" + sql.value + ", '" + xml.uuid + "', 'success', '" + Date + " 00:00:00.888 +0500', 1);");
        driver.navigate().refresh();
        Access_1761Method();

        System.out.println("Третья проверка - статус у смс Опубликовано в ФВИМИС и ФРЭМД");
        WaitElement(analyticsMO.FirstDocsFVimisFremd);
    }
}

