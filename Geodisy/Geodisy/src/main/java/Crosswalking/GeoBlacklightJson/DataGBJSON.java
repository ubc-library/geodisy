package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Author;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Description;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static BaseFiles.GeodisyStrings.XML_NS;
import static Crosswalking.GeoBlacklightJson.GeoBlacklightStrings.EXTERNAL_SERVICES;
import static Crosswalking.XML.XMLTools.XMLStrings.OPEN_METADATA_LOCAL_REPO;
import static Crosswalking.XML.XMLTools.XMLStrings.TEST_OPEN_METADATA_LOCAL_REPO;
import static Dataverse.DVFieldNameStrings.*;

public class DataGBJSON extends GeoBlacklightJSON{
    GeoLogger logger;
    public DataGBJSON(DataverseJavaObject djo) {
        super();
        javaObject = djo;
        geoBlacklightJson = "";
        doi = djo.getDOI();
        logger = new GeoLogger(this.getClass());
    }

    @Override
    protected JSONObject getRequiredFields() {
        jo.put("geoblacklight_version","1.0");
        jo.put("dc_identifier_s", javaObject.getDOI());
        jo.put("layer_slug_s",javaObject.getSimpleFields().getField(IDENTIFIER));
        jo.put("dc_title_s",javaObject.getSimpleFields().getField(TITLE));
        jo.put("dc_rights_s",javaObject.getSimpleFields().getField(LICENSE));
        jo.put("dct_provenance_s",javaObject.getSimpleFields().getField(PUBLISHER));
        jo.put("solr_geom","ENVELOPE" + getBB());
        addMetadataDownloadOptions();
        return jo;
    }

    private String getBB() {
        BoundingBox bb = javaObject.getBoundingBox();
        return "(" + bb.getLongWest()+ ", " + bb.getLongEast()+ ", " + bb.getLatNorth()+ ", " + bb.getLatSouth() + ")";
    }

    @Override
    protected void addMetadataDownloadOptions() {
        JSONArray ja = new JSONArray();
        ja.put(XML_NS + "mdb/2.0"); //ISO 19139
        ja.put("http://www.opengis.net/cat/csw/csdgm/");
        ja.put("http://www.loc.gov/mods/v3");
        ja.put("http://www.w3.org/1999/xhtml");
        ja.put(("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""));
        ja.put("xsi:schemaLocation=\"http://standards.iso.org/iso/19115/-3/mdb/2.0/mdb.xsd\">");
        jo.put(EXTERNAL_SERVICES,ja);
    }

    //TODO, check I am getting all the optional fields I should be
    @Override
    protected JSONObject getOptionalFields() {
        if(javaObject.hasGeospatialFile) {
            jo.put(EXTERNAL_SERVICES, new JSONArray());
            addWebService();
        }
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

    @Override
    protected void addWebService() {
        List<DataverseRecordFile> recordFiles = javaObject.getDataFiles();
        boolean wms = false;
        boolean wfs = false;
        String title;
        for(DataverseRecordFile drf:recordFiles){
            title = drf.getTitle().toLowerCase();
            checkReferences(title);
        }
    }

    public void saveJSONToFile(String json, String doi){
        genDirs(doi, OPEN_METADATA_LOCAL_REPO);
        BaseFiles.FileWriter file = new BaseFiles.FileWriter();
        try {
            file.writeStringToFile(json,"./"+OPEN_METADATA_LOCAL_REPO + doi + "/" + "geoblacklight.json");
        } catch (IOException e) {
            logger.error("Something went wrong trying to create a JSON file with doi:" + doi);
        }

    }

    public void saveJSONToTestFile(String json, String doi){
        genDirs(doi, TEST_OPEN_METADATA_LOCAL_REPO);
        BaseFiles.FileWriter file = new BaseFiles.FileWriter();
        try {
            file.writeStringToFile(json,"./"+TEST_OPEN_METADATA_LOCAL_REPO + doi + "/" + "geoblacklight.json");
        } catch (IOException e) {
            logger.error("Something went wrong trying to create a JSON file with doi:" + doi);
        }

    }
}
