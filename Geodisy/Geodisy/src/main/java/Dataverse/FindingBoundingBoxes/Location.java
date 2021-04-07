package Dataverse.FindingBoundingBoxes;

import BaseFiles.GeoLogger;
import BaseFiles.Geonames;
import Dataverse.ExistingLocations;
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

import static _Strings.GeodisyStrings.EXISTING_LOCATION_BBOXES;
import static _Strings.GeodisyStrings.EXISTING_LOCATION_NAMES;

public class Location implements GeographicPoliticalUnit {
    protected String givenName = "";
    protected String commonName = "";
    protected String countryCode = "";
    protected BoundingBox boundingBox = new BoundingBox();
    protected final String NO_NAME= "no name";
    protected String altNames = "";
    protected GeonamesJSON geonamesJSON;
    protected GeoLogger logger = new GeoLogger(this.getClass());
    protected ExistingLocations existingLocations;


    public Location(String givenName, GeonamesJSON geonamesJSON) {
        existingLocations = ExistingLocations.getExistingLocations();
        this.geonamesJSON = geonamesJSON;
        this.givenName = givenName;
        boundingBox = geonamesJSON.getBBFromGeonamesJSON();
        altNames = geonamesJSON.getAltNames();
        commonName = geonamesJSON.getCommonCountryName();

    }

    public Location(String givenName, BoundingBox b){
        existingLocations = ExistingLocations.getExistingLocations();
        this.geonamesJSON = new GeonamesJSON();
        this.givenName = givenName;
        this.boundingBox = b;

    }

    public Location(String countryName){
        existingLocations = ExistingLocations.getExistingLocations();
        if(existingLocations.hasBB(countryName)){
            this.geonamesJSON = new GeonamesJSON();
            this.givenName = countryName;
            this.boundingBox = existingLocations.getBB(countryName);
            this.altNames = existingLocations.getLocationNames(countryName)[1];
            this.commonName = existingLocations.getLocationNames(countryName)[0];
            this.countryCode = existingLocations.getLocationNames(countryName)[2];
        } else {
            this.geonamesJSON = new GeonamesJSON(new Geonames().getGeonamesCountry(countryName));
            this.givenName = countryName;
            this.boundingBox = geonamesJSON.getBBFromGeonamesJSON();
            this.altNames = geonamesJSON.getAltNames();
            this.commonName = this.geonamesJSON.getCommonCountryName();
            this.countryCode = this.geonamesJSON.getCountryCode();
            existingLocations.addBBox(countryName,boundingBox);
            existingLocations.saveExistingSearchs(existingLocations.getBBoxes(),EXISTING_LOCATION_BBOXES,ExistingLocations.class.getName());
            existingLocations.addNames(countryName,commonName,altNames,countryCode);
            existingLocations.saveExistingSearchs(existingLocations.getNames(),EXISTING_LOCATION_NAMES,ExistingLocations.class.getName());
        }
    }

    public Location(String countryName, String provinceName){
        existingLocations = ExistingLocations.getExistingLocations();
        if(existingLocations.hasBB(countryName,provinceName)){
            this.geonamesJSON = new GeonamesJSON();
            this.givenName = provinceName;
            this.boundingBox = existingLocations.getBB(countryName,provinceName);
            this.altNames = existingLocations.getLocationNames(countryName,provinceName)[1];
            this.commonName = existingLocations.getLocationNames(countryName,provinceName)[0];
        } else {
            this.geonamesJSON = new GeonamesJSON(new Geonames().getGeonamesProvince(provinceName,countryName));
            this.givenName = provinceName;
            this.boundingBox = geonamesJSON.getBBFromGeonamesJSON();
            this.altNames = geonamesJSON.getAltNames();
            this.commonName = this.geonamesJSON.getCommonStateName();
            this.countryCode = "";
            existingLocations.addNames(countryName,provinceName,commonName,altNames,"");
            existingLocations.addBBox(countryName,provinceName,boundingBox);
            existingLocations.saveExistingSearchs(existingLocations.getNames(),EXISTING_LOCATION_NAMES,ExistingLocations.class.getName());
            existingLocations.saveExistingSearchs(existingLocations.getBBoxes(),EXISTING_LOCATION_BBOXES,existingLocations.getClass().getName());
        }
    }

    public Location(String countryName, String provinceName, String cityName){
        existingLocations = ExistingLocations.getExistingLocations();
        if(existingLocations.hasBB(countryName,provinceName, cityName)){
            this.geonamesJSON = new GeonamesJSON();
            this.givenName = provinceName;
            this.boundingBox = existingLocations.getBB(countryName,provinceName);
            this.altNames = existingLocations.getLocationNames(countryName,provinceName, cityName)[1];
            this.commonName = existingLocations.getLocationNames(countryName,provinceName, cityName)[0];
        } else {
            this.geonamesJSON = new GeonamesJSON(new Geonames().getGeonamesCity(cityName, provinceName,countryName));
            this.givenName = cityName;
            this.boundingBox = geonamesJSON.getBBFromGeonamesJSON();
            this.altNames = geonamesJSON.getAltNames();
            this.commonName = this.geonamesJSON.getCommonCityName();
            this.countryCode = "";
            existingLocations.addBBox(countryName,provinceName,cityName,boundingBox);
            existingLocations.saveExistingSearchs(existingLocations.getBBoxes(),EXISTING_LOCATION_BBOXES,ExistingLocations.class.getName());
            existingLocations.addNames(countryName,provinceName,cityName, commonName,altNames,"");
            existingLocations.saveExistingSearchs(existingLocations.getNames(),EXISTING_LOCATION_NAMES,ExistingLocations.class.getName());
        }
    }
    public Location(String givenName, Element element){
        existingLocations = ExistingLocations.getExistingLocations();
        this.geonamesJSON = new GeonamesJSON(elementToString(element));
        this.givenName = givenName;
        boundingBox = geonamesJSON.getBBFromGeonamesBBElementString();
    }
    //Placeholder location constructor
    public Location(){
        existingLocations = ExistingLocations.getExistingLocations();
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

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public GeonamesJSON getGeonamesJSON() {
        return geonamesJSON;
    }

    public void setGeonamesJSON(GeonamesJSON geonamesJSON) {
        this.geonamesJSON = geonamesJSON;
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
