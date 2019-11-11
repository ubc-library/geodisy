/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeoServer;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import Dataverse.SourceJavaObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static GeoServer.GeoserverStrings.*;

/**
 *
 * @author pdante
 */
public class GeoServerAPI extends DestinationAPI {
    //TODO write BaseFiles.API to connect to GeoServer
    SourceJavaObject sjo;
    HTTPCallerGeosever caller;

    public GeoServerAPI(SourceJavaObject sjo) {
        this.sjo = sjo;
        caller = new HTTPCallerGeosever();
        logger =  new GeoLogger(this.getClass());
    }

    public String upload(){
        String response = "";
        String doi = sjo.getDOI();
        String workspace = (doi.length()>10? doi.substring(doi.length()-10):doi);
        String jsonString = createJSON(workspace);
        if(!jsonString.isEmpty())
            saveJsonToFile(jsonString);
            caller.callHTTP(generateLayerUploadCall(workspace));
        return response;
    }
    private void saveJsonToFile(String jsonString) {
        FileWriter fileWriter = new FileWriter();
        String doi = sjo.getDOI();
        String doiPath = doi.replaceAll(".","_").replaceAll("/","_");
        String filePath = "datasetFiles/"+ doiPath + "/import.json";
        try {
            fileWriter.writeStringToFile(jsonString,filePath);
        } catch (IOException e) {
            logger.error("Something went wrong trying to create the temp json for uploading to geoserver");
        }
    }

    private String generateLayerUploadCall(String workspace) {
        String curlCall = "curl -u " + USERNAME + ":" + PASSWORD + " -XPOST -H \"Content-type: application/json\" -d @_tempFiles/import.json \"http://localhost:8080/geoserver/rest/imports\"";
        return curlCall;
    }

    //TODO fix this monstrosity
    private String createJSON(String workspace) {

        String jsonModified = "";
        if(generateWorkspace(workspace)) {
            JSONObject root = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject obj3 = new JSONObject();
            obj3.put("\"name\"", workspace);
            JSONObject obj2 = new JSONObject();
            obj2.put("\"workspace\"", obj3);
            JSONObject obj1 = new JSONObject();
            obj1.put("\"targetWorkspace\"", obj2);
            array.put(obj1);
            JSONObject obj6 = new JSONObject();
            obj6.put("\"location\"", LOCATION);
            JSONObject obj5 = new JSONObject();
            obj5.put("\"type\"", "\"directory\"");
            JSONObject obj4 = new JSONObject();
            JSONArray array2 = new JSONArray();
            array2.put(obj5);
            array2.put(obj6);
            obj4.put("\"data\"", array2);
            array.put(obj4);
            root.put("\"import\"", array);
            String json = root.toString();
            jsonModified = json.replaceAll("\\[", "{");
            jsonModified = jsonModified.replaceAll("]", "}");
        }
        return jsonModified;
    }
    //TODO generate new workspace
    private boolean generateWorkspace(String workspaceName) {
        String callURL = "curl -v -u admin:geoserver -XPOST -H \"Content-type: text/xml\" -d \"<workspace><name>" + workspaceName + "</name></workspace>\" http://localhost:8080/geoserver/rest/workspaces";
        String response = caller.createWorkSpace(callURL);
        return (response.contains("HTTP/1.1 201 Created")||response.contains("HTTP ERROR 401"));

    }

    private boolean deleteWorkspace(String workspaceName){
        String callURL = "curl -v -u "+ USERNAME + ":" + PASSWORD + " -X DELETE http://localhost:8080/geoserver/rest/workspaces/" + workspaceName + "?recurse=true -H \"accept: application/json\" -H \"content-type: application/json\"";
        String response = caller.deleteWorkSpace(callURL);
        return response.contains("HTTP/1.1 200 OK")||response.contains("HTTP/1.1 404 Workspace");
    }
}
