package tests.DataverseTestCases;

import org.junit.Test;

public class ShapefileTests {
    String doi;

    @Test
    public void shapefileTest(){
        doi = "10.5072/FK2/QZIPVK";
    }

    @Test
    public void shapeCross180WGS84Test(){
        doi = "10.5072/FK2/GCWZZ1";
    }

    @Test
    public void shapeCross180StandTest(){
        doi = "10.5072/FK2/1TUKUB";
    }

    @Test
    public void shapeCross180CustomTest() {
        doi = "10.5072/FK2/VWSCHA";
    }

    @Test
    public void shapeCJKTest(){
        doi = "10.5072/FK2/TGMXVG";
    }

    @Test
    public void shapeNAD1983(){
        doi = "10.5072/FK2/8EB4HR";
    }

    @Test
    public void shapeWithXML(){
        doi = "NEED TO FIND";
    }
}
