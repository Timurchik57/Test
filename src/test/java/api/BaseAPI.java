package api;

import UI.SQL;
import UI.TmTest.BaseTest;
import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

abstract public class BaseAPI extends BaseTest {
    protected XML xml;
    protected SQL sql;
    public RequestSpecification requestSpecification;
    public Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    public Properties prop = new Properties();
    public Date date;
    public String Date;
    public static String HostAddress;
    public static String MO;
    public static String NameMO;
    public static String SystemId;
    public static String Password;
    public static String Position;
    public static String Speciality;
    public static String CheckSum;
    public static String Signatures;
    public static String CheckSumSign;
    public static String PatientGuid;
    public static String Token;
    public static String BodyAuthorisation;
    public static String Departmen;
    public static String Snils;
    public static long ID;
    public static long SetID;
    public static long VN;
    public Properties props;

    public void ReplacementConnection() {
        /** Тм-Тест */
        if (KingNumber == 1) {
            HostAddress = "https://api.tm-test.pkzdrav.ru";
            MO = "1.2.643.5.1.13.13.12.2.86.8986";
            NameMO = "БУ ХМАО-Югры \"Белоярская районная больница\"";
            SystemId = "21";
            Password = "123";
            Position = "4.5";
            Speciality = "5.3";
            PatientGuid = "4743E15E-488A-44C6-AF50-DFF0778DD01A";
            Departmen = "1.2.643.5.1.13.13.12.2.86.8986.0.536268";
            Snils = "15979025720";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
        }
        /** Тм-Дев */
        if (KingNumber == 2) {
            HostAddress = "https://api.tm-dev.pkzdrav.ru";
            MO = "1.2.643.5.1.13.13.12.2.86.8986";
            NameMO = "БУ ХМАО-Югры \"Белоярская районная больница\"";
            SystemId = "21";
            Password = "123";
            Position = "4.5";
            Speciality = "5.2";
            PatientGuid = "c89bfaeb-732e-4d0e-a519-559743166fca";
            Departmen = "1.2.643.5.1.13.13.12.2.86.8986.0.536268";
            Snils = "15979025720";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
        }
        /** Тест Севы */
        if (KingNumber == 3) {
            HostAddress = "http://192.168.2.86:35007";
            MO = "1.2.643.5.1.13.13.12.2.92.9191";
            NameMO = "ГБУЗ Севастополя \"Севастопольский городской онкологический диспансер им. А.А. Задорожного\"";
            SystemId = "23";
            Password = "4338E8382E0A1613C7963AEE594F2E4D";
            Position = "4.7";
            Speciality = "5.4";
            PatientGuid = "280be813-69f4-456f-a362-f291ff63b956";
            Snils = "15979025720";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
        }
        /** Тест Хмао */
        if (KingNumber == 4) {
            HostAddress = "http://192.168.2.21:34074";
            MO = "1.2.643.5.1.13.13.12.2.86.8986";
            NameMO = "ГБУЗ Севастополя \"Севастопольский городской онкологический диспансер им. А.А. Задорожного\"";
            SystemId = "21";
            Password = "123";
            Position = "4.5";
            Speciality = "5.3";
            PatientGuid = "4743E15E-488A-44C6-AF50-DFF0778DD01A";
            Departmen = "1.2.643.5.1.13.13.12.2.86.8986.0.536268";
            Snils = "15979025720";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
        }
        /** Тест ЧАО */
        if (KingNumber == 5) {
            HostAddress = "http://192.168.2.7:31035";
            MO = "1.2.643.5.1.13.13.12.2.87.9016";
            NameMO = "ГБУЗ \"Чукотская окружная больница\" г. Анадырь";
            SystemId = "13";
            Password = "123";
            Position = "4.7";
            Speciality = "5.4";
            PatientGuid = "b7065053-874f-4eba-8650-53874f2ebadf";
            Departmen = "1.2.643.5.1.13.13.12.2.87.9016.0.18101";
            Snils = "12411395113";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
        }
    }

    /**
     * Метод для замены значения в property
     * FileName - путь до файла с переменными окружения, например (src/test/resources/my.properties)
     * NameProp - название переменной
     * Input - параметр, который хотим указать в переменную
     */
    @Step("Запись в Properties переменной {1} = {2} ")
    public static void InputProp(String FileName, String NameProp, String Input) throws IOException {
        FileInputStream in = new FileInputStream(FileName);
        Properties props = new Properties();
        props.load(in);
        in.close();
        FileOutputStream out = new FileOutputStream(FileName);
        props.setProperty(NameProp, Input);
        props.store(out, null);
        out.close();
    }

    /**
     * Метод для чтения переменной из property
     * FileName - путь до файла с переменными окружения, например (src/test/resources/my.properties)
     * NameProp - название переменной, из корой хотим взять значение
     */
    public void ReadProp(String FileName, String NameProp) throws IOException {
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();
        String Name = props.getProperty(NameProp);
    }

    @BeforeEach
    public void initSpec() throws IOException, InterruptedException {
        xml = new XML();
        sql = new SQL();
        date = new Date();
        InputClass();
        InputProp("src/test/resources/my.properties", "IfCountListner", "api");
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Date = formatForDateNow.format(date);
        ReplacementConnection();
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
    }
}