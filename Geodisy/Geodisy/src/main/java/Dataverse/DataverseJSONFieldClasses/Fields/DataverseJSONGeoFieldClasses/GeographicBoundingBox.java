package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

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
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch (title) {
            case WEST_LONG:
                setWestLongitude(value);
                break;
            case EAST_LONG:
                setEastLongitude(value);
                break;
            case NORTH_LAT:
                setNorthLongitude(value);
                break;
            case SOUTH_LAT:
                setSouthLongitude(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String fieldName) {
        switch (fieldName) {
            case WEST_LONG:
                return getWestLongitude();
            case EAST_LONG:
                return getEastLongitude();
            case NORTH_LAT:
                return getNorthLongitude();
            case SOUTH_LAT:
                return getSouthLongitude();
            default:
                errorGettingValue(this.getClass().getName(),fieldName);
                return "Bad fieldName";
        }
    }
}
