package Dataverse.FindingBoundingBoxes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CountryCodes {
    private static Map<String, String> countries = new HashMap<>();
    static {
        String[] countryCodes = Locale.getISOCountries();
        for (String iso : countryCodes) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry().toUpperCase(), iso);
        }
    }

    public static Map<String, String> getCountries() {
        return countries;
    }
    public static String getCountryByCode(String code){
        if(countries.containsKey(code.toUpperCase()))
            return countries.get(code);
        else
            return "";
    }
}
