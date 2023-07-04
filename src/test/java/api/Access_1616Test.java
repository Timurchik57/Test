package api;

import UI.SQL;
import UI.TestListener;
import io.qameta.allure.*;
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
@Feature("Повторная отправка смс с успешно отправленным local_uid в РЭМД")
public class Access_1616Test extends BaseAPI {
    public void Access_1616Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd
    ) throws IOException, InterruptedException, SQLException {
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            System.out.println(xml.uuid);
            xml.ReplacementWordInFileBack(FileName);
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                System.out.println(sql.value);
            }
            System.out.println("Устанавливаем status = 1 в " + logs + "");
            sql.UpdateConnection(
                    "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + sql.value + "', '" + UUID.randomUUID() + "')");
            System.out.println("Устанавливаем status = 1 в " + remd + "");
            sql.UpdateConnection(
                    "insert into " + remd + " (sms_id, local_uid, status, created_datetime, fremd_status) values (" + sql.value + ", '" + xml.uuid + "', 'success', '" + Date + " 00:00:00.888 +0500', 1);");
            System.out.println("Повторно отправляем смс с тем же local_uid");
            System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, 2, false, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            System.out.println(xml.uuid);
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "' order by id desc limit 1;");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                System.out.println(sql.value);
            }
            System.out.println("Устанавливаем status = 1 в " + logs + "");
            sql.UpdateConnection(
                    "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + sql.value + "', '" + UUID.randomUUID() + "')");
            if (vmcl == 1) {
                InputProp("src/test/resources/my.properties", "value_1616_vmcl_1", sql.value);
            }
            if (vmcl == 2) {
                InputProp("src/test/resources/my.properties", "value_1616_vmcl_2", sql.value);
            }
            if (vmcl == 3) {
                InputProp("src/test/resources/my.properties", "value_1616_vmcl_3", sql.value);
            }
            if (vmcl == 4) {
                InputProp("src/test/resources/my.properties", "value_1616_vmcl_4", sql.value);
            }
        }
    }

    public void Access_1616AfterMethod(String NameProp, String remd) throws IOException, SQLException {
        sql = new SQL();
        xml = new XML();
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        System.out.println("Проверка, что значения не добавились заявке 1616");
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Select count(sms_id) from " + remd + " where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
            sql.NotSQL("Select count(sms_id) from " + remd + " where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
        }
    }

    @Issue(value = "TEL-1616")
    @Link(name = "ТМС-1733", url = "https://team-1okm.testit.software/projects/5/tests/1733?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Повторная отправка смс с успешно отправленным local_uid в РЭМД vmcl = 1")
    @Description("Отправляем смс - добавляем статус 1 в logs и fremd_status = 1 в remd, после отправляем смс с тем же local_uid и проверяем, что смс добавилось в таблицу logs, но не добавилась в таблицу remd")
    public void Access_1619Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1616Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @DisplayName("Формирование поля speciality в запросе к КРЭМД vmcl=2")
    @Test
    public void Access_1619Vmcl_2() throws InterruptedException, SQLException, IOException {
        Access_1616Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );

    }

    @DisplayName("Формирование поля speciality в запросе к КРЭМД vmcl=3")
    @Test
    public void Access_1619Vmcl_3() throws InterruptedException, SQLException, IOException {
        Access_1616Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );

    }

    @DisplayName("Формирование поля speciality в запросе к КРЭМД vmcl=4")
    @Test
    public void Access_1619Vmcl_4() throws InterruptedException, SQLException, IOException {
        Access_1616Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );
    }
}