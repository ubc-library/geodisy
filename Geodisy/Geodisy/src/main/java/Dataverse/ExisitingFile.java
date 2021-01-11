package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;

import java.io.IOException;
import java.util.HashMap;

public abstract class ExisitingFile {
    GeoLogger logger;
    public void saveExistingFile(Object object, String path, String className){
        FileWriter fw  = new FileWriter();
        try {
            fw.writeObjectToFile(object,path);
        } catch (IOException e) {
            logger.error("Something went wrong saving " + className);
        }
    }
}
