package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
import Crosswalking.XML.XMLTools.JGit;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static BaseFiles.GeodisyStrings.*;

public class GeoCombine {
    GeoLogger logger;
    public GeoCombine() {
        logger = new GeoLogger(this.getClass());
    }

    public void index(){
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/bin/bash", "-c", MOVE_METADATA);

        Process p = null;
        try{
            System.out.println("Clearing Solr");
            SOLR solr = new SOLR();
            solr.clearIndex();
        } catch (SolrServerException e) {
            logger.error("Failed to clear the SOLR index for some reason");
        } catch (IOException e) {
            logger.error("IOException when trying to clear solr index");
        }
        try{
            System.out.println("Calling Geocombine");
            processBuilder.command("/bin/bash", "-c", GEOCOMBINE);
            processBuilder.directory(new File("/home/geoblack/GeoCombine"));
            p = processBuilder.start();
            p.waitFor();
            p.destroy();
        } catch (IOException | InterruptedException e) {
            logger.error("Something went wrong calling GeoCombine to index files");
        }
    }
}
