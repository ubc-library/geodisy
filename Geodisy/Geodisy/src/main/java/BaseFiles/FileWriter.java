package BaseFiles;


import Dataverse.ExistingDatasetBBoxes;
import _Strings.GeodisyStrings;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * Class used to write things to files (logs and existing records files)
 */
public class FileWriter {
    GeoLogger logger;
    java.io.FileWriter fw;
    public FileWriter() {
        logger = new GeoLogger(this.getClass());
    }

    public FileWriter(String path){
        logger = new GeoLogger(this.getClass());
        try {
            fw = new java.io.FileWriter(path);
        } catch (IOException e) {
            logger.error("Something was wonky with the given path: " + path);
        }
    }

    public FileWriter(File file) throws IOException{
        logger = new GeoLogger(this.getClass());
        fw = new java.io.FileWriter(file);
    }
    /**
     * Saves a file on the local machine to the path provided
     * @param objectToSave
     * @param path
     * @throws IOException
     */
    public void writeObjectToFile(Object objectToSave, String path) throws IOException {
        verifyFileExistence(path);
        FileOutputStream f =  new FileOutputStream(new File(path));
        ObjectOutputStream o =  new ObjectOutputStream(f);
        o.writeObject(objectToSave);
        o.close();
        f.close();
    }

    public void writeStringToFile(String stringToSave, String path) throws IOException{
        path = GeodisyStrings.replaceSlashes(path);
        verifyFileExistence(path);
        File file = new File(path);
        FileUtils.writeStringToFile(file, stringToSave,"UTF-8");
    }
    //TODO figure out where this is actually getting used
    public static String fixPath(String path) {
        String answer = GeodisyStrings.removeHTTPSAndReplaceAuthority(path);
        if (path.contains("doi:")) {
            answer = path.replace("doi:", "doi/");
        }

        //TODO figure out what to do with a handle
        if(path.contains("handle:")){
            answer = answer.replace("handle:","handle/");
        }
        answer = answer.replace(".","/");
        return answer;
    }

    /**
     * Reads a local file at the path provided
     * @param path
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object readSavedObject(String path) throws ClassNotFoundException,IOException {
        if (!verifyFileExistence(path))
            throw new FileNotFoundException();
        try (FileInputStream fi = new FileInputStream(new File(path))) {
            try (ObjectInputStream oi = new ObjectInputStream(fi)) {
                Object answer = null;
                try {
                    answer = oi.readObject();
                } catch (ClassNotFoundException e) {
                    throw new ClassNotFoundException();
                }
                return answer;
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }        
    }

    /**
     * Get values from the ExistingRecords.txt. If the files isn't found create one and seed it with a filler record
     * @param path
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ExistingDatasetBBoxes readExistingSearches(String path) throws IOException {
        ExistingDatasetBBoxes es = ExistingDatasetBBoxes.getExistingHarvests();
        ObjectInputStream ois = null;
        try {
            File file = new File(path);
            File directory = new File(file.getParentFile().getAbsolutePath());
            directory.mkdirs();
            if(file.createNewFile())
                return ExistingDatasetBBoxes.getExistingHarvests();
            FileInputStream f = new FileInputStream(file);
            ois = new ObjectInputStream(f);
            es = (ExistingDatasetBBoxes) ois.readObject();
        } catch (FileNotFoundException e) {
            es = ExistingDatasetBBoxes.getExistingHarvests();
            writeObjectToFile(es, path);
        } catch (EOFException e) {
            return ExistingDatasetBBoxes.getExistingHarvests();
        } catch (ClassNotFoundException e){
            System.out.println("something went wonky loading the existing searches from the file");
            return ExistingDatasetBBoxes.getExistingHarvests();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
        return es;
    }

    /**
     * Checks if file exists. If file doesn't exist, create it.
     * @param path
     * @return
     */
    public boolean verifyFileExistence(String path) {
        path = GeodisyStrings.replaceSlashes(path);
        try {
            File f =  new File(path);
            boolean  created = f.createNewFile();
            if(created) {
                logger.warn("File " + path + " didn't already exists, so created it");
                return false;
            }
            return true;
        } catch (IOException e) {
            return true;
        }
    }

    public boolean write(String obj){

        try {
            fw.write(obj);
        } catch (IOException e) {
            logger.error("Something went wrong trying to save the string: " + obj);
            return false;
        }
        return true;
    }

    public java.io.FileWriter getFw(){
        return fw;
    }

    public String getPath(){
        return fw.toString();
    }
}
