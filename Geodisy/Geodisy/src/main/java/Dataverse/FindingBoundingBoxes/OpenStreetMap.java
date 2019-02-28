package Dataverse.FindingBoundingBoxes;

import javafx.geometry.BoundingBox;
import org.geonames.ToponymSearchCriteria;
import org.geonames.WebService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OpenStreetMap implements FindBoundBox {
    private ToponymSearchCriteria searchCriteria;
    private Map<String, String> countries = new HashMap<String, String>();

    public OpenStreetMap() {
        WebService.setUserName("geodisy");
        searchCriteria = new ToponymSearchCriteria();

        getCountryCodes();

    }

    private void getCountryCodes() {
        String[] countryCodes = Locale.getISOCountries();
        for (String iso : countryCodes) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }
    }

    @Override
    public Location getDVBoundingBox(String country) {
        String countryCode = countries.get(country.toUpperCase());
        //searchCriteria.setCountryCode();


        return null;
    }

    @Override
    public GeographicUnit getDVBoundingBox(String country, String state) {
        return null;
    }

    @Override
    public GeographicUnit getDVBoundingBox(String country, String state, String city) {
        String locationURL = city + ",%2C" + state + ",%2C" + country;
        getBBString(locationURL);
        return null;
    }

    @Override
    public GeographicUnit getDVBoundingBox(String country, String state, String city, String other) {
        return null;
    }

    @Override
    public GeographicUnit getDVBoundingBoxOther(String other) {
        return null;
    }


    private String getBBString(String locationURL) {
        String baseQuery = "https://nominatim.openstreetmap.org/search?q=";
        String suffix = "&format=xml";
        try {
            String url = baseQuery + locationURL + suffix;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(url).openStream());
            NodeList nodes = doc.getElementsByTagName("place");
            Node node = nodes.item(0);
            Element element = (Element) node;
            return element.getAttribute("boundingbox");


        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return "";
    }
}