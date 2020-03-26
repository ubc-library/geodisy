package tests.DataverseTestCases;

import Dataverse.DVFieldNameStrings;
import Dataverse.SourceJavaObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StraightMetadataTests extends DataverseTests{

    @Test
    public void metadataBoundingBox(){
        doi = "doi:10.5072/FK2/U174JA";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"UBC Research Data Management Survey: Humanities and Social Sciences");
    }
    @Test
    public void metadataCityProvinceCountry(){
        doi = "doi:10.5072/FK2/U174JA";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"UBC Research Data Management Survey: Humanities and Social Sciences");

    }

    @Test
    public void metadataProvinceCountry(){
        doi = "doi:10.5072/FK2/8O2NUZ";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"Forum Research Political Poll – Municipal Issues (Toronto) 2013");
    }
    //TODO need to get a doi
    /*@Test
    public void metadataCountry(){
        doi = "need to add still";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/

    @Test
    public void metadataAddress(){
        doi = "doi:10.5072/FK2/OOG4RY";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"Pierson v. Post Judgment Roll");
    }

    //TODO need to get a doi
    /*@Test
    public void flawedOrMinMetadata(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/

    //TODO need to get a doi
    /*@Test
    public void goodMetadata(){
        doi = "still need to add";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/
}
