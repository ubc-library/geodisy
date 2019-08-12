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
    private String path = EXISTING_RECORDS;
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
        BaseFiles.FileWriter writer = new FileWriter();
        writer.writeObjectToFile(existingSearches,path);
        }


    public ExistingSearches readExistingSearches() throws IOException {
        ExistingSearches es;
        try {
            FileInputStream f = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(f);
            es = (ExistingSearches) ois.readObject();
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