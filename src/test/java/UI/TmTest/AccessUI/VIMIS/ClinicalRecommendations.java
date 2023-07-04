package UI.TmTest.AccessUI.VIMIS;

import UI.SQL;
import UI.TestListener;
import UI.TmTest.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
public class ClinicalRecommendations extends BaseTest {
    AuthorizationObject authorizationObject;
    UI.TmTest.PageObject.VIMIS.ClinicalRecommendations clinicalRec;
    SQL sql;

    @Issue(value = "TEL-569")
    @Owner(value = "Галиакберов Тимур")
    @Description("Авторизация и переход в ВИМИС - Клинические рекомендации. Проверяем отображение у выбранного направления в связке с БД")
    @DisplayName("Проверка  работы фильтра в разделе ВИМИС - Клинические рекомендации")
    @Test
    public void ClinicalRecommendations() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        clinicalRec = new UI.TmTest.PageObject.VIMIS.ClinicalRecommendations(driver);
        sql = new SQL();
        AuthorizationMethod(authorizationObject.MIAC);
        /** ВИМИС - Клинические рекомендации */
        System.out.println("Переход в ВИМИС - Клинические рекомендации");
        WaitElement(clinicalRec.ClinicalRecommendationsWait);
        actionElementAndClick(clinicalRec.ClinicalRecommendations);
        WaitElement(clinicalRec.Header);
        clinicalRec.ChooseDirectionsMethod(clinicalRec.SelectChoseDirections, "Онкология", 1);
        clinicalRec.ChooseDirectionsMethod(clinicalRec.SelectChoseDirectionsPrev, "Профилактика", 2);
        clinicalRec.ChooseDirectionsMethod(clinicalRec.SelectChoseDirectionsAkineo, "Акушерство и неонатология", 3);
        clinicalRec.ChooseDirectionsMethod(clinicalRec.SelectChoseDirectionsCVD, "Сердечно-сосудистые заболевания", 4);
        clinicalRec.ChooseDirectionsMethod(clinicalRec.SelectChoseDirectionsOther, "Иные профили", 99);
    }
}
