package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import GeoServer.FolderFileParser;
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
    String translatedTitle = "";
    String originalTitle = "";
    String fileIdent = "";
    int dbID = 0;
    String server = "";
    GeoLogger logger = new GeoLogger(this.getClass());
    String recordURL = "";
    String datasetIdent = "";
    GeographicBoundingBox gbb;

    /**
     * Creates a DataverseRecordFile when there is a File-specific fileIdent.
     * @param translatedTitle
     * @param fileIdent
     * @param dbID
     * @param server
     * @param datasetIdent
     */
    public DataverseRecordFile(String translatedTitle, String fileIdent, int dbID, String server, String datasetIdent){
        this.translatedTitle = translatedTitle;
        this.fileIdent = fileIdent;
        this.dbID = dbID;
        this.server = server;
        recordURL = server+"api/access/datafile/" + dbID + "?format=original";
        this.datasetIdent = datasetIdent.replace(".","_").replace("/","_");
        gbb = new GeographicBoundingBox(datasetIdent);

    }
    /**
     * Creates a DataverseRecordFile when there is no File-specific fileIdent, only a dataset fileIdent and a database ID.
     * @param translatedTitle
     * @param dbID
     * @param server
     * @param datasetIdent
     */
    public DataverseRecordFile(String translatedTitle, int dbID, String server, String datasetIdent){
        this.translatedTitle = translatedTitle;
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

    public DataverseRecordFile(DataverseRecordFile drf){
        this.originalTitle = drf.originalTitle;
        this.translatedTitle = drf.translatedTitle;
        this.fileIdent = drf.getFileIdent();
        this.datasetIdent = drf.getDatasetIdent();
        this.dbID = drf.getDbID();
        this.server = drf.getServer();
        this.recordURL = drf.getRecordURL();
        this.gbb = drf.getGBB();
    }

    public LinkedList<DataverseRecordFile> retrieveFile(DataverseJavaObject djo) {
        FolderFileParser ffp = new FolderFileParser();
        System.out.println("downloading file: " + originalTitle);
        LinkedList<DataverseRecordFile> drfs = new LinkedList<>();
        DownloadedFiles downloads = DownloadedFiles.getDownloadedFiles();
        downloads.addDownload(originalTitle,djo.getDOI(),dbID);
        try {
            String dirPath = GEODISY_PATH_ROOT+GeodisyStrings.replaceSlashes(DATASET_FILES_PATH + datasetIdent.replace("_", "/").replace(".","/") + "/");

            File folder = new File(dirPath);
            folder.mkdirs();
            String filePath = dirPath + translatedTitle;
            FileUtils.copyURLToFile(
                    new URL(recordURL),
                    new File(filePath),
                    10000, //10 seconds connection timeout
                    120000); //2 minute read timeout
            File newFile = new File(filePath);
            if (translatedTitle.toLowerCase().endsWith(".zip")) {
                Unzip zip = new Unzip();
                try {
                    System.out.println("Unzipping file");
                    drfs = ffp.unzip(newFile, dirPath, this, djo);
                }catch (NullPointerException f){
                        logger.error("Got an null pointer exception, something clearly went wrong with unzipping " + filePath);
                    }
                new File(filePath).delete();
            }else if(newFile.isDirectory())
                drfs = ffp.openFolders(newFile, dirPath,djo,this);
            File[] listOfFiles = folder.listFiles();
            for (File f : listOfFiles) {
                if (f.isFile()) {
                    String name = f.getName();
                    if (name.endsWith(".tab")) {
                        System.out.println("Converting tab file");
                        drfs.add(ffp.convertTab(f, dirPath, name, this));
                        f.delete();
                    }else if(name.endsWith(".zip"))
                        drfs.addAll(ffp.unzip(f,dirPath,this,djo));
                }else
                    drfs.addAll(ffp.openFolders(f,dirPath,djo,this));
            }
            if(!this.getOriginalTitle().endsWith("zip")&&!djo.hasDataRecord(this.getOriginalTitle()))
                drfs.add(this);
        } catch (FileNotFoundException e){
            logger.info(String.format("This dataset file %s couldn't be found from dataset %s. ", dbID, datasetIdent) + "Check out dataset " + datasetIdent, djo);
        }catch (MalformedURLException e) {
            logger.error(String.format("Something is wonky with the file PERSISTENT_ID " + fileIdent + " or the dbID " + dbID + " of Dataset with PID "+ datasetIdent));
        } catch (IOException e) {
            logger.error(String.format("Something went wrong with downloading file %s, with fileIdent %s or dbID %d of Dataset with PID " + datasetIdent, translatedTitle, fileIdent, dbID));
            e.printStackTrace();
        }
        return drfs;
    }

    public DataverseRecordFile translateFile(DataverseJavaObject djo) {

        //System.out.println("Made it to Translate File");
        String dirPath = GEODISY_PATH_ROOT + DATASET_FILES_PATH + GeodisyStrings.replaceSlashes(datasetIdent.replace("_", "/") + "/");
        //System.out.println(dirPath);
        File f = new File(dirPath + this.getTranslatedTitle());
        GDALTranslate gdalTranslate = new GDALTranslate();
        GDAL gdal = new GDAL();
        if (f.isFile()) {
            String name = f.getName().toLowerCase();
            if (GeodisyStrings.ogrinfoVectorExtension(f.getName()) && !name.endsWith("csv")) {
                if (GeodisyStrings.otherShapeFilesExtensions(name))
                    return new DataverseRecordFile();
                if(originalTitle.isEmpty())
                    setOriginalTitle(f.getName());
                setTranslatedTitle(gdalTranslate.vectorTransform(dirPath, f.getName()));
                return replaceRecord();
            } else if (GeodisyStrings.gdalinfoRasterExtention(f.getName())) {
                if(originalTitle.isEmpty())
                    setOriginalTitle(name);
                setTranslatedTitle(gdalTranslate.rasterTransform(dirPath, f.getName()));
                return replaceRecord();
            } else if (name.contains(".csv")) {
                GeographicBoundingBox temp = gdal.generateBoundingBoxFromCSV( f.getName(), djo);
                if (temp.hasBB()) {
                    setOriginalTitle(name);
                    setTranslatedTitle(gdalTranslate.vectorTransform(dirPath, f.getName()));
                    replaceRecord();
                }
            } else {
                String path = djo.getDOI().replace("/", "_");
                path = path.replace(".", "_");
                String badFilesPath = GeodisyStrings.replaceSlashes(GEODISY_PATH_ROOT + DATASET_FILES_PATH + path + "/" + name);
                File file = new File(badFilesPath);
                file.delete();
                return new DataverseRecordFile();
            }
        }
        return new DataverseRecordFile();
    }

    private DataverseRecordFile replaceRecord() {
        DataverseRecordFile newDRF = new DataverseRecordFile(translatedTitle,fileIdent,dbID,server,datasetIdent);
        newDRF.setOriginalTitle(originalTitle);
        newDRF.setRecordURL(recordURL);
        return newDRF;
    }


    private String convertFromTabToCSV(File inputFile, String dirPath, String title) {
        String fileName = title.substring(0, title.length() - 3) + "csv";
        File outputFile = new File(dirPath + fileName);
        BufferedReader br = null;
        FileWriter writer = null;
        try {
            String line;
            Stack<String> stack = new Stack<>();
            br = new BufferedReader(new FileReader(inputFile));
            writer = (new FileWriter(outputFile));
            while ((line = br.readLine()) != null) {
                stack.push(line.replace("\t", ","));
            }
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
        }catch(OutOfMemoryError e){
            logger.warn("Tab was too big to convert to csv, maybe check? Path = " +dirPath + " And title = " + title);
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
    return fileName;
    }
    public String getTranslatedTitle(){return translatedTitle; }
    //getUUID is also in ISOXMLGen, so change there if changed here

    public String getFileIdent(){return fileIdent;}
    public GeographicBoundingBox getGBB(){return gbb;}
    public BoundingBox getBB(){return gbb.getBB();}
    public boolean hasValidBB(){return gbb.hasBB();}
    public void setBB(BoundingBox boundingBox){
        gbb.setBB(boundingBox);}

    public boolean isPreviewable() {
        return GeodisyStrings.isPreviewable(translatedTitle);
    }

    public String getProjection(){
        return gbb.getField(PROJECTION);
    }

    public void setProjection(String s){
        gbb.setField(PROJECTION,s);
    }

    public String getGBBFileNumber(){
        return gbb.getFileNumber();
    }

    public void setGeoserverLabel(String s){
        gbb.setField(BASE_GEOSERVER_LABEL,s);
    }

    public String getGeoserverLabel(){
        return gbb.getField(GEOSERVER_LABEL);
    }

    public String getBaseGeoserverLabel(){
        return gbb.getField(BASE_GEOSERVER_LABEL);
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

    public void setTranslatedTitle(String title) {
        this.translatedTitle = title;
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


    public void setGbb(GeographicBoundingBox gbb) {
        this.gbb = gbb;
    }

    public void setFileNumber(int fileNumber) {
        gbb.setFileNumber(fileNumber);
    }

    public String getFileURL() {
        return gbb.getField(FILE_URL);
    }

    public void setFileURL(String fileURL) {
        gbb.setField(FILE_URL,fileURL);
    }
}
