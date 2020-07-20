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
import FixScripts.FixGeoserverFiles;
import GeoServer.GeoServerAPI;
import GeoServer.GeoserverTest;
import _Strings.GeodisyStrings;

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
        geodisyTask.run();

        /*System.out.println("Running a test on Raster Upload");
        GeoserverTest gt = new GeoserverTest();
        gt.testAddingARaster();
        gt.testAddingAVector();*/


        /*GeoCombine geoCombine = new GeoCombine();
        geoCombine.index();*/

        //Run the below solo to download
        /*DownloadRecord downloadRecord = new DownloadRecord();
        downloadRecord.run("doi:10.5072/FK2/KZRG9F");*/


    }

}
