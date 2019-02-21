package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.Date;
import org.json.JSONObject;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

public class TimePeriodCovered extends CompoundJSONField {
    private Date timePeriodCoveredStart, timePeriodCoveredEnd;

    public TimePeriodCovered() {
        this.timePeriodCoveredStart = new Date("6000");
        this.timePeriodCoveredEnd = new Date("");
    }

    public String getTimePeriodCoveredStart() {
        return timePeriodCoveredStart.getDateAsString();
    }

    public void setTimePeriodCoveredStart(String timePeriodCoveredStart) {
        this.timePeriodCoveredStart = new Date(timePeriodCoveredStart);
    }

    public String getTimePeriodCoveredEnd() {
        return timePeriodCoveredEnd.getDateAsString();
    }

    public void setTimePeriodCoveredEnd(String timePeriodCoveredEnd) {
        this.timePeriodCoveredEnd = new Date(timePeriodCoveredEnd);
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch (title) {
            case TIME_PER_COV_START:
                setTimePeriodCoveredStart(value);
                break;
            case TIME_PER_COV_END:
                setTimePeriodCoveredEnd(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String fieldName) {
        switch (fieldName) {
            case TIME_PER_COV_START:
                getTimePeriodCoveredStart();
            case TIME_PER_COV_END:
                getTimePeriodCoveredEnd();
            default:
                errorGettingValue(this.getClass().getName(), fieldName);
                return "Bad field name";
        }
    }
}
