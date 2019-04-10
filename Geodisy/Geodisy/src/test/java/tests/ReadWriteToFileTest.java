package tests;

import BaseFiles.ExistingSearchesFile;
import Dataverse.ExistingSearches;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.junit.Test;

import java.io.*;
import java.time.LocalDateTime;

public class ReadWriteToFileTest {

    @Test
    public void getFileTest(){
        try {
            ExistingSearchesFile eSF = new ExistingSearchesFile("ExistingRecordsTest.txt");
            ExistingSearches es = eSF.readExistingSearches();
            int i = 1;//
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void writeFileTest(){
        ExistingSearchesFile eSF = new ExistingSearchesFile("ExistingRecordsTest.txt");
        ExistingSearches es =  ExistingSearches.getExistingSearches();
        es.addBBox("test"+ LocalDateTime.now(),new BoundingBox());
        try {
            eSF.writeExistingSearches(es);
            ExistingSearches es2 = eSF.readExistingSearches();
            int i =1;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
