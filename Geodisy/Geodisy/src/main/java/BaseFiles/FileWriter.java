package BaseFiles;


import Dataverse.ExistingSearches;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriter {
    public void writeObjectToFile(Object objectToSave, String path) throws IOException {
        verifyFileExistence(path);
        FileOutputStream f =  new FileOutputStream(new File(path));
        ObjectOutputStream o =  new ObjectOutputStream(f);
        o.writeObject(objectToSave);
        o.close();
        f.close();
    }
    public Object readSavedObject(String path) throws IOException, ClassNotFoundException,FileNotFoundException {
        if (!verifyFileExistence(path))
            throw new FileNotFoundException();
        FileInputStream fi =  new FileInputStream(new File(path));
        ObjectInputStream oi;
        try{
            oi =  new ObjectInputStream(fi);
        } catch (EOFException e){
            ExistingSearches es = ExistingSearches.getExistingSearches();
            writeObjectToFile(ExistingSearches.getExistingSearches(),path);
            fi.close();
            return es;
        }
        Object answer = (Object) oi.readObject();
        oi.close();
        fi.close();
        return answer;
    }

    private boolean verifyFileExistence(String path) {
        Path filePath = Paths.get(path);
        try {
            Files.createFile(filePath);
            System.out.println("File didn't already exists, so created it");
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
