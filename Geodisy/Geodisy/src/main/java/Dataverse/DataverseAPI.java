/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dataverse;



import BaseFiles.HTTPCaller;
import Crosswalking.JSONParsing.DataverseParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    Logger logger = LogManager.getLogger(this.getClass());

    public DataverseAPI(String dvName) {

        this.dvName = dvName;
        records = new HashSet<>();

    }
    //TODO test if this works
    @Override
    public LinkedList<DataverseJavaObject> harvest() {
        HashSet<String> dois = searchDV();
        LinkedList<JSONObject> jsons = downloadMetadata(dois);
        LinkedList<DataverseJavaObject> answers =  new LinkedList<>();
        DataverseParser parser = new DataverseParser();
        ExistingSearches eS = ExistingSearches.getExistingSearches();
        for(JSONObject jo:jsons){
            DataverseJavaObject djo = parser.parse(jo);
            if(djo.dJOHasContent())
                answers.add(djo);
        }
        return answers;
    }

    private HashSet<String> getRecords(String searchURL) {
        boolean moreEntries = true;
        HashSet<String> answer = new HashSet<>();
        int start = 0;
        String result;
        while(moreEntries){
            HTTPCaller hC = new HTTPCaller(searchURL+"&start="+ start);
            result = hC.getJSONString();
            moreEntries = parseResponseForDOIs(result,start, answer);
            start+=1000;
        }
        return answer;
    }

    private boolean parseResponseForDOIs(String result, int start, HashSet<String> answer) {
        boolean more = false;
        try{

            JSONObject current = new JSONObject(result);
            current = (JSONObject) current.get("data");
            int totalRecords = Integer.parseInt(current.getString("total_count"));
            if(totalRecords-1>start+999)
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
        String baseURL = BASE_DV_URL + "api/datasets/export?exporter=dataverse_json&persistentId=";
        LinkedList<JSONObject> answers =  new LinkedList<>();
        for(String s: dOIs){
            getMetadata = new HTTPCaller(baseURL+s);
            String dataverseJSON = getMetadata.getJSONString();
            JSONObject jo = new JSONObject(dataverseJSON);
            answers.add(jo);
        }
        return answers;
    }
    //TODO download the Datasets somewhere to pass on to Geoserver
    @Override
    protected void downloadDatasets(String dIOList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
