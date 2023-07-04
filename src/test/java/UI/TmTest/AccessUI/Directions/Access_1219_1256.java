package UI.TmTest.AccessUI.Directions;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationArchived;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingArchived;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.IncomingUnfinished;
import api.BaseAPI;
import api.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
public class Access_1219_1256 extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    IncomingUnfinished incomingUnfinished;
    ConsultationUnfinished consultationUnfinished;
    ConsultationArchived consultationArchived;
    ConsultationOutgoingArchived consultationOutgoingArchived;
    SQL sql;
    public static String[] FIO;
    public static String Role;
    public static String RoleId;
    public static String Post;
    public static String PostId;
    public static String Specialization;
    public static String SpecializationId;
    public static String Id;
    public static String Doctype;
    public static String LocalUid;
    public static String org_signature_id;
    public static String surname;
    public static String name;
    public static String patrName;
    public static String snils;
    public String URLRemd;
    public static String orgSignatureSum;
    public static String personalSignaturesSum;
    public static String uuid;
    public static String NumberConsul;

    /**
     * Метод для установки Крипто Про в расширение
     */
    public void CryptoProMethod() throws AWTException, InterruptedException {
        Robot r = new Robot();
        driver.get("https://chrome.google.com/webstore/category/extensions");
        inputWord(driver.findElement(By.xpath("//input[@aria-label]")), "CryptoProo");
        driver.findElement(By.xpath("//input[@aria-label]")).sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        ClickElement(By.xpath("//div[@role='row']/div[1]//div[@class='a-na-d-K-A']"));
        Thread.sleep(5000);
        ClickElement(By.xpath("(//div[@aria-label='Установить'])[1]"));
        Thread.sleep(3000);
        r.keyPress(KeyEvent.VK_LEFT);
        r.keyPress(KeyEvent.VK_ENTER);
        while (!isElementNotVisible(By.xpath(
                "(//div[@class='g-c-R  webstore-test-button-label'][contains(.,'Удалить из Chrome')])[1]"))) {
            r.keyPress(KeyEvent.VK_ESCAPE);
            Thread.sleep(1500);
            ClickElement(By.xpath("(//div[@aria-label='Установить'])[1]"));
            Thread.sleep(1500);
            r.keyPress(KeyEvent.VK_LEFT);
            Thread.sleep(1500);
            r.keyPress(KeyEvent.VK_ENTER);
        }
        Thread.sleep(2000);
        r.keyPress(KeyEvent.VK_ESCAPE);
        Thread.sleep(2000);
    }

    /**
     * Метод для создания консультации
     */
    public void Access_1219Method(
            By Consultation, String Direction, boolean MyMO, By Research, By Doctor, By Goal
    ) throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        /** Переход в создание консультации на оборудование */
        ClickElement(directionsForQuotas.ConsultationWait);
        /** Создание консультации */
        System.out.println("Создание консультации");
        ClickElement(directionsForQuotas.CreateWait);
        System.out.println("Выбираем консультацию");
        ClickElement(Consultation);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        Thread.sleep(1500);
        ClickElement(directionsForQuotas.NextWait);
        if (Direction == "Направление на диагностику") {
            /** Заполнение информации о направившем враче */
            System.out.println("Заполнение информации о направившем враче");
            WaitElement(directionsForQuotas.InfoDoctorWait);
            WaitElement(directionsForQuotas.SubmittingDoctorWait);
            Thread.sleep(2000);
            SelectClickMethod(
                    directionsForQuotas.SubmittingDoctorWait,
                    directionsForQuotas.FIO
            );
            Thread.sleep(1500);
            WaitNotElement(directionsForQuotas.loading);
            SelectClickMethod(
                    directionsForQuotas.Division,
                    directionsForQuotas.SelectDivision
            );
            Thread.sleep(1500);
            WaitNotElement(directionsForQuotas.loading);
            SelectClickMethod(
                    directionsForQuotas.DepartmentWait,
                    directionsForQuotas.SelectDepartment
            );
            Thread.sleep(1500);
            WaitNotElement(directionsForQuotas.loading);
            SelectClickMethod(
                    directionsForQuotas.Post,
                    directionsForQuotas.SelectPost
            );
            Thread.sleep(1500);
            WaitNotElement(directionsForQuotas.loading);
            SelectClickMethod(
                    directionsForQuotas.Specialization,
                    directionsForQuotas.SelectSpecializationFirst
            );
            Thread.sleep(1500);
            WaitNotElement(directionsForQuotas.loading);
            SelectClickMethod(directionsForQuotas.AnatomicalAreas, directionsForQuotas.SelectAnatomicalAreas);
            if (MyMO) {
                ClickElement(directionsForQuotas.MyMODirection);
                Thread.sleep(2000);
                SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);
            }
            if (!MyMO) {
                if (KingNumber == 1 | KingNumber == 2) {
                    SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirection);
                }
                if (KingNumber == 4) {
                    SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirectionKon);
                }
            }
            directionsForQuotas.Diagnosis.sendKeys(Keys.SPACE);
            ClickElement(directionsForQuotas.SelectDiagnosisWait);
            SelectClickMethod(directionsForQuotas.Research, Research);
            SelectClickMethod(directionsForQuotas.BenefitCode, directionsForQuotas.SelectBenefitCode);
            WaitElement(directionsForQuotas.MassWait);
            inputWord(directionsForQuotas.Mass, "500");
            System.out.println("Вес пациента " + "500");
            inputWord(directionsForQuotas.NamePatient, "Тестовыйй");
            inputWord(directionsForQuotas.LastNamePatient, "Тестт");
            inputWord(directionsForQuotas.MiddleNamePatient, "Тестовичч");
            actionElementAndClick(directionsForQuotas.NextPatient);
            /** Окно направления на диагностику*/
            WaitElement(directionsForQuotas.Header);
            WaitElement(directionsForQuotas.CreateDirectionWait);
            Thread.sleep(1000);
            actionElementAndClick(directionsForQuotas.CreateDirection);
            /** Прикрепление файла */
            System.out.println("Прикрепление  файла");
            WaitElement(directionsForQuotas.FileWait);
            WaitElement(directionsForQuotas.AddFileWait);
            Thread.sleep(1000);
            java.io.File file = new File("src/test/resources/test.txt");
            directionsForQuotas.File.sendKeys(file.getAbsolutePath());
            Thread.sleep(500);
            ClickElement(directionsForQuotas.Close);
            Thread.sleep(2000);
        } else {
            System.out.println("Заполняем данные");
            SelectClickMethod(directionsForQuotas.DoctorWait, Doctor);
            if (!MyMO) {
                if (KingNumber == 1 | KingNumber == 2) {
                    SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMO);
                }
                if (KingNumber == 4) {
                    SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMOKon);
                }
            } else {
                ClickElement(directionsForQuotas.MyMO);
                SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);

            }
            SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.SelectFirst);
            inputWord(directionsForQuotas.Diagnos, "AA");
            Thread.sleep(1000);
            ClickElement(authorizationObject.SelectFirst);
            ClickElement(directionsForQuotas.Plan);
            SelectClickMethod(directionsForQuotas.Goal, Goal);
            ClickElement(directionsForQuotas.NextConsul);
            ClickElement(directionsForQuotas.CreateConsul);
            System.out.println("Прикрепление  файла");
            WaitElement(directionsForQuotas.AddFilesWait);
            Thread.sleep(1000);
            File file = new File("src/test/resources/test.txt");
            directionsForQuotas.File.sendKeys(file.getAbsolutePath());
            Thread.sleep(500);
            ClickElement(directionsForQuotas.SendConsul);
            Thread.sleep(2000);
        }
    }

    /**
     * Метод для сбора данных из селекта
     */
    public void SelectAccessMethod(
            String Select, By NumberSelect, String SQL, String NameSQL, String Name
    ) throws SQLException {
        authorizationObject = new AuthorizationObject(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        sql = new SQL();
        List<WebElement> select = driver.findElements(authorizationObject.SelectALL);
        List<String> Web = new ArrayList<>();
        for (int i = 0; i < select.size(); i++) {
            Web.add(select.get(i).getAttribute("innerText"));
        }
        Select = driver.findElement(NumberSelect).getAttribute("innerText")
        ;
        System.out.println("Сверяем значения с БД");
        List<String> RoleSQL = new ArrayList<>();
        sql.StartConnection(SQL);
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString(NameSQL);
            RoleSQL.add(sql.value);
        }
        Assertions.assertEquals(Web, RoleSQL, "Значения " + Name + " не совпадают с БД");
    }

    /**
     * Метод для сбора названия и id выбранной роли, должности и специальности
     */
    public void ChoiceMethod(String SQL, String NameSQL, String Id, String Name) throws SQLException {
        sql = new SQL();
        System.out.println("Берём значение из БД");
        sql.StartConnection(SQL);
        while (sql.resultSet.next()) {
            if (Name == "Роль") {
                RoleId = sql.resultSet.getString(NameSQL);
            }
            if (Name == "Должность") {
                PostId = sql.resultSet.getString(NameSQL);
            }
            if (Name == "Специальность") {
                SpecializationId = sql.resultSet.getString(NameSQL);
            }

        }
    }

    /**
     * Метод для отправки протокола с проверкой выбранных значений в бд и в запросе отправки самого протокола
     */
    public void AddProtocolMethod(
            By Select, boolean MyMO, boolean CurrentUser
    ) throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        sql = new SQL();
        System.out.println("Переходим в МО куда отправили консультацию и завершаем её");
        if (!MyMO) {
            if (KingNumber == 1 | KingNumber == 2) {
                AuthorizationMethod(authorizationObject.YATCKIV);
            }
            if (KingNumber == 4) {
                AuthorizationMethod(authorizationObject.Kondinsk);
            }
        } else {
            AuthorizationMethod(authorizationObject.OKB);
        }
        ClickElement(consultationUnfinished.UnfinishedWait);
        ClickElement(consultationUnfinished.FirstWait);
        ClickElement(consultationUnfinished.Closed);
        WaitElement(consultationUnfinished.ClosedText);
        uuid = String.valueOf(UUID.randomUUID());
        System.out.println(uuid);
        inputWord(driver.findElement(consultationUnfinished.ClosedText), uuid + "j");
        SelectClickMethod(consultationUnfinished.DataDay, consultationUnfinished.NextMonth);
        ClickElement(consultationUnfinished.Closed2);
        Thread.sleep(3000);
        System.out.println("Отправляем протокол");
        ClickElement(consultationUnfinished.AddConsultation);
        if (!CurrentUser) {
            System.out.println("Заполняем Фамилия");
            inputWord(driver.findElement(consultationUnfinished.LastNameProtocol), "Тест ");
            System.out.println("Заполняем Имя");
            inputWord(driver.findElement(consultationUnfinished.NameProtocol), "Тест ");
            System.out.println("Заполняем Отчество");
            inputWord(driver.findElement(consultationUnfinished.MiddleNameProtocol), "Тест ");
            System.out.println("Заполняем Снилс");
            inputWord(driver.findElement(consultationUnfinished.SnilsProtocol), "15979025720 ");
            System.out.println("Выбираем подразделение");
            ClickElement(consultationUnfinished.DivisionProtocol);
            if (KingNumber != 4) {
                ClickElement(consultationUnfinished.SelectDivisionProtocol);
            } else {
                ClickElement(consultationUnfinished.SelectDivisionProtocoBuh);
            }
        } else {
            System.out.println("Выбираем подразделение");
            ClickElement(consultationUnfinished.DivisionProtocol);
            if (KingNumber != 4) {
                ClickElement(consultationUnfinished.SelectDivisionProtocol);
            } else {
                ClickElement(consultationUnfinished.SelectDivisionProtocoBuh);
            }
            ClickElement(consultationUnfinished.CheckBox);
        }
        System.out.println("Берём все значения Роли");
        ClickElement(consultationUnfinished.Role);
        Thread.sleep(1500);
        WaitElement(Select);
        SelectAccessMethod(
                Role, consultationUnfinished.FirstSelect, "SELECT * FROM dpc.signer_role;", "name_role", "Роли");
        Role = driver.findElement(Select).getText();
        ClickElement(Select);
        ChoiceMethod("SELECT * FROM dpc.signer_role where name_role = '" + Role + "';", "code_role", RoleId, "Роль");
        System.out.println(Role);
        System.out.println(RoleId);
        System.out.println("Берём все значения Должности");
        ClickElement(consultationUnfinished.Post);
        Thread.sleep(1500);
        WaitElement(Select);
        SelectAccessMethod(
                Post, consultationUnfinished.FirstSelect, "SELECT * FROM dpc.med_worker_positions;", "work_position",
                "Должности"
        );
        Post = driver.findElement(Select).getText();
        ClickElement(Select);
        ChoiceMethod(
                "SELECT * FROM dpc.med_worker_positions where work_position = '" + Post + "';", "id", PostId,
                "Должность"
        );
        System.out.println(Post);
        System.out.println(PostId);
        System.out.println("Берём все значения Специальности");
        ClickElement(consultationUnfinished.Specialization);
        Thread.sleep(1500);
        WaitElement(Select);
        SelectAccessMethod(
                Specialization, consultationUnfinished.FirstSelect,
                "SELECT * FROM dpc.medical_and_pharmaceutical_specialties;", "name", "Специальности"
        );
        Specialization = driver.findElement(Select).getText();
        ClickElement(Select);
        ChoiceMethod(
                "SELECT * FROM dpc.medical_and_pharmaceutical_specialties where name = '" + Specialization + "' limit 1;",
                "id", SpecializationId, "Специальность"
        );
        System.out.println(Specialization);
        System.out.println(SpecializationId);
        System.out.println("Выбираем сертификаты");
        ClickElement(consultationUnfinished.CertDoctor);
        ClickElement(consultationUnfinished.CertDoctorTim);
        ClickElement(consultationUnfinished.CertMO);
        ClickElement(consultationUnfinished.CertDoctorTim);
        ClickElement(consultationUnfinished.AddConsultation2);
        Thread.sleep(7000);

        System.out.println("Проверяем добавление в таблицу vimis.remd_sent_result");
        sql.StartConnection(
                "SELECT * FROM vimis.remd_sent_result  where  created_datetime > '" + Date + " 00:00:00.888 +0500' order by id desc limit 1;");
        while (sql.resultSet.next()) {
            Id = sql.resultSet.getString("id");
            Doctype = sql.resultSet.getString("doctype");
            LocalUid = sql.resultSet.getString("local_uid");
            org_signature_id = sql.resultSet.getString("org_signature_id");
        }
        System.out.println(LocalUid);
        if (CurrentUser) {
            System.out.println("Берём значения доктора из бд");
            sql.StartConnection("SELECT * FROM telmed.users WHERE sname = 'Хакимова';");
            while (sql.resultSet.next()) {
                name = sql.resultSet.getString("fname");
                patrName = sql.resultSet.getString("mname");
                surname = sql.resultSet.getString("sname");
                snils = sql.resultSet.getString("snils");
            }
            System.out.println("Убираем в snils пробелы и тире");
            String chars = "- !";
            for (char c : chars.toCharArray()) {
                snils = snils.replace(String.valueOf(c), "");
            }
        } else {
            name = "Тест";
            patrName = "Тест";
            surname = "Тест";
            snils = "15979025720";
        }
        System.out.println("Сверяем тело запроса в РЭМД");
        Token = new String(Files.readAllBytes(Paths.get("AuthorizationToken.txt")));
        if (KingNumber == 1) {
            URLRemd = "http://192.168.2.126:1108/api/rremd/" + LocalUid + "?CreationDateBegin=" + Date + "";
        }
        if (KingNumber == 2) {
            URLRemd = "http://192.168.2.126:1131/api/rremd/" + LocalUid + "?CreationDateBegin=" + Date + "";
        }
        if (KingNumber == 4) {
            Thread.sleep(3000);
            URLRemd = "http://192.168.2.21:34154/api/rremd/" + LocalUid + "?CreationDateBegin=" + Date + "";
        }
        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .get(URLRemd)
                .body()
                .jsonPath();
        if (SmdToMinio == 0) {
            assertThat(response.get("result[0].localUid"), equalTo("" + LocalUid + ""));
            assertThat(response.get("result[0].kind.code"), equalTo("8"));
            assertThat(response.get("result[0].patient.surname"), equalTo("Тестировщик"));
            assertThat(response.get("result[0].patient.name"), equalTo("Тест"));
            assertThat(response.get("result[0].patient.patrName"), equalTo("Тестович"));
            assertThat(response.get("result[0].patient.snils"), equalTo("15979025720"));
            orgSignatureSum = response.get("result[0].orgSignature.checksum");
            personalSignaturesSum = response.get("result[0].personalSignatures[0].signature.checksum");
            assertThat(response.get("result[0].personalSignatures[0].signer.role.code"), equalTo("" + RoleId + ""));
            assertThat(response.get("result[0].personalSignatures[0].signer.position.code"), equalTo("" + PostId + ""));
            assertThat(
                    response.get("result[0].personalSignatures[0].signer.speciality.code"),
                    equalTo("" + SpecializationId + "")
            );
            assertThat(response.get("result[0].personalSignatures[0].signer.surname"), equalTo("" + surname + ""));
            assertThat(response.get("result[0].personalSignatures[0].signer.name"), equalTo("" + name + ""));
            assertThat(response.get("result[0].personalSignatures[0].signer.patrName"), equalTo("" + patrName + ""));
            assertThat(response.get("result[0].personalSignatures[0].signer.snils"), equalTo("" + snils + ""));
        } else {
            assertThat(response.get("result[0].documentDto.localUid"), equalTo("" + LocalUid + ""));
            assertThat(response.get("result[0].documentDto.kind.code"), equalTo("8"));
            assertThat(response.get("result[0].documentDto.patient.surname"), equalTo("Тестировщик"));
            assertThat(response.get("result[0].documentDto.patient.name"), equalTo("Тест"));
            assertThat(response.get("result[0].documentDto.patient.patrName"), equalTo("Тестович"));
            assertThat(response.get("result[0].documentDto.patient.snils"), equalTo("15979025720"));
            orgSignatureSum = response.get("result[0].documentDto.orgSignature.checksum");
            personalSignaturesSum = response.get("result[0].documentDto.personalSignatures[0].signature.checksum");
            assertThat(
                    response.get("result[0].documentDto.personalSignatures[0].signer.role.code"),
                    equalTo("" + RoleId + "")
            );
            assertThat(
                    response.get("result[0].documentDto.personalSignatures[0].signer.position.code"),
                    equalTo("" + PostId + "")
            );
            assertThat(
                    response.get("result[0].documentDto.personalSignatures[0].signer.speciality.code"),
                    equalTo("" + SpecializationId + "")
            );
            assertThat(
                    response.get("result[0].documentDto.personalSignatures[0].signer.surname"),
                    equalTo("" + surname + "")
            );
            assertThat(
                    response.get("result[0].documentDto.personalSignatures[0].signer.name"), equalTo("" + name + ""));
            assertThat(
                    response.get("result[0].documentDto.personalSignatures[0].signer.patrName"),
                    equalTo("" + patrName + "")
            );
            assertThat(
                    response.get("result[0].documentDto.personalSignatures[0].signer.snils"), equalTo("" + snils + ""));
        }
    }

    /**
     * Метод проверки добавления записи в архивные, в МО, из которой/в которую отправляли (заявка 1709)
     */
    public void CheckArchived() throws InterruptedException, SQLException {
        consultationArchived = new ConsultationArchived(driver);
        consultationOutgoingArchived = new ConsultationOutgoingArchived(driver);
        sql = new SQL();
        System.out.println("Берём ID консультации");
        sql.StartConnection("Select * from telmed.directions where conclusion = '" + uuid + "';");
        while (sql.resultSet.next()) {
            NumberConsul = sql.resultSet.getString("id");
        }
        System.out.println("Переходим в Направления - Консультации - Входящие - Архивные у МО в Которую отправили");
        ClickElement(consultationArchived.ArchivedWait);
        ClickElement(consultationArchived.DESK);
        ClickElement(By.xpath("//tbody/tr[1]/td[1][contains(.,'" + NumberConsul + "')]"));
        System.out.println("Проверяем, та ли это кансультация");
        WaitElement(consultationArchived.TextConclusion);
        driver.findElement(consultationArchived.TextConclusion).getText().equals("" + uuid + "");
        System.out.println("Переходим в Направления - Консультации - Исходящие - Архивные у МО из Которой отправили");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(consultationOutgoingArchived.OutgoingArchived);
        ClickElement(consultationOutgoingArchived.DESK);
        ClickElement(By.xpath("//tbody/tr[1]/td[1][contains(.,'" + NumberConsul + "')]"));
        WaitElement(consultationOutgoingArchived.TextConclusion);
        driver.findElement(consultationOutgoingArchived.TextConclusion).getText().equals("" + uuid + "");
    }

    @Issue(value = "TEL-1219")
    @Issue(value = "TEL-1228")
    @Issue(value = "TEL-1521")
    @Issue(value = "TEL-1523")
    @Issue(value = "TEL-1709")
    @Link(name = "ТМС-1469", url = "https://team-1okm.testit.software/projects/5/tests/1469?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Link(name = "ТМС-1725", url = "https://team-1okm.testit.software/projects/5/tests/1725?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Link(name = "ТМС-1751", url = "https://team-1okm.testit.software/projects/5/tests/1751?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Направления")
    @DisplayName("Подписание протокола удалённой консультации")
    @Description("Перейти в Консультации - Создать направление - Направление на консультацию - Создаём направление, указывая цель. Переходим в МО, куда отправили - входящие, переходим к ней, завершаем т отправляем протокол")
    public void Access_1219() throws IOException, SQLException, InterruptedException, AWTException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        System.out.println("Авторизуемся и переходим в создание направления на оборудование");
        System.out.println("Первая проверка - создание направления на диагностику и отсутствие создания протокола");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(
                directionsForQuotas.DistrictDiagnosticWait, "Направление на диагностику", false,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorFirst,
                directionsForQuotas.SelectOchnoe
        );
        System.out.println("Переходим в МО куда отправили направление и завершаем его");
        if (KingNumber == 1 | KingNumber == 2) {
            AuthorizationMethod(authorizationObject.YATCKIV);
        }
        if (KingNumber == 4) {
            AuthorizationMethod(authorizationObject.Kondinsk);
        }
        ClickElement(incomingUnfinished.ConsultationWait);
        ClickElement(incomingUnfinished.FirstLineMO);
        ClickElement(incomingUnfinished.CloseOutDirection);
        WaitElement(incomingUnfinished.CloseText);
        inputWord(driver.findElement(incomingUnfinished.CloseText), "Проверяю 12199");
        ClickElement(incomingUnfinished.CloseOutDirection2);
        Thread.sleep(2000);
        WaitNotElement(consultationUnfinished.AddConsultation);

        System.out.println("Вторая проверка - создание консультации с целью очное консультирование");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(
                directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", false,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorFirst,
                directionsForQuotas.SelectOchnoe
        );
        System.out.println("Переходим в МО куда отправили консультацию и завершаем её");
        if (KingNumber == 1 | KingNumber == 2) {
            AuthorizationMethod(authorizationObject.YATCKIV);
        }
        if (KingNumber == 4) {
            AuthorizationMethod(authorizationObject.Kondinsk);
        }
        ClickElement(consultationUnfinished.UnfinishedWait);
        ClickElement(consultationUnfinished.FirstWait);
        ClickElement(consultationUnfinished.Closed);
        WaitElement(consultationUnfinished.ClosedText);
        inputWord(driver.findElement(consultationUnfinished.ClosedText), "Проверяю 12199");
        SelectClickMethod(consultationUnfinished.DataDay, consultationUnfinished.NextMonth);
        ClickElement(consultationUnfinished.Closed2);
        Thread.sleep(3000);
        WaitNotElement(consultationUnfinished.AddConsultation);

        System.out.println("Устанавливаем расширение Крипто про");
        CryptoProMethod();

        System.out.println(
                "Третья проверка - создание консультации с целью отличной от очное консультирование, в другое МО, с чек боксом");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", false,
                          directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                          directionsForQuotas.SelectCovid);
        AddProtocolMethod(consultationUnfinished.FirstSelect, false, true);
        CheckArchived();

        System.out.println(
                "Четвертая проверка - создание консультации с целью отличной от очное консультирование, в другое МО, без чек бокса (заявка 1521)");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(
                directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", false,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                directionsForQuotas.SelectCovid
        );
        AddProtocolMethod(consultationUnfinished.FirstSelect, false, false);

        System.out.println(
                "Пятая проверка - создание консультации с целью отличной от очное консультирование, в МО с которого отправляется консультация, с чек боксом");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(
                directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", true,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorFirst,
                directionsForQuotas.SelectUtoch
        );
        AddProtocolMethod(consultationUnfinished.SecondSelect, true, true);
        CheckArchived();
    }

    @Issue(value = "TEL-1256")
    @Issue(value = "TEL-1424")
    @Link(name = "ТМС-1470", url = "https://team-1okm.testit.software/projects/5/tests/1470?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Link(name = "ТМС-1511", url = "https://team-1okm.testit.software/projects/5/tests/1511?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Направления")
    @DisplayName("Сохранение ЭЦП протокола консультаций")
    @Description("После подписания и отправки протокола, переходим в таблицы vimis.eds_signatures/ vimis.eds_personal_signatures/ vimis.eds_signers/ telmed.directions и проверяем значения")
    public void Access_1256() throws IOException, SQLException, InterruptedException, AWTException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        sql = new SQL();
        String IdOrg = null;
        System.out.println(
                "Переходим в vimis.eds_signatures и берём значение check_sum, сверяем значение подписи организации");
        sql.StartConnection("SELECT * FROM vimis.eds_signatures WHERE id = '" + org_signature_id + "';");
        while (sql.resultSet.next()) {
            IdOrg = sql.resultSet.getString("id");
            sql.value = sql.resultSet.getString("check_sum");
            Assertions.assertEquals(orgSignatureSum, sql.value, "Чек сумма подписи организации не совпадает");
        }
        Integer IdVrach = Integer.valueOf(IdOrg) + 1;
        System.out.println(
                "Переходим в vimis.eds_signatures и берём значение check_sum уже у подписи врача, она следует сразу за подписью МО");
        sql.StartConnection("SELECT * FROM vimis.eds_signatures WHERE id = '" + IdVrach + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("check_sum");
            Assertions.assertEquals(personalSignaturesSum, sql.value, "Чек сумма подписи врача не совпадает");
        }
        System.out.println(
                "Переходим в vimis.eds_personal_signatures и берём значение signer_id чтобы найти врача отправителя в vimis.eds_signers");
        sql.StartConnection("SELECT * FROM vimis.eds_personal_signatures WHERE signature_id = '" + IdVrach + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("signer_id");
        }
        System.out.println("Переходим в vimis.eds_signers и сверяем данные врача");
        sql.StartConnection(
                "SELECT x.id, x.role_id, s.name_role, x.last_name, x.first_name, x.middle_name, x.birthdate, x.snils, x.position_id, mwp.work_position, x.speciality_id, m.\"name\", x.department_oid FROM vimis.eds_signers x\n" +
                        "join dpc.signer_role s on x.role_id = s.id \n" +
                        "join dpc.med_worker_positions mwp on x.position_id = mwp.id \n" +
                        "join dpc.medical_and_pharmaceutical_specialties m on x.speciality_id = m.id \n" +
                        "WHERE x.id = " + sql.value + ";");
        while (sql.resultSet.next()) {
            String Roleid = sql.resultSet.getString("role_id");
            String role = sql.resultSet.getString("name_role");
            String Name = sql.resultSet.getString("first_name");
            String LastName = sql.resultSet.getString("last_name");
            String MiddleName = sql.resultSet.getString("middle_name");
            String SNILS = sql.resultSet.getString("snils");
            String Postid = sql.resultSet.getString("position_id");
            String post = sql.resultSet.getString("work_position");
            String Specialityid = sql.resultSet.getString("speciality_id");
            String speciality = sql.resultSet.getString("name");
            Assertions.assertEquals(role, Role, "Наименование Роли не совпадает");
            Assertions.assertEquals(Postid, PostId, "Код Должности не совпадает");
            Assertions.assertEquals(post, Post, "Наименование Должности не совпадает");
            Assertions.assertEquals(Specialityid, SpecializationId, "Наименование Специальности не совпадает");
            Assertions.assertEquals(speciality, Specialization, "Наименование Специальности не совпадает");
            Assertions.assertEquals(Name, name, "Имя Врача не совпадает");
            Assertions.assertEquals(LastName, surname, "Фамилия Врача не совпадает");
            Assertions.assertEquals(MiddleName, patrName, "Отчество Врача не совпадает");
            Assertions.assertEquals(SNILS, snils, "Снилс Врача не совпадает");
            System.out.println("Переходим в telmed.directions и проверяем, что установился статус 21");
            sql.SQL("Select count(*) from telmed.directions where conclusion = '" + uuid + "';");
            sql.StartConnection("Select * from telmed.directions where conclusion = '" + uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
                Assertions.assertEquals(
                        sql.value, "21", "Не установился статус 21 для смс с conclusion = '" + uuid + "'");
            }
        }
    }

    @Issue(value = "TEL-1364")
    @Link(name = "ТМС-1470", url = "https://team-1okm.testit.software/projects/5/tests/1470?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Направления")
    @DisplayName("Запоминание данных подписанта при отправке протокола")
    @Description("Перейти в Консультации - Создать направление - Направление на консультацию - Создаём направление, указывая цель. Переходим в МО, куда отправили - входящие, переходим к ней, завершаем и отправляем протокол. После проверяем таблицу  telmed.userplacework.role,position,speciality на добавленные значения из dpc.signer_role.id, dpc.med_worker_positions.id,dpc.medical_and_pharmaceutical_specialties.id. Обновляем страницу и проверяем, что на ui отображаются наименования, которые выбрали до это (запомнились), меняем значения и проверяем, что изменились значения в telmed.userplacework")
    public void Access_1364() throws IOException, SQLException, InterruptedException, AWTException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        System.out.println("Устанавливаем расширение Крипто про");
        CryptoProMethod();
        System.out.println("Авторизуемся и переходим в создание направления на оборудование");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(
                directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", false,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                directionsForQuotas.SelectCovid
        );
        AddProtocolMethod(consultationUnfinished.FirstSelect, false, true);
        Thread.sleep(1500);
        System.out.println("Сверяем значения с таблицей telmed.userplacework");
        sql.StartConnection("select u.*, s.name_role, w.work_position, m.\"name\"  FROM telmed.userplacework u\n" +
                                    "join dpc.signer_role s on u.\"role\" = s.id \n" +
                                    "join dpc.med_worker_positions w on u.\"position\" = w.id \n" +
                                    "join dpc.medical_and_pharmaceutical_specialties m on u.speciality = m.id \n" +
                                    "where u.userid = 25 order by id asc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("name_role");
            Assertions.assertEquals(sql.value, Role, "Не совпадет роль с БД");
            sql.value = sql.resultSet.getString("work_position");
            Assertions.assertEquals(sql.value, Post, "Не совпадет должность с БД");
            sql.value = sql.resultSet.getString("name");
            Assertions.assertEquals(sql.value, Specialization, "Не совпадет специализация с БД");
        }
        System.out.println("Снова Авторизуемся и переходим в создание направления на оборудование");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(
                directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", false,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                directionsForQuotas.SelectCovid
        );
        System.out.println("Переходим в МО куда отправили консультацию и завершаем её");
        if (KingNumber == 1 | KingNumber == 2) {
            AuthorizationMethod(authorizationObject.YATCKIV);
        }
        if (KingNumber == 4) {
            AuthorizationMethod(authorizationObject.Kondinsk);
        }
        ClickElement(consultationUnfinished.UnfinishedWait);
        ClickElement(consultationUnfinished.FirstWait);
        ClickElement(consultationUnfinished.Closed);
        WaitElement(consultationUnfinished.ClosedText);
        uuid = String.valueOf(UUID.randomUUID());
        System.out.println(uuid);
        inputWord(driver.findElement(consultationUnfinished.ClosedText), uuid + "j");
        SelectClickMethod(consultationUnfinished.DataDay, consultationUnfinished.NextMonth);
        ClickElement(consultationUnfinished.Closed2);
        Thread.sleep(3000);
        System.out.println("Отправляем протокол");
        ClickElement(consultationUnfinished.AddConsultation);
        System.out.println("Выбираем подразделение");
        ClickElement(consultationUnfinished.DivisionProtocol);
        if (KingNumber != 4) {
            ClickElement(consultationUnfinished.SelectDivisionProtocol);
        } else {
            ClickElement(consultationUnfinished.SelectDivisionProtocoBuh);
        }
        ClickElement(consultationUnfinished.CheckBox);
        ClickElement(consultationUnfinished.Role);
        Thread.sleep(1500);
        String RoleUI = driver.findElement(consultationUnfinished.Selected).getText();
        Assertions.assertEquals(RoleUI, Role, "Выбранная роль не совпадает с ранее выбранной");
        RoleUI = driver.findElement(consultationUnfinished.SecondSelect).getText();
        ClickElement(consultationUnfinished.SecondSelect);
        ClickElement(consultationUnfinished.Post);
        Thread.sleep(1500);
        String PostUI = driver.findElement(consultationUnfinished.Selected).getText();
        Assertions.assertEquals(PostUI, Post, "Выбранная должность не совпадает с ранее выбранной");
        PostUI = driver.findElement(consultationUnfinished.SecondSelect).getText();
        ClickElement(consultationUnfinished.SecondSelect);
        ClickElement(consultationUnfinished.Specialization);
        Thread.sleep(1500);
        String SpecialisationUI = driver.findElement(consultationUnfinished.Selected).getText();
        Assertions.assertEquals(
                SpecialisationUI, Specialization, "Выбранная специализация не совпадает с ранее выбранной");
        SpecialisationUI = driver.findElement(consultationUnfinished.SecondSelect).getText();
        ClickElement(consultationUnfinished.SecondSelect);
        System.out.println("Выбираем сертификаты");
        ClickElement(consultationUnfinished.CertDoctor);
        ClickElement(consultationUnfinished.CertDoctorTim);
        ClickElement(consultationUnfinished.CertMO);
        ClickElement(consultationUnfinished.CertDoctorTim);
        ClickElement(consultationUnfinished.AddConsultation2);
        Thread.sleep(5000);
        System.out.println("Сверяем значения с таблицей telmed.userplacework");
        sql.StartConnection("select u.*, s.name_role, w.work_position, m.\"name\"  FROM telmed.userplacework u\n" +
                                    "join dpc.signer_role s on u.\"role\" = s.id \n" +
                                    "join dpc.med_worker_positions w on u.\"position\" = w.id \n" +
                                    "join dpc.medical_and_pharmaceutical_specialties m on u.speciality = m.id \n" +
                                    "where u.userid = 25 order by userid asc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("name_role");
            Assertions.assertEquals(sql.value, RoleUI, "Не совпадет роль с БД");
            sql.value = sql.resultSet.getString("work_position");
            Assertions.assertEquals(sql.value, PostUI, "Не совпадет должность с БД");
            sql.value = sql.resultSet.getString("name");
            Assertions.assertEquals(sql.value, SpecialisationUI, "Не совпадет специализация с БД");
        }
    }
}
