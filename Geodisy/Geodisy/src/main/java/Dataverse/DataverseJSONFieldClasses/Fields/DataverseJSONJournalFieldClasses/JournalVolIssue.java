package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.Date;
import org.json.JSONObject;

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

    }

    @Override
    public String getField(String fieldName) {
        return null;
    }
}
