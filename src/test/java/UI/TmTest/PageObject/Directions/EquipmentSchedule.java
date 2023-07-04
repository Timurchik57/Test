package UI.TmTest.PageObject.Directions;

import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.NSI.Equipments;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

public class EquipmentSchedule extends BaseTest {
    /**
     * Расписание оборудования
     */
    public EquipmentSchedule(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Метод для добавления расписания оборудования
     * SelectEquipment - выбираемое оборудование
     * Datastart - дата начала расписания оборудования
     * Dataend - дата окончания расписания оборудования
     */
    public void AddEquipmentSchedule(By SelectEquipment, By Datastart, By Dataend) throws InterruptedException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        EquipmentSchedule equipmentSchedule = new EquipmentSchedule(driver);
        Equipments equipments = new Equipments(driver);

        /** Переход в Создание расписания оборудования */
        System.out.println("Переход в Создание расписания оборудования");
        WaitElement(equipmentSchedule.EquipmentSchedulesWait);
        actionElementAndClick(equipmentSchedule.EquipmentSchedules);
        WaitElement(equipmentSchedule.HeaderEquipmentSchedulesWait);
        WaitElement(equipmentSchedule.ButtonWorkScheduleWait);
        equipmentSchedule.ButtonWorkSchedule.click();
        /** Расписание оборудования / График работы оборудования */
        System.out.println("Расписание оборудования / График работы оборудования");
        WaitElement(equipmentSchedule.HeaderWorkSchedule);
        ClickElement(equipmentSchedule.ButtonEquipmentSchedulesWait);
        /** Составление расписания */
        System.out.println("Составление расписания");
        SelectClickMethod(equipmentSchedule.SelectEquipmentWait, SelectEquipment);
        /** Выбор даты */
        equipmentSchedule.DataStart.click();
        System.out.println("Выбор даты");
        WaitElement(authorizationObject.Xplacement);
        ClickElement(Datastart);
        ClickElement(Dataend);
        WaitNotElement(authorizationObject.Xplacement);
        /** Выбор Времени */
        System.out.println("Выбор Времени");
        SelectClickMethod(equipmentSchedule.TimeStart, equipmentSchedule.SelectTimeStart);
        SelectClickMethod(equipmentSchedule.TimeEnd, equipmentSchedule.SelectTimeEnd);
        /** Интервал */
        System.out.println("Интервал");
        inputWord(equipmentSchedule.Interval, "451");
        /** Дни */
        System.out.println("Дни");
        equipmentSchedule.Day1.click();
        equipmentSchedule.Day2.click();
        equipmentSchedule.Day3.click();
        equipmentSchedule.Day4.click();
        equipmentSchedule.Day5.click();
        equipmentSchedule.Day6.click();
        equipmentSchedule.Day7.click();
        Thread.sleep(1500);
        equipmentSchedule.Add.click();
        Thread.sleep(1500);

        ClickElement(equipments.EquipmentWaitt);
        /** Выбор организации для редактирования */
        WaitElement(equipments.EquipmentWait);
        WaitElement(equipments.HeaderEquipmentWait);
        if (KingNumber == 1 | KingNumber == 2) {
            SelectClickMethod(equipments.InputMoWait, equipments.SelectMO);
            inputWord(equipments.Name, "X-OMATT");
            ClickElement(equipments.CheckIs);
            inputWord(equipments.Description, "1233");
            equipments.Search.click();
            WaitElement(equipments.SearchNameWait);
        }
        if (KingNumber == 4) {
            SelectClickMethod(equipments.InputMoWait, equipments.SelectMOKon);
            ClickElement(equipments.CheckIs);
            inputWord(equipments.Description, "3213");
            equipments.Search.click();
            WaitElement(equipments.FirstLineSet);
        }
        /** Поиск нужного оборудования */
        System.out.println("Поиск нужного оборудования");
        ClickElement(equipments.FirstLineSet);
        WaitElement(equipments.HeaderEditWait);
        WaitElement(equipments.DivisionWait);
        if (KingNumber == 1 | KingNumber == 2) {
            SelectClickMethod(equipments.DivisionWait, equipments.SelectDivision);
            SelectClickMethod(equipments.TypeWait, equipments.SelectType);
            SelectClickMethod(equipments.ModelWait, equipments.SelectModel);
        }
        if (KingNumber == 4) {
            SelectClickMethod(equipments.DivisionWait, equipments.SelectDivisionBuh);
            SelectClickMethod(equipments.TypeWait, equipments.SelectType);
            SelectClickMethod(equipments.ModelWait, equipments.SelectModel);
        }
        inputWord(equipments.Weight, "500");
        Thread.sleep(3000);
        if (isElementNotVisible(equipments.CheckWait)) {
            equipments.Check.click();
        }
        /** Редактирование Исследования */
        System.out.println("Редактирование Исследования");
        equipments.Researches.click();
        WaitElement(equipments.InputWordWait);
        if (!isElementNotVisible(equipments.ResearchesTrue)) {
            inputWord(equipments.InputWord, "HMH");
            WaitElement(equipments.BottomStartWait);
            WaitElement(equipments.HMP01Wait);
            actionElementAndClick(equipments.HMP01);
            wait.until(invisibilityOfElementLocated(equipments.BottomStartWait));
            equipments.Add.click();
            Thread.sleep(1000);
            equipments.Update.click();
            Thread.sleep(1500);
        } else {
            equipments.Close.click();
        }
        System.out.println("Оборудование отредактировано");
    }

    /**
     * Расписание оборудования
     */
    @FindBy(xpath = "//a[@href='/operator/slots']")
    public WebElement EquipmentSchedules;
    public By EquipmentSchedulesWait = By.xpath("//a[@href='/operator/slots']");
    /**
     * Заголовок - Расписание оборудования
     */
    public By HeaderEquipmentSchedulesWait = By.xpath("//h1[contains(.,'Расписание оборудования')]");
    /**
     * Медицинская организация
     */
    public By MO = By.xpath("//div[@class='form-item-label'][contains(.,'Медицинская организация')]");
    /**
     * Тип оборудования
     */
    public By Type = By.xpath("//div[@class='form-item-label'][contains(.,'Тип оборудования')]");
    /**
     * Оборудование
     */
    public By Equipment = By.xpath("//div[@class='form-item-label'][contains(.,'Оборудование')]");
    /**
     * Расписание оборудования - Следующая страница
     */
    @FindBy(xpath = "//div[@class='datepicker']/button[@class='datepicker__btn-next']")
    public WebElement NextPage;
    public By NextPageWait = By.xpath("//div[@class='datepicker']/button[@class='datepicker__btn-next']");
    /**
     * Расписание оборудования - Недоступный слот
     */
    public By NotPageWait = By.xpath(
            "//div[@class='vuecal__cell-events']/div[1]/div[@class='el-tooltip event-info item disabled']");
    /**
     * Расписание оборудования - Колонка где есть слоты
     */
    public By SlotsTrue = By.xpath("//div[@class='vuecal__cell-events']/div[1]");
    /**
     * Расписание оборудования - Колонка где есть слоты - Свободные слоты
     */
    @FindBy(xpath = "(//div[@class='vuecal__cell-events']//div[@class='el-tooltip event-info item free'])[1]")
    public WebElement SlotsFree;
    public By SlotsFreeWait = By.xpath(
            "(//div[@class='vuecal__cell-events']//div[@class='el-tooltip event-info item free'])[1]");
    /**
     * Свободный слот - Запись на приём
     */
    public By WriteWait = By.xpath("//h3[contains(.,'Запись на прием')]");
    /**
     * Свободный слот - Запись на приём - Прикрепите соответсвующие файлы
     */
    @FindBy(xpath = "(//input[@type='file'])[1]")
    public WebElement AddFile;
    @FindBy(xpath = "(//input[@type='file'])[2]")
    public WebElement AddFile2;
    @FindBy(xpath = "(//input[@type='file'])[3]")
    public WebElement AddFile3;
    public By AddFileWait = By.xpath("//p[contains(.,'Прикрепите соответсвующие файлы')]");
    /**
     * Свободный слот - Запись на приём - Файлы
     */
    public By FileWait1 = By.xpath("(//a[contains(.,'test.txt')])[1]");
    public By FileWait2 = By.xpath("(//a[contains(.,'test.txt')])[2]");
    public By FileWait3 = By.xpath("(//a[contains(.,'test.txt')])[3]");
    /**
     * Свободный слот - Запись наприём - Записать
     */
    @FindBy(xpath = "//button/span[contains(.,'Записать')]")
    public WebElement Write;
    public By WriteTwo = By.xpath("//button/span[contains(.,'Записать')]");
    /**
     * Свободный слот - Запись наприём - Запись на прием успешна создана
     */
    @FindBy(xpath = "//button/span[contains(.,'Завершить')]")
    public WebElement AlertClose;
    public By AlertCloseWait = By.xpath("//button/span[contains(.,'Завершить')]");
    public By AlertWait = By.xpath("//p[contains(.,'Запись на прием успешна создана')]");
    /**
     * Свободный слот - Запись наприём - Предупреждение о превышении веса
     */
    public By AlertErrorWait = By.xpath("//div[@aria-label='Предупреждение']");
    /**
     * Отображение фамилии при записи
     */
    public By FioWait = By.xpath(
            "(//div[@class='vuecal__cell-events']/div[1]/div[@class='el-tooltip event-info item busy'])[1]/p[contains(.,'Галиакберов Т.О.')]p[contains(.,'Запись на прием успешна создана')]");
    /**
     * Занятый слот
     */
    @FindBy(xpath = "(//div[@class='vuecal__cell-events']/div[1]/div[@class='el-tooltip event-info item busy'])[1]")
    public WebElement OccupiedSlot;
    public By OccupiedSlotWaitt = By.xpath(
            "(//div[@class='vuecal__cell-events']/div[1]/div[@class='el-tooltip event-info item busy'])[1]");
    public By OccupiedSlotWait = By.xpath("//div[@x-placement][contains(.,'Слот занят')]");
    /**
     * График работы оборудования
     */
    @FindBy(xpath = "//a[@href='/operator/schedule']")
    public WebElement ButtonWorkSchedule;
    public By ButtonWorkScheduleWait = By.xpath("//a[@href='/operator/schedule']");
    /**
     * Расписание оборудования / График работы оборудования
     */
    public By HeaderWorkSchedule = By.xpath("//h1[.='Расписание оборудования / График работы оборудования']");
    /**
     * Добавить Расписание оборудования
     */
    @FindBy(xpath = "//button//span[contains(.,'Добавить расписание')]")
    public WebElement ButtonEquipmentSchedules;
    public By ButtonEquipmentSchedulesWait = By.xpath("//button//span[contains(.,'Добавить расписание')]");
    /**
     * Добавить Расписание оборудования - Оборудование
     */
    @FindBy(xpath = "//label[contains(.,'Оборудование')]/following-sibling::div")
    public WebElement SelectEquipment;
    public By SelectEquipmentWait = By.xpath("//label[contains(.,'Оборудование')]/following-sibling::div");
    /**
     * Добавить Расписание оборудования - Оборудование - X-OMAT
     */
    public By SelectEquipmentChoice = By.xpath("//div[@x-placement]//li/span[contains(.,'X-OMAT')]");
    public By SelectEquipmentChorus = By.xpath("//div[@x-placement]//li/span[contains(.,'Chorus 1.5T')]");
    public By SelectEquipmentCDR = By.xpath("//div[@x-placement]//li/span[contains(.,'CDR Kit')]");
    /**
     * Добавить Расписание оборудования - Оборудование - Дата начала
     */
    @FindBy(xpath = "//input[@placeholder='Дата начала']")
    public WebElement DataStart;
    /**
     * Добавить Расписание оборудования - Оборудование - Дата начала - Текущая дата
     */
    @FindBy(xpath = "//tr/td[@class='available today']")
    public WebElement DataToday;
    public By DataTodayWait = By.xpath("//tr/td[@class='available today']");
    /**
     * Добавить Расписание оборудования - Оборудование - Дата начала - Завтра
     */
    public By Tomorrow = By.xpath("//tr/td[@class='available today']/following-sibling::td[1]");
    /**
     * Добавить Расписание оборудования - Оборудование - 1 число следующего месяца
     */
    public By NextMouth = By.xpath(
            "(//div[@class='el-picker-panel__content el-date-range-picker__content is-right']//td[@class='available'])[1]");
    /**
     * Добавить Расписание оборудования - Оборудование - Дата начала - Через день, после текущей даты
     */
    @FindBy(xpath = "//tr/td[@class='available today in-range start-date end-date']/following-sibling::td[2]")
    public WebElement DataTodayTwo;
    public By DataTodayTwoWait = By.xpath(
            "//tr/td[@class='available today in-range start-date end-date']/following-sibling::td[2]");
    /**
     * Добавить Расписание оборудования - Оборудование - Дата начала - Другая дата
     */
    @FindBy(css = ".is-right tr:nth-child(4) td:nth-child(1)")
    public WebElement DataAnother;
    public By DataAnotherWait = By.cssSelector(".is-right tr:nth-child(4) td:nth-child(1)");
    /**
     * Добавить Расписание оборудования - Оборудование - Время начала
     */
    public By TimeStart = By.xpath("//input[@placeholder='Начало']");
    public By SelectTimeStart = By.xpath("//div[@x-placement]//div[@class='time-select-item'][10]");
    /**
     * Добавить Расписание оборудования - Оборудование - Время окончания
     */
    public By TimeEnd = By.xpath("//input[@placeholder='Окончание']");
    public By SelectTimeEnd = By.xpath("//div[@x-placement]//div[@class='time-select-item'][150]");
    /**
     * Добавить Расписание оборудования - Оборудование - Интервал
     */
    @FindBy(xpath = "//input[@placeholder='от 1 до 60']")
    public WebElement Interval;
    /**
     * Добавить Расписание оборудования - Оборудование - Дни
     */
    @FindBy(xpath = "//div[@role='group']/label[1]")
    public WebElement Day1;
    @FindBy(xpath = "//div[@role='group']/label[2]")
    public WebElement Day2;
    @FindBy(xpath = "//div[@role='group']/label[3]")
    public WebElement Day3;
    @FindBy(xpath = "//div[@role='group']/label[4]")
    public WebElement Day4;
    @FindBy(xpath = "//div[@role='group']/label[5]")
    public WebElement Day5;
    @FindBy(xpath = "//div[@role='group']/label[6]")
    public WebElement Day6;
    @FindBy(xpath = "//div[@role='group']/label[7]")
    public WebElement Day7;
    /**
     * Добавить Расписание оборудования - Оборудование - Добавить
     */
    @FindBy(xpath = "(//button//span[contains(.,'Добавить')])[3]")
    public WebElement Add;
    /**
     * Добавить Расписание оборудования - Оборудование - Уведомление о пересечении расписания оборудования
     */
    public By Alert = By.xpath("//div[@role='alert']");
}
