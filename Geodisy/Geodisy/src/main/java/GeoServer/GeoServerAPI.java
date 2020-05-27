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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;

import static _Strings.GeodisyStrings.*;
import static _Strings.GeoserverStrings.*;

/**
 *
 * @author pdante
 */
public class GeoServerAPI extends DestinationAPI {
    SourceJavaObject sjo;
    HTTPCallerGeosever caller;

    public GeoServerAPI(SourceJavaObject sjo) {
        this.sjo = sjo;
        caller = new HTTPCallerGeosever();
        logger =  new GeoLogger(this.getClass());
    }

    private boolean generateWorkspace(String workspaceName) {
        ProcessBuilder processBuilder= new ProcessBuilder();
        try {
            String generateWorkspace = "admin:" + GEOSERVER_PASSWORD + "-XPOST -H \"Content-type: text/xml\" -d \"<workspace><name>" + workspaceName + "</name></workspace>\" http://localhost:8080/geoserver/rest/workspaces";
            processBuilder.command("curl", "-u", generateWorkspace);
            Process p = processBuilder.start();
            p.waitFor();
            p.destroy();
        } catch (InterruptedException|IOException e) {
            logger.error("Something went wrong trying to create the workspace " + workspaceName + " in geoserver");
            return false;
        }
        return true;
    }

    @Override
    public boolean addVector(String fileName,String geoserverLabel){
        boolean success = addVectorToPostGIS(fileName,geoserverLabel);
        if(!success) return success;
        return addPostGISLayerToGeoserver(geoserverLabel,fileName);
    }

        private boolean addVectorToPostGIS(String fileName, String geoserverlabel) {
            PostGIS postGIS = new PostGIS();
            return postGIS.addFile2PostGIS((DataverseJavaObject) sjo, fileName,geoserverlabel);
        }

        //TODO fix this?
        public boolean addPostGISLayerToGeoserver(String geoserverlabel, String filename){
        String vectorDB = VECTOR_DB;
        String title = filename.substring(0,filename.lastIndexOf('.'));
        ProcessBuilder processBuilder= new ProcessBuilder();

        try {
            //Delete existing layer
            String deleteFirst = GEOSERVER_USERNAME + ":" + GEOSERVER_PASSWORD + " -X DELETE \"http://localhost:8080/geoserver/rest/workspaces/geodisy/layers/" + geoserverlabel.toLowerCase() + "\"";
            processBuilder.command("curl", "-u",deleteFirst);
            Process p = processBuilder.start();
            p.waitFor();
            p.destroy();

            //bring new layer over from POSTGIS
            String call = GEOSERVER_USERNAME + ":" + GEOSERVER_PASSWORD + " -XPOST -H \"Content-type: text/xml\" -d \"<featureType><name>" + geoserverlabel.toLowerCase() + "</name><title>"+ title +"</title><nativeCRS>EPSG:4326</nativeCRS><srs>EPSG:4326</srs><enabled>true</enabled></featureType>\" http://localhost:8080/geoserver/rest/workspaces/geodisy/datastores/" + vectorDB + "/featuretypes";
            processBuilder.command("curl", "-u",call);
            p = processBuilder.start();
            p.waitFor();
            p.destroy();
        } catch (IOException | InterruptedException e) {
            logger.error("Something went wrong adding vector layer " + geoserverlabel + " from POSTGIS");
            return false;
        }
        return true;
    }

    @Override
    public boolean addRaster(DataverseGeoRecordFile dgrf){
        String fileName = dgrf.getOriginalTitle()+".tif";
        System.out.println("FileName = " + fileName);
            ProcessBuilder processBuilder= new ProcessBuilder();

            try { deleteOldCoverstore(processBuilder, dgrf);
            }catch (InterruptedException | IOException f) {
                logger.error("Error trying to delete existing raster from geoserver: doi=" + sjo.getDOI() + ", geoserver label=" + dgrf.getGeoserverLabel() + ", file name=" + fileName);
                return false;
            }

            try { normalizeRaster(processBuilder,dgrf);
            }catch (InterruptedException | IOException f) {
                logger.error("Error trying to normalize raster from geoserver: doi=" + sjo.getDOI() + ", geoserver label=" + dgrf.getGeoserverLabel() + ", file name=" + fileName);
                return false;
            }

            try { renameRasterToOrig(processBuilder,sjo.getDOI().replace(".","/"),fileName);
            }catch (InterruptedException | IOException f) {
                logger.error("Error trying to rename raster back to correct name from geoserver: doi=" + sjo.getDOI() + ", geoserver label=" + dgrf.getGeoserverLabel() + ", file name=" + fileName);
                return false;
            }

            try { addRasterOverviews(processBuilder,dgrf);
            }catch (InterruptedException | IOException f) {
                logger.error("Error trying to update raster with overviews from geoserver: doi=" + sjo.getDOI() + ", geoserver label=" + dgrf.getGeoserverLabel() + ", file name=" + fileName);
                return false;
            }

            try { createCoverstore(processBuilder, dgrf);
            }catch (InterruptedException | IOException f) {
                logger.error("Error trying to create a coverstore for raster from geoserver: doi=" + sjo.getDOI() + ", geoserver label=" + dgrf.getGeoserverLabel() + ", file name=" + fileName);
                return false;
            }

            try{ addRasterLayer(processBuilder, dgrf);
            }catch (InterruptedException | IOException f){
            logger.error("Error trying to add raster to geoserver: doi=" + sjo.getDOI() + ", geoserver label=" + dgrf.getGeoserverLabel() + ", file name=" + fileName);
                return false;
            }
        ExistingRasterRecords existingRasterRecords = ExistingRasterRecords.getExistingRasters();
        existingRasterRecords.addOrReplaceRecord(sjo.getDOI(),fileName);
        return true;
    }

        private void deleteOldCoverstore(ProcessBuilder processBuilder, DataverseGeoRecordFile dgrf) throws InterruptedException, IOException{
            String deleteCoveragestore = " admin:" + GEOSERVER_PASSWORD + " -XDELETE " + stringed("http://localhost:8080/geoserver/rest/workspaces/geodisy/coveragestores/" + dgrf.getGeoserverLabel() + "?recurse=true");
            Process p;
            processBuilder.command("curl", "-u", deleteCoveragestore);
            p = processBuilder.start();
            p.waitFor();
            p.destroy();

        }

        private void normalizeRaster(ProcessBuilder processBuilder, DataverseGeoRecordFile dgrf) throws InterruptedException, IOException {
            String warp = GDALWARP(DATA_DIR_LOC + sjo.getDOI().replace(".","/") + "/", dgrf.getOriginalTitle()+".tif");
            Process p;
            processBuilder.command("/usr/bin/bash", "-c", warp);
            p = processBuilder.start();
            p.waitFor();
            p.destroy();
        }

        private void renameRasterToOrig(ProcessBuilder processBuilder, String datasetID, String fileName) throws InterruptedException, IOException{
            String rename = "mv " + DATA_DIR_LOC + datasetID + "/1" + fileName + " " + DATA_DIR_LOC + datasetID + fileName;
            Process p;
            processBuilder.command("/usr/bin/bash", "-c", rename);
            p = processBuilder.start();
            p.waitFor();
            p.destroy();
        }

        private void addRasterOverviews(ProcessBuilder processBuilder, DataverseGeoRecordFile dgrf) throws InterruptedException, IOException {
            String addo = GDALADDO(DATA_DIR_LOC + sjo.getDOI().replace(".","/") + "/" + dgrf.getOriginalTitle()+".tif");
            Process p;
            processBuilder.command("/usr/bin/bash", "-c", addo);
            p = processBuilder.start();
            p.waitFor();
            p.destroy();
        }

        private void createCoverstore(ProcessBuilder processBuilder,  DataverseGeoRecordFile dgrf) throws InterruptedException, IOException{
            String createCoveragestore = " admin:" + GEOSERVER_PASSWORD + " -XPOST -H " + stringed("Content-type:text/xml") +  " -d '<coverageStore><name>" + dgrf.getGeoserverLabel() + "</name><workspace>geodisy</workspace><enabled>true</enabled><type>GeoTIFF</type><url>file:data/" + dgrf.getDatasetIdent() + dgrf.getFileName() + "</url></coverageStore>' " + stringed("http://localhost:8080/geoserver/rest/workspaces/geodisy/coveragestores?configure=all");
            Process p;
            processBuilder.command("curl", "-u", createCoveragestore);
            p = processBuilder.start();
            p.waitFor();
            p.destroy();
        }

        private void addRasterLayer(ProcessBuilder processBuilder, DataverseGeoRecordFile dgrf) throws InterruptedException, IOException{
        String addRasterLayer = " admin:" + GEOSERVER_PASSWORD + " -XPOST -H " + stringed("Content-type:application/xml") + " -d '<coverage><name>"+ dgrf.getGeoserverLabel() + "</name><nativeCRS>" + RASTER_CRS + "</nativeCRS><title>" + dgrf.getTranslatedTitle() + "</title><enabled>True</enabled></coverage>' " + stringed("http://localhost:8080/geoserver/rest/workspaces/geodisy/coveragestores/"+ dgrf.getGeoserverLabel() + "/coverages");
        Process p;
        processBuilder.command("curl", "-u", addRasterLayer);
        p = processBuilder.start();
        p.waitFor();
        p.destroy();
    }


    /// Below methods seem unused at the moment
    private void saveJsonToFile(String jsonString) {
        FileWriter fileWriter = new FileWriter();
        String doi = sjo.getDOI();
        String doiPath = doi.replace("/","_");
        String filePath = DATA_DIR_LOC + doiPath + "/import.json";
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

    /**TODO need to get vector files from POSTGRIS, is below method the way?
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

}
