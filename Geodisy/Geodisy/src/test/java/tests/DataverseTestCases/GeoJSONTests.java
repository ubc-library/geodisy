package tests.DataverseTestCases;

import Dataverse.DataverseJSONFieldClasses.DVFieldNames;
import Dataverse.DataverseJavaObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeoJSONTests extends DataverseTests {

    @Test
    public void geoJSONPoints(){
        doi = "doi:10.5072/FK2/ZWAV7Z";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"GeoJSON feature collection of points");
    }

    @Test
    public void geoJSONPointsLinesPolyTest(){
        doi = "doi:10.5072/FK2/73OWOX";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"GeoJSON feature collection of points and lines and polygons");
    }

    @Test
    public void geoJSONPoly(){
        doi = "doi:10.5072/FK2/2KQIL0";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"GeoJSON polygon features");
    }

    @Test
    public void geoJSONNonWG84(){
        doi = "doi:10.5072/FK2/LFZMTA";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"GeoJSON in EPSG:5179 Korea 2000");
    }
}
