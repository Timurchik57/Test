package UI.TmTest.AccessUI.Directions;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationArchived;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class ConsultationArchivedTest extends BaseTest {
    AuthorizationObject authorizationObject;
    ConsultationArchived consultationArchived;

    @Test
    @DisplayName("Проверка отображения консультаций за определённое время")
    @Description("Переходим в Направления - Консультации - Входящие - Архивные и смотрим, что консультации прогружаются менее чем за 20 секунд")
    public void ConsultationArchived() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        consultationArchived = new ConsultationArchived(driver);
        System.out.println("Проверка отображения консультаций за определённое время");
        /**  Переход в Направления - Консультации - Входящие - Архивные **/
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(consultationArchived.ArchivedWait);
        actionElementAndClick(consultationArchived.Archived);
        WaitElement(consultationArchived.HeaderWait);
        /**  Проверка отображения 1 и последней записи **/
        System.out.println("Проверка отображения 1 и последней записи");
        WaitElement(consultationArchived.FirstWait);
        WaitElement(consultationArchived.LastWait);
        System.out.println("Записи отображаются менее чем за 20 секунд");
    }
}
