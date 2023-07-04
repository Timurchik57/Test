package api;

import UI.TestListener;
import UI.TmTest.AccessUI.Administration.Access_1791Test;
import api.A.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Проверка бд после тестов")
public class After extends BaseAPI {
    @Issue(value = "TEL-904")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Хранение информации о смерти пациентов из СМС13")
    @Description("Проверка заявки 904, на добавление в таблицу vimis.cvd_death_cause")
    public void AfterTests904() throws IOException, SQLException, InterruptedException {
        Access_904Test access_904Test = new Access_904Test();
        access_904Test.StorageInformationBefore("value_904");
    }

    @Issue(value = "TEL-922")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование даты создания документа")
    @Description("Проверка заявки 922, на добавление в таблицы vimis.remd_onko_sent_result/vimis.remd_prevention_sent_result/vimis.remd_akineo_sent_result/vimis.remd_cvd_sent_result.")
    public void AfterTests922() throws IOException, SQLException, InterruptedException {
        Access_922Test access_922Test = new Access_922Test();
        access_922Test.Access_922MethodBefore("vimis.remd_onko_sent_result", 1, "value_922_Vmcl_1");
        access_922Test.Access_922MethodBefore("vimis.remd_prevention_sent_result", 2, "value_922_Vmcl_2");
        access_922Test.Access_922MethodBefore("vimis.remd_akineo_sent_result", 3, "value_922_Vmcl_3");
        access_922Test.Access_922MethodBefore("vimis.remd_cvd_sent_result", 4, "value_922_Vmcl_4");
    }

    @Issue(value = "TEL-937")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Хранение информации по пациентам ССЗ по скорой помощи")
    @Description("Проверка 937, на добавление в таблицу vimis.cvd_call_an_ambulance")
    public void AfterTests937() throws IOException, SQLException, InterruptedException {
        Access_937Test access_937Test = new Access_937Test();
        access_937Test.Access_937Before("value_937ID_107");
        access_937Test.Access_937Before("value_937ID_116");
    }

    @Issue(value = "TEL-1100")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС")
    @Description("После прохождения тестов смотрим, что документ, который только в ВИМИС не добавился, а документ в ВИМИС и РЭМД добавился в таблицы vimis.remd_onko_sent_result/ vimis.remd_cvd_sent_result/ vimis.remd_akineo_sent_result/ vimis.remd_prevention_sent_result")
    public void AfterTests1100() throws IOException, SQLException, InterruptedException {
        Access_1100Test access_1100Test = new Access_1100Test();
        access_1100Test.Access_1100BeforeMethod("value_1100_vmcl_1", "vimis.remd_onko_sent_result", false);
        access_1100Test.Access_1100BeforeMethod("value_1100_vmcl_2", "vimis.remd_prevention_sent_result", false);
        access_1100Test.Access_1100BeforeMethod("value_1100_vmcl_3", "vimis.remd_akineo_sent_result", false);
        access_1100Test.Access_1100BeforeMethod("value_1100_vmcl_4", "vimis.remd_cvd_sent_result", false);
        access_1100Test.Access_1100BeforeMethod("value_1100_remd", "vimis.remd_onko_sent_result", true);
    }

    @Issue(value = "TEL-1272")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких vmcl")
    @Description("Отправляем смс по нескольким направлениям и проверяем, что for_send = true только у первого стоящего vmcl, и отправка в РЭМД идёт только у него")
    public void AfterTests1272() throws IOException, SQLException, InterruptedException {
        Access_1272Test access_1272Test = new Access_1272Test();
        access_1272Test.BeforeAccess_1272("vimis.remd_onko_sent_result", "value_1272_vmcl_1");
        access_1272Test.BeforeAccess_1272("vimis.remd_prevention_sent_result", "value_1272_vmcl_2");
        access_1272Test.BeforeAccess_1272("vimis.remd_akineo_sent_result", "value_1272_vmcl_3");
        access_1272Test.BeforeAccess_1272("vimis.remd_cvd_sent_result", "value_1272_vmcl_4");
    }

    @Issue(value = "TEL-1555")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка добавления записи после успешного принятия документа ФРЭМД")
    @Description("Отправляем смс в РЭМД, добавляем  fremd_status = '1'. После отправяем смс в ВИМИС и РЭМД с таким же Local_uid, добавляем статус 1 в logs и после проверяем, что смс не добавилась в таблицы remd")
    public void AfterTests1555() throws IOException, SQLException, InterruptedException {
        Access_1555Test access_1555Test = new Access_1555Test();
        access_1555Test.Access_1555AfterMethod("value_1555_vmcl_1", "vimis.remd_onko_sent_result");
        access_1555Test.Access_1555AfterMethod("value_1555_vmcl_2", "vimis.remd_prevention_sent_result");
        access_1555Test.Access_1555AfterMethod("value_1555_vmcl_3", "vimis.remd_akineo_sent_result");
        access_1555Test.Access_1555AfterMethod("value_1555_vmcl_4", "vimis.remd_cvd_sent_result");

    }

    @Issue(value = "TEL-1616")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Повторная отправка смс с успешно отправленным local_uid в РЭМ")
    @Description("Отправляем смс - добавляем статус 1 в logs и fremd_status = 1 в remd, после отправляем смс с тем же local_uid и проверяем, что смс добавилось в таблицу logs, но не добавилась в таблицу remd")
    public void AfterTests1616() throws IOException, SQLException, InterruptedException {
        Access_1616Test access_1616Test = new Access_1616Test();
        access_1616Test.Access_1616AfterMethod("value_1616_vmcl_1", "vimis.remd_onko_sent_result");
        access_1616Test.Access_1616AfterMethod("value_1616_vmcl_2", "vimis.remd_prevention_sent_result");
        access_1616Test.Access_1616AfterMethod("value_1616_vmcl_3", "vimis.remd_akineo_sent_result");
        access_1616Test.Access_1616AfterMethod("value_1616_vmcl_4", "vimis.remd_cvd_sent_result");
    }

    @Issue(value = "TEL-1786")
    @Issue(value = "TEL-1800")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Добавление данных диспансерных больных в таблицу по смс5")
    @Description("Отправить СЭМД смс5 с triggerPoint = 6, установить статус 1 в таблицу vimis.preventionlogs, проверить заполение теблицы vimis.prevention_sms_v5_register")
    public void AfterTests1786() throws IOException, SQLException, InterruptedException {
        Access_1786Test access_1786Test = new Access_1786Test();
        access_1786Test.Access_1786After("value_1786_Vmcl_2_tg6", "vimis.prevention_sms_v5_register");
        access_1786Test.Access_1786After("value_1786_Vmcl_2_tg12", "vimis.prevention_sms_v5_register");
    }

    @Issue(value = "TEL-1791")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отображение данных из регистра Профилактика")
    @Description("Создаём новый регистр с Профилактикой - переходим в данный регистр и проверяем добавленную запись из таблицы prevention_sms_v5_register")
    public void AfterTests1791() throws IOException, SQLException, InterruptedException {
        Access_1791Test access_1791Test = new Access_1791Test();
        access_1791Test.Access_1791After("SizePatients_1791");
    }

    @Test
    @DisplayName("Редактируем файл запуска упавших тестов")
    public void AfterFiledTest() throws IOException {
        String str = new String(
                Files.readAllBytes(Paths.get("src/test/resources/FiledTests.sh"))).replace("mvn test -Dtest=, ", "");

        String modifiedText = Arrays.stream(str.split(" "))
                .reduce("", (result, word) -> result.contains(word) ? result : result + " " + word);

        String modifiedText1 = modifiedText.replace("mvn test -Dtest= ", "mvn test -Dtest=").substring(
                1);

        new FileWriter("src/test/resources/FiledTests.sh", false).close();

        FileWriter writer = new FileWriter("src/test/resources/FiledTests.sh", true);
        BufferedWriter bufferWriter = new BufferedWriter(writer);
        bufferWriter.write(
                "mvn test -Dtest=\"" + modifiedText1 + "\" -DUrlChrome=" + remote_url_chrome + " -DSmdToMINIO=" + SmdToMinio + " -DContour=" + KingNumber + "");
        bufferWriter.close();
    }

    @Test
    @DisplayName("Повторно запускаем настройку контура")
    public void AfterSettings() throws IOException, SQLException, InterruptedException {
        Authorization authorization = new Authorization();
        authorization.BeforeData();
    }

}
