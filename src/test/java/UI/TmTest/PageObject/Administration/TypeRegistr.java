package UI.TmTest.PageObject.Administration;

import UI.TmTest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TypeRegistr extends BaseTest {
    public TypeRegistr(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Администрирование - Типы регистров
     */
    @FindBy(xpath = "//a[@href='/permission/registers']")
    public WebElement TypeRegistr;
    public By TypeRegistrWait = By.xpath("//a[@href='/permission/registers']");
    /**
     * Типы регистров - Заголовок
     */
    public By Header = By.xpath("//h1[.='Типы регистров']");
    /**
     * Администрирование - Типы регистров
     */
    @FindBy(xpath = "//button[contains(.,'Добавить регистр')]")
    public WebElement AddRegistr;
    public By AddRegistrWait = By.xpath("//button[contains(.,'Добавить регистр')]");
    /**
     * Добавить регистр - Заголовок
     */
    public By HeaderAddRegistrWait = By.xpath("//h3[.='Добавить регистр']");
    /**
     * Добавить регистр - Введите наименование регистра
     */
    @FindBy(xpath = "//input[@placeholder='Введите наименование регистра']")
    public WebElement InputNameRegistr;
    public By NameRegistrWait = By.xpath("//label[contains(.,'Наименование регистра')]");
    public By AlertNameRegistr = By.xpath(
            "//label[contains(.,'Наименование регистра')]/following-sibling::div[contains(.,'Регистр с таким наименованием уже существует')]");
    /**
     * Добавить регистр - Введите краткое наименование регистра
     */
    @FindBy(xpath = "//input[@placeholder='Введите краткое наименование']")
    public WebElement InputShortNameRegistr;
    /**
     * Добавить регистр - Выберите источник данных
     */
    public By SourceData = By.xpath("//input[@placeholder='Выберите источник данных']");
    public By SelectSourceDataOnko = By.xpath("//div[@x-placement]//li/span[contains(.,'Онкология')]");
    public By SelectSourceDataPrev = By.xpath("//div[@x-placement]//li/span[contains(.,'Профилактика')]");
    public By SelectSSZ = By.xpath("//div[@x-placement]//li/span[contains(.,'Сердечно-сосудистые заболевания')]");
    /**
     * Добавить регистр - Признак отбора
     */
    public By Sign = By.xpath("//label[contains(.,'Признак отбора')]/following-sibling::div/label");
    /**
     * Добавить регистр - По дате оперативного вмешательства
     */
    public By OP = By.xpath(
            "//span[contains(.,'По дате оперативного вмешательства')][@class='el-radio__label']/preceding::span[1]");
    /**
     * Добавить регистр - Операции
     */
    public By Operations = By.xpath("//div[@id='tab-2']");
    /**
     * Операции - Добавить услугу
     */
    public By AddService = By.xpath("//button/span[contains(.,'Добавить услугу')]");
    /**
     * Операции - Добавить услугу - Введите код услуги
     */
    @FindBy(xpath = "//input[@placeholder='Введите код услуги']")
    public WebElement CodeService;
    /**
     * Операции - Добавить услугу - Введите код услуги
     */
    @FindBy(xpath = "//div/strong[text()='A26.08.028']/parent::div/following-sibling::div/button")
    public WebElement AddCodeService;
    public By AddCodeServiceWait = By.xpath("//div/strong[text()='A26.08.028']");
    /**
     * Добавить регистр - Специалисты
     */
    public By Specialists = By.xpath("//div[@id='tab-1'][contains(.,'Специалисты')]");
    /**
     * Специалисты - Добавить специалиста
     */
    public By AddSpecialists = By.xpath("//button/span[contains(.,'Добавить специалиста')]");
    /**
     * Специалисты - Добавить специалиста - Введите ФИО специалиста
     */
    public By SearchAddSpecialists = By.xpath("//input[@placeholder='Введите ФИО специалиста']");
    /**
     * Специалисты - Добавить специалиста - Введите ФИО специалиста - АБАБКОВ ВЯЧЕСЛАВ ГЕННАДЬЕВИЧ
     */
    public By ABABKOV = By.xpath("//div[contains(.,'АБАБКОВ ВЯЧЕСЛАВ ГЕННАДЬЕВИЧ')]/following-sibling::div/button");
    public By ABABKOVWait = By.xpath("//li[@class='list__item']/div[contains(.,'АБАБКОВ ВЯЧЕСЛАВ ГЕННАДЬЕВИЧ')]");
    /**
     * Добавить регистр - Добавить диагноз
     */
    @FindBy(xpath = "//button/span[contains(.,'Добавить диагноз')]")
    public WebElement AddDiagnosis;
    /**
     * Добавить регистр - Добавить диагноз - Select
     */
    public By SelectAddDiagnosisWait = By.xpath("//div[@class='select__container']");
    /**
     * Добавить регистр - Добавить диагноз - Введите код регистра
     */
    @FindBy(xpath = "//input[@placeholder='Введите код регистра']")
    public WebElement CodRegistr;
    /**
     * Добавить регистр - Добавить диагноз - Введите код регистра - 1 элемент из списка
     */
    @FindBy(xpath = "//div[@class='select__container']//ul/li[1]/div/button[1]")
    public WebElement FirstCodRegistr;
    public By FirstCodRegistrWait = By.xpath("//div[@class='select__container']//ul/li[1]/div/button[1]");
    @FindBy(xpath = "//div[@class='select__container']//ul/li[1]/div/button[2]")
    public WebElement FirstCodRegistr2;
    /**
     * Добавить регистр - Добавить диагноз - Введите код регистра - A00
     */
    public By CodRegistrA00 = By.xpath("//ul/li//strong[text()='A00']");
    public By CodRegistrA001 = By.xpath("//ul/li//strong[text()='A00.1']");
    /**
     * Добавить регистр - Добавить диагноз - Введите код регистра - 2 элемент из списка
     */
    @FindBy(xpath = "//div[@class='select__container']//ul/li[2]/div/button[2]")
    public WebElement SecondCodRegistr;
    /**
     * Добавить регистр - Добавить диагноз - Введите код регистра - 4 элемент из списка
     */
    @FindBy(xpath = "//div[@class='select__container']//ul/li[4]/div/button")
    public WebElement FourCodRegistr;
    /**
     * Добавить регистр - Добавить диагноз - Введите код регистра - последний элемент из списка
     */
    @FindBy(xpath = "//div[@class='select__container']//ul/li[last()]/div/button")
    public WebElement LastCodRegistr;

    /**
     * Добавить регистр - Добавить диагноз - Введите код регистра - Добавить
     */
    @FindBy(xpath = "//footer/button/span[contains(.,'Добавить')]")
    public WebElement AddCodRegistr;
    public By AddCodRegistrWait = By.xpath("//footer/button/span[contains(.,'Добавить')]");

    /**
     * Добавить регистр - Добавить диагноз - Введите код регистра - Обновить
     */
    @FindBy(xpath = "//footer/button/span[contains(.,'Обновить')]")
    public WebElement Update;

    /**
     * Добавить регистр - Добавить диагноз - Введите код регистра - Закрыть
     */
    @FindBy(xpath = "//footer/button/span[contains(.,'Закрыть')]")
    public WebElement Close;

    /**
     * Добавить регистр - Добавить диагноз - Введите код регистра - Сохранить
     */
    public By Save = By.xpath("//button/span[contains(.,'Сохранить')]");
    /**
     * Добавить регистр - Добавить диагноз - Данные успешно добавлены
     */
    @FindBy(xpath = "//div[@role='alert']//div[@class='el-notification__closeBtn el-icon-close']")
    public WebElement AlertSuccessClose;
    public By AlertSuccess = By.xpath("//div[@role='alert']//p[contains(.,'Данные успешно добавлены')]");
    /**
     * Добавить регистр - Добавить диагноз - Данные успешно обновлены
     */
    public By UpdateSuccessTrue = By.xpath("//div[@role='alert']//p[contains(.,'Данные успешно обновлены')]");
    /**
     * Добавить регистр - Добавить диагноз - Значение диапазона уже указано в списке
     */
    public By AlertFalse = By.xpath("//div[@role='alert']//p[contains(.,'Значение диапазона уже указано в списке')]");
    /**
     * Типы регистров - Нужный регистр из списка
     */
    public By TrueRegistr = By.xpath("//table//tbody//tr//span[contains(.,'ТЕСТ')]");
    /**
     * Типы регистров - ССЗ
     */
    public By SSZ = By.xpath("(//tbody//span[contains(.,'ССЗ')])[1]");
    /**
     * Типы регистров - Регистр онкология
     */
    public By ONKO = By.xpath("(//tbody//span[contains(.,'Регистр онкология')])[1]");

    /**
     * Типы регистров - следующая страница
     */
    @FindBy(xpath = "//button[@class='btn-next']")
    public WebElement Next;
    public By NextWait = By.xpath("//button[@class='btn-next']");

    /**
     * Типы регистров - следующая страница - неактивная кнопка
     */
    public By NextDisabled = By.xpath("//button[@class='btn-next'][@disabled='disabled']");

    /**
     * Типы регистров - Нужный регистр из списка - Редактировать
     */
    @FindBy(xpath = "//table//tbody//tr//span[contains(.,'ТЕСТ')]/parent::div/parent::td/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement Edit;
    /**
     * Типы регистров - Нужный регистр из списка - ССЗ - Редактировать
     */
    @FindBy(xpath = "(//tbody//span[contains(.,'ССЗ')])[1]/parent::div/parent::td/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement EditSSZ;
    public By EditSSZWait = By.xpath(
            "(//tbody//span[contains(.,'ССЗ')])[1]/parent::div/parent::td/following-sibling::td//button[@tooltiptext='Редактировать']");
    /**
     * Типы регистров - Нужный регистр из списка - Регистр онкология - Редактировать
     */
    public By EditONKO = By.xpath(
            "(//tbody//span[contains(.,'Регистр онкология')])[1]/parent::div/parent::td/following-sibling::td//button[@tooltiptext='Редактировать']");
    /**
     * Типы регистров - Нужный регистр из списка - Удалить
     */
    @FindBy(xpath = "//table//tbody//tr//span[contains(.,'ТЕСТ')]/parent::div/parent::td/following-sibling::td//button[@text='Удалить данный пункт?']")
    public WebElement Delete;
    @FindBy(xpath = "//div[@aria-hidden='false']//span[contains(.,'Удалить')]")
    public WebElement DeleteButton;
    public By AlertDelete = By.xpath("//div[@aria-hidden='false']//span[contains(.,'Удалить')]");
    /**
     * Типы регистров - Нужный регистр из списка - Данные успешно удалены
     */
    public By DeleteSuccess = By.xpath("//div[@role='alert']//p[contains(.,'Данные успешно удалены')]");
    /**
     * Обновить регистр - Заголовок
     */
    public By HeaderUpdate = By.xpath("//h3[.='Обновить регистр']");
    /**
     * Обновить регистр - Список Диагнозов
     */
    public By ListDiagnosis = By.xpath("//div[@class='margin-bottom-3']/strong/span");
    /**
     * Обновить регистр - Список Диагнозов - 1 диагноз
     */
    public By FirstListDiagnosis = By.xpath("(//div[@class='margin-bottom-3']/strong/span)[1]");
    /**
     * Обновить регистр - Список Диагнозов - Нужный по счёту диагноз
     */
    public Integer TrueNumber;
    /**
     * Обновить регистр - Список Диагнозов - Нужное название диагноза
     */
    public String SelectedDiagnosis;
    public String NameDiagnosis;
    /**
     * Обновить регистр - Список Диагнозов - Алерт удаления - Удалить
     */
    @FindBy(xpath = "//div[@x-placement='top']//span[contains(.,'Удалить')]")
    public WebElement AlertDeleteDiagnosis;
    public By AlertDeleteDiagnosisWait = By.xpath("//div[@x-placement='top']//span[contains(.,'Удалить')]");
    /**
     * Закрыть
     */
    @FindBy(xpath = "//button/span[contains(.,'Закрыть')]")
    public WebElement CloseRegistr;
}
