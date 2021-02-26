package Dataverse;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;

import static _Strings.DVFieldNameStrings.GDAL_STRING;

public class DataverseGeoRecordFile extends DataverseRecordFile {
    GDALInformation gdalInfo;
    boolean onGeoserver = false;

    public DataverseGeoRecordFile(String title, String fileIdent, int dbID, String server, String datasetIdent) {
        super(title, fileIdent, dbID, server, datasetIdent);
        gdalInfo = new GDALInformation();
    }

    public DataverseGeoRecordFile(String datasetIdent, GeographicBoundingBox gbb) {
        super(datasetIdent, gbb);
        gdalInfo = new GDALInformation();
    }

    public DataverseGeoRecordFile (DataverseRecordFile drf){
        super(drf.getTranslatedTitle(), drf.datasetIdent, drf.recordURL);
        if(!drf.getFileIdent().equals(""))
            fileIdent = drf.getFileIdent();
        gdalInfo = new GDALInformation();
        setGbb(drf.getGBB(), drf.translatedTitle);
        this.setFileNumber(Integer.valueOf(drf.getGBBFileNumber()));
        this.setOriginalTitle(drf.getOriginalTitle());
        this.setProjection(drf.getProjection());
        this.setFileURL(drf.getFileURL());
    }
    public DataverseGeoRecordFile (DataverseRecordFile drf, String fileIdent){
        super(drf.getTranslatedTitle(),drf.getFileIdent(),drf.dbID,drf.server,drf.datasetIdent);
        gdalInfo = new GDALInformation();
    }

    public DataverseGeoRecordFile(){
        super();
        gdalInfo = new GDALInformation();
    }

    //For testing
    public DataverseGeoRecordFile(String doi, String translatedTitle){
        super();
        setTranslatedTitle(translatedTitle);
        setGeoserverLabel(doi);
    }

    @Override
    public void setProjection(String projection){
        gdalInfo.setProjection(projection);
    }

    public String getProjection(){
        return gdalInfo.getProjection();
    }

    public void setGdalString(String gdalString, boolean raster){
        gdalInfo.setFullGdalString(gdalString, raster);
    }


    public void getGdalString(){
        gdalInfo.getFullGdalString();
    }

    public String getMaxX() {
        return gdalInfo.getMaxX();
    }

    public void setMaxX(String maxX) {
        gdalInfo.setMaxX(maxX);
    }

    public String getMaxY() {
        return gdalInfo.getMaxY();
    }

    public void setMaxY(String maxY) {
        gdalInfo.setMaxY(maxY);
    }

    public String getMinX() {
        return gdalInfo.getMinX();
    }

    public void setMinX(String minX) {
        gdalInfo.setMinX(minX);
    }

    public String getMinY() {
        return gdalInfo.getMinY();
    }

    public void setMinY(String minY) {
        gdalInfo.setMinY(minY);
    }

    public String getNativeCRS(){
        return gdalInfo.getNativeCRS();
    }

    public void setNativeCRS(String crs){
        gdalInfo.setNativeCRS(crs);
    }

    @Override
    public boolean isOnGeoserver() {
        return onGeoserver;
    }

    @Override
    public void setGbb(GeographicBoundingBox gbb, boolean isRaster){
        this.gbb = gbb;
        this.gdalInfo = new GDALInformation();
        this.gdalInfo.setFullGdalString(gbb.getField(GDAL_STRING), isRaster);
    }

    public void setGbb(GeographicBoundingBox gbb, String filename){
        setGbb(gbb,filename.endsWith(".tif"));
    }
}
