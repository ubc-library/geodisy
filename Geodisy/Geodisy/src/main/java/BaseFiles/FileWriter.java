package BaseFiles;


import Dataverse.ExistingSearches;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class used to write things to files (logs and existing records files)
 */
public class FileWriter {
    public FileWriter() {
    }

    public void writeObjectToFile(Object objectToSave, String path) throws IOException {
        verifyFileExistence(path);
        FileOutputStream f =  new FileOutputStream(new File(path));
        ObjectOutputStream o =  new ObjectOutputStream(f);
        o.writeObject(objectToSave);
        o.close();
        f.close();
    }
    public Object readSavedObject(String path) throws IOException, ClassNotFoundException {
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
        Object answer = oi.readObject();
        oi.close();
        fi.close();
        return answer;
    }

    /**
     * Get values from the ExistingRecords.txt. If the files isn't found create one and seed it with a filler record
     * @param path
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ExistingSearches readExistingSearches(String path) throws IOException {
        ExistingSearches es = ExistingSearches.getExistingSearches();
        try {
            File file = new File(path);
            File directory = new File(file.getParentFile().getAbsolutePath());
            directory.mkdirs();
            if(file.createNewFile())
                return ExistingSearches.getExistingSearches();
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(f);
            es = (ExistingSearches) ois.readObject();
        } catch (FileNotFoundException e) {
            es = ExistingSearches.getExistingSearches();
            writeObjectToFile(es, path);
        } catch (EOFException e) {
            return ExistingSearches.getExistingSearches();
        } catch (ClassNotFoundException e){
            System.out.println("something went wonky loading the existing searches from the file");
            return ExistingSearches.getExistingSearches();
        }
        return es;
    }

    /**
     * Checks if file exists. If file doesn't exist, create it.
     * @param path
     * @return
     */
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
