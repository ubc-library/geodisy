package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

public class GeographicBoundingBox extends CompoundJSONField {
    private String westLongitude, eastLongitude, northLatitude, southLatitude;

    public GeographicBoundingBox() {
        this.westLongitude = "";
        this.eastLongitude = "";
        this.northLatitude = "";
        this.southLatitude = "";
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

    public String getNorthLatitude() {
        return northLatitude;
    }

    public void setNorthLatitude(String northLatitude) {
        this.northLatitude = northLatitude;
    }

    public String getSouthLatitude() {
        return southLatitude;
    }

    public void setSouthLatitude(String southLatitude) {
        this.southLatitude = southLatitude;
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
                setNorthLatitude(value);
                break;
            case SOUTH_LAT:
                setSouthLatitude(value);
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
                return getNorthLatitude();
            case SOUTH_LAT:
                return getSouthLatitude();
            default:
                errorGettingValue(this.getClass().getName(),fieldName);
                return "Bad fieldName";
        }
    }
}
