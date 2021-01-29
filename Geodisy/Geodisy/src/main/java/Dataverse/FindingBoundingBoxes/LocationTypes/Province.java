package Dataverse.FindingBoundingBoxes.LocationTypes;

import Dataverse.FindingBoundingBoxes.Location;

import java.util.HashMap;

public class Province extends Location {
    private Country country;
    private HashMap<String, City> cities = new HashMap<>();

    public Province(String name, String countryName) {
        super(countryName,name);
        this.country = new Country(countryName);
    }
    //placeholder province constructor
    public Province() {
        super();

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
