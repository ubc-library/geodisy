package Crosswalking.GeoBlacklightJson;

import Dataverse.DataverseGeoRecordFile;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;

import static Dataverse.DVFieldNameStrings.*;

public class GeoBlacklightStrings {

    //Set this to false to remove functionality that uses Geoserver (WMS/WFS full preview and data download)
    public final static boolean USE_GEOSERVER = true;

    //GEOBLACKLIGHT
    public final static String GEOBLACKLIGHT_BASE = "http://206-12-92-97.cloud.computecanada.ca:3000/catalog/";
    //TODO enter in the geoserver wfs location
    public final static String GEOSERVER_BASE = "\"https://206-12-92-97.cloud.computecanada.ca/"; //may need geoserver/web/
    private final static String GEOSERVER_DOWNLOAD_BASE = GEOSERVER_BASE + "geoserver/geodisy/";
    public final static String GEOSERVER_RASTER_DOWNLOAD_BASE = GEOSERVER_DOWNLOAD_BASE + "wms?service=WMS&version=1.1.0&request=GetMap&layers=geodisy:";
    public final static String GEOSERVER_VECTOR_DOWNLOAD_BASE = GEOSERVER_DOWNLOAD_BASE + "ows?service=WFS&version=1.0.0&request=GetFeature&typeName=geodisy:";
    public final static String GEOSERVER_REST = GEOSERVER_BASE + "geoserver/web/";
    public final static String GEOSERVER_WFS_LOCATION = GEOSERVER_BASE+"geoserver/wfs\"";
    //TODO enter in the geoserver wms location
    public final static String GEOSERVER_WMS_LOCATION = GEOSERVER_BASE+"geoserver/wms\"";
    public final static String EXTERNAL_SERVICES = "dct_references_s";
    public final static String RECORD_URL = "\"http://schema.org/url\":";
    public final static String WMS = "\"http://www.opengis.net/def/serviceType/ogc/wms\":"+GEOSERVER_WMS_LOCATION;
    public final static String WFS = "\"http://www.opengis.net/def/serviceType/ogc/wfs\":"+GEOSERVER_WFS_LOCATION;
    public final static String DIRECT_FILE_DOWNLOAD = "\"http://schema.org/downloadUrl\":";
    public final static String ISO_METADATA = "\"http://lccn.loc.gov/sh85035852\":";
    public final static String ISO_METADATA_FILE_ZIP = "iso19115.zip";
    public final static String[] METADATA_DOWNLOAD_SERVICES = {RECORD_URL, WMS, WFS,DIRECT_FILE_DOWNLOAD,ISO_METADATA};

    public static String getGeoDownloadUrl(DataverseGeoRecordFile dgrf){
        if(dgrf.getTranslatedTitle().endsWith("tif"))
            return getWMSDownload(dgrf);
        else
            return getWFSDownload(dgrf);
    }
    private static String getWMSDownload(DataverseGeoRecordFile drf){
        GeographicBoundingBox gbb = drf.getGBB();
        String width = gbb.getField(WIDTH);
        String height = gbb.getField(HEIGHT);
        if(!width.isEmpty()&&!height.isEmpty())
            return GEOSERVER_RASTER_DOWNLOAD_BASE + drf.getGeoserverLabel() + "&styles=&bbox=" + gbb.getField(NORTH_LAT) + ","+ gbb.getField(WEST_LONG) + "," + gbb.getField(SOUTH_LAT) + "," + gbb.getField(EAST_LONG) + "&width=" + gbb.getField(WIDTH) + "&height=" + gbb.getField(HEIGHT) + "&srs=" + gbb.getField(PROJECTION) + "&format=image/geotiff\"";
        else
            return "";
    }

    private static String getWFSDownload(DataverseGeoRecordFile drf){
        return GEOSERVER_VECTOR_DOWNLOAD_BASE + drf.getGeoserverLabel() + "&maxfeatures=50&outputformat=SHAPE-ZIP\"";
    }

}
