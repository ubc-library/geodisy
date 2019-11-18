package Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields;

import Dataverse.DataverseJSONFieldClasses.CompoundDateJSONField;
import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.Date;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.*;

public class DateOfCollection extends CompoundDateJSONField {
    private Date dateOfCollectionStart, dateOfCollectionEnd;

    public DateOfCollection() {
        this.dateOfCollectionStart = new Date("6000");
        this.dateOfCollectionEnd = new Date("");
    }
    @Override
    public String getStartDate() {
        return dateOfCollectionStart.getDateAsString();
    }
    @Override
    public void setStartDate(String dateOfCollectionStart) {
        this.dateOfCollectionStart = new Date(dateOfCollectionStart);
    }
    @Override
    public String getEndDate() {
        return dateOfCollectionEnd.getDateAsString();
    }

    @Override
    public void setEndDate(String dateOfCollectionEnd) {
        this.dateOfCollectionEnd = new Date(dateOfCollectionEnd);
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch(title){
            case DATE_OF_COLLECT_START:
                setStartDate(value);
                break;
            case DATE_OF_COLLECT_END:
                setEndDate(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String title) {
        switch(title){
            case DATE_OF_COLLECT_START:
                return getStartDate();
            case DATE_OF_COLLECT_END:
                return getEndDate();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
