package UI.TmTest.AccessUI.Administration;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.PageObject.Administration.RouteOMP;
import UI.TmTest.PageObject.AuthorizationObject;
import api.BaseAPI;
import api.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Добавение и Удаление маршрута ОМП")
public class Access_256Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    RouteOMP routeOMP;
    SQL sql;
    private String SQLRegName;
    private String SQLMarName;
    private String SQLOMPName;
    private String SQLPodOMPName;
    private String SQLNextPodOMPName;

    @Test
    @Story("Администрирование")
    @Issue(value = "TEL-256")
    @Link(name = "ТМС-1378", url = "https://team-1okm.testit.software/projects/5/tests/945?isolatedSection=9087652d-2067-4fe3-9490-66bb61e906ba")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Добавение и Удаление маршрута ОМП")
    @Description("Переходим в администрирование - настройка маршрутов ОМП, добавляем новый маршрут со всеми обязательными полями, проверяем добавление в БД. После удаляем созданный маршрут")
    public void Access_256() throws SQLException, InterruptedException {
        routeOMP = new RouteOMP(driver);
        authorizationObject = new AuthorizationObject(driver);
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Авторизуемся и переходим в настройка маршрутов ОМП");
            AuthorizationMethod(authorizationObject.MIAC);
            ClickElement(routeOMP.RouteOMPWait);
            System.out.println("Добавление нового маршрута");
            WaitElement(routeOMP.Header);
            ClickElement(routeOMP.Add);
            System.out.println("Выбираем регистр");
            ClickElement(routeOMP.Registers);
            WaitElement(routeOMP.SelectNameFirst);
            String RegName = driver.findElement(routeOMP.SelectNameFirst).getText();
            ClickElement(authorizationObject.SelectFirst);
            System.out.println("Выбираем Тип");
            ClickElement(routeOMP.Federation);
            System.out.println("Выбираем Наименование маршрута");
            ClickElement(routeOMP.NameRoute);
            WaitElement(routeOMP.SelectNameFirst);
            String MarName = driver.findElement(routeOMP.SelectNameLast).getText();
            ClickElement(authorizationObject.SelectLast);
            System.out.println("Выбираем ОМП");
            ClickElement(routeOMP.OMP);
            WaitElement(routeOMP.SelectNameFirst);
            String OMPName = driver.findElement(routeOMP.SelectNameFirst).getText();
            ClickElement(authorizationObject.SelectFirst);
            System.out.println("Выбираем Подэтап ОМП");
            ClickElement(routeOMP.PodOMP);
            WaitElement(routeOMP.SelectNameFirst);
            String PodOMPName = driver.findElement(routeOMP.SelectNameFirst).getText();
            ClickElement(authorizationObject.SelectFirst);
            System.out.println("Выбираем Следующий подэтап ОМП");
            ClickElement(routeOMP.NextPodOMP);
            WaitElement(routeOMP.SelectNameSecond);
            String NextPodOMPName = driver.findElement(routeOMP.SelectNameSecond).getText();
            ClickElement(authorizationObject.SelectSecond);
            inputWord(driver.findElement(routeOMP.NormTime), "3211");
            ClickElement(routeOMP.AddTwo);
            Thread.sleep(1500);
            sql.StartConnection(
                    "select med.\"name\", t.\"name\" Rname, st.stage, su.short_name, sub.short_name NextName, m.\"period\"\n" +
                            "from vimis.medical_care_procedure_settings_period_stages m\n" +
                            "join vimis.medical_care_procedure_routes me on m.graph_id  =  me.id\n" +
                            "join vimis.medical_care_procedure_detailed med on med.id=me.graph_id\n" +
                            "join vimis.medical_care_procedure_registers_for_routes r on r.graph_id = me.id\n" +
                            "join telmed.registertypes t on t.id = r.register_id\n" +
                            "join vimis.medical_care_procedure_routes_stages st on st.id = m.stage_id\n" +
                            "join vimis.medical_care_procedure_routes_substages su on su.id = m.start_state\n" +
                            "join vimis.medical_care_procedure_routes_substages sub on sub.id = m.final_state\n" +
                            "where \"period\" = '321';");
            while (sql.resultSet.next()) {
                SQLRegName = sql.resultSet.getString("rname");
                SQLMarName = sql.resultSet.getString("name");
                SQLOMPName = sql.resultSet.getString("stage");
                SQLPodOMPName = sql.resultSet.getString("short_name");
                SQLNextPodOMPName = sql.resultSet.getString("nextname");
                sql.value = sql.resultSet.getString("period");
            }
            Assertions.assertEquals(SQLRegName, RegName, "Регистр не совпадает");
            Assertions.assertEquals(SQLMarName, MarName, "Наименование маршрута не совпадает");
            Assertions.assertEquals(SQLOMPName, OMPName, "ОМП не совпадает");
            Assertions.assertEquals(SQLPodOMPName, PodOMPName, "Подэтап ОМП не совпадает");
            Assertions.assertEquals(SQLNextPodOMPName, NextPodOMPName, "Следующий подэтап ОМП не совпадает");
            Assertions.assertEquals(sql.value, "321", "Нормативный срок не совпадает");
            System.out.println("Удаляем Маршрут");
            inputWord(routeOMP.NameMarch, "Траектория КАСC");
            ClickElement(routeOMP.Search);
            Thread.sleep(1500);
            ClickElement(routeOMP.Delete);
            ClickElement(routeOMP.DeleteYes);
        }
    }
}
