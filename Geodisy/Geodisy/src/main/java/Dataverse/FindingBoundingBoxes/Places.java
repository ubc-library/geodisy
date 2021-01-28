package Dataverse.FindingBoundingBoxes;

import BaseFiles.Geonames;
import Dataverse.ExistingLocations;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.City;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import Dataverse.FindingBoundingBoxes.LocationTypes.Province;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.text.WordUtils;

import static _Strings.GeodisyStrings.COUNTRY_VALS;
import static _Strings.GeodisyStrings.EXISTING_LOCATION_BBOXES;

/**
 * A basically static class that opens a file that holds all the Country codes and bounding box values
 * This can then be accessed to quickly grab a bounding box for a dataset that has a country label but
 * no defined bounding box or geospatial file.
 */
public class Places {
    HashMap<String, Country> countries;
    HashMap<String, String> countryCodes;
    ExistingLocations boundingBoxes;
    private static Places single_instance = null;
    static Document doc;

    public static Places getCountry(){
        if(single_instance==null)
            single_instance = new Places();
        return single_instance;
    }

    private Places(){
        File xmlFile = new File(COUNTRY_VALS);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        countries = new HashMap<>();
        countryCodes = new HashMap<>();
        boundingBoxes = ExistingLocations.getExistingLocations();

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getDocumentElement().getElementsByTagName("givenCountry");
            int nodeListLen = nodeList.getLength();
            for(int i = 0; i<nodeListLen; i++){
                setCountry(nodeList.item(i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void setCountry(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String commonName = Location.getTagValue("countryName", element);
            Country country = new Country(element, commonName);
            countries.put(WordUtils.capitalizeFully(commonName),country);
            countryCodes.put(country.getCountryCode(),country.getGivenName());
        }
    }



    public Country getCountryByName(String name){
        if(countries.containsKey(name))
            return countries.get(name);
        //TODO finish this
        else {
           return getCountryFromGeoNames(name);
        }
    }

    private Country getCountryFromGeoNames(String name) {
        Geonames geonames = new Geonames();
        String fullCountry = geonames.getGeonamesCountry(name);
        Country country = new Country();
        if(fullCountry.contains("<totalResultsCount>0</totalResultsCount>"))
            return new Country();
        String countryStub = getNodeString(fullCountry,"<geoname>");
        country.setGivenName(name);
        country.setCommonName(getNodeString(countryStub,"<name>"));
        country.setAltNames(getNodeString(countryStub,"<alternateNames>"));
        String bbox = getNodeString(countryStub,"<bbox>");
        if(!bbox.isEmpty()) {
            country.setLongWest(getNodeString(bbox, "<west>"));
            country.setLongEast(getNodeString(bbox, "<east>"));
            country.setLatNorth(getNodeString(bbox,"<north>"));
            country.setLatSouth(getNodeString(bbox,"<south>"));
        }
        GeonamesJSON gnj = new GeonamesJSON();
        gnj.setJSONObject(countryStub);
        country.setGeonamesJSON(gnj);
        country.setCountryCode(getNodeString(countryStub,"<countryCode>"));
        return country;
    }

    private String getNodeString(String xmlString, String s) {
        try {
            int start = xmlString.indexOf(s) + s.length();
            String end = "</" + s.substring(1);
            return xmlString.substring(start, xmlString.indexOf(end));
        }catch(IndexOutOfBoundsException e) {
            return "";
        }
    }

    private Country createCountryFromNode(Node node){
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String commonName = Location.getTagValue("countryName", element);
            return new Country(element, commonName);
        }
        return new Country();
    }

    public Country getCountryByCode(String code){
        String codeCorrect = code.toUpperCase();
        if(countryCodes.containsKey(codeCorrect))
            return getCountryByName(countryCodes.get(codeCorrect));
        else
            return new Country();
    }

    public String getCountryCode(String countryName){
        String capName = WordUtils.capitalizeFully(countryName);
        Country country;
        if(countries.containsKey(capName))
            country = countries.get(capName);
        else
            return "_JJ";
        return country.getCountryCode();
    }

    public boolean isCountryCode(String code){
        String codeCorrect = code.toUpperCase();
        return countryCodes.containsKey(codeCorrect);
    }

    public void addProvince(String country, Province province){
        if(province.hasBoundingBox()) {
            boundingBoxes.addBBox(country + "zzz" + province, province.getBoundingBox());
            boundingBoxes.saveExistingSearchs(boundingBoxes.getbBoxes(),EXISTING_LOCATION_BBOXES,boundingBoxes.getClass().getName());
        }

    }

    public void addCity(String country, City city){
        if(city.getBoundingBox().getLongWest()!=361) {
            boundingBoxes.addBBox(country + "zzz" + city.getProvinceName() + "zzz" + city.getCommonName(), city.boundingBox);

        }
    }

    public void addCountry(String country,BoundingBox b){
        boundingBoxes.addBBox(country,b);
    }

    public BoundingBox getExistingBB(String country, String province, String city){
        if(boundingBoxes.hasBB(country,province,city))
            return boundingBoxes.getBB(country, province, city);
        return new BoundingBox();
    }

    public BoundingBox getExistingBB(String country, String province){
        if(boundingBoxes.getBB(country,province).getLongWest()!=361)
            return boundingBoxes.getBB(country,province);
        return new BoundingBox();
    }

    public BoundingBox getExistingBB(String country){
        if(boundingBoxes.hasBB(country))
            return boundingBoxes.getBB(country);
        return new BoundingBox();
    }

    public HashMap getBoundingBoxes(){
        return boundingBoxes.getbBoxes();
    }

    public void setBoundingBoxes(HashMap<String, BoundingBox> bBoxes){
        this.boundingBoxes.setbBoxes(bBoxes);;
    }
}
