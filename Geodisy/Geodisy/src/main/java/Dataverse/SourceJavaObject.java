package Dataverse;

import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses.JournalFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static Dataverse.DVFieldNameStrings.PROTOCOL;

public abstract class SourceJavaObject {
    protected CitationFields citationFields;
    protected GeographicFields geoFields;
    protected SocialFields socialFields;
    //protected JournalFields journalFields;
    protected List<DataverseRecordFile> dataFiles; //Stores the datafiles
    protected List<DataverseRecordFile> geoDataFiles; //Stores the datafiles that are geospatial in nature
    protected String server;
    protected boolean hasContent;
    public boolean hasGeospatialFile;

    public abstract void parseCitationFields(JSONObject citationFieldsArray);
    public abstract void parseGeospatialFields(JSONArray geoFieldsArray);
    public abstract void parseSocialFields(JSONArray socialFieldsArray);
    //Uncomment if we decide to include Journal Fields metadata block
    // public abstract void parseJournalFields(JSONArray journalFieldsArray);
    public abstract void parseFiles(JSONArray fileFieldsArray);
    public abstract JSONObject getVersionSection(JSONObject current);
    public abstract void downloadFiles();

    public SourceJavaObject(String server) {
        this.citationFields = new CitationFields();
        this.dataFiles = new LinkedList<>();
        this.geoDataFiles = new LinkedList<>();
        this.geoFields = new GeographicFields();
        this.socialFields = new SocialFields();
        //this.journalFields = new JournalFields();
        this.server = server;
        hasContent = false;
        hasGeospatialFile = false;
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

    public List<DataverseRecordFile> getGeoDataFiles() {
        return geoDataFiles;
    }

    public void setGeoDataFiles(List<DataverseRecordFile> geoDataFiles) {
        this.geoDataFiles = geoDataFiles;
    }

    public void addGeoDataFile(DataverseRecordFile drf){
        geoDataFiles.add(drf);
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

    public SocialFields getSocialFields(){
        return socialFields;
    }

    public String getDOIProtocal(){
        String protocol = getSimpleFields().getField(PROTOCOL);
        if(protocol.contains("doi"))
            return "DOI";
        if(protocol.contains("handle"))
            return "Handle";
        if(protocol.contains("ark"))
            return "ARK";
        if(protocol.contains("urn"))
            return "URN";
        return "Error";
    }

    public boolean hasGeoGraphicCoverage(){
        GeographicFields gf = geoFields;
        return gf.getGeoCovers().size()>0;
    }

    //public JournalFields getJournalFields(){ return journalFields;}
}
