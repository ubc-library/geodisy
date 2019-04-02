package tests.DataverseTestCases;

import Dataverse.DataverseJSONFieldClasses.DVFieldNames;
import Dataverse.DataverseJavaObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StraightMetadataTests extends DataverseTests{

    @Test
    public void metadataBoundingBox(){
        doi = "10.5072/FK2/U174JA";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }
    @Test
    public void metadataCityStateCountry(){
        doi = "doi:10.5072/FK2/U174JA";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");

    }

    @Test
    public void metadataStateCountry(){
        doi = "doi:10.5072/FK2/8O2NUZ";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }
    //TODO need to get a doi
    /*@Test
    public void metadataCountry(){
        doi = "need to add still";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }*/

    @Test
    public void metadataAddress(){
        doi = "doi:10.5072/FK2/OOG4RY";
    }

    //TODO need to get a doi
    /*@Test
    public void flawedOrMinMetadata(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }*/

    //TODO need to get a doi
    /*@Test
    public void goodMetadata(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNames.TITLE),"not entered yet");
    }*/
}
