package Dataverse;

import BaseFiles.GeoLogger;
import Crosswalking.JSONParsing.DataverseParser;
import _Strings.TestStrings;
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

import static _Strings.GeodisyStrings.*;


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
        // Repeatedly call the FRDR Harvester Export function until getting a json with finished: True
        while(!done) {
            String fullJSON;
            if(!testing)
                fullJSON = getJson();
            else
                fullJSON = "{\"records\": ["+ TestStrings.doiJson + "], \"finished\": true}";
            if(fullJSON.isEmpty())
                break;
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
                        djo = (DataverseJavaObject) getBBFromGeonames(djo);if (djo.hasContent) {
                        if(!dontProcessSpecificRecords(djo.getPID())) {
                            if(!testing) {
                                System.out.println("Downloading record: " + djo.getPID());
                                djo.setGeoDataFiles(djo.downloadFiles());
                            }
                            if(djo.geoDataFiles.size()>0||djo.geoDataMeta.size()>0)
                                djo.updateRecordFileNumbers();
                            if(djo.geoDataFiles.size()>0)
                                djo.updateGeoserver();
                        }
                    }
                    if (djo.hasBoundingBox()) {
                        crosswalkRecord(djo);
                        ExistingDatasetBBoxes existingDatasetBBoxes = ExistingDatasetBBoxes.getExistingHarvests();
                        existingDatasetBBoxes.addBBox(djo.getPID(),djo.getBoundingBox());
                        existingDatasetBBoxes.saveExistingSearchs(existingDatasetBBoxes.getbBoxes(), EXISTING_DATASET_BBOXES, "ExistingBBoxes");
                        djos.add(djo);
                    }
                    int record_id = jo.getInt("id");

                    if(!testing)
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
            URL url = new URL(MARK_AS_PROCESSED + record_id);
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


            in.close();
            conn.disconnect();
        } catch (IOException e) {
            logger.error("Something went wrong trying to call FRDR to mark record #" + record_id + " as processed by Geodisy");
        }
    }

    public String getJson() {
        try {
            URL url = new URL(EXPORTER);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            String inlines = "";
            if(responsecode != 200) {
                logger.error("Failed to get harvester json, HttpResponseCode:" + responsecode);
                return "";
            }
            else{
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()){
                    inlines+=sc.nextLine();
                }
                sc.close();
                return inlines;
            }
        } catch (MalformedURLException e) {
            logger.error("Failed to get harvester json, malformed URL: " + e.toString());
        } catch (IOException e) {
            logger.error("Failed to get harvester json, IOException: " + e.toString());
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
