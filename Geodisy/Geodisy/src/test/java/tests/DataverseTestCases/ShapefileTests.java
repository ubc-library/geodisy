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
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void shapeCross180WGS84Test(){
        doi = "doi:10.5072/FK2/GCWZZ1";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void shapeCross180StandTest(){
        doi = "doi:10.5072/FK2/1TUKUB";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void shapeCross180CustomTest() {
        doi = "doi:10.5072/FK2/VWSCHA";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void shapeCJKTest(){
        doi = "doi:10.5072/FK2/TGMXVG";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void shapeNAD1983(){
        doi = "doi:10.5072/FK2/8EB4HR";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }
    //TODO still need to add doi
    /*@Test
    public void shapeWithXML(){
        doi = "NEED TO FIND";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }*/
}
