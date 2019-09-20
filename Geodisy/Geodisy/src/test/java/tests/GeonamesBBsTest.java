package tests;

import Dataverse.DataverseJavaObject;
import Dataverse.FindingBoundingBoxes.GeonamesBBs;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GeonamesBBsTest {
    GeonamesBBs geonames = new GeonamesBBs(new DataverseJavaObject("fakeServer"));
    private String USER_NAME = "geodisy";
    HashMap<String, String> parameters;


    @BeforeEach
    void setUp() {
        parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");


    }

    @Test
    void readResponse() {
    }

    @Test
    void parseXMLString() {
    }

    //Country Only
    @Test
    void getDVBoundingBox() {
        BoundingBox bb = geonames.getDVBoundingBox("France");
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

    //Country, Province, City, Other
    @Test
    void getDVBoundingBox3() {
        BoundingBox bb = geonames.getDVBoundingBoxOther("United States", "Arches National Park");
        assertEquals(bb.getLongEast(),361);
    }


    @Test
    void getHttpURLConnection() {
        HashMap<String,String> parameters = new HashMap<>();

    }
}