package tests.DataverseTestCases;

import org.junit.Test;

public class CSVTests {
    String doi;

    @Test
    public void CSVNonGeo(){
        doi = "10.5072/FK2/VLOTSI";
    }

    @Test
    public void CSVCityNames(){
        doi = "206-12-90-131.cloud.computecanada.ca/dataverse/bam";
    }
}
