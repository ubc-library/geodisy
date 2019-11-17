package Crosswalking.GeoBlacklightJson;

import BaseFiles.FileWriter;
import Crosswalking.MetadataSchema;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import java.util.LinkedList;
import java.util.List;


import static Crosswalking.GeoBlacklightJson.GeoBlacklightStrings.*;

/**
 * Takes a DataverseJavaObject and creates a GeoBlacklight JSON from it
 */
public abstract class GeoBlacklightJSON implements JSONCreator, MetadataSchema {
    protected DataverseJavaObject javaObject;
    protected String geoBlacklightJson;
    protected JSONObject jo;
    protected String doi;
    boolean download = false;
    List<DataverseRecordFile> files;

    public GeoBlacklightJSON() {
        this.jo = new JSONObject();
    }

    @Override
    public void createJson() {
        int count = 1;
        LinkedList<GeographicBoundingBox> gbbs = javaObject.getGeoFields().getBBoxesForJSON();
        int size = gbbs.size();
        for(int i = 0; i<size; i++){
            getRequiredFields(gbbs.get(i),i,size);
            getOptionalFields();
            geoBlacklightJson = jo.toString();
            saveJSONToFile(geoBlacklightJson, doi, DataverseRecordFile.getUUID(doi + i));
        }
    }

    protected void addWMS() {
            JSONArray ja = jo.getJSONArray(EXTERNAL_SERVICES);
            ja.put("http://www.opengis.net/def/serviceType/ogc/wms");
            ja.put(GEOSERVER_WMS_LOCATION);
            jo.put(EXTERNAL_SERVICES, ja);
    }

    protected void addWFS() {
            JSONArray ja = jo.getJSONArray(EXTERNAL_SERVICES);
            ja.put("http://www.opengis.net/def/serviceType/ogc/wfs");
            ja.put(GEOSERVER_WFS_LOCATION);
            jo.put(EXTERNAL_SERVICES, ja);
    }

    public File genDirs(String doi, String localRepoPath) {
        doi = FileWriter.fixPath(doi);
        File fileDir = new File("./"+localRepoPath + doi);
        if(!fileDir.exists())
            fileDir.mkdirs();
        return fileDir;
    }

    public String getDoi(){
        return doi;
    }
    protected abstract JSONObject getRequiredFields(GeographicBoundingBox gbb, int number, int total);



    protected abstract JSONObject getOptionalFields();
    protected abstract void addMetadataDownloadOptions(DataverseRecordFile drf); //for records with datasetfiles
    protected abstract void addMetadataDownloadOptions(); //for records with only metadata
    protected abstract JSONArray addBaseMetadataDownloadOptions(); //adds the base metadata external services that all records need regardless of existance of datafiles
    protected abstract void saveJSONToFile(String json, String doi, String uuid);
}
