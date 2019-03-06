/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dataverse;



import org.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *  Search Dataverse for datasets
 * @author pdante
 */
public class DataverseAPI extends SourceAPI {
    private final String dvName;
    private Set<Integer> entry_IDs;
    
    public DataverseAPI(String dvName) {
        this.dvName = dvName;
        LinkedList<String> entries = searchDVFirst();
        entry_IDs = new HashSet<>();

    }

    private LinkedList<String> searchDVFirst() {
        String searchURL = "https" + dvName + "search?q=*&type=dataset&show_entity_id=true";
        int start = 0;
        int rows = 100;
        boolean moreEntries = true;
        while(moreEntries){

        }
        Set<Integer> newEntries = new HashSet<>();
        //TODO make the search call and parse to get the number of entries and a list of entry_ids
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

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
