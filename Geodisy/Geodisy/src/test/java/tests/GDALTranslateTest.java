package tests;

import Dataverse.GDAL;
import Dataverse.GDALTranslate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static _Strings.GeodisyStrings.OGR2OGR;

public class GDALTranslateTest {
    @BeforeEach
    void setup(){
        String[] exts = {".prj",".sbn",".shp",".shx"};
        String source = "src/test/UnzipTestDepositLocation/OldStreamsPolyline - Copy";
        String destPath = "src/test/UnzipTestDepositLocation/OldStreamsPolyline ";
        for(String s: exts){
            File sourceFile = new File(source+s);
            File destFile = new File(destPath+s);
            try {
                Files.copy(sourceFile.toPath(),destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void utm2LatLongTest(){
        String baseCall = OGR2OGR;
        String source = "src/test/UnzipTestDepositLocation/OldStreamsPolyline.shp";
        String destPath = "src/test/UnzipTestDepositLocation/OldStreamsPolyline.shp ";
        String name = "OldStreamsPolyline.shp";
        GDALTranslate trans =  new GDALTranslate();
        trans.vectorTransform(source,name);
        /*try {
            Runtime.getRuntime().exec(baseCall + destPath + source).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        GDAL gdal = new GDAL();
        String filePath = destPath;
        try {
            String answer = gdal.getGDALInfo(filePath, name);
            System.out.println(answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
