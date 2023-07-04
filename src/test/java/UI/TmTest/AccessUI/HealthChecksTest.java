package UI.TmTest.AccessUI;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Cookie;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Проверка HeathCheck без авторизации")
public class HealthChecksTest extends BaseTest {
    AuthorizationObject authorizationObject;

    @Description("Открываем HeathCheck проверяем отсутствие данных без авторизации. После берём Cookies авторизации и проверяем что данные отображаются")
    @DisplayName("Проверка HeathCheck без авторизации")
    @Test
    public void HealthCheck() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        if (KingNumber == 1) {
            driver.get(authorizationObject.GetHealth);
        }
        if (KingNumber == 2) {
            driver.get(authorizationObject.GetHealthDev);
        }
        if (KingNumber == 4) {
            driver.get(authorizationObject.GetHealthHMAO);
        }
        /** Метод проверяющий, что все элементы прогрузились */
        Thread.sleep(40000);
        authorizationObject.WaitIntegrationServicesMethod();
        System.out.println("Страница открывается");
        /** Проверка, что в Интеграционные сервисы не отображаются адреса  */
        authorizationObject.GetIntegrationServicesMethod();
        Assertions.assertEquals(authorizationObject.addressTWApi, "");
        Assertions.assertEquals(authorizationObject.addressApi, "");
        Assertions.assertEquals(authorizationObject.addressHubs, "");
        Assertions.assertEquals(authorizationObject.addressUserStatuses, "");
        Assertions.assertEquals(authorizationObject.addressVWAPI, "");
        Assertions.assertEquals(authorizationObject.addressVAPI, "");
        System.out.println("Адреса не отображаются");
        /** Авторизуемся и берём куки  */
        System.out.println("Авторизуемся и берём куки");
        AuthorizationMethod(authorizationObject.MIAC);
        Thread.sleep(2000);
        Cookie Session = driver.manage().getCookieNamed(".AspNetCore.Session");
        Cookie TelemedC1 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC1");
        Cookie TelemedC2 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC2");
        Cookie Telemed = driver.manage().getCookieNamed(".AspNet.Core.Telemed");
        /** Переходим в health, удаляём куки и вставляем новые */
        System.out.println("Переходим в health, удаляём куки и вставляем новые");
        if (KingNumber == 1) {
            driver.get(authorizationObject.GetHealth);
        }
        if (KingNumber == 2) {
            driver.get(authorizationObject.GetHealthDev);
        }
        if (KingNumber == 4) {
            driver.get(authorizationObject.GetHealthHMAO);
        }
        driver.manage().deleteAllCookies();
        driver.manage().addCookie(Session);
        driver.manage().addCookie(TelemedC1);
        driver.manage().addCookie(TelemedC2);
        driver.manage().addCookie(Telemed);
        /** Проверка, что адреса отображаются */
        System.out.println("Проверка, что адреса отображаются");
        authorizationObject.WaitIntegrationServicesMethod();
        authorizationObject.GetIntegrationServicesMethod();
        Assertions.assertNotEquals(authorizationObject.addressTWApi, "");
        Assertions.assertNotEquals(authorizationObject.addressApi, "");
        Assertions.assertNotEquals(authorizationObject.addressHubs, "");
        Assertions.assertNotEquals(authorizationObject.addressUserStatuses, "");
        Assertions.assertNotEquals(authorizationObject.addressVWAPI, "");
        Assertions.assertNotEquals(authorizationObject.addressVAPI, "");
    }
}
