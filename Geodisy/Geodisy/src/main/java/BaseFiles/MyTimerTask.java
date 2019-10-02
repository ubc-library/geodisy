package BaseFiles;

import Crosswalking.Crosswalk;
import Dataverse.DataverseRecordInfo;
import Dataverse.ExistingSearches;
import Dataverse.FindingBoundingBoxes.Countries;
import Dataverse.SourceJavaObject;

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
       long startTime = Calendar.getInstance().getTimeInMillis();
        Countries.getCountry();
        try {
            startRecsToCheck = new String(Files.readAllBytes(Paths.get(RECORDS_TO_CHECK)));
            startErrorLog = new String(Files.readAllBytes(Paths.get(ERROR_LOG)));

            Geodisy geo = new Geodisy();
            FileWriter fW = new FileWriter();

            ExistingSearches existingSearches = ExistingSearches.readExistingSearches();
            List<SourceJavaObject> sJOs = geo.harvestDataverse(existingSearches);
            for(SourceJavaObject sJO : sJOs) {
                existingSearches.addOrReplaceRecord(new DataverseRecordInfo(sJO, logger.getName()));
            }

            crosswalkSJOsToXML(sJOs);

            endRecsToCheck = trimErrors();
            endErrorLog = trimInfo();
            if(!startRecsToCheck.equals(endRecsToCheck)) {
                emailCheckRecords();
                fW.writeStringToFile(endRecsToCheck,RECORDS_TO_CHECK);
            }
            if(!startErrorLog.equals(endErrorLog)){
                fW.writeStringToFile(endErrorLog,ERROR_LOG);
            }
            existingSearches.saveExistingSearchs();

        } catch (IOException  e) {
            logger.error("Something went wrong trying to read permanent file ExistingRecords.txt!");
        } finally {
            Calendar end =  Calendar.getInstance();
            Long total = end.getTimeInMillis()-startTime;
            System.out.println("Finished a run at: " + end.getTime() + " after " + total + " milliseconds");
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
    private String trimInfo()throws IOException {
        String end = new String(Files.readAllBytes(Paths.get(ERROR_LOG)));
        String[] lines = end.split(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(String s: lines){
            if(s.contains("INFO"))
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
    public String trimErrors() throws IOException {
        String end = new String(Files.readAllBytes(Paths.get(RECORDS_TO_CHECK)));
        String[] lines = end.split(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(String s: lines){
            if(s.contains("ERROR"))
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
