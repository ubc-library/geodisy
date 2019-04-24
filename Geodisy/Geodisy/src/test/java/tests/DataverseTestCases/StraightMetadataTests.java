package tests.DataverseTestCases;

import Dataverse.DVFieldNameStrings;
import Dataverse.DataverseJavaObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StraightMetadataTests extends DataverseTests{

    @Test
    public void metadataBoundingBox(){
        doi = "doi:10.5072/FK2/U174JA";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"UBC Research Data Management Survey: Humanities and Social Sciences");
    }
    @Test
    public void metadataCityStateCountry(){
        doi = "doi:10.5072/FK2/U174JA";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"UBC Research Data Management Survey: Humanities and Social Sciences");

    }

    @Test
    public void metadataStateCountry(){
        doi = "doi:10.5072/FK2/8O2NUZ";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"Forum Research Political Poll – Municipal Issues (Toronto) 2013");
    }
    //TODO need to get a doi
    /*@Test
    public void metadataCountry(){
        doi = "need to add still";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/

    @Test
    public void metadataAddress(){
        doi = "doi:10.5072/FK2/OOG4RY";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"Pierson v. Post Judgment Roll");
    }

    //TODO need to get a doi
    /*@Test
    public void flawedOrMinMetadata(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/

    //TODO need to get a doi
    /*@Test
    public void goodMetadata(){
        doi = "still need to add";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/
}
