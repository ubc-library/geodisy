package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import GeoServer.Unzip;
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
    String fileIdent = "";
    int dbID = 0;
    String server = "";
    GeoLogger logger = new GeoLogger(this.getClass());
    String recordURL = "";
    String datasetIdent = "";
    GeographicBoundingBox gbb;
    int fileNumber = 0;

    /**
     * Creates a DataverseRecordFile when there is a File-specific fileIdent.
     * @param title
     * @param fileIdent
     * @param dbID
     * @param server
     * @param datasetIdent
     */
    public DataverseRecordFile(String title, String fileIdent, int dbID, String server, String datasetIdent){
        this.title = title;
        this.fileIdent = fileIdent;
        this.dbID = dbID;
        this.server = server;
        recordURL = server+"api/access/datafile/" + dbID + "?format=original";
        this.datasetIdent = datasetIdent.replace(".","_").replace("/","_");
        gbb = new GeographicBoundingBox(datasetIdent);

    }
    /**
     * Creates a DataverseRecordFile when there is no File-specific fileIdent, only a dataset fileIdent and a database ID.
     * @param title
     * @param dbID
     * @param server
     * @param datasetIdent
     */
    public DataverseRecordFile(String title, int dbID, String server, String datasetIdent){
        this.title = title;
        this.dbID = dbID;
        this.fileIdent = "";
        this.server = server;
        recordURL = server+"api/access/datafile/" + dbID + "?format=original";
        this.datasetIdent = datasetIdent.replace(".","_").replace("/","_");
        gbb = new GeographicBoundingBox(datasetIdent);


    }
    /**
      * Only to be used for temp DRFs
     */
    public DataverseRecordFile(){
        gbb = new GeographicBoundingBox("temp");
    }

    /**
     * DataverseRecordFile for Geographic coverage generated BoundingBoxes
     * @param datasetIdent
     * @param gbb
     */
    public DataverseRecordFile(String datasetIdent, GeographicBoundingBox gbb){
        this.fileIdent = datasetIdent;
        this.gbb = gbb;
        this.gbb.setField(GEOSERVER_LABEL, datasetIdent.replace(".","_").replace("/","_").replace("\\","_"));
    }

    public LinkedList<DataverseRecordFile> retrieveFile(DataverseJavaObject djo) {
        LinkedList<DataverseRecordFile> drfs = new LinkedList<>();
        try {
            String dirPath = DATASET_FILES_PATH + datasetIdent.replace("_", "/").replace(".","/") + "/";

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
                drfs = zip.unzip(filePath, dirPath, this, djo);
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
            if(!this.getTitle().endsWith("zip")&&!djo.hasDataRecord(this.getTitle()))
                drfs.add(this);
        } catch (FileNotFoundException e){
            logger.info(String.format("This dataset file %s couldn't be found from dataset %s. ", dbID, datasetIdent) + "Check out dataset " + datasetIdent, djo);
        }catch (MalformedURLException e) {
            logger.error(String.format("Something is wonky with the PERSISTENT_ID " + fileIdent + " or the dbID " + dbID));
        } catch (IOException e) {
            logger.error(String.format("Something went wrong with downloading file %s, with fileIdent %s or dbID %d", title, fileIdent, dbID));
            e.printStackTrace();
        }
        return drfs;
    }

    public void translateFile(DataverseJavaObject djo){

        String dirPath = DATASET_FILES_PATH + datasetIdent.replace("_","/") + "/";
        File f = new File(dirPath+this.getTitle());
        System.out.println(f.getName());
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GDALTranslate gdalTranslate = new GDALTranslate();
        GDAL gdal = new GDAL();
        String originalName = "";
        DataverseRecordFile dgrf = new DataverseRecordFile();
        ExistingVectorRecords evr = ExistingVectorRecords.getExistingVectors();
        if (f.isFile()) {
            String name = f.getName().toLowerCase();
            originalName = name;
            if (GeodisyStrings.ogrinfoVectorExtension(name) && !name.endsWith("csv")) {
                if (GeodisyStrings.otherShapeFilesExtensions(name))
                    return;
                name = gdalTranslate.vectorTransform(dirPath, f.getName());

                if(fileIdent.isEmpty())
                    dgrf = new DataverseRecordFile(name, this.dbID, this.server, this.datasetIdent);
                else
                    dgrf = new DataverseRecordFile(name, this.fileIdent, this.dbID, this.server, this.datasetIdent);
                f = new File(dirPath+name);
                GeographicBoundingBox gbb;
                gbb = gdal.generateBB(f,datasetIdent,String.valueOf(this.fileNumber));
                dgrf.setGbb(gbb);
                dgrf.setOriginalTitle(originalTitle);
                dgrf.setIsFromFile(true);
                dgrf.setFileURL(getFileURL());
                if(dgrf.hasValidBB()) {
                    evr.addOrReplaceRecord(dgrf.datasetIdent + name, originalName);
                }
            } else if (GeodisyStrings.gdalinfoRasterExtention(f.getName())) {
                name = gdalTranslate.rasterTransform(dirPath, f.getName());
                dgrf = new DataverseGeoRecordFile(name, this.fileIdent, this.dbID, this.server, this.datasetIdent);
                f = new File(dirPath+name);
                GeographicBoundingBox gbb;
                gbb = gdal.generateBB(f,datasetIdent,String.valueOf(this.fileNumber));
                dgrf.setGbb(gbb);
                dgrf.setOriginalTitle(originalTitle);
                dgrf.setIsFromFile(true);
                dgrf.setFileURL(getFileURL());
            } else if (name.contains(".csv")) {
                GeographicBoundingBox temp = gdal.generateBoundingBoxFromCSV(f, djo);
                if (temp.hasBB()) {
                    name = gdalTranslate.vectorTransform(dirPath, f.getName());
                    }
                    dgrf = new DataverseGeoRecordFile(name, this.fileIdent, this.dbID, this.server, this.datasetIdent);
                    dgrf.setOriginalTitle(originalTitle);
                    dgrf.setIsFromFile(true);
                    dgrf.setBB(temp.getBB());
                    dgrf.setFileURL(getFileURL());
                    if(dgrf.hasValidBB()) {
                        evr.addOrReplaceRecord(dgrf.datasetIdent + name, originalName);
                    }
                } else {
                    String path = djo.getDOI().replace("/", "_");
                    path = path.replace(".", "_");
                    String badFilesPath = DATASET_FILES_PATH + path + "/" + name;
                    File file = new File(badFilesPath);
                    file.delete();
                }
            }
        System.out.println("Valid bb from translate. DIO: " + datasetIdent + " filename: " + getTitle() + " BB (NE/SW): (" + getGBB().getNorthLatitude() + "," + getGBB().getEastLongitude() + "), (" +getGBB().getSouthLatitude() + "," + getGBB().getWestLongitude() + ")");
        return dgrf;
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

    public String getFileIdent(){return fileIdent;}
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

    public String getDatasetIdent() {
        return datasetIdent;
    }

    public void setOriginalTitle(String s){
        originalTitle = s;
    }

    public String getOriginalTitle(){
        return originalTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFileIdent(String fileIdent) {
        this.fileIdent = fileIdent;
    }

    public int getDbID() {
        return dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getRecordURL() {
        return recordURL;
    }

    public void setRecordURL(String recordURL) {
        this.recordURL = recordURL;
    }

    public void setDatasetIdent(String datasetIdent) {
        this.datasetIdent = datasetIdent;
    }

    public GeographicBoundingBox getGbb() {
        return gbb;
    }

    public void setGbb(GeographicBoundingBox gbb) {
        this.gbb = gbb;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getFileURL() {
        return gbb.getField(FILE_URL);
    }

    public void setFileURL(String fileURL) {
        gbb.setField(FILE_URL,fileURL);
    }
}
