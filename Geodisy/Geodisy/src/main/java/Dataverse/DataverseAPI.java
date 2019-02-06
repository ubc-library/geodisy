/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dataverse;


import Crosswalking.JSONParsing.JacksonParser;

import java.util.LinkedList;

/**
 *
 * @author pdante
 */
public class DataverseAPI extends SourceAPI {
    private final String dvName;
    private JacksonParser jParse;
    
    public DataverseAPI(String dvName) {
        this.dvName = dvName;
        jParse = new JacksonParser();
    }

    public DataverseAPI(String dvName, JacksonParser jParse){
        this.dvName = dvName;
        this.jParse = jParse;
    }
    //Find all the datasets and create a linkedlist of their DOIs
    @Override
    protected LinkedList<String> searchDV() {
        String searchURL = "https" + dvName + "search?q=*&type=dataset";
        LinkedList<String> doi = new LinkedList<String>();
        //TODO make the search call and parse to get the number of entries and a list of DOIs
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //interate through the dOI
    @Override
    protected void downloadMetadata(LinkedList<String> dOIs) {
        LinkedList<String> current = dOIs;
        StringBuilder dOIList = new StringBuilder();
        while (current!= null){
            dOIList.append(",\"" + current + "\"");
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void downloadDatasets(String dIOList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<DataverseJSONObject> harvest() {
        return new LinkedList<>();
    }


}
