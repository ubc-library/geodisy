package Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields;

import Dataverse.DataverseJSONFieldClasses.CompoundDateJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.Date;
import org.json.JSONObject;

import static Strings.DVFieldNameStrings.*;

public class TimePeriodCovered extends CompoundDateJSONField {
    private Date timePeriodCoveredStart, timePeriodCoveredEnd;

    public TimePeriodCovered() {
        this.timePeriodCoveredStart = new Date("6000");
        this.timePeriodCoveredEnd = new Date("");
    }
    @Override
    public String getStartDate() {
        return timePeriodCoveredStart.getDateAsString();
    }
    @Override
    public void setStartDate(String timePeriodCoveredStart) {
        this.timePeriodCoveredStart = new Date(timePeriodCoveredStart);
    }
    @Override
    public String getEndDate() {
        return timePeriodCoveredEnd.getDateAsString();
    }
    @Override
    public void setEndDate(String timePeriodCoveredEnd) {
        this.timePeriodCoveredEnd = new Date(timePeriodCoveredEnd);
    }


    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch (title) {
            case TIME_PER_COV_START:
                setStartDate(value);
                break;
            case TIME_PER_COV_END:
                setEndDate(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String fieldName) {
        switch (fieldName) {
            case TIME_PER_COV_START:
                getStartDate();
            case TIME_PER_COV_END:
                getEndDate();
            default:
                errorGettingValue(this.getClass().getName(), fieldName);
                return "Bad field name";
        }
    }
}
