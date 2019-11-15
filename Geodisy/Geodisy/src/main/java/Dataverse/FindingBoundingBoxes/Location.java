package Dataverse.FindingBoundingBoxes;

import BaseFiles.GeoLogger;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class Location implements GeographicPoliticalUnit {
    protected String givenName;
    protected String commonName;
    protected BoundingBox boundingBox;
    protected final String NO_NAME= "no name";
    protected String altNames;
    protected GeonamesJSON geonamesJSON;
    protected GeoLogger logger = new GeoLogger(this.getClass());


    public Location(String givenName, GeonamesJSON geonamesJSON) {
        this.geonamesJSON = geonamesJSON;
        this.givenName = givenName;
        boundingBox = geonamesJSON.getBBFromGeonamesJSON();
        altNames = geonamesJSON.getAltNames();

    }

    public Location(String givenName, Element element){
        this.geonamesJSON = new GeonamesJSON(elementToString(element));
        this.givenName = givenName;
        boundingBox = geonamesJSON.getBBFromGeonamesBBElementString();
    }
    //Placeholder location constructor
    public Location(){
        boundingBox = new BoundingBox();
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

    public boolean hasBoundingBox(){
        return boundingBox.hasBoundingBox();
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getCommonName(){
        return commonName;
    }

    public void setAltNames(String altNames){
        this.altNames = altNames;
    }

    public static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    protected String elementToString(Element element) {
        String json = "";
        try {
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            StringWriter buffer = new StringWriter();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(element),
                    new StreamResult(buffer));
            json = buffer.toString();

        } catch (TransformerException e) {
            logger.error("Something went wrong when trying to convert" + element + "to a string");
        }
        return json;
    }
}
