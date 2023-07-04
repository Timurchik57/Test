package api;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Clean extends BaseAPI {

    public void CleanMethod(Integer vmcl, String sms, String date, String guid, String CaseId, String remd, String EP, String logs, String info, String indicators, String remdAssociation, String mistakes, String docsexample, String xml_document, String eds) throws SQLException {

        List<String> id = new ArrayList<>();
        List<String> xml_document_id = new ArrayList<>();
        List<String> org_signature_id = new ArrayList<>();

        sql.StartConnection(
                "Select * from " + sms + " where create_date > '" + date + " 00:00:00.346 +0500' and patient_guid = '" + guid + "';");
        while (sql.resultSet.next()) {
            id.add(sql.resultSet.getString("id"));
            xml_document_id.add(sql.resultSet.getString("xml_document_id"));
            org_signature_id.add(sql.resultSet.getString("org_signature_id"));
        }
        for (int i = 0; i < id.size(); i++) {

            System.out.println("Удаление из таблицы " + CaseId + "");
            sql.UpdateConnection("Delete from vimis.oncocaseidabsence where smsid = '" + i + "';");

            System.out.println("Удаление из таблицы " + remd + "");
            sql.UpdateConnection("Delete from vimis.remd_onko_sent_result where sms_id = '" + i + "';");

            System.out.println("Удаление из таблицы " + EP + "");
            sql.UpdateConnection(
                    "Delete from vimis.eds_personal_signature_sms_relations where sms_id = '" + i + "';");

            System.out.println("Удаление из таблицы " + logs + "");
            sql.UpdateConnection("Delete from vimis.documentlogs where sms_id = '" + i + "';");

            System.out.println("Удаление из таблицы " + info + "");
            sql.UpdateConnection("Delete from vimis.additionalinfo where smsid = '" + i + "';");

            System.out.println("Удаление из таблицы " + indicators + "");
            sql.UpdateConnection("Delete from vimis.oncological_indicators where sms_id = '" + i + "';");

            System.out.println("Удаление из таблицы " + remdAssociation + "");
            sql.UpdateConnection("Delete from vimis.remd_onko_associations where sms_id = '" + i + "';");

            System.out.println("Удаление из таблицы " + mistakes + "");
            sql.UpdateConnection("Delete from vimis.clinical_mistakes where sms_id = '" + i + "';");

            System.out.println("Удаление из таблицы " + docsexample + "");
            sql.UpdateConnection("Delete from vimis.docsexample where smsid = '" + i + "';");

            if (vmcl == 1) {
                System.out.println("Удаление из таблицы vimis.onko_sms_v7_register");
                sql.UpdateConnection("Delete from vimis.onko_sms_v7_register where sms_id = '" + sql.value + "';");

                System.out.println("Удаление из таблицы vimis.onco_sms_report_information");
                sql.UpdateConnection(
                        "Delete from vimis.onco_sms_report_information where sms_id = '" + sql.value + "';");
            }
        }

        for (int i = 0; i < xml_document_id.size(); i++) {

            System.out.println("Удаление из таблицы " + xml_document + "");
            sql.UpdateConnection("Delete from vimis.xml_document where id = '" + i + "';");
        }

        for (int i = 0; i < org_signature_id.size(); i++) {

            System.out.println("Удаление из таблицы " + eds + "");
            sql.UpdateConnection("Delete from vimis.eds_signatures id = '" + i + "';");
        }

        for (int i = 0; i < id.size(); i++) {
            System.out.println("Удаление из таблицы " + sms + "");
            sql.UpdateConnection("Delete from " + sms + " where id = '" + i + "';");
        }
    }

    @Test
    public void CleanTest() throws SQLException {
        CleanMethod(1, "vimis.sms", "2023-05-18", "4743e15e-488a-44c6-af50-dff0778dd01a", "vimis.oncocaseidabsence",
                    "vimis.remd_onko_sent_result", "vimis.eds_personal_signature_sms_relations", "vimis.documentlogs",
                    "vimis.additionalinfo", "vimis.oncological_indicators", "vimis.remd_onko_associations",
                    "vimis.clinical_mistakes", "vimis.docsexample", "vimis.xml_document", "vimis.eds_signatures");
    }
}
