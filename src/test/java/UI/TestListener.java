package UI;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogType;

import java.io.FileInputStream;
import java.util.Properties;

import static UI.TmTest.BaseTest.InputClassFile;
import static UI.TmTest.BaseTest.driver;

public class TestListener implements TestWatcher {

    @SneakyThrows
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Allure.getLifecycle().addAttachment(
                "Скриншот на месте падания теста", "image/png", "png",
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)
        );
        Allure.addAttachment(
                "Логи после падения теста: ", String.valueOf(driver.manage().logs().get(LogType.BROWSER).getAll()));
        WebDriverManager.chromedriver().quit();
        driver.quit();

        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();
        if (props.getProperty("IfCountListner").contains("web") == true) {
            InputClassFile();
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        Allure.getLifecycle().addAttachment(
                "Скриншот после успешного прохождения теста", "image/png", "png",
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)
        );
        Allure.addAttachment(
                "Логи после успешного прохождения теста: ",
                String.valueOf(driver.manage().logs().get(LogType.BROWSER).getAll())
        );
        WebDriverManager.chromedriver().quit();
        driver.quit();
    }
}
