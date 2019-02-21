package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

public class GeographicCoverage extends CompoundJSONField {
    private String country, state, city, otherGeographicCoverage;

    public GeographicCoverage() {
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
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("country"):
               setCountry(value);
                break;
            case("state"):
                setState(value);
                break;
            case("city"):
                setCity(value);
                break;
            case("otherGeographicCoverage"):
                setOtherGeographicCoverage(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);

        }
    }

    @Override
    protected String getSpecifiedField(String fieldName) {
        switch (fieldName) {
            case ("country"):
                return getCountry();
            case ("state"):
                return getState();
            case ("city"):
                return getCity();
            case ("otherGeographicCoverage"):
                return getOtherGeographicCoverage();
            default:
                errorParsing(this.getClass().getName(), fieldName);
                return "Bad FieldName";
        }
    }
}
