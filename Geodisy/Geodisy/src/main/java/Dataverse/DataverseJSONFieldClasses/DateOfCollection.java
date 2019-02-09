package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONArray;
import org.json.JSONObject;

public class DateOfCollection extends JSONField{
    private String dateOfCollectionStart, dateOfCollectionEnd;

    public DateOfCollection() {
        this.dateOfCollectionStart = "";
        this.dateOfCollectionEnd = "";
    }

    public String getDateOfCollectionStart() {
        return dateOfCollectionStart;
    }

    public void setDateOfCollectionStart(String dateOfCollectionStart) {
        this.dateOfCollectionStart = dateOfCollectionStart;
    }

    public String getDateOfCollectionEnd() {
        return dateOfCollectionEnd;
    }

    public void setDateOfCollectionEnd(String dateOfCollectionEnd) {
        this.dateOfCollectionEnd = dateOfCollectionEnd;
    }


    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch(title){
            case("dateOfCollectionStart"):
                this.dateOfCollectionStart = value;
                break;
            case("dateOfCollectionEnd"):
                this.dateOfCollectionEnd = value;
                break;
            default:
                System.out.println("Something wrong with Date of Collection parsing");
        }
    }
}
