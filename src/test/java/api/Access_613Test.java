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
@Feature("Сохранение значение структурного подразделения из СЭМД")
public class Access_613Test extends BaseAPI {
    SQL sql;
    public String value613;
    public String URLKremd;

    public void Access_613Method(
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
                    value613 = sql.resultSet.getString("id");
                    System.out.println(value613);
                }
            } else {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value613 = sql.resultSet.getString("id");
                    System.out.println(value613);
                }
            }
            System.out.println("Проверяем добавление значений в таблице " + info + "");
            sql.StartConnection("Select * from " + info + " where smsid = '" + value613 + "';");
            while (sql.resultSet.next()) {
                String department_oid = sql.resultSet.getString("department_oid");
                Assertions.assertEquals(
                        department_oid, "1.2.643.5.1.13.13.12.2.77.8312.0.166444",
                        "Значение в поле department_oid не совпадаетс с providerOrganization/id.extension"
                );
            }
        }
    }

    @Issue(value = "TEL-613")
    @Link(name = "ТМС-1179", url = "https://team-1okm.testit.software/projects/5/tests/1179?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Сохранение значение структурного подразделения из СЭМД для vmcl =1")
    @Description("Отправить СЭМД с заполненным полем providerOrganization/id.extension. После перейти в таблицу info и проверить заполнение поля department_oid")
    public void Access_613Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_613Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "", "vimis.additionalinfo");

    }

    @Test
    @DisplayName("Сохранение значение структурного подразделения из СЭМД для vmcl =2")
    public void Access_613Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_613Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "",
                "vimis.preventionadditionalinfo"
        );

    }

    @Test
    @DisplayName("Сохранение значение структурного подразделения из СЭМД для vmcl =3")
    public void Access_613Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_613Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "",
                "vimis.akineoadditionalinfo"
        );

    }

    @Test
    @DisplayName("Сохранение значение структурного подразделения из СЭМД для vmcl =4")
    public void Access_613Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_613Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "", "vimis.cvdadditionalinfo");

    }

    @Test
    @DisplayName("Сохранение значение структурного подразделения из СЭМД для vmcl =99")
    public void Access_613Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_613Method(
                "SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "", "vimis.remd_sent_result",
                "vimis.remdadditionalinfo"
        );
    }
}
