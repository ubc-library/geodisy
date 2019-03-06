package Dataverse.FindingBoundingBoxes;

import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Geonames extends FindBoundBox {
    private String USER_NAME = "geodisy";
    @Override
    public BoundingBox getDVBoundingBox(String countryName) {
        Country country = Countries.getCountryByName(countryName);

        return country.getBoundingBox();
    }

    @Override
    public BoundingBox getDVBoundingBox(String country, String state) throws IOException {
        BoundingBox box =  new BoundingBox();
        HttpURLConnection con = getHttpURLConnection(country);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        parameters.put("name","state");

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();
        int status = con.getResponseCode();
        box = readResponse(status,con);
        return box;
    }

    @Override
    public BoundingBox getDVBoundingBox(String country, String state, String city) {
        return null;
    }

    @Override
    public BoundingBox getDVBoundingBox(String country, String state, String city, String other) {
        return null;
    }

    @Override
    public BoundingBox getDVBoundingBoxOther(String other) {
        return null;
    }

    @Override
    public HttpURLConnection getHttpURLConnection(String country) {
        try {
            URL url = new URL("http://api.geonames.org/search?q=" + country );
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
