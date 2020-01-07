package tests;

import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJavaObject;
import Dataverse.GDAL;
import Dataverse.GDALTranslate;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static BaseFiles.GeodisyStrings.VECTOR;

public class GdalTest {

    @Test
    public void gdalparseTest() {
        GDAL gdal = new GDAL();
        String filePath = "C:\\geodisy\\Geodisy\\Geodisy\\datasetFiles\\10\\5072\\FK2\\GFCTVC\\NetCDF_GFCTVC.shp";
        String name = "NetCDF_GFCTVC.shp";
        int success = 0;
        for (int i = 0; i < 100; i++) {

            try {
                String answer = gdal.getGDALInfo(filePath, name, true);
                if (answer.contains("successful.Layer name: NetCDF_GFCTVCMetadata"))
                    success++;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Success = " + success + "/100");
    }

    @Test
    public void gdalTranslateTest() {
        String sourcePath = "datasetFiles\\10\\5072\\FK2\\GFCTVC/";
        String destPath = "testFiles\\10\\5072\\FK2\\GFCTVC/";
        /*String name = "NetCDF_GFCTVC.geoJSON";
        String newName = "NetCDF_GFCTVC.shp";*/
        String name = "precip.mon.mean.nc";
        String newName = "precip.mon.mean.tif";
        String filePath = "C:\\geodisy\\Geodisy\\Geodisy\\testFiles\\10\\5072\\FK2\\GFCTVC\\precip.mon.mean.tif";
        GDAL gdal = new GDAL();
        GDALTranslate translate = new GDALTranslate();
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        File file = new File(destPath);
        file.mkdirs();
        boolean success = false;
        int count = 0;
        String call;
        for (int i = 0; i < 10; i++) {
            call = "gdal_translate -of GTiff " + sourcePath + name + " " + destPath + newName;
            try {

                if (isWindows) {
                    Runtime.getRuntime().exec(call);
                } else {
                    Runtime.getRuntime().exec(call);
                }
                String answer = gdal.getGDALInfo(destPath + newName, name, isWindows);
                if (answer.contains("Lower Left( 00,72.0)"))
                    count++;

            } catch (IOException e) {
                System.out.println(e);
            }
            System.out.println("Failed " + count + "/10");
        }
    }
}