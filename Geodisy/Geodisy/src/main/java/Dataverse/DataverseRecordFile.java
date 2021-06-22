package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.HTTPGetCall;
import BaseFiles.ProcessCallFileDownload;
import _Strings.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import GeoServer.FolderFileParser;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import static _Strings.GeodisyStrings.*;
import static _Strings.DVFieldNameStrings.*;


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
    int bbCount = 0;

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
        recordURL = server+"api/access/datafile/" + dbID;
        this.datasetIdent = GeodisyStrings.removeHTTPSAndReplaceAuthority(datasetIdent).replace(".","_").replace("/","_");
        gbb = new GeographicBoundingBox(datasetIdent);
        setFileName(translatedTitle);

    }

    public DataverseRecordFile(String translatedTitle, String fileIdent, int dbID, String server, String datasetIdent, String fileURL){
        this.translatedTitle = translatedTitle;
        this.fileIdent = fileIdent;
        this.dbID = dbID;
        this.server = server;
        recordURL = fileURL;
        this.datasetIdent = GeodisyStrings.removeHTTPSAndReplaceAuthority(datasetIdent.replace(".","_").replace("/","_"));
        gbb = new GeographicBoundingBox(datasetIdent);
        setFileURL(fileURL);
        setFileName(translatedTitle);
        this.originalTitle=translatedTitle;

    }
    /**
     * Creates a DataverseRecordFile when there is no File-specific fileIdent, only a dataset fileIdent and a database ID.
     * @param translatedTitle
     * @param datasetIdent
     * @param dbID
     */
    public DataverseRecordFile(String translatedTitle, String datasetIdent, int dbID){
        this.translatedTitle = translatedTitle;
        this.dbID = dbID;
        this.fileIdent = "";
        this.server = server;
        recordURL = server+"api/access/datafile/" + dbID;
        this.datasetIdent = GeodisyStrings.removeHTTPSAndReplaceAuthority(GeodisyStrings.replaceSlashes(datasetIdent)).replace(".","_").replace(GeodisyStrings.replaceSlashes("/"),"_");
        gbb = new GeographicBoundingBox(datasetIdent);
        setFileName(translatedTitle);
        originalTitle = translatedTitle;
    }

    /**
     * Creates a DataverseRecord file from the FRDR-generated json
     * @param translatedTitle
     * @param datasetIdent
     * @param fileURL
     */
    public DataverseRecordFile(String translatedTitle, String datasetIdent, String fileURL){
        this.translatedTitle = translatedTitle;
        this.dbID = 0;
        this.fileIdent = "";
        this.server = "N/A";
        recordURL = fileURL;
        this.datasetIdent = GeodisyStrings.removeHTTPSAndReplaceAuthority(GeodisyStrings.replaceSlashes(datasetIdent)).replace(".","_").replace(GeodisyStrings.replaceSlashes("/"),"_");
        gbb = new GeographicBoundingBox(datasetIdent);
        this.originalTitle = translatedTitle;
        setFileName(translatedTitle);
        setFileURL(fileURL);
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
        String dI =  GeodisyStrings.removeHTTPSAndReplaceAuthority(datasetIdent);
        this.fileIdent = dI;
        this.gbb = gbb;
        this.gbb.setField(GEOSERVER_LABEL, dI.replace(".","_").replace("/","_").replace("\\","_"));
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
        LinkedList<DataverseRecordFile> drfs = new LinkedList<>();
        DownloadedFiles downloads = DownloadedFiles.getDownloadedFiles();
        downloads.addDownload(originalTitle,djo.getPID(),recordURL);
        String dirPath = GeodisyStrings.replaceSlashes(DATA_DIR_LOC + GeodisyStrings.removeHTTPSAndReplaceAuthority(datasetIdent).replace("_", "/").replace(".","/")+"/");

        ProcessCallFileDownload process = new ProcessCallFileDownload();
        String fileName = getFileName();
        String url = getFileURL();
        String path = dirPath;
        try{
            try {
                process.downloadFile(getFileURL(),getFileName(),dirPath,logger,20, TimeUnit.MINUTES);
            } catch (TimeoutException e) {
                logger.error("Download Timedout for " + fileName + " at url " + url);
                Files.deleteIfExists(Paths.get(path+fileName));
            } catch (InterruptedException e) {
                logger.error("Download Error for " + fileName + " at url " + url);
                Files.deleteIfExists(Paths.get(path+fileName));
            }
        }catch (IOException e) {
            logger.error("Delete failed download failed for " + fileName + " from " + url);
        }


        String filePath = dirPath + translatedTitle;
        File newFile = new File(filePath);
        if(!newFile.exists())
            return drfs;
        else if (translatedTitle.toLowerCase().endsWith(".zip")) {
            try {
                drfs = ffp.unzip(newFile, dirPath, this);
            }catch (NullPointerException f){
                logger.error("Got an null pointer exception, something clearly went wrong with unzipping " + filePath);
            }
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                logger.error("Something went wrong trying to delete file: " + filePath);
            }
        }else if(newFile.isDirectory())
            drfs = ffp.openFolders(newFile, dirPath,djo,this);
        else if (newFile.getName().endsWith(".tab")) {
            //System.out.println("Converting tab file");
            drfs.add(ffp.convertTab(newFile, dirPath, newFile.getName(), this));
        }
        else if(GeodisyStrings.fileToAllow(newFile.getName())) {
            drfs.add(this);
        }
        return drfs;
    }

    public DataverseRecordFile translateFile(DataverseJavaObject djo) {

        //System.out.println("Made it to Translate File");
        String dirPath = GeodisyStrings.replaceSlashes(DATA_DIR_LOC + datasetIdent.replace("_", "/") + "/");
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
                    return replaceRecord();
                }
            } else {
                String path = djo.getPID().replace("/", "_");
                path = path.replace(".", "_");
                String badFilesPath = GeodisyStrings.replaceSlashes(DATA_DIR_LOC + path + "/" + name);
                try {
                    Files.deleteIfExists(Paths.get(badFilesPath));
                } catch (IOException e) {
                    logger.error("Something went wrong trying to delete file that couldn't generate BBox: " + badFilesPath);
                }
                return new DataverseRecordFile();
            }
        }
        return new DataverseRecordFile();
    }

    private DataverseRecordFile replaceRecord() {
        DataverseRecordFile newDRF = new DataverseRecordFile(translatedTitle,fileIdent,dbID,server,datasetIdent,recordURL);
        newDRF.setOriginalTitle(originalTitle);
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
            Files.deleteIfExists(inputFile.toPath());
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
        gbb.setField(GEOSERVER_LABEL,s);
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

    public void setFileName(String s){
        gbb.setField(FILE_NAME,s);
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


    public void setGbb(GeographicBoundingBox gbb, boolean isRaster) {
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

    public boolean isOnGeoserver(){
        return false;
    }

    public int getBbCount() {
        return bbCount;
    }

    public void setBbCount(int bbCount) {
        this.bbCount = bbCount;
    }
}
