package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
import Crosswalking.MetadataSchema;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

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
        int total = javaObject.getGeoDataFiles().size();
        if(total == 0){
            getRequiredFields();
            getOptionalFields();
            geoBlacklightJson = jo.toString();
            saveJSONToFile(geoBlacklightJson, doi);
        }else {
            for (DataverseRecordFile drf : javaObject.getGeoDataFiles()) {
                getRequiredFields(drf, total, count);
                getOptionalFields();
                geoBlacklightJson = jo.toString();
                saveJSONToFile(geoBlacklightJson, drf.getDoi(), drf.getUUID(doi + drf.getTitle()));

                count++;
            }
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
        File fileDir = new File("./"+localRepoPath + doi);
        if(!fileDir.exists())
            fileDir.mkdirs();
        return fileDir;
    }

    public String getDoi(){
        return doi;
    }
    protected abstract JSONObject getRequiredFields(DataverseRecordFile drf, int total, int thisFile);
    protected abstract JSONObject getRequiredFields();



    protected abstract JSONObject getOptionalFields();
    protected abstract void addMetadataDownloadOptions(DataverseRecordFile drf); //for records with datasetfiles
    protected abstract void addMetadataDownloadOptions(); //for records with only metadata
    protected abstract JSONArray addBaseMetadataDownloadOptions(); //adds the base metadata external services that all records need regardless of existance of datafiles
    protected abstract void saveJSONToFile(String json, String doi, String uuid);
    protected abstract void saveJSONToFile(String json, String doi);
}
