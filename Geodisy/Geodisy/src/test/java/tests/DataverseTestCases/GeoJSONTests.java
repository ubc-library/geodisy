package tests.DataverseTestCases;

import org.junit.Test;

public class GeoJSONTests {
    String doi;

    @Test
    public void geoJSONPoints(){
        doi = "10.5072/FK2/ZWAV7Z";
    }

    @Test
    public void geoJSONPointsLinesPolyTest(){
        doi = "10.5072/FK2/73OWOX";
    }

    @Test
    public void geoJSONPoly(){
        doi = "10.5072/FK2/2KQIL0";
    }

    @Test
    public void geoJSONNonWG84(){
        doi = "10.5072/FK2/LFZMTA";
    }
}
