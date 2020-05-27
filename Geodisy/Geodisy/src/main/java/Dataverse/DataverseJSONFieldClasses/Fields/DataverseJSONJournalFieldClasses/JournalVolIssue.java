package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.Date;
import org.json.JSONObject;

import static _Strings.DVFieldNameStrings.*;

public class JournalVolIssue extends CompoundJSONField {
    private String journalVolume, journalIssue;
    private Date journalPubDate;

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
    public void setField(String fieldName, String value) {
        switch (value) {
            case JOURNAL_VOLUME: journalVolume = value; break;
            case JOURNAL_ISSUE: journalIssue = value; break;
            case JOURNAL_PUB_DATE: journalPubDate =  new Date(value); break;
            default: errorParsing(this.getClass().toString(),fieldName);
        }
    }
    @Override
    public String getField(String fieldName) {
        switch (fieldName) {
            case JOURNAL_VOLUME: return journalVolume;
            case JOURNAL_ISSUE: return journalIssue;
            case JOURNAL_PUB_DATE: return journalPubDate.getDateAsString();
            default:
                errorGettingValue(this.getClass().toString(), fieldName);
                return "Error getting value from field: " + fieldName;
        }
    }
}
