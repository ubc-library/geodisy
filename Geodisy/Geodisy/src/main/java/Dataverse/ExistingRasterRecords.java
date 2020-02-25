package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import static BaseFiles.GeodisyStrings.RASTER_RECORDS;


public class ExistingRasterRecords extends ExisitingFile implements Serializable {
    private static final long serialVersionUID = 5416853597895403201L;
    private HashMap<String, String> records;
    private static ExistingRasterRecords single_instance = null;

    public static ExistingRasterRecords getExistingRasters() {
        if (single_instance == null) {
            single_instance = new ExistingRasterRecords();
        }
        return single_instance;
    }

    private ExistingRasterRecords(){
        logger = new GeoLogger(this.getClass());
        records = readExistingRecords(RASTER_RECORDS);
    }

    public void addOrReplaceRecord(String doi, String fileName){
        records.put(doi+fileName,fileName);
    }

    public HashMap<String, String> readExistingRecords(String path){
        HashMap<String, String> newFile = new HashMap<>();
        FileWriter fw = new FileWriter();
        File checkBlank = new File(path);
        if(checkBlank.toString().isEmpty())
            return newFile;
        try {
            records =  (HashMap<String, String>) fw.readSavedObject(GeodisyStrings.replaceSlashes(path));
            return records;
        } catch (FileNotFoundException e){
            return newFile;
        } catch (IOException e) {
            logger.error("IO Exception: Something went wrong reading " + path);
            return newFile;
        } catch (ClassNotFoundException e) {
            logger.error("Class Not Found Error: Something went wrong parsing " + path + " or file was empty");
            return newFile;
        }
    }
    public void removeRecord(String doi, String filename){
        if(hasRecord(doi, filename));
            records.remove(doi+filename);
    }

    public HashMap<String, String> getRecords() {
        return records;
    }

    public boolean hasRecord(String doi, String filename){
        return records.containsKey(doi+filename);
    }
}
