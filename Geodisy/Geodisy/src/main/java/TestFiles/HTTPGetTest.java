package TestFiles;

import BaseFiles.HTTPGetCall;
import Dataverse.DataverseRecordFile;
import _Strings.GeodisyStrings;

import java.util.LinkedList;

public class HTTPGetTest {
    public void run(){
        HTTPGetCall call = new HTTPGetCall();
        /*call.getFile("https://data.montreal.ca/dataset/fab160ae-c81d-46f8-8f92-4a01c10d4390/resource/af110ab4-03fe-4d2a-82b7-11220ec419c4/download/batiment_2d_2016_ac.zip","unknown", GeodisyStrings.dataDir()+"test/");*/
        //call.getFile("https://abacus.library.ubc.ca/api/access/datafile/64388","unknown",GeodisyStrings.dataDir()+"test/");
        LinkedList<DataverseRecordFile> files =  new LinkedList<>();
        files.add(new DataverseRecordFile("a1","aa11","https://data.montreal.ca/dataset/fab160ae-c81d-46f8-8f92-4a01c10d4390/resource/af110ab4-03fe-4d2a-82b7-11220ec419c4/download/batiment_2d_2016_ac.zip"));
        files.add(new DataverseRecordFile("b2","aa11","https://abacus.library.ubc.ca/api/access/datafile/64388"));
        call.checkDataset(files,"aa11");
    }
}
