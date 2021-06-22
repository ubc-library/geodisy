package tests;

import BaseFiles.GeodisyTask;
import Dataverse.DataverseJavaObject;
import Dataverse.ExistingLocations;
import Dataverse.FindingBoundingBoxes.GeonamesBBs;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import _Strings.GeodisyStrings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import static _Strings.GeodisyStrings.EXISTING_LOCATION_BBOXES;
import static org.junit.jupiter.api.Assertions.assertEquals;


class GeonamesBBsTest {
    GeonamesBBs geonames;
    private String USER_NAME = "geodisy";
    HashMap<String, String> parameters;
    GeodisyTask geodisyTask;


    @BeforeEach
    void setUp() {
        GeodisyStrings.load();
        geodisyTask = new GeodisyTask();
        try {
            geodisyTask.loadSavedFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        geonames = new GeonamesBBs(new DataverseJavaObject("fakeServer"));


    }

    @Test
    void readResponse() {

    }

    @Test
    void parseXMLString() {
    }

    @Test
    void updatingValsInExisting__Files(){
        ExistingLocations es = ExistingLocations.getExistingLocations();
        HashMap<String, BoundingBox> locations = es.getBBoxes();
        Set keys = locations.keySet();
        Iterator<String> it = keys.iterator();
        HashMap<String,BoundingBox> fixedLocations = new HashMap<>();
        LinkedList<String> badLocations = new LinkedList<>();
        while (it.hasNext()){
            String key = it.next();
           BoundingBox bb = locations.get(key);
           if(!key.startsWith("Viet") && bb.getLongWest()==104.37915 && bb.getLongEast()==106.07916)
               badLocations.add(key);
           else
               fixedLocations.put(key, bb);
        }
        System.out.println(fixedLocations.size());
        System.out.println(badLocations.size());
        //es.setbBoxes(fixedLocations);
        //es.saveExistingSearchs(fixedLocations,EXISTING_LOCATION_BBOXES, ExistingLocations.class.getName());

    }

    @Test
    void checkExistingLocations(){
        ExistingLocations existingLocations = ExistingLocations.getExistingLocations();
        Set<String> keys = existingLocations.getBBoxes().keySet();
        HashMap<String, BoundingBox> questionable = new HashMap<>();
        for(String k: keys){
            BoundingBox b = existingLocations.getBB(k);
            double west = b.getLongWest();
            if(west == 174.67517)
                questionable.put(k,b);
        }
        keys = questionable.keySet();
        /*for(String k:keys){
            if(k.equals("")||k.equals("zzz"))
                continue;
            existingLocations.deleteBBox(k);
        }
        existingLocations.saveExistingSearchs(existingLocations.getBBoxes(),EXISTING_LOCATION_BBOXES,ExistingLocations.class.toString());*/
        System.out.println("Fin");
    }
    //Country Only
    @Test
    void getDVBoundingBox() {
        BoundingBox bb = geonames.getDVBoundingBox("Canada");
        bb = geonames.getDVBoundingBox("Swaziland");
        assertEquals(bb.getLongEast(),9.5596148665824);
    }

    //Country and Province
    @Test
    void getDVBoundingBox1() {
        BoundingBox bb = geonames.getDVBoundingBox("Spain","Valencia");
        assertEquals(bb.getLatNorth(),40.78863);
    }

    //Country, Province, City
    @Test
    void getDVBoundingBox2() {
        BoundingBox bb =geonames.getDVBoundingBox("Canada", "British Columbia", "Courtenay");
        assertEquals(bb.getLatSouth(),49.64722);
    }


    @Test
    void getHttpURLConnection() {
        HashMap<String,String> parameters = new HashMap<>();

    }
}