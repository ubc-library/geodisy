package Dataverse.FindingBoundingBoxes.LocationTypes;

import Dataverse.FindingBoundingBoxes.Location;
import Dataverse.FindingBoundingBoxes.LocationTypes.City;

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
    public double getLatMin() {
        if(latMin == 361)
            return city.getLatMin();
        else
            return latMin;
    }

    @Override
    public double getLatMax() {
        if(latMax == 361)
            return city.getLatMax();
        else
            return latMax;
    }

    @Override
    public double getLongMin() {
        if(longMin == 361)
            return city.getLongMin();
        else
            return longMin;
    }

    @Override
    public double getLongMax() {
        if(longMin == 361)
            return city.getLongMax();
        else
            return longMax;
    }
}
