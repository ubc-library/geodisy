package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;


import java.io.*;
import java.util.HashMap;

import static _Strings.GeodisyStrings.EXISTING_DATASET_BBOXES;

/**
 * Class for getting the list of records that have already been downloaded
 */
public class ExistingSearchesFile {
    GeoLogger logger = new GeoLogger(this.getClass());
    ExistingDatasetBBoxes es;

    /**
     * Constructor to use for production environment;
     */
    public ExistingSearchesFile(){
        es = ExistingDatasetBBoxes.getExistingHarvests();
    }

    /**
     * Only use this constructor for testing by writing to a test Existing records file
     * @param path
     */
    public ExistingSearchesFile(String path){
        es = ExistingDatasetBBoxes.getExistingHarvests();
    }

    public void writeExistingSearches(ExistingDatasetBBoxes existingSearches) throws IOException {
        HashMap<String, BoundingBox> bboxes = new HashMap<>();
        es.getbBoxes().forEach((key,value)->bboxes.put(key,value));
        FileWriter writer = new FileWriter();
        writer.writeObjectToFile(bboxes, EXISTING_DATASET_BBOXES);
        }

    /**
     * Get the existing searches, if some exist. Otherwise call the ExistingSearch constructor
     * @return
     * @throws IOException
     */
    public ExistingDatasetBBoxes readExistingSearches() throws IOException {
        ExistingDatasetBBoxes es;
        FileWriter writer = new FileWriter();
        try {
            //TODO something maybe going wrong here. Says it's trying to convert a HashMap to Dataverse.ExistingDatasetBBoxes. Maybe gone now that there is only one map
            HashMap<String, BoundingBox> bBoxes = (HashMap<String, BoundingBox>) writer.readSavedObject(EXISTING_DATASET_BBOXES);
            es = ExistingDatasetBBoxes.getExistingHarvests();
            es.setbBoxes(bBoxes);
        } catch (FileNotFoundException e) {
            es = ExistingDatasetBBoxes.getExistingHarvests();
            writeExistingSearches(es);
        } catch (ClassNotFoundException e){
            logger.error("something went wonky loading the existing searches from the file");
            return ExistingDatasetBBoxes.getExistingHarvests();
        }
        return es;
    }
}
