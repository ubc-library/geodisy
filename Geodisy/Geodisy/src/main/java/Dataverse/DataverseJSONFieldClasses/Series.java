package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONArray;
import org.json.JSONObject;

public class Series extends JSONField{
    private String seriesName, seriesInformation;

    public Series() {
        this.seriesName = "";
        this.seriesInformation = "";
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesInformation() {
        return seriesInformation;
    }

    public void setSeriesInformation(String seriesInformation) {
        this.seriesInformation = seriesInformation;
    }


    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("seriesName"):
                this.seriesName = value;
                break;
            case("seriesInformation"):
                this.seriesInformation = value;
                break;
            default:
                System.out.println("Something went wrong with Series parsing");
        }
    }
}
