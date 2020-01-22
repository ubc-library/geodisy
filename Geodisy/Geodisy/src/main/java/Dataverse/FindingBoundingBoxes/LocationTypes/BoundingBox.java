package Dataverse.FindingBoundingBoxes.LocationTypes;
import java.io.Serializable;

import static BaseFiles.GeodisyStrings.*;

public class BoundingBox implements Serializable {
    private static final long serialVersionUID = -515599959188846468L;
    private double latSouth = 361;
    private double latNorth = 361;
    private double longWest = 361;
    private double longEast = 361;
    private boolean generated = false;
    private String fileName = "";
    private String geometryType = "Non-geospatial";
    private String geoserverLabel;
    private String doi;

    /**
     *
     * @param lat or long
     * @return 361 if value is outside the range for lat or long, respectively, or returns val if valid
     */

    private double checkLat(double lat) {
        if(lat<-90 || lat>90)
            return 361;
        return lat;
    }

    private double checkLong(double longitude) {
        if(longitude<-180 || longitude>180)
            return 361;
        return longitude;
    }
    public boolean hasBoundingBox(){
        return (latSouth>=-90 && latNorth<=90 && longEast<=180 && longWest>=-180)&&(latSouth!=361 && latNorth!=361 && longEast!=361 && longWest!=361)&&!(latSouth==0 && latNorth==0 && longEast==0 && longWest==0);
    }
    public boolean hasUTMCoords(){
        return Math.abs(latSouth) > 90 || latNorth > 90 || Math.abs(longEast) > 180 || Math.abs(longWest) > 180;
    }
    /**
     *
     * @param numString
     * @return Double parsed from the given string or 361 if the string was not parsable into a double
     */

    private double getDoubleLat(String numString) {
        double val;
        try{
            val = Double.parseDouble(numString);
        } catch (NumberFormatException e){
            val = 361;
        }
        return checkLat(val);
    }

    private double getDoubleLong(String numString) {
        double val;
        try{
            val = Double.parseDouble(numString);
        } catch (NumberFormatException e){
            val = 361;
        }
        return checkLong(val);
    }

    public double getLatSouth() {
        return latNorth>=latSouth? latSouth:latNorth;
    }

    public void setLatSouth(double latSouth) {
        this.latSouth = checkLat(latSouth);

    }

    public void setLatSouth(String latSouth){
        this.latSouth = getDoubleLat(latSouth);
    }

    public double getLatNorth() {
        return latNorth>=latSouth? latNorth:latSouth;
    }

    public void setLatNorth(double latNorth) {
        this.latNorth = checkLat(latNorth);
    }

    public void setLatNorth(String latNorth){
        this.latNorth = getDoubleLat(latNorth);
    }

    public double getLongWest() {
        return longWest;
    }

    public void setLongWest(double longWest) {
        this.longWest = checkLong(longWest);
    }

    public void setLongWest(String longWest){
        this.longWest =  getDoubleLong(longWest);
    }

    public double getLongEast() {
        return longEast;
    }

    public void setLongEast(double longEast) {
        this.longEast = longEast;
    }

    public void setLongEast(String longEast){
        this.longEast = getDoubleLong(longEast);
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        if(fileName.startsWith("/"))
            fileName = fileName.substring(1);
        this.fileName = fileName;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    public String getGeoserverLabel() {
        return geoserverLabel;
    }

    public void setGeoserverLabel(String s){
        geoserverLabel = "geodisy:" + s;
    }


    public boolean isWMS() {
        for(String s: PREVIEWABLE_FILE_EXTENSIONS) {
            if (fileName.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public boolean isWFS(){
        for(String s:OGRINFO_VECTOR_FILE_EXTENSIONS){
            if(fileName.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

}
