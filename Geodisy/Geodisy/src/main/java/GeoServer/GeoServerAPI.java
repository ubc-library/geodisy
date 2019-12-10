/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeoServer;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import BaseFiles.HTTPCaller;
import Crosswalking.GeoBlacklightJson.HTTPCombineCaller;
import Dataverse.SourceJavaObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static BaseFiles.GeodisyStrings.DATASET_FILES_PATH;
import static BaseFiles.GeodisyStrings.PATH_TO_XML_JSON_FILES;
import static BaseFiles.PrivateStrings.*;
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
        //TODO figure out the STORENAME
        String STORENAME = POSTGIS_BD;
        String response = "";
        String doi = sjo.getDOI();
        String workspace = WORKSPACE_NAME;
        String jsonString = createDirectoryUploadJSON(doi, STORENAME, workspace);
        if(!jsonString.isEmpty())
            saveJsonToFile(jsonString);
            caller.callHTTP(generateUploadCall());
        return response;
    }
    private void saveJsonToFile(String jsonString) {
        FileWriter fileWriter = new FileWriter();
        String doi = sjo.getDOI();
        String doiPath = doi.replaceAll("/","_");
        String filePath = DATASET_FILES_PATH + doiPath + "/import.json";
        try {
            fileWriter.writeStringToFile(jsonString,filePath);
        } catch (IOException e) {
            logger.error("Something went wrong trying to create the temp json for uploading to geoserver");
        }
    }

    private String generateUploadCall() {
        String curlCall = "curl -u " + GEOSERVER_USERNAME + ":" + GEOSERVER_PASSWORD + " -XPOST -H \"Content-type: application/json\" -d @_tempFiles/import.json \"http://localhost:8080/geoserver/rest/imports\"";
        return curlCall;
    }

    private String createDirectoryUploadJSON(String doi, String store, String workspace) {

        String jsonModified = "";
        if(generateWorkspace(workspace)) {
            if (addPostGISStore()) {
                JSONObject root = new JSONObject();
                JSONArray array = addWorkspaceStore(workspace, store, doi);
                root.put("\"import\"", array);
                String json = root.toString();
                jsonModified = jsonArrayBracketChange(json);
            }
        }
        return jsonModified;
    }

    private boolean addPostGISStore() {
        FileWriter fileWriter = new FileWriter();
        JSONObject jo = new JSONObject("<dataStore>" +
                                                    "<name>geodisyDB</name>" +
                                                    "<connectionParameters>" +
                                                        "<host>localhost</host>" +
                                                        "<port>5432</port>" +
                                                        "<database>geodisy</database>" +
                                                        "<user>geodisy_user</user>" +
                                                        "<passwd>"+ POSTGIS_USER_PASSWORD + "</passwd>" +
                                                        "<dbtype>postgis</dbtype>" +
                                                    "</connectionParameters>" +
                                                "</dataStore>");
        String storePath = PATH_TO_XML_JSON_FILES+"geodisy.xml";
        fileWriter.write(jo.toString(),storePath);
        HTTPCaller caller = new HTTPCombineCaller();
        caller.callHTTP("curl -v -u admin:" + GEOSERVER_PASSWORD + "-XPOST -T "+ storePath +" -H \"Content-type: text/xml\" http://localhost:8080/geoserver/rest/workspaces/"+ WORKSPACE_NAME + "/datastores");
        return deleteXMLFile(storePath);
    }

    private boolean deleteXMLFile(String path) {
        File file = new File(path);
        try {
            return Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            logger.error("Something went wrong trying to delete file at: " + path);
            return false;
        }
    }

    private String jsonArrayBracketChange(String json) {
        String answer = json.replaceAll("\\[", "{");
        return answer.replaceAll("]", "}");
    }

    private JSONArray addWorkspaceStore(String workspace, String store) {
        JSONArray array = new JSONArray();
        array.put(storeJSON(store));
        array.put(workspaceJSON(workspace));
        array.put(locationJSON());
        return array;
    }

    private JSONObject locationJSON() {
        JSONObject obj3 = new JSONObject();
        obj3.put("\"location\"", LOCATION);
        JSONObject obj2 = new JSONObject();
        obj2.put("\"type\"", "\"directory\"");
        JSONArray array = new JSONArray();
        array.put(obj2);
        array.put(obj3);
        JSONObject obj1 = new JSONObject();
        obj1.put("\"data\"",array);
        return obj1;
    }

    private JSONObject workspaceJSON(String workspace) {
        JSONObject obj3 = new JSONObject();
        obj3.put("\"name\"", workspace);
        JSONObject obj2 = new JSONObject();
        obj2.put("\"workspace\"", obj3);
        JSONObject obj1 = new JSONObject();
        obj1.put("\"targetWorkspace\"", obj2);
        return obj1;
    }

    private JSONObject storeJSON(String store) {
        JSONObject obj3 = new JSONObject();
        obj3.put("\"name\"", store);
        JSONObject obj2 = new JSONObject();
        obj2.put("\"dataStore\"", obj3);
        JSONObject obj1 = new JSONObject();
        obj1.put("\"targetStore\"", obj2);
        return obj1;
    }
    //TODO generate new workspace
    private boolean generateWorkspace(String workspaceName) {
        String callURL = "curl -v -u admin:" + GEOSERVER_PASSWORD + "-XPOST -H \"Content-type: text/xml\" -d \"<workspace><name>" + workspaceName + "</name></workspace>\" http://localhost:8080/geoserver/rest/workspaces";
        String response = caller.createWorkSpace(callURL);
        return (response.contains("HTTP/1.1 201 Created")||response.contains("HTTP ERROR 401"));

    }

    private boolean deleteWorkspace(String workspaceName){
        String callURL = "curl -v -u "+ GEOSERVER_USERNAME + ":" + GEOSERVER_PASSWORD + " -X DELETE http://localhost:8080/geoserver/rest/workspaces/" + workspaceName + "?recurse=true -H \"accept: application/json\" -H \"content-type: application/json\"";
        String response = caller.deleteWorkSpace(callURL);
        return response.contains("HTTP/1.1 200 OK")||response.contains("HTTP/1.1 404 Workspace");
    }

    public String uploadShapeToPostGIS(String workspace,String store,String file){
        String response = "";
        String doi = sjo.getDOI();
        String workspaceName = (doi.length()>10? doi.substring(doi.length()-10):doi);
        String jsonString = createFileUploadJSON(workspace, store, file);
        if(!jsonString.isEmpty())
            saveJsonToFile(jsonString);
        caller.callHTTP(generateUploadCall());
        return response;
    }

    private String createFileUploadJSON(String workspace,String store, String file) {
        String jsonModified = "";
        if(generateWorkspace(workspace)) {
            JSONObject root = new JSONObject();
            JSONArray array = addWorkspaceStore(workspace,store,file);
            root.put("\"import\"", array);
            String json = root.toString();
            jsonModified = jsonArrayBracketChange(json);
        }
        return jsonModified;
    }

    private JSONArray addWorkspaceStore(String workspace, String store, String doi) {
        JSONArray array = new JSONArray();
        array.put(workspaceJSON(workspace));
        array.put(storeJSON(store));
        array.put(fileJSON(doi));
        return array;
    }

    private JSONObject fileJSON(String file) {
        JSONObject obj3 = new JSONObject();
        obj3.put("\"file\"", file);
        JSONObject obj2 = new JSONObject();
        obj2.put("\"type\"", "\"file\"");
        JSONArray array = new JSONArray();
        array.put(obj2);
        array.put(obj3);
        JSONObject obj1 = new JSONObject();
        obj1.put("\"data\"",array);
        return obj1;
    }
}
