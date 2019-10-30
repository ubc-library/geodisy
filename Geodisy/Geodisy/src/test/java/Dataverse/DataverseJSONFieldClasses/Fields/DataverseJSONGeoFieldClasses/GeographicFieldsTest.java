package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJavaObject;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeographicFieldsTest {

    @Test
    void setBoundingBox() {
        String fakeDOI = "10.10.10";
        String fakeServer = "noRealServer";
        GeographicFields gf = new GeographicFields(new DataverseJavaObject(fakeServer));
        gf.setDoi(fakeDOI);
        List<GeographicBoundingBox> bboxes = gf.getGeoBBoxes();
        GeographicBoundingBox gBB =  new GeographicBoundingBox(fakeDOI);
        gBB.setEastLongitude(-10);
        gBB.setWestLongitude(-100);
        gBB.setNorthLatitude(24);
        gBB.setSouthLatitude(-18);
        gf.addBB(bboxes,gBB);
        BoundingBox bb = gf.getBoundingBox();
        assertEquals(bb.getLongWest(),-100);
        assertEquals(bb.getLongEast(),-10);
        gBB = new GeographicBoundingBox(fakeDOI);
        gBB.setEastLongitude("20");
        gBB.setWestLongitude("-34");
        gBB.setNorthLatitude("26");
        gBB.setSouthLatitude(-16);
        gf.addBB(bboxes,gBB);
        bb = gf.getBoundingBox();
        assertEquals(bb.getLongWest(),-100);
        assertEquals(bb.getLongEast(),20);
        assertEquals(bb.getLatNorth(),26);
        assertEquals(bb.getLatSouth(),-18);
        gBB = new GeographicBoundingBox(fakeDOI);
        gBB.setEastLongitude("20");
        gBB.setWestLongitude("25");
        gBB.setNorthLatitude("26");
        gBB.setSouthLatitude(-16);
        gf.addBB(bboxes,gBB);
        bb = gf.getBoundingBox();
        assertEquals(bb.getLongWest(),25);
        assertEquals(bb.getLongEast(),20);
        assertEquals(bb.getLatNorth(),26);
        assertEquals(bb.getLatSouth(),-18);
        bboxes = new LinkedList<>();
        gBB =  new GeographicBoundingBox(fakeDOI);
        gBB.setEastLongitude(170);
        gBB.setWestLongitude(160);
        gBB.setNorthLatitude(80);
        gBB.setSouthLatitude(75);
        gf.addBB(bboxes,gBB);
        bb = gf.getBoundingBox();
        assertEquals(bb.getLongWest(),160);
        assertEquals(bb.getLongEast(),170);
        gBB = new GeographicBoundingBox(fakeDOI);
        gBB.setEastLongitude("-140");
        gBB.setWestLongitude("-150");
        gBB.setNorthLatitude("26");
        gBB.setSouthLatitude(-16);
        gf.addBB(bboxes,gBB);
        bb = gf.getBoundingBox();
        assertEquals(bb.getLongWest(),160);
        assertEquals(bb.getLongEast(),-140);
    }

    @Test
    void addBB() {
    }
}