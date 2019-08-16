/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dataverse;



import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import BaseFiles.HTTPCaller;
import Crosswalking.JSONParsing.DataverseParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.Folder;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static Dataverse.DVFieldNameStrings.BASE_DV_URL;

/**
 *  Search Dataverse for datasets
 * @author pdante
 */
public class DataverseAPI extends SourceAPI {
    private final String dvName;
    private Set<DataverseRecordInfo> records;
    GeoLogger logger = new GeoLogger(this.getClass());

    public DataverseAPI(String dvName) {

        this.dvName = dvName;
        records = new HashSet<>();

    }

    @Override
    public LinkedList<SourceJavaObject> harvest(ExistingSearches es) {
        HashSet<String> dois = searchDV();
        LinkedList<JSONObject> jsons = downloadMetadata(dois);
        LinkedList<SourceJavaObject> answers =  new LinkedList<>();
        HashMap<String, DataverseRecordInfo> deletedRecords = es.recordVersions;
        DataverseParser parser = new DataverseParser();
        System.out.println("This is using the " + dvName + " dataverse for getting files, should it be changed to something else?");
        for(JSONObject jo:jsons){
            DataverseJavaObject djo = parser.parse(jo,dvName);
            if(djo.hasContent&& es.hasRecord(djo.getDOI()))
                deletedRecords.remove(djo.getDOI());
            if(djo.hasContent()&& hasNewInfo(djo, es)) {
                djo.downloadFiles();
                if(!djo.hasBoundingBox())
                    djo = generateBoundingBox(djo);
                if(djo.hasBoundingBox())
                    answers.add(djo);
                else{
                    String doi = djo.getDOI();
                    String folderizedDOI = doi.replaceAll("\\.","_");
                    folderizedDOI = folderizedDOI.replaceAll("/","_");
                    File folderToDelete = new File("./datasetFiles/" + folderizedDOI);
                    deleteFolder(folderToDelete);
                }
            }

        }
        if(answers.size()>0)
            System.out.println("Updated or added " + answers.size() + " records.");
        removeDeletedRecords(deletedRecords,es);
        return answers;
    }
    //TODO remove record from es and delete local file
    private void removeDeletedRecords(HashMap<String, DataverseRecordInfo> deletedRecords, ExistingSearches es) {

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

    private DataverseJavaObject generateBoundingBox(DataverseJavaObject djo) {
        GDAL gdal = new GDAL();
        djo = gdal.generateBB(djo);
        return djo;
    }

    private boolean hasNewInfo(DataverseJavaObject djo, ExistingSearches es) {
        DataverseRecordInfo dri = new DataverseRecordInfo(djo, logger.getName());
        return dri.younger(es.getRecordInfo(djo.getDOI()));
    }

    private HashSet<String> getRecords(String searchURL) {
        boolean moreEntries = true;
        HashSet<String> answer = new HashSet<>();
        int start = 0;
        String result;
        while(moreEntries){
            HTTPCaller hC = new HTTPCaller(searchURL+"&start="+ start);
            result = hC.getJSONString();
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
        String searchURL = dvName + "api/search?q=*&type=dataset&show_entity_id=true&rows=1000";
        return getRecords(searchURL);

    }
    
    //interate through the dOI to get JSONObjects for each metadata record
    @Override
    protected LinkedList<JSONObject> downloadMetadata(HashSet<String> dOIs) {
        HTTPCaller getMetadata;
        //TODO set base URL programmatically
        String baseURL = BASE_DV_URL + "api/datasets/export?exporter=dataverse_json&persistentId=";
        LinkedList<JSONObject> answers =  new LinkedList<>();
        for(String s: dOIs){
            getMetadata = new HTTPCaller(baseURL+s);
            String dataverseJSON = getMetadata.getJSONString();
            if(dataverseJSON.equals("HTTP Fail"))
                continue;
            JSONObject jo = new JSONObject(dataverseJSON);
            answers.add(jo);
        }
        return answers;
    }

}
