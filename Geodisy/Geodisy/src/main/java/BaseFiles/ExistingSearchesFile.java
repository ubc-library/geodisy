package BaseFiles;

import Dataverse.ExistingSearches;


import java.io.*;


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