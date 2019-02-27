package Dataverse.FindingBoundingBoxes;

public class Location implements GeographicUnit {
    protected String name;
    protected double latMin = 361;
    protected double latMax = 361;
    protected double longMin = 361;
    protected double longMax = 361;
    protected final String NO_NAME= "no name";

    public Location(String name) {
        this.name = name;
    }

    public String getName() {
        if(name == NO_NAME)
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatMin() {
        return latMin;
    }

    public void setLatMin(double latMin) {
        this.latMin = latMin;
    }

    public void setLatMin(String latMin) {
        this.latMin = Double.parseDouble(latMin);
    }
    public double getLatMax() {
        return latMax;
    }

    public void setLatMax(double latMax) {
        this.latMax = latMax;
    }

    public void setLatMax(String latMax) {
        this.latMax = Double.parseDouble(latMax);
    }

    public double getLongMin() {
        return longMin;
    }

    public void setLongMin(double longMin) {
        this.longMin = longMin;
    }

    public void setLongMin(String longMin) {
        this.longMin = Double.parseDouble(longMin);
    }
    public double getLongMax() {
        return longMax;
    }

    public void setLongMax(double longMax) {
        this.longMax = longMax;
    }

    public void setLongMax(String longMax) {
        this.longMax = Double.parseDouble(longMax);
    }


}
