package BaseFiles;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * This class is what schedules the metadata harvest to occur at a specific interval.
 * Currently the plan is to have it run once a day at 2am PST.
 * 
 * @author pdante
 * 

 */
public class Scheduler {
    
    /**
     * Class sets the schedule for how often Dataverse is harvested 
     * and how often Geoserver is updated.
     
     * HOUR_TO_START_RUN is a 24-hour representation of hour when program will run. 
     * Time is based on the time zone of the program's location (PST)
     * 
     */
    
    private final static int HOUR_TO_START_RUN = 2;
    private final static int MINUTE_TO_START_RUN = 0;
    private final static TimeUnit UNIT_BETWEEN_RUNS = TimeUnit.DAYS;
    private long testDelay = 0;
    private long testPeriod = 200000;
    
    //for testing the timer
    //private final static TimeUnit UNIT_BETWEEN_RUNS = TimeUnit.SECONDS;
    
    /**
     * Used to set the time zone for the Calendar object.
     */
    TimeZone tz = TimeZone.getTimeZone("America/Vancouver");
    
    protected Calendar today;
    public Scheduler() {
        today = Calendar.getInstance(tz);
        today.set(Calendar.HOUR_OF_DAY, HOUR_TO_START_RUN);
        today.set(Calendar.MINUTE, MINUTE_TO_START_RUN);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND,0);
    }

    public Calendar getToday() {
        return today;
    }

public void run(){
// every night at 2am you run your task
Timer timer = new Timer();

//TODO uncomment the following to get the timer actually running, but for now I want the task to run when I start the program so I can test
    new MyTimerTask().run(); //runs Geodisy when the program first get's spun up to get all the existing records
    //timer.scheduleAtFixedRate(new MyTimerTask(), today.getTime(), TimeUnit.MILLISECONDS.convert(1, UNIT_BETWEEN_RUNS)); // period: 1 day
    //timer.scheduleAtFixedRate(new MyTimerTask(),testDelay,testPeriod);
    //new MyTimerTask().run();
    }
    
    




}
