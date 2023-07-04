package UI.TmTest;

import UI.SQL;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.Users;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.opentest4j.AssertionFailedError;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static api.BaseAPI.InputProp;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

abstract public class BaseTest {
    public static RemoteWebDriver driver;
    public static WebDriverWait wait;
    public static WebDriverWait waitThree;
    public static WebDriverWait waitOne;
    public static Actions actions;
    public static ChromeOptions chromeOptions;
    public static Date date;
    public static String Date;
    public SQL sql;
    public static String LOGIN_PAGE_URL;
    static SecureRandom RND = new SecureRandom();
    public static final String GENERATE_SNILS = "https://ortex.github.io/snils-generator/";

    /**
     * Фича отвечающая за отправку в minio (0 - не отправляется, 1 - отправляется)
     */
    public static Integer SmdToMinio = Integer.valueOf(System.getProperty("SmdToMINIO"));
    public static Integer KingNumber = Integer.valueOf(System.getProperty("Contour"));
    public static String remote_url_chrome = System.getProperty("UrlChrome");

    public static void setUp() throws MalformedURLException {
        WebDriverManager.chromedriver().setup();
        chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        chromeOptions.addArguments("window-size=1920,1080");
        driver = new RemoteWebDriver(new URL(remote_url_chrome), chromeOptions);
        driver.setFileDetector(new LocalFileDetector());
        wait = new WebDriverWait(driver, 20);
        actions = new Actions(driver);
        date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Date = formatForDateNow.format(date);
    }

    @BeforeEach
    public void init() throws SQLException, IOException, InterruptedException {
        sql = new SQL();
        setUp();
        InputClass();
        InputProp("src/test/resources/my.properties", "IfCountListner", "web");
        if (KingNumber == 1) {
            LOGIN_PAGE_URL = "https://tm-test.pkzdrav.ru/auth/bysnils?snils=136-307-230%2032&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
        }
        if (KingNumber == 2) {
            LOGIN_PAGE_URL = "https://tm-dev.pkzdrav.ru/auth/bysnils?snils=136-307-230%2032&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
        }
        if (KingNumber == 3) {
            LOGIN_PAGE_URL = "http://192.168.2.86:35006/auth/bysnils?snils=136-307-230%2032&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
        }
        if (KingNumber == 4) {
            LOGIN_PAGE_URL = "https://remotecons-test.miacugra.ru/auth/bysnils?snils=136-307-230%2032&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
        }
        sql.ReplacementConnection();
    }

    /**
     * Метод записи названия класса выполняемого теста в properties
     */
    public void InputClass() throws IOException {
        String str = String.valueOf(this.getClass().getSimpleName());
        InputProp("src/test/resources/my.properties", "className", str);
    }

    /**
     * Метод записи названия класса выполняемого теста из properties в Файл
     */
    public static void InputClassFile() throws IOException {
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();

        String text = props.getProperty("className");
        FileWriter writer = new FileWriter("src/test/resources/FiledTests.sh", true);
        BufferedWriter bufferWriter = new BufferedWriter(writer);
        bufferWriter.write(", " + text);
        bufferWriter.close();
    }

    /**
     * метод для создания рандомных переменных
     * length - длинна нужного слова
     * alphabet - набор символов из которых будет состоять слово
     */
    public static String getRandomWord(int length, String alphabet) {
        StringBuilder sb = new StringBuilder(Math.max(length, 16));
        for (int i = 0; i < length; i++) {
            int len = alphabet.length();
            int random = RND.nextInt(len);
            char c = alphabet.charAt(random);
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * метод для ожидания элемента
     * locator - локатор со страницы
     */
    public static void WaitElement(By locator) {
        wait.until(visibilityOfElementLocated(locator));
    }

    /**
     * метод для ожидания, когда пропадёт элемент
     * locator - локатор со страницы
     */
    public static void WaitNotElement(By locator) {
        wait.until(invisibilityOfElementLocated(locator));
    }

    /**
     * метод для ожидания, когда пропадёт элемент с выставлением времени
     * locator - локатор со страницы
     * time - время ожидания исчезновения элемента
     */
    public static void WaitNotElement3(By locator, Integer time) {
        waitThree = new WebDriverWait(driver, time);
        waitThree.until(invisibilityOfElementLocated(locator));
    }

    /**
     * метод для ввода данных в shadow-root
     * element - веб элемент, куда нужно ввести данные
     * word - слово для ввода
     */
    public static void inputWord(WebElement element, String word) {
        JavascriptExecutor js = driver;
        js.executeScript("arguments[0].value='" + word + "'", element);
        element.sendKeys(Keys.BACK_SPACE);
    }

    /**
     * метод для того, чтобы взять значение из shadow-root
     * element - веб элемент, откуда нужно взять данные
     */
    public static void TakeWord(WebElement element) {
        JavascriptExecutor js = driver;
        String number = (String) js.executeScript("return arguments[0].value", element);
    }

    /**
     * метод для проверки 2 значений
     * element1, element2 - веб элементы со страницы
     */
    public static boolean Assertions(String element1, String element2) {
        try {
            Assertions.assertEquals(element1, element2);
            System.out.println(element1 + " и " + element2 + " совпадают");
            return true;
        } catch (AssertionFailedError te) {
            System.out.println(element1 + " и " + element2 + " не совпадают");
            return false;
        }
    }

    /**
     * метод для условия видимости элемента
     * locator - локатор со страницы
     */
    public static boolean isElementVisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException te) {
            return false;
        }
    }

    /**
     * метод для условия видимости элемента с ожиданием 3 секунды
     * locator - локатор со страницы
     */
    public static boolean isElementNotVisible(By locator) {
        try {
            waitThree = new WebDriverWait(driver, 3);
            waitThree.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException te) {
            return false;
        }
    }

    /**
     * метод для условия видимости элемента с ожиданием 1 секунда
     * locator - локатор со страницы
     */
    public static boolean isElementVisibleOne(By locator) {
        try {
            waitOne = new WebDriverWait(driver, 1);
            waitOne.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException te) {
            return false;
        }
    }

    /**
     * метод Найти эелемент, показать его и нажать
     * element - веб элемент со страницы
     */
    public static void actionElementAndClick(WebElement element) throws InterruptedException {
        actions.moveToElement(element);
        actions.perform();
        Thread.sleep(1000);
        element.click();
    }

    /**
     * метод Найти эелемент, показать его и нажать
     * locator - локатор со страницы
     */
    public static void ClickElement(By locator) throws InterruptedException {
        WaitElement(locator);
        WebElement element = driver.findElement(locator);
        actions.moveToElement(element);
        actions.perform();
        Thread.sleep(1200);
        element.click();
    }

    /**
     * Авторизация
     * SelectName - выбираемое значение из селекта
     */
    public static void AuthorizationMethod(By SelectName) throws InterruptedException {
        driver.get(LOGIN_PAGE_URL);
        wait.until(visibilityOfElementLocated(By.xpath("//span[@class='el-input__suffix']")));
        driver.findElement(By.xpath("//span[@class='el-input__suffix']")).click();
        /** Ожидание */
        wait.until(visibilityOfElementLocated(By.cssSelector(".el-scrollbar__view")));
        wait.until(visibilityOfElementLocated(SelectName));
        WebElement element = driver.findElement(SelectName);
        actionElementAndClick(element);
        driver.findElement(By.xpath("//button[contains(.,'Выбрать')]")).click();
    }

    /**
     * Метод для добавления нового пользователя (AddUsers)
     * MO - Выбранная Мед. Организация
     * Subdivision - выбранное подразделение
     * Role - Выбранная Роль
     * mail - почта пользователя
     */
    public static void AddUsersMethod(By MO, By Subdivision, By Role, String mail) throws InterruptedException {
        Users users = new Users(driver);
        System.out.println("Пользователя нет в ИЕЭМК, добавляю");
        WaitElement(users.LastName);
        inputWord(driver.findElement(users.LastName), "Тести");
        inputWord(driver.findElement(users.Name), "Тести");
        inputWord(driver.findElement(users.Login), "Тести");
        inputWord(driver.findElement(users.Password), "Тести");

        ClickElement(users.Date);
        ClickElement(users.DateYToday);

        inputWord(driver.findElement(users.Password), "Тести");
        driver.findElement(users.Number).sendKeys("+7 (111) 111-11-11");
        inputWord(users.Email, mail);

        System.out.println("Добавление места работы");
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        driver.findElement(By.xpath("//button[@tooltiptext='Редактировать']/span[contains(.,'Добавить')]")).click();
        wait.until(visibilityOfElementLocated(By.xpath("//h3[contains(.,'Добавление места работы')]")));
        SelectClickMethod(By.xpath("//input[@placeholder = 'Укажите мед. организацию']"), MO);
        Thread.sleep(1500);
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        SelectClickMethod(By.xpath("//input[@placeholder = 'Укажите подразделение']"), Subdivision);
        Thread.sleep(1500);
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        SelectClickMethod(By.xpath("//input[@placeholder = 'Укажите роль пользователя']"), Role);
        driver.findElement(By.xpath(
                "//div[@class='el-dialog__footer']//button[@class='el-button el-button--success el-button--medium']/span[contains(.,'Добавить')]")).click();
        wait.until(visibilityOfElementLocated(
                By.xpath("//footer[@class='margin-top-30 text-right']//span[contains(.,'Добавить')]")));
        /** Добавление профиля */
        driver.findElement(By.xpath("(//span[contains(.,'Профили')])[1]")).click();
        wait.until(visibilityOfElementLocated(By.xpath("//h3[contains(.,'Медицинские профили')]")));
        driver.findElement(By.xpath("//div[@class='cell']//button[@tooltiptext='Добавить']")).click();
        wait.until(invisibilityOfElementLocated(By.xpath("//span[contains(.,'Нет данных')]")));
        driver.findElement(By.xpath("//input[@placeholder='Выберите медицинские профили']")).click();
        wait.until(visibilityOfElementLocated(By.xpath("//div[@x-placement='bottom-start']")));
        driver.findElement(By.xpath(
                "//div[@x-placement='bottom-start']//ul/li[contains(.,'авиационной и космической медицине')]")).click();
        wait.until(invisibilityOfElementLocated(By.xpath("//div[@x-placement='bottom-start']")));
        driver.findElement(By.xpath("//button[@tooltiptext='Применить']")).click();
        Thread.sleep(300);
        driver.findElement(By.xpath("(//button/span[contains(.,'Закрыть')])[3]")).click();
        wait.until(visibilityOfElementLocated(
                By.xpath("//footer[@class='margin-top-30 text-right']//span[contains(.,'Добавить')]")));
        driver.findElement(
                By.xpath("//footer[@class='margin-top-30 text-right']//span[contains(.,'Добавить')]")).click();
        Thread.sleep(1500);
        if (KingNumber == 4) {
            Thread.sleep(1500);
        }
    }

    /**
     * Метод для добавления МО в "Запись на оборудование" (AddEquipmentsTest)
     * Razdel - выбор раздела в настройках обордования
     * MO - Мед. Организация для которой будет доступено оборудование
     * MoSearch - Мед. Организация, которая должна быть добавлена в список
     */
    public static void AddEquipmentsMethod(By Razdel, By MO, By MoSearch) throws InterruptedException {
        MedOrganization medOrganization = new MedOrganization(driver);
        driver.findElement(Razdel).click();
        wait.until(visibilityOfElementLocated(medOrganization.MOReceiveWait));
        if (isElementNotVisible(MoSearch) == false) {
            ClickElement(medOrganization.AddWait);
            wait.until(visibilityOfElementLocated(medOrganization.SearchValueWait));
            wait.until(visibilityOfElementLocated(By.xpath("//div[@x-placement]")));
            WebElement organization1 = driver.findElement(MO);
            actionElementAndClick(organization1);
            ClickElement(medOrganization.AddMOWait);
            driver.findElement(Razdel).click();
        } else {
            driver.findElement(Razdel).click();
        }
    }

    /**
     * Метод для нажатия на селект и выбор нужного значения
     * element - веб элемент со страницы
     * SelectName - элемент в списке селекта
     */
    public static void SelectClickMethod(By element, By SelectName) throws InterruptedException {
        ClickElement(element);
        ClickElement(SelectName);
    }

    /**
     * метод для проверки расстояния подписей полей фильтрации (SignaturesTest)
     * locator - локатор со страницы
     */
    public static void AssertSignatures(By locator) {
        WaitElement(locator);
        String element = driver.findElement(locator).getCssValue("margin-bottom");
        Assertions.assertEquals("15px", element);
    }

    /**
     * метод для проверки расстояния между кнопками справа 10px (SignaturesTest)
     * locator - локатор со страницы
     */
    public static void AssertSignaturesButtonRight10(By locator) {
        WaitElement(locator);
        String element = driver.findElement(locator).getCssValue("margin-right");
        Assertions.assertEquals("10px", element);
    }

    /**
     * метод для проверки расстояния между кнопками справа 5px (SignaturesTest)
     * locator - локатор со страницы
     */
    public static void AssertSignaturesButtonRight5(By locator) {
        WaitElement(locator);
        String element = driver.findElement(locator).getCssValue("margin-right");
        Assertions.assertEquals("5px", element);
    }

    /**
     * метод для проверки расстояния между кнопками слева 5px (SignaturesTest)
     * locator - локатор со страницы
     */
    public static void AssertSignaturesButtonLeft5(By locator) {
        WaitElement(locator);
        String element = driver.findElement(locator).getCssValue("margin-left");
        Assertions.assertEquals("5px", element);
    }

    /**
     * метод для проверки расстояния между кнопками слева 5px (SignaturesTest)
     * locator - локатор со страницы
     */
    public static void AssertSignaturesButtonRightAndLeft(By locator) {
        WaitElement(locator);
        String element = driver.findElement(locator).getCssValue("padding-right");
        String element1 = driver.findElement(locator).getCssValue("padding-left");
        Assertions.assertEquals("5px", element);
        Assertions.assertEquals("5px", element1);
    }
}
