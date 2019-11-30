package Crosswalking.GeoBlacklightJson;

public class GeoBlacklightStrings {

    //TODO enter in the geoserver wfs location
    public final static String GEOSERVER_BASE = "\"https://206-12-92-97.cloud.computecanada.ca/"; //may need geoserver/web/
    public final static String GEOSERVER_WFS_LOCATION = GEOSERVER_BASE+"geoserver/wfs\"";
    //TODO enter in the geoserver wms location
    public final static String GEOSERVER_WMS_LOCATION = GEOSERVER_BASE+"geoserver/wms\"";
    public final static String EXTERNAL_SERVICES = "dct_references_s";
    public final static String RECORD_URL = "\"http://schema.org/url\":";
    public final static String WMS = "\"http://www.opengis.net/def/serviceType/ogc/wms\":"+GEOSERVER_WMS_LOCATION;
    public final static String WFS = "\"http://www.opengis.net/def/serviceType/ogc/wfs\":"+GEOSERVER_WFS_LOCATION;
    public final static String DIRECT_FILE_DOWNLOAD = "\"http://schema.org/downloadUrl\":";
    public final static String ISO_METADATA = "\"http://www.isotc211.org/schemas/2005/gmd/\":";
    public final static String[] METADATA_DOWNLOAD_SERVICES = {RECORD_URL, WMS, WFS,DIRECT_FILE_DOWNLOAD,ISO_METADATA};



}
