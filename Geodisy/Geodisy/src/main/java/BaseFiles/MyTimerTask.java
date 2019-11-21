package BaseFiles;

import Crosswalking.Crosswalk;
import Crosswalking.GeoBlacklightJson.DataGBJSON;
import Crosswalking.GeoBlacklightJson.GeoCombine;
import Dataverse.*;
import Dataverse.FindingBoundingBoxes.Countries;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;

import static BaseFiles.GeodisyStrings.*;

/**
 *This extends TimerTask to create the class that will
 * be used in the BaseFiles.Scheduler to start the tests, harvesting from Dataverse and
 * exporting data and ISO-19115 metadata to Geoserver.
 * @author pdante
 */
public class MyTimerTask extends TimerTask {
    GeoLogger logger = new GeoLogger(this.getClass());
    ExistingHarvests existingHarvests;
    ExistingCallsToCheck existingCallsToCheck;
    public MyTimerTask() {
    }
/**
 * 
 */
    @Override
    public void run() {
       String startRecsToCheck;
       String endRecsToCheck;
       String startErrorLog;
       String endErrorLog;
       String startWarningLog;
       String endWarningLog;
       long startTime = Calendar.getInstance().getTimeInMillis();
        Countries.getCountry();
        try {
            FileWriter fW = new FileWriter();
            fW.verifyFileExistence(RECORDS_TO_CHECK);
            fW.verifyFileExistence(ERROR_LOG);
            fW.verifyFileExistence(WARNING_LOG);
            existingHarvests = ExistingHarvests.getExistingHarvests();
            existingCallsToCheck = ExistingCallsToCheck.getExistingCallsToCheck();
            startRecsToCheck = new String(Files.readAllBytes(Paths.get(RECORDS_TO_CHECK)));
            startErrorLog = new String(Files.readAllBytes(Paths.get(ERROR_LOG)));
            startWarningLog = new String(Files.readAllBytes(Paths.get(WARNING_LOG)));

            Geodisy geo = new Geodisy();

            //This section is the initial search for new records in the repositories. We will need to add a new harvest call for each new repository type [Geodisy 2]
            List<SourceJavaObject> sJOs = geo.harvestDataverse();
            for(SourceJavaObject sJO : sJOs) {
                existingHarvests.addOrReplaceRecord(new DataverseRecordInfo(sJO, logger.getName()));
            }
            crosswalkRecords(sJOs);
            //sendRecordsToGeoBlacklight();


            endRecsToCheck = keepInfo();
            endErrorLog = keepMajErrors();
            endWarningLog = keepMinErrors();
            if(!startRecsToCheck.equals(endRecsToCheck)) {
                emailCheckRecords();
                fW.writeStringToFile(endRecsToCheck,RECORDS_TO_CHECK);
            }
            if(!startErrorLog.equals(endErrorLog)){
                fW.writeStringToFile(endErrorLog,ERROR_LOG);
            }
            if(!startWarningLog.equals(endWarningLog)){
                fW.writeStringToFile(endWarningLog,WARNING_LOG);
            }
            existingHarvests.saveExistingSearchs(existingHarvests.getRecordVersions(),EXISTING_RECORDS, "ExistingRecords");
            existingHarvests.saveExistingSearchs(existingHarvests.getbBoxes(),EXISTING_BBOXES, "ExistingBBoxes");


        } catch (IOException  e) {
            logger.error("Something went wrong trying to read permanent file ExistingRecords.txt!");
        } finally {
            Calendar end =  Calendar.getInstance();
            Long total = end.getTimeInMillis()-startTime;
            System.out.println("Finished a run at: " + end.getTime() + " after " + total + " milliseconds");
        }
    }



    private void sendRecordsToGeoBlacklight() {
        GeoCombine combine = new GeoCombine();
        combine.index();

    }

    private void crosswalkRecords(List<SourceJavaObject> sJOs) {
        crosswalkSJOsToGeoBlackJSON(sJOs);
        crosswalkSJOsToXML(sJOs);
    }

    private void crosswalkSJOsToGeoBlackJSON(List<SourceJavaObject> sJOs) {
        for(SourceJavaObject sjo: sJOs){
            DataverseJavaObject djo = (DataverseJavaObject) sjo;
            DataGBJSON dataGBJSON = new DataGBJSON(djo);
            dataGBJSON.createJson();
        }
    }

    /**
     * Create ISO XML files and run Geocombine on them to push them to GeoBlacklight
     * @param sJOs
     */
    private void crosswalkSJOsToXML(List<SourceJavaObject> sJOs) {
        Crosswalk crosswalk = new Crosswalk();
        crosswalk.convertSJOs(sJOs);
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
