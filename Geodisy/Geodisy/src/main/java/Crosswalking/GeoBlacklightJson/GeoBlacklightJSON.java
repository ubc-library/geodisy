package Crosswalking.GeoBlacklightJson;

import Dataverse.SourceJavaObject;
import org.json.JSONArray;
import org.json.JSONObject;

import static Crosswalking.GeoBlacklightJson.GeoBlacklightStrings.*;

/**
 * Takes a SourceJavaObject and creates a GeoBlacklight JSON from it
 */
public abstract class GeoBlacklightJSON implements JSONCreator {
    protected SourceJavaObject javaObject;
    protected String geoBlacklightJson;
    protected JSONObject jo;
    protected String doi;
    boolean wms = false;
    boolean wfs = false;
    boolean download = false;

    public GeoBlacklightJSON() {
        this.jo = new JSONObject();
    }

    @Override
    public String createJson() {
        getRequiredFields();
        getOptionalFields();
        geoBlacklightJson = jo.toString();
        return geoBlacklightJson;
    }

    protected boolean addWMS() {
        if (!wms) {
            JSONArray ja = jo.getJSONArray(EXTERNAL_SERVICES);
            ja.put("http://www.opengis.net/def/serviceType/ogc/wms");
            ja.put(GEOSERVER_WMS_LOCATION);
            jo.put(EXTERNAL_SERVICES, ja);
            wms = true;
        }
        return wms;
    }

    protected boolean addWFS() {
        if (!wfs) {
            JSONArray ja = jo.getJSONArray(EXTERNAL_SERVICES);
            ja.put("http://www.opengis.net/def/serviceType/ogc/wfs");
            ja.put(GEOSERVER_WFS_LOCATION);
            jo.put(EXTERNAL_SERVICES, ja);
            wfs = true;
        }
        return wfs;
    }

    protected boolean checkWMS(String title) {
        if (title.endsWith("kmz") || title.endsWith("geotiff"))
            return addWMS();
        return false;
    }

    protected boolean checkWFS(String title) {
        if (title.endsWith("shp") || title.endsWith("geojson"))
            return addWFS();
        return false;
    }

    protected void checkReferences(String title) {
        if (checkWMS(title) || checkWFS(title))
            addDownload();
    }

    protected void addDownload() {
        if (!download) {
            JSONArray ja = jo.getJSONArray(EXTERNAL_SERVICES);
            ja.put("http://schema.org/downloadUrl");
            jo.put(EXTERNAL_SERVICES, ja);
            download = true;
        }
    }
    public String getDoi(){
        return doi;
    }
    protected abstract void addWebService();
    protected abstract JSONObject getRequiredFields();
    protected abstract JSONObject getOptionalFields();
    protected abstract void addMetadataDownloadOptions();
}
