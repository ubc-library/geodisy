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
                setSeriesName(value);
                break;
            case("seriesInformation"):
                setSeriesInformation(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch (title) {
            case("seriesName"):
                return getSeriesName();
            case("seriesInformation"):
                return getSeriesInformation();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
