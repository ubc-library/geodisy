package TestFiles;

import BaseFiles.GeoLogger;
import BaseFiles.ProcessCall;
import Dataverse.DataverseJavaObject;
import Dataverse.FRDRAPI;
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
        djo.setPID("http://hdl.handke.net/11272.1/AB2/CFHEOW");
        PostGIS p = new PostGIS();
        p.addFile2PostGIS(djo,"2011AgriCensusBoundary___gcar000a11a_e.shp", "v0000001666");
        ProcessCall pc = new ProcessCall();
        String s = "curl -u admin:YsHtK5XyuCNWRA9j -XPOST -H \"Content-type: text/xml\" -d \"<featureType><name>v0000001666</name><title>2011AgriCensusBoundary___gcar000a11a_e</title><nativeCRS>EPSG:4326</nativeCRS><srs>EPSG:4326</srs><enabled>true</enabled></featureType>\" http://prod-gs-g1:8080/geoserver/rest/workspaces/geodisy/datastores/vectordata/featuretypes";
        GeoLogger logger = new GeoLogger(this.getClass());
        try {
            System.out.println("Start");
            System.out.println(pc.runProcess(s,30, TimeUnit.SECONDS,logger));
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}