package tests;

import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import GeoServer.Unzip;
import org.junit.jupiter.api.Test;

public class UnzipTest {
    @Test
    public void unzipTest() {
        Unzip zip = new Unzip();
        DataverseRecordFile drf = new DataverseRecordFile();
        drf.setOriginalTitle("OldPoly.zip");
        zip.unzip("geodisyFiles/OldPoly.zip","src/test/UnzipTestDepositLocation",drf);
        
    }
}
