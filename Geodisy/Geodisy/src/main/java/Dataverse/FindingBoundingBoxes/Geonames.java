package Dataverse.FindingBoundingBoxes;

import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import com.sun.deploy.net.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Geonames extends FindBoundBox {
    private String USER_NAME = "geodisy";
    @Override
    public BoundingBox getDVBoundingBox(String countryName) {
        Country country = Countries.getCountryByName(countryName);
        if(country.getCountryCode().matches("_JJ")){
            logger.error(countryName + " is not a valid country name, so no bounding box could be automatically generated");
        }
        return country.getBoundingBox();
    }

    @Override
    public BoundingBox getDVBoundingBox(String country, String state)  {
        BoundingBox box =  new BoundingBox();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        parameters.put("name",state);
        HttpURLConnection con = getHttpURLConnection(country, parameters);

        try {
            box = readResponse(con, parameters);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
        return box;
    }

    @Override
    public BoundingBox getDVBoundingBox(String country, String state, String city) {
        BoundingBox box =  new BoundingBox();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        parameters.put("name",city+ "%2C%20" + state);
        HttpURLConnection con = getHttpURLConnection(country, parameters);

        try {
            box = readResponse(con, parameters);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
        return box;
    }

    @Override
    public BoundingBox getDVBoundingBox(String country, String state, String city, String other) {
        BoundingBox box =  new BoundingBox();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        parameters.put("name",other);
        HttpURLConnection con = getHttpURLConnection(country, parameters);

        try {
            box = readResponse(con, parameters);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
        return box;
    }

    @Override
    public BoundingBox getDVBoundingBoxOther(String other) {
        return null;
    }

    @Override
    public HttpURLConnection getHttpURLConnection(String country, Map parameters) {
        try {
            String urlString = "http://api.geonames.org/search?q=" + country;
            Iterator it = parameters.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                urlString = urlString + "&" + pair.getKey() + "=" + pair.getValue();
            }
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
