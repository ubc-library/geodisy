package Crosswalking.GeoBlacklightJson;

import BaseFiles.FileWriter;
import BaseFiles.GeodisyStrings;
import Crosswalking.MetadataSchema;
import DataSourceLocations.Dataverse;
import Dataverse.DataverseGeoRecordFile;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Description;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.SourceRecordFiles;
import GeoServer.GeoServerAPI;
import GeoServer.PostGIS;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import java.util.LinkedList;
import java.util.List;

import static BaseFiles.GeodisyStrings.*;
import static Crosswalking.GeoBlacklightJson.GeoBlacklightStrings.GEOBLACKLIGHT_BASE;
import static Dataverse.DVFieldNameStrings.DS_DESCRIPT;
import static Dataverse.DVFieldNameStrings.FILE_NAME;

/**
 * Takes a DataverseJavaObject and creates a GeoBlacklight JSON from it
 */
public abstract class GeoBlacklightJSON extends JSONCreator implements MetadataSchema {
    protected DataverseJavaObject javaObject;
    protected String geoBlacklightJson;
    protected JSONObject jo;
    protected String doi;
    boolean download = false;
    LinkedList<DataverseGeoRecordFile> geoFiles;
    LinkedList<DataverseGeoRecordFile> geoMeta;
    SourceRecordFiles files;

    public GeoBlacklightJSON() {
        this.jo = new JSONObject();
        files = SourceRecordFiles.getSourceRecords();
    }

    @Override
    public void createJson() {
        int countFile = geoFiles.size();
        int countMeta = geoMeta.size();
        System.out.println("DOI = " + doi + " . Number geoFile: " + countFile + " . Number geoMeta: " + countMeta);
        List<DataverseGeoRecordFile> list = (countFile >= countMeta)? geoFiles : geoMeta;
        int count = 1;
        int total = list.size();
        int innerCount = 1;
        List<Description> description = javaObject.getCitationFields().getListField(DS_DESCRIPT);
        description.add(new Description("Connected GeoBlacklight records:"));
        for(DataverseGeoRecordFile drf:list){
            description.add(new Description("(" + innerCount + " of " + total + ") - " + GEOBLACKLIGHT_BASE + drf.getGeoserverLabel()));
        }
        for(DataverseRecordFile drf:list){
            createJSONFromFiles(drf,count,total);
            count++;
        }
    }

    private void createJSONFromFiles(DataverseRecordFile drf, int count, int total) {
        boolean single = total == 1;
            getRequiredFields(drf.getGBB(), count, total);
            getOptionalFields(drf.getGBB());
            geoBlacklightJson = jo.toString();
            if (!single)
                saveJSONToFile(geoBlacklightJson, doi, doi + " (File " + (count) + " of " + total + ")");
            else
                saveJSONToFile(geoBlacklightJson, doi, doi);
        //Don't think I need this here, I'm making the geoserver calls earlier
        /*String name = drf.getTranslatedTitle();
        String transformType = RASTER;
        if(name.endsWith(".shp")) {
            addToPostGIS(name);
            transformType = VECTOR;
        }
        addToGeoserver(name,transformType);*/
    }

    public File genDirs(String doi, String localRepoPath) {
        doi = FileWriter.fixPath(doi);
        localRepoPath = FileWriter.fixPath(localRepoPath);
        File fileDir = new File(localRepoPath + doi.replace(".","/"));
        if(!fileDir.exists())
            fileDir.mkdirs();
        return fileDir;
    }

    //I believe I'm calling Geoserver earlier, so don't need these
    /*private void addToGeoserver(String nameStub, String transformType) {
        GeoServerAPI geoServerAPI = new GeoServerAPI(javaObject);
        if(transformType.equals(VECTOR))
            geoServerAPI.uploadVector(nameStub);
        else
            geoServerAPI.uploadRaster(nameStub);//TODO need to remove this and add raster upload call
    }

    private void addToPostGIS(String nameStub) {
        PostGIS postGis = new PostGIS();
        postGis.addFile2PostGIS(javaObject,nameStub+".shp",VECTOR, TEST);
        *//* Raster Data is added to Geoserver directly rather than through PostGIS*//*

    }*/

    public String getDoi(){
        return doi;
    }
    protected abstract JSONObject getRequiredFields(GeographicBoundingBox gbb, int number, int total);



    protected abstract JSONObject getOptionalFields(GeographicBoundingBox gbb);
    protected abstract JSONArray addDataDownloadOptions(GeographicBoundingBox bb, JSONArray ja); //for records with datasetfiles
    protected abstract JSONArray addBaseRecordInfo(); //adds the base metadata external services that all records need regardless of existence of datafiles
    protected abstract void saveJSONToFile(String json, String doi, String folderName);
}
