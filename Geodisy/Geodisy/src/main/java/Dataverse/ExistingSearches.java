package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import static BaseFiles.GeodisyStrings.*;

/**
 * Class that holds bounding boxes that have already been found and what versions of records have already been downloaded.
 */
public class ExistingSearches implements Serializable {
    private static final long serialVersionUID = 8947943825774008362L;
    private static HashMap<String, BoundingBox> bBoxes;
    private static HashMap<String, DataverseRecordInfo> recordVersions;
    private static ExistingSearches single_instance = null;
    static GeoLogger logger;


    public static ExistingSearches getExistingSearches() {
        if (single_instance == null) {
            single_instance = new ExistingSearches();
            single_instance.addBBox("_filler_", new BoundingBox());
        }
        return single_instance;
    }

    private ExistingSearches(){
        logger = new GeoLogger(this.getClass());
        bBoxes = new HashMap<>();
        recordVersions = new HashMap<>();
        FileWriter fw = new FileWriter();
        fw.verifyFileExistence(EXISTING_BBOXES);
        fw.verifyFileExistence(EXISTING_RECORDS);
    }
    public boolean isEmpty(){
        return bBoxes.isEmpty()&&recordVersions.isEmpty();
    }
    public void addBBox(String name, BoundingBox boundingBox){
        bBoxes.put(name,boundingBox);
    }

    private boolean hasBBox(String doi){
        return bBoxes.containsKey(doi);
    }

    public BoundingBox getBBox(String doi){
        if(hasBBox(doi))
            return bBoxes.get(doi);
        return new BoundingBox();
    }

    public int numberOfBBoxes(){
        return bBoxes.size()-1;
    }

    public int numberOfRecords(){
        return recordVersions.size();
    }

    public void addOrReplaceRecord(DataverseRecordInfo dataverseRecordInfo){
        recordVersions.put(dataverseRecordInfo.getDoi(), dataverseRecordInfo);
    }

    public boolean isNewerRecord(DataverseRecordInfo dataverseRecordInfo){
        return dataverseRecordInfo.newer(recordVersions.get(dataverseRecordInfo.getDoi()));
    }


    public DataverseRecordInfo getRecordInfo(String doi){
        if(recordVersions.containsKey(doi))
            return recordVersions.get(doi);
        return new DataverseRecordInfo();
    }

    public void deleteBBox(String location){
        bBoxes.remove(location);
    }

    public boolean hasRecord(String doi){
        return recordVersions.containsKey(doi);
    }

    public void deleteRecord(String doi){
        if(bBoxes.containsKey(doi))
            bBoxes.remove(doi);
        if(recordVersions.containsKey(doi))
            recordVersions.remove(doi);
    }

    public void saveExistingSearchs(){
        FileWriter fw  = new FileWriter();
        try {
            fw.writeObjectToFile(recordVersions,EXISTING_RECORDS);
        } catch (IOException e) {
            logger.error("Something went wrong saving existing searches");
        }
        try {
            fw.writeObjectToFile(bBoxes,EXISTING_BBOXES);
        } catch (IOException e) {
            logger.error("Something went wrong saving existing bboxes");
        }
    }

    public void testSaveExistingSearches(){
        FileWriter fw  = new FileWriter();
        try {
            fw.writeObjectToFile(recordVersions,TEST_EXISTING_RECORDS);
        } catch (IOException e) {
            logger.error("Something went wrong saving existing searches");
        }
        try {
            fw.writeObjectToFile(bBoxes,TEST_EXISTING_BBOXES);
        } catch (IOException e) {
            logger.error("Something went wrong saving existing bboxes");
        }
    }

    public static ExistingSearches readExistingSearches(){
        ExistingSearches es = ExistingSearches.getExistingSearches();
        FileWriter fw = new FileWriter();
        try {
            es.recordVersions = (HashMap<String, DataverseRecordInfo>) fw.readSavedObject(EXISTING_RECORDS);
        } catch (IOException e) {
            logger.error("Something went wrong reading the Existing Searches file");
            return es;
        } catch (ClassNotFoundException e) {
            logger.error("Something went wrong parsing the Existing Searches file");
            return es;
        }
        try {
            bBoxes = (HashMap<String, BoundingBox>) fw.readSavedObject(EXISTING_BBOXES);
        } catch (IOException e) {
            logger.error("Something went wrong reading the Existing bBoxes file");
            return es;
        } catch (ClassNotFoundException e) {
            logger.error("Something went wrong parsing the Existing BBoxes file");
            return es;
        }
        return es;
    }

    public HashMap<String, BoundingBox> getbBoxes() {
        return bBoxes;
    }

    public  HashMap<String, DataverseRecordInfo> getRecordVersions() {
        return recordVersions;
    }

    public void setbBoxes(HashMap<String, BoundingBox> bboxes){
        bBoxes = bboxes;
    }

    public void setRecords(HashMap<String,DataverseRecordInfo> records){
        recordVersions = records;
    }
}
