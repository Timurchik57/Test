package api;

import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.FileInputStream;
import java.util.Properties;

import static UI.TmTest.BaseTest.InputClassFile;

public class TestListenerApi implements TestWatcher {

    XML xml;
    public String name;

    @SneakyThrows
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        xml = new XML();
        System.out.println(xml.Type);
        xml.ReplacementWordInFileBack(xml.Type);

        FileInputStream in = new FileInputStream("src/test/resources/my.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();
        String str = props.getProperty("IfCountListner");
        System.out.println(str);
        if (props.getProperty("IfCountListner").contains("api") == true) {
            InputClassFile();
        }
    }

    @SneakyThrows
    @Override
    public void testSuccessful(ExtensionContext context) {
        xml = new XML();
        System.out.println(xml.Type);
        xml.ReplacementWordInFileBack(xml.Type);
    }
}
