package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;

import java.io.IOException;

public abstract class ExisitingFile {
    GeoLogger logger;
    public void saveExistingFile(Object object, String path, String className){
        FileWriter fw  = new FileWriter();
        try {
            fw.writeObjectToFile(object,path);
        } catch (IOException e) {
            getLogger().error("Something went wrong saving " + className);
        }
    }

    protected abstract GeoLogger getLogger();
}
