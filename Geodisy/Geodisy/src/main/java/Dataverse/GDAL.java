package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.ProcessCall;
import _Strings.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static _Strings.GeodisyStrings.*;
import static _Strings.DVFieldNameStrings.*;

public class GDAL {
    GeoLogger logger = new GeoLogger(this.getClass());

    public String getGDALInfo(String filePath, String name) throws IOException {
        String gdal;
        StringBuilder gdalString = new StringBuilder();

        if(GeodisyStrings.gdalinfoRasterExtention(name)) {
            gdal = GDALINFO;
        }
        else if(GeodisyStrings.ogrinfoVectorExtension(name) && !GeodisyStrings.otherShapeFilesExtensions(name)) {
            gdal = OGRINFO;
        }else
            return "FAILURE";

        ProcessCall processCall = new ProcessCall();
        try{
            if (IS_WINDOWS) {
                LinkedList<String> args = new LinkedList<>();
                args.add("cmd.exe");
                args.add("/c");
                gdalString = new StringBuilder(processCall.runProcess(gdal + filePath,30, TimeUnit.SECONDS,args,logger)[0]);

            } else {
                gdalString = new StringBuilder(processCall.runProcess(gdal + filePath,30, TimeUnit.SECONDS,logger)[0]);

            }
        } catch (InterruptedException|ExecutionException e) {
            logger.error("Something went wrong running GDAL info on " + filePath);
        } catch (TimeoutException e) {
            logger.error("System timed out running GDAL info on " + filePath);
        }
        return gdalString.toString();
    }
    //Not used by main program
    public DataverseJavaObject generateBB(DataverseJavaObject djo) {
        String doi = djo.getPID();
        String path = GeodisyStrings.removeHTTPSAndReplaceAuthority(doi).replace(".","/") + GeodisyStrings.replaceSlashes("/");
        String folderName = DATA_DIR_LOC +path;
        LinkedList<DataverseGeoRecordFile> origRecords = djo.getGeoDataFiles();
        if(origRecords.size()==0)
            return djo;
        File folder = new File(folderName);
        if(!folder.exists())
            folder.mkdirs();


        if(folder.listFiles().length==0) {
            try {
                Files.deleteIfExists(folder.toPath());
            } catch (IOException e) {
                logger.error("Something went wrong trying to delete folder: " + folder.getAbsolutePath());
            }
            return djo;
        }

        int raster = 1;
        int vector = 1;
        LinkedList<DataverseGeoRecordFile> records = new LinkedList<>();
        GeographicBoundingBox temp = new GeographicBoundingBox(doi);
        for(DataverseGeoRecordFile drf : origRecords) {
            String name = drf.getTranslatedTitle();
            String filePath = DATA_DIR_LOC + path + name;
            File file = new File(filePath);

            if (name.endsWith("tif")) {
                temp = generateBB(file, doi, String.valueOf(raster));
                if(temp.hasBB())
                    raster++;
                else
                    continue;
            } else if (name.endsWith("shp")) {
                temp = generateBB(file, doi, String.valueOf(vector));
                if(temp.hasBB())
                    vector++;
                else
                    continue;
            } else {
                logger.error("Somehow got a DataverseGeoRecordFile that isn't for a shp or tif. File name" + name);
            }
            drf.setGbb(temp, temp.getField(FILE_NAME));
            records.add(drf);
        }
        djo.setGeoDataFiles(records);
        return djo;
    }

    public GeographicBoundingBox generateBB(File file, String doi, String number){
        String lowerName = file.getName().toLowerCase();
        String regularName = file.getName();
        String filePath = file.getPath();
        if(!GeodisyStrings.gdalinfoRasterExtention(lowerName) && !GeodisyStrings.ogrinfoVectorExtension(lowerName))
            return new GeographicBoundingBox(doi);
        if(GeodisyStrings.otherShapeFilesExtensions(lowerName))
            return new GeographicBoundingBox(doi);
        boolean gdalInfo = GeodisyStrings.gdalinfoRasterExtention(lowerName);

        String gdalString;
        GeographicBoundingBox temp;
        String projection =  "";
        try {
            gdalString = getGDALInfo(filePath, regularName);
            if(gdalString.contains("FAILURE")||(gdalString.contains("ERROR"))) {
                logger.warn("Something went wrong parsing " + regularName + " at " + filePath);
                return new GeographicBoundingBox(doi);
            }
            if(gdalInfo) {
                temp = getRaster(gdalString, filePath, regularName );
                projection = getProjection(gdalString);
            }
            else {
                temp = getVector(gdalString, regularName, filePath);
                projection = temp.getField(PROJECTION);
            }
            temp.setIsGeneratedFromGeoFile(true);

            if(temp.hasBB()) {
                GeographicBoundingBox gbb = new GeographicBoundingBox(doi);
                gbb.setIsGeneratedFromGeoFile(temp.isGeneratedFromGeoFile());
                gbb.setField(FILE_NAME,temp.getField(FILE_NAME));
                gbb.setField(GEOMETRY,temp.getField(GEOMETRY));
                gbb.setField(PROJECTION,projection);
                gbb.setBB(temp.getBB());
                gbb.setField(GDAL_STRING,gdalString);
                ExistingGeoLabelsVals existingGeoLabelsVals = ExistingGeoLabelsVals.getExistingGeoLabelsVals();
                lowerName = gbb.getField(FILE_NAME).toLowerCase();
                if(lowerName.endsWith(".shp")) {
                    gbb.setField(GEOSERVER_LABEL,existingGeoLabelsVals.addVector(doi,file.getName()));
                    gbb.setFileNumber(Integer.valueOf(number));
                    existingGeoLabelsVals.saveExistingGeoLabels();
                    return gbb;
                }
                else if(lowerName.endsWith(".tif")) {
                    gbb.setField(GEOSERVER_LABEL, existingGeoLabelsVals.addRaster(doi,file.getName()));
                    gbb.setFileNumber(Integer.valueOf(number));
                    existingGeoLabelsVals.saveExistingGeoLabels();
                    return gbb;
                }
                else {
                    logger.error("Somehow got a bounding box, but isn't a shp or tif with file " + filePath + " and persistantID=" + doi);
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

    public GeographicBoundingBox generateBoundingBoxFromCSV(String fileName, DataverseJavaObject djo){
        String path = GeodisyStrings.removeHTTPSAndReplaceAuthority(djo.getPID()).replace("/","_") + GeodisyStrings.replaceSlashes("/");
        path = path.replace(".","_");
        String filePath = DATA_DIR_LOC + path + fileName;
        String name = fileName;
        String ogrString = null;
        try {
            ogrString = getGDALInfo(filePath, name);
            if(ogrString.contains("FAILURE")) {
                logger.warn("Something went wrong parsing " + name + " at " + filePath);
                return new GeographicBoundingBox(djo.getPID());
            }
        GeographicBoundingBox temp = getVector(ogrString, name, filePath);
        temp.setIsGeneratedFromGeoFile(true);
        return temp;
        } catch (IOException e) {
            logger.error("Something went wrong trying to check " + name + " from record " + djo.getPID());
        }
        return new GeographicBoundingBox("junk");
    }

    private GeographicBoundingBox getRaster(String gdalString, String filePath, String fileName) throws IOException {

        GeographicBoundingBox temp = new GeographicBoundingBox("temp");
        BoundingBox bb = getLatLongGdalInfo(gdalString);
        if(isZeroPoint(bb))
            return new GeographicBoundingBox("junk");
        if(bb.hasUTMCoords()||!fileName.endsWith(".tif")) {
            fileName = convertToAppropriateFileFormat(filePath, fileName);
            filePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".tif";
            gdalString = getGDALInfo(filePath, fileName);
            bb = getLatLongGdalInfo(gdalString);

        }

        bb.setFileName(fileName);
        temp.setBB(bb);
        temp.setField(GEOMETRY,RASTER);
        temp.setWidthHeight(gdalString);
        temp.setField(FILE_NAME,fileName);
        return temp;
    }

    private boolean isZeroPoint(BoundingBox bb) {
        return bb.getLongWest()==0 && bb.getLongEast()==0 && bb.getLatNorth()==0 && bb.getLatSouth()==0;
    }

    private GeographicBoundingBox getVector(String gdalString, String fileName, String filePath) throws IOException {
        String geo = getGeometryType(gdalString);
        GeographicBoundingBox gbb = new GeographicBoundingBox("temp");
        BoundingBox bb;
        //System.out.println("Bounding box: " + temp.getLatNorth() + "N, " + temp.getLatSouth() + "S, " + temp.getLongEast() + "E, " + temp.getLongWest() + "W");
        gbb.setField(PROJECTION,"EPSG:4326");
        bb = getLatLongOgrInfo(gdalString);
        if(gdalString.contains("FAILURE"))
            return new GeographicBoundingBox("junk");
        if(isZeroPoint(bb))
            return new GeographicBoundingBox("junk");
        if(bb.hasUTMCoords() || !fileName.endsWith("shp")) {
            fileName = convertToAppropriateFileFormat(filePath, fileName);
            filePath = filePath.substring(0,filePath.lastIndexOf("."))+".shp";
            gdalString = getGDALInfo(filePath,fileName);
            bb = getLatLongOgrInfo(gdalString);
        }
        bb.setFileName(fileName);
        gbb.setField(GEOMETRY,geo);
        if(bb.hasBoundingBox())
            gbb.setBB(bb);
        return gbb;
    }

    private String getAuthority(String gdalString) throws IndexOutOfBoundsException {

        int projLoc = gdalString.lastIndexOf("AUTHORITY[\"") + 11;
        String projectionString = gdalString.substring(projLoc);
        int projLocEnd = projectionString.indexOf("\"");
        String first = projectionString.substring(0, projLocEnd);
        projLoc = projectionString.indexOf("\",\"") + 3;
        projectionString = projectionString.substring(projLoc);
        projLocEnd = projectionString.indexOf("\"]]");
        String second = projectionString.substring(0, projLocEnd);
        return first + ":" + second;
    }
 
    private String convertToAppropriateFileFormat(String filePath, String name) throws IOException {
        GDALTranslate gdalTranslate = new GDALTranslate();
        String path = new File(filePath).getPath();
        String stub;
        if(GeodisyStrings.ogrinfoVectorExtension(name))
            stub = gdalTranslate.vectorTransform(path,name);
        else
            stub = gdalTranslate.rasterTransform(path,name);
        if(!path.endsWith(stub))
            path = path.substring(0,path.lastIndexOf(GeodisyStrings.replaceSlashes("/"))+1) + stub;
        File check = new File(path);
        if(!check.exists())
            logger.warn("Couldn't convert " + name +" to  WGS84 from location " + filePath);
        return stub;
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

    private BoundingBox getLatLongOgrInfo(String gdalString) {
        BoundingBox bb = new BoundingBox();
        int start = gdalString.indexOf("Extent: (")+9;
        int end;

        if(start != -1+9) {
            end = gdalString.indexOf(", ", start);

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
            } catch (StringIndexOutOfBoundsException e) {
                return new BoundingBox();
            }
        }
        return bb;
    }
    private BoundingBox getLatLongGdalInfo(String gdalStringFull) {
        int start = gdalStringFull.indexOf("Upper Left  (")+13;
        if(start > 12) {
            try {
                String gdalString = gdalStringFull.substring(start);
                gdalString = gdalString.substring(gdalString.indexOf("(") + 1);
                int end = gdalString.indexOf(",");
                String west = parseDecimalDegrees(gdalString.substring(0, end));
                gdalString = gdalString.substring(end + 1);
                start = 0;
                end = gdalString.indexOf(")");
                String north = parseDecimalDegrees(gdalString.substring(start, end));
                start = gdalString.indexOf("Lower Right (") + 13;
                gdalString = gdalString.substring(start);
                gdalString = gdalString.substring(gdalString.indexOf("(") + 1);
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
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                return new BoundingBox();
            }
        }
        return new BoundingBox();
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
            String gdalString = getGDALInfo(path, name);
            if (gdalString.contains("FAILURE")){
                logger.warn("Something went wrong parsing " + file.getName() + " at " + file.getPath());
                return temp;
        }
            temp.setProjection(getProjection(gdalString));
            temp.setGeometryType(getGeometryType(gdalString));
            GeographicBoundingBox bb;
            if(GeodisyStrings.gdalinfoRasterExtention(name))
                bb = getRaster(gdalString, file.getAbsolutePath() , file.getName() );
            else
                bb = getVector(gdalString, file.getName(),file.getAbsolutePath());
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
