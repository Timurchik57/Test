package api;

import UI.SQL;
import UI.TestListener;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Хранение информации о смерти пациентов из СМС13")
public class Access_904Test extends BaseAPI {
    XML xml;
    SQL sql;
    public static String value_904;

    public void StorageInformationBefore(String NameProp) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        xml = new XML();
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        System.out.println("Проверка добавления значения по заявке 904");
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Select * from vimis.cvd_death_cause where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
            sql.SQL("Select count(sms_id) from vimis.cvd_death_cause where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
            sql.StartConnection("Select * from vimis.cvd_death_cause where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
            while (sql.resultSet.next()) {
                String death_date = sql.resultSet.getString("death_date");
                String death_cause_codesystem = sql.resultSet.getString("death_cause_codesystem");
                String death_cause = sql.resultSet.getString("death_cause");
                String pathology_caused_death = sql.resultSet.getString("pathology_caused_death");
                String original_death_cause = sql.resultSet.getString("original_death_cause");
                String external_death_cause = sql.resultSet.getString("external_death_cause");
                Assertions.assertEquals(death_date, "2015-08-06 23:01:00");
                Assertions.assertEquals(death_cause_codesystem, "1.2.643.5.1.13.13.11.1005");
                Assertions.assertEquals(death_cause, "G93.5");
                Assertions.assertEquals(pathology_caused_death, "S06.1");
                Assertions.assertEquals(original_death_cause, "S02.7");
                Assertions.assertEquals(external_death_cause, "V80.0");
            }
        }
    }

    @Issue(value = "TEL-904")
    @Link(name = "ТМС-1273", url = "https://team-1okm.testit.software/projects/5/tests/1273?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Хранение информации о смерти пациентов из СМС13")
    @Description("Отправляем смс13, добавляем status 1 в logs и проверяем, что добавилась в таблицу vimis.cvd_death_cause")
    public void StorageInformation() throws IOException, SQLException, InterruptedException {
        sql = new SQL();
        xml = new XML();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            /**Отправляем смс с id = 33 и vmcl = 1/2/3/4 */
            //  System.out.println("Отправляем смс с id = 33 и vmcl=" + Vmlc + "");
            xml.ApiSmd("SMS/SMS13-id=13.xml", "13", 4, 1, true, 2, 6, 4, 18, 1, 57, 21);
            sql.StartConnection(
                    "Select * from vimis.cvdsms where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value_904 = sql.resultSet.getString("id");
                System.out.println(value_904);
            }
            sql.UpdateConnection(
                    "insert into vimis.cvdlogs (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + value_904 + "', '" + UUID.randomUUID() + "')");
            InputProp("src/test/resources/my.properties", "value_904", value_904);
        }
    }
}

