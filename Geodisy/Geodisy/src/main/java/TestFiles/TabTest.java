package TestFiles;

import GeoServer.FolderFileParser;

import java.io.File;

public class TabTest implements Test {

    @Override
    public void run(){
        FolderFileParser ffp = new FolderFileParser();
        ffp.convertFromTabToCSV(new File("D:\\Work\\Geodisy\\Geodisy\\geodisyFiles\\cs-meq-mapinfotab___CS_ANG.tab"),"D:\\Work\\Geodisy\\Geodisy\\geodisyFiles\\","cs-meq-mapinfotab___CS_ANG");
    }

}
