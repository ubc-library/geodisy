package Dataverse.FindingBoundingBoxes;

import Crosswalking.JSONParsing.DataverseParser;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.commons.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Location implements GeographicPoliticalUnit {
    protected String name;
    protected BoundingBox boundingBox;
    protected final String NO_NAME= "no name";
    Logger logger = LogManager.getLogger(DataverseParser.class);


    public Location(String name) {
        this.name = WordUtils.capitalizeFully(name);
        this.boundingBox = new BoundingBox();
    }

    public String getName() {
        if(name.matches(NO_NAME))
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatSouth() {
        double answer= boundingBox.getLatSouth();
        if(answer==361)
            logger.error("No latSouth for %s, returning 361", name);
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
            logger.error("No latNorth for %s, returning 361", name);
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
            logger.error("No longWest for %s, returning 361", name);
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
            logger.error("No longEast for %s, returning 361", name);
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
}
