package Dataverse.FindingBoundingBoxes.LocationTypes;

import Dataverse.FindingBoundingBoxes.Location;

public class Province extends Location {
    private Country country;

    public Province(String name, String countryName) {
        super(name);
        country = new Country(countryName);
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
}
