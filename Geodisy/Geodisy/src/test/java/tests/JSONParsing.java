package tests;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.GDAL;
import _Strings.GeodisyStrings;
import org.json.JSONObject;

import org.junit.jupiter.api.Test;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static _Strings.GeodisyStrings.TEST;


public class JSONParsing {

    @Test
    void parseJSON() {
        File file = new File("D:\\Work\\Geodisy\\Geodisy\\geodisyFiles\\TestGBL.json");
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(file.getPath())));

            JSONObject jo = new JSONObject(content);
            JSONObject jo2 = jo.getJSONObject("dc_type_s");
            System.out.println("test");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testTest(){
        TEST = true;
        GeodisyStrings.load();
        String be = GeodisyStrings.BACKEND_ADDRESS;

    }
    
}
