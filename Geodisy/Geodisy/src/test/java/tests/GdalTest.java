package tests;

import Dataverse.GDAL;
import Dataverse.GDALTranslate;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static _Strings.GeodisyStrings.*;


public class GdalTest {

    @Test
    public void gdalparseTest() {
        GDAL gdal = new GDAL();
        String filePath = "src/test/UnzipTestDepositLocation/OldStreamsPolyline.shp";
        String name = "OldStreamsPolyline.shp";
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
        String sourcePath = "datasetFiles\\10\\5072\\FK2\\WQLIQD\\";
        String destPath = "testFiles\\10\\5072\\FK2\\WQLIQD\\";
        String name = "GeoTIFF_WQLIQD.geojson";
        String newName = "GeoTIFF_WQLIQD.shp";
        //String name = "precip.mon.mean.nc";
        //String newName = "precip.mon.mean.tif";
        String filePath = "C:\\geodisy\\Geodisy\\Geodisy\\testFiles\\10\\5072\\FK2\\GFCTVC\\precip.mon.mean.tif";
        GDAL gdal = new GDAL();
        GDALTranslate translate = new GDALTranslate();
        File file = new File(destPath);
        file.mkdirs();
        boolean success = false;
        int countGDALInfo = 0;
        int countGDALOrig = 0;
        int countTranslate = 0;
        int origTotal = 100;
        int newTotal = 100;
        String call;
        ProcessBuilder processBuilder = new ProcessBuilder();
        for (int i = 0; i < 500; i++) {
            call = OGR2OGR + destPath + newName + " " + sourcePath + name;
            processBuilder.command("bash", "-c", call);
            processBuilder.redirectErrorStream(true);
            try {
                if (IS_WINDOWS) {
                    Runtime.getRuntime().exec(call).waitFor();

                } else {
                    processBuilder.start();
                }
                File tempfile = new File(destPath+newName);
                if(tempfile.exists()) {
                    countTranslate++;
                    String answer = gdal.getGDALInfo(destPath, newName);
                    if (answer.contains("successful.Layer name:")) {
                        countGDALInfo++;
                    }
                    answer = gdal.getGDALInfo(destPath, name);
                    if (answer.contains("successful.Layer name:")) {
                        countGDALOrig++;
                    }
                    tempfile.delete();
                }else{
                    origTotal--;
                    newTotal--;
                }
            } catch (IOException | InterruptedException e) {
                System.out.println(e);
            }
        }
        System.out.println("Translate Success " + countTranslate + "/10");
        System.out.println("New Read Success " + countGDALInfo + "/" + newTotal);
        System.out.println("Old Read Success " + countGDALOrig + "/" + origTotal);

    }
}