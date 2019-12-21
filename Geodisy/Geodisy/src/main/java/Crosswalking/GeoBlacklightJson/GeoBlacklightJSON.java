package Crosswalking.GeoBlacklightJson;

import BaseFiles.FileWriter;
import Crosswalking.MetadataSchema;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import Dataverse.SourceRecordFiles;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import java.util.LinkedList;
import java.util.List;


import static Crosswalking.GeoBlacklightJson.GeoBlacklightStrings.*;

/**
 * Takes a DataverseJavaObject and creates a GeoBlacklight JSON from it
 */
public abstract class GeoBlacklightJSON extends JSONCreator implements MetadataSchema {
    protected DataverseJavaObject javaObject;
    protected String geoBlacklightJson;
    protected JSONObject jo;
    protected String doi;
    boolean download = false;
    List<DataverseRecordFile> oldFiles;
    SourceRecordFiles files;

    public GeoBlacklightJSON() {
        this.jo = new JSONObject();
        files = SourceRecordFiles.getSourceRecords();
    }

    @Override
    public void createJson() {
        int count = 1;
        LinkedList<GeographicBoundingBox> gbbs = javaObject.getGeoFields().getBBoxesForJSON();
        int size = gbbs.size();
        for(int i = 0; i<size; i++){
            getRequiredFields(gbbs.get(i),i+1,size);
            getOptionalFields();
            geoBlacklightJson = jo.toString();
            if(size>1)
                saveJSONToFile(geoBlacklightJson, doi, doi + " (File " + (i+1) + " of " + size + ")");
            else
                saveJSONToFile(geoBlacklightJson, doi, doi);
        }
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
    protected abstract JSONArray addMetadataDownloadOptions(GeographicBoundingBox gbb, JSONArray ja); //for records with datasetfiles
    protected abstract JSONArray addBaseRecordInfo(GeographicBoundingBox gbb); //adds the base metadata external services that all records need regardless of existence of datafiles
    protected abstract void saveJSONToFile(String json, String doi, String folderName);
}
