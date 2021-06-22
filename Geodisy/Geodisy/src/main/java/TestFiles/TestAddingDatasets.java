package TestFiles;

import Dataverse.DataverseJavaObject;
import GeoServer.GeoServerAPI;

public class TestAddingDatasets implements Test{
    @Override
    public void run() {

        DataverseJavaObject djo = new DataverseJavaObject("test");
        djo.setPID("http://hdl.handle.net/11272.1/AB2/RAMZ8A");
        GeoServerAPI g = new GeoServerAPI(djo);
        g.addRaster("F1.tif", "r0000009999");
    }
}
