package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Crosswalking.GeoBlacklightJson.GeoBlacklightStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.*;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import GeoServer.GeoServerAPI;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.File;
import java.util.*;

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
            citationFields = citationFields.setFields(jo);
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
             geoFields = geoFields.setFields(jo);
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
                        if(province.isEmpty()) {
                            bb.add(gc.getCountryObject().getBoundingBox());
                        }
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
            socialFields = socialFields.setFields(jo);
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
            int dbID;
            if(dataFile.has(PERSISTENT_ID)&& !dataFile.getString(PERSISTENT_ID).equals("")) {
                dbID = (int) dataFile.get("id");
                String doi = dataFile.getString(PERSISTENT_ID);
                dRF = new DataverseRecordFile(title, doi,dataFile.getInt("id"), server, citationFields.getDOI());
                dRF.setFileURL(server+DV_FILE_ACCESS_PATH+dbID);
                dRF.setOriginalTitle(title);
            }else{
                dbID = (int) dataFile.get("id");
                dRF = new DataverseRecordFile(title,dbID,server,citationFields.getDOI());
                dRF.setOriginalTitle(title);
                dRF.setFileURL(server+DV_FILE_ACCESS_PATH+dbID);
            }
            SourceRecordFiles srf = SourceRecordFiles.getSourceRecords();
            if(!dRF.getDatasetIdent().equals(""))
                srf.putRecord(dRF.getDatasetIdent(),dRF.translatedTitle,dRF);
            else
                srf.putRecord(dRF.getDatasetIdent(),dRF.translatedTitle,dRF);
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
    //@Override
    public DataverseJavaObject downloadFiles() {
        String path = GEODISY_PATH_ROOT + GeodisyStrings.replaceSlashes(DATASET_FILES_PATH + urlized(citationFields.getDOI()));
        File f = new File(path);
        deleteDir(f);
        f.mkdir();
        LinkedList<DataverseRecordFile> drfs = new LinkedList<>();
        for (DataverseRecordFile dRF : dataFiles) {
            if (GeodisyStrings.fileTypesToIgnore(dRF.translatedTitle)) {
                System.out.println("Ignored file: " + dRF.translatedTitle);
                continue;
            }
            drfs.addAll(dRF.retrieveFile(this));

        }
        if(drfs.size()==0){
            f.delete();
            return this;
        }

        dataFiles = drfs;
        for (int i = 0; i < dataFiles.size(); i++) {
            if (dataFiles.get(i).getTranslatedTitle().endsWith("zip"))
                dataFiles.remove(i);
        }
        //dataFiles.addAll(getNewFiles());
        LinkedList<DataverseRecordFile> newRecs = new LinkedList<>();
        DataverseRecordFile tempRec;
        DataverseGeoRecordFile dgrf;
        for (DataverseRecordFile dRF : dataFiles) {
            if (!GeodisyStrings.isProcessable(dRF.translatedTitle))
                continue;
            DataverseRecordFile temp = new DataverseRecordFile(dRF);
            if (temp.getDbID() == -1)
                temp.setFileURL("");
            GDAL gdal = new GDAL();
            String dirPath = GEODISY_PATH_ROOT + GeodisyStrings.replaceSlashes(DATASET_FILES_PATH + getDOI().replace(".","/") + "/");
            dgrf = new DataverseGeoRecordFile(dRF);
            dgrf.setGbb(gdal.generateBB(new File(dirPath+temp.getTranslatedTitle()),getDOI(),dRF.getGBBFileNumber()));
            if(dgrf.hasValidBB()) {
                tempRec = temp.translateFile(this);
                dgrf.setTranslatedTitle(tempRec.translatedTitle);
                dgrf.setFileURL(server+"api/access/datafile/" + dgrf.dbID + "?format=original");
                if (!tempRec.getOriginalTitle().isEmpty())
                    newRecs.add(tempRec);
                if(!dgrf.getTranslatedTitle().isEmpty())
                    geoDataFiles.add(dgrf);
            }
        }
        dataFiles = newRecs;
        return this;
    }

    private LinkedList<DataverseRecordFile> getNewFiles() {
        HashSet<String> fileNames = new HashSet<>();
        LinkedList<DataverseRecordFile> newFiles = new LinkedList<>();
        for(DataverseRecordFile drf: getDataFiles()){
            fileNames.add(drf.getTranslatedTitle());
        }
        File f = new File(GeodisyStrings.replaceSlashes(GEODISY_PATH_ROOT + DATASET_FILES_PATH + getDOI().replace("_", "/").replace(".","/") + "/"));
        if(f.exists()&&f.isDirectory()){
            File[] files = f.listFiles();
            for(File file:files){
                if(!fileNames.contains(file.getName()))
                    continue;
                newFiles.add(new DataverseRecordFile(file.getName(),-1,server,getDOI()));
            }
        }
        return newFiles;
    }
    public void updateGeoserver() {
        for(DataverseGeoRecordFile dgrf:getGeoDataFiles()){
            if(dgrf.getTranslatedTitle().endsWith(".shp")) {
                createRecords(dgrf, Integer.parseInt(dgrf.getGBBFileNumber()), VECTOR);
            }else if(dgrf.getTranslatedTitle().endsWith(".tif")) {
                createRecords(dgrf, Integer.parseInt(dgrf.getGBBFileNumber()), RASTER);
            }
        }
    }
    //TODO
    private DataverseGeoRecordFile getRecord(String name) {
        DataverseGeoRecordFile dvgr = new DataverseGeoRecordFile();
        for(DataverseRecordFile drf : dataFiles){
            if(drf.getTranslatedTitle().equals(name)) {
                dvgr = new DataverseGeoRecordFile(drf);
                dvgr.setFileURL(server+"api/access/datafile/" + drf.dbID + "?format=original");
                return dvgr;
            }
        }
        dvgr.setOriginalTitle(name);
        dvgr.setTranslatedTitle(name);
        String fileURL = dataFiles.get(0).getFileURL();
        fileURL = fileURL.substring(0,fileURL.lastIndexOf(GeodisyStrings.replaceSlashes("/"))+1);
        dvgr.setFileURL(fileURL + name);
        dvgr.setServer(dataFiles.get(0).server);
        dvgr.setDbID(-1);
        dvgr.setDatasetIdent(dataFiles.get(0).datasetIdent);
        return dvgr;
    }

    //May need to merge tilesets later, but ignore this for now
    /*private void mergeTiles(DataverseRecordFile drf) {
        String merge = "gdalbuildvrt mpsaic.vrt " + DATASET_FILES_PATH + drf.getDatasetIdent().replace("_","/") + "/";
        String delete = "rm "+ DATASET_FILES_PATH + drf.getDatasetIdent().replace("_","/") + "/*.tif";
        String transform ="gdal_translate -of GTiff -co \"COMPRESS=JPEG\" -co \"PHOTOMETRIC=YCBCR\" -co \"TILED=YES\" mosaic.vrt" + drf.getDatasetIdent() + ".tif";
        ProcessBuilder p = new ProcessBuilder();
        p.command("bash","-c",merge);
        Process process = null;
        try {
            process = p.start();

            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            logger.error("Something went wrong trying to merge tifs in a mosaic file in record: " + drf.getDatasetIdent());
        }
        try{
            p.command("bash","-c",delete);
            process = p.start();
            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            logger.error("Something went wrong trying to delete merged tifs in record: " + drf.getDatasetIdent());
        }
        try{
        p.command("bash","-c",transform);
            process = p.start();
            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            logger.error("Something went wrong trying to convert merged tifs (mosaic) to to a new tif in record: " + drf.getDatasetIdent());
        }
        DataverseRecordFile temp;
        if(drf.getFileIdent().equals(""))
            temp = new DataverseRecordFile(drf.getDatasetIdent().replace("_","/") + "/*.tif",drf.getDbID(),drf.getServer(),drf.getDatasetIdent());
        else
            temp = new DataverseRecordFile(drf.getDatasetIdent().replace("_","/") + "/*.tif",drf.getFileIdent(),drf.getDbID(),drf.getServer(),drf.getDatasetIdent());

        for(Iterator<DataverseRecordFile> iter = dataFiles.iterator(); iter.hasNext();){
            DataverseRecordFile check = iter.next();
            if(check.getTranslatedTitle().toLowerCase().endsWith(".tif"))
                iter.remove();
        }
        dataFiles.add(temp);
    }*/

    private void createRecords(DataverseGeoRecordFile dgrf, int number, String type) {
        String dirPath = DATASET_FILES_PATH + dgrf.getDatasetIdent().replace("_","/") + "/";
        String filePath = dirPath + dgrf.getTranslatedTitle();
        File fUpdate = new File(filePath);
        if(type.equals(VECTOR)) {
            addVectorToGeoserver(fUpdate.getName(),dgrf.getGeoserverLabel());
        }else if(type.equals(RASTER)){
            addRasterToGeoserver(dgrf);
        } else{
            logger.error("Something went wrong parsing DataverseRecordFile: " + dgrf.getTranslatedTitle());
        }
    }

    private String labelize(String simpleFieldVal) {
        return simpleFieldVal.replace(".","_").replace("/","_").replace("\\\\","_").replace(":","");
    }

    private void addVectorToGeoserver(String name, String geoserverLabel) {
        GeoServerAPI geoServerAPI =  new GeoServerAPI(this);
        geoServerAPI.addVector(name,geoserverLabel);
    }

    private void addRasterToGeoserver(DataverseGeoRecordFile drf) {
        GeoServerAPI geoServerAPI =  new GeoServerAPI(this);
        geoServerAPI.uploadRaster(drf);
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
    public boolean hasDataRecord(String name){
        for(DataverseRecordFile drf : dataFiles){
            if(drf.getTranslatedTitle().equals(name))
                return true;
        }
        return false;
    }
    public void updateRecordFileNumbers() {
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
                logger.error("Somehow have a DataverseGeoRecordFile that doesn't end in shp or tif: File = " + dgrf.getTranslatedTitle() + " and doi = " + getDOI());
            }
        }
        int count = 1;
        for(DataverseGeoRecordFile dgrf:getGeoDataMeta()){
            dgrf.setFileNumber(count);
            count++;
        }
    }
}
