package Dataverse;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.City;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import Dataverse.FindingBoundingBoxes.LocationTypes.Province;
import GeoServer.Unzip;
import _Strings.GeodisyStrings;

import java.io.File;
import java.util.LinkedList;

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
        String sourcePath = GeodisyStrings.replaceSlashes( "D:/geodata/geoserver/data/doi/10/5683/SP2/KITZQK/AB.zip");
        String destPath = GeodisyStrings.replaceSlashes("D:/geodata/geoserver/data/doi/10/5683/SP2/KITZQK/");
        Unzip unzip = new Unzip();
        DataverseRecordFile drf =  new DataverseRecordFile();
        drf.setOriginalTitle("AB.zip");
        drf.setTranslatedTitle("AB.zip");
        drf.setDbID(1);
        DataverseJavaObject djo = new DataverseJavaObject("fake");
        LinkedList<DataverseRecordFile> list = unzip.unzip(sourcePath,destPath,drf,djo);
        for (DataverseRecordFile dRF: list){
            DataverseGeoRecordFile dgrf = new DataverseGeoRecordFile(dRF);
            GDAL gdal = new GDAL();
            GeographicBoundingBox gbb = gdal.generateBB(new File(destPath+dRF.getTranslatedTitle()), "testDOI","1");
        }
    }

    public void testLocation(){
        Country country = new Country("United States of America");
        country = new Country("Canada");
        Province province = new Province("British Columbia", "Canada");
        province = new Province("Washington","United States of America");
        City city = new City("Seattle","Washington","United States of America");
        country = new Country("United States of America");
        province = new Province("British Columbia", "Canada");
        city = new City("Bethesda","Maryland","United States of America");
        province = new Province("British Columbia", "Canada");
        province = new Province("British Columbia", "Canada");
        country = new Country("United States of America");
    }

}
