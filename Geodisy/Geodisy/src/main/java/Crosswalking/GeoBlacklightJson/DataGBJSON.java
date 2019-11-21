package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
import DataSourceLocations.Dataverse;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Author;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Description;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import static BaseFiles.GeodisyStrings.XML_NS;
import static Crosswalking.GeoBlacklightJson.GeoBlacklightStrings.EXTERNAL_SERVICES;
import static Crosswalking.GeoBlacklightJson.GeoBlacklightStrings.METADATA_DOWNLOAD_SERVICES;
import static Crosswalking.XML.XMLTools.XMLStrings.OPEN_METADATA_LOCAL_REPO;
import static Crosswalking.XML.XMLTools.XMLStrings.TEST_OPEN_METADATA_LOCAL_REPO;
import static Dataverse.DVFieldNameStrings.*;

public class DataGBJSON extends GeoBlacklightJSON{
    GeoLogger logger;
    private GeographicBoundingBox gbb;

    public DataGBJSON(DataverseJavaObject djo) {
        super();
        javaObject = djo;
        geoBlacklightJson = "";
        doi = djo.getDOI();
        logger = new GeoLogger(this.getClass());
        files = djo.getGeoDataFiles();
    }
    //TODO check if Dataverse publisher field be included in the slug?
    @Override
    protected JSONObject getRequiredFields(GeographicBoundingBox gbb, int number, int total){
        jo.put("geoblacklight_version","1.0");
        jo.put("dc_identifier_s", javaObject.getDOI());
        String name = gbb.getField(FILE_NAME);
        if(!name.isEmpty()) {
            name = "/" + name;
        }
        jo.put("layer_slug_s", DataverseRecordFile.getUUID(getDoi()+name));
        if(total>1) {
            jo.put("dc_title_s", javaObject.getSimpleFields().getField(TITLE) + name + " (" + number + " of " + total + ")");
        }
        else
            jo.put("dc_title_s",javaObject.getSimpleFields().getField(TITLE) + name);
        jo.put("dc_rights_s",javaObject.getSimpleFields().getField(LICENSE));
        jo.put("dct_provenance_s",javaObject.getSimpleFields().getField(PUBLISHER));

        jo.put("solr_geom","ENVELOPE(" + getBB(gbb) + ")");
        jo.put("layer_geom_type_s",gbb.getField(GEOMETRY));
        addMetadataDownloadOptions();
        return jo;
    }

    private String getBB(GeographicBoundingBox gbb){
        return gbb.getWestLongitude() + ", " + gbb.getEastLongitude() + ", " + gbb.getNorthLatitude() + ", " + gbb.getSouthLatitude();
    }

    @Override
    protected void addMetadataDownloadOptions(DataverseRecordFile drf) {
        JSONArray ja = addBaseMetadataDownloadOptions();
        jo.put(EXTERNAL_SERVICES,ja);
        addWFS();
        if(drf.isPreviewable())
            addWMS();
    }
    @Override
    protected void addMetadataDownloadOptions() {
        JSONArray ja = addBaseMetadataDownloadOptions();
        jo.put(EXTERNAL_SERVICES,ja);
    }

    @Override
    protected JSONArray addBaseMetadataDownloadOptions(){
        JSONArray ja = new JSONArray();
        for(String s:METADATA_DOWNLOAD_SERVICES) {
         ja.put(s);
        }
        return ja;
    }
    //TODO, check I am getting all the optional fields I should be
    @Override
    protected JSONObject getOptionalFields() {
        getDSDescription();
        getAuthors();
        getIssueDate();
        getLanguages();
        getSubjects();
        getType();
        return jo;
    }

    private void getType() {
        jo.put("dc_type_s","dataset");
    }

    private void getSubjects() {
        JSONArray ja = new JSONArray();
        List<String> subjects = javaObject.getCitationFields().getListField(SUBJECT);
        for(String s : subjects){
            ja.put(s);
        }
        jo.put("dc_subject_sm",ja);
    }

    private void getLanguages() {
        JSONArray ja = new JSONArray();
        List<String> languages = javaObject.getCitationFields().getListField(LANGUAGE);
        for(String s : languages){
            ja.put(s);
        }
        jo.put("dc_language_s",ja);
    }

    private void getIssueDate() {
        jo.put("dct_issued_dt",javaObject.getSimpleFields().getField(PUB_DATE));
    }


    private void getAuthors() {
        JSONArray ja = new JSONArray();
        List<Author> authors = javaObject.getCitationFields().getListField(AUTHOR);
        for(Author a:authors){
            ja.put(a.getField(AUTHOR_NAME));
        }
        jo.put("dc_creator_sm",ja);
    }

    private void getDSDescription() {
        JSONArray ja = new JSONArray();
        List<Description> descriptions = javaObject.getCitationFields().getListField(DS_DESCRIPT);
        for(Description d:descriptions){
            ja.put(d.getDsDescriptionValue());
        }
        jo.put("dc_description_s",ja);
    }

    public void saveJSONToFile(String json, String doi, String folderName){
        genDirs(doi + "/" + folderName, OPEN_METADATA_LOCAL_REPO);
        BaseFiles.FileWriter file = new BaseFiles.FileWriter();
        try {
            file.writeStringToFile(json,"./"+OPEN_METADATA_LOCAL_REPO + doi + "/" + folderName + "/" +"geoblacklight.json");
        } catch (IOException e) {
            logger.error("Something went wrong trying to create a JSON file with doi:" + doi);
        }

    }

    public void saveJSONToTestFile(String json, String doi, String uuid){
        genDirs(doi, TEST_OPEN_METADATA_LOCAL_REPO);
        BaseFiles.FileWriter file = new BaseFiles.FileWriter();
        try {
            file.writeStringToFile(json,"./"+TEST_OPEN_METADATA_LOCAL_REPO + doi + "/" + uuid + "/" +"geoblacklight.json");
        } catch (IOException e) {
            logger.error("Something went wrong trying to create a JSON file with doi:" + doi);
        }

    }
}
