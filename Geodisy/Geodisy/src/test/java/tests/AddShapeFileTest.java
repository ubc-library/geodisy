package tests;

import Dataverse.DataverseJavaObject;
import GeoServer.GeoServerAPI;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class AddShapeFileTest {

    @Test
    public void addShapeFileFromPostGIS(){
        String fileName = "vectortest";
        String geoserverName = "testVectorAdd";
        GeoServerAPI geo = new GeoServerAPI(new DataverseJavaObject("server"));
        Assert.assertTrue(geo.addVector(fileName, geoserverName));
    }
}
