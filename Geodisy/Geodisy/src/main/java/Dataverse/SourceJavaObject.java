package Dataverse;

import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public abstract class SourceJavaObject {
    protected CitationFields citationFields;
    protected GeographicFields geoFields;
    protected List<DataverseRecordFile> dataFiles; //Stores the datafiles
    protected String server;
    protected boolean hasContent;

    public abstract void parseCitationFields(JSONObject citationFieldsArray);
    public abstract void parseGeospatialFields(JSONArray geoFieldsArray);
    public abstract void parseFiles(JSONArray fileFieldsArray);
    public abstract JSONObject getVersionSection(JSONObject current);
    public abstract void downloadFiles();

    public SourceJavaObject(String server) {
        this.citationFields = new CitationFields();
        this.dataFiles = new LinkedList<>();
        this.geoFields = new GeographicFields("placeholder");
        this.server = server;
        hasContent = false;
    }
    public SimpleCitationFields getSimpleFields(){
        return citationFields.getSimpleCitationFields();
    }

    public boolean fileTypeToIgnore(String title) {
        for (String s : GeodisyStrings.FILE_TYPES_TO_IGNORE) {
            if (title.endsWith(s))
                return true;
        }
        return false;
    }
    protected String urlized(String doi) {
        doi = doi.replaceAll("\\.","_");
        return doi.replaceAll("/","_");
    }
    protected void deleteDir(File f) {
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
    public String getDOI(){
        return citationFields.getDOI();
    }
    public int getVersion(){
        return getCitationFields().getVersion();
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

    public void setGeoFields(GeographicFields geoFields) {
        this.geoFields = geoFields;
    }

    public List<DataverseRecordFile> getDataFiles() {
        return dataFiles;
    }

    public void setDataFiles(List<DataverseRecordFile> dataFiles) {
        this.dataFiles = dataFiles;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean isHasContent() {
        return hasContent;
    }

    public void setHasContent(boolean hasContent) {
        this.hasContent = hasContent;
    }

    public BoundingBox getBoundingBox(){
        return geoFields.getBoundingBox();
    }

    public void setBoundingBox(BoundingBox b){
        geoFields.setFullBB(b);
    }

    public boolean hasBoundingBox(){
        return geoFields.hasBB();
    }

    public void setSimpleFields(SimpleCitationFields simpleCitationFields) {
        citationFields.setSimpleCitationFields(simpleCitationFields);
    }

    public void setSimpleFieldVal (String fieldName, String fieldValue){
        SimpleCitationFields simpleCitationFields = getSimpleFields();
        simpleCitationFields.setField(fieldName,fieldValue);
        citationFields.setSimpleCitationFields(simpleCitationFields);
    }
    public String getSimpleFieldVal(String fieldName){
        SimpleCitationFields simpleCitationFields = getSimpleFields();
        return simpleCitationFields.getField(fieldName);
    }

    public boolean hasContent(){
        return hasContent;
    }
}
