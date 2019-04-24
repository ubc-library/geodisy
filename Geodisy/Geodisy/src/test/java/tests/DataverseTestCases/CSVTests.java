package tests.DataverseTestCases;

import Dataverse.DVFieldNameStrings;
import Dataverse.DataverseJavaObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CSVTests extends DataverseTests {

    @Test
    public void CSVNonGeo(){
        doi = "doi:10.5072/FK2/VLOTSI";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"CSV file that is not geospatial data");
    }

    //TODO this seems like an invalid doi
    /*@Test
    public void CSVCityNames(){
        doi = "206-12-90-131.cloud.computecanada.ca/dataverse/bam";
        DataverseJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/
}
