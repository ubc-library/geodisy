package Dataverse;

import BaseFiles.GeoLogger;
import Crosswalking.JSONParsing.DataverseParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.LinkedList;


public class FRDRAPI{
    GeoLogger logger =  new GeoLogger(this.getClass());
    public LinkedList<SourceJavaObject> callFRDRHarvester(){
        String fullJSON = getJson();
        JSONParser jsonParser = new JSONParser();
        LinkedList<SourceJavaObject> djos = new LinkedList<>();
        try {
            JSONObject json = (JSONObject) jsonParser.parse(fullJSON);
            boolean done = json.getBoolean("finished");
            JSONArray records = json.getJSONArray("records");
            DataverseParser parser = new DataverseParser();
            for (Object o: records){
                JSONObject jo = (JSONObject) o;
                djos.add(parser.frdrParse(jo));
                int record_id = jo.getInt("id");
                updateFRDRHarvesterDB(record_id);
            }
            if(!done) {
                djos.addAll(callFRDRHarvester());
            }
        } catch (ParseException e) {
            System.out.println("Something went wrong parsing the FRDR JSON");
            logger.error("Something went wrong parsing the FRDR JSON: \n" + fullJSON);
        }
        return djos;
    }

    private void updateFRDRHarvesterDB(int record_id) {
        try {
            //TODO update url with actual prod url
            String generateWorkspace = "/usr/bin/curl -X PUT -H \"Content-Type: application/json\" -d '{\"geodisy_harvested\":1}' http://test-web-1.frdr.ca:8101/records/" + record_id;
            ProcessBuilder processBuilder= new ProcessBuilder();
            Process p;
            processBuilder.command("/usr/bin/bash", "-c", generateWorkspace);
            p = processBuilder.start();
            p.waitFor();
            p.destroy();
        } catch (InterruptedException| IOException e) {
            logger.error("Something went wrong trying to call FRDR to mark record #" + record_id + " as processed by Geodisy");
        }
    }

    private String getJson() {
        //TODO set this up once Joel has given me the API endpoint to hit
        return "";
    }
}
