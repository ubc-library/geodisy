package Dataverse.FindingBoundingBoxes;

import BaseFiles.HTTPCaller;
import Dataverse.DataverseJavaObject;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.City;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import Dataverse.FindingBoundingBoxes.LocationTypes.Province;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Geonames collects a bounding box for a record that has enough geospatial fields filled out.
 * If just country is available then a local file with all the country bounding boxes is used.
 * If country and state/province or country state/province and city are provided, a call to the geonames.org API is
 * made to get a bounding box for that place. If no bounding box can be found, then a bounding box with all lat/longs
 * set to 361 is used.
 */
public class Geonames extends FindBoundBox {
    private String USER_NAME = "geodisy";
    Countries countries;
    HashMap<String, BoundingBox> bBoxes;
    String doi;
    public DataverseJavaObject djo;

    public Geonames(String doi) {
        this.doi = doi;
        this.countries = Countries.getCountry();
        this.bBoxes = countries.getBoundingBoxes();
    }

    public Geonames(DataverseJavaObject djo){
        this.djo = djo;
        doi = this.djo.getDOI();
        this.countries = Countries.getCountry();
        this.bBoxes = countries.getBoundingBoxes();
    }

    /**
     *
     * @param countryName
     * Country name is compared to list of country bounding boxes in Geonames_countries.xml
     * @return value from xml file, or a 361 lat/long bounding box if the country can't be found
     */
    @Override
    public BoundingBox getDVBoundingBox(String countryName) {
        Country country;
        if(countries.isCountryCode(countryName))
            country = countries.getCountryByCode(countryName);
         else
             country = countries.getCountryByName(countryName);
        if(country.getCountryCode().matches("_JJ")){
            logger.info(countryName + " is not a valid country name in record at DOI: " + doi + ", so no bounding box could be automatically generated. Check this record manually ", djo, logger.getName());

            return new BoundingBox();
        }
        return country.getBoundingBox();
    }

    /**
     * Get the bounding box from Geonames when Country and State/Province/Etc is known
     * For other Geonames Feature Codes [fcode] see: https://www.geonames.org/export/codes.html
     * @param country
     * @param state
     * @return a Bounding box with N/S/E/W extents
     *
     * If bounding box can't be found in geonames, return a search for just the country.
     */
    @Override
    public BoundingBox getDVBoundingBox(String country, String state)  {
        BoundingBox box = countries.getExistingBB(country, state);
        if(box.getLongWest()!=361)
            return box;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        parameters.put("maxRows","1");
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryCode(country);
        parameters.put("country",countryCode);
        parameters.put("fcode","ADM*");
        String unURLedState =state;
        try {
            state = URLEncoder.encode(state, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }
        String searchString = state + addParameters(parameters);
        box = readResponse(getJSONString(searchString),doi, djo);
        if(box.getLongWest()==361)
            return getDVBoundingBox(country);
        Province p = new Province(unURLedState, country);
        p.setBoundingBox(box);
        bBoxes.put(addDelimiter(country,unURLedState), box);
        countries.setBoundingBoxes(bBoxes);
        return box;
    }


    /**
     *
     * Get the bounding box from Geonames when Country, State/Province/Etc, and city are known
     *      * For other Geonames Feature Codes [fcode] see: https://www.geonames.org/export/codes.html
     * @param country
     * @param state
     * @param city
     * @return a Bounding box with N/S/E/W extents
     *
     * If bounding box cannot be found in geonames, run search with just country and state/province
     */

    @Override
    public BoundingBox getDVBoundingBox(String country, String state, String city) {
        BoundingBox box = countries.getExistingBB(country, state, city);
        if(box.getLongWest()!=361)
            return box;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryCode(country);
        parameters.put("country",countryCode);
        parameters.put("fcode","PPL*");
        String unURLedState = state;
        String unURLedCity = city;
        try {
            city = URLEncoder.encode(city, "UTF-8");
            state = URLEncoder.encode(state, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }
        String searchString = city + "%2C%20" + state + addParameters(parameters);
            box = readResponse(getJSONString(searchString),doi, djo);
        if(box.getLongWest()==361)
            return getDVBoundingBox(country,state);
        City cit = new City(city,state,country);
        cit.setBoundingBox(box);
        bBoxes.put(addDelimiter(country,unURLedState,unURLedCity),box);
        countries.setBoundingBoxes(bBoxes);

        return box;
    }

    /**
     *
     * @param country
     * @param other
     * @return a bounding box with likely no real values
     *
     * We decided that if the Other Geographic Coverage is filled in at all that an actual human needs to look into
     * the geographic coverage.
     */
    @Override
    public BoundingBox getDVBoundingBoxOther(String country, String other) {
        BoundingBox box =  countries.getExistingBB(country, other);

        /*
        if(box.getLongWest()!=361)
            return box;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryCode(country);
        parameters.put("country",countryCode);
        String unURLedOther = other;
        try {
            other = URLEncoder.encode(other, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }
        String urlString  = other + addParameters(parameters);
        HttpURLConnection con = getHttpURLConnection(urlString);

        try {
            box = readResponse(con);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
        if(box.getLongWest()!=361)
            bBoxes.put(addDelimiter(country,unURLedOther),box);
        else
            box = getDVBoundingBox(country);*/
        if(box.getLatSouth() == 361)
            logger.info("Record with DOI: " + doi + " has something in the 'Other Geographic Coverage' field so needs to be inspected" ,djo, logger.getName());
        return box;

    }
    /**
     *
     * @param country
     * @param state
     * @param other
     * @return a bounding box with likely no real values
     *
     * We decided that if the Other Geographic Coverage is filled in at all that an actual human needs to look into
     * the geographic coverage.
     */
    @Override
    BoundingBox getDVBoundingBoxOther(String country, String state, String other) {
        BoundingBox box =  countries.getExistingBB(country, state, other);
        /*if(box.getLongWest()!=361)
            return box;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryCode(country);
        parameters.put("country",countryCode);
        String unURLedOther = other;
        String unURLedState = state;
        try {
            other = URLEncoder.encode(other, "UTF-8");
            state = URLEncoder.encode(state, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }
        String urlString  = other + "%2C%20" + state + addParameters(parameters);
        HttpURLConnection con = getHttpURLConnection(urlString);

        try {
            box = readResponse(con);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
        if(box.getLongWest()!=361)
            bBoxes.put(addDelimiter(country,unURLedState,unURLedOther),box);
        else
            box = getDVBoundingBoxOther(country, other);*/
        if(box.getLatSouth() == 361)
            logger.info("Record with DOI: " + doi + " has something in the 'Other Geographic Coverage' field so needs to be inspected" ,djo, logger.getName());
        return box;
    }

    @Override
    public String getJSONString(String searchValue) {

        String urlString = "http://api.geonames.org/search?q=" + searchValue;
        HTTPCaller caller = new HTTPCaller(urlString);
        return caller.getJSONString();
    }

    private String addParameters(Map<String, String> parameters) {
        String answer = "";
        for(String s: parameters.keySet()){
            answer+= "&" + s + "=" + parameters.get(s);
        }

        return answer;
    }


}
