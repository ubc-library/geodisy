package BaseFiles;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Dataverse.DataverseGeoRecordFile;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJavaObject;
import Dataverse.SourceJavaObject;
import GeoServer.GeoServerAPI;

import static _Strings.DVFieldNameStrings.RECORD_URL;
import static _Strings.GeodisyStrings.*;

/**
 *
 * @author pdante
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Run the below to remove a single records
        //RemoveRecord rr = new RemoveRecord();
        //rr.removeRecord();
        String dev;
        if(args.length>0)
            TEST = true;
        else
            TEST = false;
        if(TEST)
            dev = "Using the dev servers, is this correct?";
        else
            dev = "Using the prod servers, is this correct?";
        System.out.println(dev);
       GeodisyTask geodisyTask = new GeodisyTask();
        geodisyTask.run();

        /*System.out.println("Running a test on Raster Upload");
        testRasterUploadToGeoserver();*/
        /*GeoCombine geoCombine = new GeoCombine();
        geoCombine.index();*/

        //Run the below solo to download
        /*DownloadRecord downloadRecord = new DownloadRecord();
        downloadRecord.run("doi:10.5072/FK2/KZRG9F");*/


    }

    private static void testRasterUploadToGeoserver() {
        SourceJavaObject sjo = new DataverseJavaObject(SCHOLARS_PORTAL);
        CitationFields cf = sjo.getCitationFields();
        SimpleCitationFields sf = cf.getSimpleCitationFields();
        sf.setField(RECORD_URL,"10.5683/SP2/UEJGTV");
        cf.setSimpleCitationFields(sf);
        sjo.setCitationFields(cf);
        DataverseGeoRecordFile dgrf = new DataverseGeoRecordFile();
        dgrf.setOriginalTitle("VanOldStreamOriginalMapScan.zip");
        dgrf.setTranslatedTitle("VanOldStreamOriginalMapScan.tif");
        dgrf.setGeometryType(RASTER);
        dgrf.setGeoserverLabel("g_10_5683_SP2_UEJGTV");
        dgrf.setIsFromFile(true);
        GeoServerAPI geo = new GeoServerAPI(sjo);
        geo.addRaster(dgrf);
    }

}
