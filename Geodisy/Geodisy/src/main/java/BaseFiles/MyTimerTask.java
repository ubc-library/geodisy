package BaseFiles;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.TimerTask;

/**
 *This extends TimerTask to create the class that will
 * be used in the BaseFiles.Scheduler to start the tests, harvesting from Dataverse and
 * exporting data and ISO-19115 metadata to Geoserver.
 * @author pdante
 */
public class MyTimerTask extends TimerTask {
    
    protected MyTimerTask() {
    }
/**
 * 
 */
    @Override
    public void run() {
        Path manualCheckPath = Paths.get("./logs/recordsToCheck");
        byte[] f1, f2;
        try {
            f1 = Files.readAllBytes(manualCheckPath);

        Geodisy geo = new Geodisy();
        geo.harvestDataverse();
        f1 = Files.readAllBytes(manualCheckPath);
        if(Arrays.equals(f1,f2))
            emailCheckRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
