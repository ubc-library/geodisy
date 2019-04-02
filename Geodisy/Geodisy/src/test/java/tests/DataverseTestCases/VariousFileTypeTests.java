package tests.DataverseTestCases;

import Dataverse.DataverseJSONFieldClasses.DVFieldNames;
import Dataverse.DataverseJavaObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class VariousFileTypeTests extends DataverseTests {
    
    @Test
    public void geoTiffTest(){
        doi = "doi:10.5072/FK2/WQLIQD";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }
    //TODO need to add doi's
   /* @Test
    public void netCDF(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }*/

    @Test
    public void ECW(){
        doi = "doi:10.5072/FK2/ISX6U3";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void mrSID(){
        doi = "doi:10.5072/FK2/44IRIG";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }
    //TODO need to add doi's
    /*@Test
    public void jPEG2000(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }*/

    @Test
    public void KMLWGS84(){
        doi = "doi:10.5072/FK2/5KNT6U";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void KMZWGS84(){
        doi = "doi:10.5072/FK2/GVYQRL";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }
    //TODO need to add doi's
    /*@Test
    public void autoCAD(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }*/

    @Test
    public void spatialite(){
        doi = "doi:10.5072/FK2/LICNGT";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }
    //TODO still need to add doi's
    /*@Test
    public void tIGER(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void mapInfoTAB(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void geopackage(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void wellKnownText(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void chineseChars(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }

    @Test
    public void mapTileSet(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }*/
}