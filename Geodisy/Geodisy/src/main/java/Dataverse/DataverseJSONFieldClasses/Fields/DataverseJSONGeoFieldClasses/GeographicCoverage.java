package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.City;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import Dataverse.FindingBoundingBoxes.LocationTypes.Province;
import org.json.JSONObject;

import java.util.*;

import static _Strings.DVFieldNameStrings.*;

public class GeographicCoverage extends CompoundJSONField {
    private String givenCountry, givenProvince, givenCity, otherGeographicCoverage, doi, commonCountry, commonProvince, commonCity;
    private Country countryObject;
    private Province provinceObject;
    private City cityObject;


    public GeographicCoverage(String doi) {
        this.doi = doi;
        this.givenCountry = "";
        this.givenProvince = "";
        this.givenCity = "";
        this.otherGeographicCoverage = "";
        this.commonCountry = "";
        this.commonProvince = "";
        this.commonCity = "";
        this.countryObject = new Country();

    }

    public HashSet<String> getPlaceNames(){
        HashSet<String> placenames = new HashSet<>();
        placenames.add(givenCountry);
        placenames.add(givenProvince);
        placenames.add(givenCity);
        placenames.add(commonCountry);
        placenames.add(commonProvince);
        placenames.add(commonCity);
        placenames.add(otherGeographicCoverage);
        placenames.remove("");
        return placenames;
    }
    public Country getCountryObject(){
        return countryObject;
    }

    public Province getProvinceObject(){
        return provinceObject;
    }

    public City getCityObject(){
        return cityObject;
    }

    public void setGivenCountry(String givenCountry) {
        this.givenCountry = givenCountry;
        countryObject = new Country(givenCountry);
        commonCountry = countryObject.getCommonName();
    }

    public void setGivenProvince(String givenProvince) {
        if(!givenCountry.equals("")) {
            this.givenProvince = givenProvince;
            provinceObject = new Province(this.givenProvince, givenCountry);
            commonProvince = provinceObject.getGivenName();
        }
    }

    public void setGivenCity(String givenCity) {
        if(!(givenCountry.equals("")||givenProvince.equals(""))){
            this.givenCity = givenCity;
            cityObject = new City(this.givenCity, givenProvince, givenCountry);
            commonCity = cityObject.getGivenName();
        }
    }



    @Override
    public void setField(JSONObject field) {
        ArrayList<String> geofields =  new ArrayList<>();
        geofields.add(COUNTRY);
        geofields.add(STATE);
        geofields.add(CITY);
        geofields.add(OTHER_GEO_COV);
        for(String s:geofields){
            if(!field.has(s))
                continue;
            JSONObject fieldTitle = (JSONObject) field.get(s);
            String title = fieldTitle.getString(TYPE_NAME);
            String value = fieldTitle.getString(VAL);
            if(value.startsWith("http"))
                continue;
            switch (title) {
                case COUNTRY:
                    setGivenCountry(value);
                    break;
                case PROVINCE:
                case STATE:
                    setGivenProvince(value);
                    break;
                case CITY:
                    setGivenCity(value);
                    break;
                case OTHER_GEO_COV:
                    this.otherGeographicCoverage = value;
                    break;
                case "":
                    break;
                default:
                    errorParsing(this.getClass().getName(), title);
            }
        }
    }

    @Override
    public String getField(String fieldName) {
        switch (fieldName) {
            case GIVEN_COUNTRY:
                return givenCountry;
            case GIVEN_PROVINCE:
                return givenProvince;
            case GIVEN_CITY:
                return givenCity;
            case OTHER_GEO_COV:
                return otherGeographicCoverage;
            case COMMON_COUNTRY:
                return (commonCountry.isEmpty()? givenCountry:commonCountry);
            case COMMON_PROVINCE:
                return (commonProvince.isEmpty()? givenProvince:commonProvince);
            case COMMON_CITY:
                return (commonCity.isEmpty()? givenCity:commonCity);
            default:
                errorParsing(this.getClass().getName(), fieldName);
                return "Bad FieldName";
        }
    }

    public BoundingBox getBoundingBox(){
        if(this.cityObject!=null){
            if(cityObject.hasBoundingBox()) {
                cityObject.getBoundingBox().setPlace(cityObject.getCommonName());
                return cityObject.getBoundingBox();
            }
        }else if(provinceObject!=null){
            if(provinceObject.hasBoundingBox()) {
                provinceObject.getBoundingBox().setPlace(provinceObject.getCommonName());
                return provinceObject.getBoundingBox();
            }
        }else if(countryObject!=null){
            if(countryObject.hasBoundingBox()) {
                countryObject.getBoundingBox().setPlace(countryObject.getCommonName());
                return countryObject.getBoundingBox();
            }
        }
        return new BoundingBox();
    }
}
