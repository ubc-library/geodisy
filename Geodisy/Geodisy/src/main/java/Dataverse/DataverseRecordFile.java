package Dataverse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Info for downloading a geospatial dataset file, and the methods used to download the files.
 */
public class DataverseRecordFile {
    String title;
    String doi = "N/A";
    int dbID;
    String server;
    Logger logger = LogManager.getLogger(this.getClass());
    String recordURL;
    String datasetDOI;

    /**
     * Creates a DataverseRecordFile when there is a File-specific doi.
     * @param title
     * @param doi
     * @param dbID
     * @param server
     * @param datasetDOI
     */
    public DataverseRecordFile(String title, String doi, int dbID, String server, String datasetDOI){
        this.title = title;
        this.doi = doi;
        this.dbID = dbID;
        this.server = server;
        recordURL = server+"api/access/datafile/:persistentId/?persistentId=" + doi;
        this.datasetDOI = datasetDOI.replaceAll("\\.","_").replaceAll("/","_");
    }

    /**
     * Creates a DataverseRecordFile when there is no File-specific doi, only a dataset doi and a database ID.
     * @param title
     * @param dbID
     * @param server
     * @param datasetDOI
     */
    public DataverseRecordFile(String title, int dbID, String server, String datasetDOI){
        this.title = title;
        this.dbID = dbID;
        this.doi = String.valueOf(dbID);
        this.server = server;
        recordURL = String.format(server+"api/access/datafile/$d", dbID);
        this.datasetDOI = datasetDOI;
    }

    public void getFile() {
        try {
            new File("./datasetFiles/" + datasetDOI + "/").mkdirs();
            String filePath = "./datasetFiles/" + datasetDOI + "/" + title;
            FileUtils.copyURLToFile(
                    new URL(recordURL),
                    new File(filePath),
                    10000, //10 seconds connection timeout
                    120000); //2 minute read timeout
        } catch (FileNotFoundException e){
            logger.error(String.format("This dataset file %s couldn't be found from dataset %s", dbID, doi));
            logger.info("Check out dataset " + datasetDOI);
        }catch (MalformedURLException e) {
            logger.error(String.format("Something is wonky with the DOI " + doi + " or the dbID " + dbID));
        } catch (IOException e) {
            logger.error(String.format("Something went wrong with downloading file %s, with doi %s or dbID %d", title, doi, dbID));
            e.printStackTrace();
        }

    }

    public String getFileIdentifier(){
        return doi;
    }

}
