package Dataverse.FindingBoundingBoxes.LocationTypes;
import java.io.Serializable;

public class BoundingBox implements Serializable {
    private static final long serialVersionUID = -515599959188846468L;
    private double latSouth = 361;
    private double latNorth = 361;
    private double longWest = 361;
    private double longEast = 361;

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

    public boolean hasUTMCoords(){
        return latSouth <-90 || latNorth > 361 || Math.abs(longEast) > 361 || Math.abs(longWest) > 361;
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
        return latSouth;
    }

    public void setLatSouth(double latSouth) {
        this.latSouth = checkLat(latSouth);

    }

    public void setLatSouth(String latSouth){
        this.latSouth = getDoubleLat(latSouth);
    }

    public double getLatNorth() {
        return latNorth;
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

}
