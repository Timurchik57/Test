package UI.TmTest.PageObject.VIMIS;

import UI.SQL;
import UI.TmTest.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClinicalRecommendations extends BaseTest {
    public ClinicalRecommendations(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Метод для выбора направления и сравнение значений с БД
     * Diagnosis - Локатор диагноза
     * NameDiagnosis - Название направления
     * NumberDiagnosis - Нумерация направления
     */
    public void ChooseDirectionsMethod(
            By Diagnosis, String NameDiagnosis, int NumberDiagnosis
    ) throws InterruptedException, SQLException {
        SQL sql = new SQL();
        /** Клинические рекомендации - Выберите направление медицинской помощи */
        System.out.println("Выбор направления медицинской помощи");
        WaitElement(ChoseDirectionsWait);
        SelectClickMethod(ChoseDirectionsWait, Diagnosis);
        Thread.sleep(1500);
        Search.click();
        /** Обработка данных таблицы. Проверка Наименования */
        System.out.println("Обработка данных таблицы. Проверка Наименования - " + NameDiagnosis + "");
        Thread.sleep(3000);
        List<String> GetNameKR = new ArrayList<String>();
        List<WebElement> NameKR = driver.findElements(By.xpath("//*[@id='table_diagnostics']/tbody/tr/td[2]"));
        for (int i = 0; i < NameKR.size(); i++) {
            GetNameKR.add(NameKR.get(i).getText());
        }
        Collections.sort(GetNameKR);
        List<String> SQLNameKR = new ArrayList<String>();
        sql.StartConnection("select c.\"name\" from vimis.clinrec c \n" +
                                    "join vimis.clinrec_diagnoses cd on c.id = cd.clinical_recommendation_id\n" +
                                    "join dpc.mkb10 m on cd.mkb_code = m.mkb_code \n" +
                                    "join vimis.mkb_vmcl mv on m.id = mv.mkb_id\n" +
                                    "where mv.vmcl = '" + NumberDiagnosis + "' group by c.\"name\";");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("name");
            SQLNameKR.add(sql.value);
        }
        Collections.sort(SQLNameKR);
        /** Проверка значений */
        System.out.println("Проверка значений");
        Assertions.assertEquals(GetNameKR, SQLNameKR);
        System.out.println("-----Значения для " + NameDiagnosis + " совпадают");
    }

    /**
     * ВИМИС - Клинические рекомендации
     */
    @FindBy(xpath = "//a[@href='/vimis/clinical-recommendations']")
    public WebElement ClinicalRecommendations;
    public By ClinicalRecommendationsWait = By.xpath("//a[@href='/vimis/clinical-recommendations']");
    /**
     * Клинические рекомендации - Заголовок
     */
    public By Header = By.xpath("//h1[.='Клинические рекомендации']");
    /**
     * Клинические рекомендации - Выберите направление медицинской помощи
     */
    @FindBy(xpath = "//input[@placeholder='Выберите направление медицинской помощи']")
    public WebElement ChoseDirections;
    public By ChoseDirectionsWait = By.xpath("//input[@placeholder='Выберите направление медицинской помощи']");
    public By SelectChoseDirections = By.xpath("//li/span[contains(.,'1 - Онкология')]");
    public By SelectChoseDirectionsPrev = By.xpath("//li/span[contains(.,'2 - Профилактика')]");
    public By SelectChoseDirectionsAkineo = By.xpath("//li/span[contains(.,'3 - Акушерство и неонатология')]");
    public By SelectChoseDirectionsCVD = By.xpath("//li/span[contains(.,'4 - Сердечно-сосудистые заболевания')]");
    public By SelectChoseDirectionsOther = By.xpath("//li/span[contains(.,'99 - Иные профили')]");
    /**
     * Клинические рекомендации - Выберите название документа
     */
    public By NameDocument = By.xpath("//input[@placeholder='Введите значение для поиска']");
    public By SelectNameDocumentRakStomach = By.xpath(
            "//li/span[contains(.,'Клиническая рекомендация \"Рак желудка\"')]");
    public By SelectNameDocumentMammaryGland = By.xpath(
            "//li/span[contains(.,'Клиническая рекомендация \"Рак молочной железы\"')]");
    public By SelectNameDocumentPredGland = By.xpath(
            "//li/span[contains(.,'Клиническая рекомендация \"Рак предстательной железы\"')]");
    public By SelectNameDocumentRectum = By.xpath(
            "//li/span[contains(.,'Клиническая рекомендация \"Рак прямой кишки\"')]");
    public By SelectNameDocumentBodyUterus = By.xpath(
            "//li/span[contains(.,'Клиническая рекомендация \"Рак тела матки\"')]");
    public By SelectNameDocumentNeckUterus = By.xpath(
            "//li/span[contains(.,'Клиническая рекомендация \"Рак шейки матки\"')]");
    public By SelectNameDocumentOvaries = By.xpath(
            "//li/span[contains(.,'Клиническая рекомендация \"Рак яичников/рак маточной трубы/первичный рак брюшины\"')]");
    /**
     * Клинические рекомендации - Поиск
     */
    @FindBy(xpath = "//button/span[contains(.,'Поиск')]")
    public WebElement Search;
    /**
     * Клинические рекомендации - Таблица - наименование
     */
    @FindBy(xpath = "//*[@id='table_diagnostics']/tbody/tr[1]/td[2]")
    public WebElement TableName;
    public By TableWaitName = By.xpath("//*[@id='table_diagnostics']/tbody/tr[1]/td[2]");
    /**
     * Клинические рекомендации - Таблица - Диагноз
     */
    @FindBy(xpath = "//*[@id='table_diagnostics']/tbody/tr/td[4]")
    public WebElement TableDiagnosis;
    public By TableWaitDiagnosis = By.xpath("//*[@id='table_diagnostics']/tbody/tr/td[4]");
}
