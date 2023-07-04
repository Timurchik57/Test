package api;

import UI.SQL;
import UI.TestListener;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Хранение информации о снилс получателя документа")
public class Access_1096Test extends BaseAPI {
    SQL sql;
    public String value1096;

    public void Access_1096Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String remd, String info
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            if (vmcl == 99) {
                sql.StartConnection(
                        "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value1096 = sql.resultSet.getString("id");
                    System.out.println(value1096);
                }
            } else {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value1096 = sql.resultSet.getString("id");
                    System.out.println(value1096);
                }
            }
            System.out.println("Проверяем добавление значений в таблице " + info + ".recipient_snils");
            sql.StartConnection("Select * from " + info + " where smsid = '" + value1096 + "';");
            while (sql.resultSet.next()) {
                String recipient_snils = sql.resultSet.getString("recipient_snils");
                if (FileName == "SMS/SMS3.xml") {
                    Assertions.assertNull(recipient_snils, "Значение recipient_snils не совпадает");
                } else {
                    Assertions.assertEquals(recipient_snils, "19591054097", "Значение recipient_snils не совпадает");
                }
            }
            Thread.sleep(1500);
        }
    }

    @Issue(value = "TEL-1096")
    @Issue(value = "TEL-1080")
    @Link(name = "ТМС-1374", url = "https://team-1okm.testit.software/projects/5/tests/1374?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Хранение информации о снилс получателе документа для vmcl = 1")
    @Description("Отправляем смс с блоком IRCP, после проверяем таблицу vimis.cvd/akineo/prevention/remd/additionalinfo.recipient_snils на заполненное поле")
    public void Access_1096Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1096Method(
                "SMS/id=15-vmcl=99.xml", "15", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "",
                "vimis.additionalinfo"
        );
    }

    @Test
    @DisplayName("Хранение информации о снилс получателе документа для vmcl = 2")
    public void Access_1096Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1096Method(
                "SMS/id=15-vmcl=99.xml", "15", 2, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms", "",
                "vimis.preventionadditionalinfo"
        );
    }

    @Test
    @DisplayName("Хранение информации о снилс получателе документа для vmcl = 3")
    public void Access_1096Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1096Method(
                "SMS/id=15-vmcl=99.xml", "15", 3, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "",
                "vimis.akineoadditionalinfo"
        );
    }

    @Test
    @DisplayName("Хранение информации о снилс получателе документа для vmcl = 4")
    public void Access_1096Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1096Method(
                "SMS/id=15-vmcl=99.xml", "15", 4, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.cvdsms", "",
                "vimis.cvdadditionalinfo"
        );
    }

    @Test
    @DisplayName("Хранение информации о снилс получателе документа для vmcl = 99")
    public void Access_1096Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1096Method(
                "SMS/id=15-vmcl=99.xml", "15", 99, 1, true, 2, 6, 4, 18, 1, 57, 21, "", "vimis.remd_sent_result",
                "vimis.remdadditionalinfo"
        );
    }

    @Test
    @DisplayName("Хранение информации о снилс получателе документа для vmcl = 1 id =3")
    public void Access_1096Vmcl_1Id_3() throws IOException, SQLException, InterruptedException {
        Access_1096Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "", "vimis.additionalinfo");
    }
}
