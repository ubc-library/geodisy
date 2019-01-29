/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geodisy;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.TimerTask;

/**
 *
 * @author pdante
 */
public class MyTimerTask extends TimerTask {
    private static int i;
    protected MyTimerTask() {
        i = 0;
    }

    @Override
    public void run() {
        Geodisy geo = new Geodisy();
        geo.getMetadata();
    }
    
    //for testing the scheduler
    /*@Override
    public void run(){
        TimeZone tz = TimeZone.getTimeZone("America/Vancouver");
        Calendar today = Calendar.getInstance(tz);
        System.out.println("Current time: " + today.getTime());
        i++;
    }*/
  
    
}
