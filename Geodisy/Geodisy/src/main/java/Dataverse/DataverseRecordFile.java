package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import GeoServer.Unzip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;
import java.util.UUID;


/**
 * Info for downloading a geospatial dataset file, and the methods used to download the files.
 */
public class DataverseRecordFile {
    String title;
    String doi = "N/A";
    int dbID;
    String server;
    GeoLogger logger = new GeoLogger(this.getClass());
    String recordURL;
    String datasetDOI;
    DataverseJavaObject djo;
    BoundingBox bb;
    public boolean isGeospatial;

    /**
     * Creates a DataverseRecordFile when there is a File-specific doi.
     * @param title
     * @param doi
     * @param dbID
     * @param server
     * @param datasetDOI
     */
    public DataverseRecordFile(String title, String doi, int dbID, String server, String datasetDOI, DataverseJavaObject djo){
        this.djo = djo;
        this.title = title;
        this.doi = doi;
        this.dbID = dbID;
        this.server = server;
        recordURL = server+"api/access/datafile/:persistentId/?persistentId=" + doi + "&format=original";
        this.datasetDOI = datasetDOI.replaceAll("\\.","_").replaceAll("/","_");
        bb = new BoundingBox();
    }

    /**
     * Creates a DataverseRecordFile when there is no File-specific doi, only a dataset doi and a database ID.
     * @param title
     * @param dbID
     * @param server
     * @param datasetDOI
     */
    public DataverseRecordFile(String title, int dbID, String server, String datasetDOI){
        this.title = title;
        this.dbID = dbID;
        this.doi = String.valueOf(dbID);
        this.server = server;
        recordURL = String.format(server+"api/access/datafile/$d?format=original", dbID);
        this.datasetDOI = datasetDOI;
        bb = new BoundingBox();

    }

    public void getFile() {
        try {
            String dirPath = "./datasetFiles/" + datasetDOI + "/";
            File folder = new File(dirPath);
            folder.mkdirs();
            String filePath = dirPath + title;
            FileUtils.copyURLToFile(
                    new URL(recordURL),
                    new File(filePath),
                    10000, //10 seconds connection timeout
                    120000); //2 minute read timeout
            if(title.endsWith(".zip")) {
                Unzip zip = new Unzip();
                zip.unzip(filePath, this);
                new File(filePath).delete();
            }

            //Unzip any zip files
            File[] listOfFiles = folder.listFiles();
            for(File f: listOfFiles){
                if(f.isFile()) {
                    String name = f.getName();
                    if (name.endsWith(".tab"))
                        convertFromTabToCSV(f, dirPath,name);
                }
            }

            listOfFiles = folder.listFiles();
            for(File f: listOfFiles) {
                if (f.isFile()) {
                    String name = f.getName();
                    DataverseRecordFile drf;
                    if (GeodisyStrings.ogrinfoVectorExtension(name) || GeodisyStrings.gdalinfoRasterExtention(name)) {
                        drf = new DataverseRecordFile(name, this.doi, this.dbID, this.server, this.datasetDOI, this.djo);
                        djo.addGeoDataFile(drf);
                    }else if(name.contains(".csv")){
                        GDAL gdal = new GDAL();
                        BoundingBox temp = gdal.generateBoundingBoxFromCSV(f,djo);
                        if(temp.hasBoundingBox()) {
                            drf = new DataverseRecordFile(name, this.doi, this.dbID, this.server, this.datasetDOI, this.djo);
                            drf.bb = temp;
                            djo.addGeoDataFile(drf);
                        }
                    }

                }
            }
        } catch (FileNotFoundException e){
            logger.info(String.format("This dataset file %s couldn't be found from dataset %s. ", dbID, doi) + "Check out dataset " + datasetDOI, djo, logger.getName());
        }catch (MalformedURLException e) {
            logger.error(String.format("Something is wonky with the PERSISTENT_ID " + doi + " or the dbID " + dbID));
        } catch (IOException e) {
            logger.error(String.format("Something went wrong with downloading file %s, with doi %s or dbID %d", title, doi, dbID));
            e.printStackTrace();
        }

    }

    public void convertFromTabToCSV(File inputFile, String dirPath, String title) {
        String fileName = title.substring(0, title.length() - 3) + "csv";
        File outputFile = new File(dirPath + fileName);
        BufferedReader br = null;
        FileWriter writer = null;
        try {
            String line;
            Stack stack = new Stack();
            br = new BufferedReader(new FileReader(inputFile));
            writer = (new FileWriter(outputFile));
            while ((line = br.readLine()) != null)
                stack.push(line.replace("\t", ","));
            while (!stack.isEmpty()) {
                writer.write((String) stack.pop());
                if (!stack.empty())
                    writer.write("\n");
            }
            inputFile.delete();
        } catch (FileNotFoundException e) {
            logger.error("Tried to convert an non-existant .tab file: " + title);
        } catch (IOException e) {
            logger.error("Something went wrong when converting a .tab file to .csv: " + title);
        }
        finally {
            try{
                br.close();
                writer.close();
            }
            catch(IOException d){
                logger.error("Something went wrong when converting a .tab file to .csv when closing br or writer: " + title);
            }
        }

    }
    public String getFileIdentifier(){
    return doi;
}
    public String getTitle(){return title; }
    //getUUID is also in ISOXMLGen, so change there if changed here
    public static String getUUID(String name) throws UnsupportedEncodingException {

        byte[] bytes = name.getBytes("UTF-8");
        return UUID.nameUUIDFromBytes(bytes).toString();
    }
    public String getDoi(){return doi;}
    public BoundingBox getBb(){return bb;}

    public boolean isPreviewable() {
        return GeodisyStrings.isPreviewable(title);
    }
}
