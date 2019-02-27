package Dataverse.FindingBoundingBoxes;

import javafx.geometry.BoundingBox;
import org.geonames.ToponymSearchCriteria;
import org.geonames.WebService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GeoNames implements FindBoundBox {
    private ToponymSearchCriteria searchCriteria;
    private Map<String, String> countries = new HashMap<String, String>();

    public GeoNames() {
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
}
