package BaseFiles;

import Dataverse.ExistingSearches;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ExistingSearchesFile {
    String path;

    public ExistingSearchesFile(String path) {
        this.path = path;
    }

    public void writeExistingSearches(ExistingSearches existingSearches) throws IOException {
        FileWriter writer = new FileWriter();
        writer.writeObjectToFile(existingSearches,path);
        }



    public ExistingSearches readExistingSearches() throws IOException, ClassNotFoundException {
        FileWriter writer = new FileWriter();
        ExistingSearches es = ExistingSearches.getExistingSearches();
        try {
            es = (ExistingSearches) writer.readSavedObject(path);
        } catch (FileNotFoundException e) {
            writeExistingSearches(es);
        }
        return es;

    }
}