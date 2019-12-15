package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Author;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Description;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static BaseFiles.GeodisyStrings.*;
import static Crosswalking.GeoBlacklightJson.GeoBlacklightStrings.*;
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
        jo.put("dc_identifier_s", javaObject.getSimpleFieldVal(PERSISTENT_URL));
        String name = gbb.getField(FILE_NAME);
        jo.put("layer_slug_s", DataverseRecordFile.getUUID(getDoi()+name));
        if(total>1) {
            jo.put("dc_title_s", javaObject.getSimpleFields().getField(TITLE) + " (" + number + " of " + total + ")");
        }
        else
            jo.put("dc_title_s",javaObject.getSimpleFields().getField(TITLE));
        jo.put("dc_rights_s",javaObject.getSimpleFields().getField(LICENSE));
        jo.put("dct_provenance_s",javaObject.getSimpleFields().getField(PUBLISHER));

        jo.put("solr_geom","ENVELOPE(" + getBB(gbb) + ")");
        jo.put("layer_geom_type_s",gbb.getField(GEOMETRY));
        JSONArray ja = addBaseRecordInfo(gbb);
        String externalServices = "{";
        for(Object o:ja){
            if(!externalServices.equals("{"))
                externalServices += ",";
            externalServices += (String) o;
        }
        externalServices+="}";

        //TODO uncomment once I have geoserver upload figured out
        //ja = addMetadataDownloadOptions(gbb,ja);

        jo.put(EXTERNAL_SERVICES,externalServices);
        return jo;
    }

    private String getBB(GeographicBoundingBox gbb){
        return gbb.getWestLongitude() + ", " + gbb.getEastLongitude() + ", " + gbb.getNorthLatitude() + ", " + gbb.getSouthLatitude();
    }

    @Override
    protected JSONArray addMetadataDownloadOptions(GeographicBoundingBox gbb, JSONArray ja) {
        if(gbb.isGeneratedFromGeoFile()) {
            if(gbb.isWMS())
                ja.put(WMS + stringed(gbb.getGeoserverLocation()));
            if(gbb.isWFS())
                ja.put(WFS + stringed(gbb.getGeoserverLocation()));
        }
        //TODO uncomment once pushing to OpenGeoMetadata is working
        //ja.put(ISO_METADATA + stringed(gbb.getOpenGeoMetaLocation()));

        return ja;
    }

    @Override
    protected JSONArray addBaseRecordInfo(GeographicBoundingBox gbb){
        JSONArray ja = new JSONArray();
        ja.put(RECORD_URL + stringed(javaObject.getSimpleFieldVal(PERSISTENT_URL)));
        ja.put(ISO_METADATA + stringed(END_XML_JSON_FILE_PATH + javaObject.getSimpleFieldVal(PERSISTENT_ID) + "/" + ISO_METADATA_FILE_ZIP));
        return ja;
    }

    private String stringed(String string) {
        return "\"" + string + "\"";
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

    /*This version is only be activated when the GeoBlacklight schema changes to allow multiple values
    private void getLanguages() {
        JSONArray ja = new JSONArray();
        List<String> languages = javaObject.getCitationFields().getListField(LANGUAGE);
        for(String s : languages){
            ja.put(s);
        }
        jo.put("dc_language_s",ja);
    }
     */
    // This version is only to be used while the GeoBlacklight schema doesn't allow multiple value, when that happens use the method directly above
    private void getLanguages() {
        String languageStrings = "";
        List<String> languages = javaObject.getCitationFields().getListField(LANGUAGE);
        for (String s : languages) {
            if (languageStrings.isEmpty())
                languageStrings = s;
            else
                languageStrings = languageStrings + ", " + s;
        }
        jo.put("dc_language_s", languageStrings);
    }

    private void getIssueDate() {
        String dateString = javaObject.getSimpleFields().getField(PUB_DATE);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'");
        LocalDate date = LocalDate.parse(dateString);
        LocalDateTime dateTime = date.atStartOfDay();
        String dateTimeString = dateTime.format(formatter);
        jo.put("dct_issued_dt",dateTimeString);
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
        String name = folderName;
        String end = "";
        if(folderName.contains("(")){
            name = folderName.substring(0,folderName.indexOf(" ("));
            end = "/" + getNumber(folderName);
        }
        genDirs(name + end, OPEN_METADATA_LOCAL_REPO);
        BaseFiles.FileWriter file = new BaseFiles.FileWriter();
        try {
            file.writeStringToFile(json,"./"+OPEN_METADATA_LOCAL_REPO + name + end + "/" +"geoblacklight.json");
        } catch (IOException e) {
            logger.error("Something went wrong trying to create a JSON file with doi:" + doi);
        }

    }

    private String getNumber(String folderName) {
        int start = folderName.indexOf("File ") + 5;
        int end = folderName.indexOf(" of");
        return folderName.substring(start,end);
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
