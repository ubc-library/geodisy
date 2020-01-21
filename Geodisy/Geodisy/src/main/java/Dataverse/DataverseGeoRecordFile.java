package Dataverse;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;

public class DataverseGeoRecordFile extends DataverseRecordFile {
    GDALInfo gdalInfo;

    public DataverseGeoRecordFile(String title, String fileIdent, int dbID, String server, String datasetIdent) {
        super(title, fileIdent, dbID, server, datasetIdent);
        gdalInfo = new GDALInfo();
    }

    public DataverseGeoRecordFile(String title, int dbID, String server, String datasetIdent) {
        super(title, dbID, server, datasetIdent);
        gdalInfo = new GDALInfo();
    }

    public DataverseGeoRecordFile(String datasetIdent, GeographicBoundingBox gbb) {
        super(datasetIdent, gbb);
        gdalInfo = new GDALInfo();
    }

    public DataverseGeoRecordFile (DataverseRecordFile drf){
        super(drf.getTitle(),drf.dbID,drf.server,drf.datasetIdent);
        if(!drf.getFileIdent().equals(""))
            fileIdent = drf.getFileIdent();
        gdalInfo = new GDALInfo();
        this.setGbb(drf.getGBB());
    }
    public DataverseGeoRecordFile (DataverseRecordFile drf, String fileIdent){
        super(drf.getTitle(),drf.getFileIdent(),drf.dbID,drf.server,drf.datasetIdent);
        gdalInfo = new GDALInfo();
    }

    public DataverseGeoRecordFile(){
        super();
        gdalInfo = new GDALInfo();
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
}
