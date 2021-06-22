package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.ProcessCall;
import _Strings.GeodisyStrings;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import static _Strings.GeodisyStrings.*;

public class GDALTranslate {
    GeoLogger logger;
    ProcessCall processCall;

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
    public void rasterTransformTest(String dirPath, String name){
        process(dirPath,name,RASTER);
    }

    public String vectorTransform(String dirPath, String name){
        boolean transformed = process(dirPath,name,VECTOR);

        if(transformed) {
            int period = name.lastIndexOf(".");
            int endIndex;
            //postgres maximum table name length is 63 characters
            endIndex = period;
            return name.substring(0, endIndex) + ".shp";
        }
        else{
            return name;
        }

    }
    public void vectorTransformTest(String dirPath, String name) {
        process(dirPath, name, VECTOR);
    }

    private boolean process(String dirPath, String name, String transformType) {
        return process(dirPath,dirPath,name,transformType);
    }

    public boolean process(String sourcePath, String destPath, String name, String transformType){

        sourcePath = GeodisyStrings.replaceSlashes(sourcePath);
        destPath = GeodisyStrings.replaceSlashes(destPath);
        destPath = destPath.substring(0,destPath.lastIndexOf(GeodisyStrings.replaceSlashes("/")))+GeodisyStrings.replaceSlashes("/");


        String call;
        String nameStub = name.substring(0,name.lastIndexOf("."));
        File file;

        if(transformType.equals(RASTER)) {
            call = GDAL_TRANSLATE + sourcePath + " " + destPath + "temp.tif";
            call = GeodisyStrings.replaceSlashes(call);

            try {
                processCall = new ProcessCall();
                processCall.runProcess(call,120,TimeUnit.SECONDS,logger);
                if(name.endsWith(".tif"))
                    return true;
                File newFile = new File(destPath + "temp.tif");
                if(newFile.exists()) {
                    file = new File(sourcePath + name);
                    Files.deleteIfExists(file.toPath());
                    newFile.renameTo(new File(destPath + nameStub + ".tif"));
                    return true;
                }else{
                    throw new IOException();
                }

            } catch (IOException | InterruptedException |  ExecutionException e) {
                logger.error("Something went wrong converting " + name + " to geotiff");
            } catch (TimeoutException e) {
                logger.error("Call timed out trying to convert " + name + " to geotiff");
            }
        } else
            {
                call = OGR2OGR + destPath + "temp.shp " + sourcePath;
                call = GeodisyStrings.replaceSlashes(call);
                String output;
                try {
                    processCall = new ProcessCall();
                    output = processCall.runProcess(call,20,TimeUnit.SECONDS,logger)[0];
                    if(output.contains("FAILURE"))
                        return false;
                    if(name.toLowerCase().endsWith(".shp")) {
                        File[] files = new File(destPath).listFiles();
                        Set<String> extensions = new HashSet<>();
                        for(File f: files){
                            String pathName = f.getName();
                            String fileName = pathName.substring(pathName.lastIndexOf(GeodisyStrings.replaceSlashes("/"))+1);
                            if(fileName.startsWith("temp."))
                                extensions.add(fileName.substring(fileName.lastIndexOf(".")));
                        }
                        for(String s: extensions){
                            File tempFile = new File(destPath+nameStub+s);
                            if(tempFile.exists())
                                Files.deleteIfExists(tempFile.toPath());
                        }
                        files = new File(destPath).listFiles();
                        //Postgres maximum table name length is 63 characters
                        for(File f: files){
                            String fileName = f.getName();
                            if(fileName.startsWith("temp.")){
                                String ext = fileName.substring(fileName.lastIndexOf("."));
                                f.renameTo(new File(destPath+nameStub+ext));
                            }
                        }
                        return true;
                    }

                    File newFile = new File(destPath+"temp.shp");
                    if(newFile.exists()) {
                        if(sourcePath.endsWith(".shp")){
                            String stub = sourcePath.substring(0,sourcePath.indexOf(".shp"));
                            for(String ex: NON_SHP_SHAPEFILE_EXTENSIONS){
                                File file1 = new File(stub+ex);
                                if(file1.exists())
                                    Files.deleteIfExists(file1.toPath());
                            }
                        }
                        file = new File(sourcePath);
                        Files.deleteIfExists(file.toPath());
                        File[] files = new File(destPath).listFiles();
                        for(File f: files){
                            String fileName = f.getName();
                            if(fileName.startsWith("temp.")){
                                fileName = fileName.replace("temp",nameStub);
                                f.renameTo(new File(destPath+fileName));
                            }
                        }
                        return true;
                    }
                } catch (IOException | ExecutionException | InterruptedException e) {
                    logger.error("Something went wrong converting " + name + " to shapefile");
                } catch (TimeoutException e) {
                    logger.error("Timeout when trying to convert " + name + " to shapefile at path:" + destPath);
                }
            }
        file = new File(sourcePath);
        if(!(sourcePath.toLowerCase().endsWith(".tif")||sourcePath.toLowerCase().endsWith(".shp"))) {
            try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                logger.error("Something went wrong trying to delete not TIF or SHP file: " + file.getAbsolutePath());
            }
        }
        return false;
    }




}
