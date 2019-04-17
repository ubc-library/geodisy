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
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public MyTimerTask() {
    }
/**
 * 
 */
    @Override
    public void run() {
       String start;
       String end;
        try {
            start = new String(Files.readAllBytes(Paths.get(RECORDS_TO_CHECK)));


        Geodisy geo = new Geodisy();
        ExistingSearchesFile eSF = new ExistingSearchesFile(EXISTING_RECORDS);

        //noinspection UnusedAssignment
        ExistingSearches existingSearches = eSF.readExistingSearches();
        geo.harvestDataverse();
        end = trimErrors();
        if(!start.equals(end)) {
            emailCheckRecords();
        }
        existingSearches = ExistingSearches.getExistingSearches();
        eSF.writeExistingSearches(existingSearches);
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Something went wrong trying to read permanent file ExistingRecords.txt!");
        }
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

    private boolean filesDiffer(BufferedReader reader1, BufferedReader reader2) throws IOException {
        String line1 = reader1.readLine();

        String line2 = reader2.readLine();


        boolean areDifferent = false;

        int lineNum = 1;

        while (line1 != null || line2 != null)
        {
            if(line1.contains("ERROR Dataverse"))
            if(line1 == null || line2 == null)
            {
                areDifferent = true;

                break;
            }
            else if(! line1.equalsIgnoreCase(line2))
            {
                areDifferent = true;

                break;
            }

            line1 = reader1.readLine();

            line2 = reader2.readLine();

            lineNum++;
        }
    return areDifferent;
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
