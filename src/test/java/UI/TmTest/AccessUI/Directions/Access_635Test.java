package UI.TmTest.AccessUI.Directions;

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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class Access_635Test extends BaseTest {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;

    @Issue(value = "TEL-635")
    @Link(name = "ТМС-1483", url = "https://team-1okm.testit.software/projects/5/tests/1483?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отображение количества пациентов РРП")
    @Description("Переходим в создание консультации и выбираем расширенный поиск, ввести Иванов и проверить отображение количества пациентов в зависимости с параметром 10/20/30")
    public void Access_635() throws IOException, SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        /** Авторизация и переход в создание консультации */
        System.out.println("Авторизация и переход в пользователи");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);
        /** Переход в создание консультации  */
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);
        /** Создать консультацию - добавить пациента */
        System.out.println("Создать консультацию - добавить пациента");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.CreateWait);
        directionsForQuotas.Create.click();
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.DistrictDiagnostic.click();
        directionsForQuotas.Next.click();
        ClickElement(directionsForQuotas.BigSearchWait);
        WaitElement(directionsForQuotas.LastNameWait);
        inputWord(directionsForQuotas.LastName, "Ивановв");
        ClickElement(directionsForQuotas.SearchWait);
        Thread.sleep(5000);
        if (KingNumber == 4) {
            Thread.sleep(3000);
        }
        System.out.println("Проверка отображения 10 пациентов");
        List<WebElement> list = driver.findElements(directionsForQuotas.PatientList);
        Assertions.assertEquals(list.size(), 10, "Количество не совпадает с 10");
        System.out.println("Проверка отображения 20 пациентов");
        ClickElement(directionsForQuotas.PatientListNumber);
        ClickElement(directionsForQuotas.PatientListNumber20);
        Thread.sleep(5000);
        list = driver.findElements(directionsForQuotas.PatientList);
        Assertions.assertEquals(list.size(), 20, "Количество не совпадает с 20");
        System.out.println("Проверка отображения 30 пациентов");
        ClickElement(directionsForQuotas.PatientListNumber);
        ClickElement(directionsForQuotas.PatientListNumber30);
        Thread.sleep(5000);
        list = driver.findElements(directionsForQuotas.PatientList);
        Assertions.assertEquals(list.size(), 30, "Количество не совпадает с 30");
    }
}
