package Dataverse.FindingBoundingBoxes.LocationTypes;

import BaseFiles.Geonames;
import Dataverse.FindingBoundingBoxes.GeonamesJSON;
import Dataverse.FindingBoundingBoxes.Location;

import java.util.HashMap;

public class Province extends Location {
    private Country country;
    private HashMap<String, City> cities;

    public Province(String name, String countryName) {
        super(name);
        country = new Country(countryName);
        cities = new HashMap<>();
        Geonames geo =  new Geonames();
        geonamesLocationJson = new GeonamesJSON(geo.getGeonamesProvince(name, countryName));
        setCommonName();
    }
    @Override
    public double getLatSouth() {
        double answer = boundingBox.getLatSouth();
        if(answer != 361)
            return answer;
        else
            return country.getLatSouth();
    }
    @Override
    public double getLatNorth() {
        double answer = boundingBox.getLatNorth();
        if(answer != 361)
            return answer;
        else
            return country.getLatNorth();
    }
    @Override
    public double getLongWest() {
        double answer = boundingBox.getLongWest();
        if(answer != 361)
            return answer;
        else
            return country.getLongWest();
    }

    @Override
    public double getLongEast() {
        double answer = boundingBox.getLongEast();
        if(answer != 361)
            return answer;
        else
            return country.getLongEast();
    }

    public void addCity(City c){
        cities.put(c.getGivenName(),c);
    }

    public HashMap getCities(){
        return cities;
    }
}
