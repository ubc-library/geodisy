package Dataverse;

import BaseFiles.GeoLogger;
import _Strings.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.*;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.File;
import java.util.*;

import static _Strings.GeodisyStrings.*;
import static _Strings.DVFieldNameStrings.*;

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
            try {
                JSONObject jo = (JSONObject) o;
                if (jo.getBoolean("restricted"))
                    continue;
                JSONObject dataFile = jo.getJSONObject("dataFile");
                String title = dataFile.getString("filename");
                DataverseRecordFile dRF;
                //Some Dataverses don't have individual DOIs for files, so for those I will use the database's file id instead
                int dbID;
                String url ="";
                if (jo.has("frdr_harvester")) {
                    url = dataFile.getString("fileURI");
                    dRF = new DataverseRecordFile(title, citationFields.getPID(), url);
                    dRF.setOriginalTitle(title);
                } else if (dataFile.has(PERSISTENT_ID) && !dataFile.getString(PERSISTENT_ID).equals("")) {
                    dbID = (int) dataFile.get("id");
                    String doi = dataFile.getString(PERSISTENT_ID);
                    dRF = new DataverseRecordFile(title, doi, dataFile.getInt("id"), server, citationFields.getPID());
                    dRF.setFileURL(server + DV_FILE_ACCESS_PATH + dbID);
                    dRF.setOriginalTitle(title);
                } else {
                    dbID = (int) dataFile.get("id");
                    dRF = new DataverseRecordFile(title, citationFields.getPID(), dbID);
                    dRF.setOriginalTitle(title);
                    dRF.setFileURL(server + DV_FILE_ACCESS_PATH + dbID);
                }
                SourceRecordFiles srf = SourceRecordFiles.getSourceRecords();
                if (!dRF.getDatasetIdent().equals(""))
                    srf.putRecord(dRF.getDatasetIdent(), dRF.translatedTitle, dRF);
                else
                    srf.putRecord(dRF.getDatasetIdent(), dRF.translatedTitle, dRF);
                dataFiles.add(dRF);
            }catch (ClassCastException e){
                logger.error("Something went wrong trying to parse the file metadata section for " + getPID() + " the json was " + fileFieldsArray.toString());
            }
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

    /**
     *  Deletes the directory of the record's files if it exists and then downloads the updated
     *  files, excluding non-geospatial file types other than .zip
     *  If there aren't any geospatial files uploaded, the entire directory get's deleted.
     */

    @Override
    public LinkedList<DataverseGeoRecordFile> downloadFiles() {
        String path = GeodisyStrings.replaceSlashes(DATA_DIR_LOC + urlized(GeodisyStrings.removeHTTPSAndReplaceAuthority(citationFields.getPID())));
        File f = new File(path);
        deleteDir(f);
        f.mkdirs();
        LinkedList<DataverseRecordFile> drfs = new LinkedList<>();
        checkDataset();
        for (DataverseRecordFile dRF : dataFiles) {
            if (GeodisyStrings.fileTypesToIgnore(dRF.translatedTitle)) {
                File bad = new File(path + GeodisyStrings.replaceSlashes("/") + dRF.translatedTitle);
                bad.delete();
                //System.out.println("Ignored file: " + dRF.translatedTitle);
                continue;
            }
            if(dRF.translatedTitle.startsWith("LAS_")) {
                File bad = new File(path+GeodisyStrings.replaceSlashes("/")+dRF.translatedTitle);
                bad.delete();
                continue;
            }
            LinkedList<DataverseRecordFile> temp = dRF.retrieveFile(this);
            for(DataverseRecordFile d: temp){
                boolean add = true;
                for(DataverseRecordFile d2: drfs){
                    if(d.getTranslatedTitle() == d2.getTranslatedTitle()) {
                        add = false;
                        break;
                    }
                }
                if(add)
                    drfs.add(d);
            }
        }

        if(drfs.size()==0){
            f.delete();
            return new LinkedList<DataverseGeoRecordFile>();
        }


        dataFiles = drfs;
        for (int i = 0; i < dataFiles.size(); i++) {
            String name = dataFiles.get(i).getTranslatedTitle();
            if (name.endsWith("zip") ||name.endsWith("tab"))
                dataFiles.remove(i);
        }

        LinkedList<DataverseGeoRecordFile> newRecs = new LinkedList<>();
        DataverseGeoRecordFile dgrf;
        for (DataverseRecordFile dRF : dataFiles) {
            if (!GeodisyStrings.isProcessable(dRF.translatedTitle))
                continue;
            if (dRF.getDbID() == -1)
                dRF.setFileURL("");
            GDAL gdal = new GDAL();
            String dirPath = GeodisyStrings.replaceSlashes(DATA_DIR_LOC + GeodisyStrings.removeHTTPSAndReplaceAuthority(getPID()).replace(".","/") + "/");
            dgrf = new DataverseGeoRecordFile(dRF);
            GeographicBoundingBox gbb = gdal.generateBB(new File(dirPath+dRF.getTranslatedTitle()), getPID(),dRF.getGBBFileNumber());
            dgrf.setGbb(gbb, gbb.getField(FILE_NAME));
            if(dgrf.hasValidBB()) {
                dgrf.setTranslatedTitle(dgrf.gbb.getField(FILE_NAME));
                dgrf.setFileURL(dRF.getFileURL());
                newRecs.add(dgrf);
            }
            else{
                String name = dgrf.getTranslatedTitle();
                File f1;
                if(name.endsWith("shp")){
                    String stub = name.substring(0,name.lastIndexOf(".shp"));
                    for(String ex: NON_SHP_SHAPEFILE_EXTENSIONS){
                        f1 = new File(dirPath+stub+ex);
                        if(f1.exists())
                            f1.delete();
                    }
                }
                f1 = new File(dirPath+name);
                if(f1.exists())
                    f1.delete();
            }
            ExistingGeoLabels existingGeoLabels = ExistingGeoLabels.getExistingLabels();
            ExistingGeoLabelsVals existingGeoLabelsVals = ExistingGeoLabelsVals.getExistingGeoLabelsVals();
            existingGeoLabels.saveExistingFile(existingGeoLabels.getGeoLabels(),EXISTING_GEO_LABELS,"ExistingGeoLabels");
            existingGeoLabelsVals.saveExistingFile(existingGeoLabelsVals.getValues(),EXISTING_GEO_LABELS_VALS,"ExistingGeoLabelsVals");
        }
        return newRecs;
    }
    //Filter files to potentially download with a max 5GB individual file size and max 100GB dataset size
    private void checkDataset() {
        Long total = 0L;
        LinkedList<DataverseRecordFile> list = new LinkedList<>();
        HTTPCallerFiles hCF = new HTTPCallerFiles();
        for(DataverseRecordFile dataverseRecordFile: dataFiles){
            String url = dataverseRecordFile.recordURL;
            if( url.startsWith("ftp://") | url.startsWith("http://ftp") | url.startsWith("https://ftp") )
                continue;
            long current = hCF.getFileLength(dataverseRecordFile.recordURL);
            try {
                if (current == -2 | current > 5000000000L)
                    continue;
                if(current==-1)
                    current = 0;
                total += current;
                list.add(dataverseRecordFile);
                if (total > 100000000000L) {
                    list = new LinkedList<>();
                    logger.warn("Dataset " + getPID() + " was too large to download.");
                    System.out.println("Dataset too large to download");
                    break;
                }
            } catch (NumberFormatException e){
                logger.error("Something weird with the file length of " + dataverseRecordFile.translatedTitle + ": " + current + "at " + dataverseRecordFile.recordURL);
            }
        }
        dataFiles = list;
    }

    @Override
    public void updateGeoserver() {
        System.out.println("Updating geoserver");
        Collections.sort(getGeoDataFiles(), new SortByFileName());
        for(DataverseGeoRecordFile dgrf:getGeoDataFiles()){
            if(dgrf.getTranslatedTitle().endsWith(".shp")) {
                dgrf.onGeoserver = createRecords(dgrf, Integer.parseInt(dgrf.getGBBFileNumber()), VECTOR);
            }else if(dgrf.getTranslatedTitle().endsWith(".tif")) {
                dgrf.onGeoserver = createRecords(dgrf, Integer.parseInt(dgrf.getGBBFileNumber()), RASTER);
            }
        }
    }
    class SortByFileName implements Comparator<DataverseGeoRecordFile>{
        public int compare(DataverseGeoRecordFile a, DataverseGeoRecordFile b){
            return a.getTranslatedTitle().compareToIgnoreCase(b.getTranslatedTitle());
        }
    }
    //TODO
    private DataverseGeoRecordFile getRecord(String name) {
        DataverseGeoRecordFile dvgr = new DataverseGeoRecordFile();
        for(DataverseRecordFile drf : dataFiles){
            if(drf.getTranslatedTitle().equals(name)) {
                dvgr = new DataverseGeoRecordFile(drf);
                dvgr.setFileURL(drf.getFileURL());
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

    @Override
    protected boolean createRecords(DataverseGeoRecordFile dgrf, int number, String type) {
        String dirPath = GeodisyStrings.replaceSlashes(DATA_DIR_LOC + GeodisyStrings.removeHTTPSAndReplaceAuthority(dgrf.getDatasetIdent().replace("_","/")) + "/");
        String filePath = dirPath + dgrf.getTranslatedTitle();
        File fUpdate = new File(filePath);
        if(type.equals(VECTOR)) {
            return addVectorToGeoserver(fUpdate.getName(),dgrf.getGeoserverLabel());
        }else if(type.equals(RASTER)){
            return addRasterToGeoserver(dgrf);
        } else{
            logger.error("Something went wrong parsing DataverseRecordFile: " + dgrf.getTranslatedTitle());
            return false;
        }
    }

    private String labelize(String simpleFieldVal) {
        return simpleFieldVal.replace(".","_").replace("/","_").replace("\\\\","_").replace(":","");
    }

    @Override
    public String getDOIProtocal(){
        String answer = super.getDOIProtocal();
        if(answer.equals("Error")){
            logger.warn("Unknown persistant URL protocal used with record: " + getPID());
            return "";
        } else
            return answer;
    }

    @Override
    public void updateRecordFileNumbers() {
        updateRecordFileNumbers(logger);
    }
}
