package UI.TmTest.PageObject.Administration;

import UI.TmTest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AcceessRoles extends BaseTest {

    public AcceessRoles(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Метод для очистки Cooke
     */
    public void Cooke() {
        /** Очищаем Cookie */
        Cookie cookie = driver.manage().getCookieNamed(".AspNetCore.Session");
        Cookie cookie1 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC1");
        Cookie cookie2 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC2");
        Cookie cookie3 = driver.manage().getCookieNamed(".AspNet.Core.Telemed");
        driver.manage().deleteCookie(cookie);
        driver.manage().deleteCookie(cookie1);
        driver.manage().deleteCookie(cookie2);
        driver.manage().deleteCookie(cookie3);
    }

    /**
     * Метод для Перехода в роли доступа и открытие редактирование для роли "Тестовая роль 213"
     */
    public void OpenRole() throws InterruptedException {
        AcceessRoles acceessRoles = new AcceessRoles(driver);
        WaitElement(acceessRoles.RolesWait);
        Thread.sleep(1000);
        actionElementAndClick(acceessRoles.Roles);
        WaitElement(acceessRoles.HeaderRoles);
        if (KingNumber == 1) {
            while (!isElementNotVisible(acceessRoles.RoleTest213)) {
                acceessRoles.Next.click();
            }
            actionElementAndClick(acceessRoles.EditTest);
        }
        if (KingNumber == 2) {
            while (!isElementNotVisible(acceessRoles.RoleTest1)) {
                acceessRoles.Next.click();
            }
            actionElementAndClick(acceessRoles.EditTest1);
        }
        if (KingNumber == 4) {
            while (!isElementNotVisible(acceessRoles.RoleTest999)) {
                acceessRoles.Next.click();
            }
            actionElementAndClick(acceessRoles.EditTest999);
        }
        WaitElement(acceessRoles.EditRole);
        WaitElement(acceessRoles.InputWordWait);
    }

    /**
     * Администрирование - Роли доступа
     */
    @FindBy(xpath = "//a[@href='/permission/roles']")
    public WebElement Roles;
    public By RolesWait = By.linkText("Роли доступа");
    public By RolesWait1 = By.xpath("//a[@href='/permission/roles']");

    /**
     * Заголовок Роли доступа
     */
    public By HeaderRoles = By.xpath("//h1[.='Роли доступа']");

    /**
     * Роли доступа - Добавить роль
     */
    public By AddRoles = By.xpath("//button/span[contains(.,'Добавить роль')]");

    /**
     * Роли доступа - Согласие на удаление
     */
    public By YesDelete = By.xpath(
            "//div[@role='tooltip'][@x-placement]/p[contains(.,'Удалить данный пункт?')]/following-sibling::div/button[1]");

    /**
     * Роль - Администратор
     */
    public By RoleAdministrator = By.xpath("(//table//tbody//tr//span[contains(.,'Администратор')])[1]");

    /**
     * Роль - Полный доступ
     */
    public By RolePolny = By.xpath("(//table//tbody//tr//span[contains(.,'Полный доступ')])[1]");
    /**
     * Роль - Тестовая роль 213
     */
    public By RoleTest213 = By.xpath("(//table//tbody//tr//span[contains(.,'Тестовая роль 213')])[1]");
    /**
     * Роль - Тестовая роль 2
     */
    public By RoleTest2 = By.xpath("(//table//tbody//tr//span[contains(.,'Tестовая роль 2')])[1]");
    /**
     * Роль - Тестовая роль
     */
    public By RoleTest1 = By.xpath("(//table//tbody//tr//span[contains(.,'Тестовая роль')])[1]");
    /**
     * Роль - Тестовая 999
     */
    public By RoleTest999 = By.xpath("//table//tbody//tr//span[contains(.,'Тестовая 999')]");
    /**
     * Роль - Тестовая роль 213 - Редактировать
     */
    @FindBy(xpath = "(//table//tbody//td[contains(.,'Тестовая роль 213')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement EditTest;
    /**
     * Роль - Тестовая роль 2 - Редактировать
     */
    @FindBy(xpath = "(//table//tbody//td[contains(.,'Tестовая роль 2')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement EditTest2;
    /**
     * Роль - Тестовая роль - Редактировать
     */
    @FindBy(xpath = "(//table//tbody//td[contains(.,'Тестовая роль')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement EditTest1;
    /**
     * Роль - Тестовая роль 999 - Редактировать
     */
    @FindBy(xpath = "//table//tbody//td[contains(.,'Тестовая 999')]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement EditTest999;
    /**
     * Следующая страница
     */
    @FindBy(xpath = "//button[@class='btn-next']")
    public WebElement Next;
    /**
     * Роль - Администратор - Редактировать
     */
    @FindBy(xpath = "(//table//tbody//td[contains(.,'Администратор')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement Edit;
    /**
     * Роль - Полный доступ - Редактировать
     */
    @FindBy(xpath = "(//table//tbody//td[contains(.,'Полный доступ')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement EditPolny;
    /**
     * Заголовок редактирование роли
     */
    public By EditRole = By.xpath("//h3['Редактирование роли']");

    /**
     * Редактировать - Наименование роли
     */
    public By NameRole = By.xpath("//label[contains(.,'Наименование роли')]");

    /**
     * Роль - Наименование роли
     */
    public By NameRoleText = By.xpath("//input[@placeholder='Введите наименование роли']");

    /**
     * Роль - Наименование роли
     */
    public By DescriptionRoleText = By.xpath("//textarea[@placeholder='Введите описание роли']");

    /**
     * Введите текст
     */
    @FindBy(xpath = "//input[@placeholder='Введите текст']")
    public WebElement InputWord;
    public By InputWordWait = By.xpath("//input[@placeholder='Введите текст']");

    /**
     * 1. Доступ к разделу "Нозологические регистры"
     */
    public By NosologicalRegisters = By.xpath(
            "//span[contains(.,'1. Доступ к разделу \"Нозологические регистры\"')]/preceding-sibling::label/span");

    /**
     * 1.1 Доступ к отдельным нозологическим группам
     */
    public By NosologicalRegisters1_1 = By.xpath(
            "//span[contains(.,'1.1. Доступ к отдельным нозологическим группам')]/preceding-sibling::label/span");

    /**
     * 15. Доступ к разделу "Типы регистров"
     */
    @FindBy(xpath = "//section//div[@class='el-tree-node is-expanded is-focusable']//span[contains(.,'15. Доступ к разделу \"Типы регистров\"')]")
    public WebElement TypeRegisters;
    public By TypeRegistersWait = By.xpath(
            "//section//div[@class='el-tree-node is-expanded is-focusable']//span[contains(.,'15. Доступ к разделу \"Типы регистров\"')]");
    /**
     * 16. Процедурный кабинет
     */
    @FindBy(xpath = "//section//div[@class='el-tree-node is-expanded is-focusable']//span[contains(.,'16. Процедурный кабинет')]")
    public WebElement ProcedureRoom;
    /**
     * Доступ - 11.7. Доступ к аналитической отчетности из системы BI
     */
    public By AnalysisBi = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'11.7. Доступ к аналитической отчетности из системы BI')]");
    /**
     * Чек бокс доступа - 11.7. Доступ к аналитической отчетности из системы BI
     */
    @FindBy(xpath = "//section//div[@class='el-tree-node__content']//span[contains(.,'11.7. Доступ к аналитической отчетности из системы BI')]/preceding-sibling::label/span")
    public WebElement CheckBox;
    /**
     * Чек бокс доступа - 11.7. Доступ к аналитической отчетности из системы BI не нажат
     */
    public By CheckBoxFalse = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'11.7. Доступ к аналитической отчетности из системы BI')]/preceding-sibling::label[@class='el-checkbox']");
    /**
     * Чек бокс доступа активный - 11.7. Доступ к аналитической отчетности из системы BI
     */
    public By CheckBoxActive = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'11.7. Доступ к аналитической отчетности из системы BI')]/preceding-sibling::label[@class='el-checkbox is-checked']");
    /**
     * Доступ - 6. Доступ к разделу "Расписание консультаций
     */
    public By Schedule = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'6. Доступ к разделу \"Расписание консультаций\"')]");
    /**
     * Чек бокс доступа - 6. Доступ к разделу "Расписание консультаций"
     */
    @FindBy(xpath = "//section//div[@class='el-tree-node__content']//span[contains(.,'6. Доступ к разделу \"Расписание консультаций\"')]/preceding-sibling::label/span")
    public WebElement CheckBoxSchedule;
    /**
     * Чек бокс доступа - 6. Доступ к разделу "Расписание консультаций" не нажат
     */
    public By CheckBoxScheduleFalse = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'6. Доступ к разделу \"Расписание консультаций\"')]/preceding-sibling::label[@class='el-checkbox']");
    /**
     * Чек бокс доступа активный - 6. Доступ к разделу "Расписание консультаций
     */
    public By CheckBoxScheduleActive = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'6. Доступ к разделу \"Расписание консультаций\"')]/preceding-sibling::label[@class='el-checkbox is-checked']");
    /**
     * Доступ - 17. Доступ к разделу "Расписание госпитализаций"
     */
    public By ScheduleHospital = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'17. Доступ к разделу \"Расписание госпитализаций\"')]");
    /**
     * Чек бокс доступа - 17. Доступ к разделу "Расписание госпитализаций"
     */
    @FindBy(xpath = "//section//div[@class='el-tree-node__content']//span[contains(.,'17. Доступ к разделу \"Расписание госпитализаций\"')]/preceding-sibling::label/span")
    public WebElement CheckBoxHospital;
    /**
     * Чек бокс доступа - 17. Доступ к разделу "Расписание госпитализаций" не нажат
     */
    public By CheckBoxHospitalFalse = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'17. Доступ к разделу \"Расписание госпитализаций\"')]/preceding-sibling::label[@class='el-checkbox']");
    /**
     * Чек бокс доступа активный - 17. Доступ к разделу "Расписание госпитализаций"
     */
    public By CheckBoxHospitalActive = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'17. Доступ к разделу \"Расписание госпитализаций\"')]/preceding-sibling::label[@class='el-checkbox is-checked']");
    /**
     * Чек бокс доступа не активный - 19.3. Доступ к структурированным медицинским сведениям по своей МО
     */
    public By CheckBoxAccessMyMO = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'19.3. Доступ к структурированным медицинским сведениям по своей МО')]/preceding-sibling::label[@class='el-checkbox']");
    /**
     * Чек бокс доступа активный - 19.3. Доступ к структурированным медицинским сведениям по своей МО
     */
    public By CheckBoxAccessMyMOActive = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'19.3. Доступ к структурированным медицинским сведениям по своей МО')]/preceding-sibling::label[@class='el-checkbox is-checked']");
    /**
     * Чек бокс доступа не активный - 19.2. Доступ к структурированным медицинским сведениям по всем МО
     */
    public By CheckBoxAccessMO = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'19.2. Доступ к структурированным медицинским сведениям по всем МО')]/preceding-sibling::label[@class='el-checkbox']");
    /**
     * Чек бокс доступа активный - 19.2. Доступ к структурированным медицинским сведениям по всем МО
     */
    public By CheckBoxAccessMOActive = By.xpath(
            "//section//div[@class='el-tree-node__content']//span[contains(.,'19.2. Доступ к структурированным медицинским сведениям по всем МО')]/preceding-sibling::label[@class='el-checkbox is-checked']");

    /**
     * Обновить
     */
    @FindBy(xpath = "//button/span[contains(.,'Обновить')]")
    public WebElement Update;
    public By UpdateWait = By.xpath("//button/span[contains(.,'Обновить')]");

    /**
     * Добавить
     */
    public By Add = By.xpath("(//button/span[contains(.,'Добавить')])[2]");

    /**
     * Закрыть
     */
    @FindBy(xpath = "//button/span[contains(.,'Закрыть')]")
    public WebElement Close;
}
