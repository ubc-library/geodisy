package tests;

import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GeoNameTest {
    private Map<String, String> countries = new HashMap<String, String>();
    private ToponymSearchCriteria searchCriteria;
    @Test
    public void testGeoNameLookup(){
        String countryBoundingBoxesCSV = "/Geodisy/country-boundingboxes.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        List<Country> countries = new LinkedList<>();
        try {

            br = new BufferedReader(new FileReader(countryBoundingBoxesCSV));
            Country country;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] countryLine = line.split(csvSplitBy);
                if(countryLine[0]=="country")
                    continue;
                country = new Country(countryLine[0]);
                country.setCountryCode(countryLine[1]);
                country.setLongMin(countryLine[2]);
                country.setLatMin(countryLine[3]);
                country.setLongMax(countryLine[4]);
                country.setLatMax(countryLine[5]);


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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
