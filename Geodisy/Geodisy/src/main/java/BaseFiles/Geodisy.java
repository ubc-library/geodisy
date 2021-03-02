package BaseFiles;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import Dataverse.DataverseAPI;
import Dataverse.SourceAPI;
import Dataverse.SourceJavaObject;
import DataSourceLocations.Dataverse;
import GeoServer.GeoServerAPI;
import java.util.LinkedList;
import java.util.List;



/**
 * This is the main activity class of the tests middleware.
 * From here the calls for harvesting of Dataverse and exporting to Geoserver happen.
 * @author pdante
 */
public class Geodisy {
    /**
     * Front side of middleware, this part harvests data from Dataverse
     */

    public List<SourceJavaObject> harvestDataverseMetadata() {
        Dataverse dv = new Dataverse();
        String[] dvs = dv.getDataLocationURLs();
        LinkedList<SourceJavaObject> records = new LinkedList<>();
        SourceAPI dVAPI;
        for (String s : dvs) {
            dVAPI = new DataverseAPI(s);
            records = dVAPI.harvest(records);
        }

        return records;
    }

    /**
     * Harvesting metadata from FRDR Harvester
     * @return
     */
    public List<SourceJavaObject> harvestFRDRMetadata(){
        FRDRGeodisy frdrGeodisy = new FRDRGeodisy();
        return frdrGeodisy.harvestFRDRMetadata();
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
    public void exportToGeoserver(SourceJavaObject sjo){
        GeoServerAPI geoServerAPI = new GeoServerAPI(sjo);

    }
}
