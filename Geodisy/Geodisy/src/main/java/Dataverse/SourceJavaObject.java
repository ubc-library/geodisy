package Dataverse;

import BaseFiles.GeoLogger;
import GeoServer.GeoServerAPI;
import _Strings.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedList;

import static _Strings.DVFieldNameStrings.PROTOCOL;

public abstract class SourceJavaObject {
    protected CitationFields citationFields;
    protected GeographicFields geoFields;
    protected SocialFields socialFields;
    //protected JournalFields journalFields;
    protected LinkedList<DataverseRecordFile> dataFiles; //Stores the datafiles
    protected LinkedList<DataverseGeoRecordFile> geoDataFiles; //Stores the datafiles that are geospatial in nature
    protected LinkedList<DataverseGeoRecordFile> geoDataMeta;
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
    public abstract LinkedList<DataverseGeoRecordFile> downloadFiles();
    public boolean moreRecordsToGet;
    public abstract void updateGeoserver();
    protected abstract boolean createRecords(DataverseGeoRecordFile dgrf, int number, String type);
    public abstract void updateRecordFileNumbers();


    public SourceJavaObject(String server) {
        this.citationFields = new CitationFields();
        this.dataFiles = new LinkedList<>();
        this.geoDataFiles = new LinkedList<>();
        this.geoDataMeta = new LinkedList<>();
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
    protected String urlized(String pid) {
        return pid.replace(".","/");

    }
    protected void deleteDir(File f) throws IOException {
        File[] files = f.listFiles();
        if(files != null && files.length >0) {
            for (File myFile : files) {
                if (myFile.isDirectory()) {
                    deleteDir(myFile);
                }
                Files.deleteIfExists(myFile.toPath());
            }
        }
        Files.deleteIfExists(f.toPath());
    }
    public String getPID(){
        return citationFields.getPID();
    }
    public void setPID(String pid){
        citationFields.setPID(pid);
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

    public LinkedList<DataverseRecordFile> getDataFiles() {
        return dataFiles;
    }


    public void setDataFiles(LinkedList<DataverseRecordFile> dataFiles) {
        this.dataFiles = dataFiles;
    }

    public LinkedList<DataverseGeoRecordFile> getGeoDataFiles() {
        return geoDataFiles;
    }

    public void setGeoDataFiles(LinkedList<DataverseGeoRecordFile> geoDataFiles) {
        this.geoDataFiles = geoDataFiles;
    }

    public void addGeoDataFile(DataverseGeoRecordFile drf){
        if(drf.hasValidBB())
            geoDataFiles.add(drf);
    }

    public void removeGeoDataFile(String name){
        Iterator<DataverseGeoRecordFile> iterator = geoDataFiles.iterator();
        while(iterator.hasNext()) {
            if (iterator.next().getTranslatedTitle().equalsIgnoreCase(name)) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            }
        }
    }

    public LinkedList<DataverseGeoRecordFile> getGeoDataMeta() {
        return geoDataMeta;
    }

    public void addGeoDataMeta(DataverseGeoRecordFile drf) {
        geoDataMeta.add(drf);
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
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
        return geoDataFiles.size()>0||geoDataMeta.size()>0;
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
            return "doi";
        if(protocol.contains("handle")||protocol.contains("hdl"))
            return "handle";
        if(protocol.contains("ark"))
            return "ark";
        if(protocol.contains("urn"))
            return "urn";
        if(protocol.contains(""))
            return "unspecified_in_metadata";
        return "Error";
    }

    public boolean hasGeoGraphicCoverage(){
        GeographicFields gf = geoFields;
        return gf.getGeoCovers().size()>0;
    }

    protected boolean addVectorToGeoserver(String name, String geoserverLabel) {
        GeoServerAPI geoServerAPI =  new GeoServerAPI(this);
        return geoServerAPI.addVector(name,geoserverLabel);
    }

    protected boolean addRasterToGeoserver(DataverseGeoRecordFile drf) {
        GeoServerAPI geoServerAPI =  new GeoServerAPI(this);
        return geoServerAPI.addRaster(drf);
    }

    public void updateRecordFileNumbers(GeoLogger logger) {
        System.out.println("Updating Record File numbers");
        int vector = 1;
        int raster = 1;
        for(DataverseGeoRecordFile dgrf : getGeoDataFiles()){
            if(dgrf.getTranslatedTitle().toLowerCase().endsWith(".shp")) {
                dgrf.setFileNumber(vector);
                vector++;
            }else if(dgrf.getTranslatedTitle().toLowerCase().endsWith(".tif")){
                dgrf.setFileNumber(raster);
                raster++;
            }else{
                logger.error("Somehow have a DataverseGeoRecordFile that doesn't end in shp or tif: File = " + dgrf.getTranslatedTitle() + " and doi = " + getPID());
            }
        }
        int count = 1;
        for(DataverseGeoRecordFile dgrf:getGeoDataMeta()){
            dgrf.setFileNumber(count);
            count++;
        }
    }
    public boolean hasDataRecord(String name){
        for(DataverseRecordFile drf : dataFiles){
            if(drf.getTranslatedTitle().equals(name))
                return true;
        }
        return false;
    }


    //public JournalFields getJournalFields(){ return journalFields;}
}
