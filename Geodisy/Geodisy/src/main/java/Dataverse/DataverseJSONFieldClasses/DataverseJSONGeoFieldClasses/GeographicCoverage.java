package Dataverse.DataverseJSONFieldClasses.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.JSONField;
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
                this.country = value;
                break;
            case("state"):
                this.state = value;
                break;
            case("city"):
                this.city = value;
                break;
            case("otherGeographicCoverage"):
                this.otherGeographicCoverage = value;
                break;
            default:
                logger.error("Something wrong parsing Geographic Coverage. Title is %s", title);
                System.out.println("Something went wrong with Geographic Coverage parsing");

        }
    }
}
