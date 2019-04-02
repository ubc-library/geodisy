package BaseFiles;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import DataSourceLocations.DataLocation;
import Dataverse.DataverseAPI;
import Dataverse.SourceAPI;
import Dataverse.DataverseJavaObject;

import DataSourceLocations.Dataverse;

import java.util.LinkedList;
import java.util.List;


/**
 * This is the main activity class of the tests middleware.
 * From here the calls for harvesting of Dataverse and exporting to Geoserver happen.
 * @author pdante
 */
public class Geodisy {
    public Geodisy() {
    }

    /**
     * Front side of middleware, this part harvests data from Geoserver
     */
    //TODO Broken, need to finish
    public List<DataverseJavaObject> harvestDataverse(){
        DataLocation dv = new Dataverse();
        List<DataLocation> locations = new LinkedList<>();
        locations.add(dv);
        List<String> baseURLs = new LinkedList<>();
        List<DataverseJavaObject> records = new LinkedList<>();
        for(DataLocation dl: locations) {
            SourceAPI dLAPI = new DataverseAPI(createDataverseURL(dl.getDataLocationURL()));
            dLAPI.harvest();
        }
            return records;
        }


    /** 
     * Creates the universal part of the Dataverse BaseFiles.API search/retrieve
     * URLs "://{database name}/api/". 
     * Will still need to add http/http and whatever is needed at the end.
     */
    
    private String createDataverseURL(String s) {
        return "://" + s + "/api/";
    }

    /**
     * Backside of middleware, this is the part that sends the processed data/metadata to Geoserver
     */
    public void exportToGeoserver(){
        //TODO
    }
}
