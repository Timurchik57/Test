package UI.TmTest.PageObject.Directions.Consultation;

import UI.TmTest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ConsultationOutgoingUnfinished extends BaseTest {
    /**
     * Консультации / Исходящие / Незавершенные
     */
    public ConsultationOutgoingUnfinished(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Консультации / Исходящие / Незавершенные
     */
    public By Consultation = By.xpath(
            "//a[@href='/direction/requestConsultation/uncompleted?directionType=2&directionTarget=1&directionPageType=1']");
    /**
     * Консультации / Исходящие / Незавершенные - 1 запись в таблице
     */
    public By ConsultationFirst = By.xpath("//tbody//tr[1]");
    /**
     * Записи на странице - 1 запись - Фамилия
     **/
    public By LastName = By.xpath("(//tbody/tr/td/div/span[contains(.,'Отправлен')])[1]/preceding::td[2]");
    public String Class = "el-button el-button--primary el-button--small";
    /**
     * id записи
     */
    public By NumberConsultation = By.xpath("//span[contains(.,'Номер консультации')]");
    /**
     * Дата отправки консультации
     */
    public By DateConsultation = By.xpath("//td[contains(.,'Дата отправки консультации:')]/following-sibling::td");
    /**
     * 1 запись в таблице - Добавить ссылку
     */
    @FindBy(xpath = "//button[contains(.,'Добавить ссылку')]")
    public WebElement AddLink;
    public By AddLinkWait = By.xpath("//button[contains(.,'Добавить ссылку')]");
    /**
     * 1 запись в таблице - Добавить файлы
     */
    @FindBy(xpath = "//button[contains(.,'Добавить файлы')]")
    public WebElement AddFile;
    public By AddFileWait = By.xpath("//button[contains(.,'Добавить файлы')]");
    /**
     * 1 запись в таблице - Добавить файлы - Прикрепление файла
     */
    @FindBy(xpath = "(//input[@type='file'])[4]")
    public WebElement File;
    /**
     * 1 запись в таблице - Добавить файлы - Закрыть
     */
    public By AddFileClose = By.xpath(
            "(//button[@class='el-button el-button--primary el-button--medium is-plain'][contains(.,'Закрыть')])[3]");
    /**
     * 1 запись в таблице - Добавить файлы - 2 добавленный файл
     */
    public By AddFileDownload2 = By.xpath("(//tbody/tr[contains(.,'test.txt')])[4]/td[contains(.,'Скачать')]");
    /**
     * 1 запись в таблице - Скачать архивом
     */
    @FindBy(xpath = "//button[contains(.,'Скачать архивом')]")
    public WebElement Download;
    public By DownloadWait = By.xpath("//button[contains(.,'Скачать архивом')]");
    /**
     * 1 запись в таблице - Сформировать протокол
     */
    @FindBy(xpath = "//button[contains(.,'Сформировать протокол')]")
    public WebElement AddProt;
    public By AddProtWait = By.xpath("//button[contains(.,'Сформировать протокол')]");
    /**
     * Сформировать протокол - Сформированный протокол
     */
    public By CreateDirectionAfter = By.xpath("//td[contains(.,'Протокол_Тестировщик_Т.Т.docx')]");
}
