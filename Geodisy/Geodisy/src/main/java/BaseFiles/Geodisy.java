package BaseFiles;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Dataverse.DataverseAPI;
import Dataverse.SourceAPI;

import DataSourceLocations.Dataverse;


/**
 * This is the main activity class of the tests middleware.
 * From here the calls for harvesting of Dataverse and exporting to Geoserver happen.
 * @author pdante
 */
class Geodisy {
    public Geodisy() {
    }

    /**
     * Front side of middleware, this part harvests data from Geoserver
     */
    public void harvestDataverse(){
        Dataverse dv = new Dataverse();
        String[] dataverses = dv.getDataverses();
        for(String s: dataverses){
        SourceAPI dvAPI = new DataverseAPI(createDataverseURL(s));
        dvAPI.harvest();
        }
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
