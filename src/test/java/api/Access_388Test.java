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
@Feature("Возможность передавать в РЭМД документы pdf в base64")
public class Access_388Test extends BaseAPI {
    SQL sql;
    public String value388;

    public void Access_388TestMethod(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String remd, String info
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем смс с Doctype = " + DocType + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            sql.StartConnection(
                    "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value388 = sql.resultSet.getString("id");
                System.out.println(value388);
            }
            sql.StartConnection(
                    "Select * from " + info + " where effectivetime > '" + Date + " 00:00:00.888 +0500' and smsid = '" + value388 + "';");
            while (sql.resultSet.next()) {
                String documentid = sql.resultSet.getString("documentid");
                Assertions.assertEquals(
                        documentid, "" + xml.uuid + "", "Значение в поле documentid не совпадает с local_uid");
            }
        }
    }

    @Issue(value = "TEL-388")
    @Link(name = "ТМС-11", url = "https://team-1okm.testit.software/projects/5/tests/11?isolatedSection=827ef86d-406f-4fec-a839-c7939a1a4497")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Возможность передавать в РЭМД документы pdf в base64")
    @Description("Отправляем смс vmcl = 99 с ошибкой в xml. После отправить такой же документ, но для dpc.registered_emd.format = 1 и проверить, что ошибки нет")
    public void Access_388() throws IOException, SQLException, InterruptedException {
        Access_388TestMethod(
                "SMS/388.xml", "38", 99, 1, true, 1, 1, 11, 18, 1, 57, 21, "vimis.remd_sent_result",
                "vimis.remdadditionalinfo"
        );
    }
}
