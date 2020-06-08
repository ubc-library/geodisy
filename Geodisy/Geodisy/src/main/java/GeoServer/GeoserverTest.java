package GeoServer;

import Dataverse.DataverseGeoRecordFile;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJavaObject;
import Dataverse.SourceJavaObject;

public class GeoserverTest {

    public void testAddingARaster(){
        String fileName = "age.tif";
        String doi = "test";
        SourceJavaObject sjo = new DataverseJavaObject("server");
        CitationFields cf = sjo.getCitationFields();
        cf.setDoi(doi);
        sjo.setCitationFields(cf);
        System.out.println(sjo.getDOI() + " = doi from sjo");
        GeoServerAPI geoServerAPI = new GeoServerAPI(sjo);
        DataverseGeoRecordFile dgrf = new DataverseGeoRecordFile(doi,fileName);
        geoServerAPI.addRaster(dgrf);
    }
}
