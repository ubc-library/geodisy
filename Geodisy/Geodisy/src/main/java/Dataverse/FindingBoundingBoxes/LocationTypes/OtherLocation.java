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
        double answer = boundingBox.getLatSouth();
        if(answer == 361)
            return city.getLatSouth();
        else
            return answer;
    }

    @Override
    public double getLatNorth() {
        double answer = boundingBox.getLatNorth();
        if(answer == 361)
            return city.getLatNorth();
        else
            return answer;
    }

    @Override
    public double getLongWest() {
        double answer = boundingBox.getLongWest();
        if(answer == 361)
            return city.getLongWest();
        else
            return answer;
    }

    @Override
    public double getLongEast() {
        double answer = boundingBox.getLongEast();
        if(answer == 361)
            return city.getLongEast();
        else
            return answer;
    }
}
