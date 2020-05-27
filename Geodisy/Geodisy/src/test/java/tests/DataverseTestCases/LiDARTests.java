package tests.DataverseTestCases;

import _Strings.DVFieldNameStrings;

import Dataverse.SourceJavaObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LiDARTests extends DataverseTests{

    @Test
    public void liDATACV(){
        doi = "doi:10.5072/FK2/JSE79W";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"CITY OF SURREY. 2013 BARE EARTH DIGITAL ELEVATION MODEL");
    }
    //TODO still need to add doi's
    /*@Test
    public void liDARLAS(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }

    @Test
    public void liDARXYZ(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/
}
