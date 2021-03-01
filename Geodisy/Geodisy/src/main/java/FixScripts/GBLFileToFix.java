package FixScripts;

import _Strings.GeodisyStrings;

public class GBLFileToFix {
    String pID;
    String geoserverLabel;
    String dbID;
    String serverDataAccessBase;
    boolean isGF = false;
    String boundBoxNumber;
    boolean isRaster;
    String geoblacklightJSON;
    String gblJSONFilePath;
    DVJSONFileInfo dvjsonFileInfo;

    public GBLFileToFix() {
        serverDataAccessBase = "https://dataverse.scholarsportal.info/api/access/datafile/";
    }

    public String getPID() {
        return pID;
    }

    public void setpID(String fullPID) {
        pID = fullPID;
    }

    public String getGeoserverLabel() {
        return geoserverLabel;
    }

    public void setGeoserverLabel(String geoserverEnd) {
        geoserverLabel = "geodisy:" + GeodisyStrings.removeHTTPSAndReplaceAuthority(pID).replace(".","_").replace("/","_")+  GeodisyStrings.replaceSlashes("/") + geoserverEnd;
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
        return isRaster;
    }

    public void setRaster(boolean raster) {
        this.isRaster = raster;
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

    public DVJSONFileInfo getDvjsonFileInfo() {
        return dvjsonFileInfo;
    }

    public void setDvjsonFileInfo(DVJSONFileInfo dvjsonFileInfo) {
        this.dvjsonFileInfo = dvjsonFileInfo;
    }

}
