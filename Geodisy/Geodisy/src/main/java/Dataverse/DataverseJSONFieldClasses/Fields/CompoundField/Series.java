package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

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
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch (title) {
            case SERIES_NAME:
                setSeriesName(value);
                break;
            case SERIES_INFO:
                setSeriesInformation(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch (title) {
            case SERIES_NAME:
                return getSeriesName();
            case SERIES_INFO:
                return getSeriesInformation();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
