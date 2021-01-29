package tests;

import Dataverse.ExistingDatasetBBoxes;
import Dataverse.ExistingSearchesFile;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadWriteToFileTest {
    @Test
    public void getFileTest(){
        try {
            ExistingSearchesFile eSF = new ExistingSearchesFile();
            ExistingDatasetBBoxes es = eSF.readExistingSearches();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void writeFileTest(){
        ExistingSearchesFile eSF = new ExistingSearchesFile("ExistingRecordsTest.txt");
        ExistingDatasetBBoxes es =  ExistingDatasetBBoxes.getExistingHarvests();
        String location = "test"+ LocalDateTime.now();
        es.addBBox(location,new BoundingBox());
        try {
            eSF.writeExistingSearches(es);
            es = eSF.readExistingSearches();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(es.numberOfBBoxes(),1);
        es.deleteBBox(location);
        try{
            eSF.writeExistingSearches(es);
            es = eSF.readExistingSearches();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(es.numberOfBBoxes(),0);
        es = ExistingDatasetBBoxes.getExistingHarvests();
        assertEquals(es.numberOfBBoxes(),0);
    }
}
