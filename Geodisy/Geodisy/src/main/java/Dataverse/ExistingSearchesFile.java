package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;


import java.io.*;
import java.util.HashMap;

import static BaseFiles.GeodisyStrings.EXISTING_BBOXES;
import static BaseFiles.GeodisyStrings.EXISTING_RECORDS;

/**
 * Class for getting the list of records that have already been downloaded
 */
public class ExistingSearchesFile {
    GeoLogger logger = new GeoLogger(this.getClass());
    ExistingHarvests es;

    /**
     * Constructor to use for production environment;
     */
    public ExistingSearchesFile(){
        es = ExistingHarvests.getExistingHarvests();
    }

    /**
     * Only use this constructor for testing by writing to a test Existing records file
     * @param path
     */
    public ExistingSearchesFile(String path){
        es = ExistingHarvests.getExistingHarvests();
    }

    public void writeExistingSearches(ExistingHarvests existingSearches) throws IOException {
        HashMap<String, DataverseRecordInfo> records = new HashMap<>();
        es.getRecordVersions().forEach((key, value)-> records.put(key,value));
        HashMap<String, BoundingBox> bboxes = new HashMap<>();
        es.getbBoxes().forEach((key,value)->bboxes.put(key,value));
        FileWriter writer = new FileWriter();
        writer.writeObjectToFile(records,EXISTING_RECORDS);
        writer.writeObjectToFile(bboxes,EXISTING_BBOXES);
        }

    /**
     * Get the existing searches, if some exist. Otherwise call the ExistingSearch constructor
     * @return
     * @throws IOException
     */
    public ExistingHarvests readExistingSearches() throws IOException {
        ExistingHarvests es;
        FileWriter writer = new FileWriter();
        try {
            //TODO something is going wrong here. Says it's trying to convert a HashMap to Dataverse.ExistingHarvests. Maybe save the maps separately?
            HashMap<String, DataverseRecordInfo> records = (HashMap<String, DataverseRecordInfo>) writer.readSavedObject(EXISTING_RECORDS);
            HashMap<String, BoundingBox> bBoxes = (HashMap<String, BoundingBox>) writer.readSavedObject(EXISTING_BBOXES);
            es = ExistingHarvests.getExistingHarvests();
            es.setbBoxes(bBoxes);
            es.setRecords(records);
        } catch (FileNotFoundException e) {
            es = ExistingHarvests.getExistingHarvests();
            writeExistingSearches(es);
        } catch (ClassNotFoundException e){
            logger.error("something went wonky loading the existing searches from the file");
            return ExistingHarvests.getExistingHarvests();
        }
        return es;
    }
}
