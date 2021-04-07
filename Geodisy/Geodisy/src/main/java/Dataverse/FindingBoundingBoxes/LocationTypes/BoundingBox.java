package Dataverse.FindingBoundingBoxes.LocationTypes;
import java.io.Serializable;

import static _Strings.GeodisyStrings.*;

public class BoundingBox implements Serializable {
    private static final long serialVersionUID = -515599959188846468L;
    private double latSouth = 361;
    private double latNorth = 361;
    private double longWest = 361;
    private double longEast = 361;
    private boolean generated = false;
    private String fileName = "";
    private String geometryType = UNDETERMINED;
    private String geoserverLabel = "";
    private String doi = "";
    private String width = "0";
    private String height = "0";
    private String place = "";

    /**
     *
     * @param lat or long
     * @return 361 if value is outside the range for lat or long, respectively, or returns val if valid
     */

    private double checkLat(double lat) {
        if(lat<-90.0 || lat>90.0)
            return 361;
        return lat;
    }

    private double checkLong(double longitude) {
        if(longitude<-180.0 || longitude>180.0)
            return 361;
        return longitude;
    }
    public boolean hasBoundingBox(){
        return ((getLatSouth()>=-90 && getLatNorth()<=90 && getLongEast()<=180 && getLongWest()>=-180)&&(getLatSouth()!=361 && getLatNorth()!=361 && getLongEast()!=361 && getLongWest()!=361));
    }
    public boolean hasUTMCoords(){
        return Math.abs(getLatSouthUnchecked()) > 90 || Math.abs(getLatNorthUncheck()) > 90 || Math.abs(getLongEastUnchecked()) > 180 || Math.abs(getLongWestUnchecked()) > 180;
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
            if(Math.abs(val)<0.0001)
                val = 0;
        } catch (NumberFormatException e){
            val = 361;
        }
        return val;
    }

    private double getDoubleLong(String numString) {
        double val;
        try{
            val = Double.parseDouble(numString);
            if(Math.abs(val)<0.0001)
                val = 0;
        } catch (NumberFormatException e){
            val = 361;
        }
        return val;
    }

    public double getLatSouth() {
        return latNorth>=latSouth? checkLat(latSouth):checkLat(latNorth);
    }

    public void setLatSouth(double latSouth) {
        this.latSouth = latSouth;

    }

    public void setLatSouth(String latSouth){
        this.latSouth = getDoubleLat(latSouth);
    }

    public double getLatNorth() {
        return latNorth>=latSouth? checkLat(latNorth):checkLat(latSouth);
    }

    public void setLatNorth(double latNorth) {
        this.latNorth = latNorth;
    }

    public void setLatNorth(String latNorth){
        this.latNorth = getDoubleLat(latNorth);
    }

    public double getLongWest() {
        return checkLong(longWest);
    }

    public void setLongWest(double longWest) {
        this.longWest = longWest;
    }

    public void setLongWest(String longWest){
        this.longWest =  getDoubleLong(longWest);
    }

    public double getLongEast() {
        return checkLong(longEast);
    }

    public void setLongEast(double longEast) {
        this.longEast = longEast;
    }

    public void setLongEast(String longEast){
        this.longEast = getDoubleLong(longEast);
    }

    public double getLongEastUnchecked(){
        return longEast;
    }

    public double getLongWestUnchecked(){
        return longWest;
    }

    public double getLatNorthUncheck(){
        return latNorth>latSouth? latNorth:latSouth;
    }

    public double getLatSouthUnchecked(){
        return latSouth<latNorth? latSouth:latNorth;
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

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
