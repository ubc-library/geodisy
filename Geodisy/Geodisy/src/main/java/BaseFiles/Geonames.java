package BaseFiles;

import Dataverse.FindingBoundingBoxes.Countries;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

import java.util.HashMap;
import java.util.Map;


public class Geonames {
    GeoLogger logger =  new GeoLogger(this.getClass());
    Countries countries;
    private String USER_NAME = "geodisy";

    public Geonames() {
        this.countries = Countries.getCountry();
    }

    public String callGeonames(String searchValue, String country, String fcode) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        parameters.put("maxRows","1");
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryCode(country);
        parameters.put("country",countryCode);
        parameters.put("fcode",fcode);
        searchValue +=  addParameters(parameters);
        String urlString = "http://api.geonames.org/search?q=" + searchValue;
        HTTPCaller caller = new HTTPCaller(urlString);
        String answer = "";
        answer = caller.getJSONString();

        return answer;
    }

    public String callGeonames(String searchValue, String fcode){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        //parameters.put("maxRows","1");
        parameters.put("fcode",fcode);
        searchValue +=  addParameters(parameters);
        String urlString = "http://api.geonames.org/search?q=" + searchValue;
        HTTPCaller caller = new HTTPCaller(urlString);
        String answer ="";
        answer = caller.getJSONString();

        return answer;
    }


    private String addParameters(Map<String, String> parameters) {
        String answer = "";
        for(String s: parameters.keySet()){
            answer+= "&" + s + "=" + parameters.get(s);
        }

        return answer;
    }

    /**
     * Create an XML Document from an XML string
     * @param xmlString
     * @return
     */
    public Document getXMLDoc(String xmlString){
        DocumentBuilderFactory factory;
        DocumentBuilder builder = null;
        try {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlString));
        return builder.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.error("Something went wrong parsing the string from Geonames");
        }
        return builder.newDocument();

    }
}
