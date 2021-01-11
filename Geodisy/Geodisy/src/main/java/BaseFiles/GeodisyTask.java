package BaseFiles;

import Crosswalking.GeoBlacklightJson.GeoCombine;
import Dataverse.*;
import Dataverse.FindingBoundingBoxes.Countries;
import _Strings.GeodisyStrings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static _Strings.GeodisyStrings.*;

public class GeodisyTask {
    GeoLogger logger = new GeoLogger(this.getClass());
    ExistingHarvests existingHarvests;
    ExistingCallsToCheck existingCallsToCheck;
    ExistingGeoLabels existingGeoLabels;
    ExistingGeoLabelsVals existingGeoLabelsVals;

    SourceRecordFiles srf;
    public GeodisyTask() {
    }
/**
 * 
 */
    public void run() {
       String recsToCheck;
       String startErrorLog;
       String endErrorLog;
       String startWarningLog;
       String endWarningLog;
       long startTime = Calendar.getInstance().getTimeInMillis();
        Countries.getCountry();
        try {
            FileWriter fW = new FileWriter();
            verifyFiles(fW);

            existingHarvests = ExistingHarvests.getExistingHarvests();
            existingHarvests.saveExistingSearchs(existingHarvests.getbBoxes(),EXISTING_BBOXES, "ExistingBBoxes");
            existingCallsToCheck = ExistingCallsToCheck.getExistingCallsToCheck();
            existingCallsToCheck.saveExistingSearchs(existingCallsToCheck.getRecords(),EXISTING_CHECKS,"ExistingCallsToCheck");
            existingGeoLabels = ExistingGeoLabels.getExistingLabels();
            existingGeoLabelsVals = ExistingGeoLabelsVals.getExistingGeoLabelsVals();
            srf = SourceRecordFiles.getSourceRecords();

            startErrorLog = new String(Files.readAllBytes(Paths.get(ERROR_LOG)));
            startWarningLog = new String(Files.readAllBytes(Paths.get(WARNING_LOG)));

            Geodisy geo = new Geodisy();

            //Call to get metadata from FRDR Harvester
            List<SourceJavaObject> sJOs = geo.harvestFRDRMetadata();
            //deleteEmptyFolders();

            /** We are no longer updating Open Geo Metadata records due to sparely populated metadata files
            /if(!IS_WINDOWS) {
                sendRecordsToGeoBlacklight();
                if(!TEST) {
                    JGit jgit = new JGit();
                    jgit.updateRemoteMetadata();
                }
            }**
             
            /**
             *
             * Saving a record of all the files that were downloaded
             */

            endErrorLog = keepMajErrors();
            endWarningLog = keepMinErrors();

                HashMap<String, String> newRecords = existingCallsToCheck.getNewRecords();
                if(newRecords.size() != 0){
                    recsToCheck = new String(Files.readAllBytes(Paths.get(RECORDS_TO_CHECK)));
                    Set<String> keys = newRecords.keySet();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    recsToCheck += System.lineSeparator() + dtf.format(now);
                    for(String k : keys){
                        recsToCheck += System.lineSeparator() + newRecords.get(k);
                    }
                    emailCheckRecords();
                    fW.writeStringToFile(recsToCheck,RECORDS_TO_CHECK);
            }
            if(!startErrorLog.equals(endErrorLog)){
                fW.writeStringToFile(endErrorLog,ERROR_LOG);
            }
            if(!startWarningLog.equals(endWarningLog)){
                fW.writeStringToFile(endWarningLog,WARNING_LOG);
            }
            existingHarvests.saveExistingSearchs(existingHarvests.getbBoxes(),EXISTING_BBOXES, "ExistingBBoxes");
            existingGeoLabelsVals.saveExistingFile(existingGeoLabelsVals.getValues(), EXISTING_GEO_LABELS_VALS, "ExistingGeoLabelsVals");
            existingGeoLabels.saveExistingFile(existingGeoLabels.getGeoLabels(),EXISTING_GEO_LABELS,"ExistingGeoLabels");

            //TODO Uncomment the following once Geoserver has been implemented
            ExistingRasterRecords existingRasterRecords = ExistingRasterRecords.getExistingRasters();
            existingRasterRecords.saveExistingFile(existingRasterRecords.getRecords(),RASTER_RECORDS, "ExistingRasterRecords");
            ExistingVectorRecords existingVectorRecords = ExistingVectorRecords.getExistingVectors();
            existingVectorRecords.saveExistingFile(existingVectorRecords.getRecords(),VECTOR_RECORDS,"ExistingVectorRecords");


        } catch (IOException  e) {
            logger.error("Something went wrong trying to read permanent file ExistingRecords.txt!");
        } finally {
            Calendar end =  Calendar.getInstance();
            Long total = end.getTimeInMillis()-startTime;
            System.out.println("Finished a run at: " + end.getTime() + " after " + total + " milliseconds");
        }
    }

    private void verifyFiles(FileWriter fW) {
        File folder = new File(SAVED_FILES);
        folder.mkdir();
        File logs = new File(LOGS);
        logs.mkdir();
        if(!fW.verifyFileExistence(RECORDS_TO_CHECK))
            ExistingCallsToCheck.getExistingCallsToCheck();
        fW.verifyFileExistence(ERROR_LOG);
        fW.verifyFileExistence(WARNING_LOG);
        fW.verifyFileExistence(EXISTING_BBOXES);
        fW.verifyFileExistence(EXISTING_CHECKS);
        fW.verifyFileExistence(DOWNLOADED_FILES);
        fW.verifyFileExistence(EXISTING_GEO_LABELS_VALS);
        fW.verifyFileExistence(RASTER_RECORDS);
        fW.verifyFileExistence(VECTOR_RECORDS);
    }

    private void deleteEmptyFolders() {
        boolean isFinished;
        String location = GEODISY_PATH_ROOT + GeodisyStrings.replaceSlashes("metadata/");
        do {
            isFinished = true;
            isFinished = deleteFolders(location, isFinished);
    }while(!isFinished);
        location = GEODISY_PATH_ROOT + GeodisyStrings.replaceSlashes("datasetFiles/");
        do {
            isFinished = true;
            isFinished = deleteFolders(location,isFinished);
        }while(!isFinished);
    }

    private boolean deleteFolders(String location, boolean isFinished) {

        File folder = new File(location);
        File[] listofFiles = folder.listFiles();
        if (listofFiles.length == 0) {
            System.out.println("Folder Name :: " + folder.getAbsolutePath() + " is deleted.");
            folder.delete();
            return false;
        } else {
            for (int j = 0; j < listofFiles.length; j++) {
                File file = listofFiles[j];
                if (file.isDirectory()) {
                    deleteFolders(file.getAbsolutePath(),isFinished);
                }
            }
        }
        return isFinished;
    }


    private void sendRecordsToGeoBlacklight() {
        GeoCombine combine = new GeoCombine();
        combine.index();
    }





    /**
     * Removes INFO messages from the error log.
     * @return String with no INFO messages
     * @throws IOException
     */
    private String keepMajErrors()throws IOException {
        String end = new String(Files.readAllBytes(Paths.get(ERROR_LOG)));
        String[] lines = end.split(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(String s: lines){
            if(s.contains("INFO")||s.contains("WARN"))
                continue;
            sb.append(s+System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Removes ERROR messages from the recordsToCheck log.
     * @return String with no ERROR messages
     * @throws IOException
     */
    public String keepInfo() throws IOException {
        String end = new String(Files.readAllBytes(Paths.get(RECORDS_TO_CHECK)));
        String[] lines = end.split(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(String s: lines){
            if(s.contains("ERROR")||s.contains("WARN"))
                continue;
            sb.append(s + System.lineSeparator());        }
        return sb.toString();
    }

    private String keepMinErrors() throws IOException{
        String end = new String(Files.readAllBytes(Paths.get(WARNING_LOG)));
        String[] lines = end.split(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(String s: lines){
            if(s.contains("ERROR")||s.contains("INFO"))
                continue;
            sb.append(s + System.lineSeparator());        }
        return sb.toString();
    }
    //TODO setup email system
    private void emailCheckRecords() {
    }

    //for testing the scheduler
    /*@Override
    public void run(){
        TimeZone tz = TimeZone.getTimeZone("America/Vancouver");
        Calendar today = Calendar.getInstance(tz);
        System.out.println("Current time: " + today.getTime());
    }*/
  
    
}
