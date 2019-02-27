package Dataverse.FindingBoundingBoxes;

import org.apache.commons.text.WordUtils;

public class Location implements GeographicUnit {
    protected String name;
    protected double latSouth = 361;
    protected double latNorth = 361;
    protected double longWest = 361;
    protected double longEast = 361;
    protected final String NO_NAME= "no name";

    public Location(String name) {
        this.name = WordUtils.capitalizeFully(name);
    }

    public String getName() {
        if(name == NO_NAME)
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatSouth() {
        return latSouth;
    }

    public void setLatSouth(double latSouth) {
        this.latSouth = latSouth;
    }

    public void setLatMin(String latMin) {
        this.latSouth = Double.parseDouble(latMin);
    }
    public double getLatNorth() {
        return latNorth;
    }

    public void setLatNorth(double latNorth) {
        this.latNorth = latNorth;
    }

    public void setLatMax(String latMax) {
        this.latNorth = Double.parseDouble(latMax);
    }

    public double getLongWest() {
        return longWest;
    }

    public void setLongWest(double longWest) {
        this.longWest = longWest;
    }

    public void setLongMin(String longMin) {
        this.longWest = Double.parseDouble(longMin);
    }
    public double getLongEast() {
        return longEast;
    }

    public void setLongEast(double longEast) {
        this.longEast = longEast;
    }

    public void setLongMax(String longMax) {
        this.longEast = Double.parseDouble(longMax);
    }


}
