package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

public class GeographicCoverage extends CompoundJSONField {
    private String country, state, city, otherGeographicCoverage, doi;

    public GeographicCoverage(String doi) {
        this.doi = doi;
        this.country = "";
        this.state = "";
        this.city = "";
        this.otherGeographicCoverage = "";
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOtherGeographicCoverage() {
        return otherGeographicCoverage;
    }

    public void setOtherGeographicCoverage(String otherGeographicCoverage) {
        this.otherGeographicCoverage = otherGeographicCoverage;
    }


    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch (title) {
            case COUNTRY:
               setCountry(value);
                break;
            case STATE:
                setState(value);
                break;
            case CITY:
                setCity(value);
                break;
            case OTHER_GEO_COV:
                setOtherGeographicCoverage(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);

        }
    }

    @Override
    protected String getSpecifiedField(String fieldName) {
        switch (fieldName) {
            case COUNTRY:
                return getCountry();
            case STATE:
                return getState();
            case CITY:
                return getCity();
            case OTHER_GEO_COV:
                return getOtherGeographicCoverage();
            default:
                errorParsing(this.getClass().getName(), fieldName);
                return "Bad FieldName";
        }
    }
}
