package Dataverse.FindingBoundingBoxes.LocationTypes;


import Dataverse.FindingBoundingBoxes.CountryCodes;
import Dataverse.FindingBoundingBoxes.Location;

public class Country extends Location {
    private String countryCode;

    public Country(String country) {
        super(country);
        this.countryCode = CountryCodes.getCountryCode(country);

    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
