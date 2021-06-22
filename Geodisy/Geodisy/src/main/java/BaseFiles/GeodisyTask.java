package BaseFiles;

import Crosswalking.GeoBlacklightJson.GeoCombine;
import Crosswalking.XML.XMLTools.JGit;
import Dataverse.*;
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
    ExistingDatasetBBoxes existingDatasetBBoxes;
    ExistingCallsToCheck existingCallsToCheck;
    ExistingGeoLabels existingGeoLabels;
    ExistingGeoLabelsVals existingGeoLabelsVals;
    ExistingLocations existingLocations;

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
        try {
            FileWriter fW = loadSavedFiles();

            srf = SourceRecordFiles.getSourceRecords();

            startErrorLog = new String(Files.readAllBytes(Paths.get(ERROR_LOG)));
            startWarningLog = new String(Files.readAllBytes(Paths.get(WARNING_LOG)));

            Geodisy geo = new Geodisy();

            //Call to get metadata from FRDR Harvester
            List<SourceJavaObject> sJOs = geo.harvestFRDRMetadata();
            //deleteEmptyFolders();

            if(!IS_WINDOWS) {
                sendRecordsToGeoBlacklight();
                if(!TEST) {
                    JGit jgit = new JGit();
                    jgit.updateRemoteMetadata();
                }
            }
             
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
            existingDatasetBBoxes.saveExistingSearchs(existingDatasetBBoxes.getbBoxes(), EXISTING_DATASET_BBOXES, "ExistingBBoxes");
            existingGeoLabelsVals.saveExistingFile(existingGeoLabelsVals.getValues(), EXISTING_GEO_LABELS_VALS, "ExistingGeoLabelsVals");
            existingGeoLabels.saveExistingFile(existingGeoLabels.getGeoLabels(),EXISTING_GEO_LABELS,"ExistingGeoLabels");

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

    public FileWriter loadSavedFiles() throws IOException{
        FileWriter fw = new FileWriter();
        fw.verifyFiles();
        existingDatasetBBoxes = ExistingDatasetBBoxes.getExistingHarvests();
        existingDatasetBBoxes.saveExistingSearchs(existingDatasetBBoxes.getbBoxes(), EXISTING_DATASET_BBOXES, ExistingDatasetBBoxes.class.getName());
        existingLocations = ExistingLocations.getExistingLocations();
        existingLocations.saveExistingSearchs(existingLocations.getBBoxes(),EXISTING_LOCATION_BBOXES, ExistingLocations.class.getName());
        existingLocations.saveExistingSearchs(existingLocations.getNames(),EXISTING_LOCATION_NAMES, ExistingLocations.class.getName());
        existingCallsToCheck = ExistingCallsToCheck.getExistingCallsToCheck();
        existingCallsToCheck.saveExistingSearchs(existingCallsToCheck.getRecords(),EXISTING_CHECKS,"ExistingCallsToCheck");
        existingGeoLabels = ExistingGeoLabels.getExistingLabels();
        existingGeoLabels.saveExistingFile(existingGeoLabels.getGeoLabels(),EXISTING_GEO_LABELS,ExistingGeoLabels.class.getName());
        existingGeoLabelsVals = ExistingGeoLabelsVals.getExistingGeoLabelsVals();
        existingGeoLabelsVals.saveExistingFile(existingGeoLabelsVals.getValues(),EXISTING_GEO_LABELS_VALS,ExistingGeoLabelsVals.class.getName());
        return fw;
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
            try {
                Files.deleteIfExists(Paths.get(location));
            } catch (IOException e) {
                logger.error("Something went wrong trying to delete folder: " + location);
            }
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
