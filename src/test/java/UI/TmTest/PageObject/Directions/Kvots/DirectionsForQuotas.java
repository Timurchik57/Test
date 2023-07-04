package UI.TmTest.PageObject.Directions.Kvots;

import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.EquipmentSchedule;
import api.A.Authorization;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class DirectionsForQuotas extends BaseTest {
    /**
     * Направления на квоты / Исходящие / Незавершенные
     */
    public DirectionsForQuotas(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    AuthorizationObject authorizationObject;
    EquipmentSchedule equipmentSchedule;
    DirectionsForQuotas directionsForQuotas;
    public int CountAsk;

    /**
     * Метод создания удалённой консультации
     * MyMO - true = в МО, по которой авторизовались, false = в другую МО
     * Doctor - Врач назначающий консультацию
     * ClickDirection - Условие надо ли перед созданием коснультации заходить в "Добавить консультацию"
     */
    public void CreateConsul(Boolean MyMO, By Doctor, Boolean ClickDirection) throws InterruptedException {
        directionsForQuotas = new DirectionsForQuotas(driver);
        authorizationObject = new AuthorizationObject(driver);
        /** Условие надо ли перед созданием коснультации заходить в "Добавить консультацию" **/
        if (ClickDirection) {
            /** Переход в создание консультации на оборудование */
            ClickElement(directionsForQuotas.ConsultationWait);
            /** Создание консультации */
            System.out.println("Создание консультации");
            WaitElement(directionsForQuotas.Heading);
            WaitElement(directionsForQuotas.CreateWait);
            directionsForQuotas.Create.click();
            WaitElement(directionsForQuotas.TypeConsultationWait);
            directionsForQuotas.DistrictDiagnostic.click();
            WaitElement(directionsForQuotas.DistrictDiagnosticWait);
            directionsForQuotas.Next.click();
        }
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);
        System.out.println("Заполняем данные");
        SelectClickMethod(directionsForQuotas.DoctorWait, Doctor);
        if (!MyMO) {
            if (KingNumber == 1 | KingNumber == 2) {
                SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMO);
            }
            if (KingNumber == 4) {
                SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMOKon);
            }
        } else {
            ClickElement(directionsForQuotas.MyMO);
            SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);

        }
        SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.SelectFirst);
        inputWord(directionsForQuotas.Diagnos, "AA");
        Thread.sleep(1000);
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.Plan);
        SelectClickMethod(directionsForQuotas.Goal, directionsForQuotas.SelectCovid);
        ClickElement(directionsForQuotas.NextConsul);
        ClickElement(directionsForQuotas.CreateConsul);
        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.AddFilesWait);
        Thread.sleep(1000);
        File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(500);
        ClickElement(directionsForQuotas.SendConsul);
    }

    /**
     * Метод создания консультации на оборудование и заполнении информации о направившем враче
     * Mass - вес пациента
     * MyMO - true = в МО, по которой авторизовались, false = в другую МО
     * Research - выбор исследования
     * ClickDirection - Условие надо ли перед созданием коснультации заходить в "Добавить консультацию"
     **/
    public void CreateConsultationMethod(
            String Mass, Boolean MyMO, By Research, Boolean ClickDirection
    ) throws InterruptedException, SQLException, IOException {
        this.authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        Authorization authorization = new Authorization();
        /** Условие надо ли перед созданием коснультации заходить в "Добавить консультацию" **/
        if (ClickDirection) {
            /** Переход в создание консультации на оборудование */
            ClickElement(directionsForQuotas.ConsultationWait);
            /** Создание консультации */
            System.out.println("Создание консультации");
            WaitElement(directionsForQuotas.Heading);
            WaitElement(directionsForQuotas.CreateWait);
            directionsForQuotas.Create.click();
            WaitElement(directionsForQuotas.TypeConsultationWait);
            directionsForQuotas.DistrictDiagnostic.click();
            WaitElement(directionsForQuotas.DistrictDiagnosticWait);
            directionsForQuotas.Next.click();
        }
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);
        /** Заполнение информации о направившем враче */
        System.out.println("Заполнение информации о направившем враче");
        WaitElement(directionsForQuotas.InfoDoctorWait);
        WaitElement(directionsForQuotas.SubmittingDoctorWait);
        Thread.sleep(2000);
        SelectClickMethod(directionsForQuotas.SubmittingDoctorWait, directionsForQuotas.FIO);
        Thread.sleep(1500);
        SelectClickMethod(directionsForQuotas.Division, directionsForQuotas.SelectDivision);
        Thread.sleep(3000);
        SelectClickMethod(directionsForQuotas.DepartmentWait, directionsForQuotas.SelectDepartment);
        Thread.sleep(2000);
        SelectClickMethod(directionsForQuotas.Post, directionsForQuotas.SelectPost);
        Thread.sleep(1500);
        SelectClickMethod(directionsForQuotas.Specialization, directionsForQuotas.SelectSpecializationFirst);
        Thread.sleep(1500);
        SelectClickMethod(directionsForQuotas.AnatomicalAreas, directionsForQuotas.SelectAnatomicalAreas);
        if (MyMO) {
            ClickElement(directionsForQuotas.MyMODirection);
            Thread.sleep(2000);
            SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);
        }
        if (!MyMO) {
            if (KingNumber == 1 | KingNumber == 2) {
                SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirection);
            }
            if (KingNumber == 4) {
                SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirectionKon);
            }
        }
        directionsForQuotas.Diagnosis.sendKeys(Keys.SPACE);
        WaitElement(this.authorizationObject.BottomStart);
        Thread.sleep(1000);
        ClickElement(directionsForQuotas.SelectDiagnosisWait);
        SelectClickMethod(directionsForQuotas.Research, Research);
        SelectClickMethod(directionsForQuotas.BenefitCode, directionsForQuotas.SelectBenefitCode);
        WaitElement(directionsForQuotas.MassWait);
        inputWord(directionsForQuotas.Mass, Mass);
        System.out.println("Вес пациента " + Mass);
        inputWord(directionsForQuotas.NamePatient, "Тестовыйй");
        inputWord(directionsForQuotas.LastNamePatient, "Тестт");
        inputWord(directionsForQuotas.MiddleNamePatient, "Тестовичч");
        actionElementAndClick(directionsForQuotas.NextPatient);
        /** Окно направления на диагностику*/
        WaitElement(directionsForQuotas.Header);
        WaitElement(directionsForQuotas.CreateDirectionWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.CreateDirection);
        /** Прикрепление файла */
        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.FileWait);
        WaitElement(directionsForQuotas.AddFileWait);
        Thread.sleep(1000);
        java.io.File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(500);
        WaitElement(directionsForQuotas.ReceptionWait);
        directionsForQuotas.Reception.click();
        /** Запись на приём */
        System.out.println("Запись на приём");
        WaitElement(equipmentSchedule.HeaderEquipmentSchedulesWait);
        WaitElement(equipmentSchedule.NextPageWait);
        Thread.sleep(3000);
        if (!isElementNotVisible(equipmentSchedule.SlotsTrue)) {
            ClickElement(equipmentSchedule.NextPageWait);
            Thread.sleep(500);
        }
        while (!isElementNotVisible(equipmentSchedule.SlotsFreeWait)) {
            ClickElement(equipmentSchedule.NextPageWait);
            Thread.sleep(500);
            /* Условие нужно потому что, такое может быть при отсутствии расписания оборудования. Добавляем расписание*/
            if (!isElementNotVisible(equipmentSchedule.SlotsTrue)) {
                System.out.println("Добавляем расписание с завтрашнего дня");
                AuthorizationMethod(this.authorizationObject.YATCKIV);
                equipmentSchedule.AddEquipmentSchedule(
                        equipmentSchedule.SelectEquipmentChoice, equipmentSchedule.Tomorrow,
                        equipmentSchedule.NextMouth
                );
                if (isElementNotVisible(By.xpath("//div[@role='alert']//h2[contains(.,'Успешно')]"))) {
                    System.out.println("Новое расписание добавлено");
                } else {
                    System.out.println("Проверка уведомления о пересечении расписания");
                }
                AuthorizationMethod(this.authorizationObject.OKB);
                ClickElement(directionsForQuotas.ConsultationWait);
                ClickElement(directionsForQuotas.FirstLine);
                ClickElement(directionsForQuotas.AddReception);
                Thread.sleep(1000);
            }
        }
        ClickElement(equipmentSchedule.SlotsFreeWait);
        Thread.sleep(1500);
        System.out.println("Выбрали свободный слот");
    }

    /**
     * Метод на заполнение информации о направившем враче
     **/
    public void DoctorMethod() throws InterruptedException {
        directionsForQuotas = new DirectionsForQuotas(driver);
        authorizationObject = new AuthorizationObject(driver);
        /** Заполнение информации о направившем враче */
        System.out.println("Заполнение информации о направившем враче");
        WaitElement(directionsForQuotas.InfoDoctorWait);
        WaitElement(directionsForQuotas.SubmittingDoctorWait);
        Thread.sleep(2000);
        SelectClickMethod(
                directionsForQuotas.SubmittingDoctorWait,
                directionsForQuotas.FIO
        );
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        SelectClickMethod(
                directionsForQuotas.Division,
                directionsForQuotas.SelectDivision
        );
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        SelectClickMethod(
                directionsForQuotas.DepartmentWait,
                directionsForQuotas.SelectDepartment
        );
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        SelectClickMethod(
                directionsForQuotas.Post,
                directionsForQuotas.SelectPost
        );
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        SelectClickMethod(
                directionsForQuotas.Specialization,
                directionsForQuotas.SelectSpecializationFirst
        );
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        SelectClickMethod(directionsForQuotas.AnatomicalAreas, directionsForQuotas.SelectAnatomicalAreas);
        if (KingNumber == 4) {
            if (KingNumber == 4) {
                Thread.sleep(2500);
            }
            ClickElement(directionsForQuotas.MyMODirection);
            Thread.sleep(2000);
            SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);
        } else {
            SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirection);
        }
        directionsForQuotas.Diagnosis.sendKeys(Keys.SPACE);
        WaitElement(authorizationObject.BottomStart);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.SelectDiagnosis);
        if (KingNumber == 4) {
            if (KingNumber == 4) {
                Thread.sleep(1500);
            }
            SelectClickMethod(directionsForQuotas.Research, authorizationObject.SelectFirst);
        } else {
            SelectClickMethod(directionsForQuotas.Research, directionsForQuotas.SelectResearch);
        }
        SelectClickMethod(directionsForQuotas.BenefitCode, directionsForQuotas.SelectBenefitCode);
        WaitElement(directionsForQuotas.MassWait);
        inputWord(directionsForQuotas.Mass, "500");
        inputWord(directionsForQuotas.NamePatient, "Тестовыйй");
        inputWord(directionsForQuotas.LastNamePatient, "Тестт");
        inputWord(directionsForQuotas.MiddleNamePatient, "Тестовичч");
        actionElementAndClick(directionsForQuotas.NextPatient);
        /** Окно направления на диагностику*/
        WaitElement(directionsForQuotas.Header);
        WaitElement(directionsForQuotas.CreateDirectionWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.CreateDirection);
        /** Прикрепление файла */
        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.FileWait);
        WaitElement(directionsForQuotas.AddFileWait);
        ClickElement(directionsForQuotas.Close);
    }

    /**
     * Метод создания пациента в Консультации
     * Snils - Снилс, который используем при поиске пациента
     **/
    public void CreatePatientMethod(String Snils) throws InterruptedException {
        directionsForQuotas = new DirectionsForQuotas(driver);
        authorizationObject = new AuthorizationObject(driver);
        Faker faker = new Faker();
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys(Snils);
        inputWord(directionsForQuotas.LastName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        inputWord(directionsForQuotas.Name, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        inputWord(directionsForQuotas.MiddleName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        System.out.println("Заполняем данные о пациенте для консультации");
        SelectClickMethod(DateWait, SelectDate);
        ClickElement(directionsForQuotas.ManWait);
        SelectClickMethod(directionsForQuotas.TypeDocument, directionsForQuotas.SelectTypeDocument);
        inputWord(directionsForQuotas.Serial, "12344");
        inputWord(directionsForQuotas.Number, "1234566");
        actionElementAndClick(directionsForQuotas.Address);
        inputWord(directionsForQuotas.Address, "г Москва, ул Арбат, д 9АФ");
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.AddressHome);
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.NextPatient);
    }

    /**
     * Метод только для создания консультации на оборудование
     **/
    public void CreateConsultationEquipmentMethod() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        /** Переход в создание консультации на оборудование */
        WaitElement(By.linkText("Незавершенные"));
        actionElementAndClick(directionsForQuotas.Consultation);
        /** Создание консультации */
        System.out.println("Создание консультации");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.CreateWait);
        directionsForQuotas.Create.click();
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.DistrictDiagnostic.click();
        WaitElement(directionsForQuotas.DistrictDiagnosticWait);
        directionsForQuotas.Next.click();
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);
    }

    /**
     * Метод определения нужного запроса из списка Network
     * message - запрос JavaScript
     * word - искомый запрос из Network
     **/
    public int searchWord(String message, String word) {
        message = message.toLowerCase();
        word = word.toLowerCase();
        int i = message.indexOf(word);
        int count = 0;
        while (i >= 0) {
            count++;
            i = message.indexOf(word, i + 1);
        }
        CountAsk = count;
        return CountAsk;
    }

    /**
     * Метод для проверки размера шрифта
     * locator - локатор на веб странице
     **/
    public void AssertFontSizeMethod(By locator) {
        wait.until(visibilityOfElementLocated(locator));
        String element = driver.findElement(locator).getCssValue("font-size");
        Assertions.assertEquals("14px", element);
        System.out.println("---Значение на интерфейсе " + element + " совпадает с 14px");
    }

    /**
     * Незавершенные
     */
    public By Unfinished = By.linkText("Незавершенные");
    /**
     * Направления на квоты / Исходящие / Незавершенные
     */
    @FindBy(xpath = "//a[@href='/direction/requestDiagnostic/uncompleted?directionType=1&directionTarget=1&directionPageType=1']")
    public WebElement Consultation;
    public By ConsultationWait = By.xpath(
            "//a[@href='/direction/requestDiagnostic/uncompleted?directionType=1&directionTarget=1&directionPageType=1']");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Заголовок
     */
    public By Heading = By.xpath("//h1[.='Направления на квоты / Исходящие / Незавершенные']");
    /**
     * Тип даты
     */
    public By Type = By.xpath("//div[@class='form-item-label'][contains(.,'Тип даты')]");
    /**
     * Период
     */
    public By Period = By.xpath("//div[@class='form-item-label'][contains(.,'Период')]");
    /**
     * Мед.организация
     */
    public By MOTwo = By.xpath("//div[@class='form-item-label'][contains(.,'Мед.организация')]");
    /**
     * ФИО пациента
     */
    public By FOI = By.xpath("//div[@class='form-item-label'][contains(.,'ФИО пациента')]");
    /**
     * Снилс пациента
     */
    public By SnilsTwo = By.xpath("//div[@class='form-item-label'][contains(.,'Снилс пациента')]");
    /**
     * Дистанция
     */
    public By DistanceType = By.xpath("(//div[@class='el-col el-col-4'])[1]");
    /**
     * Дистанция
     */
    public By DistancePeriod = By.xpath("(//div[@class='el-col el-col-4'])[2]");
    /**
     * Дистанция
     */
    public By DistanceMO = By.xpath("(//div[@class='el-col el-col-5'])[1]");
    /**
     * Дистанция
     */
    public By DistanceFOI = By.xpath("(//div[@class='el-col el-col-5'])[2]");
    /**
     * Дистанция
     */
    public By DistanceSnils = By.xpath("//div[@class='el-col el-col-6']");
    /**
     * Дистанция
     */
    public By Distance = By.xpath("//div[@class='el-col el-col-3']");
    /**
     * Поиск
     */
    public By SearchTwo = By.xpath("//button[@type='submit'][contains(.,'Поиск')]");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Снилс
     */
    @FindBy(xpath = "//div[text()='Снилс пациента']/following-sibling::div/input")
    public WebElement InputSnils;
    /**
     * Направления на квоты / Исходящие / Незавершенные - Поиск
     */
    @FindBy(xpath = "//button[@type='submit']")
    public WebElement Search;
    /**
     * Направления на квоты / Исходящие / Незавершенные - Записать на приём
     */
    public By AddReception = By.xpath("//button/span[contains(.,'Записать на приём')]");
    /**
     * Направления на квоты / Исходящие / Незавершенные - 1 строка в таблице
     */
    public By FirstLine = By.xpath("//tbody/tr[1]");
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
     * Направления на квоты / Исходящие / Незавершенные - Последняя строка в таблице
     */
    @FindBy(xpath = "//tbody/tr[last()]")
    public WebElement lastLine;
    /**
     * Направления на квоты / Исходящие / Незавершенные - столбец со снилс
     */
    @FindBy(xpath = "//tbody/tr[last()]/td[8]")
    public WebElement TableSnils;
    public By TableSnilsWait = By.xpath("//tbody/tr[last()]/td[8]");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Создать направление
     */
    @FindBy(xpath = "//button//span[contains(.,'Создать направление')]")
    public WebElement Create;
    public By CreateWait = By.xpath("//button//span[contains(.,'Создать направление')]");
    /**
     * Создать направление - Тип консультации
     */
    public By TypeConsultationWait = By.xpath("//h3[.='Выберите текущий тип консультации ?']");
    /**
     * Создать направление - Направление на диагностику
     */
    @FindBy(xpath = "//label//span[contains(.,'Направление на диагностику')]")
    public WebElement DistrictDiagnostic;
    public By DistrictDiagnosticWait = By.xpath("//div//span[contains(.,'Направление на диагностику')]");
    /**
     * Создать направление - Удаленная консультация
     */
    @FindBy(xpath = "//label//span[contains(.,'Удаленная консультация')]")
    public WebElement RemoteConsultation;
    public By RemoteConsultationWait = By.xpath("//label//span[contains(.,'Удаленная консультация')]");
    /**
     * Далее
     */
    @FindBy(xpath = "//button//span[contains(.,'Далее')]")
    public WebElement Next;
    public By NextWait = By.xpath(
            "//button[@class='el-button el-button--primary el-button--medium']//span[contains(.,'Далее')]");
    /**
     * Введите СНИЛС
     */
    @FindBy(xpath = "//section[@class='patient-form']//input[@placeholder='Введите СНИЛС']")
    public WebElement Snils;
    public By SnilsWait = By.xpath("//section[@class='patient-form']//input[@placeholder='Введите СНИЛС']");
    /**
     * Направления на квоты / Исходящие / Незавершенные - Выбранное направление - Сформировать направление
     */
    public By CreateDirectionLow = By.xpath("//button//span[contains(.,'Сформировать направление')]");
    /**
     * Выбранное направление - Сформировать направление - Сформированное направление
     */
    public By CreateDirectionAfter = By.xpath("//td[contains(.,'Направление_Тестировщик_Т.Т..docx')]");
    /**
     * Выбранное направление - Сформировать направление - Сформированное направление - Скачать
     */
    public By DownloadCreateDirection = By.xpath(
            "//td[contains(.,'Направление_Тестировщик_Т.Т..docx')]/following-sibling::td[3]/a");
    /**
     * Выбранное направление - Ошибка
     */
    public By AlertError = By.xpath("//div[@role='alert'][contains(.,'Ошибка')]");
    /**
     * Выбранное направление - Сформировать направление - Сформированное направление - Скачать архивом
     */
    public By DownloadArhiv = By.xpath("(//a[contains(.,'Скачать архивом')])[1]");
    /**
     * Выбранное направление - Сформировать направление - Сформированное протокол
     */
    public By CreateProtocol = By.xpath("//button//span[contains(.,'Сформировать протокол')]");
    /**
     * ----------------- Направление на диагностику - Общие данные о пациенте-------------
     */
    public By PatientDataWait = By.xpath(
            "//div[@class='el-divider el-divider--horizontal']//div[contains(.,'Общие данные о пациенте')]");
    /**
     * Направление на диагностику - Информация о направившем враче
     */
    public By InfoDoctorWait = By.xpath(
            "(//form[@class='el-form']//div[contains(.,'Информация о направившем враче')])[1]");
    /**
     * Направление на диагностику - Направивший врач
     */
    public By SubmittingDoctorWait = By.xpath("(//div/input[@placeholder='Введите значение для поиска'])[1]");
    /**
     * Направление на диагностику - ФИО
     */
    public By FIO = By.xpath("(//div[@x-placement]//li/span)[1]");
    /**
     * Направление на диагностику - ожидание загрузки
     */
    public By loading = By.xpath(
            "//div[@class='el-select el-select--medium el-loading-parent--relative']//div[@class='el-loading-spinner']");
    /**
     * Направление на диагностику - Подразделение
     */
    public By Division = By.xpath("(//input[@placeholder='Введите значение для поиска'])[2]");
    public By SelectDivision = By.xpath("//div[@x-placement]//li/span[contains(.,'Женская консультация')]");
    /**
     * Направление на диагностику - Отделение
     */
    @FindBy(xpath = "(//input[@placeholder='Введите значение для поиска'])[3]")
    public WebElement Department;
    public By DepartmentWait = By.xpath("(//input[@placeholder='Введите значение для поиска'])[3]");
    public By SelectDepartment = By.xpath("//div[@x-placement]//li/span[contains(.,'Акушерско-гинекологические')]");
    /**
     * Направление на диагностику - Должность
     */
    public By Post = By.xpath("(//input[@placeholder='Введите значение для поиска'])[4]");
    public By SelectPost = By.xpath(
            "//div[@x-placement]//li/span[contains(.,'главный врач медицинской организации ')]");
    /**
     * Направление на диагностику - Специальность
     */
    public By Specialization = By.xpath("(//input[@placeholder='Введите значение для поиска'])[5]");
    public By SelectSpecialization = By.xpath(
            "//div[@x-placement]//li/span[contains(.,'Общественное здравоохранение')]");
    public By SelectSpecializationFirst = By.xpath("//div[@x-placement]//li[1]");
    public By SelectSpecializationBottom = By.xpath("//div[@x-placement]//ul/li");
    /**
     * Направление на диагностику - Анатомические области
     */
    public By AnatomicalAreas = By.xpath(
            "(//label[contains(.,'Анатомические области')]/following-sibling::div//input)[1]");
    public By SelectAnatomicalAreas = By.xpath("//div[@x-placement]//li/span[contains(.,'Головной мозг')]");
    @FindBy(xpath = "//div[@x-placement='top-start']//li/span[contains(.,'Головной мозг')]")
    public WebElement SelectAnatomicalAreasTop;
    /**
     * Направление на диагностику - Мо, куда направлен
     */
    public By MODirection = By.xpath("(//input[@placeholder='Введите значение для поиска'])[6]");
    public By SelectMODirection = By.xpath(
            "//div[@x-placement]//li/span[contains(.,'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"')]");
    public By SelectMODirectionKon = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'Кондинская районная стоматологическая поликлиника')]");
    /**
     * Направление на диагностику - Мо, куда направлен - Мо через которую авторизованы
     */
    public By MyMODirection = By.xpath("//label[contains(.,'МО, куда направлен')]/following-sibling::div//button");
    /**
     * Направление на диагностику - Подразделение при Мо через которую авторизованы
     */
    public By MyDivision = By.xpath(
            "//label[contains(.,'Подразделение')]/following-sibling::div//input[@placeholder='Укажите подразделение']");
    public By MySelectDivision = By.xpath("//div[@x-placement]//ul//li/span[text()='Женская консультация']");
    public By MySelectDivisionApteka = By.xpath("//div[@x-placement]//ul//li/span[text()='Аптека']");
    /**
     * Направление на диагностику - Диагноз
     */
    @FindBy(xpath = "//input[@placeholder='Введите значение или пробел для поиска']")
    public WebElement Diagnosis;
    @FindBy(xpath = "//ul/li/span[contains(.,'A01.0 Брюшной тиф')]")
    public WebElement SelectDiagnosis;
    public By SelectDiagnosisWait = By.xpath("//ul/li/span[contains(.,'A01.0 Брюшной тиф')]");
    /**
     * Направление на диагностику - Исследование
     */
    public By Research = By.xpath("//label[contains(.,'Исследование')]/following-sibling::div//input");
    public By SelectResearch = By.xpath("//div//li/span[contains(.,'HMP01')]");
    public By SelectResearch_HMP30 = By.xpath("(//div//li/span[contains(.,'HMP30')])[1]");
    public By SelectResearch_HMP04 = By.xpath("(//div//li/span[contains(.,'HMP04')])[1]");
    /**
     * Направление на диагностику - Код льготы
     */
    public By BenefitCode = By.xpath("(//input[@placeholder='Введите значение для поиска'])[8]");
    public By SelectBenefitCode = By.xpath("//div//li/span[contains(.,'010.Инвалиды войны')]");
    /**
     * Направление на диагностику - Вес
     */
    @FindBy(xpath = "//label[contains(.,'Вес пациента')]/following-sibling::div//input")
    public WebElement Mass;
    public By MassWait = By.xpath("//label[contains(.,'Вес пациента')]/following-sibling::div//input");
    /**
     * Направление на диагностику - Фамилия
     */
    @FindBy(xpath = "//input[@placeholder='Фамилия']")
    public WebElement NamePatient;
    /**
     * Направление на диагностику - Имя
     */
    @FindBy(xpath = "//input[@placeholder='Имя']")
    public WebElement LastNamePatient;
    /**
     * Направление на диагностику - Отчество
     */
    @FindBy(xpath = "//input[@placeholder='Отчество']")
    public WebElement MiddleNamePatient;
    /**
     * Направление на диагностику - Header
     */
    public By Header = By.xpath("//th[contains(.,'Направление на диагностику')]");
    /**
     * Направление на диагностику - Создать направление
     */
    @FindBy(xpath = "//footer/button/span[contains(.,'Создать направление')]")
    public WebElement CreateDirection;
    public By CreateDirectionWait = By.xpath("//footer/button/span[contains(.,'Создать направление')]");
    /**
     * Направление на диагностику - Прикрепление файла
     */
    @FindBy(xpath = "//input[@type='file']")
    public WebElement File;
    public By FileWait = By.xpath("//h3[contains(.,'Файлы')]");
    public By AddFileWait = By.xpath("//label[contains(.,'Добавить файлы')]");
    /**
     * Направление на диагностику - Прикрепление файла - Скачать
     */
    public By Download = By.xpath("//a/span[contains(.,'Скачать')]");
    /**
     * Направление на диагностику - Прикрепление файла - Закрыть
     */
    public By Close = By.xpath("(//footer/button[contains(.,'Закрыть')])[2]");
    /**
     * Направление на диагностику - Записать на прием
     */
    @FindBy(xpath = "//a[contains(.,'Записать на прием')]")
    public WebElement Reception;
    public By ReceptionWait = By.xpath("//a[contains(.,'Записать на прием')]");

    /**------------------- Создание пациента для консультации ------------------------ */

    /**
     * Расширенный поиск
     */
    @FindBy(xpath = "//button//span[contains(.,'Расширенный поиск')]")
    public WebElement BigSearch;
    public By BigSearchWait = By.xpath("//button//span[contains(.,'Расширенный поиск')]");
    /**
     * Сбросить форму
     */
    public By ResetForm = By.xpath("//button//span[contains(.,'Сбросить форму')]");
    @FindBy(xpath = "//button//span[contains(.,'Создать пациента')]")
    public WebElement CreatePatient;
    public By CreatePatientWait = By.xpath("//button//span[contains(.,'Создать пациента')]");
    /**
     * Создание пациента для консультации - направления на удаленную консультацию
     */
    public By ReferralsRemoteConsultation = By.xpath(
            "//div//span[contains(.,'направления на удаленную консультацию')]");
    /**
     * Создание пациента для консультации - направления на диагностику
     */
    public By RemoteConsultationForDiagnostics = By.xpath("//div//span[contains(.,'направления на диагностику')]");
    /**
     * Создание пациента для консультации - Введите снилс
     */
    @FindBy(xpath = "(//input[@placeholder='Введите СНИЛС'])[1]")
    public WebElement AddSnils;
    /**
     * Список пациентов - Пятый - Снилс
     */
    @FindBy(xpath = "((//tbody)[2]/tr)[5]/td[5]//span")
    public WebElement listPatientSnils;
    public By listPatientSnilsWait = By.xpath("((//tbody)[2]/tr)[5]/td[5]//span");
    /**
     * Список пациентов - Пятый - ЕНП
     */
    @FindBy(xpath = "((//tbody)[2]/tr)[5]/td[6]//span")
    public WebElement listPatientENP;
    /**
     * Список пациентов - Первый
     */
    public By listPatientFirst = By.xpath("(//tbody)[2]/tr[1]");
    /**
     * Список пациентов - Первый - Снилс
     */
    public By listPatientFirstnils = By.xpath("(//tbody)[2]/tr[1]/td[5]//span");
    /**
     * Список пациентов - Выбрать
     */
    public By Choose = By.xpath("//button/span[contains(.,'Выбрать')]");
    /**
     * Создание пациента для консультации - Пациент (ФИО)
     */
    public By PatientFIO = By.xpath("//label[contains(.,'Пациент (ФИО)')]");
    /**
     * Создание пациента для консультации - Фамилия
     */
    @FindBy(xpath = "(//input[@placeholder='Фамилия'])[1]")
    public WebElement LastName;
    public By LastNameWait = By.xpath("(//input[@placeholder='Фамилия'])[1]");
    /**
     * Создание пациента для консультации - Поиск
     */
    public By SearchWait = By.xpath("(//button[@type='submit'])[2]");
    /**
     * Расширенный поиск - количество найденных пациентов
     */
    public By PatientList = By.xpath("//tbody/tr[@class='el-table__row cursor-pointer']");
    /**
     * Расширенный поиск - Выбор количества найденных пациентов
     */
    public By PatientListNumber = By.xpath("(//div[@class='grid-table__pagination']//input[@readonly='readonly'])[2]");
    /**
     * Расширенный поиск - Выбор количества найденных пациентов - 20
     */
    public By PatientListNumber20 = By.xpath("//div[@x-placement]//li[contains(.,'20')]");
    /**
     * Расширенный поиск - Выбор количества найденных пациентов - 30
     */
    public By PatientListNumber30 = By.xpath("//div[@x-placement]//li[contains(.,'30')]");
    /**
     * Создание пациента для консультации - Имя
     */
    @FindBy(xpath = "(//input[@placeholder='Имя'])[1]")
    public WebElement Name;
    /**
     * Создание пациента для консультации - Отчество
     */
    @FindBy(xpath = "(//input[@placeholder='Отчество'])[1]")
    public WebElement MiddleName;

    /**
     * Создание пациента для консультации - Ошибка не более 40 символов
     */
    public By Error40 = By.xpath("//div[@class='el-form-item__error'][contains(.,'Не больше 40 символов')]");

    /**
     * Создание пациента для консультации - Укажите дату
     */
    @FindBy(xpath = "//input[@placeholder = 'Укажите дату']")
    public WebElement Date;
    public By DateWait = By.xpath("//input[@placeholder = 'Укажите дату']");
    public By SelectDate = By.xpath("(//div[@x-placement]//td[@class='available today'])[1]");
    /**
     * Создание пациента для консультации - Укажите дату - год
     */
    @FindBy(xpath = "(//span[@role='button'])[1]")
    public WebElement Year;
    public By YearWait = By.xpath("(//span[@role='button'])[1]");
    /**
     * Создание пациента для консультации - Укажите дату - Предыдущий год
     */
    @FindBy(xpath = "//button[@aria-label='Предыдущий год']")
    public WebElement BeforeYear;
    /**
     * Создание пациента для консультации - Укажите дату - Следующий год
     */
    @FindBy(xpath = "//button[@aria-label='Следующий год']")
    public WebElement AfterYear;
    /**
     * Создание пациента для консультации - Укажите дату - Первое число месяца
     */
    public By DayOne = By.xpath("(//table[@class='el-date-table']//tr/td[@class='available'])[1]");
    /**
     * Создание пациента для консультации - Укажите дату - Текущее число месяца
     */
    public By Today = By.xpath("(//table[@class='el-date-table']//tr/td//span[text()=7])[1]");
    /**
     * Создание пациента для консультации - Пол
     */
    @FindBy(xpath = "(//div//span[contains(.,'Мужской')])[1]")
    public WebElement Man;
    public By ManWait = By.xpath("(//div//span[contains(.,'Мужской')])[1]");
    /**
     * Создание пациента для консультации - Выбранный Пол - Мужской
     */
    public By ManWaitTrue = By.xpath("//label[@aria-checked='true']//span[contains(.,'Мужской')]");
    /**
     * Создание пациента для консультации - Гражданство
     */
    public By CitizenshipError = By.xpath(
            "//label[contains(.,'Гражданство')]/following-sibling::div[contains(.,'Поле обязателено для заполнения')]");
    /**
     * Создание пациента для консультации - Социальный статус
     */
    public By Status = By.xpath("(//label[contains(.,'Социальный статус')]/following-sibling::div//input)[1]");
    public By StatusError = By.xpath(
            "//label[contains(.,'Социальный статус')]/following-sibling::div[contains(.,'Поле обязателено для заполнения')]");
    public By StatusSelectT = By.xpath("//div[@x-placement]//ul/li/span");
    public By StatusSelectJob = By.xpath("//div[@x-placement='bottom-start']//ul/li/span[text()='Работающий']");
    /**
     * Создание пациента для консультации - Выбран Вахтовик - Нет
     */
    public By VahtFalse = By.xpath("//label[@aria-checked='true']//span[contains(.,'Нет')]");
    /**
     * Создание пациента для консультации - Выбран Вахтовик - Нет
     */
    public By VahtTrue = By.xpath("//label[@aria-checked='true']//span[contains(.,'Да')]");
    /**
     * Создание пациента для консультации - Выберите тип документа
     */
    public By TypeDocument = By.xpath("//input[@placeholder = 'Выберите тип документа']");
    public By SelectTypeDocument = By.xpath("//div//span[contains(.,'Паспорт гражданина РФ')]");
    /**
     * Создание пациента для консультации - Серия
     */
    @FindBy(xpath = "//input[@placeholder='Введите серию документа']")
    public WebElement Serial;
    /* Создание пациента для консультации - Серия - Ошибка */
    public By SerialError = By.xpath(
            "//input[@placeholder='Введите серию документа']/ancestor::div[1]/following-sibling::div");
    /**
     * Создание пациента для консультации - Номер
     */
    @FindBy(xpath = "//input[@placeholder='Введите номер документа']")
    public WebElement Number;
    /* Создание пациента для консультации - Номер - Ошибка */
    public By NumberError = By.xpath(
            "//input[@placeholder='Введите номер документа']/ancestor::div[1]/following-sibling::div");
    /**
     * Создание пациента для консультации - Введите адрес постоянной регистрации
     */
    @FindBy(xpath = "//input[@placeholder='Введите адрес постоянной регистрации']")
    public WebElement Address;
    /**
     * Создание пациента для консультации - Введите адрес проживания
     */
    @FindBy(xpath = "//input[@placeholder='Введите адрес проживания']")
    public WebElement AddressHome;
    /**
     * Создание пациента для консультации - Bottom1
     */
    @FindBy(xpath = "//div[@x-placement='bottom-start']//li[1]")
    public WebElement Bottom1Click;
    public By Bottom1 = By.xpath("//div[@x-placement='bottom-start']//li[1]");
    public By Select = By.xpath("//div[@x-placement]//li[1]");
    /**
     * Создание пациента для консультации - Информация о работе
     */
    public By Jobinfo = By.xpath("//div[@role='button'][contains(.,'Информация о работе')]");
    @FindBy(xpath = "//input[@placeholder='Введите информацию о месте работы']")
    public WebElement Job;
    @FindBy(xpath = "//input[@placeholder='Введите должность']")
    public WebElement PostJob;
    public By JobError = By.xpath(
            "//label[contains(.,'Место работы')]/following-sibling::div[contains(.,'Поле обязателено для заполнения')]");
    public By JobErrorSimvol = By.xpath("//div[@class='el-form-item__error'][contains(.,'Не больше 100 символов')]");
    public By JobPost = By.xpath(
            "//label[contains(.,'Должность')]/following-sibling::div[contains(.,'Поле обязателено для заполнения')]");
    /**
     * Создание пациента для консультации - Данные о страховом полисе
     */
    public By Polis = By.xpath("//div[@role='button'][contains(.,'Данные о страховом полисе')]");
    /**
     * Создание пациента для консультации - Данные о страховом полисе - Тип полиса / ЕНП(обязательное)
     */
    public By PolisTypeError = By.xpath(
            "//label[contains(.,'Тип полиса / ЕНП')]/following-sibling::div[contains(.,'Тип полиса обязателен для заполнения')]");
    /**
     * Создание пациента для консультации - Данные о страховом полисе - Выберите тип полиса
     */
    @FindBy(xpath = "//input[@placeholder='Выберите тип полиса']")
    public WebElement PolisType;
    public By PolisTypeWait = By.xpath("//input[@placeholder='Выберите тип полиса']");
    public By PolisTypeSelect = By.xpath("//li/span[contains(.,'Полис ОМС старого образца')]");
    public By PolisTypeSelectEdin = By.xpath("//li/span[contains(.,'Полис ОМС единого образца')]");
    /**
     * Создание пациента для консультации - Данные о страховом полисе - Введите значение ЕНП
     */
    @FindBy(xpath = "//input[@placeholder='Введите значение ЕНП']")
    public WebElement PolisENP;
    /**
     * Создание пациента для консультации - Данные о страховом полисе - Тип полиса / ЕНП - Недоступно указание
     */
    public By PolisTypeError2 = By.xpath(
            "//input[@placeholder='Введите значение ЕНП']/ancestor::div[1]/following-sibling::div[contains(.,'Недоступно указание значения в поле «ЕНП»')]");
    /**
     * Создание пациента для консультации - Данные о страховом полисе - Тип полиса / ЕНП - Длина должна равняться 16 символам
     */
    public By PolisTypeError3 = By.xpath(
            "//input[@placeholder='Введите значение ЕНП']/ancestor::div[1]/following-sibling::div[contains(.,'Длина должна равняться 16 символам')]");
    /**
     * Создание пациента для консультации - Top1
     */
    @FindBy(xpath = "//div[@x-placement='top-start']//li[1]")
    public WebElement Top1Click;
    public By Top1 = By.xpath("//div[@x-placement='top-start']//li[1]");
    /**
     * Создание пациента для консультации - Далее
     */
    @FindBy(xpath = "//footer/button[contains(.,'Далее')]")
    public WebElement NextPatient;
    public By NextPatientWait = By.xpath("//footer/button[contains(.,'Далее')]");
    /**
     * Создание пациента для консультации - Назад
     */
    @FindBy(xpath = "//footer/button[contains(.,'Назад')]")
    public WebElement BackPatient;
    /**
     * Создание пациента для консультации - Закрыть
     */
    @FindBy(xpath = "//footer/button[contains(.,'Закрыть')]")
    public WebElement ClosePatient;
    public By ClosePatientWait = By.xpath("//div[@class='el-message-box']");
    /**
     * Создание пациента для консультации - Да, закрыть окно
     */
    @FindBy(xpath = "//div[@class='el-message-box']//span[contains(.,'Да, закрыть окно')]")
    public WebElement YesClosePatient;
    /**
     * Создание пациента для консультации - Врач
     */
    @FindBy(xpath = "//label[@for='userId']/following-sibling::div//input")
    public WebElement Doctor;
    public By DoctorWait = By.xpath("//label[@for='userId']/following-sibling::div//input");
    public By SelectDoctor = By.xpath("//ul/li/span[contains(.,'Хакимова Имя Отчество')]");
    public By SelectDoctorDuble = By.xpath("//ul/li/span[contains(.,'Хакимова Имя Отчество')]");
    public By SelectDoctorFirst = By.xpath("//div[@x-placement]//ul/li[1]/span");
    public By SelectDoctorSecond = By.xpath("//div[@x-placement]//ul/li[2]/span");
    /**
     * Создание пациента для консультации - МО, куда направлен
     */
    public By MO = By.xpath("//label[contains(.,'МО, куда направлен')]");
    public By MOWait = By.xpath("//label[contains(.,'МО, куда направлен')]/following-sibling::div//input");
    public By SelectMO = By.xpath(
            "//ul//span[contains(.,'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"')]");
    public By SelectMOOKB = By.xpath("//ul//span[contains(.,'БУ ХМАО-Югры \"Окружная клиническая больница\"')]");
    public By SelectMOKon = By.xpath(
            "//ul//span[contains(.,'АУ ХМАО-Югры \"Кондинская районная стоматологическая поликлиника\"')]");
    public By MyMO = By.xpath("//div[@class='el-select el-select--medium']/following-sibling::button");
    /**
     * Создание пациента для консультации - Профиль
     */
    @FindBy(xpath = "(//input[@placeholder='Введите значение для поиска'])[3]")
    public WebElement Profile;
    public By ProfileWait = By.xpath("//label[contains(.,'Профиль')]/following-sibling::div//input");
    public By SelectProfileWaitKosmetologi = By.xpath("//div//ul/li/span[contains(.,'косметологии')]");
    public By SelectProfileWaitOptiki = By.xpath("//div//ul/li/span[contains(.,'медицинской оптике')]");
    public By SelectProfileFirst = By.xpath("//div[@x-placement]//ul/li[1]/span");
    /**
     * Создание пациента для консультации - Диагноз
     */
    @FindBy(xpath = "//label[contains(.,'Диагноз')]/following-sibling::div//input")
    public WebElement Diagnos;
    public By DiagnosWait = By.xpath("//label[contains(.,'Диагноз')]/following-sibling::div//input");
    /**
     * Форма консультации
     */
    public By Plan = By.xpath("(//label[contains(.,'Форма консультации')]/following-sibling::div//span)[1]");
    /**
     * Цель консультации
     */
    public By Goal = By.xpath("//label[contains(.,'Цель консультации')]/following-sibling::div//input");
    public By SelectGoal = By.xpath("//div[@x-placement]//ul/li[3]");
    public By SelectCovid = By.xpath("//div[@x-placement]//ul/li[contains(.,'Подозрение на COVID-19')]");
    public By SelectUtoch = By.xpath("//div[@x-placement]//ul/li[contains(.,'Уточнение диагноза')]");
    public By SelectOchnoe = By.xpath("//div[@x-placement]//ul/li[contains(.,'Очное консультирование')]");
    /**
     * Создание направления на удаленную консультацию - Создать консультацию
     */
    public By CreateConsul = By.xpath("//footer/button[contains(.,'Создать консультацию')]");
    /**
     * Создание направления на удаленную консультацию - Далее
     */
    public By NextConsul = By.xpath("//footer/button[contains(.,'Далее')]");
    /**
     * Создать консультацию - Добавить файлы
     */
    @FindBy(xpath = "//label[@for='file-upload-context-after-create'][contains(.,'Добавить файлы')]")
    public WebElement AddFiles;
    @FindBy(xpath = "//input[@type='file']")
    public WebElement AddFilesTwo;
    public By AddFilesWait = By.xpath("//label[@for='file-upload-context-after-create'][contains(.,'Добавить файлы')]");
    /**
     * Добавить файлы - Отправить консультацию
     */
    public By SendConsul = By.xpath("//footer/button[contains(.,'Отправить консультацию')]");

    /**
     * Отправить консультацию - Ошибка - Введены недопустимые символы в имени
     */
    public By ErrorName = By.xpath(
            "//h2[contains(.,'Ошибка')]/following-sibling::div/p[contains(.,'Введены недопустимые символы в имени;')]");


}
