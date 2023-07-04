package api;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.isOneOf;

public class XML extends BaseAPI {
    public String body;
    public String content;
    public UUID uuid;
    public static String Type;
    public String encodedString;

    /**
     * Метод для отправки СМС
     * FileName - название файла
     * DocType - doctype фалйа XML
     * vmcl - направление СЭМД, vmcl фалйа XML
     * number - переменная, которая отвечает за увеличение генерируемых переменных для отправки СЭМД XML
     * RanLoc - true = нужно генерировать новый LocalUid, false = не нужно
     * docTypeVersion - docTypeVersion фалйа XML
     * Role - роль подписанта
     * position - должность подписанта
     * speciality - специальность подписанта
     * Role1,position1,speciality1 - может быть несколько подписантов
     */
    @Step("Отправка СМС")
    public void ApiSmd(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException {
        if (vmcl == 99) {
            ReplacementWordInFile(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            String value = String.valueOf(given()
                                                  .filter(new AllureRestAssured())
                                                  .header("Authorization", "Bearer " + Token)
                                                  .contentType(ContentType.JSON)
                                                  .when()
                                                  .body(body)
                                                  .post(HostAddress + "/api/smd")
                                                  .prettyPeek()
                                                  .then()
                                                  .statusCode(200)
                                                  .extract().jsonPath().getString("result[0].message"));
            Assertions.assertEquals(
                    value, "СМС предназначенная только для передачи в РЭМД успешно опубликована в ЦУ РС ЕГИСЗ.");
        }
        if (vmcl == 1) {
            ReplacementWordInFile(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            String value = String.valueOf(given()
                                                  .filter(new AllureRestAssured())
                                                  .header("Authorization", "Bearer " + Token)
                                                  .contentType(ContentType.JSON)
                                                  .when()
                                                  .body(body)
                                                  .post(HostAddress + "/api/smd")
                                                  .prettyPeek()
                                                  .then()
                                                  .statusCode(200)
                                                  .extract().jsonPath().getString("result[0].message"));
            assertThat(
                    value, isOneOf(
                            "СМС по направлению \"Онкология\" успешно опубликован в ЦУ РС ЕГИСЗ.",
                            "СМС по направлению \"Онкология\" успешно опубликован в РИЭМК.",
                            "СМС по направлению \"Онкология\" успешно опубликован в ПК РМИС."
                    ));
        }
        if (vmcl == 2) {
            ReplacementWordInFile(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            String value = String.valueOf(given()
                                                  .filter(new AllureRestAssured())
                                                  .header("Authorization", "Bearer " + Token)
                                                  .contentType(ContentType.JSON)
                                                  .when()
                                                  .body(body)
                                                  .post(HostAddress + "/api/smd")
                                                  .prettyPeek()
                                                  .then()
                                                  .statusCode(200)
                                                  .extract().jsonPath().getString("result[0].message"));
            Assertions.assertEquals(value, "СМС по направлению \"Профилактика\" успешно опубликован в ЦУ РС ЕГИСЗ.");
        }
        if (vmcl == 3) {
            ReplacementWordInFile(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            String value = String.valueOf(given()
                                                  .filter(new AllureRestAssured())
                                                  .header("Authorization", "Bearer " + Token)
                                                  .contentType(ContentType.JSON)
                                                  .when()
                                                  .body(body)
                                                  .post(HostAddress + "/api/smd")
                                                  .prettyPeek()
                                                  .then()
                                                  .statusCode(200)
                                                  .extract().jsonPath().getString("result[0].message"));
            Assertions.assertEquals(value, "СМС по направлению \"АкиНео\" успешно опубликован в ЦУ РС ЕГИСЗ.");
        }
        if (vmcl == 4) {
            ReplacementWordInFile(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            String value = String.valueOf(given()
                                                  .filter(new AllureRestAssured())
                                                  .header("Authorization", "Bearer " + Token)
                                                  .contentType(ContentType.JSON)
                                                  .when()
                                                  .body(body)
                                                  .post(HostAddress + "/api/smd")
                                                  .prettyPeek()
                                                  .then()
                                                  .statusCode(200)
                                                  .extract().jsonPath().getString("result[0].message"));
            Assertions.assertEquals(value, "СМС по направлению \"ССЗ\" успешно опубликован в ЦУ РС ЕГИСЗ.");
        }
    }

    /**
     * Метод для замены слов в файле
     * FileName - название файла
     * Search - слово в файле, которое нужно заменить
     * Value - слово на которое нужно заменить
     */
    public void ReplaceWord(String FileName, String Search, String Value) throws IOException, InterruptedException {
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        String fileName = FileName;
        String search = Search;
        String replace = Value;
        Charset charset = StandardCharsets.UTF_8;
        Path path = Paths.get(fileName);
        Files.write(
                path,
                new String(Files.readAllBytes(path), charset).replace(search, replace)
                        .getBytes(charset)
        );
    }

    /**
     * Метод для замены слов в файле
     */
    public void ReplaceWord1() throws IOException, InterruptedException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("test.txt")));
        String newStr = "";
        while ((newStr = in.readLine()) != null) {
            newStr = newStr.replace("15", "45");
        }
        System.out.println(in);
    }

    /**
     * Метод для замены нужных переменных в смс
     * FileName - название файла
     */
    @Step("Возвращаем значения в Файле")
    public void ReplacementWordInFileBack(String FileName) throws IOException, InterruptedException {
        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        ReplaceWord(FileName, props.getProperty("value_ID"), "${iD}");
        ReplaceWord(FileName, props.getProperty("value_SetID"), "${setId}");
        ReplaceWord(FileName, props.getProperty("value_VN"), "${versionNumber}");
        ReplaceWord(FileName, Departmen, "${depart}");
        ReplaceWord(FileName, MO, "${mo}");
        ReplaceWord(FileName, PatientGuid, "${guid}");
        ReplaceWord(FileName, NameMO, "${namemo}");
        ReplaceWord(FileName, Snils, "${snils}");
    }

    /**
     * Метод для замены нужных переменных
     * FileName - название файла
     * DocType - doctype фалйа XML
     * vmcl - направление СЭМД, vmcl фалйа XML
     * number - переменная, которая отвечает за увеличение генерируемых переменных для отправки СЭМД XML
     * RanLoc - true = нужно генерировать новый LocalUid, false = не нужно
     * docTypeVersion - docTypeVersion фалйа XML
     * Role - роль подписанта
     * position - должность подписанта
     * speciality - специальность подписанта
     * Role1,position1,speciality1 - может быть несколько подписантов
     */
    public String ReplacementWordInFile(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1
    ) throws IOException, InterruptedException {

        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        Token = new String(Files.readAllBytes(Paths.get("AuthorizationToken.txt")));
        Type = FileName;

        if (number == 1) {
            Thread.sleep(1000);
            ID = (int) Math.floor(timestamp.getTime() / 1000);
            SetID = (int) Math.floor(timestamp.getTime() / 1000) + 1;
            VN = (int) Math.floor(timestamp.getTime() / 1000) + 2;
            InputProp("src/test/resources/my.properties", "value_ID", String.valueOf(ID));
            InputProp("src/test/resources/my.properties", "value_SetID", String.valueOf(SetID));
            InputProp("src/test/resources/my.properties", "value_VN", String.valueOf(VN));
            ReplaceWord(FileName, "${iD}", String.valueOf(ID));
            ReplaceWord(FileName, "${setId}", String.valueOf(SetID));
            ReplaceWord(FileName, "${versionNumber}", String.valueOf(VN));
            ReplaceWord(FileName, "${depart}", Departmen);
            ReplaceWord(FileName, "${mo}", MO);
            ReplaceWord(FileName, "${guid}", PatientGuid);
            ReplaceWord(FileName, "${namemo}", NameMO);
            ReplaceWord(FileName, "${snils}", Snils);

        }
        if (number == 2) {
            Thread.sleep(1000);
            ID = Long.parseLong(props.getProperty("value_ID")) + 5;
            SetID = Long.parseLong(props.getProperty("value_SetID")) + 5;
            VN = Long.parseLong(props.getProperty("value_VN")) + 5;
            InputProp("src/test/resources/my.properties", "value_ID", String.valueOf(ID));
            InputProp("src/test/resources/my.properties", "value_SetID", String.valueOf(SetID));
            InputProp("src/test/resources/my.properties", "value_VN", String.valueOf(VN));
            ReplaceWord(FileName, "${iD}", String.valueOf(ID));
            ReplaceWord(FileName, "${setId}", String.valueOf(SetID));
            ReplaceWord(FileName, "${versionNumber}", String.valueOf(VN));
            ReplaceWord(FileName, "${depart}", Departmen);
            ReplaceWord(FileName, "${mo}", MO);
            ReplaceWord(FileName, "${guid}", PatientGuid);
            ReplaceWord(FileName, "${namemo}", NameMO);
            ReplaceWord(FileName, "${snils}", Snils);
        }
        content = new String(Files.readAllBytes(Paths.get(FileName)));
        encodedString = Base64.getEncoder().encodeToString(content.getBytes());
        /** Подсчёт чек Суммы для XML */
        String sum = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                              "  \"base64String\": \"" + encodedString + "\"\n" +
                              "}")
                .when()
                .post("http://192.168.2.126:10117/CheckSum/CalculateCheckSum")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("checkSum");
        CheckSum = sum;
        /** Получение подписи */
        String Signat = given()
                .contentType(ContentType.JSON)
                .body("{\"requestId\": \"2\",\n" +
                              "            \"serialNumber\": \"008B55DE3674250EE3DE4983AF7C4D455A\",\n" +
                              "            \"message\": \"" + encodedString + "\",\n" +
                              "            \"isBase64\": true}")
                .when()
                .post("http://192.168.2.126:8082/sign/message")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("result.signedMessage");
        Signatures = Signat;
        /** Подсчёт чек Суммы для Подписи */
        String sum1 = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                              "  \"base64String\": \"" + Signatures + "\"\n" +
                              "}")
                .when()
                .post("http://192.168.2.126:10117/CheckSum/CalculateCheckSum")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("checkSum");
        CheckSumSign = sum1;
        if (RanLoc) {
            uuid = UUID.randomUUID();
            System.out.println(uuid);
        }
        if (vmcl == 99) {
            body = "{\n" +
                    "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                    "    \"DocType\":\"" + DocType + "\",\n" +
                    "    \"DocContent\":\n" +
                    "    {\n" +
                    "        \"Document\":\"" + encodedString + "\",\n" +
                    "        \"CheckSum\":" + CheckSum + "\n" +
                    "        },\n" +
                    "        \"LocalUid\":\"" + uuid + "\",\n" +
                    "        \"Payment\":1,\n" +
                    "        \"ReasonForAbsenceIdcase\":\n" +
                    "        {\n" +
                    "            \"CodeSystemVersion\":\"1.1\",\n" +
                    "            \"Code\":10,\n" +
                    "            \"CodeSystem\":\"1.2.643.5.1.13.13.99.2.286\"\n" +
                    "        },\n" +
                    "            \"VMCL\":\n" +
                    "            [\n" +
                    "                {\n" +
                    "                    \"vmcl\": " + vmcl + "\n" +
                    "                    \n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"OrgSignature\":\n" +
                    "            {\n" +
                    "                \"Data\":\"" + Signatures + "\",\n" +
                    "                \"CheckSum\":" + CheckSumSign + "\n" +
                    "                },\n" +
                    "                \"PersonalSignatures\":\n" +
                    "                [\n" +
                    "                    {\n" +
                    "                        \"Signer\":\n" +
                    "                        {\n" +
                    "                            \"LocalId\":\"0001510378\",\n" +
                    "                            \"Role\":{\"$\":\"" + Role + "\",\"@version\":\"2.4\"},\n" +
                    "                            \"LastName\":\"Коситченков\",\n" +
                    "                            \"FirstName\":\"Андрей\",\n" +
                    "                            \"MiddleName\":\"Александрович\",\n" +
                    "                            \"Snils\":\"18259202174\",\n" +
                    "                            \"Position\":{\"$\":\"" + position + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                            \"Speciality\":{\"$\":" + speciality + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                            \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                        },\n" +
                    "                            \"Signature\":\n" +
                    "                            {\n" +
                    "                                \"Data\":\"" + Signatures + "\",\n" +
                    "                                \"CheckSum\":" + CheckSumSign + "\n" +
                    "                            },\n" +
                    "                            \"Description\":\"Подпись сотрудника\"\n" +
                    "                            },\n" +
                    "                            {\n" +
                    "                                \"Signer\":\n" +
                    "                                {\n" +
                    "                                    \"LocalId\":\"0003948083\",\n" +
                    "                                    \"Role\":{\"$\":\"" + Role1 + "\",\"@version\":\"2.4\"},\n" +
                    "                                    \"LastName\":\"Стрекнев\",\n" +
                    "                                    \"FirstName\":\"Денис\",\n" +
                    "                                    \"MiddleName\":\"Юрьевич\",\n" +
                    "                                    \"Snils\":\"18287265004\",\n" +
                    "                                    \"Position\":{\"$\":\"" + position1 + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                                    \"Speciality\":{\"$\":" + speciality1 + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                                    \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                                },\n" +
                    "                                    \"Signature\":\n" +
                    "                                    {\n" +
                    "                                        \"Data\":\"" + Signatures + "\",\n" +
                    "                                        \"CheckSum\":" + CheckSumSign + "\n" +
                    "                                        },\n" +
                    "                                        \"Description\":\"Подпись сотрудника\"\n" +
                    "                                    }\n" +
                    "        ]\n" +
                    "    }";

        }
        if (vmcl == 1 || vmcl == 2 || vmcl == 3 || vmcl == 4) {
            body = "{\n" +
                    "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                    "    \"DocType\":\"" + DocType + "\",\n" +
                    "    \"DocContent\":\n" +
                    "    {\n" +
                    "        \"Document\":\"" + encodedString + "\",\n" +
                    "        \"CheckSum\":" + CheckSum + "\n" +
                    "        },\n" +
                    "        \"LocalUid\":\"" + uuid + "\",\n" +
                    "        \"Payment\":1,\n" +
                    "        \"ReasonForAbsenceIdcase\":\n" +
                    "        {\n" +
                    "            \"CodeSystemVersion\":\"1.1\",\n" +
                    "            \"Code\":10,\n" +
                    "            \"CodeSystem\":\"1.2.643.5.1.13.13.99.2.286\"\n" +
                    "        },\n" +
                    "            \"VMCL\":\n" +
                    "            [\n" +
                    "                {\n" +
                    "                    \"vmcl\": " + vmcl + ",\n" +
                    "                    \"triggerPoint\": 2,\n" +
                    "                    \"docTypeVersion\": " + docTypeVersion + "\n" +
                    "                    \n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"OrgSignature\":\n" +
                    "            {\n" +
                    "                \"Data\":\"" + Signatures + "\",\n" +
                    "                \"CheckSum\":" + CheckSumSign + "\n" +
                    "                },\n" +
                    "                \"PersonalSignatures\":\n" +
                    "                [\n" +
                    "                    {\n" +
                    "                        \"Signer\":\n" +
                    "                        {\n" +
                    "                            \"LocalId\":\"0001510378\",\n" +
                    "                            \"Role\":{\"$\":\"" + Role + "\",\"@version\":\"2.4\"},\n" +
                    "                            \"LastName\":\"Коситченков\",\n" +
                    "                            \"FirstName\":\"Андрей\",\n" +
                    "                            \"MiddleName\":\"Александрович\",\n" +
                    "                            \"Snils\":\"18259202174\",\n" +
                    "                            \"Position\":{\"$\":\"" + position + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                            \"Speciality\":{\"$\":" + speciality + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                            \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                        },\n" +
                    "                            \"Signature\":\n" +
                    "                            {\n" +
                    "                                \"Data\":\"" + Signatures + "\",\n" +
                    "                                  \"CheckSum\":" + CheckSumSign + "\n" +
                    "                            },\n" +
                    "                            \"Description\":\"Подпись сотрудника\"\n" +
                    "                            },\n" +
                    "                            {\n" +
                    "                                \"Signer\":\n" +
                    "                                {\n" +
                    "                                    \"LocalId\":\"0003948083\",\n" +
                    "                                    \"Role\":{\"$\":\"" + Role1 + "\",\"@version\":\"2.4\"},\n" +
                    "                                    \"LastName\":\"Стрекнев\",\n" +
                    "                                    \"FirstName\":\"Денис\",\n" +
                    "                                    \"MiddleName\":\"Юрьевич\",\n" +
                    "                                    \"Snils\":\"18287265004\",\n" +
                    "                                    \"Position\":{\"$\":\"" + position1 + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                                    \"Speciality\":{\"$\":" + speciality1 + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                                    \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                                },\n" +
                    "                                    \"Signature\":\n" +
                    "                                    {\n" +
                    "                                        \"Data\":\"" + Signatures + "\",\n" +
                    "                                       \"CheckSum\":" + CheckSumSign + "\n" +
                    "                                        },\n" +
                    "                                        \"Description\":\"Подпись сотрудника\"\n" +
                    "                                    }\n" +
                    "        ]\n" +
                    "    }";
        }
        return body;
    }

    /**
     * Вычислить количество строк в файле
     * filename - название файла
     */
    public int CountLinesInFile(String filename) throws IOException {
        // 1. Объявить внутренние переменные
        int count = 0; // количество строк в файле - результат
        FileReader fr = null;
        int symbol;
        try {
            // 2. Попытка открыть файл для чтения
            fr = new FileReader(filename);
            // Цикл чтения символов из файла и подсчета их количества
            do {
                // Считать символ из файла
                symbol = fr.read();
                // Проверить, есть ли символ конца строки
                if ((char) symbol == '\n')
                    count++; // Увеличить количество строк в файле на 1
            } while (fr.ready()); // Проверка на конец файла
        } catch (IOException e) {
            // 3. Если файл не открыт, то вывести соответствующее сообщение
            System.out.println("I/O error: " + e);
        } finally {
            // 4. Закрыть файл, если он был открыт
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                System.out.println("File close error.");
            }
        }
        // 5. Вернуть результат
        return count;
    }

    /**
     * Получить строки файла в виде массива String[]
     * filename - название файла
     */
    public String[] GetLinesFromFile(String filename) throws IOException {
        // 1. Объявить внутренние переменные
        int count; // количество строк в файле
        String[] lines = null; // массив строк - результат
        FileReader fr = null;
        String s; // дополнительная переменная - строка
        int symbol;
        int i;
        // 2. Получить кооличество строк в файле - вызвать функцию CountLinesInFile()
        count = CountLinesInFile(filename);
        // 3. Проверка, есть ли в файле строки
        if (count <= 0) return null;
        // 4. Выделить память для count строк
        lines = new String[count];
        // 5. Чтение данных из файла и создание массива lines[]
        try {
            // 5.1. Попытка открыть файл для чтения
            fr = new FileReader(filename);
            // 5.2. Цикл чтения символов из файла и создание массива lines
            s = "";
            i = 0;
            do {
                // Считать символ из файла
                symbol = fr.read();
                // Проверить на символ конца строки
                if (((char) symbol == '\n')) {
                    // удалить из s символ '\n'
                    s = s.substring(0, s.length() - 1);
                    // Добавить в массив строк строку s
                    lines[i] = s;
                    s = "";
                    i++; // Увеличить количество строк в файле на 1
                } else {
                    // добавить символ к строке
                    s = s + (char) symbol;
                }
            } while (fr.ready()); // Проверка на конец файла
        } catch (IOException e) {
            // 5.3. Если файл не открыт, то вывести соответствующее сообщение
            System.out.println("I/O error: " + e);
        } finally {
            // 5.4. Закрыть файл, если он был открыт
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                System.out.println("File close error.");
            }
        }
        // 6. Вернуть результат
        return lines;
    }

    /**
     * Записать массив типа String[] в файл
     * filename - название файла
     * lines - название массива
     */
    public void WriteLinesToFile(String[] lines, String filename) throws IOException {
        // 1. Объявить внутренние переменные
        FileOutputStream fs = null;
        PrintStream ps = null;
        try {
            // 2. Создать экземпляры классов FileOutputStream, PrintStream
            fs = new FileOutputStream(filename); // создать файловый поток
            ps = new PrintStream(fs); // связать файловый поток с потоком вывода PrintStream
            // 3. Цикл записи массива lines[] в файл
            for (int i = 0; i < lines.length; i++)
                ps.println(lines[i]);
        } catch (IOException e) {
            // Если ошибка открытия файла или другая ошибка
            System.out.println("I/O error: " + e);
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e2) {
                    System.out.println("Error closing " + filename);
                }
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Заменить строку в файле в заданной позиции
     * position - позиция с которой нужно заменить строки в файле
     * str - на что нужно замннить
     * filename - название файла
     */
    public boolean ReplaceStringInFile(int position, String str, String filename) throws IOException {
        // Параметры функции:
        // - position - позиция строки в файле;
        // - str - строка, которая заменяет строку в файле;
        // - filename - имя файла, в котором заменяется строка.
        // Если операция успешна, то функция возвращает true.
        // 1. Выполнить необходимые проверки
        // Корректно ли значение position?
        int count = CountLinesInFile(filename); // количество строк в файле
        if ((position < 0) || (position >= count)) return false;
        // 2. Получить список строк файла
        String[] lines = GetLinesFromFile(filename);
        // 3. Заменить строку в позиции position
        lines[position] = str;
        // 4. Записать измененный список строк обратно в файл
        WriteLinesToFile(lines, filename);
        return true;
    }
}
