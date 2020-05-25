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
import Dataverse.*;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;

import static Strings.GeodisyStrings.*;
import static Strings.XMLStrings.OPEN_METADATA_LOCAL_REPO;
import static Strings.DVFieldNameStrings.*;
import static Strings.GeoserverStrings.*;

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
        //TODO fix this to access layers from POSTGIS
    public void addPostGISLayer(String geoserverlabel, String filename){
        String vectorDB = VECTOR_DB;
        String call = "curl -v -u "+ GEOSERVER_USERNAME + ":" + GEOSERVER_PASSWORD + " -XPOST -H \"Content-type: text/xml\" -d \"<featureType><name>" + geoserverlabel.toLowerCase() + "</name><nativeCRS>EPSG:4326</nativeCRS><srs>EPSG:4326</srs><enabled>true</enabled></featureType>\" http://localhost:8080/geoserver/rest/workspaces/geodisy/datastores/" + vectorDB + "/featuretypes";
        String deleteFirst = "curl -v -u "+ GEOSERVER_USERNAME + ":" + GEOSERVER_PASSWORD + " -X DELETE \"http://localhost:8080/geoserver/rest/workspaces/geodisy/" + geoserverlabel.toLowerCase() + "\"?recurse=true -H  \"accept: application/json\" -H  \"content-type: application/json\"";
        //String modifyName = "curl -v -u "+ GEOSERVER_USERNAME + ":" + GEOSERVER_PASSWORD + " -XPOST -H \"Content-type: text/xml\" -d \"<GeoServerLayer><enabled>true</enabled><name>" + geoserverlabel.toLowerCase() + "</name><title>" + filename + "</title></GeoServerLayer>\"  \"http://localhost:8080/geoserver/gwc/rest/layers/" + geoserverlabel.toLowerCase() +".xml\"";
        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.command("bash", "-c",deleteFirst);

        try {
            //Delete existing layer
            Process p = processBuilder.start();
            p.waitFor();
            p.destroy();
            //bring new layer over from POSTGIS
            processBuilder.command("bash", "-c",call);
            p = processBuilder.start();
            p.waitFor();
            p.destroy();
            //processBuilder.command("bash", "-c",modifyName);
        } catch (IOException | InterruptedException e) {
            logger.error("Something went wrong adding vector layer " + geoserverlabel + " from POSTGIS");
        }
    }

    private boolean addVectorToPostGIS(String fileName, String geoserverlabel) {
        PostGIS postGIS = new PostGIS();
        return postGIS.addFile2PostGIS((DataverseJavaObject) sjo, fileName,geoserverlabel);
        //TODO uncomment this once I have getting the layers from postgis to geoserver
        //addVectorToGeoserver(fileName);
    }


     /**
    //TODO need to get vector files from POSTGRIS, is this the way?
    private boolean addVectorToGeoserver(String fileName) {
        JSONObject jo = new JSONObject();
        JSONObject outer = new JSONObject();
        jo.put("name",sjo.getSimpleFieldVal(PERSISTENT_ID));
        jo.put("title",fileName);
        outer.put("featureType",jo);
        createFileUploadJSON(jo);
        String urlString = GEOSERVER_REST + "workspaces/geodisy/datastores/shapefiles/featuretypes";
        String command = "curl -U " + GEOSERVER_USERNAME + ":" + GEOSERVER_PASSWORD + " -XPOST -H " + stringed("Content-type: application/json") + "-d @" + DATASET_FILES_PATH + "import.json" + urlString;
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            p.destroy();
        } catch (InterruptedException | IOException e) {
            logger.error("Something went wrong trying to add a vector to geoserver from postGIS. File name was: " + fileName);
            return false;
        }
    return true;
    }*/

    private void createFileUploadJSON(JSONObject jo) {
        FileWriter file = new FileWriter(DATASET_FILES_PATH + "input.json");
        file.write(jo.toString());
    }

    public boolean addVectorTest(String fileName,String geoserverLabel){
        return addVectorToPostGIS(fileName,geoserverLabel);
    }

    public boolean addVector(String fileName,String geoserverLabel){
        return addVectorToPostGIS(fileName,geoserverLabel);
    }
    public void uploadRaster(DataverseGeoRecordFile dgrf){
        String fileName = dgrf.getFileName();
        GeographicBoundingBox gbb = dgrf.getGBB();
        String deleteCoveragestore = " admin:" + GEOSERVER_PASSWORD + " -XDELETE " + stringed("http://localhost:8080/geoserver/rest/workspaces/geodisy/coveragestores/" + dgrf.getGeoserverLabel());
        String createCoveragestore = " admin:" + GEOSERVER_PASSWORD + " -XPOST -H " + stringed("Content-type:text/xml") +  " -d '<coverageStore><name>" + dgrf.getGeoserverLabel() + "</name><workspace>geodisy</workspace><enabled>true</enabled><type>GeoTIFF</type><url>"+ GEODISY_PATH_ROOT + dgrf.getDatasetIdent() + dgrf.getTranslatedTitle() + "</url></coverageStore>'" + stringed("http://localhost:8080/geoserver/rest/workspaces/geodisy/coveragestores?configure=all");
        String addRasterLayer = " admin:" + GEOSERVER_PASSWORD + " -XPOST -H " + stringed("Content-type:application/xml") + " -d '<coverage><name>"+ dgrf.getGeoserverLabel() + "</name><nativeCRS>" + dgrf.getNativeCRS() + "</nativeCRS><title>" + dgrf.getTranslatedTitle() + "</title><enabled>True</enabled></coverageStore>' " + stringed("http://localhost:8080/geoserver/rest/workspaces/geodisy/coveragestores/"+ dgrf.getGeoserverLabel() + "/coverages");
        Process p;
            ProcessBuilder processBuilder= new ProcessBuilder();
            try {
                deleteOldCoverstore(processBuilder, deleteCoveragestore);
                createCoverstore(processBuilder, createCoveragestore);
                createLayer(processBuilder, addRasterLayer);
            }catch (InterruptedException | IOException f){
                logger.error("Error trying to add raster to geoserver: doi=" + sjo.getDOI() + ", geoserver label=" + dgrf.getGeoserverLabel() + ", file name=" + dgrf.getFileName());
            }


        ExistingRasterRecords existingRasterRecords = ExistingRasterRecords.getExistingRasters();
        existingRasterRecords.addOrReplaceRecord(sjo.getDOI(),fileName);
    }

    private void createLayer(ProcessBuilder processBuilder, String addRasterLayer) throws InterruptedException, IOException{
        Process p;
        processBuilder.command("curl", "-u", addRasterLayer);
        p = processBuilder.start();
        p.waitFor();
        p.destroy();
    }

    private void createCoverstore(ProcessBuilder processBuilder, String createCoveragestore) throws InterruptedException, IOException{
        Process p;
        processBuilder.command("curl", "-u", createCoveragestore);
        p = processBuilder.start();
        p.waitFor();
        p.destroy();
    }

    private void deleteOldCoverstore(ProcessBuilder processBuilder, String deleteCoveragestore) throws InterruptedException, IOException{
        Process p;
        processBuilder.command("curl", "-u", deleteCoveragestore);
        p = processBuilder.start();
        p.waitFor();
        p.destroy();

    }


    private void saveJsonToFile(String jsonString) {
        FileWriter fileWriter = new FileWriter();
        String doi = sjo.getDOI();
        String doiPath = doi.replace("/","_");
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

    private String createDirectoryUploadJSON(String filename, String doi, String store, String workspace) {

        String jsonModified = "";
        if(generateWorkspace(workspace)) {
            if (addPostGISStore()) {
                JSONObject root = new JSONObject();
                JSONArray array = addWorkspaceStore(workspace, store, doi);
                root.put(stringed("import"), array);
                String json = root.toString();
                jsonModified = jsonArrayBracketChange(json);
            }
        }
        return jsonModified;
    }

    private boolean addPostGISStore() {

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
        FileWriter fileWriter = new FileWriter(storePath);
        fileWriter.write(jo.toString());
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
        String answer = json.replace("[", "{");
        return answer.replace("]", "}");
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
        obj3.put(stringed("location"), LOCATION);
        JSONObject obj2 = new JSONObject();
        obj2.put(stringed("type"), stringed("directory"));
        JSONArray array = new JSONArray();
        array.put(obj2);
        array.put(obj3);
        JSONObject obj1 = new JSONObject();
        obj1.put(stringed("data"),array);
        return obj1;
    }

    private JSONObject workspaceJSON(String workspace) {
        JSONObject obj3 = new JSONObject();
        obj3.put(stringed("name"), workspace);
        JSONObject obj2 = new JSONObject();
        obj2.put(stringed("workspace"), obj3);
        JSONObject obj1 = new JSONObject();
        obj1.put(stringed("targetWorkspace"), obj2);
        return obj1;
    }

    private JSONObject storeJSON(String store) {
        JSONObject obj3 = new JSONObject();
        obj3.put(stringed("name"), store);
        JSONObject obj2 = new JSONObject();
        obj2.put(stringed("dataStore"), obj3);
        JSONObject obj1 = new JSONObject();
        obj1.put(stringed("targetStore"), obj2);
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


    private String createFileUploadJSON(String workspace,String store, String file) {
        String jsonModified = "";
        if(generateWorkspace(workspace)) {
            JSONObject root = new JSONObject();
            JSONArray array = addWorkspaceStore(workspace,store,file);
            root.put(stringed("import"), array);
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
        obj3.put(stringed("file"), file);
        JSONObject obj2 = new JSONObject();
        obj2.put(stringed("type"), stringed("file"));
        JSONArray array = new JSONArray();
        array.put(obj2);
        array.put(obj3);
        JSONObject obj1 = new JSONObject();
        obj1.put(stringed("data"),array);
        return obj1;
    }

    //TODO do I need to create something for this method. Bad coding if no.
    protected void createJson(){}
}
