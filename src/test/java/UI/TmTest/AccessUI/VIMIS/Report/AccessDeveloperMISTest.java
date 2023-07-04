package UI.TmTest.AccessUI.VIMIS.Report;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.Report;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис - Отчёты")
public class AccessDeveloperMISTest extends BaseTest {
    AuthorizationObject authorizationObject;
    Users users;
    Report report;
    SQL sql;

    @Issue(value = "TEL-551")
    @Link(name = "ТМС-1153", url = "https://team-1okm.testit.software/projects/5/tests/1153?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Формирование отчета ВИМИС для разработчиков МИС")
    @Description("Создаём нового пользователя, указываем ему нужную МО с Разработчиком МИС. Авторизуемся под созданным пользователем и проверяем в ВИМИС- Отчёты список доступных Информационных систем, сравниваем значения с БД")
    public void AccessNumberCharacters() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);
        report = new Report(driver);
        sql = new SQL();
        if (KingNumber != 4) {
            /** Метод для генерации СНИЛС и авторизации */
            authorizationObject.GenerationSnilsAndAuthorizationMethod();
            /** Условие, нужно ли заполнять данные о пользователе */
            if (!isElementNotVisible(users.NotHeaderIEMK)) {
                AddUsersMethod(users.WorkBRB, users.Division5, users.RoleUserDeveloperMIS, "");
                System.out.println("Новый пользователь добавлен в ИЭМК");
            } else {
                WaitElement(users.TrueAlertWait);
                users.CloseAlert.click();
                System.out.println("Пользователь с данным СНИЛС уже есть в базе ИЕМК");
                users.Update.click();
            }
            /** Авторизуемся под созданным пользователем */
            System.out.println("Авторизуемся под созданным пользователем");
            if (KingNumber == 1) {
                driver.get(
                        "https://tm-test.pkzdrav.ru/auth/bysnils?snils=" + authorizationObject.GenerationSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            }
            if (KingNumber == 2) {
                driver.get(
                        "https://tm-dev.pkzdrav.ru/auth/bysnils?snils=" + authorizationObject.GenerationSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
            }
            if (isElementNotVisible(authorizationObject.Alert)) {
                authorizationObject.OK.click();
            }
            /** Переходим в Вимис - Отчёты */
            System.out.println("Переходим в Вимис - Отчёты");
            ClickElement(report.ReportWait);
            ClickElement(report.IsWait);
            Thread.sleep(1500);
            /** Берём все значения в Информационная система */
            System.out.println("Берём все значения в Информационная система");
            List<String> GetIS = new ArrayList<>();
            List<WebElement> IS = driver.findElements(report.SelectIs);
            for (int i = 0; i < IS.size(); i++) {
                GetIS.add(IS.get(i).getText());
            }
            Collections.sort(GetIS);
            System.out.println(GetIS);
            String name = null;
            if (KingNumber == 1) {
                name = "ООО «КОМТЕК»";
            }
            if (KingNumber == 2) {
                name = "ООО «Облачные технологии»";
            }
            System.out.println("Берём все значения в SQL");
            List<String> SQLIS = new ArrayList<>();
            sql.StartConnection(
                    "SELECT c.id, c.fullname, c.\"name\", c.isunloading, md.\"name\", c.email  FROM telmed.centralized_unloading_systems c\n" +
                            "join telmed.mis_developers md on c.developer_id = md.id\n" +
                            "where md.\"name\" ='" + name + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("name");
                SQLIS.add(sql.value);
            }
            Collections.sort(SQLIS);
            /** Проверка на совпадение */
            System.out.println("Проверка на совпадение");
            Assertions.assertEquals(GetIS, SQLIS);
            System.out.println("Значения на интерфейсе " + GetIS + " и значения в БД " + SQLIS + " совпадают");
            /** Увольнение сотрудника для дальнейших тестов */
            users.DeleteUsersMethod(
                    authorizationObject.MIAC, authorizationObject.GenerationSnils, users.BRBEditWait, users.BRBEdit);
        }
    }
}
