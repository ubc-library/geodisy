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
        /*FRDRAPI api = new FRDRAPI();
        api.callFRDRHarvester(true);*/
        DataverseJavaObject djo = new DataverseJavaObject("test");
        djo.setPID("http://hdl.handle.net/11272.1/AB2/CGHEOW");
        GeoServerAPI g = new GeoServerAPI(djo);
        g.addVector("2011AgriCensusBoundary___gcar000a11a_e.shp", "v0000001666");
    }
}
