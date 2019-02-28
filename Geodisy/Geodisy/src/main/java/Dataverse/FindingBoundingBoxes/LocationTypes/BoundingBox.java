package Dataverse.FindingBoundingBoxes.LocationTypes;

public class BoundingBox {
    private double latSouth = 361;
    private double latNorth = 361;
    private double longWest = 361;
    private double longEast = 361;


    public BoundingBox() {
    }

    public double getLatSouth() {
        return latSouth;
    }

    public void setLatSouth(double latSouth) {
        this.latSouth = latSouth;
    }

    public double getLatNorth() {
        return latNorth;
    }

    public void setLatNorth(double latNorth) {
        this.latNorth = latNorth;
    }

    public double getLongWest() {
        return longWest;
    }

    public void setLongWest(double longWest) {
        this.longWest = longWest;
    }

    public double getLongEast() {
        return longEast;
    }

    public void setLongEast(double longEast) {
        this.longEast = longEast;
    }
}
