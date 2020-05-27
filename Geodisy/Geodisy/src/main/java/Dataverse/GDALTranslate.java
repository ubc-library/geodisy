package Dataverse;

import BaseFiles.GeoLogger;
import _Strings.GeodisyStrings;


import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import static _Strings.GeodisyStrings.*;

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

    public String vectorTransform(String dirPath, String name){
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

    private boolean process(String dirPath, String name, String transformType, boolean newLocation) {
        return process(dirPath,dirPath,name,transformType, newLocation);
    }
    public boolean process(String sourcePath, String destPath, String name, String transformType, boolean newLocation){

        sourcePath = GeodisyStrings.replaceSlashes(sourcePath);
        destPath = GeodisyStrings.replaceSlashes(destPath);
        destPath = destPath.substring(0,destPath.lastIndexOf(GeodisyStrings.replaceSlashes("/")))+GeodisyStrings.replaceSlashes("/");


        String call;
        String nameStub = name.substring(0,name.lastIndexOf("."));
        File file;

        ProcessBuilder processBuilder= new ProcessBuilder();

        if(transformType.equals(RASTER)) {
            call = GDAL_TRANSLATE + sourcePath + " " + destPath + "temp.tif";
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
                File newFile = new File(destPath + "temp.tif");
                if(newFile.exists()) {
                    file = new File(sourcePath + name);
                    file.delete();
                    newFile.renameTo(new File(destPath + nameStub + ".tif"));
                    return true;
                }else{
                    //System.out.println("Translation failure #" + i);
                }

            } catch (IOException | InterruptedException e) {
                logger.error("Something went wrong converting " + name + " to geotiff");
            }finally{
                if(process!=null)
                    process.destroy();
            }
        } else{
                call = OGR2OGR + destPath + "temp.shp " + sourcePath;
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
                                tempFile.delete();
                        }
                        files = new File(destPath).listFiles();
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
                        file = new File(sourcePath);
                        file.delete();
                        File[] files = new File(destPath).listFiles();
                        for(File f: files){
                            String fileName = f.getName();
                            if(fileName.startsWith("temp.")){
                                fileName.replace("temp",nameStub);
                                f.renameTo(new File(destPath+fileName));
                            }
                        }
                        return true;
                    }else{
                        //System.out.println("Translation failure #" + i);
                    }
                } catch (IOException | InterruptedException e) {
                    logger.error("Something went wrong converting " + name + " to shapefile");
                }
            }
        System.out.println("Couldn't convert file: " + sourcePath);
        file = new File(sourcePath);
        file.delete();
        return false;
    }




}
