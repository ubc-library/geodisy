package tests.DataverseTestCases;

import _Strings.DVFieldNameStrings;
import Dataverse.SourceJavaObject;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CSVTests extends DataverseTests {

    @Test
    public void CSVNonGeo(){
        doi = "doi:10.5072/FK2/VLOTSI";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"CSV file that is not geospatial data");
    }

    /*@Test
    public void CSVCityNames(){
        doi = "206-12-90-131.cloud.computecanada.ca/dataverse/bam";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleCitationFields().getField(DVFieldNameStrings.TITLE),"not entered yet");
    }*/
}
