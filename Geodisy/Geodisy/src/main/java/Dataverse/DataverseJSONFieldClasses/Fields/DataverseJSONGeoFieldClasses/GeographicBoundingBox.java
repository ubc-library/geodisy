package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

public class GeographicBoundingBox extends CompoundJSONField {
    private String westLongitude, eastLongitude, northLongitude, southLongitude;

    public GeographicBoundingBox() {
        this.westLongitude = "";
        this.eastLongitude = "";
        this.northLongitude = "";
        this.southLongitude = "";
    }

    public String getWestLongitude() {
        return westLongitude;
    }

    public void setWestLongitude(String westLongitude) {
        this.westLongitude = westLongitude;
    }

    public String getEastLongitude() {
        return eastLongitude;
    }

    public void setEastLongitude(String eastLongitude) {
        this.eastLongitude = eastLongitude;
    }

    public String getNorthLongitude() {
        return northLongitude;
    }

    public void setNorthLongitude(String northLongitude) {
        this.northLongitude = northLongitude;
    }

    public String getSouthLongitude() {
        return southLongitude;
    }

    public void setSouthLongitude(String southLongitude) {
        this.southLongitude = southLongitude;
    }


    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("westLongitude"):
                setWestLongitude(value);
                break;
            case("eastLongitude"):
                setEastLongitude(value);
                break;
            case("northLongitude"):
                setNorthLongitude(value);
                break;
            case("southLongitude"):
                setSouthLongitude(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String fieldName) {
        switch (fieldName) {
            case("westLongitude"):
                return getWestLongitude();
            case("eastLongitude"):
                return getEastLongitude();
            case("northLongitude"):
                return getNorthLongitude();
            case("southLongitude"):
                return getSouthLongitude();
            default:
                errorGettingValue(this.getClass().getName(),fieldName);
                return "Bad fieldName";
        }
    }
}
