package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


import static BaseFiles.GeodisyStrings.*;

public class GDALTranslate {
    GeoLogger logger;

    public GDALTranslate() {
        logger = new GeoLogger(this.getClass());
    }

    public String rasterTransform(String dirPath, String name, String number){
        boolean transformed = process(dirPath,name,RASTER, number);

        if(transformed) {
            int period = name.lastIndexOf(".");
            return name.substring(0, period + 1) + "tif";
        }else
            return name;
    }
    public void rasterTransformTest(String dirPath, String name, boolean newLocation, String number){
        process(dirPath,name,RASTER,newLocation,"1");
    }

    public String vectorTransform(String dirPath, String name, String number){
        boolean transformed = process(dirPath,name,VECTOR, number);

        if(transformed) {
            int period = name.lastIndexOf(".");
            return name.substring(0, period + 1) + "shp";
        }
        else{
            return name;
        }

    }
    public void vectorTransformTest(String dirPath, String name, boolean newLocation, String number) {
        boolean transformed = process(dirPath, name, VECTOR, newLocation,"1");
    }

    private boolean process(String dirPath, String name, String transformType, String number) {
        return process(dirPath,dirPath,name,transformType, false, number);
    }

    private boolean process(String dirPath, String name, String transformType,boolean newLocation, String number) {
        return process(dirPath,dirPath,name,transformType, newLocation, number);
    }
    public boolean process(String sourcePath, String destPath, String name, String transformType, boolean newLocation, String number){

        if(newLocation){
            sourcePath = GeodisyStrings.replaceSlashes(sourcePath);
            destPath = GeodisyStrings.replaceSlashes(destPath);
        }else {
            sourcePath = GeodisyStrings.replaceSlashes(sourcePath);
            destPath = GeodisyStrings.replaceSlashes(destPath);
        }

        String call;
        String nameStub = name.substring(0,name.lastIndexOf("."));
        File file;

        ProcessBuilder processBuilder= new ProcessBuilder();

        if(transformType.equals(RASTER)) {
            call = GDAL_TRANSLATE + sourcePath + name + " " + destPath + nameStub + number + ".tif";
            //System.out.println(call);
            processBuilder.command("bash", "-c", call);
            Process process = null;
            try {

                if (IS_WINDOWS) {
                    Runtime.getRuntime().exec(call).waitFor();
                } else {
                    process = processBuilder.start();
                    process.waitFor(120, TimeUnit.SECONDS);
                }
                if(name.endsWith(".tif"))
                    return true;
                File newFile = new File(destPath+nameStub + number +".tif");
                if(newFile.exists()) {
                    file = new File(sourcePath + name);
                    file.delete();
                    return true;
                }else{
                    //System.out.println("Translation failure #" + i);
                }

            } catch (IOException | InterruptedException e) {
                logger.error("Something went wrong converting " + name + " to shapefile");
            }finally{
                if(process!=null)
                    process.destroy();
            }
        } else{
                call = OGR2OGR + destPath + nameStub + number + ".shp " + sourcePath + name;
                //System.out.println(call);
                processBuilder.command("bash", "-c", call);
                try {
                    if (IS_WINDOWS) {
                        Runtime.getRuntime().exec(call).waitFor();
                    } else {
                        Process p = processBuilder.start();
                        try {
                            p.waitFor(120, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        p.destroy();

                    }
                    if(name.endsWith(".shp"))
                        return true;
                    File newFile = new File(destPath+nameStub + number +".shp");
                    if(newFile.exists()) {
                        file = new File(sourcePath + name);
                        file.delete();
                        return true;
                    }else{
                        //System.out.println("Translation failure #" + i);
                    }
                } catch (IOException | InterruptedException e) {
                    logger.error("Something went wrong converting " + name + " to shapefile");
                }
            }
        System.out.println("Couldn't convert file: " + sourcePath+name);
        file = new File(sourcePath+name);
        file.delete();
        return false;
    }




}
