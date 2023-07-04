package UI.TmTest.PageObject.Directions;

import UI.TmTest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ConsultationSchedule extends BaseTest {
    /**
     * Направления - Расписание консультаций
     */
    public ConsultationSchedule(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления - Расписание консультаций
     */
    @FindBy(xpath = "//li//a[@href='/operator/doctorschedule']")
    public WebElement SchedulesElement;
    public By SchedulesWait = By.xpath("//li//a[@href='/operator/doctorschedule']");
    public By Schedules = By.linkText("Расписание консультаций");
}
