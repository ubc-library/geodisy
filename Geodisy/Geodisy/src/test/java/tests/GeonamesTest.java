package tests;

import Dataverse.FindingBoundingBoxes.Geonames;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GeonamesTest {
    Geonames geonames = new Geonames();
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

    //Country and State
    @Test
    void getDVBoundingBox1() {
        BoundingBox bb = geonames.getDVBoundingBox("Spain","Valencia");
        assertEquals(bb.getLatNorth(),40.78863);
    }

    //Country, State, City
    @Test
    void getDVBoundingBox2() {
        BoundingBox bb =geonames.getDVBoundingBox("Canada", "British Columbia", "Courtenay");
        assertEquals(bb.getLatSouth(),49.64722);
    }

    //Country, State, City, Other
    @Test
    void getDVBoundingBox3() {
        BoundingBox bb = geonames.getDVBoundingBoxOther("United States", "Arches National Park");
        assertEquals(bb.getLongEast(),-66.949607);
    }


    @Test
    void getHttpURLConnection() {
        HashMap<String,String> parameters = new HashMap<>();

    }
}