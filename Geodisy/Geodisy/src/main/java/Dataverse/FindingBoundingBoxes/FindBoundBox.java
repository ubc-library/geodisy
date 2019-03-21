package Dataverse.FindingBoundingBoxes;

import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;


public abstract class FindBoundBox {
    abstract BoundingBox getDVBoundingBox(String country);
    abstract BoundingBox getDVBoundingBox(String country, String state) throws IOException;
    abstract BoundingBox getDVBoundingBox(String country, String state, String city);
    abstract BoundingBox getDVBoundingBoxOther(String country, String other);
    abstract BoundingBox getDVBoundingBoxOther(String country,String state, String other);
    abstract HttpURLConnection getHttpURLConnection(String country);
    Logger logger = LogManager.getLogger(FindBoundBox.class.getName());
    Logger checkRecordLogger = LogManager.getLogger(FindBoundBox.class);
    //TODO get HTTP response (XML) and parse for boundingbox coordinates
    BoundingBox readResponse(HttpURLConnection con) throws IOException{
        con.setDoOutput(true);
        /*DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();*/
        BoundingBox box = new BoundingBox();
        int responseCode = 0;
        try {
            responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                String responseString = response.toString();
                System.out.println(responseString);
                box = parseXMLString(responseString);
            } else {
                System.out.println("GET request not worked");
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Something went wrong getting a bounding box from an external source");
        }
        return box;
    }

    protected  BoundingBox parseXMLString(String responseString){
        BoundingBox box = new BoundingBox();
        int start = responseString.indexOf("<west>");
        if(start==-1)
            return box;
        int end = responseString.indexOf("</west>");
        box.setLongWest(responseString.substring(start+6, end));
        start = responseString.indexOf("<east>");
        end = responseString.indexOf("</east>");
        box.setLongEast(responseString.substring(start+6, end));
        start = responseString.indexOf("<north>");
        end = responseString.indexOf("</north>");
        box.setLatNorth(responseString.substring(start+7, end));
        start = responseString.indexOf("<south>");
        end = responseString.indexOf("</south>");
        box.setLatSouth(responseString.substring(start+7, end));

        return box;
    }

    protected String addDelimiter(String country, String secondParam) {
        return country + "zzz"+ secondParam;
    }

    protected String addDelimiter(String country, String secondParam, String thirdParam) {
        return country + "zzz"+ secondParam + "zzz" + thirdParam;
    }

}