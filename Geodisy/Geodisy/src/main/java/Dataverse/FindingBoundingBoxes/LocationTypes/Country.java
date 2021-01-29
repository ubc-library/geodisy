package Dataverse.FindingBoundingBoxes.LocationTypes;


import Dataverse.FindingBoundingBoxes.Location;
import org.w3c.dom.Element;

public class Country extends Location {
    private String countryCode = "";

    public Country(Element country, String commonName) {
        super(commonName, country);
        this.commonName = this.geonamesJSON.getCommonCountryName();
        countryCode = geonamesJSON.getCountryCode();


    }
    public Country(String countryName){
        super(countryName);
    }


    //Placeholder country constructor
    public Country(){
        super();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }



}
