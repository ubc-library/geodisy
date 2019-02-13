package Dataverse.DataverseJSONFieldClasses;

import Crosswalking.JSONParsing.DataverseParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
                this.dateOfCollectionStart = DataverseParser.filterForDate(value);
                break;
            case("dateOfCollectionEnd"):
                this.dateOfCollectionEnd = DataverseParser.filterForDate(value);
                break;
            default:
                logger.error("Something wrong parsing Date of Collection. Title is %s", title);
                System.out.println("Something wrong with Date of Collection parsing");
        }
    }
}
