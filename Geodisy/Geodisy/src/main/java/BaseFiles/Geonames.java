package BaseFiles;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicCoverage;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJavaObject;
import Dataverse.FindingBoundingBoxes.Countries;
import Dataverse.FindingBoundingBoxes.GeonamesBBs;
import Dataverse.SourceJavaObject;
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

import static _Strings.GeodisyStrings.GEONAMES_SEARCH_BASE;
import static _Strings.GeodisyStrings.GEONAMES_USERNAME;


public class Geonames {
    GeoLogger logger =  new GeoLogger(this.getClass());
    Countries countries;

    public Geonames() {
        this.countries = Countries.getCountry();
    }



    public String getGeonamesCity(String city, String province, String country){
        if(country.isEmpty()||province.isEmpty())
            return "";
        Map<String, String> parameters = getBaseParameters(country);
        parameters.put("fcode","PPL*");
        String searchString;
        if(city.isEmpty()||province.isEmpty())
            return "";
        searchString = city + "%2C%20" + province;
        searchString +=  addParameters(parameters);
        String urlString = GEONAMES_SEARCH_BASE + searchString;
        HTTPCallerGeoNames caller = new HTTPCallerGeoNames();
        String answer = "";
        answer = caller.callHTTP(urlString);
        if(answer.contains("<totalResultsCount>0</totalResultsCount>"))
            answer = getGeonamesProvince(province,country);
        return answer;
    }

    public String getGeonamesProvince(String province, String country) {
        Map<String, String> parameters = getBaseParameters(country);
        parameters.put("fcode","ADM1*");
        String searchString = province + addParameters(parameters);
        String urlString = GEONAMES_SEARCH_BASE + searchString;
        HTTPCallerGeoNames caller = new HTTPCallerGeoNames();
        String answer = "";
        answer = caller.callHTTP(urlString);

        //Check territory if province (US state level) doesn't work. TERR is between Province and Country
        if(answer.contains("<totalResultsCount>0</totalResultsCount>")) {
            parameters = getBaseParameters(country);
            parameters.put("fcode", "TERR");
            searchString = province + addParameters(parameters);
            urlString = GEONAMES_SEARCH_BASE + searchString;
            caller = new HTTPCallerGeoNames();
            answer = caller.callHTTP(urlString);
        }
        if(answer.contains("<totalResultsCount>0</totalResultsCount>"))
            answer = getGeonamesCountry(country);

        return answer;
    }

    public String getGeonamesCountry(String country){
        if(country.isEmpty())
            return "";
        Map<String, String> parameters = getBaseParameters(country);
        parameters.remove("country");
        String searchString = country;
        parameters.put("fcode","PCL*");
        searchString +=  addParameters(parameters);
        String urlString = GEONAMES_SEARCH_BASE + searchString;
        HTTPCallerGeoNames caller = new HTTPCallerGeoNames();
        String answer ="";
        answer = caller.callHTTP(urlString);

        if(answer.contains("<totalResultsCount>0</totalResultsCount>"))
            answer = getGeonamesTerritory(country);
        return answer;
    }

    private String getGeonamesTerritory(String country) {
        Map<String, String> parameters = getBaseParameters(country);
        String searchString = country;
        parameters.put("fcode","TERR");
        searchString +=  addParameters(parameters);
        String urlString = GEONAMES_SEARCH_BASE + searchString;
        HTTPCallerGeoNames caller = new HTTPCallerGeoNames();
        String answer ="";
        answer = caller.callHTTP(urlString);
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
        parameters.put("username", GEONAMES_USERNAME);
        parameters.put("style","FULL");
        parameters.put("maxRows","1");
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryCode(country);
        parameters.put("country",countryCode);

        return parameters;
    }

    public SourceJavaObject getBoundingBox(SourceJavaObject sjo) {
        DataverseJavaObject djo = (DataverseJavaObject) sjo;
        GeonamesBBs geonamesBBs = new GeonamesBBs(djo);
        GeographicFields gf = djo.getGeoFields();
        for(GeographicCoverage geoCoverage: djo.getGeoFields().getGeoCovers()){
            gf.setGeoBBoxes(geonamesBBs.getDVBoundingBox(geoCoverage,gf.getGeoBBoxes()));
        }
        djo.setGeoFields(gf);
        return djo;
    }

    public String getCountryCode(String countryName){
        if(countries.isCountryCode(countryName))
            return countryName;
        return countries.getCountryCode(countryName);
    }
}
