package BaseFiles;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Crosswalking.GeoBlacklightJson.GeoCombine;
import Dataverse.DataverseGeoRecordFile;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJavaObject;
import Dataverse.GDALTest;
import Dataverse.SourceJavaObject;
import FixScripts.FixGeoserverFiles;
import GeoServer.GeoServerAPI;
import GeoServer.GeoserverTest;
import GeoServer.PostGIS;
import _Strings.GeodisyStrings;

import java.util.ArrayList;
import java.util.List;

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
        GeodisyStrings.load();
        GeodisyTask geodisyTask = new GeodisyTask();
        /*geodisyTask.run();*/
        PostGIS postGIS = new PostGIS();
        DataverseJavaObject djo = new DataverseJavaObject("Fake");
        djo.setPID("https://hdl.handle.net/11272.1/AB2/OFSCDC");
        String geoserverLabel = "v0000000001";
        String fileName = "KelownaRoutes.shp";
        postGIS.addFile2PostGIS(djo,fileName,geoserverLabel);
        GeoServerAPI geoServerAPI = new GeoServerAPI(djo);
        geoServerAPI.addPostGISLayerToGeoserver(geoserverLabel,fileName);

        /*System.out.println("Running a test on Raster Upload");
        GeoserverTest gt = new GeoserverTest();
        gt.testAddingARaster();
        gt.testAddingAVector();*/

        //testCombine(args);

        //Run the below solo to download
        /*DownloadRecord downloadRecord = new DownloadRecord();
        downloadRecord.run("doi:10.5072/FK2/KZRG9F");*/

        /*GDALTest gdalTest = new GDALTest();
        gdalTest.testUnzip();
        System.out.println("Finished unzipping");
        gdalTest.testTransform();*/

    }
    //Only use this method for testing syntax for calling Geocombine
    private static void testCombine(String[] args) {
        List<String> cmdList = new ArrayList<String>();
        cmdList.add("/bin/bash");
        cmdList.add("-c");
        cmdList.add(GEOCOMBINE);
        if(args.length>0) {
            cmdList.clear();
            for(String s:args) {
                cmdList.add(s);
            }
        }
        GeoCombine geoCombine = new GeoCombine();
        geoCombine.combine(cmdList);
    }

}
