package UI.TmTest.PageObject.Administration;

import UI.TmTest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MedOrganization extends BaseTest {
    public MedOrganization(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Администрирование - Мед. Организации
     */
    @FindBy(xpath = "//a[@href='/permission/access']")
    public WebElement Organization;
    public By OrganizationWait = By.xpath("//a[@href='/permission/access']");
    /**
     * Заголовок Мед. Организации
     */
    public By HeaderOrganizationWait = By.xpath("//h1[.='Мед. организации']");
    /**
     * Ввод МО
     */
    public By InputOrganizationWait = By.xpath("//input[@placeholder='Название медицинской организации']");
    /**
     * Выбор МО
     */
    public By SelectMO = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Яцкив')]");
    public By SelectMOKon = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'Кондинская районная стоматологическая поликлиника')]");
    /**
     * Отображение одной мо в селекте
     */
    public By SelectMOOne = By.xpath("//div[@x-placement]//ul/li[@class='el-select-dropdown__item'][1]");
    /**
     * Отображение более одной мо в селекте
     */
    public By SelectMOTwo = By.xpath("//div[@x-placement]//ul/li[@class='el-select-dropdown__item hover'][2]");
    /**
     * Поиск
     */
    @FindBy(xpath = "//div[@class='grid-table']/div[1]/form/div[3]//button[@type='submit']")
    public WebElement Search;
    /**
     * Найденная МО
     */
    public By SearchMO = By.xpath("//div[@class='grid-table']/div[2]//span[contains(.,'Яцкив')]");
    /**
     * 1 МО - Редактировать
     */
    public By EditFirst = By.xpath("(//tbody/tr)[1]/td[3]//button");
    /**
     * Найденная МО - Редактировать
     */
    public By Edit = By.xpath("//tbody/tr[1]/td[3]//button[@tooltiptext='Редактировать']");
    /**
     * Найденная МО - Редактировать - Заголовок
     */
    public By HeaderMOWait = By.xpath("//h3[contains(.,'Карточка медицинской организации')]");
    /**
     * Редактировать - Состояние организации
     */
    public By HealthOrganization = By.xpath("//label[contains(.,'Состояние организации')]");
    /**
     * Редактировать - Пароль COVID - Ввод
     */
    public By Covid = By.xpath("//label[contains(.,'Пароль COVID')]/following-sibling::div//input");
    /**
     * Редактировать - Пароль COVID - Ввод - Ошибка
     */
    public By Error = By.xpath(
            "//div[@class='el-form-item__error'][contains(.,'Недопустимо написание \"пробел\" первым и последним символом')]");
    /**
     * Вкладка - Запись на оборудование
     */
    @FindBy(xpath = "//div[@role='tablist']/div[contains(.,'Запись на оборудование')]")
    public WebElement Recording;
    public By RecordingWait = By.xpath("//div[@role='tablist']/div[contains(.,'Запись на оборудование')]");
    /**
     * Вкладка - Информационные системы
     */
    @FindBy(xpath = "//div[@role='tablist']/div[contains(.,'Информационные системы')]")
    public WebElement InfoSystem;
    /**
     * Информационные системы - Наименование системы
     */
    public By NameSystem = By.xpath("//label[contains(.,'Наименование системы')]");
    /**
     * Вкладка - Информационные системы - Закрыть
     */
    @FindBy(xpath = "(//footer/button/span[contains(.,'Закрыть')])[2]")
    public WebElement CloseInfoSystem;
    /**
     * Гама Камера
     */
    @FindBy(xpath = "//div[@role='tab']/div[contains(.,'Гамма-камера')]")
    public WebElement GammaCamera;
    public By GammaCameraWait = By.xpath("//div[@role='tab']/div[contains(.,'Гамма-камера')]");
    /**
     * КТ
     */
    public By KT = By.xpath("//div[@role='tab']/div[contains(.,'КТ')]");
    /**
     * МРТ
     */
    public By MRT = By.xpath("//div[@role='tab']/div[contains(.,'МРТ')]");
    /**
     * Медицинские организации, получающие консультацию
     */
    public By MOReceiveWait = By.xpath(
            "//div[@class='el-collapse-item is-active'][contains(.,'Медицинские организации, получающие консультацию')]");
    /**
     * Добавить
     */
    @FindBy(xpath = "//div[@class='el-collapse-item is-active']//div[@class='el-table__header-wrapper'][1]//span[contains(.,'Добавить')]")
    public WebElement Add;
    public By AddWait = By.xpath(
            "//div[@class='el-collapse-item is-active']//div[@class='el-table__header-wrapper'][1]//span[contains(.,'Добавить')]");
    /**
     * ведите значение для поиска
     */
    @FindBy(xpath = "//input[@placeholder='Введите значение для поиска']")
    public WebElement SearchValue;
    public By SearchValueWait = By.xpath("//input[@placeholder='Введите значение для поиска']");
    /**
     * Окружная клиническая больница
     */
    public By OKB = By.xpath(
            "//div[@x-placement='bottom-start']//ul/li[@class='el-select-dropdown__item']/span[contains(.,'Окружная клиническая больница')]");
    /**
     * Окружная клиническая больница - Добавленная
     */
    public By OKBTrue = By.xpath(
            "//div[@class='el-table__body-wrapper is-scrolling-none']//tbody/tr//span[contains(.,'БУ ХМАО-Югры \"Окружная клиническая больница\"')]");
    /**
     * Добавить МО
     */
    @FindBy(xpath = "//button[@tooltiptext='Добавить'][1]")
    public WebElement AddMO;
    public By AddMOWait = By.xpath("//div[@class='el-dialog__body']//footer//button//span[contains(.,'Обновить')]");
    /**
     * Закрыть
     */
    @FindBy(xpath = "//button[@tooltiptext='Закрыть']")
    public WebElement Close;
    public By CloseWait = By.xpath("(//footer/button[contains(.,'Закрыть')])[2]");
    /**
     * Обновить
     */
    @FindBy(xpath = "//div[@class='el-dialog__body']//footer//button//span[contains(.,'Обновить')]")
    public WebElement Update;
    public By UpdateWait = By.xpath("//div[@class='el-dialog__body']//footer//button//span[contains(.,'Обновить')]");
}
