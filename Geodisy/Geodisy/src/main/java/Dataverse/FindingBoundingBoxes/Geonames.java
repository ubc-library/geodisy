package Dataverse.FindingBoundingBoxes;

import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.City;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import Dataverse.FindingBoundingBoxes.LocationTypes.Province;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Geonames extends FindBoundBox {
    private String USER_NAME = "geodisy";
    Countries countries;
    HashMap<String, BoundingBox> bBoxes;

    public Geonames() {
        this.countries = Countries.getCountry();
        this.bBoxes = countries.getBoundingBoxes();
    }

    @Override
    public BoundingBox getDVBoundingBox(String countryName) {
        Country country;
        if(countries.isCountryCode(countryName))
            country = countries.getCountryByCode(countryName);
         else
             country = countries.getCountryByName(countryName);
        if(country.getCountryCode().matches("_JJ")){
            logger.error(countryName + " is not a valid country name, so no bounding box could be automatically generated");
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
     */
    @Override
    public BoundingBox getDVBoundingBox(String country, String state)  {
        BoundingBox box = countries.get2ParamBB(country, state);
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
        String urlString = state + addParameters(parameters);
        HttpURLConnection con = getHttpURLConnection(urlString);

        try {
            box = readResponse(con);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
        if(box.getLongWest()==361)
            return getDVBoundingBox(country);
        Province p = new Province(unURLedState, country);
        p.setBoundingBox(box);
        bBoxes.put(addDelimiter(country,unURLedState), box);
        countries.setBoundingBoxes(bBoxes);
        return box;
    }



    @Override
    public BoundingBox getDVBoundingBox(String country, String state, String city) {
        BoundingBox box = countries.get3ParamBB(country, state, city);
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
        HttpURLConnection con = getHttpURLConnection(searchString);

        try {
            box = readResponse(con);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
        if(box.getLongWest()==361)
            return getDVBoundingBox(country,state);
        City cit = new City(city,state,country);
        cit.setBoundingBox(box);
        bBoxes.put(addDelimiter(country,unURLedState,unURLedCity),box);
        countries.setBoundingBoxes(bBoxes);
        return box;
    }

    @Override
    public BoundingBox getDVBoundingBoxOther(String country, String other) {
        BoundingBox box =  countries.get2ParamBB(country, other);
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
            box = getDVBoundingBox(country);
        return box;
    }

    @Override
    BoundingBox getDVBoundingBoxOther(String country, String state, String other) {
        BoundingBox box =  countries.get3ParamBB(country, state, other);
        if(box.getLongWest()!=361)
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
            box = getDVBoundingBoxOther(country, other);
        return box;
    }

    @Override
    public HttpURLConnection getHttpURLConnection(String searchValue) {
        try {
            String urlString = "http://api.geonames.org/search?q=" + searchValue;
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            return con;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String addParameters(Map<String, String> parameters) {
        String answer = "";
        for(String s: parameters.keySet()){
            answer+= "&" + s + "=" + parameters.get(s);
        }

        return answer;
    }


}
