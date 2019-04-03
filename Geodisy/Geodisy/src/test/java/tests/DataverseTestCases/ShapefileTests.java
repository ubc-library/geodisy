package tests.DataverseTestCases;

import Dataverse.DataverseJSONFieldClasses.DVFieldNames;
import Dataverse.DataverseJavaObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShapefileTests extends DataverseTests{

    @Test
    public void shapefileTest(){
        doi = "doi:10.5072/FK2/QZIPVK";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"BC Transit Routes for Victoria, Whistler, Pemberton Local and Commuter, Squamish Commuter, Kelowna and Kamloops, 21 March 2013");
    }

    @Test
    public void shapeCross180WGS84Test(){
        doi = "doi:10.5072/FK2/GCWZZ1";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"Shapefile crossing over 180 degrees west long with WGS84 Pseudo-Mercator projection");
    }

    @Test
    public void shapeCross180StandTest(){
        doi = "doi:10.5072/FK2/1TUKUB";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"Shapefile crossing over 180 degrees west long with standard projection");
    }

    @Test
    public void shapeCross180CustomTest() {
        doi = "doi:10.5072/FK2/VWSCHA";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"Shapefile with crossing over 180 degree longitude using custom projection");
    }

    @Test
    public void shapeCJKTest(){
        doi = "doi:10.5072/FK2/TGMXVG";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"Shapefile with CJK characters in attribute table");
    }

    @Test
    public void shapeNAD1983(){
        doi = "doi:10.5072/FK2/8EB4HR";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"Shapefile with NAD 1983 BC Environment Albers projection");
    }
    //TODO still need to add doi
    /*@Test
    public void shapeWithXML(){
        doi = "NEED TO FIND";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }*/
}
