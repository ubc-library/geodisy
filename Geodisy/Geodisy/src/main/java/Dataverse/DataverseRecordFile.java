package Dataverse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;




import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class DataverseRecordFile {
    String title;
    String doi = "N/A";
    int dbID;
    String server;
    Logger logger = LogManager.getLogger(this.getClass());
    String recordURL;

    public DataverseRecordFile(String title, String doi, String server){
        this.title = title;
        this.doi = doi;
        this.server = server;
        recordURL = server+"api/access/datafile/:/persistentId/?persistentId=" + doi;
        getFile();
    }

    public DataverseRecordFile(String title, int dbID, String server){
        this.title = title;
        this.dbID = dbID;
        this.server = server;
        recordURL = String.format(server+"api/access/datafile/$d", dbID);
        getFile();
    }

    private void getFile() {
        try {
            FileUtils.copyURLToFile(
                    new URL(recordURL),
                    new File(title),
                    10000, //10 seconds connection timeout
                    120000); //2 minute read timeout
        } catch (MalformedURLException e) {
            logger.error(String.format("Something is wonky with the DOI $s or the dbID $d", doi, dbID));
        } catch (IOException e) {
            logger.error(String.format("Something went wrong with downloading file $s, with doi $s or dbID $d", title, doi, dbID));
            e.printStackTrace();
        }

    }

}
