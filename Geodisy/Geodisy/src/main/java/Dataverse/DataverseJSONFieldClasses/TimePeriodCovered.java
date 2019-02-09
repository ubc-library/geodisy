package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONObject;

public class TimePeriodCovered extends JSONField{
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
                this.timePeriodCoveredStart = value;
                break;
            case("timePeriodCoverEnd"):
                this.timePeriodCoveredEnd = value;
                break;
            default:
                System.out.println("Something went wrong with Time Period Cover parsing");
        }
    }
}
