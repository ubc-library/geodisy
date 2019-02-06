/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.geodisy;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.TimerTask;

/**
 *This extends TimerTask to create the class that will
 * be used in the Scheduler to start the Geodisy, harvesting from Dataverse and 
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
        Geodisy geo = new Geodisy();
        geo.harvestDataverse();
    }
    
    //for testing the scheduler
    /*@Override
    public void run(){
        TimeZone tz = TimeZone.getTimeZone("America/Vancouver");
        Calendar today = Calendar.getInstance(tz);
        System.out.println("Current time: " + today.getTime());
    }*/
  
    
}
