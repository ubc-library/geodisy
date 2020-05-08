package tests.DataverseTestCases;

import Strings.DVFieldNameStrings;

import Dataverse.SourceJavaObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GeoJSONTests extends DataverseTests {

    @Test
    public void geoJSONPoints(){
        doi = "doi:10.5072/FK2/ZWAV7Z";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"GeoJSON feature collection of points");
    }

    @Test
    public void geoJSONPointsLinesPolyTest(){
        doi = "doi:10.5072/FK2/73OWOX";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"GeoJSON feature collection of points and lines and polygons");
    }

    @Test
    public void geoJSONPoly(){
        doi = "doi:10.5072/FK2/2KQIL0";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"GeoJSON polygon features");
    }

    @Test
    public void geoJSONNonWG84(){
        doi = "doi:10.5072/FK2/LFZMTA";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"GeoJSON in EPSG:5179 Korea 2000");
    }
}
