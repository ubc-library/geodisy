package tests.DataverseTestCases;

import _Strings.DVFieldNameStrings;
import Dataverse.SourceJavaObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorTests extends DataverseTests{

    //TODO still need to add doi's
    @Test
    public void lineVector(){
        doi = "doi:10.5072/FK2/ZWAV7Z";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getBoundingBox().getLongWest(),Double.parseDouble("-148.0229187011718750"),13);
    }

    @Test
    public void pointVector(){
        doi = "doi:10.5072/FK2/73OWOX";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getBoundingBox().getLatNorth(),Double.parseDouble("83.6769430484155379"),13);
    }

    @Test
    public void polygonVector(){
        doi = "doi:10.5072/FK2/2KQIL0";
        SourceJavaObject val = dataverseCallTest();
        assertEquals(val.getSimpleFields().getField(DVFieldNameStrings.TITLE),"GeoJSON polygon features");
    }
}
