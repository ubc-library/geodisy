package GeoServer;

import Dataverse.DataverseGeoRecordFile;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJavaObject;
import Dataverse.SourceJavaObject;

public class GeoserverTest {

    public void testAddingARaster(){
        String fileName = "age";
        String filePath = "/geodata/geoserver/data";
        SourceJavaObject sjo = new DataverseJavaObject("server");
        CitationFields cf = sjo.getCitationFields();
        cf.setDoi("testDOI");
        GeoServerAPI geoServerAPI = new GeoServerAPI(sjo);
        DataverseGeoRecordFile dgrf = new DataverseGeoRecordFile(cf.getDOI(),"temp.tif");
        dgrf.setTranslatedTitle("temp.tif");
        geoServerAPI.addRaster(dgrf);
    }
}
