package TestFiles;

import BaseFiles.HTTPGetCall;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import _Strings.GeodisyStrings;

import java.util.LinkedList;

public class HTTPGetTest implements Test{
    public void run(){
        HTTPGetCall call = new HTTPGetCall();
        /*call.getFile("https://www150.statcan.gc.ca/n1/tbl/sdmx/17100109-SDMX.zip","36100051-SDMX.zip", GeodisyStrings.dataDir()+"test/");*/
        //call.getFile("https://abacus.library.ubc.ca/api/access/datafile/64388","unknown",GeodisyStrings.dataDir()+"test/");
        /*LinkedList<DataverseRecordFile> files =  new LinkedList<>();
        files.add(new DataverseRecordFile("a1","aa11","https://data.montreal.ca/dataset/fab160ae-c81d-46f8-8f92-4a01c10d4390/resource/af110ab4-03fe-4d2a-82b7-11220ec419c4/download/batiment_2d_2016_ac.zip"));
        files.add(new DataverseRecordFile("b2","aa11","https://abacus.library.ubc.ca/api/access/datafile/64388"));
        call.checkDataset(files,"aa11");*/
        DataverseRecordFile drf = new DataverseRecordFile("CARL CRKN 2016-17 LICENSING COSTS - INSTITUTION DATA.csv","https://doi.org/10.20383/101.033","https://g-624536.53220.5898.data.globus.org/1/published/publication_33/submitted_data/CARL%20CRKN%202016-17%20LICENSING%20COSTS%20-%20INSTITUTION%20DATA.csv");
        drf.setOriginalTitle(drf.getTranslatedTitle());
        drf.setFileName(drf.getTranslatedTitle());
        DataverseJavaObject djo  = new DataverseJavaObject("test");
        djo.setPID("1GBTest");
        LinkedList list = new LinkedList();
        list.add(drf);
        djo.setDataFiles(list);
        drf.retrieveFile(djo);
    }
}
