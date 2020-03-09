package Dataverse;

import BaseFiles.GeoLogger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;

import static BaseFiles.GeodisyStrings.DOWNLOADED_FILES;

public class DownloadedFiles {
    private static DownloadedFiles single_instance = null;
    private LinkedList<String> downloads;
    private GeoLogger logger;

    public static DownloadedFiles getDownloadedFiles() {
        if (single_instance == null) {
            single_instance = new DownloadedFiles();
        }
        return single_instance;
    }

    private DownloadedFiles() {
        logger = new GeoLogger(this.getClass());
        this.downloads = new LinkedList<>();
    }

    public void saveDownloads() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(DOWNLOADED_FILES,true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        boolean first = true;
        for(String s:downloads){
            if(!first){
                s = "," + s;
            }
            first = false;
            printWriter.print(s);

        }
        printWriter.println();
        printWriter.close();
        } catch (IOException e) {
            logger.error("Something went wrong trying to save the list of downloaded files to file " + DOWNLOADED_FILES);
        }
    }
    public void addDownload(String fileName, String doi, int dbID){
        String datetime = String.valueOf(ZonedDateTime.now(ZoneId.of("Canada/Pacific")));
        downloads.add("FileID=" + dbID + ";PersistantID=" + doi + ";File Name=" + fileName + ";DateTime=" + datetime);
    }

    public void resetList(){
        downloads = new LinkedList<>();
    }
}
