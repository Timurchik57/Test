package UI.TmTest.AccessUI.Directions;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class CreateDirectionsAccessSpecializationTest extends BaseTest {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    SQL sql;

    @Test
    @Issue(value = "TEL-1015")
    @Link(name = "ТМС-1350", url = "https://team-1okm.testit.software/projects/5/tests/1350?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка специальности при создании консультации на диагностику")
    @Description("Переходим в Направления на квоты - Создать направление - Направление на диагностику - Ввести снилс созданного пользователя - Далее - нажимаем на специальность и сверяем с таблицей dpc.medical_and_pharmaceutical_specialties.name (только актуальные, т.е dateout = NULL)")
    public void CreateDirectionsAccessSpecialization() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        sql = new SQL();
        /** Авторизуемся и переходим в создание консультации */
        System.out.println("Авторизуемся и переходим в создание консультации");
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            AuthorizationMethod(authorizationObject.MIAC);
        }
        if (KingNumber == 3) {
            AuthorizationMethod(authorizationObject.Seva);
        }
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            directionsForQuotas.CreateConsultationEquipmentMethod();
            ClickElement(directionsForQuotas.Specialization);
            List<String> Select = new ArrayList<String>();
            if (isElementNotVisible(authorizationObject.BottomStart)) {
                List<WebElement> Specialization = driver.findElements(directionsForQuotas.SelectSpecializationBottom);
                for (int i = 0; i < Specialization.size(); i++) {
                    Select.add(Specialization.get(i).getText());
                }
                Collections.sort(Select);
            } else {
                List<WebElement> Specialization = driver.findElements(directionsForQuotas.SelectSpecializationBottom);
                for (int i = 0; i < Specialization.size(); i++) {
                    Select.add(Specialization.get(i).getText());
                }
                Collections.sort(Select);
            }
            /** Берём значение специальности из бд */
            System.out.println("Берём значение специальности из бд");
            List<String> SelectSql = new ArrayList<String>();
            sql.StartConnection(
                    "Select * from dpc.medical_and_pharmaceutical_specialties where dateout is null and parent is not null;");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("name");
                SelectSql.add(sql.value);
            }
            Collections.sort(SelectSql);
            /** Проверяем совпадение значений */
            System.out.println("Проверяем совпадение значений");
            Assertions.assertEquals(Select, SelectSql, "Значения специальности в вебе и бд не совпадают");
        }
    }
}
