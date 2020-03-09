package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import static BaseFiles.GeodisyStrings.*;

/**
 * Class that holds bounding boxes that have already been found and what versions of records have already been downloaded.
 */
public class ExistingHarvests extends ExistingSearches implements Serializable {
    private static final long serialVersionUID = 8947943825774008362L;
    private static HashMap<String, BoundingBox> bBoxes;
    private static HashMap<String, DataverseRecordInfo> recordVersions;
    private static ExistingHarvests single_instance = null;

    public static ExistingHarvests getExistingHarvests(){

        if (single_instance == null) {
            single_instance = new ExistingHarvests();
        }

        return single_instance;
    }

    private ExistingHarvests(){
        logger = new GeoLogger(this.getClass());
        FileWriter fw = new FileWriter();
        bBoxes = readExistingBoundingBoxes();
        recordVersions = readExistingRecords(EXISTING_RECORDS);
    }

    public boolean isEmpty(){
        return recordVersions.isEmpty();
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
        return bBoxes.size();
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
        bBoxes.remove(doi);
        recordVersions.remove(doi);
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

    public HashMap<String, BoundingBox> readExistingBoundingBoxes(){
        HashMap<String, BoundingBox> newFile = new HashMap<>();
        FileWriter fw = new FileWriter();
        try {
            return  (HashMap<String, BoundingBox>) fw.readSavedObject(EXISTING_BBOXES);
        } catch (IOException e) {
            logger.error("Something went wrong reading the Existing bBoxes file");
            return newFile;
        } catch (ClassNotFoundException e) {
            logger.error("Something went wrong parsing the Existing BBoxes file");
            return newFile;
        }catch (NullPointerException e){
            return newFile;
        }
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
