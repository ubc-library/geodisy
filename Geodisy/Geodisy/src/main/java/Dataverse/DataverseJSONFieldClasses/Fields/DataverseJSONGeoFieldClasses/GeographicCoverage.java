package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import BaseFiles.Geonames;
import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.FindingBoundingBoxes.LocationTypes.City;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import Dataverse.FindingBoundingBoxes.LocationTypes.Province;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static Dataverse.DVFieldNameStrings.*;

public class GeographicCoverage extends CompoundJSONField {
    private String country, province, city, otherGeographicCoverage, doi, altCountry, altProvince, altCity;
    private Country countryObject;
    private Province provinceObject;
    private City cityObject;
    Geonames geo = new Geonames();

    public GeographicCoverage(String doi) {
        this.doi = doi;
        this.country = "";
        this.province = "";
        this.city = "";
        this.otherGeographicCoverage = "";
        this.altCountry = "";
        this.altProvince = "";
        this.altCity = "";
        this.countryObject = new Country("");

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
    private List<String> getGeoCoverageField(String name, String altName){
        String[] array;
        if(name.equalsIgnoreCase(altName))
            array = new String[]{name};
        else
            array = new String[]{name,altName};
        List answer = Arrays.asList(array);
        return answer;
    }
    public List<String> getCountryList() {
        return (country.isEmpty() ? new LinkedList<String>() : getGeoCoverageField(country,altCountry));
    }

    public void setCountry(String country) {
        this.country = country;
        countryObject = new Country(country);
        altCountry = countryObject.getGivenName();
    }

    public List<String> getProvinceList() {
        return (province.isEmpty() ? new LinkedList<String>() : getGeoCoverageField(province,altProvince));
    }

    public void setProvince(String province) {
        this.province = province;
        provinceObject =  new Province(this.province,country);
        altProvince = provinceObject.getGivenName();
    }

    public List<String> getCityList() {
        return (city.isEmpty() ? new LinkedList<String>() : getGeoCoverageField(city,altCity));
    }

    public void setCity(String city) {
        this.city = city;
        cityObject = new City(this.city,province,country);
        altCity = cityObject.getGivenName();
    }

    public List<String> getOtherGeographicCoverage() {
        return Arrays.asList(otherGeographicCoverage);
    }

    public void setOtherGeographicCoverage(String otherGeographicCoverage) {
        this.otherGeographicCoverage = otherGeographicCoverage;
    }


    @Override
    public void setField(JSONObject field) {
        for(String s:field.keySet()){
            JSONObject fieldTitle = (JSONObject) field.get(s);
            String title = fieldTitle.getString(TYPE_NAME);
            String value = fieldTitle.getString(VAL);
            switch (title) {
                case COUNTRY:
                    setCountry(value);
                    break;
                case STATE:
                    setProvince(value);
                    break;
                case CITY:
                    setCity(value);
                    break;
                case OTHER_GEO_COV:
                    setOtherGeographicCoverage(value);
                    break;
                default:
                    errorParsing(this.getClass().getName(), title);
            }
        }
    }

    @Override
    public String getField(String fieldName) {
        return null;
    }


    public List<String> getListField(String fieldName) {
        switch (fieldName) {
            case COUNTRY:
                return getCountryList();
            case STATE:
                return getProvinceList();
            case CITY:
                return getCityList();
            case OTHER_GEO_COV:
                return getOtherGeographicCoverage();
            default:
                errorParsing(this.getClass().getName(), fieldName);
                return Arrays.asList("Bad FieldName");
        }
    }
}
