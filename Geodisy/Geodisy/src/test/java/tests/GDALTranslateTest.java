package tests;

import Dataverse.GDAL;
import Dataverse.GDALTranslate;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static BaseFiles.GeodisyStrings.GDAL_TRANSLATE;
import static BaseFiles.GeodisyStrings.OGR2OGR;

public class GDALTranslateTest {
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
