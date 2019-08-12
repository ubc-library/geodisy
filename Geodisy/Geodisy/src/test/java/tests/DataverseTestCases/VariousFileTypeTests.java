package tests.DataverseTestCases;

import Dataverse.DVFieldNameStrings;
import Dataverse.DataverseJavaObject;
import Dataverse.SourceJavaObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class VariousFileTypeTests extends DataverseTests {
    
    @Test
    public void geoTiffTest(){
        doi = "doi:10.5072/FK2/WQLIQD";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"Point Roberts OE N, WA Orthophoto");
    }
    //TODO need to add doi's
   /* @Test
    public void netCDF(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/

    @Test
    public void ECW(){
        doi = "doi:10.5072/FK2/ISX6U3";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"City of Vancouver - 2006 Orthophoto imagery section 06-07-LM");
    }

    @Test
    public void mrSID(){
        doi = "doi:10.5072/FK2/44IRIG";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"City of Vancouver 2006 Orthophoto imagery section N13");
    }
    //TODO need to add doi's
    /*@Test
    public void jPEG2000(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/

    @Test
    public void KMLWGS84(){
        doi = "doi:10.5072/FK2/5KNT6U";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"KML file in WGS84 Pseudo Mercator EPSG:3857");
    }

    @Test
    public void KMZWGS84(){
        doi = "doi:10.5072/FK2/GVYQRL";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"KMZ file in WGS84 Pseudo Mercator EPSG:3857");
    }
    //TODO need to add doi's
    /*@Test
    public void autoCAD(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/

    @Test
    public void spatialite(){
        doi = "doi:10.5072/FK2/LICNGT";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"Spatialite polygon data set (one layer) in UTM 10N EPSG:26910 projection");
    }
    //TODO still need to add doi's
    /*@Test
    public void tIGER(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }

    @Test
    public void mapInfoTAB(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }

    @Test
    public void geopackage(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }

    @Test
    public void wellKnownText(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }

    @Test
    public void chineseChars(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }

    @Test
    public void mapTileSet(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/
}