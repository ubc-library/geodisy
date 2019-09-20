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

    public String getGeonamesProvince(String province, String country) {
        Map<String, String> parameters = getBaseParameters(country);
        parameters.put("fcode","ADM1*");
        province +=  addParameters(parameters);
        String urlString = "http://api.geonames.org/search?q=" + province;
        HTTPCaller caller = new HTTPCaller(urlString);
        String answer = "";
        answer = caller.getJSONString();

        if(answer.contains("<totalResultsCount>0</totalResultsCount>"))
            answer = getGeonamesCountry(province);

        if(answer.contains("<totalResultsCount>0</totalResultsCount>"))
            answer = getGeonamesCountry(country);

        return answer;
    }

    public String getGeonamesCity(String city, String province, String country){
        Map<String, String> parameters = getBaseParameters(country);
        parameters.put("fcode","PPL*");
        String searchValue = city + "%2C%20" + province;
        searchValue +=  addParameters(parameters);
        String urlString = "http://api.geonames.org/search?q=" + searchValue;
        HTTPCaller caller = new HTTPCaller(urlString);
        String answer = "";
        answer = caller.getJSONString();

        if(answer.contains("<totalResultsCount>0</totalResultsCount>"))
            answer = getGeonamesProvince(province,country);
        return answer;
    }

    public String getGeonamesCountry(String country){
        Map<String, String> parameters = getBaseParameters(country);
        String searchString = country;
        parameters.put("fcode","PCL*");
        searchString +=  addParameters(parameters);
        String urlString = "http://api.geonames.org/search?q=" + searchString;
        HTTPCaller caller = new HTTPCaller(urlString);
        String answer ="";
        answer = caller.getJSONString();

        if(answer.contains("<totalResultsCount>0</totalResultsCount>"))
            answer = getGeonamesTerritory(country);
        return answer;
    }

    private String getGeonamesTerritory(String country) {
        Map<String, String> parameters = getBaseParameters(country);
        String searchString = country;
        parameters.put("fcode","TERR");
        searchString +=  addParameters(parameters);
        String urlString = "http://api.geonames.org/search?q=" + searchString;
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

    private HashMap<String, String> getBaseParameters(String country){
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", USER_NAME);
        parameters.put("style","FULL");
        parameters.put("maxRows","1");
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryCode(country);
        parameters.put("country",countryCode);

        return parameters;
    }
}
