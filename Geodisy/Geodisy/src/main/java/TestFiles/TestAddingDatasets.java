package TestFiles;

import BaseFiles.GeoLogger;
import BaseFiles.ProcessCall;
import Dataverse.DataverseJavaObject;
import Dataverse.FRDRAPI;
import GeoServer.GeoServerAPI;
import GeoServer.PostGIS;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestAddingDatasets implements Test{
    @Override
    public void run() {

        DataverseJavaObject djo = new DataverseJavaObject("test");
        djo.setPID("http://hdl.handle.net/11272.1/AB2/RAMZ8A");
        GeoServerAPI g = new GeoServerAPI(djo);
        g.addRaster("F1.tif", "r0000009999");
    }
}
