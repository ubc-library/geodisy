/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geodisy;

import java.util.TimerTask;

/**
 *
 * @author pdante
 */
public class MyTimerTask extends TimerTask {
    protected MyTimerTask() {

    }

    @Override
    public void run() {
        Geodisy geo = new Geodisy();
        geo.getMetadata();
    }
    
}
