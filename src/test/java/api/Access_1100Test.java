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
@Feature("Проверка отправки документа в РЭМД с отправкой только в ВИМИС")
public class Access_1100Test extends BaseAPI {
    SQL sql;
    public static String value_1100_vmcl_1;
    public static String value_1100_vmcl_2;
    public static String value_1100_vmcl_3;
    public static String value_1100_vmcl_4;
    public static String value_1100_remd;

    public void Access_1100BeforeMethod(String NameProp, String sms, boolean remd) throws IOException, SQLException {
        sql = new SQL();
        xml = new XML();
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        System.out.println("Проверка добавления значения по заявке 1100");
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println(
                    "Select * from " + sms + " where sms_id = '" + props.getProperty("" + NameProp + "") + "';");
            if (remd) {
                sql.SQL("Select count(sms_id) from " + sms + " where sms_id = '" + props.getProperty(
                        "" + NameProp + "") + "';");
            } else {
                sql.NotSQL("Select count(sms_id) from " + sms + " where sms_id = '" + props.getProperty(
                        "" + NameProp + "") + "';");
            }
        }
    }

    public void Access_1100Method(
            String FileName, String DocType, Integer vmcl, Boolean RanLoc, Integer docTypeVersion, Integer Role,
            Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms,
            String logs, String value
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(
                FileName, DocType, vmcl, 1, RanLoc, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1
        );
        sql.StartConnection(
                "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            value = sql.resultSet.getString("id");
            System.out.println(value);
        }
        System.out.println("Устанавливаем status = 1 в " + logs + "");
        sql.UpdateConnection(
                "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + value + "', '" + UUID.randomUUID() + "')");
        if (vmcl == 1 & FileName != "SMS/SMS3.xml") {
            InputProp("src/test/resources/my.properties", "value_1100_vmcl_1", value);
        }
        if (vmcl == 2 & FileName != "SMS/SMS3.xml") {
            InputProp("src/test/resources/my.properties", "value_1100_vmcl_2", value);
        }
        if (vmcl == 3 & FileName != "SMS/SMS3.xml") {
            InputProp("src/test/resources/my.properties", "value_1100_vmcl_3", value);
        }
        if (vmcl == 4 & FileName != "SMS/SMS3.xml") {
            InputProp("src/test/resources/my.properties", "value_1100_vmcl_4", value);
        }
        if (vmcl == 1 & FileName == "SMS/SMS3.xml") {
            InputProp("src/test/resources/my.properties", "value_1100_remd", value);
        }

    }

    @Issue(value = "TEL-1100")
    @Link(name = "ТМС-1427", url = "https://team-1okm.testit.software/projects/5/tests/1427?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС")
    @Description("Отправляем документ в ВИМИС, добавляем статус в Logs. Далее отправляем другой документ в ВИМИС и РЭМД, добавляем статус в Logs. После прохождения тестов смотрим, что документ, который только в ВИМИС не добавился, а документ в ВИМИС и РЭМД добавился в таблицы vimis.remd_onko_sent_result/ vimis.remd_cvd_sent_result/ vimis.remd_akineo_sent_result/ vimis.remd_prevention_sent_result")
    public void Access_1100Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1100Method(
                "SMS/SMS2-id=42.xml", "42", 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                value_1100_vmcl_1
        );
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС")
    public void Access_1100Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1100Method(
                "SMS/SMS2-id=42.xml", "42", 2, true, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms",
                "vimis.preventionlogs", value_1100_vmcl_2
        );
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС")
    public void Access_1100Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1100Method(
                "SMS/SMS2-id=42.xml", "42", 3, true, 2, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                value_1100_vmcl_3
        );
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС")
    public void Access_1100Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1100Method(
                "SMS/SMS2-id=42.xml", "42", 4, true, 2, 6, 4, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                value_1100_vmcl_4
        );
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС")
    public void Access_1100Vmcl_1Sms_3() throws IOException, SQLException, InterruptedException {
        Access_1100Method(
                "SMS/SMS3.xml", "3", 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                value_1100_remd
        );
    }
}
