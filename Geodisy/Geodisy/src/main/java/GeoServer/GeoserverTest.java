package GeoServer;

import Dataverse.DataverseGeoRecordFile;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJavaObject;
import Dataverse.SourceJavaObject;

public class GeoserverTest {

    public void testAddingARaster(){
        String fileName = "age.tif";
        String doi = "rasterTest";
        SourceJavaObject sjo = new DataverseJavaObject("server");
        CitationFields cf = sjo.getCitationFields();
        cf.setPID(doi);
        sjo.setCitationFields(cf);
        GeoServerAPI geoServerAPI = new GeoServerAPI(sjo);
        DataverseGeoRecordFile dgrf = new DataverseGeoRecordFile(doi,fileName);
        geoServerAPI.addRaster(dgrf);
    }

    public void testAddingAVector(){
        String fileName = "states.shp";
        String doi = "vectorTest";
        SourceJavaObject sjo = new DataverseJavaObject("server");
        CitationFields cf = sjo.getCitationFields();
        cf.setPID(doi);
        sjo.setCitationFields(cf);
        GeoServerAPI geoServerAPI = new GeoServerAPI(sjo);
        DataverseGeoRecordFile dgrf = new DataverseGeoRecordFile(doi,fileName);
        geoServerAPI.addVector(fileName,doi);
    }
}
