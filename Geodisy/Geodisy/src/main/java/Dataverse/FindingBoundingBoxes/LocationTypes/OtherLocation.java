package Dataverse.FindingBoundingBoxes.LocationTypes;

import Dataverse.FindingBoundingBoxes.Location;

public class OtherLocation extends Location {
    private City city;
    public OtherLocation(String name) {
        super(name);
        city = new City(NO_NAME,NO_NAME);
    }

    public OtherLocation(String name, String countryName){
        super(name);
        city = new City(NO_NAME, countryName);
    }

    public OtherLocation(String name, String provinceName, String countryName){
        super(name);
        city = new City(NO_NAME, provinceName, countryName);
    }

    public OtherLocation(String name, String cityName, String provinceName, String countryName){
        super(name);
        city = new City(cityName,provinceName,countryName);
    }

    @Override
    public double getLatSouth() {
        if(latSouth == 361)
            return city.getLatSouth();
        else
            return latSouth;
    }

    @Override
    public double getLatNorth() {
        if(latNorth == 361)
            return city.getLatNorth();
        else
            return latNorth;
    }

    @Override
    public double getLongWest() {
        if(longWest == 361)
            return city.getLongWest();
        else
            return longWest;
    }

    @Override
    public double getLongEast() {
        if(longWest == 361)
            return city.getLongEast();
        else
            return longEast;
    }
}
