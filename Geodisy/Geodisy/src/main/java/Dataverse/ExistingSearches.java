package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import _Strings.GeodisyStrings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public abstract class ExistingSearches extends ExisitingFile {
    GeoLogger logger;

    public void saveExistingSearchs(HashMap object, String path, String className){
        saveExistingFile(object,path,className);
    }

    public HashMap<String, DataverseRecordInfo> readExistingRecords(String path){
        HashMap<String, DataverseRecordInfo> newFile = new HashMap<>();
        FileWriter fw = new FileWriter();
        File checkBlank = new File(path);
        if(checkBlank.toString().isEmpty())
            return newFile;
        try {
            return  (HashMap<String, DataverseRecordInfo>) fw.readSavedObject(GeodisyStrings.replaceSlashes(path));
        } catch (IOException e) {
            getLogger().error("Something went wrong reading " + path + ". Ignore this if the file didn't exist before this run or the file is empty");
            return newFile;
        } catch (ClassNotFoundException e) {
            getLogger().error("Something went wrong parsing " + path);
            return newFile;
        } catch (NullPointerException e){
            return newFile;
        }
    }
}
