package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;


import java.io.*;
import java.util.HashMap;

import static _Strings.GeodisyStrings.EXISTING_BBOXES;

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
        HashMap<String, BoundingBox> bboxes = new HashMap<>();
        es.getbBoxes().forEach((key,value)->bboxes.put(key,value));
        FileWriter writer = new FileWriter();
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
            //TODO something maybe going wrong here. Says it's trying to convert a HashMap to Dataverse.ExistingHarvests. Maybe gone now that there is only one map
            HashMap<String, BoundingBox> bBoxes = (HashMap<String, BoundingBox>) writer.readSavedObject(EXISTING_BBOXES);
            es = ExistingHarvests.getExistingHarvests();
            es.setbBoxes(bBoxes);
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
