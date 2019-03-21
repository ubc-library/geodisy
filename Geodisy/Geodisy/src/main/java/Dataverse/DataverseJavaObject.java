package Dataverse;



import Crosswalking.JSONParsing.DataverseParser;

import Dataverse.DataverseJSONFieldClasses.Fields.CompoundField.*;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.*;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.SimpleFields;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

/**
 * Java object structure to parse Dataverse Json into
 * May need to change field types for dates, URLs, and/or email addresses.
 * Also need to test to see how textboxes work with this.
 *
 * Dataverse Alias: Java Class: JavaObject Variable
 *
 *
 */

public class DataverseJavaObject {
    private CitationFields citationFields;
    private GeographicFields geoFields;
    private Logger logger = LogManager.getLogger(DataverseParser.class);

    public DataverseJavaObject() {
        this.citationFields = new CitationFields();
        this.geoFields = new GeographicFields();
    }

    public void parseGeospatialFields(JSONArray jsonArray) {
        for(Object o: jsonArray){
            JSONObject jo = (JSONObject) o;
            geoFields.setFields(jo);
        }
    }
    public void parseCitationFields(JSONObject current) {
        citationFields.setBaseFields(current);
        JSONObject metadata = current.getJSONObject("latestVersion").getJSONObject("metadataBlocks");
        JSONArray currentArray = metadata.getJSONObject(CITATION).getJSONArray(FIELDS);
        for (Object o : currentArray) {
            JSONObject jo = (JSONObject) o;
            citationFields.setFields(jo);
        }
        geoFields.setDoi(citationFields.getSimpleFields().getField(ALT_URL));
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
}
