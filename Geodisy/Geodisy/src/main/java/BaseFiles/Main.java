package BaseFiles;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Crosswalking.GeoBlacklightJson.SOLR;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;

import static BaseFiles.GeodisyStrings.TEST;

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
        if(TEST)
            dev = "Using the dev servers, is this correct?";
        else
            dev = "Using the prod severs, is this correct?";
        System.out.println(dev);
        GeodisyTask geodisyTask = new GeodisyTask();
        geodisyTask.run();

        //Run the below solo to download
        /*DownloadRecord downloadRecord = new DownloadRecord();
        downloadRecord.run("doi:10.5072/FK2/KZRG9F");*/


    }
    
}
