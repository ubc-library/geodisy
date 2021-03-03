/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dataverse;



import BaseFiles.GeoLogger;
import BaseFiles.Geonames;
import BaseFiles.HTTPCallerDataverse;
import BaseFiles.HTTPCallerGeoNames;
import Crosswalking.JSONParsing.DataverseParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static _Strings.GeodisyStrings.*;

/**
 *  Search Dataverse for datasets
 * @author pdante
 */
public class DataverseAPI extends SourceAPI {
    private final String dvURL;
    GeoLogger logger = new GeoLogger(this.getClass());

    public DataverseAPI(String dvURL) {

        this.dvURL = dvURL;

    }

    @Override
    public LinkedList<SourceJavaObject> harvest(LinkedList<SourceJavaObject> answers) {
        ExistingDatasetBBoxes existingDatasetBBoxes = ExistingDatasetBBoxes.getExistingHarvests();
        HashSet<String> dois = searchDV();
        LinkedList<JSONObject> jsons = downloadMetadata(dois);
        HashMap<String, DataverseRecordInfo> recordsThatNoLongerExist = new HashMap<>();
        DataverseParser parser = new DataverseParser();
        System.out.println("This is using the " + dvURL + " dataverse for getting files, should it be changed to something else?");
        int counter = jsons.size();
        System.out.println("Downloading records starting at: " + Calendar.getInstance().getTime());
        long fullStartTime = Calendar.getInstance().getTimeInMillis();
        for(JSONObject jo:jsons){
            Calendar fullEnd =  Calendar.getInstance();
            // Uncomment this and set milliseconds if you want to put a maximum time per run
            /*Long fullTotal = fullEnd.getTimeInMillis()-fullStartTime;
            if(fullTotal>10800000)
                break;*/
            String doi ="";
            try {
                doi = jo.getString("authority") + "/" + jo.getString("identifier");

                if(processSpecificRecords(doi)) {
                    System.out.println("Processing only specic records, is that what you want? If not delete all doi in PROCESS_THESE_DOIS in GeodisyStrings class");
                    for(String s:PROCESS_THESE_DOIS){
                        System.out.println("Dataset: " + s);
                    }
                }
                if(dontProcessSpecificRecords(doi))
                    continue;

            }catch (JSONException j){
                logger.error("Tried to get a record but I got a blank or malformed JSONObject");
                continue;
            }
            DataverseJavaObject djo = parser.parse(jo, dvURL);
            doi = djo.getPID();
            if(djo.hasContent()) {
                System.out.println("Downloading record: " + doi);
                long startTime = Calendar.getInstance().getTimeInMillis();
                djo.setGeoDataFiles(djo.downloadFiles());
                Calendar end =  Calendar.getInstance();
                Long total = end.getTimeInMillis()-startTime;
                System.out.println("Finished downloading " + doi +" after " + total + " milliseconds");
                djo.updateRecordFileNumbers();
                djo.updateGeoserver();

                if(djo.hasGeoGraphicCoverage())
                    djo = (DataverseJavaObject) getBBFromGeonames(djo);
                if(djo.hasBoundingBox()) {
                    crosswalkRecord(djo);
                    existingDatasetBBoxes.addBBox(djo.getPID(),djo.getBoundingBox());
                    existingDatasetBBoxes.saveExistingSearchs(existingDatasetBBoxes.getbBoxes(), EXISTING_DATASET_BBOXES, "ExistingBBoxes");
                    answers.add(djo);
                } else{
                    File folderToDelete = new File(doi);
                    deleteFolder(folderToDelete);
                }
            }else{
                continue;
            }
            existingDatasetBBoxes.saveExistingSearchs(existingDatasetBBoxes.getbBoxes(), EXISTING_DATASET_BBOXES, "ExistingBBoxes");
            System.out.println("Parsed and saved " + doi);
            DownloadedFiles dF = DownloadedFiles.getDownloadedFiles();
            dF.saveDownloads();
            dF.resetList();
        }

        removeDeletedRecords(recordsThatNoLongerExist);


        return answers;
    }


    //Only process dois in the below doi array (only for testing, really)
    private boolean processSpecificRecords(String doi) {

        return PROCESS_THESE_DOIS.length > 0;
    }

    @Override
    protected void deleteMetadata(String doi) {
        super.deleteMetadata(logger, doi);
    }
    //TODO figure out how to delete files from Geoserver
    @Override
    protected void deleteFromGeoserver(String dio) {

    }

    @Override
    protected SourceJavaObject getBBFromGeonames(SourceJavaObject djo) {
        Geonames geonames = new Geonames();
        return geonames.getBoundingBox(djo);
    }

    /**
     * Deletes an existing record if that record is later deleted from Dataverse
     * @param recordsToDelete
     */
    private void removeDeletedRecords(HashMap<String, DataverseRecordInfo> recordsToDelete) {
        Set<String> keySet = recordsToDelete.keySet();
        Object[] keys = keySet.toArray();
        for(Object key:keys){
            String doi = (String) key;
            String folderizedDOI = folderizedDOI(doi);
            deleteFolder(new File(folderizedDOI));
            deleteFromGeoserver(doi);
            deleteMetadata(doi);
            //TODO delete dataset and metadata from Geoserver
        }
        //TODO change this when geoserver is setup
        //GeoServerAPI geoserver = new GeoServerAPI();
        //TODO uncomment once I've got JGIT working
        //JGit git = new JGit();
        //git.deleteXMLFiles(keySet);
    }


    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        try {
            if(files!=null) { //some JVMs return null for empty dirs
                for(File f: files) {
                    if(f.isDirectory()) {
                        deleteFolder(f);
                    } else {
                            Files.deleteIfExists(Paths.get(f.getAbsolutePath()));
                    }
                }
            }
            Files.deleteIfExists(Paths.get(folder.getAbsolutePath()));
        } catch (IOException e) {
            logger.error("Something went wrong trying to delete folder: " + folder.getAbsolutePath());
        }
    }
    //Not used by main program
    public DataverseJavaObject generateBoundingBox(DataverseJavaObject djo) {
        GDAL gdal = new GDAL();
        djo = gdal.generateBB(djo);
        return djo;
    }

    private HashSet<String> getRecords(String searchURL) {
        boolean moreEntries = true;
        HashSet<String> answer = new HashSet<>();
        int start = 0;
        String result;
        if(NUMBER_OF_RECS_TO_HARVEST==0){
            while(moreEntries){
                HTTPCallerDataverse hC = new HTTPCallerDataverse();
                result = hC.callHTTP(searchURL+"&start="+ start);
                if(result.equals("HTTP Fail"))
                    break;
                moreEntries = parseResponseForDOIs(result,start, answer);
                start+=10;
            }
        }else {
            while (moreEntries && start < NUMBER_OF_RECS_TO_HARVEST) {
                HTTPCallerDataverse hC = new HTTPCallerDataverse();
                result = hC.callHTTP(searchURL + "&start=" + start);
                if (result.equals("HTTP Fail"))
                    break;
                moreEntries = parseResponseForDOIs(result, start, answer);
                start += 10;
            }
        }
        return answer;
    }

    private boolean parseResponseForDOIs(String result, int start, HashSet<String> answer) {
        boolean more = false;
        try{

            JSONObject current = new JSONObject(result);
            current = (JSONObject) current.get("data");
            int totalRecords = (int) current.get("total_count");
            if(totalRecords-1>start+9)
                more = true;
            JSONArray records = (JSONArray) current.get("items");
            for(Object o:records){
                JSONObject jo = (JSONObject) o;
                answer.add(jo.getString("global_id"));

            }
        }catch (JSONException e){
            logger.error("Something was malformed with the JSON string returned from Dataverse: " + result);
        }
        return more;
    }

    //Find all the datasets and create a HashSet of their DOIs
    @Override
    protected HashSet<String> searchDV() {
        String searchURL = dvURL + "api/search?q=*&type=dataset&show_entity_id=true";
        if(PROCESS_THESE_DOIS.length>0)
            return new HashSet<String>();
        return getRecords(searchURL);

    }
    
    //iterate through the dOI to get JSONObjects for each metadata record
    @Override
    protected LinkedList<JSONObject> downloadMetadata(HashSet<String> dOIs) {
        HTTPCallerGeoNames getMetadata;
        //TODO set base URL programmatically
        String baseURL = dvURL + "api/datasets/export?exporter=dataverse_json&persistentId=";
        LinkedList<JSONObject> answers =  new LinkedList<>();
        if(PROCESS_THESE_DOIS.length>0){
            for(String s: PROCESS_THESE_DOIS){
                String pid = (s.contains("."))? "doi:" + s: "hdl:" +s;
                getMetadata = new HTTPCallerGeoNames();
                String dataverseJSON = getMetadata.callHTTP(baseURL+pid);
                if(dataverseJSON.equals("HTTP Fail"))
                    continue;
                JSONObject jo = new JSONObject(dataverseJSON);
                if(GEOSPATIAL_ONLY){
                    logger.error("Only accessing records with Files, this is not correct if a production run");
                    if(hasFiles(jo))
                        answers.add(jo);
                }else
                    answers.add(jo);
            }
        }else {
            for (String s : dOIs) {
                getMetadata = new HTTPCallerGeoNames();
                String dataverseJSON = getMetadata.callHTTP(baseURL + s);
                if (dataverseJSON.equals("HTTP Fail"))
                    continue;
                JSONObject jo = new JSONObject(dataverseJSON);
                answers.add(jo);
            }
        }
        return answers;
    }

    private boolean hasFiles(JSONObject jo) {
        return jo.getJSONObject("datasetVersion").has("files");
    }


}
