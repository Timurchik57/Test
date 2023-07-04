package UI;

import UI.TmTest.BaseTest;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.sql.*;

public class SQL extends BaseTest {
    public static Connection connection;
    public static Statement statement;
    public static Session session;
    public ResultSet resultSet;
    public String value;
    public static String URL;
    public static String USERNAME;
    public static String PASSWORD;
    public static String User;
    public static String Host;
    public static String Password;
    public static Integer Port;
    JSch jsch = new JSch();

    public void ReplacementConnection() {
        /** Тм-Тест */
        if (KingNumber == 1) {
            URL = "jdbc:postgresql://192.168.2.38:5432/telemed_test";
            USERNAME = "postgres";
            PASSWORD = "LBGScJFq";
        }
        /** Тм-Дев */
        if (KingNumber == 2) {
            URL = "jdbc:postgresql://192.168.2.38:5432/postgres";
            USERNAME = "postgres";
            PASSWORD = "LBGScJFq";
        }
        /** Тест Севы */
        if (KingNumber == 3) {
            URL = "jdbc:postgresql://192.168.2.86:35008/postgres";
            USERNAME = "miac_read";
            PASSWORD = "uBXgeaTMA4QpgHfD";
        }
        /** Тест ХМАО*/
        if (KingNumber == 4) {
            URL = "jdbc:postgresql://192.168.2.21:34031/postgres";
            USERNAME = "postgres";
            PASSWORD = "Qu28aT2";
        }
        /** Тест ЧАО*/
        if (KingNumber == 5) {
            URL = "jdbc:postgresql://192.168.137.77:6432/dev";
            USERNAME = "postgres";
            PASSWORD = "Qu28aT2";
            User = "user";
            Host = "192.168.2.7";
            Port = 32052;
            Password = "57NheFyjHfpLfh";
        }
    }

    /**
     * Метод для подключения к БД
     * SQL - запрос к БД
     */
    @Step("Запрос к БД")
    public void StartConnection(String SQL) throws SQLException {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(SQL);
    }

    /**
     * Метод для подключения к БД с использованием SSH
     * SQL - запрос к БД
     */
    public void StartConnectionSSH(String SQL) throws SQLException, JSchException {
        session = jsch.getSession(User, Host, 22);
        session.setPassword(Password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        session.setPortForwardingL(Port, "192.168.2.21", 6432);
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(SQL);
    }

    /**
     * Метод для изменений данных в БД Тм-тест
     * SQL - запрос к БД
     */
    @Step("Изменение данные в БД")
    public void UpdateConnection(String SQL) throws SQLException {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        statement = connection.createStatement();
        statement.executeUpdate(SQL);
        connection.close();
    }

    /**
     * Метод, который проверяет, есть ли по данному запросу, какая либо запись в БД
     * SQL - запрос к БД
     */
    public void SQL(String SQL) throws SQLException {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(SQL);
        while (resultSet.next()) {
            value = resultSet.getString("count");
            Assertions.assertNotEquals("0", value, "По данному запросу нет записей");
        }
        connection.close();
    }

    /**
     * Метод, который проверяет не добавилась запись в БД
     * SQL - запрос к БД
     */
    public void NotSQL(String SQL) throws SQLException {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(SQL);
        while (resultSet.next()) {
            value = resultSet.getString("count");
            System.out.println(value + " Записей");
            Assertions.assertEquals("0", value);
        }
        connection.close();
    }
}
