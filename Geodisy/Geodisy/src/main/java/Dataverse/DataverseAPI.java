/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dataverse;



import BaseFiles.HTTPCaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
        LinkedList<String> entries = searchDVFirst();
        records = new HashSet<>();

    }

    private LinkedList<String> searchDVFirst() {
        String searchURL = "https" + dvName + "search?q=*&type=dataset&show_entity_id=true&rows=1000";
        int start = 0;
        HashSet<String> dois =  new HashSet<>();
        dois = getRecords(start, searchURL);
        //TODO make the search call and parse to get the number of entries and a list of entry_ids
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    private HashSet<String> getRecords(int start, String searchURL) {
        boolean moreEntries = true;
        HashSet<String> answer = new HashSet<>();
        String result;
        while(moreEntries){
            HTTPCaller hC = new HTTPCaller(searchURL);
            result = hC.getSearchJSON();
            moreEntries = parseResponse(result,start, answer);
        }
        return answer;
    }

    private boolean parseResponse(String result, int start, HashSet<String> answer) {
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

    //Find all the datasets and create a linkedlist of their DOIs
    @Override
    protected LinkedList<String> searchDV() {
        String searchURL = "https" + dvName + "search?q=*&type=dataset&";
        LinkedList<String> entryIDs = new LinkedList<>();
        //TODO make the search call and parse to get the number of entries and a list of entry_ids
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //interate through the dOI
    @Override
    protected void downloadMetadata(LinkedList<String> dOIs) {
        LinkedList<String> current = dOIs;
        StringBuilder dOIList = new StringBuilder();
        while (current!= null){
            dOIList.append(",\"" + current.pop() + "\"");
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //TODO download the Datasets somewhere to pass on to Geoserver
    @Override
    protected void downloadDatasets(String dIOList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //TODO make call to get dataverseJSON, parse DataverseJavaObjects, and return a list of the DataverseJavaObjects
    @Override
    public LinkedList<DataverseJavaObject> harvest() {


        return new LinkedList<>();
    }


}
