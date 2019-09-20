package Dataverse.FindingBoundingBoxes;

import BaseFiles.GeoLogger;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;

public class Location implements GeographicPoliticalUnit {
    protected String givenName;
    protected String commonName;
    protected BoundingBox boundingBox;
    protected final String NO_NAME= "no name";
    protected GeonamesJSON geonamesLocationJson;
    GeoLogger logger = new GeoLogger(this.getClass());


    public Location(String givenName) {
        this.givenName = givenName;
        this.commonName = this.geonamesLocationJson.getCommonName();
        this.boundingBox = this.geonamesLocationJson.getBoundingBox();
    }

    protected void setCommonName(){
        commonName = this.geonamesLocationJson.getCommonName();
        setBoundingBoxFromJSON();
    }

    protected void setBoundingBoxFromJSON() {
        boundingBox = this.geonamesLocationJson.getBoundingBox();
    }

    public String getGivenName() {
        if(givenName.matches(NO_NAME))
            return "";
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public double getLatSouth() {
        double answer= boundingBox.getLatSouth();
        if(answer==361)
            logger.error("No latSouth for "+ givenName + ", returning 361");
        return answer;
    }

    public void setLatSouth(double latSouth) {
        boundingBox.setLatSouth(latSouth);
    }

    public void setLatSouth(String latSouth) {
        boundingBox.setLatSouth(latSouth);
    }
    public double getLatNorth() {
        double answer= boundingBox.getLatNorth();
        if(answer==361)
            logger.error("No latNorth for "+ givenName + ", returning 361");
        return answer;
    }

    public void setLatNorth(double latNorth) {
       boundingBox.setLatNorth(latNorth);
    }

    public void setLatNorth(String latNorth) {
        boundingBox.setLatNorth(latNorth);
    }

    public double getLongWest() {
        double answer= boundingBox.getLongWest();
        if(answer==361)
            logger.error("No longWest for for "+ givenName + ", returning 361");
        return answer;
    }

    public void setLongWest(double longWest) {
        boundingBox.setLongWest(longWest);
    }

    public void setLongWest(String longWest){
        boundingBox.setLongWest(longWest);
    }

    public void setLongMin(String longWest) {
        boundingBox.setLongWest(longWest);
    }
    public double getLongEast() {
        double answer= boundingBox.getLongEast();
        if(answer==361)
            logger.error("No longEast for "+ givenName + ", returning 361");
        return answer;
    }

    public void setLongEast(double longEast){
        boundingBox.setLongEast(longEast);
    }
    public void setLongEast(String longEast) {
        boundingBox.setLongEast(longEast);
    }

    public void setLongMax(String longEast) {
        boundingBox.setLongEast(longEast);
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public boolean hasGeoRecord(){
        return geonamesLocationJson.hasGeoRecord();
    }


}
