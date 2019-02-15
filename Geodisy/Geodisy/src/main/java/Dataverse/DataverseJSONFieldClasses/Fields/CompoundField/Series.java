package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

public class Series extends CompoundJSONField {
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
                logger.error("Something wrong parsing Series. Title is %s", title);
                System.out.println("Something went wrong with Series parsing");
        }
    }
}
