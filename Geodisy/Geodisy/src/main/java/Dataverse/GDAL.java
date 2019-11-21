package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import java.io.*;
import java.util.List;
import static BaseFiles.GeodisyStrings.*;

public class GDAL {
    GeoLogger logger = new GeoLogger(this.getClass());

    public DataverseJavaObject generateBB(DataverseJavaObject djo) {
        String doi = djo.getDOI();
        String path = doi.replace("/","_");
        path = path.replace(".","_");
        String folderName = DATASET_FILES_PATH +path+"/";
        File folder = new File(folderName);
        if(!folder.exists())
            folder.mkdirs();
    /*    if(folder.createNewFile()) {
            folder.delete();
            return djo;
        }*/

        if(folder.listFiles().length==0) {
            folder.delete();
            return djo;
        }
        String[] files = folder.list();
        for(String name: files){
            String filePath = DATASET_FILES_PATH + path + "/" + name;
            if(!GeodisyStrings.gdalinfoRasterExtention(name) && !GeodisyStrings.ogrinfoVectorExtension(name))
                continue;
            boolean gdalInfo = GeodisyStrings.gdalinfoRasterExtention(name);
            boolean isWindows = System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");

            String gdalString;
            BoundingBox temp;
            try {
                gdalString = getGDALInfo(filePath, name, isWindows);
                if(gdalInfo)
                    temp = getRaster(gdalString);
                else
                    temp = getVector(gdalString, isWindows, name, filePath);
                temp.setGenerated(true);
                GeographicFields gf = djo.getGeoFields();
                List<GeographicBoundingBox> bboxes = gf.getGeoBBoxes();
                GeographicBoundingBox gBB =  new GeographicBoundingBox(djo.getDOI());
                gBB.setEastLongitude(String.valueOf(temp.getLongEast()));
                gBB.setWestLongitude(String.valueOf(temp.getLongWest()));
                gBB.setNorthLatitude(String.valueOf(temp.getLatNorth()));
                gBB.setEastLongitude(String.valueOf(temp.getLatSouth()));
                gBB.setFileName(name);
                gf.addBB(bboxes,gBB);
                djo.setGeoFields(gf);
            } catch (IOException e) {
                logger.error("Something went wrong trying to call GDAL with " + name);
            }

        }
        return djo;
    }
    public BoundingBox generateBoundingBoxFromCSV(File file, DataverseJavaObject djo){
        String path = djo.getDOI().replace("/","_");
        path = path.replace(".","_");
        String filePath = DATASET_FILES_PATH + path + "/" + file.getName();
        String name = file.getName();
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        String ogrString = null;
        try {
            ogrString = getGDALInfo(filePath, name, isWindows);

        BoundingBox temp = getVector(ogrString, isWindows, name, filePath);
        temp.setGenerated(true);
        return temp;
        } catch (IOException e) {
            logger.error("Something went wrong trying to check " + name + " from record " + djo.getDOI());
        }
        return new BoundingBox();
    }

    private BoundingBox getRaster(String gdalString) {
        int start = gdalString.indexOf("Upper Left  (")+12;
        BoundingBox temp = new BoundingBox();
        if(start != -1+12) {
            temp = getLatLongGdalInfo(gdalString, start);
        }
        return temp;
    }



    private BoundingBox getVector(String gdalString, boolean isWindows, String name, String filePath) throws IOException {
        int start = gdalString.indexOf("Extent: (")+9;
        BoundingBox temp = new BoundingBox();
        if(start != -1+9) {
            int end = gdalString.indexOf(", ", start);
            temp = getLatLongOgrInfo(gdalString, start, end);
            if (temp.hasUTMCoords()) {
                convertToWGS84(filePath, isWindows, name);
                gdalString = getGDALInfo(filePath, name, isWindows);
                start = gdalString.indexOf("Extent: (") + 9;
                end = gdalString.indexOf(", ", start);
                temp = getLatLongOgrInfo(gdalString, start, end);
            }
        }
        return temp;
    }



    private void convertToWGS84(String filePath, boolean isWindows, String name) throws IOException {
        String gdal;
        convertName(filePath,name);
        if(GeodisyStrings.gdalinfoRasterExtention(name))
            gdal = GDAL_TRANSLATE_LOCAL;
        else
            gdal = OGR2OGR_LOCAL;
        String newAndOld = filePath + " " + filePath.substring(0,filePath.indexOf(name)) + "_old_" + name;
        if(isWindows){
            Runtime.getRuntime().exec(gdal + newAndOld);
        } else {
            Runtime.getRuntime()
                    .exec(String.format("sh %s", gdal + newAndOld));
        }
    }

    private void convertName(String filePath, String name) throws IOException {
        // File (or directory) with old name
        File file = new File(filePath);

// File (or directory) with new name
        File file2 = new File(filePath.substring(0,filePath.indexOf(name)) + "_old_" + name);

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

    private String getGDALInfo(String filePath, String name, boolean isWindows) throws IOException {
        String gdal;
        StringBuilder gdalString = new StringBuilder();
        Process process;
        if(GeodisyStrings.gdalinfoRasterExtention(name))
            gdal = GDALINFO_LOCAL;
        else
            gdal = OGRINFO_LOCAL;
        if (isWindows) {
            process = Runtime.getRuntime()
                    .exec(String.format(gdal + filePath));
        } else {
            process = Runtime.getRuntime()
                    .exec(String.format("sh %s", gdal + filePath));
        }

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        String s;
        while((s = stdInput.readLine()) != null) {
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
        String long1 = gdalString.substring(start,end).trim();
        start = end+2;
        end = gdalString.indexOf(")",start);
        String lat1 = gdalString.substring(start,end).trim();
        start = gdalString.indexOf("- (",end)+3;
        end = gdalString.indexOf(", ",start);
        String long2 = gdalString.substring(start,end).trim();
        start = end+2;
        end = gdalString.indexOf(")",start);
        String lat2 = gdalString.substring(start,end).trim();
        BoundingBox bb =  new BoundingBox();
        bb.setLongWest(long2);
        bb.setLongEast(long1);
        bb.setLatNorth(lat2);
        bb.setLatSouth(lat1);
        bb.setGenerated(true);
        return bb;
    }
    private BoundingBox getLatLongGdalInfo(String gdalString, int start) {
        int end = gdalString.indexOf(",",start);
        String long1 = gdalString.substring(start,end).trim();
        start = end+2;
        end = gdalString.indexOf(")",start);
        String lat1 = gdalString.substring(start,end).trim();
        start = gdalString.indexOf("Lower Right (")+12;
        end = gdalString.indexOf(",",start);
        String long2 = gdalString.substring(start,end).trim();
        start = end+2;
        end = gdalString.indexOf(")",start);
        String lat2 = gdalString.substring(start,end).trim();
        BoundingBox bb =  new BoundingBox();
        bb.setLongWest(long1);
        bb.setLongEast(long2);
        bb.setLatNorth(lat1);
        bb.setLatSouth(lat2);
        bb.setGenerated(true);
        return bb;
    }
}
