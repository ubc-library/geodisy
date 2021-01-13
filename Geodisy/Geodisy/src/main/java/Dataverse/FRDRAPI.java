package Dataverse;

import BaseFiles.GeoLogger;
import Crosswalking.JSONParsing.DataverseParser;
import org.json.JSONException;
import org.json.JSONTokener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

import static _Strings.GeodisyStrings.EXISTING_BBOXES;


public class FRDRAPI extends SourceAPI{
    GeoLogger logger =  new GeoLogger(this.getClass());

    // Calls "callFRDRHarvester as prod version rather than testing version so that downloading of files
    // happens and geoserver is updated
    public LinkedList<SourceJavaObject> callFRDRHarvester(){
        return callFRDRHarvester(false);
    }

    public LinkedList<SourceJavaObject> callFRDRHarvester(boolean testing){
        boolean done = false;
        int counter = 0;
        LinkedList<SourceJavaObject> djos = new LinkedList<>();
        DataverseParser parser = new DataverseParser();
        // Repeatedly call the FRDR Harvester Export function until geting a json with finished: True
        while(!done) {
            String fullJSON = getJson();
            try {
                JSONTokener tokener = new JSONTokener(fullJSON);
                JSONObject json = new JSONObject(tokener);
                done = (boolean) json.get("finished");
                JSONArray records = json.getJSONArray("records");
                for (Object o : records) {
                    counter += 1;
                    JSONObject jo = (JSONObject) o;
                    DataverseJavaObject djo = parser.frdrParse(jo);
                    System.out.println("#" + counter + " ID = " + djo.getPID());
                    if (djo.hasGeoGraphicCoverage())
                        djo = (DataverseJavaObject) getBBFromGeonames(djo);
                    if (djo.hasContent && !testing) {
                        System.out.println("Downloading record: " + djo.getPID());
                        long startTime = Calendar.getInstance().getTimeInMillis();
                        djo.setGeoDataFiles(djo.downloadFiles());
                        Calendar end = Calendar.getInstance();
                        Long total = end.getTimeInMillis() - startTime;
                        System.out.println("Finished downloading " + djo.getPID() + " after " + total + " milliseconds");
                        djo.updateRecordFileNumbers();
                        djo.updateGeoserver();
                    }
                    if (djo.hasBoundingBox()) {
                        crosswalkRecord(djo);
                        ExistingHarvests existingHarvests = ExistingHarvests.getExistingHarvests();
                        existingHarvests.addBBox(djo.getPID(),djo.getBoundingBox());
                        existingHarvests.saveExistingSearchs(existingHarvests.getbBoxes(),EXISTING_BBOXES, "ExistingBBoxes");
                        djos.add(djo);
                    }
                    int record_id = jo.getInt("id");

                    //TODO remove next line comment and following line entirely after testing downloading
                    updateFRDRHarvesterDB(record_id);
                }
            } catch (JSONException e) {
                logger.error("Something went wrong parsing the FRDR JSON: \n" + fullJSON);
            }
        }
        return djos;
    }

    public void updateFRDRHarvesterDB(int record_id) {
        try {
            //TODO update url with actual prod url
            //String generateWorkspace = "/usr/bin/curl -X PUT -H \"Content-Type: application/json\" -d '{\"geodisy_harvested\":1}' https://dev3.frdr.ca/harvestapi/record/" + record_id;
            URL url = new URL("https://dev3.frdr.ca/harvestapi/records/" + record_id);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            String json = "{\"geodisy_harvested\":1}";
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            JSONObject jsonObject = new JSONObject(result);


            in.close();
            conn.disconnect();

            /*ProcessBuilder processBuilder= new ProcessBuilder();
            Process p;
            processBuilder.command("/usr/bin/bash", "-c", generateWorkspace);
            p = processBuilder.start();
            p.waitFor();
            p.destroy();*/
        } catch (IOException e) {
            logger.error("Something went wrong trying to call FRDR to mark record #" + record_id + " as processed by Geodisy");
        }
    }

    public String getJson() {
        //TODO set this up once Joel has given me the API endpoint to hit
        try {
            URL url = new URL("https://dev3.frdr.ca/harvestapi/exporter");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            String inlines = "";
            if(responsecode != 200)
                throw new RuntimeException("HttpResponseCode:" +responsecode);
            else{
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()){
                    inlines+=sc.nextLine();
                }
                sc.close();
                return inlines;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    //___________________________________________________________________________________________
    //No longer needed since metadata coming from FRDR Harvester

    @Override
    protected HashSet<String> searchDV() {
        return null;
    }

    @Override
    protected LinkedList<JSONObject> downloadMetadata(HashSet<String> dIOs) {
        return null;
    }

    @Override
    public LinkedList<SourceJavaObject> harvest(LinkedList<SourceJavaObject> answers) {
        return null;
    }

    @Override
    protected void deleteMetadata(String identifier) {

    }

    @Override
    protected void deleteFromGeoserver(String identifier) {

    }
}
