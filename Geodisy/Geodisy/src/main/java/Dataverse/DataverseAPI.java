/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dataverse;



import BaseFiles.GeoLogger;
import BaseFiles.Geonames;
import BaseFiles.HTTPCallerGeoNames;
import Crosswalking.JSONParsing.DataverseParser;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static BaseFiles.GeodisyStrings.DATASET_FILES_PATH;

/**
 *  Search Dataverse for datasets
 * @author pdante
 */
public class DataverseAPI extends SourceAPI {
    private final String dvURL;
    private Set<DataverseRecordInfo> records;
    GeoLogger logger = new GeoLogger(this.getClass());

    public DataverseAPI(String dvURL) {

        this.dvURL = dvURL;
        records = new HashSet<>();

    }

    @Override
    public LinkedList<SourceJavaObject> harvest(LinkedList<SourceJavaObject> answers) {
        ExistingHarvests es = ExistingHarvests.getExistingHarvests();
        HashSet<String> dois = searchDV();
        LinkedList<JSONObject> jsons = downloadMetadata(dois);
        HashMap<String, DataverseRecordInfo> recordsThatNoLongerExist = new HashMap<>();
        HashMap<String, DataverseRecordInfo> recs = es.getRecordVersions();
        for(String key: recs.keySet()){
            recordsThatNoLongerExist.put(key, recs.get(key));
        }
        DataverseParser parser = new DataverseParser();
        System.out.println("This is using the " + dvURL + " dataverse for getting files, should it be changed to something else?");
        int counter = jsons.size();
        System.out.println("Downloading records starting at: " + Calendar.getInstance().getTime());
        for(JSONObject jo:jsons){
            String doi ="";
            try {
                doi = jo.getString("authority") + "/" + jo.getString("identifier");
                if(es.hasRecord(doi)) {
                    JSONObject joInfo = jo.getJSONObject("datasetVersion");
                    int version = joInfo.getInt("versionNumber") * 1000 + joInfo.getInt("versionMinorNumber");
                    if (es.getRecordInfo(doi).getVersion() == version) {
                        recordsThatNoLongerExist.remove(doi);
                        continue;
                    }
                }
            }catch (JSONException j){
                logger.error("Tried to get a record but I got a blank JSONObject");
                continue;
            }
            DataverseJavaObject djo = parser.parse(jo, dvURL);
            doi = djo.getDOI();
            if(djo.hasContent && es.hasRecord(djo.getDOI()))
                recordsThatNoLongerExist.remove(djo.getDOI());
            if(djo.hasContent()&& hasNewInfo(djo, es)) {
                //System.out.println("Downloading record: " + fileIdent);
                djo = djo.downloadFiles();
                djo.updateGeoserver();
                //System.out.println("Finished downloading files from: " + fileIdent + " only " + counter + "more records to download");
                //djo = generateBoundingBox(djo);
                //System.out.println("Finished generating Bounding boxes for: " + fileIdent);
                if(djo.hasGeoGraphicCoverage())
                    djo = (DataverseJavaObject) getBBFromGeonames(djo);
                if(djo.hasBoundingBox()) {
                    answers.add(djo);
                } else{
                    File folderToDelete = new File(doi);
                    deleteFolder(folderToDelete);
                }
                es.addOrReplaceRecord(new DataverseRecordInfo(djo,logger.getName()));
            }else{
                continue;
            }

        }
        removeDeletedRecords(recordsThatNoLongerExist);
        for(SourceJavaObject djo: answers){
            DataverseRecordInfo dri = new DataverseRecordInfo(djo,logger.getName());
            es.addOrReplaceRecord(dri);
            es.addBBox(djo.getDOI(),djo.getBoundingBox());
        }

        return answers;
    }

    @Override
    protected void deleteMetadata(String doi) {

        try {
            FileUtils.deleteDirectory(new File(folderizedDOI(doi)));
        } catch (IOException e) {
            logger.error("Tried to delete records at " + doi);
        }
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
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
    //Not used by main program
    public DataverseJavaObject generateBoundingBox(DataverseJavaObject djo) {
        GDAL gdal = new GDAL();
        djo = gdal.generateBB(djo);
        return djo;
    }

    private boolean hasNewInfo(DataverseJavaObject djo, ExistingHarvests es) {
        DataverseRecordInfo dri = new DataverseRecordInfo(djo, logger.getName());
        return dri.newer(es.getRecordInfo(djo.getDOI()));
    }

    private HashSet<String> getRecords(String searchURL) {
        boolean moreEntries = true;
        HashSet<String> answer = new HashSet<>();
        int start = 0;
        String result;
        while(moreEntries){
            HTTPCallerGeoNames hC = new HTTPCallerGeoNames();
            result = hC.callHTTP(searchURL+"&start="+ start);
            if(result.equals("HTTP Fail"))
                break;
            moreEntries = parseResponseForDOIs(result,start, answer);
            start+=10;
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
            logger.error("Something was malformed with the JSON string returned from Dataverse");
        }
        return more;
    }

    //Find all the datasets and create a HashSet of their DOIs
    @Override
    protected HashSet<String> searchDV() {
        String searchURL = dvURL + "api/search?q=*&type=dataset&show_entity_id=true&rows=1000";
        return getRecords(searchURL);

    }
    
    //iterate through the dOI to get JSONObjects for each metadata record
    @Override
    protected LinkedList<JSONObject> downloadMetadata(HashSet<String> dOIs) {
        HTTPCallerGeoNames getMetadata;
        //TODO set base URL programmatically
        String baseURL = dvURL + "api/datasets/export?exporter=dataverse_json&persistentId=";
        LinkedList<JSONObject> answers =  new LinkedList<>();
        for(String s: dOIs){
            getMetadata = new HTTPCallerGeoNames();
            String dataverseJSON = getMetadata.callHTTP(baseURL+s);
            if(dataverseJSON.equals("HTTP Fail"))
                continue;
            JSONObject jo = new JSONObject(dataverseJSON);
            answers.add(jo);
        }
        return answers;
    }
    protected String folderizedDOI(String doi){
        String folderizedDOI = doi.replace(".","_");
        folderizedDOI = folderizedDOI.replace("/","_");
        return DATASET_FILES_PATH + folderizedDOI;
    }
}
