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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public class GeoNameTest {
    private Map<String, String> countries = new HashMap<String, String>();
    private ToponymSearchCriteria searchCriteria;
    @Test
    public void testGeoNameLookup(){
        WebService.setUserName("geodisy"); // add your username here
        getCountryCodes();
        searchCriteria = new ToponymSearchCriteria();
        searchCriteria.setQ("zurich");
        ToponymSearchResult searchResult = null;
        try {
            searchResult = WebService.search(searchCriteria);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Toponym toponym : searchResult.getToponyms()) {
            System.out.println(toponym.getName()+" "+ toponym.getCountryName());
        }



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
