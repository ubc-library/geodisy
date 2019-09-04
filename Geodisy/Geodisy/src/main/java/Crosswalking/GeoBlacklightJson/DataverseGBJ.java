package Crosswalking.GeoBlacklightJson;

import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class DataverseGBJ extends GeoBlacklightJSON{
    public DataverseGBJ(DataverseJavaObject djo) {
        super();
        javaObject = djo;
        geoBlacklightJson = "";
    }

    @Override
    protected JSONObject getRequiredFields() {
        jo.put("geoblacklight_version","1.0");

        JSONArray ja = new JSONArray();
        ja.put(javaObject.getDOI());
        jo.put("dc_identifier_s",ja);
        if(javaObject.hasGeospatialFile)
            addWebService();

        return jo;
    }

    @Override
    protected JSONObject getOptionalFields() {


        return jo;
    }

    @Override
    protected void addWebService() {
        List<DataverseRecordFile> recordFiles = javaObject.getDataFiles();
        boolean wms = false;
        boolean wfs = false;
        String title;
        for(DataverseRecordFile drf:recordFiles){
            title = drf.getTitle().toLowerCase();
            checkReferences(title);
        }
    }
}
