/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeoServer;

import Dataverse.SourceJavaObject;
import org.json.JSONObject;

import java.io.File;

import static GeoServer.GeoserverStrings.LOCATION;
import static GeoServer.GeoserverStrings.WORKSPACE_NAME;
import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

/**
 *
 * @author pdante
 */
public class GeoServerAPI extends DestinationAPI {
    //TODO write BaseFiles.API to connect to GeoServer
    SourceJavaObject sjo;

    public GeoServerAPI(SourceJavaObject sjo) {
        this.sjo = sjo;
    }

    public String upload(String name){
        String response = "";
        createJSON(name);

        return response;
    }
    //TODO fix this monstrosity
    private void createJSON(String fileName) {
        String doi = sjo.getDOI();
        String workspace = (doi.length()>10? doi.substring(doi.length()-10):doi);
        generateWorkspace(workspace);
        JSONObject root = JSON.createObjectBuilder();
        JSONObject obj3 = new JSONObject();
        obj3.put("\"name\"",workspace);
        JSONObject obj2 = new JSONObject();
        obj2.put("\"workspace\"",obj3);
        JSONObject obj1 = new JSONObject();
        obj1.put("\"targetWorkspace\"",obj2);
        JSONObject root = new JSONObject();
        root.put("\"import\"",obj1);
        JSONObject obj6 = new JSONObject();
        obj6.put("\"location\"",LOCATION);
        JSONObject obj5 = new JSONObject();
        obj5.put("\"type\"","\"directory\"");
        JSONObject obj4 = new JSONObject();
        obj4.put("\"data\"",obj5);
        obj4.
    }
    //TODO generate new workspace
    private void generateWorkspace(String workspace) {

    }
}
