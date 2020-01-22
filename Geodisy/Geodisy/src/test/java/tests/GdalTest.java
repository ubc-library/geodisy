package tests;

import Dataverse.GDAL;
import Dataverse.GDALTranslate;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static BaseFiles.GeodisyStrings.*;


public class GdalTest {

    @Test
    public void gdalparseTest() {
        GDAL gdal = new GDAL();
        String filePath = "C:\\geodisy\\Geodisy\\Geodisy\\datasetFiles\\10\\5072\\FK2\\GFCTVC\\NetCDF_GFCTVC.shp";
        String name = "NetCDF_GFCTVC.shp";
        int success = 0;
        for (int i = 0; i < 100; i++) {

            try {
                String answer = gdal.getGDALInfo(filePath, name);
                if (answer.contains("FAILURE"))
                    success++;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Success = " + (100-success) + "/100");
    }

    @Test
    public void gdalTranslateTest() {
        String sourcePath = "datasetFiles\\10\\5072\\FK2\\GFCTVC/";
        String destPath = "testFiles\\10\\5072\\FK2\\GFCTVC/";
        String name = "NetCDF_GFCTVC.shp";
        String newName = "NetCDF_GFCTVC.shp";
        //String name = "precip.mon.mean.nc";
        //String newName = "precip.mon.mean.tif";
        String filePath = "C:\\geodisy\\Geodisy\\Geodisy\\testFiles\\10\\5072\\FK2\\GFCTVC\\precip.mon.mean.tif";
        GDAL gdal = new GDAL();
        GDALTranslate translate = new GDALTranslate();
        File file = new File(destPath);
        file.mkdirs();
        boolean success = false;
        int count = 0;
        String call;
        ProcessBuilder processBuilder = new ProcessBuilder();
        for (int i = 0; i < 10; i++) {
            call = OGR2OGR + destPath + newName + " " + sourcePath + name;
            processBuilder.command("bash", "-c", call);
            try {
                if (IS_WINDOWS) {
                    Runtime.getRuntime().exec(call);
                } else {
                    processBuilder.start();
                }
                String answer = gdal.getGDALInfo(destPath + newName + " ", name);
                if(answer.contains("successful.Layer name:")) {
                    count++;
                }
            } catch (IOException e) {
            }
            System.out.println("Success " + count + "/10");
        }
    }
}