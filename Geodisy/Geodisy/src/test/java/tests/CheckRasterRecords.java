package tests;

import Dataverse.ExistingRasterRecords;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;

import static BaseFiles.GeodisyStrings.RASTER_RECORDS;

public class CheckRasterRecords {
    @Test
    public void checkRasterRecords(){
        ExistingRasterRecords existingRasterRecords = ExistingRasterRecords.getExistingRasters();
        existingRasterRecords.readExistingRecords(RASTER_RECORDS);
        HashMap<String, String> records = existingRasterRecords.getRecords();
        Set<String> keys =  records.keySet();
        for(String key:keys){
            System.out.println("Key : " + key + ", and filename: " + records.get(key));
        }
    }
}
