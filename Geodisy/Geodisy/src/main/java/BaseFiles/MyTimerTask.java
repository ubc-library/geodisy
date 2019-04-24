package BaseFiles;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

;


import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordInfo;
import Dataverse.ExistingSearches;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.TimerTask;

import static BaseFiles.GeodisyStrings.*;

/**
 *This extends TimerTask to create the class that will
 * be used in the BaseFiles.Scheduler to start the tests, harvesting from Dataverse and
 * exporting data and ISO-19115 metadata to Geoserver.
 * @author pdante
 */
public class MyTimerTask extends TimerTask {
    Logger logger = LogManager.getLogger(this.getClass());
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
        try {
            startRecsToCheck = new String(Files.readAllBytes(Paths.get(RECORDS_TO_CHECK)));
            startErrorLog = new String(Files.readAllBytes(Paths.get(ERROR_LOG)));

            Geodisy geo = new Geodisy();
            FileWriter fW = new FileWriter();

            ExistingSearches existingSearches = fW.readExistingSearches(EXISTING_RECORDS);
            List<DataverseJavaObject> dJOs= geo.harvestDataverse(existingSearches);
            for(DataverseJavaObject dJO : dJOs) {
                existingSearches.addOrReplaceRecord(new DataverseRecordInfo(dJO));
            }

            endRecsToCheck = trimErrors();
            endErrorLog = trimInfo();
            if(!startRecsToCheck.equals(endRecsToCheck)) {
                emailCheckRecords();
                fW.writeObjectToFile(endRecsToCheck,RECORDS_TO_CHECK);
            }
            if(!startErrorLog.equals(endErrorLog)){
                fW.writeObjectToFile(endErrorLog,ERROR_LOG);
            }
            fW.writeExistingSearches(existingSearches, EXISTING_RECORDS);

        } catch (IOException | ClassNotFoundException e) {
            logger.error("Something went wrong trying to read permanent file ExistingRecords.txt!");
        }
    }

    private String trimInfo()throws IOException {
        String end = new String(Files.readAllBytes(Paths.get(ERROR_LOG)));
        String[] lines = end.split(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(String s: lines){
            if(s.contains("INFO"))
                continue;
            sb.append(s);        }
        return sb.toString();
    }

    public String trimErrors() throws IOException {
        String end = new String(Files.readAllBytes(Paths.get(RECORDS_TO_CHECK)));
        String[] lines = end.split(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(String s: lines){
            if(s.contains("ERROR"))
                continue;
            sb.append(s);        }
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
