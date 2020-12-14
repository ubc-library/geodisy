package Dataverse;

import Crosswalking.JSONParsing.DataverseParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.LinkedList;

public class FRDRAPI{
    public LinkedList<SourceJavaObject> callFRDRHarvester(){
        boolean done = true;
        String fullJSON = getJson();
        JSONParser jsonParser = new JSONParser();
        JSONObject json = new JSONObject();
        LinkedList<SourceJavaObject> djos = new LinkedList<>();
        try {
            json = (JSONObject) jsonParser.parse(fullJSON);
            done = json.getBoolean("finished");
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
        }
        return djos;
    }

    private void updateFRDRHarvesterDB(int record_id) {
        //TODO set this up once Joel has given me the API endpoint to hit
    }

    private String getJson() {
        //TODO set this up once Joel has given me the API endpoint to hit
        return "";
    }
}
