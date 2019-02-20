package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.Date;
import org.json.JSONObject;

public class DateOfCollection extends CompoundJSONField {
    private Date dateOfCollectionStart, dateOfCollectionEnd;

    public DateOfCollection(String start, String end) {
        this.dateOfCollectionStart = new Date(start);
        this.dateOfCollectionEnd = new Date(end);
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
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch(title){
            case("dateOfCollectionStart"):
                setDateOfCollectionStart(value);
                break;
            case("dateOfCollectionEnd"):
                setDateOfCollectionEnd(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch(title){
            case("dateOfCollectionStart"):
                return getDateOfCollectionStart();
            case("dateOfCollectionEnd"):
                return getDateOfCollectionEnd();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
