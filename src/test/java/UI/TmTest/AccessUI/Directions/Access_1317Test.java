package UI.TmTest.AccessUI.Directions;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import api.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class Access_1317Test extends BaseTest {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;

    @Issue(value = "TEL-1317")
    @Link(name = "ТМС-1504", url = "https://team-1okm.testit.software/projects/5/tests/1504?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка обязательности поля ЕНП")
    @Description("Перейти в Консультации - Создать направление - Направление на диагностику - Проверяем обязательность поля ЕНП")
    public void Access_1317() throws IOException, SQLException, InterruptedException, AWTException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        AuthorizationMethod(authorizationObject.OKB);
        System.out.println("Переход в создание консультации на оборудование");
        ClickElement(directionsForQuotas.ConsultationWait);
        /** Создание консультации */
        System.out.println("Создание консультации");
        ClickElement(directionsForQuotas.CreateWait);
        System.out.println("Выбираем консультацию");
        ClickElement(directionsForQuotas.DistrictDiagnosticWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        Thread.sleep(1500);
        System.out.println("Выбираем тип полиса Полис ОМС старого образца");
        SelectClickMethod(directionsForQuotas.PolisTypeWait, directionsForQuotas.PolisTypeSelect);
        inputWord(directionsForQuotas.PolisENP, "dfg");
        WaitElement(directionsForQuotas.PolisTypeError2);
        System.out.println("Выбираем тип полиса Полис ОМС единого образца");
        SelectClickMethod(directionsForQuotas.PolisTypeWait, directionsForQuotas.PolisTypeSelectEdin);
        inputWord(directionsForQuotas.PolisENP, "dfgл");
        WaitElement(directionsForQuotas.PolisTypeError3);
    }
}
