package UI.TmTest.AccessUI.Statistick;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
public class Access_1133Test extends BaseTest {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;
    public String SNILS;

    @Test
    @Issue(value = "TEL-1133")
    @Issue(value = "TEL-1178")
    @Link(name = "ТМС-1441", url = "https://team-1okm.testit.software/projects/5/tests/1441?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Link(name = "ТМС-1445", url = "https://team-1okm.testit.software/projects/5/tests/1445?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Добавление заданий в Маршрутах ОМП")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок гдк есть маршруты, переходим к пациенту, у пациента смотрим на блок добавления заданий. Добавляем задание, проверяем бд")
    public void Access_1133() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();
        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.MIAC);
        ClickElement(analyticsMO.Analytics);
        System.out.println("Удаляем запись, которую создадим далее (нужно, если тест упал и не дошёл да конца)");
        sql.UpdateConnection(
                "delete from vimis.routes_tasks where task_header = '1133---------------------------------------------------------------------------------------------------------';");
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
        System.out.println("Выбираем первого пациента и переходим к нему");
        WaitNotElement3(analyticsMO.Loading, 30);
        ClickElement(analyticsMO.FirstPatient);
        WaitElement(analyticsMO.Snils);
        String Snils = driver.findElement(analyticsMO.Snils).getText();
        ClickElement(analyticsMO.Action);
        ClickElement(analyticsMO.ActionAddTask);
        WaitElement(analyticsMO.TaskPatient);
        Thread.sleep(2000);
        if (KingNumber == 4) {
            Thread.sleep(15000);
        }
        System.out.println(
                "Берём численное значение всех заданий, считаем отображаемые задания, берём список заданий из бд и сравниваем");
        List<WebElement> QuantityList = driver.findElements(analyticsMO.TaskList);
        String AllTask = driver.findElement(analyticsMO.AllTask).getText();
        Integer Error = Integer.valueOf(driver.findElement(analyticsMO.ErrorTask).getText());
        Assertions.assertEquals(
                QuantityList.size(), Integer.valueOf(AllTask),
                "Численное значение всех заданий и отображаемые задания не совпадают"
        );
        sql.StartConnection("SELECT count(rt.task_header) FROM vimis.routes_tasks rt\n" +
                                    "join vimis.nosological_patients np on rt.nosological_patient_id = np.id \n" +
                                    "join vimis.nosological_patients_details npd on np.patient_guid = npd.patient_guid \n" +
                                    "where npd.snils = '" + Snils + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(
                QuantityList.size(), Integer.valueOf(sql.value),
                "Значение заданий в бд и отображаемые задания не совпадают"
        );
        System.out.println("Создаём задание");
        ClickElement(analyticsMO.NewTask);
        ClickElement(analyticsMO.Done);
        System.out.println("Проверяем обязательность полей");
        String error = driver.findElement(analyticsMO.Header).getAttribute("class");
        Assertions.assertEquals(
                error, "el-form-item is-error is-required el-form-item--medium",
                "Поле Заголовок не выделилось обязательным"
        );
        error = driver.findElement(analyticsMO.Text).getAttribute("class");
        Assertions.assertEquals(
                error, "el-form-item is-error is-required el-form-item--medium",
                "Поле Текст задания не выделилось обязательным"
        );
        error = driver.findElement(analyticsMO.Data).getAttribute("class");
        Assertions.assertEquals(
                error, "el-form-item is-error is-required el-form-item--medium",
                "Поле Дата окончания не выделилось обязательным"
        );
        error = driver.findElement(analyticsMO.MO).getAttribute("class");
        Assertions.assertEquals(
                error, "el-form-item is-error is-required el-form-item--medium",
                "Поле Мед организация не выделилось обязательным"
        );
        System.out.println("Заполняем поля");
        inputWord(
                analyticsMO.HeaderInput,
                "1133----------------------------------------------------------------------------------------------------------"
        );
        ClickElement(analyticsMO.Done);
        inputWord(
                analyticsMO.TextInput,
                "1133----------------------------------------------------------------------------------------------------------"
        );
        ClickElement(analyticsMO.Done);
        ClickElement(analyticsMO.Data);
        ClickElement(analyticsMO.Day1);
        ClickElement(analyticsMO.Done);
        SelectClickMethod(analyticsMO.MO, authorizationObject.SelectFirst);
        ClickElement(analyticsMO.Done);
        Thread.sleep(1500);
        System.out.println("Проверяем что запись появилась");
        WaitElement(analyticsMO.TaskHeader);
        System.out.println("Проверяем Появление полного названия заголовка");
        actions.moveToElement(driver.findElement(analyticsMO.TaskHeader));
        actions.perform();
        Thread.sleep(1000);
        WaitElement(analyticsMO.Tooltip);
        ClickElement(analyticsMO.AllTask);
        if (KingNumber == 4) {
            driver.navigate().refresh();
            WaitElement(analyticsMO.TaskText);
        }
        System.out.println("Проверяем Появление полного названия Текст задания");
        actions.moveToElement(driver.findElement(analyticsMO.TaskText));
        actions.perform();
        Thread.sleep(1000);
        if (KingNumber != 4) {
            WaitElement(analyticsMO.Tooltip1);
        } else {
            WaitElement(analyticsMO.Tooltip);
        }
        System.out.println("Считаем отображаемые задания, берём список заданий из бд и сравниваем");
        QuantityList = driver.findElements(analyticsMO.TaskList);
        sql.StartConnection("SELECT count(rt.task_header) FROM vimis.routes_tasks rt\n" +
                                    "join vimis.nosological_patients np on rt.nosological_patient_id = np.id \n" +
                                    "join vimis.nosological_patients_details npd on np.patient_guid = npd.patient_guid \n" +
                                    "where npd.snils = '" + Snils + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(
                QuantityList.size(), Integer.valueOf(sql.value),
                "Значение заданий в бд и отображаемые задания не совпадают"
        );
        System.out.println("Ищем созданную задачу в бд");
        sql.StartConnection(
                "SELECT rt.id, rt.task_header, rt.task, rt.estimated_end_date, npd.last_name, npd.first_name, npd.middle_name, npd.snils, npd.birthdate  FROM vimis.routes_tasks rt\n" +
                        "join vimis.nosological_patients np on rt.nosological_patient_id = np.id \n" +
                        "join vimis.nosological_patients_details npd on np.patient_guid = npd.patient_guid  where rt.task_header = '1133---------------------------------------------------------------------------------------------------------';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            SNILS = sql.resultSet.getString("snils");
        }
        Assertions.assertEquals(SNILS, Snils, "Задача создалась на другого пациента");
        sql.UpdateConnection(
                "update vimis.routes_tasks set estimated_end_date = '" + Date + " 00:10:29.600' where id = '" + sql.value + "';");
        driver.navigate().refresh();
        Thread.sleep(1500);
        WaitElement(analyticsMO.TaskText);
        System.out.println("Берём число просроченных заданий и сверяем с бд");
        WaitElement(analyticsMO.ErrorTask);
        Integer ErrorNew = Integer.valueOf(driver.findElement(analyticsMO.ErrorTask).getText());
        Assertions.assertEquals(Error + 1, ErrorNew, "Просроченных заданий не увеличилось");
        System.out.println("Сравниваем просроченную дату с бд");
        String EndDate = driver.findElement(analyticsMO.EndDate).getText();
        date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        Date = formatForDateNow.format(date);
        Assertions.assertEquals(EndDate, Date, "Даты не совпадают");
        sql.UpdateConnection("delete from vimis.routes_tasks where id = '" + sql.value + "';");
    }
}
