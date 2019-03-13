package Dataverse.FindingBoundingBoxes;

import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.City;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import Dataverse.FindingBoundingBoxes.LocationTypes.Province;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Geonames extends FindBoundBox {
    private String USER_NAME = "geodisy";
    Countries countries;
    HashMap<String, BoundingBox> bBoxes;
    @Override
    public BoundingBox getDVBoundingBox(String countryName) {
        Country country;
        Countries countries = Countries.getCountry();
        bBoxes = countries.getBoundingBoxes();
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
        Countries countries = Countries.getCountry();
        BoundingBox existing = countries.getProvincialBB(country, state);
        if(existing.getLongWest()!=361)
            return existing;
        BoundingBox box =  new BoundingBox();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        parameters.put("maxRows","1");
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryCode(country);
        parameters.put("country",countryCode);
        parameters.put("fcode","ADM*");
        HttpURLConnection con = getHttpURLConnection(state);

        try {
            box = readResponse(con, parameters);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
        if(box.getLongWest()==361)
            return getDVBoundingBox(country);
        Province p = new Province(state, country);
        p.setBoundingBox(box);
        bBoxes.put(country+state, box);
        countries.setBoundingBoxes(bBoxes);
        return box;
    }
    //TODO finish this method to update permanent records
    @Override
    public BoundingBox getDVBoundingBox(String country, String state, String city) {
        BoundingBox box =  new BoundingBox();
        if(bBoxes.containsKey(country+state+city))
            return bBoxes.get(country+state+city);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryCode(country);
        parameters.put("country",countryCode);
        parameters.put("fcode","PPL*");
        String searchString = city+ "%2C%20" + state;
        HttpURLConnection con = getHttpURLConnection(searchString);

        try {
            box = readResponse(con, parameters);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
        if(box.getLongWest()==361)
            return getDVBoundingBox(country,state);
        City cit = new City(city,state,country);
        cit.setBoundingBox(box);
        bBoxes.put(country+state+city,box);
        countries.setBoundingBoxes(bBoxes);
        return box;
    }

    @Override
    public BoundingBox getDVBoundingBoxOther(String country, String other) {
        BoundingBox box =  new BoundingBox();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        parameters.put("name",other);
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryCode(country);
        parameters.put("country",countryCode);
        HttpURLConnection con = getHttpURLConnection(other);

        try {
            box = readResponse(con, parameters);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
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


}
