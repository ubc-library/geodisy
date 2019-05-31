package Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.Date;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.*;

public class DateOfCollection extends CompoundJSONField {
    private Date dateOfCollectionStart, dateOfCollectionEnd;

    public DateOfCollection() {
        this.dateOfCollectionStart = new Date("6000");
        this.dateOfCollectionEnd = new Date("");
    }

    public String getDateOfCollectionStart() {
        return dateOfCollectionStart.getDateAsString();
    }

    public void setDateOfCollectionStart(String dateOfCollectionStart) {
        this.dateOfCollectionStart = new Date(dateOfCollectionStart);
    }

    public String getDateOfCollectionEnd() {
        return dateOfCollectionEnd.getDateAsString();
    }

    public void setDateOfCollectionEnd(String dateOfCollectionEnd) {
        this.dateOfCollectionEnd = new Date(dateOfCollectionEnd);
    }


    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch(title){
            case DATE_OF_COLLECT_START:
                setDateOfCollectionStart(value);
                break;
            case DATE_OF_COLLECT_END:
                setDateOfCollectionEnd(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String title) {
        switch(title){
            case DATE_OF_COLLECT_START:
                return getDateOfCollectionStart();
            case DATE_OF_COLLECT_END:
                return getDateOfCollectionEnd();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
