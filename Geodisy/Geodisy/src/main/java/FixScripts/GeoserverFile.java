package FixScripts;

import java.util.LinkedList;

public class GeoserverFile {
    String doi;
    String geoserverLabel;
    String dbID;
    String serverDataAccessBase;
    boolean isGF = false;
    String boundBoxNumber;
    boolean raster;
    String geoblacklightJSON;
    String gblJSONFilePath;
    LinkedList<DVJSONFileInfo> dvjsonFileInfo;

    public GeoserverFile() {
        serverDataAccessBase = "https://dataverse.scholarsportal.info/api/access/datafile/";
        dvjsonFileInfo = new LinkedList<>();
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String fullDoi) {
        int start = fullDoi.indexOf("\"dc_identifier_s\":\"https://doi.org/")+35;
        String temp = fullDoi.substring(start);
        int end = temp.indexOf("\"");
        doi = temp.substring(0,end);
    }

    public String getGeoserverLabel() {
        return geoserverLabel;
    }

    public void setGeoserverLabel(String geoserverEnd) {
        geoserverLabel = "geodisy:" + doi.replace(".","_").replace("/","_")+geoserverEnd;
        geoserverLabel = geoserverLabel.toLowerCase();
    }

    public String getDbID() {
        return dbID;
    }

    public void setDbID(String dbID) {
        this.dbID = dbID;
    }

    public void setIsGF(boolean isGF){
        this.isGF = isGF;
    }

    public boolean isGF(){
        return isGF;
    }

    public String getServerDataAccessBase() {
        return serverDataAccessBase;
    }

    public void setServerDataAccessBase(String serverDataAccessBase) {
        this.serverDataAccessBase = serverDataAccessBase;
    }

    public void setGF(boolean GF) {
        isGF = GF;
    }

    public String getBoundBoxNumber() {
        return boundBoxNumber;
    }

    public void setBoundBoxNumberAndType(String[] boundBoxNumber) {
        this.boundBoxNumber = boundBoxNumber[1];
        setRaster(boundBoxNumber[0].toLowerCase().equals("r"));
    }

    public boolean isRaster() {
        return raster;
    }

    public void setRaster(boolean raster) {
        this.raster = raster;
    }

    public String getGeoblacklightJSON() {
        return geoblacklightJSON;
    }

    public void setGeoblacklightJSON(String geoblacklightJSON) {
        this.geoblacklightJSON = geoblacklightJSON;
    }

    public String getGblJSONFilePath() {
        return gblJSONFilePath;
    }

    public void setGblJSONFilePath(String gblJSONFilePath) {
        this.gblJSONFilePath = gblJSONFilePath;
    }

    public LinkedList<DVJSONFileInfo> getDvjsonFileInfo() {
        return dvjsonFileInfo;
    }

    public void setDvjsonFileInfo(LinkedList<DVJSONFileInfo> dvjsonFileInfo) {
        this.dvjsonFileInfo = dvjsonFileInfo;
    }

    public void addDvjsonFileInfo(DVJSONFileInfo d){
        dvjsonFileInfo.add(d);
    }
}
