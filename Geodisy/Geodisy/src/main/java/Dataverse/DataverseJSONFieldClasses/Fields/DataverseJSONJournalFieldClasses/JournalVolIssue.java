package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.Date;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.TYPE_NAME;
import static Dataverse.DVFieldNameStrings.VAL;

public class JournalVolIssue extends CompoundJSONField {
    String journalVolume, journalIssue;
    Date journalPubDate;

    public JournalVolIssue() {
        this.journalVolume = "";
        this.journalIssue = "";
        this.journalPubDate = new Date("9999");
    }

    @Override
    public void setField(JSONObject field) {
        String fieldName = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        setField(fieldName,value);

    }
    //TODO after creating strings
    public void setField(String fieldName, String value) {
    }
    //TODO after creating strings
    @Override
    public String getField(String fieldName) {
        return null;
    }
}
