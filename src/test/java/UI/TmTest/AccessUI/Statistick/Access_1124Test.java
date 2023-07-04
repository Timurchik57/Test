package UI.TmTest.AccessUI.Statistick;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.RoutesTask;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
public class Access_1124Test extends BaseTest {
    AuthorizationObject authorizationObject;
    SQL sql;
    RoutesTask routesTask;
    public String URL;
    public Integer Quantity;
    public String mo_recieve;
    public String doc_recieve;
    public String nosological_patient_id;
    public String ID;

    /**
     * Подсчет количества заданий во всех блоках
     */
    @Step("Подсчет количества заданий в блоке {0}")
    public void QuantityRoutesMethod(String Name) throws SQLException {
        if (Name == "Входящие") {
            for (int i = 1; i < 4; i++) {
                List<WebElement> RoutWeb = driver.findElements(
                        By.xpath("//p[contains(.,'" + Name + "')]/following-sibling::div/div[" + i + "]/div"));
                if (RoutWeb.size() > 1) {
                    if (i == 1) {
                        sql.StartConnection("select count(r.id) from vimis.routes_tasks r\n" +
                                                    "join dpc.mis_sp_mu msm on r.mo_recieve = msm.idmu \n" +
                                                    "join telmed.users u on r.doc_recieve = u.id \n" +
                                                    "where msm.namemu = 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and u.sname != 'Хакимова' and r.status != '3';");
                        while (sql.resultSet.next()) {
                            Quantity = Integer.valueOf(sql.resultSet.getString("count"));
                        }
                        Assertions.assertEquals(
                                RoutWeb.size() - 1, Quantity,
                                "Количество заданий  в " + Name + " - Всего заданий не совпадает "
                        );
                        System.out.println(RoutWeb.size() - 1);
                    }
                    if (i == 2) {
                        sql.StartConnection("select count(r.id) from vimis.routes_tasks r\n" +
                                                    "join dpc.mis_sp_mu msm on r.mo_recieve = msm.idmu \n" +
                                                    "join telmed.users u on r.doc_recieve = u.id \n" +
                                                    "where u.sname = 'Хакимова' and r.status != '3';");
                        while (sql.resultSet.next()) {
                            Quantity = Integer.valueOf(sql.resultSet.getString("count"));
                        }
                        Assertions.assertEquals(
                                RoutWeb.size() - 1, Quantity,
                                "Количество заданий  в " + Name + " - Персональные задания не совпадает "
                        );
                        System.out.println(RoutWeb.size() - 1);
                    }
                    if (i == 3) {
                        sql.StartConnection("select count(r.id) from vimis.routes_tasks r\n" +
                                                    "left join dpc.mis_sp_mu msm on r.mo_recieve = msm.idmu \n" +
                                                    "left join telmed.users u on r.doc_recieve = u.id \n" +
                                                    "where msm.namemu != 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and r.status = '3' and u.sname = 'Хакимова' \n" +
                                                    "or msm.namemu = 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and r.status = '3';");
                        while (sql.resultSet.next()) {
                            Quantity = Integer.valueOf(sql.resultSet.getString("count"));
                        }
                        Assertions.assertEquals(
                                RoutWeb.size() - 1, Quantity,
                                "Количество заданий  в " + Name + " - Выполнено не совпадает "
                        );
                        System.out.println(RoutWeb.size() - 1);
                    }
                } else {
                    if (i == 1) {
                        System.out.println("Нет заданий в блоке - Всего заданий");
                    }
                    if (i == 2) {
                        System.out.println("Нет заданий в блоке - Персональные задания");
                    }
                    if (i == 3) {
                        System.out.println("Нет заданий в блоке - Выполнено");
                    }
                }
            }
        }
        if (Name == "Исходящие") {
            List<WebElement> RoutWeb = driver.findElements(
                    By.xpath("//p[contains(.,'" + Name + "')]/following-sibling::div/div[1]/div"));
            if (RoutWeb.size() > 1) {
                sql.StartConnection("select count(r.id) from vimis.routes_tasks r\n" +
                                            "left join dpc.mis_sp_mu msm on r.mo_recieve = msm.idmu \n" +
                                            "left join telmed.users u on r.doc_recieve = u.id \n" +
                                            "where msm.namemu != 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and u.sname != 'Хакимова' or msm.namemu != 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and r.doc_recieve is null;");
                while (sql.resultSet.next()) {
                    Quantity = Integer.valueOf(sql.resultSet.getString("count"));
                }
                Assertions.assertEquals(
                        RoutWeb.size() - 1, Quantity, "Количество заданий  в " + Name + " не совпадает ");
                System.out.println(RoutWeb.size() - 1);
            } else {
                System.out.println("Нет заданий в блоке - " + Name + "");
            }
        }
    }

    @Test
    @Issue(value = "TEL-1124")
    @Link(name = "ТМС-1438", url = "https://team-1okm.testit.software/projects/5/tests/1438?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Метод получения информации о входящих/исходящих задачах по пациентам")
    @Description("Переходим в {hostaddres}/stats/routes-tasks, добавляем новое задание в vimis.routes_tasks, изменяем и проверяем отображение на ui")
    public void Access_1124() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        sql = new SQL();
        routesTask = new RoutesTask(driver);
        System.out.println("Авторизуемся берём куки и переходим в задания, вставляем куки");
        AuthorizationMethod(authorizationObject.OKB);
        Thread.sleep(2000);
        Cookie Session = driver.manage().getCookieNamed(".AspNetCore.Session");
        Cookie TelemedC1 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC1");
        Cookie TelemedC2 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC2");
        Cookie Telemed = driver.manage().getCookieNamed(".AspNet.Core.Telemed");
        if (KingNumber == 1) {
            URL = "https://tm-test.pkzdrav.ru";
        }
        if (KingNumber == 2) {
            URL = "https://tm-dev.pkzdrav.ru";
        }
        if (KingNumber == 4) {
            URL = "https://remotecons-test.miacugra.ru";
        }
        driver.get(URL + "/stats/routes-tasks");
        driver.manage().deleteAllCookies();
        driver.manage().addCookie(Session);
        driver.manage().addCookie(TelemedC1);
        driver.manage().addCookie(TelemedC2);
        driver.manage().addCookie(Telemed);
        driver.navigate().refresh();
        WaitElement(routesTask.RoutesTaskHeader);
        WaitElement(routesTask.Incoming);
        WaitElement(routesTask.Outgoing);
        System.out.println("Сверяем количество заданий во входящие");
        QuantityRoutesMethod("Входящие");
        System.out.println("Сверяем количество заданий в исходящие");
        QuantityRoutesMethod("Исходящие");
        System.out.println("Создаём задание в МО под которой авторизованы на врача отличного от авторизованного");
        if (KingNumber == 1) {
            mo_recieve = "12";
            doc_recieve = "4785";
            nosological_patient_id = "149";
        }
        if (KingNumber == 2) {
            mo_recieve = "12";
            doc_recieve = "2805";
            nosological_patient_id = "344061";
        }
        if (KingNumber == 4) {
            mo_recieve = "100";
            doc_recieve = "1310";
            nosological_patient_id = "1660";
        }
        sql.UpdateConnection(
                "insert into vimis.routes_tasks (author_id, task_header, task, create_date, estimated_end_date, mo_recieve, doc_recieve, nosological_patient_id) values ('25', 'Проверяем заявку 1124 -------------------------------------------------------------', 'Проверяем заявку 1124 -------------------------------------------------------------', '" + Date + " 00:00:10.234', '" + Date + " 16:34:10.234', '" + mo_recieve + "', '" + doc_recieve + "', '" + nosological_patient_id + "');");
        Thread.sleep(1500);
        System.out.println("Проверяем задание в Входящие - Всего заданий");
        driver.navigate().refresh();
        WaitElement(routesTask.Incoming1Add);
        sql.StartConnection(
                "Select id from vimis.routes_tasks where task_header = 'Проверяем заявку 1124 -------------------------------------------------------------';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }
        System.out.println("Обновляем задание в Входящие - Персональные задания");
        doc_recieve = "25";
        sql.UpdateConnection(
                "update vimis.routes_tasks set doc_recieve = '" + doc_recieve + "' where id = '" + sql.value + "';");
        Thread.sleep(1500);
        System.out.println("Проверяем задание в Входящие - Персональные задания");
        driver.navigate().refresh();
        WaitElement(routesTask.Incoming2Add);
        System.out.println("Обновляем задание в Входящие - Персональные задания (2 способ)");
        if (KingNumber == 1 || KingNumber == 2) {
            mo_recieve = "176";
        }
        if (KingNumber == 4) {
            mo_recieve = "79";
        }
        sql.UpdateConnection(
                "update vimis.routes_tasks set mo_recieve = '" + mo_recieve + "' where id = '" + sql.value + "';");
        Thread.sleep(1500);
        System.out.println("Проверяем задание в Входящие - Персональные задания");
        driver.navigate().refresh();
        WaitElement(routesTask.Incoming2Add);
        System.out.println("Обновляем задание в Входящие - Выполнено");
        sql.UpdateConnection("update vimis.routes_tasks set status = '3' where id = '" + sql.value + "';");
        Thread.sleep(1500);
        System.out.println("Проверяем задание в Входящие - Выполнено");
        driver.navigate().refresh();
        WaitElement(routesTask.Incoming3Add);
        if (KingNumber == 1) {
            mo_recieve = "12";
            doc_recieve = "4785";
        }
        if (KingNumber == 2) {
            mo_recieve = "12";
            doc_recieve = "2805";
        }
        if (KingNumber == 4) {
            mo_recieve = "100";
            doc_recieve = "1310";
        }
        System.out.println("Обновляем задание в Входящие - Выполнено (2 способ)");
        sql.UpdateConnection(
                "update vimis.routes_tasks set mo_recieve = '" + mo_recieve + "', doc_recieve = '" + doc_recieve + "' where id = '" + sql.value + "';");
        Thread.sleep(1500);
        System.out.println("Проверяем задание в Входящие - Выполнено");
        driver.navigate().refresh();
        if (KingNumber == 4) {
            Thread.sleep(3000);
        }
        WaitElement(routesTask.Incoming3Add);
        System.out.println("Обновляем задание в Исходящие ");
        if (KingNumber == 1 || KingNumber == 2) {
            mo_recieve = "176";
        }
        if (KingNumber == 4) {
            mo_recieve = "79";
        }
        sql.UpdateConnection(
                "update vimis.routes_tasks set doc_recieve = NULL, mo_recieve = '" + mo_recieve + "' where id = '" + sql.value + "';");
        Thread.sleep(1500);
        System.out.println("Проверяем задание в Исходящие");
        driver.navigate().refresh();
        WaitElement(routesTask.Outgoing1Add);
        System.out.println("Обновляем задание в Исходящие (2 способ) ");
        if (KingNumber == 1) {
            doc_recieve = "4785";
        }
        if (KingNumber == 2) {
            doc_recieve = "2805";
        }
        if (KingNumber == 4) {
            doc_recieve = "1310";
        }
        sql.UpdateConnection(
                "update vimis.routes_tasks set doc_recieve = '" + doc_recieve + "' where id = '" + sql.value + "';");
        Thread.sleep(1500);
        System.out.println("Проверяем задание в Исходящие");
        driver.navigate().refresh();
        WaitElement(routesTask.Outgoing1Add);
        System.out.println("Обновляем задание в Исходящие (3 способ) ");
        sql.UpdateConnection("update vimis.routes_tasks set status = '1' where id = '" + sql.value + "';");
        Thread.sleep(1500);
        System.out.println("Проверяем задание в Исходящие");
        driver.navigate().refresh();
        WaitElement(routesTask.Outgoing1Add);
        System.out.println("Обновляем задание в Исходящие (4 способ) ");
        sql.UpdateConnection("update vimis.routes_tasks set doc_recieve = NULL where id = '" + sql.value + "';");
        Thread.sleep(1500);
        System.out.println("Проверяем задание в Исходящие");
        driver.navigate().refresh();
        WaitElement(routesTask.Outgoing1Add);
        System.out.println("Проверяем отображение полного текста при наведении на заголовок");
        actions.moveToElement(driver.findElement(routesTask.Outgoing1Add));
        actions.perform();
        WaitElement(routesTask.Tooltip);
        System.out.println("Проверяем скролл текста задания");
        String Scroll = driver.findElement(routesTask.Outgoing1AddScroll).getCssValue("overflow");
        Assertions.assertEquals(Scroll, "scroll", "Нет скролла у текста задания");
        sql.UpdateConnection("delete from vimis.routes_tasks where id = '" + sql.value + "';");
    }
}
