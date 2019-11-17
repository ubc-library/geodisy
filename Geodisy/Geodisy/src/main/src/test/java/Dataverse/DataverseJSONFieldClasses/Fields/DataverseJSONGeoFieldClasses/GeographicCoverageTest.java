package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.FindingBoundingBoxes.Countries;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Dataverse.DVFieldNameStrings.*;
import static org.junit.jupiter.api.Assertions.*;

class GeographicCoverageTest {
    GeographicCoverage gc;
    @BeforeEach
    void setUp() {
        gc = new GeographicCoverage("fake doi");
        gc.setGivenCountry("France");
    }


    @Test
    void setGivenCountry() {
        gc.setGivenCountry("Canada");
        Assertions.assertTrue(gc.getField(GIVEN_COUNTRY).equals("Canada"));
    }

    @Test
    void setGivenProvince() {
        gc.setGivenProvince("British Columbia");
        Assertions.assertTrue(gc.getField(GIVEN_PROVINCE).equals("British Columbia"));
    }

    @Test
    void setGivenCity() {
        gc.setGivenCity("Paris");
        Assertions.assertTrue(gc.getField(GIVEN_CITY).equals("Paris"));
    }

    @Test
    void getField() {
        assertEquals(gc.getField(GIVEN_COUNTRY),"France");
    }

    @Test
    void getBoundingBox() {
        BoundingBox bb = gc.getBoundingBox();
        Assertions.assertTrue(bb.equals(Countries.getCountry().getCountryByName("France").getBoundingBox()));
    }
}