package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import GeoServer.GeoServerAPI;
import GeoServer.Unzip;
import com.sun.applet2.preloader.event.ErrorEvent;
import org.apache.commons.io.FileUtils;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Stack;


import static BaseFiles.GeodisyStrings.*;
import static Dataverse.DVFieldNameStrings.*;


/**
 * Info for downloading a geospatial dataset file, and the methods used to download the files.
 */
public class DataverseRecordFile {
    String title;
    String originalTitle;
    String doi = "N/A";
    int dbID = 0;
    private String server = "";
    private GeoLogger logger = new GeoLogger(this.getClass());
    private String recordURL = "";
    private String datasetDOI = "";
    private GeographicBoundingBox gbb;
    private int fileNumber = 0;

    /**
     * Creates a DataverseRecordFile when there is a File-specific doi.
     * @param title
     * @param doi
     * @param dbID
     * @param server
     * @param datasetDOI
     */
    public DataverseRecordFile(String title, String doi, int dbID, String server, String datasetDOI){
        this.title = title;
        this.doi = doi;
        this.dbID = dbID;
        this.server = server;
        recordURL = server+"api/access/datafile/:persistentId/?persistentId=" + doi + "&format=original";
        this.datasetDOI = datasetDOI.replace(".","_").replace("/","_");
        gbb = new GeographicBoundingBox(doi);

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
        gbb = new GeographicBoundingBox(doi);


    }
    /**
      * Only to be used for temp DRFs
     */
    public DataverseRecordFile(){
        gbb = new GeographicBoundingBox("temp");
    }

    /**
     * DataverseRecordFile for Geographic coverage generated BoundingBoxes
     * @param doi
     * @param gbb
     */
    public DataverseRecordFile(String doi,GeographicBoundingBox gbb){
        this.doi = doi;
        this.gbb = gbb;
        this.gbb.setField(GEOSERVER_LABEL,doi.replace(".","_").replace("/","_").replace("\\","_"));
    }

    public DataverseRecordFile retrieveFile(DataverseJavaObject djo) {
        try {
            String dirPath = DATASET_FILES_PATH + datasetDOI.replace("_", "/").replace(".","/") + "/";
            File folder = new File(dirPath);
            folder.mkdirs();
            String filePath = dirPath + title;
            FileUtils.copyURLToFile(
                    new URL(recordURL),
                    new File(filePath),
                    10000, //10 seconds connection timeout
                    120000); //2 minute read timeout
            if (title.endsWith(".zip")) {
                Unzip zip = new Unzip();
                zip.unzip(filePath, dirPath, this);
                new File(filePath).delete();
            }

            //Unzip any zip files and convert .tab to .csv
            File[] listOfFiles = folder.listFiles();
            for (File f : listOfFiles) {
                if (f.isFile()) {
                    String name = f.getName();
                    if (name.endsWith(".tab"))
                        convertFromTabToCSV(f, dirPath, name);
                }
            }
        } catch (FileNotFoundException e){
            logger.info(String.format("This dataset file %s couldn't be found from dataset %s. ", dbID, doi) + "Check out dataset " + datasetDOI, djo);
        }catch (MalformedURLException e) {
            logger.error(String.format("Something is wonky with the PERSISTENT_ID " + doi + " or the dbID " + dbID));
        } catch (IOException e) {
            logger.error(String.format("Something went wrong with downloading file %s, with doi %s or dbID %d", title, doi, dbID));
            e.printStackTrace();
        }finally {
            return this;
        }
    }

    public void translateFile(DataverseJavaObject djo){

        String dirPath = DATASET_FILES_PATH + datasetDOI.replace("_","/") + "/";
        File f = new File(dirPath+this.getTitle());
        GDALTranslate gdalTranslate = new GDALTranslate();
        GDAL gdal = new GDAL();
        String originalName = "";
        ExistingVectorRecords evr = ExistingVectorRecords.getExistingVectors();
        if (f.isFile()) {
            String name = f.getName().toLowerCase();
            originalName = name;
            DataverseRecordFile drf;
            if (GeodisyStrings.ogrinfoVectorExtension(name) && !name.endsWith("csv")) {
                if (!name.endsWith(".shp")) {
                    if (GeodisyStrings.otherShapeFilesExtensions(name))
                        return;
                    name = gdalTranslate.vectorTransform(dirPath, f.getName(), djo);
                }
                drf = new DataverseRecordFile(name, this.doi, this.dbID, this.server, this.datasetDOI);
                drf.setOriginalTitle(originalTitle);
                drf.setIsFromFile(true);
                djo.addGeoDataFile(drf);
                evr.addOrReplaceRecord(drf.doi+name,originalName);
            } else if (GeodisyStrings.gdalinfoRasterExtention(f.getName())) {
                if (!name.endsWith(".tif"))
                    name = gdalTranslate.rasterTransform(dirPath, f.getName(), djo);
                drf = new DataverseRecordFile(name, this.doi, this.dbID, this.server, this.datasetDOI);
                drf.setOriginalTitle(originalTitle);
                drf.setIsFromFile(true);
                djo.addGeoDataFile(drf);
            } else if (name.contains(".csv")) {
                GeographicBoundingBox temp = gdal.generateBoundingBoxFromCSV(f, djo);
                if (temp.hasBB()) {
                    if (!name.endsWith(".shp")) {
                        if (name.endsWith(".shx") || name.endsWith(".dbf") || name.endsWith(".prj"))
                            throw new IllegalArgumentException();

                        name = gdalTranslate.vectorTransform(dirPath, f.getName(), djo);
                    }
                    drf = new DataverseRecordFile(name, this.doi, this.dbID, this.server, this.datasetDOI);
                    drf.setOriginalTitle(originalTitle);
                    drf.setIsFromFile(true);
                    djo.addGeoDataFile(drf);
                    evr.addOrReplaceRecord(drf.doi+name,originalName);
                } else {
                    String path = djo.getDOI().replace("/", "_");
                    path = path.replace(".", "_");
                    String badFilesPath = DATASET_FILES_PATH + path + "/" + name;
                    File file = new File(badFilesPath);
                    file.delete();
                }
            }
        }
    }


    private void convertFromTabToCSV(File inputFile, String dirPath, String title) {
        String fileName = title.substring(0, title.length() - 3) + "csv";
        File outputFile = new File(dirPath + fileName);
        BufferedReader br = null;
        FileWriter writer = null;
        try {
            String line;
            Stack<String> stack = new Stack<>();
            br = new BufferedReader(new FileReader(inputFile));
            writer = (new FileWriter(outputFile));
            while ((line = br.readLine()) != null)
                stack.push(line.replace("\t", ","));
            while (!stack.isEmpty()) {
                writer.write(stack.pop());
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
            }
            catch(IOException|NullPointerException d){
                logger.error("Something went wrong when converting a .tab file to .csv when closing br: " + title);
            }
            finally{
                try{
                    writer.close();
                } catch (IOException e) {
                    logger.error("Something went wrong when converting a .tab file to .csv when closing writer: " + title);
                }
            }
        }

    }
    public String getTitle(){return title; }
    //getUUID is also in ISOXMLGen, so change there if changed here

    public String getDoi(){return doi;}
    public GeographicBoundingBox getGBB(){return gbb;}
    public BoundingBox getBB(){return gbb.getBB();}
    public boolean hasValidBB(){return gbb.hasBB();}
    public void setBB(BoundingBox boundingBox){
        gbb.setBB(boundingBox);}

    public boolean isPreviewable() {
        return GeodisyStrings.isPreviewable(title);
    }

    public String getProjection(){
        return gbb.getField(PROJECTION);
    }

    public void setProjection(String s){
        gbb.setField(PROJECTION,s);
    }

    public void addFileNumber(int i){
        fileNumber = i;
    }

    public String getFileNumber(){
        if(fileNumber==0)
            return "";
        else
            return String.valueOf(fileNumber);
    }

    public void setGeoserverLabel(String s){
        gbb.setField(GEOSERVER_LABEL,s);
    }

    public String getGeoserverLabel(){
        return gbb.getField(GEOSERVER_LABEL);
    }

    public String getGeometryType() {
        return gbb.getField(GEOMETRY);
    }

    public void setGeometryType(String geometryType) {
        gbb.setField(GEOMETRY,geometryType);
    }

    public String getFileName(){
        return gbb.getField(FILE_NAME);
    }

    public boolean isFromFile(){
        return gbb.isGeneratedFromGeoFile();
    }

    public void setIsFromFile(boolean fromFile){
        gbb.setIsGeneratedFromGeoFile(fromFile);
    }

    public String getDatasetDOI() {
        return datasetDOI;
    }

    public void setOriginalTitle(String s){
        originalTitle = s;
    }

    public String getOriginalTitle(){
        return originalTitle;
    }
}
