package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static BaseFiles.GeodisyStrings.*;

public class GDALTranslate {
    GeoLogger logger;

    public GDALTranslate() {
        logger = new GeoLogger(this.getClass());
    }

    public String rasterTransform(String dirPath, String name){
        process(dirPath,name,RASTER);

        int period = name.lastIndexOf(".");
        return name.substring(0,period+1) + "tif";
    }

    public String vectorTransform(String dirPath, String name){
        process(dirPath,name,VECTOR);

        int period = name.lastIndexOf(".");
        return name.substring(0,period+1) + "geojson";

    }
    private void process(String dirPath, String name, String transformType) {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        if(isWindows)
            dirPath = dirPath.replace("\\","/");
        String call;
        String nameStub = name.substring(0,name.lastIndexOf(".")+1);
        File file;
        if(transformType.equals(RASTER)) {
            call = "gdal_translate " + dirPath + name + " " + dirPath + nameStub + "tif";
            try {
                if (isWindows) {
                    Runtime.getRuntime().exec(call);
                } else {
                    Runtime.getRuntime().exec(String.format("sh %s", call));
                }
            } catch (IOException e) {
                logger.error("Something went wrong converting " + name + " to geotiff");
            }
        } else{
            call = "ogr2ogr -f GEOJSON " + dirPath + nameStub + "geojson " + dirPath + name;
            try {
                if (isWindows) {
                    Runtime.getRuntime().exec(call);
                } else {
                    Runtime.getRuntime().exec(String.format("sh %s", call));
                }

            }catch (IOException e) {
                logger.error("Something went wrong converting " + name + " to geojson");
            }
        }

        if(name.endsWith(".shp")) {
            file = new File(dirPath+nameStub+"shx");
            file.delete();
        }
        file = new File(dirPath+name);
        file.delete();
    }

}
