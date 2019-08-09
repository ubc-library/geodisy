package BaseFiles;

import Crosswalking.XML.XMLDocument;
import Crosswalking.XML.XMLGenerator;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordInfo;
import Dataverse.ExistingSearches;
import Dataverse.SourceJavaObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;

import static BaseFiles.GeodisyStrings.*;

/**
 *This extends TimerTask to create the class that will
 * be used in the BaseFiles.Scheduler to start the tests, harvesting from Dataverse and
 * exporting data and ISO-19115 metadata to Geoserver.
 * @author pdante
 */
public class MyTimerTask extends TimerTask {
    GeoLogger logger = new GeoLogger(this.getClass());
    public MyTimerTask() {
    }
/**
 * 
 */
    @Override
    public void run() {
       String startRecsToCheck;
       String endRecsToCheck;
       String startErrorLog;
       String endErrorLog;
       long startTime = Calendar.getInstance().getTimeInMillis();
        try {
            startRecsToCheck = new String(Files.readAllBytes(Paths.get(RECORDS_TO_CHECK)));
            startErrorLog = new String(Files.readAllBytes(Paths.get(ERROR_LOG)));

            Geodisy geo = new Geodisy();
            FileWriter fW = new FileWriter();

            ExistingSearches existingSearches = fW.readExistingSearches(EXISTING_RECORDS);
            List<SourceJavaObject> dJOs = geo.harvestDataverse(existingSearches);
            for(SourceJavaObject dJO : dJOs) {
                existingSearches.addOrReplaceRecord(new DataverseRecordInfo(dJO, logger.getName()));

            }
            LinkedList<XMLDocument> docs = generateXML(dJOs);
            sendXMLToGeoserver(docs);
            endRecsToCheck = trimErrors();
            endErrorLog = trimInfo();
            if(!startRecsToCheck.equals(endRecsToCheck)) {
                emailCheckRecords();
                fW.writeObjectToFile(endRecsToCheck,RECORDS_TO_CHECK);
            }
            if(!startErrorLog.equals(endErrorLog)){
                fW.writeObjectToFile(endErrorLog,ERROR_LOG);
            }
            fW.writeObjectToFile(existingSearches, EXISTING_RECORDS);

        } catch (IOException  e) {
            logger.error("Something went wrong trying to read permanent file ExistingRecords.txt!");
        } finally {
            Calendar end =  Calendar.getInstance();
            Long total = end.getTimeInMillis()-startTime;
            System.out.println("Finished a run at: " + end.getTime() + " after " + total + " milliseconds");
        }
    }

    /**
     * Send all the new ISO XML files to Geoserver database
     * @param docs
     */
    //TODO actually create the XML files and send them to GeoCombine/other location
    private void sendXMLToGeoserver(LinkedList<XMLDocument> docs) {

    }

    /**
     * Create ISO XML for the new/updated records
     * @param dJOs
     */
    private LinkedList<XMLDocument> generateXML(List<SourceJavaObject> dJOs) {
        LinkedList<XMLDocument> documents = new LinkedList<>();
        for(SourceJavaObject sjo : dJOs) {
            DataverseJavaObject djo = (DataverseJavaObject) sjo;
            XMLGenerator xmlGenerator = new XMLGenerator(djo);
            documents.add(xmlGenerator.generateXMLFile());
        }
        return documents;
    }

    /**
     * Removes INFO messages from the error log.
     * @return String with no INFO messages
     * @throws IOException
     */
    private String trimInfo()throws IOException {
        String end = new String(Files.readAllBytes(Paths.get(ERROR_LOG)));
        String[] lines = end.split(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(String s: lines){
            if(s.contains("INFO"))
                continue;
            sb.append(s+System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Removes ERROR messages from the recordsToCheck log.
     * @return String with no ERROR messages
     * @throws IOException
     */
    public String trimErrors() throws IOException {
        String end = new String(Files.readAllBytes(Paths.get(RECORDS_TO_CHECK)));
        String[] lines = end.split(System.getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for(String s: lines){
            if(s.contains("ERROR"))
                continue;
            sb.append(s + System.lineSeparator());        }
        return sb.toString();
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
