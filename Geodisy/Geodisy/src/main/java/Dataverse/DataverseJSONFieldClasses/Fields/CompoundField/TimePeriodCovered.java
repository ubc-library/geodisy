package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.Date;
import org.json.JSONObject;

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
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("timePeriodCoverStart"):
                setTimePeriodCoveredStart(value);
                break;
            case("timePeriodCoverEnd"):
                setTimePeriodCoveredEnd(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String fieldName) {
        switch (fieldName) {
            case ("timePeriodCoverStart"):
                getTimePeriodCoveredStart();
            case ("timePeriodCoverEnd"):
                getTimePeriodCoveredEnd();
            default:
                errorGettingValue(this.getClass().getName(), fieldName);
                return "Bad field name";
        }
    }
}
