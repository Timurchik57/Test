package UI.TmTest.AccessUI;

import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Проверка страниц help и changelog")
public class HelpChangelogsTest extends BaseTest {
    String Test = "https://tm-test.pkzdrav.ru";
    String Dev = "https://tm-dev.pkzdrav.ru";
    String Hmao = "https://remotecons-test.miacugra.ru";
    String URL;
    AuthorizationObject authorizationObject;

    @Description("Переход на страницы help и changelog")
    @DisplayName("Проверка страниц help и changelog")
    @Test
    public void HelpChangelog() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        System.out.println("Авторизация и открытие help");
        AuthorizationMethod(authorizationObject.MIAC);
        Thread.sleep(1000);
        if (KingNumber == 1) {
            URL = Test;
        }
        if (KingNumber == 2) {
            URL = Dev;
        }
        if (KingNumber == 4) {
            URL = Hmao;
        }
        driver.get(URL + "/help");
        WaitElement(By.xpath("//div[@class='center-block']"));
        System.out.println("help работает");
        System.out.println("Открытие changelog");
        if (KingNumber == 1) {
            URL = Test;
        }
        if (KingNumber == 2) {
            URL = Dev;
        }
        if (KingNumber == 4) {
            URL = Hmao;
        }
        driver.get(URL + "/changelog");
        WaitElement(By.xpath("//div[@class='center-block']"));
        System.out.println("changelog работает");
    }
}
