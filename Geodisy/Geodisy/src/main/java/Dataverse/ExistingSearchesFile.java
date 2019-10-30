package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import Dataverse.ExistingSearches;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.*;

import static BaseFiles.GeodisyStrings.EXISTING_RECORDS;

/**
 * Class for getting the list of records that have already been downloaded
 */
public class ExistingSearchesFile {
    private String path;
    GeoLogger logger = new GeoLogger(this.getClass());

    /**
     * Constructor to use for production environment;
     */
    public ExistingSearchesFile(){
        path = EXISTING_RECORDS;
    }

    /**
     * Only use this constructor for testing by writing to a test Existing records file
     * @param path
     */
    public ExistingSearchesFile(String path){
        this.path = path;
    }

    public void writeExistingSearches(ExistingSearches existingSearches) throws IOException {
        FileWriter writer = new FileWriter();
        writer.writeObjectToFile(existingSearches,path);
        }

    /**
     * Get the existing searches, if some exist. Otherwise call the ExistingSearch constructor
     * @return
     * @throws IOException
     */
    public ExistingSearches readExistingSearches() throws IOException {
        ExistingSearches es;
        FileWriter writer = new FileWriter();
        try {
            //TODO something is going wrong here. Says it's trying to convert a HashMap to Dataverse.ExistingSearches. Maybe save the maps separately?
            es = (ExistingSearches) writer.readSavedObject(path);
        } catch (FileNotFoundException e) {
            es = ExistingSearches.getExistingSearches();
            writeExistingSearches(es);
        } catch (ClassNotFoundException e){
            logger.warn("something went wonky loading the existing searches from the file");
            return ExistingSearches.getExistingSearches();
        }
        return es;
    }
}