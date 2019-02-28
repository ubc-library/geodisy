package Dataverse.FindingBoundingBoxes.LocationTypes;

import Dataverse.FindingBoundingBoxes.Location;

public class City extends Location {
    private Province province;

    public City(String name, String provinceName, String countryName) {
        super(name);
        province = new Province(provinceName,countryName);
    }

    public City(String name, String countryName){
        super(name);
        province = new Province("no name", countryName);
    }

    @Override
    public double getLatSouth() {
        double answer = boundingBox.getLatSouth();
        if(answer != 361)
            return answer;
        else
            return province.getLatSouth();
    }
    @Override
    public double getLatNorth() {
        double answer = boundingBox.getLatNorth();
        if(answer != 361)
            return answer;
        else
            return province.getLatNorth();
    }
    @Override
    public double getLongWest() {
        double answer = boundingBox.getLongWest();
        if(answer != 361)
            return answer;
        else
            return province.getLongWest();
    }

    @Override
    public double getLongEast() {
        double answer = boundingBox.getLongEast();
        if(answer != 361)
            return answer;
        else
            return province.getLongEast();
    }
}
