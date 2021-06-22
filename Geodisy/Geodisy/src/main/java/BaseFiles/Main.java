package BaseFiles;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Crosswalking.GeoBlacklightJson.GeoCombine;
import TestFiles.Tests;
import _Strings.GeodisyStrings;

import java.util.ArrayList;
import java.util.List;

import static _Strings.GeodisyStrings.*;

/**
 *
 * @author pdante
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Run the below to remove a single records
        //RemoveRecord rr = new RemoveRecord();
        //rr.removeRecord();
        String dev;
        if(args.length>0)
            TEST = true;
        else
            TEST = false;
        if(TEST)
            dev = "Using the dev servers, is this correct?";
        else
            dev = "Using the prod servers, is this correct?";
        GeodisyStrings.load();
        GeodisyTask geodisyTask = new GeodisyTask();
        geodisyTask.run();

        /*Tests tests = new Tests();
        tests.runTests();*/

    }
}
