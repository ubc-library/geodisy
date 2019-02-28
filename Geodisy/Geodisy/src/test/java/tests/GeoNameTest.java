package tests;

import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class GeoNameTest {
    private Map<String, String> countries = new HashMap<String, String>();
    private ToponymSearchCriteria searchCriteria;
    @Test
    public void testGeoNameLookup(){
        String city = "Philadelphia";
        String state = "Mississippi";
        String country = "USA";

        String bbString = getBBString(country,state,city);
        System.out.println(bbString);
        }

    private String getBBString(String country, String state, String city) {
        String baseQuery = "https://nominatim.openstreetmap.org/search?q=";
        String suffix = "&format=xml";
        try {
            String url = baseQuery+city+",%2C" + state +",%2C" + country + suffix;
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


    private void getCountryCodes() {
        String[] countryCodes = Locale.getISOCountries();
        for (String iso : countryCodes) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry().toUpperCase(), iso);
        }
        System.out.println("hi");
    }


}
