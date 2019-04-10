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
        verifyFileExistence();
        FileOutputStream f =  new FileOutputStream(new File(path));
        ObjectOutputStream o =  new ObjectOutputStream(f);
        o.writeObject(existingSearches);
        o.close();
        f.close();
    }



    public ExistingSearches readExistingSearches() throws IOException, ClassNotFoundException {
        verifyFileExistence();
        FileInputStream fi =  new FileInputStream(new File(path));
        ObjectInputStream oi =  new ObjectInputStream(fi);
        ExistingSearches answer = (ExistingSearches) oi.readObject();
        oi.close();
        fi.close();
        return answer;

    }
    private void verifyFileExistence() {
        Path filePath = Paths.get(path);
        try {
            Files.createFile(filePath);
            System.out.println("File didn't already exists, so created it");
            ExistingSearches es = ExistingSearches.getExistingSearches();
            if(es.isEmpty())
                es.addBBox("_filler_", new BoundingBox());
            writeExistingSearches(es);
        } catch (IOException e) {

        }

    }
}