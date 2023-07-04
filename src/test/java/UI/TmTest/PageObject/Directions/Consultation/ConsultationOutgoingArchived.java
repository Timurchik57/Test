package UI.TmTest.PageObject.Directions.Consultation;

import UI.TmTest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

public class ConsultationOutgoingArchived extends BaseTest {
    /**
     * Направления - Консультации - Исходящие - Архивные
     **/
    public ConsultationOutgoingArchived(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления - Консультации - Исходящие - Архивные
     **/
    public By OutgoingArchived = By.xpath(
            "//a[@href='/direction/requestConsultation/archival?directionType=2&directionTarget=1&directionPageType=2']");
    /**
     * Записи на странице - сортировка desc
     **/
    public By DESK = By.xpath("//thead[@class='has-gutter']//th[1]//i[@class='sort-caret descending']");
    /**
     * Консультация - Текст заключения
     **/
    public By TextConclusion = By.xpath("//div/h3[contains(.,'Заключение')]/parent::div/following-sibling::div/div");
}
