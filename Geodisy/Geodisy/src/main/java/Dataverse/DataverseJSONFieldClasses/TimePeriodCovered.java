package Dataverse.DataverseJSONFieldClasses;

import Crosswalking.JSONParsing.DataverseParser;
import DataSourceLocations.Dataverse;
import org.json.JSONObject;

public class TimePeriodCovered extends CompoundJSONField{
    private String timePeriodCoveredStart, timePeriodCoveredEnd;

    public TimePeriodCovered() {
        this.timePeriodCoveredStart = "";
        this.timePeriodCoveredEnd = "";
    }

    public String getTimePeriodCoveredStart() {
        return timePeriodCoveredStart;
    }

    public void setTimePeriodCoveredStart(String timePeriodCoveredStart) {
        this.timePeriodCoveredStart = timePeriodCoveredStart;
    }

    public String getTimePeriodCoveredEnd() {
        return timePeriodCoveredEnd;
    }

    public void setTimePeriodCoveredEnd(String timePeriodCoveredEnd) {
        this.timePeriodCoveredEnd = timePeriodCoveredEnd;
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("timePeriodCoverStart"):
                this.timePeriodCoveredStart = filterForDate(value);
                break;
            case("timePeriodCoverEnd"):
                this.timePeriodCoveredEnd = filterForDate(value);
                break;
            default:
                logger.error("Something wrong parsing Time Period Covered. Title is %s", title);
                System.out.println("Something went wrong with Time Period Cover parsing");
        }
    }
}
