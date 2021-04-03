package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import _Strings.GeodisyStrings;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import static _Strings.GeodisyStrings.*;

/**
 * Class that holds bounding boxes that have already been found and what versions of records have already been downloaded.
 */
public class ExistingDatasetBBoxes extends ExistingSearches implements Serializable {
    private static final long serialVersionUID = 8947943825774008362L;
    private static HashMap<String, BoundingBox> bBoxes;
    private static ExistingDatasetBBoxes single_instance = null;

    public static ExistingDatasetBBoxes getExistingHarvests(){

        if (single_instance == null) {
            single_instance = new ExistingDatasetBBoxes();
        }

        return single_instance;
    }

    private ExistingDatasetBBoxes(){
        bBoxes = readExistingBoundingBoxes();
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

    public void deleteBBox(String location){
        bBoxes.remove(location);
    }


    public void testSaveExistingSearches(){
        FileWriter fw  = new FileWriter();
        try {
            fw.writeObjectToFile(bBoxes,TEST_EXISTING_BBOXES);
        } catch (IOException e) {
            getLogger().error("Something went wrong saving existing bboxes");
        }
    }

    public HashMap<String, BoundingBox> readExistingBoundingBoxes(){
        HashMap<String, BoundingBox> newFile = new HashMap<>();
        FileWriter fw = new FileWriter();
        try {
            return  (HashMap<String, BoundingBox>) fw.readSavedObject(GeodisyStrings.replaceSlashes(EXISTING_DATASET_BBOXES));
        } catch (IOException e) {
            getLogger().error("Something went wrong reading the Existing bBoxes file");
            return newFile;
        } catch (ClassNotFoundException e) {
            getLogger().error("Something went wrong parsing the Existing BBoxes file");
            return newFile;
        }catch (NullPointerException e){
            return newFile;
        }
    }


    public HashMap<String, BoundingBox> getbBoxes() {
        return bBoxes;
    }


    public void setbBoxes(HashMap<String, BoundingBox> bboxes){
        bBoxes = bboxes;
    }

    @Override
    protected GeoLogger getLogger() {
        if (logger == null) {
            logger = new GeoLogger(this.getClass());
        }
        return logger;
    }

}
