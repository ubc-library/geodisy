package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;


import java.io.File;
import java.io.IOException;


import static BaseFiles.GeodisyStrings.*;

public class GDALTranslate {
    GeoLogger logger;

    public GDALTranslate() {
        logger = new GeoLogger(this.getClass());
    }

    public String rasterTransform(String dirPath, String name){
        boolean transformed = process(dirPath,name,RASTER);

        if(transformed) {
            int period = name.lastIndexOf(".");
            return name.substring(0, period + 1) + "tif";
        }else
            return name;
    }
    public void rasterTransformTest(String dirPath, String name, boolean newLocation){
        process(dirPath,name,RASTER,newLocation);
    }

    public String vectorTransformTest(String dirPath, String name){
        boolean transformed = process(dirPath,name,VECTOR);

        if(transformed) {
            int period = name.lastIndexOf(".");
            return name.substring(0, period + 1) + "shp";
        }
        else{
            return name;
        }

    }
    public void vectorTransformTest(String dirPath, String name, boolean newLocation) {
        boolean transformed = process(dirPath, name, VECTOR, newLocation);
    }

    private boolean process(String dirPath, String name, String transformType) {
        return process(dirPath,dirPath,name,transformType, false);
    }

    private boolean process(String dirPath, String name, String transformType,boolean newLocation) {
        return process(dirPath,dirPath,name,transformType, newLocation);
    }
    public boolean process(String sourcePath, String destPath, String name, String transformType, boolean newLocation){
        if(transformType.equals(VECTOR))
            if(GeodisyStrings.otherShapeFilesExtensions(name))
                return false;
            else if(name.endsWith(".shp"))
                return false;
        if(transformType.equals(RASTER))
            if(name.endsWith(".tif"))
                return false;
        if(newLocation){
            sourcePath = GeodisyStrings.replaceSlashes(sourcePath);
            destPath = GeodisyStrings.replaceSlashes(destPath);
        }else {
            sourcePath = GEODISY_PATH_ROOT + GeodisyStrings.replaceSlashes(sourcePath);
            destPath = GEODISY_PATH_ROOT + GeodisyStrings.replaceSlashes(destPath);
        }
        String call;
        String nameStub = name.substring(0,name.lastIndexOf("."));
        File file;
        GDAL gdal = new GDAL();
        ProcessBuilder processBuilder= new ProcessBuilder();

        for(int i = 0; i<8; i++){
        if(transformType.equals(RASTER)) {
            call = GDAL_TRANSLATE + sourcePath + name + " " + destPath + nameStub + ".tif";
            processBuilder.command("bash", "-c", call);
            try {

                if (IS_WINDOWS) {
                    Runtime.getRuntime().exec(call);
                } else {
                    processBuilder.start();
                }
                String answer = gdal.getGDALInfo(destPath + nameStub + ".tif", name, IS_WINDOWS);
                if(answer.contains("successful.Layer name")) {
                    file = new File(sourcePath+name);
                    file.delete();
                    return true;
                }
            } catch (IOException e) {
                logger.error("Something went wrong converting " + name + " to geotiff");
            }
        } else{
                call = OGR2OGR + destPath + nameStub + ".shp " + sourcePath + name;
                processBuilder.command("bash", "-c", call);
                try {
                    if (IS_WINDOWS) {
                        Runtime.getRuntime().exec(call);
                    } else {
                        processBuilder.start();
                    }
                    String answer = gdal.getGDALInfo(destPath + nameStub + ".shp", name, IS_WINDOWS);
                    if(answer.contains("successful.Layer name:")) {
                        file = new File(sourcePath+name);
                        file.delete();
                        return true;
                    }
                } catch (IOException e) {
                    logger.error("Something went wrong converting " + name + " to shapefile");
                }
            }
        }
        file = new File(sourcePath+name);
        file.delete();
        return false;
    }




}
