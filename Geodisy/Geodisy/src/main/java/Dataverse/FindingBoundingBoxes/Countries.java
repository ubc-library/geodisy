package Dataverse.FindingBoundingBoxes;

import BaseFiles.Geonames;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import Dataverse.FindingBoundingBoxes.LocationTypes.Province;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import org.apache.commons.text.WordUtils;

import static BaseFiles.GeodisyStrings.COUNTRY_VALS;

/**
 * A basically static class that opens a file that holds all the Country codes and bounding box values
 * This can then be accessed to quickly grab a bounding box for a dataset that has a country label but
 * no defined bounding box or geospatial file.
 */
public class Countries {
    HashMap<String, Country> countries;
    HashMap<String, String> countryCodes;
    HashMap<String, BoundingBox> boundingBoxes;
    private static Countries single_instance = null;
    static Document doc;

    public static Countries getCountry(){
        if(single_instance==null)
            single_instance = new Countries();
        return single_instance;
    }

    private Countries(){
        File xmlFile = new File(COUNTRY_VALS);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        countries = new HashMap<>();
        countryCodes = new HashMap<>();
        boundingBoxes = new HashMap<>();

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
        String capName = name.toUpperCase();
        if(countries.containsKey(capName))
            return countries.get(capName);
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
        if(province.getBoundingBox().getLongWest()!=361)
            boundingBoxes.put(country+"zzz"+province, province.getBoundingBox());
    }

    public BoundingBox getExistingBB(String country, String province){
        if(boundingBoxes.containsKey(country+"zzz"+province))
            return boundingBoxes.get(country+"zzz"+province);
        return new BoundingBox();
    }

    public BoundingBox getExistingBB(String country, String province, String city){
        if(boundingBoxes.containsKey(country+"zzz"+ province+ "zzz" + city))
            return boundingBoxes.get(country+"zzz"+ province+ "zzz" + city);
        return new BoundingBox();
    }

    public HashMap getBoundingBoxes(){
        return boundingBoxes;
    }

    public void setBoundingBoxes(HashMap<String, BoundingBox> bBoxes){
        this.boundingBoxes = bBoxes;
    }
}
