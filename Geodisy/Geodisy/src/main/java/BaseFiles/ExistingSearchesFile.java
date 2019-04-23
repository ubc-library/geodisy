package BaseFiles;

import Dataverse.ExistingSearches;


import java.io.*;

import static BaseFiles.GeodisyStrings.EXISTING_RECORDS;


public class ExistingSearchesFile {
    private String path;
    public ExistingSearchesFile(){
        path = EXISTING_RECORDS;
    }

    public ExistingSearchesFile(String path){
        this.path = path;
    }
    public void writeExistingSearches(ExistingSearches existingSearches) throws IOException {
        FileWriter writer = new FileWriter();
        writer.writeObjectToFile(existingSearches,path);
        }



    public ExistingSearches readExistingSearches() throws IOException, ClassNotFoundException {
        ExistingSearches es;
        try {
            FileInputStream f = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(f);
            es = (ExistingSearches) ois.readObject();
        } catch (FileNotFoundException e) {
            es = ExistingSearches.getExistingSearches();
            writeExistingSearches(es);
        } catch (ClassNotFoundException e){
            System.out.println("something went wonky loading the existing searches from the file");
            return ExistingSearches.getExistingSearches();
        }
        return es;

    }
}