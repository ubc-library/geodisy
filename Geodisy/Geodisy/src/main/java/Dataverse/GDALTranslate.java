package Dataverse;

import BaseFiles.GeoLogger;
import GeoServer.GeoServerAPI;
import GeoServer.PostGIS;

import java.io.File;
import java.io.IOException;


import static BaseFiles.GeodisyStrings.*;

public class GDALTranslate {
    GeoLogger logger;
    DataverseJavaObject djo;

    public GDALTranslate() {
        logger = new GeoLogger(this.getClass());
    }

    public String rasterTransform(String dirPath, String name, DataverseJavaObject djo){
        this.djo = djo;
        process(dirPath,name,RASTER);

        int period = name.lastIndexOf(".");

        return name.substring(0,period+1) + "tif";
    }

    public String vectorTransform(String dirPath, String name,DataverseJavaObject djo){
        this.djo = djo;
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
            call = "ogr2ogr -f \"ESRI Shapefile\" " + dirPath + nameStub + "shp " + dirPath + name;
            try {
                if (isWindows) {
                    Runtime.getRuntime().exec(call);
                } else {
                    Runtime.getRuntime().exec(String.format("sh %s", call));
                }

            }catch (IOException e) {
                logger.error("Something went wrong converting " + name + " to shapefile");
            }
        }
        file = new File(dirPath+name);
        file.delete();

        addToPostGIS(nameStub,transformType);
        addToGeoserver(nameStub,transformType);
    }

    private void addToGeoserver(String nameStub, String transformType) {
        GeoServerAPI geoServerAPI = new GeoServerAPI(djo);
        if(transformType.equals(VECTOR))
            geoServerAPI.uploadVector(nameStub);
    }

    private void addToPostGIS(String nameStub, String transformType) {
        PostGIS postGis = new PostGIS();
        if(transformType.equals(VECTOR))
            postGis.addFile2PostGIS(djo,nameStub+"shp",VECTOR, TEST);
        /* Raster Data is added to Geoserver directly rather than through PostGIS
        else
            postGis.addFile2PostGIS(djo,nameStub+"tif",RASTER, TEST);
         */
    }



}
