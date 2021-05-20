package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
import BaseFiles.ProcessCall;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static _Strings.GeodisyStrings.*;

public class GeoCombine {
    GeoLogger logger;
    ProcessCall processCall;

    public GeoCombine() {
        this.logger = new GeoLogger(this.getClass());
        this.processCall = new ProcessCall();
    }

    public void index(){

        moveMetadata();
        clearSolr();
        combine();
    }
    public void combine(){
        try{
            System.out.println("Calling Geocombine");
            processCall.runProcess(GEOCOMBINE,5, TimeUnit.HOURS,logger);
        } catch (IOException | InterruptedException |  ExecutionException e) {
            logger.error("Something went wrong calling GeoCombine to index files: " + e);
        } catch (TimeoutException e) {
            logger.error("Geocombine timed out!");
        }
    }

    public void combine(List<String> args, String call){
        processCall = new ProcessCall();
        try{
            System.out.println("Calling Geocombine");
            processCall.runProcess(call,5, TimeUnit.HOURS,args,logger);
        } catch (IOException | InterruptedException|ExecutionException e) {
            logger.error("Something went wrong calling GeoCombine to index files: " + e);
        } catch (TimeoutException e) {
            logger.error("Geocombine timed out!");
        }
    }

    public void moveMetadata(){
        try{
            System.out.println("Moving metadata");
            processCall.runProcess(MOVE_METADATA,2,TimeUnit.HOURS,logger);
        } catch (IOException|InterruptedException|ExecutionException e){
            logger.error("Something went wrong trying to move the metadata");
        } catch (TimeoutException e) {
            logger.error("Moving metadata timed out!");
        }
    }

    public void clearSolr(){
        try{
            System.out.println("Clearing Solr");
            SOLR solr = new SOLR();
            solr.clearIndex();
        } catch (SolrServerException e) {
            logger.error("Failed to clear the SOLR index for some reason");
        } catch (IOException e) {
            logger.error("IOException when trying to clear solr index");
        }
    }
}
