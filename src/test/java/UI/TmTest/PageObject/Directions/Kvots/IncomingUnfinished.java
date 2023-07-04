package UI.TmTest.PageObject.Directions.Kvots;

import UI.TmTest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

public class IncomingUnfinished extends BaseTest {
    /**
     * Направления на квоты / Входящие / Незавершенные
     */
    public IncomingUnfinished(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления на квоты / Входящие / Незавершенные
     */
    public By ConsultationWait = By.xpath(
            "//a[@href='/direction/targetDiagnostic/uncompleted?directionType=1&directionTarget=2&directionPageType=1']");
    /**
     * Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице - МО
     */
    public By FirstLineMO = By.xpath("//tbody/tr[1]/td[2]//span");
    /**
     * Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице - Дата
     */
    public By FirstLineDate = By.xpath("//tbody/tr[1]/td[4]//span");
    /**
     * Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице - Пациент
     */
    public By FirstLinePatient = By.xpath("//tbody/tr[1]/td[7]//span");
    /**
     * Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице - Снилс
     */
    public By FirstLineSnils = By.xpath("//tbody/tr[1]/td[8]//span");
    /**
     * Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице - Статус
     */
    public By FirstLineStatus = By.xpath("(//tbody/tr[1]/td[9]//span)[3]");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Тип даты
     */
    public By TypeDate = By.xpath("//input[@placeholder='Тип даты']");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Дата направления
     */
    public By SelectDate = By.xpath("//ul/li/span[contains(.,'Дата направления')]");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Дата направления
     */
    public By FirstDate = By.xpath("//input[@placeholder='C какого числа']");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Текущая дата
     */
    public By ToDay = By.xpath("//td[@class='available today']");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Текущая дата
     */
    public By ToDay1 = By.xpath("//td[@class='available today in-range start-date end-date']");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Текущая дата
     */
    public By ToDay2 = By.xpath("//td[@class='available today in-range start-date']");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Снилс
     */
    public By Snils = By.xpath("//input[@placeholder='Введите СНИЛС']");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Поиск
     */
    public By Search = By.xpath("//span[contains(.,'Поиск')]");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Нет данных
     */
    public By NotBad = By.xpath("//div[@class='el-table__empty-block']/span[contains(.,'Нет данных')]");
    /**
     * Строка в таблице - Отменить направление
     */
    public By CloseDirection = By.xpath("(//button/span[contains(.,'Отменить направление')])[1]");
    /**
     * Строка в таблице - Отклонить направление
     */
    public By CloseOutDirection = By.xpath("(//button/span[contains(.,'Отклонить направление')])[1]");
    /**
     * Отменить направление - Причина отмены
     */
    public By CloseText = By.xpath("//textarea");
    /**
     * Отменить направление - Отменить направление
     */
    public By CloseDirection2 = By.xpath("(//button/span[contains(.,'Отменить направление')])[2]");
    /**
     * Строка в таблице - Отклонить направление
     */
    public By CloseOutDirection2 = By.xpath("(//button/span[contains(.,'Отклонить направление')])[2]");
}
