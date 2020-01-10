package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;


public class ExistingVectorRecords extends ExisitingFile implements Serializable {
    private static final long serialVersionUID = 5416853597895403201L;
    private HashMap<String, String> records;
    private static ExistingVectorRecords single_instance = null;

    public static ExistingVectorRecords getExistingVectors() {
        if (single_instance == null) {
            single_instance = new ExistingVectorRecords();
        }
        return single_instance;
    }

    private ExistingVectorRecords(){
        logger = new GeoLogger(this.getClass());
        records = new HashMap<>();
    }

    public void addOrReplaceRecord(String doiAndFileName, String originalName){
        records.put(doiAndFileName,originalName);
    }
    public HashMap<String, String> readExistingRecords(String path){
        HashMap<String, String> newFile = new HashMap<>();
        FileWriter fw = new FileWriter();
        try {
            records =  (HashMap<String, String>) fw.readSavedObject(GeodisyStrings.replaceSlashes(path));
            return records;
        } catch (IOException e) {
            logger.error("Something went wrong reading " + path);
            return newFile;
        } catch (ClassNotFoundException e) {
            logger.error("Something went wrong parsing " + path);
            return newFile;
        } catch (NullPointerException e){
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

