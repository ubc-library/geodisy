package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;
import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.OTHER_GEO_COV;

public class GeographicFields extends CompoundJSONField {
    List<GeographicCoverage> geoCovers;
    List<GeographicBoundingBox> geoBBoxes;

    public GeographicFields() {
        this.geoCovers = new LinkedList<>();
        this.geoBBoxes = new LinkedList<>();
    }

    @Override
    protected void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        switch (title) {
            case WEST_LONG:
                geoBBox.setField(field);
                break;
            case EAST_LONG:
                geoBBox.setField(field);
                break;
            case NORTH_LAT:
                geoBBox.setField(field);
                break;
            case SOUTH_LAT:
                geoBBox.setField(field);
                break;
            case COUNTRY:
                geoCover.setField(field);
                break;
            case STATE:
                geoCover.setField(field);
                break;
            case CITY:
                geoCover.setField(field);
                break;
            case OTHER_GEO_COV:
                geoCover.setField(field);
                break;
            default:
                errorParsing(this.getClass().getName(), title);
        }
    }

    @Override
    protected String getSpecifiedField(String fieldName) {
        switch (fieldName) {
            case COUNTRY:
                return geoCover.getSpecifiedField(fieldName);
            case STATE:
                return geoCover.getSpecifiedField(fieldName);
            case CITY:
                return geoCover.getSpecifiedField(fieldName);
            case OTHER_GEO_COV:
                return geoCover.getSpecifiedField(fieldName);
            case WEST_LONG:
                return geoBBox.getSpecifiedField(fieldName);
            case EAST_LONG:
                return geoBBox.getSpecifiedField(fieldName);
            case NORTH_LAT:
                return geoBBox.getSpecifiedField(fieldName);
            case SOUTH_LAT:
                return geoBBox.getSpecifiedField(fieldName);
            default:
                errorParsing(this.getClass().getName(), fieldName);
                return "Bad FieldName";
        }
    }

    public GeographicCoverage getGeoCover() {
        return geoCover;
    }

    public void setGeoCover(GeographicCoverage geoCover) {
        this.geoCover = geoCover;
    }

    public GeographicBoundingBox getGeoBBox() {
        return geoBBox;
    }

    public void setGeoBBox(GeographicBoundingBox geoBBox) {
        this.geoBBox = geoBBox;
    }
}
