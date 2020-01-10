package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.*;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import GeoServer.GeoServerAPI;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static BaseFiles.GeodisyStrings.*;
import static Dataverse.DVFieldNameStrings.*;

/**
 * Java object structure to parse Dataverse Json into
 * May need to change field types for dates, URLs, and/or email addresses.
 *
 * Dataverse Alias: Java Class: JavaObject Variable
 *
 *
 */

public class DataverseJavaObject extends SourceJavaObject {
    private GeoLogger logger = new GeoLogger(this.getClass());

    public DataverseJavaObject(String server) {
        super(server);
    }

    /**
     * Gets the Citation metadata for the record
     * @param citationFieldsArray
     */
    @Override
    public void parseCitationFields(JSONObject citationFieldsArray) {
        citationFields.setBaseFields(citationFieldsArray);
        JSONObject metadata = getVersionSection(citationFieldsArray).getJSONObject("metadataBlocks");
        JSONArray currentArray = metadata.getJSONObject(CITATION).getJSONArray(FIELDS);
        for (Object o : currentArray) {
            JSONObject jo = (JSONObject) o;
            citationFields.setFields(jo);
        }
        hasContent=true;
        geoFields = new GeographicFields(this);
        socialFields = new SocialFields(this);
    }

    /**
     * Gets the Geospatial metadata for the record
     * @param geoFieldsArray
     */
    @Override
    public void parseGeospatialFields(JSONArray geoFieldsArray) {
        for(Object o: geoFieldsArray){
            JSONObject jo = (JSONObject) o;
            this.geoFields.setFields(jo);
        }
        List<BoundingBox> bb = new LinkedList<>();
        //GeonamesBBs geonames = new GeonamesBBs(this);
        if(!geoFields.hasBB()){
            List<GeographicCoverage> coverages = geoFields.getListField(GEOGRAPHIC_COVERAGE);
            if(!coverages.isEmpty())

                for(GeographicCoverage gc: coverages){
                    String country = gc.getField(GIVEN_COUNTRY);
                    String province = gc.getField(GIVEN_PROVINCE);
                    String city = gc.getField(GIVEN_CITY);
                    if(country.isEmpty())
                        continue;
                    else{
                        if(province.isEmpty())
                            bb.add(gc.getCountryObject().getBoundingBox());
                        else{
                            if(city.isEmpty())
                                bb.add(gc.getProvinceObject().getBoundingBox());
                            else
                                bb.add(gc.getCityObject().getBoundingBox());
                        }
                    }
                }
        }
    }
    @Override
    public void parseSocialFields(JSONArray socialFieldsArray){
        for(Object o: socialFieldsArray){
            JSONObject jo = (JSONObject) o;
            this.socialFields.setFields(jo);
        }

    }

   /* //Uncomment if we decide to include the Journal Field Metadata block
    @Override
    public void parseJournalFields(JSONArray journalFieldsArray) {
        for (Object o : journalFieldsArray) {
            JSONObject jo = (JSONObject) o;
            this.journalFields.setFields(jo);
        }
    }*/
    /**
     * Gets the File metadata for the record.
     * @param fileFieldsArray
     */
    @Override
    public void parseFiles(JSONArray fileFieldsArray) {
        for(Object o: fileFieldsArray){
            JSONObject jo = (JSONObject) o;
            if(jo.getBoolean("restricted"))
                continue;
            String title = jo.getString("label");
            JSONObject dataFile = (JSONObject) jo.get("dataFile");
            DataverseRecordFile dRF;
            //Some Dataverses don't have individual DOIs for files, so for those I will use the database's file id instead
            if(dataFile.has(PERSISTENT_ID)&& !dataFile.getString(PERSISTENT_ID).equals("")) {
                String doi = dataFile.getString(PERSISTENT_ID);
                dRF = new DataverseRecordFile(title, doi,dataFile.getInt("id"), server, citationFields.getDOI());
                dRF.setOriginalTitle(title);
            }else{
                int dbID = (int) dataFile.get("id");
                dRF = new DataverseRecordFile(title,dbID,server,citationFields.getDOI());
                dRF.setOriginalTitle(title);
            }
            SourceRecordFiles srf = SourceRecordFiles.getSourceRecords();
            srf.putRecord(dRF.getDoi(),dRF.title,dRF);
            dataFiles.add(dRF);
        }
    }
    //if changed, need to change copy in CitationFields Class
    @Override
    public JSONObject getVersionSection(JSONObject current) {
        if(current.has("latestVersion"))
            return current.getJSONObject("latestVersion");
        else if(current.has("datasetVersion"))
            return current.getJSONObject("datasetVersion");
        else{
            logger.error("missing a _____Version field in the dataverseJson in " + current.toString());
            return new JSONObject();
        }
    }

    public DataverseRecordInfo generateDRI(){
        String major, minor,doi;
        major = getCitationFields().getSimpleCitationFields().getField(MAJOR_VERSION);
        minor = getCitationFields().getSimpleCitationFields().getField(MINOR_VERSION);
        doi = getCitationFields().getDOI();
        DataverseRecordInfo answer = new DataverseRecordInfo();
        answer.setDoi(doi);
        answer.setMajor(major);
        answer.setMinor(minor);
        answer.setVersion(Integer.parseInt(major)*1000 + minor);
        return answer;
    }

    /**
     *  Deletes the directory of the record's files if it exists and then downloads the updated
     *  files, excluding non-geospatial file types other than .zip
     *  If there aren't any geospatial files uploaded, the entire directory get's deleted.
     */
    @Override
    public void downloadFiles() {
        //TODO something is going wrong with record 10_5072_FK2_GMKLWX
        File f = new File("datasetFiles/" + urlized(citationFields.getDOI()));
        deleteDir(f);
        List<DataverseRecordFile> temp = new LinkedList<>();
        DataverseRecordFile tempDRF = new DataverseRecordFile();
        for (DataverseRecordFile dRF : dataFiles) {
            if(GeodisyStrings.fileTypesToIgnore(dRF.title))
                continue;
            if(tempDRF.getDoi().equals("temp"))
                tempDRF = dRF.retrieveFile(this);
            else
                dRF.retrieveFile(this);

            if(GeodisyStrings.hasGeospatialFile(dRF.getTitle()))
                hasGeospatialFile = true;
        }
        for (DataverseRecordFile dRF : dataFiles) {
            if (GeodisyStrings.fileTypesToIgnore(dRF.title))
                continue;
            dRF.translateFile(this);
        }

        int vector = 1;
        int raster = 1;
        LinkedList<DataverseRecordFile> tempList = new LinkedList<>();
        DataverseRecordFile tempRec;
        for(DataverseRecordFile drf : getGeoDataFiles()){
            if(drf.getTitle().endsWith(".shp")) {
                tempRec = createRecords(drf, vector, VECTOR);
                if(tempRec.hasValidBB()) {
                    tempList.add(tempRec);
                    vector++;
                }
            }else if(drf.getTitle().endsWith(".tif")) {
                tempRec = createRecords(drf, raster, RASTER);
                if(tempRec.hasValidBB()) {
                    tempList.add(tempRec);
                    raster++;
                }
            }
        }
        setGeoDataFiles(tempList);
        if(f.list() != null && f.list().length==0)
            f.delete();
        for(DataverseRecordFile drf: getGeoDataFiles()){
            if(drf.isFromFile()){
                hasGeospatialFile = true;
                break;
            }
        }
    }

    private DataverseRecordFile createRecords(DataverseRecordFile drf, int number, String type) {
        DataverseRecordFile tempDRF;
        GDAL gdal = new GDAL();
        String dirPath = DATASET_FILES_PATH + drf.getDatasetDOI().replace("_","/") + "/";
        String filePath = dirPath + drf.getTitle();
        File fUpdate = new File(filePath);
        tempDRF = gdal.parse(fUpdate);
        if(type.equals(VECTOR)) {
            drf.setGeometryType(tempDRF.getGeometryType());
            drf.setProjection(tempDRF.getProjection());
            drf.setBB(tempDRF.getBB());
            drf.addFileNumber(number);
            drf.setGeoserverLabel(labelize(this.getSimpleFieldVal(PERSISTENT_ID))+ "v" + number);
            addVectorToGeoserver(fUpdate.getName(),drf.getGeoserverLabel());
            return drf;
        }else if(type.equals(RASTER)){
            drf.addFileNumber(number);
            drf.setGeoserverLabel(labelize(this.getSimpleFieldVal(PERSISTENT_ID))+ "r" + number);
            drf.setGeometryType(RASTER);
            drf.setProjection(tempDRF.getProjection());
            drf.setBB(tempDRF.getBB());
            addRasterToGeoserver(drf.getGeoserverLabel());
            return drf;
        } else{
            logger.error("Something went wrong parsing DataverseRecordFile: " + drf.getTitle());
            return drf;
        }
    }

    private String labelize(String simpleFieldVal) {
        return simpleFieldVal.replace(".","_").replace("/","_").replace("\\\\","_");
    }

    private void addVectorToGeoserver(String name, String geoserverLabel) {
        GeoServerAPI geoServerAPI =  new GeoServerAPI(this);
        geoServerAPI.addVector(name,geoserverLabel);
    }

    private void addRasterToGeoserver(String name) {
        GeoServerAPI geoServerAPI =  new GeoServerAPI(this);
        geoServerAPI.uploadRaster(name);
    }
    @Override
    public String getDOIProtocal(){
        String answer = super.getDOIProtocal();
        if(answer.equals("Error")){
            logger.warn("Unknown persistant URL protocal used with record: " + getDOI());
            return "";
        } else
            return answer;
    }
}
