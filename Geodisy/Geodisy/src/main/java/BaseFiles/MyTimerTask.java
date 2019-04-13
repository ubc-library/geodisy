package BaseFiles;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

;



import Dataverse.ExistingSearches;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.TimerTask;

import static BaseFiles.GeodisyStrings.EXISTING_RECORDS;
import static BaseFiles.GeodisyStrings.RECORDS_TO_CHECK;

/**
 *This extends TimerTask to create the class that will
 * be used in the BaseFiles.Scheduler to start the tests, harvesting from Dataverse and
 * exporting data and ISO-19115 metadata to Geoserver.
 * @author pdante
 */
public class MyTimerTask extends TimerTask {
    Logger logger = LogManager.getLogger(this.getClass());
    protected MyTimerTask() {
    }
/**
 * 
 */
    @Override
    public void run() {
        Path manualCheckPath = Paths.get(RECORDS_TO_CHECK);
        byte[] f1, f2;
        try {
            f1 = Files.readAllBytes(manualCheckPath);

        Geodisy geo = new Geodisy();
        ExistingSearchesFile eSF = new ExistingSearchesFile(EXISTING_RECORDS);

        //noinspection UnusedAssignment
        ExistingSearches existingSearches = eSF.readExistingSearches();
        geo.harvestDataverse();
        f2 = Files.readAllBytes(manualCheckPath);
        if(Arrays.equals(f1,f2))
            emailCheckRecords();
        existingSearches = ExistingSearches.getExistingSearches();
        eSF.writeExistingSearches(existingSearches);
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Something went wrong trying to read permanent file ExistingRecords.txt!");
        }
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
