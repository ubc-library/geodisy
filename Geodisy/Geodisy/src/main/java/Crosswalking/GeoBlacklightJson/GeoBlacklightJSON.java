package Crosswalking.GeoBlacklightJson;

import Dataverse.SourceJavaObject;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class GeoBlacklightJSON implements JSONCreator {
    protected SourceJavaObject javaObject;
    protected String geoBlacklightJson;
    protected JSONObject jo;
    boolean wms = false;
    boolean wfs = false;
    boolean download = false;

    public GeoBlacklightJSON() {
        this.jo = new JSONObject();
    }
    //TODO finish
    @Override
    public String createJson(){

        return geoBlacklightJson;
    }
    protected boolean addWMS(){
        if(!wms) {
            JSONArray ja = jo.getJSONArray("dc_identifier_s");
            ja.put("http://www.opengis.net/def/serviceType/ogc/wms");
            wms = true;
        }
        return wms;
    }

    protected boolean addWFS(){
        if(!wfs) {
            JSONArray ja = jo.getJSONArray("dc_identifier_s");
            ja.put("http://www.opengis.net/def/serviceType/ogc/wfs");
            wfs = true;
        }
        return wfs;
    }

    protected boolean checkWMS(String title){
        if(title.endsWith("kmz")||title.endsWith("geotiff"))
            return addWMS();
        return false;
    }

    protected boolean checkWFS(String title){
        if(title.endsWith("shp")||title.endsWith("geojson"))
            return addWFS();
        return false;
    }

    protected void checkReferences(String title){
        if(checkWMS(title) || checkWFS(title))
            addDownload();
    }

    protected void addDownload(){
        if(!download) {
            JSONArray ja = jo.getJSONArray("dc_identifier_s");
            ja.put("http://schema.org/downloadUrl");
            download = true;
        }
    }

    protected abstract void addWebService();
    protected abstract JSONObject getRequiredFields();
    protected abstract JSONObject getOptionalFields();
}
