package api;

import UI.SQL;
import UI.TestListener;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Отправка документа со статусом regional")
public class Access_1148Test extends BaseAPI {
    SQL sql;

    public void Access1148Method(
            String File, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position,
            Integer speciality, Integer Role1, Integer position1, Integer speciality1, String remd
    ) throws SQLException, IOException, InterruptedException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Переходим на сайт для перехвата сообщений");
            if (KingNumber == 4) {
                driver.get("http://192.168.2.21:34329");
            } else {
                driver.get("http://192.168.2.126:10227");
            }
            Thread.sleep(1500);
            System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
            xml.ApiSmd(
                    File, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            Thread.sleep(1500);
            sql.StartConnection(
                    "SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
                System.out.println(sql.value);
                Assertions.assertEquals(
                        sql.value, "regional", "СМС не добавилось в таблицу " + remd + " со статусом regional");
            }
            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
            String text = driver.findElement(By.xpath("//div[@class='text-center']/div[1]/span")).getText();
            System.out.println(text);
            System.out.println(xml.uuid);
            Assertions.assertTrue(
                    text.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось"
            );

        }
    }

    @Issue(value = "TEL-1148")
    @Link(name = "ТМС-1420", url = "https://team-1okm.testit.software/projects/5/tests/1420?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка документа со статусом regional")
    @Description("Отправить документ у которого regional_document = true (на данный момент это id=130), проверить, что Добавился в таблицу vimis.certificate_remd_sent_result со статусом = regional. На сайте перехвата уведомлений появилось нужное уведомление")
    public void Access_1148Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access1148Method(
                "SMS/id=130-vmcl=99.xml", "130", 99, 1, 14, 4, 18, 16, 4, 21, "vimis.certificate_remd_sent_result");
    }
}
