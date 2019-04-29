package Dataverse;



import BaseFiles.GeodisyStrings;
import Crosswalking.JSONParsing.DataverseParser;

import DataSourceLocations.Dataverse;
import Dataverse.DataverseJSONFieldClasses.Fields.CompoundField.*;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.*;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.SimpleFields;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static Dataverse.DVFieldNameStrings.*;

/**
 * Java object structure to parse Dataverse Json into
 * May need to change field types for dates, URLs, and/or email addresses.
 *
 * Dataverse Alias: Java Class: JavaObject Variable
 *
 *
 */

public class DataverseJavaObject {
    private CitationFields citationFields;
    private GeographicFields geoFields;
    private Logger logger = LogManager.getLogger(DataverseJavaObject.class);
    private List<DataverseRecordFile> dataFiles; //Stores the datafiles
    private String server;
    private boolean hasContent;

    public DataverseJavaObject(String server) {
        this.citationFields = new CitationFields();
        this.dataFiles = new LinkedList<>();
        this.geoFields = new GeographicFields("placeholder");
        this.server = server;
        hasContent = false;
    }

    /**
     * Gets the Citation metadata for the record
     * @param citationFieldsArray
     */
    public void parseCitationFields(JSONObject citationFieldsArray) {
        citationFields.setBaseFields(citationFieldsArray);
        JSONObject metadata = getVersionSection(citationFieldsArray).getJSONObject("metadataBlocks");
        JSONArray currentArray = metadata.getJSONObject(CITATION).getJSONArray(FIELDS);
        for (Object o : currentArray) {
            JSONObject jo = (JSONObject) o;
            citationFields.setFields(jo);
        }
        hasContent=true;
        geoFields = new GeographicFields(citationFields.getDOI());
    }

    /**
     * Gets the Geospatial metadata for the record
     * @param geoFieldsArray
     */
    public void parseGeospatialFields(JSONArray geoFieldsArray) {
        for(Object o: geoFieldsArray){
            JSONObject jo = (JSONObject) o;
            this.geoFields.setFields(jo);
        }
    }

    /**
     * Gets the File metadata for the record.
     * @param fileFieldsArray
     */
    public void parseFiles(JSONArray fileFieldsArray) {
        for(Object o: fileFieldsArray){
            JSONObject jo = (JSONObject) o;
            if(jo.getBoolean("restricted"))
                continue;
            String title = jo.getString("label");
            JSONObject dataFile = (JSONObject) jo.get("dataFile");
            DataverseRecordFile dRF;
            //Some Dataverses don't have individual DOIs for files, so for those I will use the database's file id instead
            if(dataFile.has(DOI)&& !dataFile.getString(DOI).equals("")) {
                String doi = dataFile.getString(DOI);
                dRF = new DataverseRecordFile(title, doi,dataFile.getInt("id"), server, citationFields.getDOI());
            }else{
                int dbID = (int) dataFile.get("id");
                dRF = new DataverseRecordFile(title,dbID,server,citationFields.getDOI());
            }
            dataFiles.add(dRF);
        }
    }
    //if changed, need to change copy in CitationFields Class
    public JSONObject getVersionSection(JSONObject current) {
        if(current.has("latestVersion"))
            return current.getJSONObject("latestVersion");
        else if(current.has("datasetVersion"))
            return current.getJSONObject("datasetVersion");
        else{
            logger.error("missing a _____Version field in the dataverseJson in $s", current.toString());
            return new JSONObject();
        }
    }

    public GeographicFields getGeographicFields() {
        return geoFields;
    }

    public void setGeoFields(GeographicFields geographicFields) {
        this.geoFields = geographicFields;
    }

    public BoundingBox getBoundingBox(){
        return geoFields.getBoundingBox();
    }

    public SimpleFields getSimpleFields() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields;
    }

    public void setSimpleFields(SimpleFields simpleFields) {
        citationFields.setSimpleFields(simpleFields);
    }
    public boolean hasBoundingBox(){
        return geoFields.hasBB();
    }

    public String getDOI(){
        return citationFields.getDOI();
    }

    public void setBoundingBox(BoundingBox b){
        geoFields.setFullBB(b);
    }

    // Following methods are for getting the simple field values.
    // May end up deleting all of these and just dealing with them inside the CitationFields class
    public String getTitle() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(TITLE);
    }

    public void setTitle(String title) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(TITLE,title);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getSubtitle() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(SUBJECT); }

    public void setSubtitle(String subtitle) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(SUBJECT,subtitle);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getAlternativeTitle() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(ALT_TITLE);
    }

    public void setAlternativeTitle(String alternativeTitle) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(ALT_TITLE,alternativeTitle);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getAlternativeURL() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(ALT_URL);
    }

    public void setAlternativeURL(String alternativeURL) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(ALT_URL,alternativeURL);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getLicense() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(LICENSE);
    }

    public void setLicense(String license) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(LICENSE,license);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getNotesText() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(NOTES_TEXT);
    }

    public void setNotesText(String notesText) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(NOTES_TEXT,notesText);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getProductionPlace() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(PROD_PLACE);
    }

    public void setProductionPlace(String productionPlace) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(PROD_PLACE,productionPlace);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getDepositor() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(DEPOSITOR);
    }

    public void setDepositor(String depositor) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(DEPOSITOR,depositor);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getOriginOfSources() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(ORIG_OF_SOURCES);
    }

    public void setOriginOfSources(String originOfSources) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(ORIG_OF_SOURCES, originOfSources);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getCharacteristicOfSources() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(CHAR_OF_SOURCES);
    }

    public void setCharacteristicOfSources(String characteristicOfSources) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(CHAR_OF_SOURCES,characteristicOfSources);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getAccessToSources() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(ACCESS_TO_SOURCES);
    }

    public void setAccessToSources(String accessToSources) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(ACCESS_TO_SOURCES, accessToSources);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getProductionDate() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(PROD_DATE);
    }

    public void setProductionDate(String productionDate) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(PROD_DATE, productionDate);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getDistributionDate() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(DIST_DATE);
    }

    public void setDistributionDate(String distributionDate) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(DIST_DATE, distributionDate);
        citationFields.setSimpleFields(simpleFields);
    }

    public String getDateOfDeposit() {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        return simpleFields.getField(DEPOS_DATE);
    }

    public void setDateOfDeposit(String dateOfDeposit) {
        SimpleFields simpleFields = citationFields.getSimpleFields();
        simpleFields.setField(DEPOS_DATE,dateOfDeposit);
        citationFields.setSimpleFields(simpleFields);
    }

    public CitationFields getCitationFields() {
        return citationFields;
    }

    public void setCitationFields(CitationFields citationFields) {
        this.citationFields = citationFields;
    }

    public GeographicFields getGeoFields() {
        return geoFields;
    }



    public int getVersion(){
        return getCitationFields().getVersion();
    }

    public DataverseRecordInfo generateDRI(){
        String major, minor,doi;
        major = getCitationFields().getSimpleFields().getVersionMajor();
        minor = getCitationFields().getSimpleFields().getVersionMinor();
        doi = getCitationFields().getDOI();
        DataverseRecordInfo answer = new DataverseRecordInfo();
        answer.setDoi(doi);
        answer.setMajor(major);
        answer.setMinor(minor);
        return answer;
    }
    public boolean hasContent(){
        return hasContent;
    }

    /**
     *  Deletes the directory of the record's files if it exists and then downloads the updated
     *  files, excluding non-geospatial file types other than .zip
     *  If there aren't any geospatial files uploaded, the entire directory get's deleted.
     */
    public void downloadFiles() {
        File f = new File("datasetFiles\\\\" + urlized(citationFields.getDOI()));
        deleteDir(f);
        for (DataverseRecordFile dRF : dataFiles) {
            if(fileTypeToIgnore(dRF.title))
                continue;
            dRF.getFile();
        }
        if(f.list().length==0)
            f.delete();

    }

    private boolean fileTypeToIgnore(String title) {
        for(String s: GeodisyStrings.FILE_TYPES_TO_IGNORE){
            if(title.endsWith(s))
                return true;
        }
        return false;
    }

    private String urlized(String doi) {
        doi = doi.replaceAll("\\.","_");
        return doi.replaceAll("/","_");
    }

    private void deleteDir(File f) {
        File[] files = f.listFiles();
        if(files != null && files.length >0) {
            for (File myFile : files) {
                if (myFile.isDirectory()) {
                    deleteDir(myFile);
                }
                myFile.delete();
            }
        }
        f.delete();
    }

}
