package tests.DataverseTestCases;

import org.junit.Test;

public class StraightMetadataTests {
    String doi;

    @Test
    public void metadataBoundingBox(){
        doi = "10.5072/FK2/U174JA";
    }
    @Test
    public void metadataCityStateCountry(){
        doi = "10.5072/FK2/U174JA";
    }

    @Test
    public void metadataStateCountry(){
        doi = "10.5072/FK2/8O2NUZ";
    }
    @Test
    public void metadataCountry(){
        doi = "need to add still";
    }

    @Test
    public void metadataAddress(){
        doi = "10.5072/FK2/OOG4RY";
    }

    @Test
    public void flawedOrMinMetadata(){
        doi = "still need to add";
    }

    @Test
    public void goodMetadata(){
        doi = "still need to add";
    }
}
