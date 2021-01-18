/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dataverse;

import BaseFiles.API;
import BaseFiles.GeoLogger;
import BaseFiles.Geonames;
import Crosswalking.Crosswalk;
import Crosswalking.GeoBlacklightJson.DataGBJSON;
import _Strings.GeodisyStrings;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import static _Strings.GeodisyStrings.DATA_DIR_LOC;
import static _Strings.GeodisyStrings.HUGE_RECORDS_TO_IGNORE_UNTIL_LATER;

/**
 *
 * @author pdante
 */
public abstract class SourceAPI implements API {
    abstract protected HashSet<String> searchDV();
    abstract protected LinkedList<JSONObject> downloadMetadata(HashSet<String> dIOs);
    abstract public LinkedList<SourceJavaObject> harvest(LinkedList<SourceJavaObject> answers);
    abstract protected void deleteMetadata(String doi);
    abstract protected void deleteFromGeoserver(String identifier);
    protected SourceJavaObject getBBFromGeonames(SourceJavaObject sjo) {
            Geonames geonames = new Geonames();
            return geonames.getBoundingBox(sjo);
    }
    protected void deleteMetadata(GeoLogger logger, String doi){
        try {
            FileUtils.deleteDirectory(new File(folderizedDOI(doi)));
        } catch (IOException e) {
            logger.error("Tried to delete records at " + doi);
        }
    }
    protected String folderizedDOI(String doi){
        String folderizedDOI = GeodisyStrings.removeHTTPS(doi.replace(".","_"));
        folderizedDOI = folderizedDOI.replace("/","_");
        return DATA_DIR_LOC + folderizedDOI;
    }

    public void crosswalkRecord(SourceJavaObject sJO) {
        crosswalkSJOsToXML(sJO);
        crosswalkSJOsToGeoBlackJSON(sJO);
    }

    protected void crosswalkSJOsToGeoBlackJSON(SourceJavaObject sJO) {
        DataverseJavaObject djo = (DataverseJavaObject) sJO;
        DataGBJSON dataGBJSON = new DataGBJSON(djo);
        dataGBJSON.createJson();
    }

    /**
     * Create ISO XML file
     * @param sJO
     */
    protected void crosswalkSJOsToXML(SourceJavaObject sJO) {
        Crosswalk crosswalk = new Crosswalk();
        crosswalk.convertSJO(sJO);
    }
    protected boolean dontProcessSpecificRecords(String doi) {
        String[] doiArray = HUGE_RECORDS_TO_IGNORE_UNTIL_LATER;
        if (doiArray.length == 0)
            return false;
        for (String d : doiArray){
            if(doi.endsWith(d.toLowerCase()))
                return true;
        }
        return false;
    }
}
