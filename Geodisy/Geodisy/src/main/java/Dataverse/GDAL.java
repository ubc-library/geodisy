package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

import static BaseFiles.GeodisyStrings.*;
import static Dataverse.DVFieldNameStrings.*;

public class GDAL {
    GeoLogger logger = new GeoLogger(this.getClass());

    public DataverseJavaObject generateBB(DataverseJavaObject djo) {
        String doi = djo.getDOI();
        String path = doi.replace(".","/");
        String folderName = DATASET_FILES_PATH +path+"/";
        LinkedList<DataverseGeoRecordFile> origRecords = djo.getGeoDataFiles();
        if(origRecords.size()==0)
            return djo;
        File folder = new File(folderName);
        if(!folder.exists())
            folder.mkdirs();


        if(folder.listFiles().length==0) {
            folder.delete();
            return djo;
        }
        String[] fileNames = folder.list();
        GeographicBoundingBox gbb;
        int raster = 0;
        int vector = 0;
        LinkedList<DataverseGeoRecordFile> records = new LinkedList<>();
        for(DataverseGeoRecordFile drf : origRecords) {
            String name = drf.getTitle();
            gbb = new GeographicBoundingBox(doi);
            String lowerName = name.toLowerCase();
            String filePath = DATASET_FILES_PATH + path + "/" + name;
            boolean gdalInfo = GeodisyStrings.gdalinfoRasterExtention(lowerName);

            String gdalString;
            GeographicBoundingBox temp;
            String projection = "";


            if (gdalInfo)
                raster++;
            else
                vector++;
            try {
                gdalString = getGDALInfo(filePath, name, IS_WINDOWS);
                if (gdalString.contains("FAILURE")) {
                    logger.warn("Something went wrong parsing " + name + " at " + filePath);
                    if (name.endsWith(".tif"))
                        raster--;
                    if (name.endsWith(".shp"))
                        vector--;
                    continue;
                }
                String convertedName;
                String nameStub = name.substring(0, name.length() - 3);
                if (gdalInfo) {
                    temp = getRaster(gdalString);
                    projection = getProjection(gdalString);
                    convertedName = nameStub + "tif";
                } else {
                    temp = getVector(gdalString, IS_WINDOWS, lowerName, filePath);
                    projection = temp.getField(PROJECTION);
                    convertedName = nameStub + "shp";
                }
                temp.setIsGeneratedFromGeoFile(true);
                if (!temp.hasBB()) {
                    if (GeodisyStrings.gdalinfoRasterExtention(name))
                        raster--;
                    if (GeodisyStrings.ogrinfoVectorExtension(name))
                        vector--;
                    File f = new File(DATASET_FILES_PATH + path + "/" + convertedName);
                    f.delete();
                    continue;
                } else{
                    temp.setField(FILE_NAME,name);
                    DataverseGeoRecordFile tempRec = new DataverseGeoRecordFile(drf.getDatasetIdent(),temp);
                    tempRec.setOriginalTitle(convertedName);
                    tempRec.setTitle(convertedName);
                    tempRec.setDatasetIdent(drf.getDatasetIdent());
                    tempRec.setFileIdent(drf.getFileIdent());
                    tempRec.setDbID(drf.getDbID());
                    tempRec.setProjection(projection);
                    tempRec.setGdalString(gdalString, gdalInfo);
                    if(gdalInfo)
                        tempRec.setFileNumber(raster);
                    else
                        tempRec.setFileNumber(vector);
                    records.add(tempRec);
                }
             /*
                gbb.setField(GEOMETRY,temp.getField(GEOMETRY));
                GeographicFields gf = djo.getGeoFields();
                List<GeographicBoundingBox> bboxes = gf.getGeoBBoxes();
                gbb.setBB(temp.getBB());
                gbb.setFileName(name);
                gbb.setField(PROJECTION,projection);
                if(name.endsWith(".shp"))
                    gbb.setField(GEOSERVER_LABEL,djo.getSimpleFieldVal(PERSISTENT_ID)+ gbb.getFileNumber());

                gf.addBB(bboxes, gbb);
                djo.setGeoFields(gf);

                GeoServerAPI geoServerAPI = new GeoServerAPI(djo);
                geoServerAPI.uploadVector(name);

              */

            } catch (IOException e) {
                logger.error("Something went wrong trying to call GDAL with " + name);
            }
        }
        djo.setGeoDataFiles(records);
        return djo;
    }

    public GeographicBoundingBox generateBB(File file, String doi, String number){
        String lowerName = file.getName().toLowerCase();
        String regularName = file.getName();
        String filePath = file.getAbsolutePath();
        if(!GeodisyStrings.gdalinfoRasterExtention(lowerName) && !GeodisyStrings.ogrinfoVectorExtension(lowerName))
            return new GeographicBoundingBox(doi);
        boolean gdalInfo = GeodisyStrings.gdalinfoRasterExtention(lowerName);

        String gdalString;
        GeographicBoundingBox temp;
        String projection =  "";
        try {
            gdalString = getGDALInfo(filePath, lowerName, IS_WINDOWS);
            if(gdalString.contains("FAILURE")) {
                logger.warn("Something went wrong parsing " + lowerName + " at " + filePath);
                return new GeographicBoundingBox(doi);
            }
            if(gdalInfo) {
                temp = getRaster(gdalString);
                projection = getProjection(gdalString);
            }
            else {
                temp = getVector(gdalString, IS_WINDOWS, lowerName, filePath);
                projection = temp.getField(PROJECTION);
            }
            temp.setIsGeneratedFromGeoFile(true);

            if(temp.hasBB()) {
                GeographicBoundingBox gbb = new GeographicBoundingBox(doi);
                gbb.setIsGeneratedFromGeoFile(temp.isGeneratedFromGeoFile());
                gbb.setField(FILE_NAME,lowerName);
                gbb.setField(GEOMETRY,temp.getField(GEOMETRY));
                gbb.setField(PROJECTION,projection);
                gbb.setBB(temp.getBB());
                if(lowerName.endsWith(".shp")) {
                    gbb.setField(GEOSERVER_LABEL, doi + "v" + number);
                    return gbb;
                }
                else if(lowerName.endsWith(".tif")) {
                    gbb.setField(GEOSERVER_LABEL, doi + "r" + number);
                    return gbb;
                }
                else {
                    logger.error("Somehow got a bounding box, but isn't a shp or tif");
                    return new GeographicBoundingBox(doi);
                }

            }
        } catch (IOException e) {
            logger.error("Something went wrong trying to call GDAL with " + lowerName);
        }
        return new GeographicBoundingBox(doi);
    }


    private String getProjection(String gdalString) {
        String projection = "";
        int start = gdalString.indexOf("PROJCS[\"");
        if(start == -1)
            start = gdalString.indexOf("GEOGCS[\"");
        if(start == -1)
            return projection;
        String sub = gdalString.substring(start+8);
        int end = sub.indexOf("\"");
        projection = sub.substring(0,end);
        return projection;
    }

    public GeographicBoundingBox generateBoundingBoxFromCSV(File file, DataverseJavaObject djo){
        String path = djo.getDOI().replace("/","_");
        path = path.replace(".","_");
        String filePath = DATASET_FILES_PATH + path + "/" + file.getName();
        String name = file.getName();
        String ogrString = null;
        try {
            ogrString = getGDALInfo(filePath, name, IS_WINDOWS);
            if(ogrString.contains("FAILURE")) {
                logger.warn("Something went wrong parsing " + name + " at " + filePath);
                return new GeographicBoundingBox(djo.getDOI());
            }
        GeographicBoundingBox temp = getVector(ogrString, IS_WINDOWS, name, filePath);
        temp.setIsGeneratedFromGeoFile(true);
        return temp;
        } catch (IOException e) {
            logger.error("Something went wrong trying to check " + name + " from record " + djo.getDOI());
        }
        return new GeographicBoundingBox("junk");
    }

    private GeographicBoundingBox getRaster(String gdalString) {
        int start = gdalString.indexOf("Upper Left  (")+13;
        GeographicBoundingBox temp = new GeographicBoundingBox("temp");
        if(start > 12) {
            temp.setBB(getLatLongGdalInfo(gdalString, start));
            temp.setField(GEOMETRY,RASTER);
        }
        return temp;
    }

    private GeographicBoundingBox getVector(String gdalString, boolean isWindows, String name, String filePath) throws IOException {
        String geo = getGeometryType(gdalString);
        int start = gdalString.indexOf("Extent: (")+9;
        int end;
        GeographicBoundingBox gbb = new GeographicBoundingBox("temp");

        BoundingBox temp = new BoundingBox();
        if(start != -1+9) {
            end = gdalString.indexOf(", ", start);
            temp = getLatLongOgrInfo(gdalString, start, end);
            if (temp.hasUTMCoords()) {
                convertToWGS84(filePath, isWindows, name);
                gbb.setField(PROJECTION,"EPSG:4326");
                gdalString = getGDALInfo(filePath, name, isWindows);
                if(gdalString.contains("FAILURE"))
                    return new GeographicBoundingBox("temp");
                start = gdalString.indexOf("Extent: (") + 9;
                end = gdalString.indexOf(", ", start);
                temp = getLatLongOgrInfo(gdalString, start, end);
            }
            else{
                try {
                    int projLoc = gdalString.lastIndexOf("AUTHORITY[\"") + 11;
                    if (projLoc > 10) {
                        String authority = getAuthority(gdalString, projLoc);
                        gbb.setField(PROJECTION, authority);
                    }
                }catch (IndexOutOfBoundsException e){
                    logger.error("Couldn't determine projection for record " + name);
                }
            }
        }
        if(gdalString.contains("Geometry:")){
            start = gdalString.indexOf("Geometry:")+10;
            end = gdalString.indexOf("Feature Count:");
            gbb.setField(GEOMETRY,gdalString.substring(start,end));

        }
        gbb.setBB(temp);
        return gbb;
    }

    private String getAuthority(String gdalString, int projLoc) {
        String projectionString = gdalString.substring(projLoc);
        int projLocEnd = projectionString.indexOf("\"");
        String first = projectionString.substring(0, projLocEnd);
        projLoc = projectionString.indexOf("\",\"") + 3;
        projectionString = projectionString.substring(projLoc);
        projLocEnd = projectionString.indexOf("\"]]");
        String second = projectionString.substring(0, projLocEnd);
        return first+ ":" + second;
    }


    private void convertToWGS84(String filePath, boolean isWindows, String name) throws IOException {
        String gdal;
        convertName(filePath,name);
        String filePathLower = filePath.toLowerCase();
        String nameLower = name.toLowerCase();
        if(GeodisyStrings.gdalinfoRasterExtention(name))
            gdal = GDAL_TRANSLATE;
        else
            gdal = OGR2OGR;
        String newAndOld = filePath + " " + filePath.substring(0,filePathLower.indexOf(nameLower)) + "_old_" + name;
        if(isWindows){
            Runtime.getRuntime().exec(gdal + newAndOld);
        } else {
            ProcessBuilder processBuilder= new ProcessBuilder();
            processBuilder.command("bash", "-c", gdal + newAndOld);
            processBuilder.start();
        }
    }

    private void convertName(String filePath, String name) throws IOException {
        // File (or directory) with old name
        File file = new File(filePath);
        String filepathlower= filePath.toLowerCase();
        String nameLower = name.toLowerCase();

// File (or directory) with new name
        File file2 = new File(filePath.substring(0,filepathlower.indexOf(nameLower)) + "_old_" + name);

        if (file2.exists()) {
            logger.error("Something went wrong trying to rename the original file when converting to WSG84");
            throw new java.io.IOException("file exists");
        }

// Rename file (or directory)
        boolean success = file.renameTo(file2);

        if (!success) {
            // File was not successfully renamed
        }
    }

    public String getGDALInfo(String filePath, String name, boolean isWindows) throws IOException {
        String gdal;
        StringBuilder gdalString = new StringBuilder();

        Process process;
        if(GeodisyStrings.gdalinfoRasterExtention(name)) {
            gdal = GDALINFO;
        }
        else {
            gdal = OGRINFO;
        }
        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.command("bash", "-c", gdal+filePath);
        int counter = 0;
            if (isWindows) {
                process = Runtime.getRuntime()
                        .exec(String.format(gdal + filePath));
            } else {
                process = processBuilder.start();
            }

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            String s;
            while ((s = stdInput.readLine()) != null) {
                gdalString.append(s);
            }


        return gdalString.toString();
    }

    private BoundingBox compare(BoundingBox temp, BoundingBox fullExtent) {
        if(fullExtent.getLongWest()==361||temp.getLongWest()<fullExtent.getLongWest()||(fullExtent.getLongWest()<0 && temp.getLongWest()>=0 && temp.getLongEast()< temp.getLongWest()))
            fullExtent.setLongWest(temp.getLongWest());
        if(fullExtent.getLongEast()==361 || temp.getLongEast()>fullExtent.getLongEast() ||(fullExtent.getLongEast()>=0 && temp.getLongEast()<0 && temp.getLongWest()>temp.getLongEast()))
            fullExtent.setLongEast(temp.getLongEast());
        if(fullExtent.getLatNorth()==361 || temp.getLatNorth()>fullExtent.getLatNorth())
            fullExtent.setLatNorth(temp.getLatNorth());
        if(fullExtent.getLatSouth()==361 || temp.getLatSouth()<fullExtent.getLatSouth())
            fullExtent.setLatSouth(temp.getLatSouth());
        return fullExtent;
    }

    private BoundingBox getLatLongOgrInfo(String gdalString, int start, int end) {
        BoundingBox bb = new BoundingBox();
        try {
            String west = gdalString.substring(start, end).trim();
            start = end + 2;
            end = gdalString.indexOf(")", start);
            String south = gdalString.substring(start, end).trim();
            start = gdalString.indexOf("- (", end) + 3;
            end = gdalString.indexOf(", ", start);
            String east = gdalString.substring(start, end).trim();
            start = end + 2;
            end = gdalString.indexOf(")", start);
            String north = gdalString.substring(start, end).trim();
            bb.setLongWest(west);
            bb.setLongEast(east);
            bb.setLatNorth(north);
            bb.setLatSouth(south);
            bb.setGenerated(true);
        }catch (StringIndexOutOfBoundsException e){
            System.out.println(gdalString);
            return bb;
        }
        return bb;
    }
    private BoundingBox getLatLongGdalInfo(String gdalStringFull, int start) {
        try {
            String gdalString = gdalStringFull.substring(start);
            gdalString = gdalString.substring(gdalString.indexOf("(")+1);
            int end = gdalString.indexOf(",");
            String west = parseDecimalDegrees(gdalString.substring(0, end));
            gdalString = gdalString.substring(end + 1);
            start = 0;
            end = gdalString.indexOf(")");
            String north = parseDecimalDegrees(gdalString.substring(start, end));
            start = gdalString.indexOf("Lower Right (") + 13;
            gdalString = gdalString.substring(start);
            gdalString = gdalString.substring(gdalString.indexOf("(")+1);
            end = gdalString.indexOf(",");
            String east = parseDecimalDegrees(gdalString.substring(0, end));
            gdalString = gdalString.substring(end + 1);
            start = 0;
            end = gdalString.indexOf(")");
            String south = parseDecimalDegrees(gdalString.substring(start, end));
            BoundingBox bb = new BoundingBox();
            bb.setLongWest(west);
            bb.setLongEast(east);
            bb.setLatNorth(north);
            bb.setLatSouth(south);
            bb.setGenerated(true);
            return bb;
        }catch (IndexOutOfBoundsException | NumberFormatException e){
            return new BoundingBox();
        }
    }

    private String parseDecimalDegrees(String s) throws NumberFormatException{
        float degrees = Float.parseFloat(s.substring(0,s.indexOf("d")));
        float minutes = Float.parseFloat(s.substring(s.indexOf("d")+1,s.indexOf("'")))/60;
        float seconds = Float.parseFloat(s.substring(s.indexOf("'")+1,s.indexOf("\"")))/3600;
        degrees += minutes + seconds;
        String direction = s.substring(s.indexOf("\"")+1,s.indexOf("\"")+2);
        if(direction.equals("W")||direction.equals(("S")))
            degrees = degrees*-1;
        return String.valueOf(degrees);

    }

    public DataverseRecordFile parse(File file){
        String path = file.getAbsolutePath();
        String name = file.getName();
        DataverseRecordFile temp = new DataverseRecordFile();

        try {
            String gdalString = getGDALInfo(path, name, IS_WINDOWS);
            if (gdalString.contains("FAILURE")){
                logger.warn("Something went wrong parsing " + file.getName() + " at " + file.getPath());
                return temp;
        }
            temp.setProjection(getProjection(gdalString));
            temp.setGeometryType(getGeometryType(gdalString));
            GeographicBoundingBox bb;
            if(GeodisyStrings.gdalinfoRasterExtention(name))
                bb = getRaster(gdalString);
            else
                bb = getVector(gdalString,IS_WINDOWS,file.getName(),file.getAbsolutePath());
            temp.setIsFromFile(true);
            temp.setBB(bb.getBB());
        } catch (IOException e) {
            logger.error("Something went wrong parsing file: " + file.getName() + " at " + file.getAbsolutePath());
            return new DataverseRecordFile();
        }
        return temp;
    }

    private String getGeometryType(String gdalString) {
        if(gdalString.contains("Driver: GTiff/GeoTiFFFiles:"))
            return RASTER;
        int start = gdalString.indexOf("Geometry: ") + 10;
        if(start>9){
            int end = gdalString.indexOf("Feature Count:");
            if(end!=-1){
                return gdalString.substring(start,end).trim();
            }
        }
        return "";
    }
}
