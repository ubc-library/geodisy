package Crosswalking.GeoBlacklightJson;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import Crosswalking.MetadataSchema;
import Dataverse.DataverseGeoRecordFile;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import Dataverse.SourceRecordFiles;
import _Strings.GeodisyStrings;
import org.json.JSONObject;

import java.io.File;

import java.util.LinkedList;
import java.util.List;

import static _Strings.DVFieldNameStrings.TITLE;
import static _Strings.GeodisyStrings.GEODISY_PATH_ROOT;

/**
 * Takes a DataverseJavaObject and creates a GeoBlacklight JSON from it
 */
public abstract class GeoBlacklightJSON extends JSONCreator implements MetadataSchema {
    protected DataverseJavaObject javaObject;
    protected String geoBlacklightJson;
    protected JSONObject jo;
    protected String doi;
    boolean download = false;
    GeoLogger logger;
    LinkedList<DataverseGeoRecordFile> geoFiles;
    LinkedList<DataverseGeoRecordFile> geoMeta;
    SourceRecordFiles files;

    public GeoBlacklightJSON() {
        this.jo = new JSONObject();
        files = SourceRecordFiles.getSourceRecords();
        logger = new GeoLogger(this.getClass());
    }

    public void createJson() {
        int countFile = geoFiles.size();
        int countMeta = geoMeta.size();
        System.out.println("DOI = " + doi + " . Number geoFile: " + countFile + " . Number geoMeta: " + countMeta);
        boolean geoMoreThanMeta = countFile >= countMeta;
        if(!geoMoreThanMeta && countFile>0)
            logger.info("More bounding boxes came from the metadata than the files, but there were file-generated bounding boxes as well. Check " + doi + " to make sure we shouldn't actually be using the files instead", javaObject);
        List<DataverseGeoRecordFile> list = geoMoreThanMeta? geoFiles : geoMeta;
        if(list.size()>1){
            int count = 1;
            for(DataverseGeoRecordFile d:list){
                d.setBbCount(count);
                count++;
            }

        }
        int total = list.size();
        for(DataverseRecordFile drf:list){
            createJSONFromFiles(drf, total);
        }
    }

    private void createJSONFromFiles(DataverseRecordFile drf, int total) {
        boolean single = total == 1;
        getRequiredFields(drf.getGBB(), total, drf.getBbCount());
        getOptionalFields(drf,total);
        geoBlacklightJson = jo.toString();
        String slash = GeodisyStrings.replaceSlashes("/");
        if(!javaObject.getSimpleFields().getField(TITLE).isEmpty())
            if (!single)
                saveJSONToFile(geoBlacklightJson, doi, GeodisyStrings.replaceSlashes(GeodisyStrings.removeHTTPSAndReplaceAuthority(doi)) + slash+drf.getBbCount());
            else
                saveJSONToFile(geoBlacklightJson, doi, GeodisyStrings.replaceSlashes(GeodisyStrings.removeHTTPSAndReplaceAuthority(doi)));
    }

    public File genDirs(String doi, String localRepoPath) {
        doi = FileWriter.fixPath(doi);
        localRepoPath = FileWriter.fixPath(localRepoPath);
        File fileDir = new File(GeodisyStrings.replaceSlashes(GEODISY_PATH_ROOT +localRepoPath + GeodisyStrings.removeHTTPSAndReplaceAuthority(doi).replace(".","/")));
        if(!fileDir.exists())
            fileDir.mkdirs();
        return fileDir;
    }

    public String getDoi(){
        return doi;
    }
    protected abstract JSONObject getRequiredFields(GeographicBoundingBox gbb, int total, int bboxNumber);



    protected abstract JSONObject getOptionalFields(DataverseRecordFile drf, int totalRecordsInStudy);
    protected abstract JSONObject addDataDownloadOptions(GeographicBoundingBox bb, JSONObject ja, boolean isOnGeoserver); //for records with datasetfiles
    protected abstract JSONObject addBaseRecordInfo(); //adds the base metadata external services that all records need regardless of existence of datafiles
    protected abstract void saveJSONToFile(String json, String doi, String folderName);

}
