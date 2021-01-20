package Dataverse;

import GeoServer.Unzip;

import static _Strings.GeodisyStrings.VECTOR;

public class GDALTest {

    public void testTransform(){
        String sourcePath = "/geodata/geoserver/data/downloads/hnd/11272/1/AB2/OFSCDC/WhistlerPembertonStops.shp";
        String destPath = "/geodata/geoserver/data/downloads/hnd/11272/1/AB2/OFSCDC/";
        GDALTranslate gdalTranslate = new GDALTranslate();
        String name = "WhistlerPembertonStops.shp";
        gdalTranslate.process(sourcePath,destPath,name,VECTOR,false);
    }

    public void testUnzip(){
        String sourcePath = "/geodata/geoserver/data/downloads/hnd/11272/1/AB2/OFSCDC/WhistlerPembertonStops.zip";
        String destPath = "/geodata/geoserver/data/downloads/hnd/11272/1/AB2/OFSCDC/";
        Unzip unzip = new Unzip();
        DataverseRecordFile drf =  new DataverseRecordFile();
        drf.setOriginalTitle("WhistlerPembertonStops.zip");
        drf.setTranslatedTitle("WhistlerPembertonStops.zip");
        drf.setDbID(1);
        DataverseJavaObject djo = new DataverseJavaObject("fake");
        unzip.unzip(sourcePath,destPath,drf,djo);
    }
}
