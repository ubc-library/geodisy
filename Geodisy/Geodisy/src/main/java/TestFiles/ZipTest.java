package TestFiles;

import GeoServer.Unzip;
import _Strings.GeodisyStrings;

import java.io.File;

public class ZipTest implements Test{

    public void run(){
        String zipPath = "D:\\Work\\Geodisy\\Geodisy\\geodisyFiles\\43100051-eng.zip";/*"D:\\Work\\Geodisy\\Geodisy\\geodisyFiles\\geodisyFiles.zip";*/
        Unzip unzip = new Unzip();
        File file = new File(zipPath);
        unzip.unzipFunction(zipPath,file.getParent());
    }
}
