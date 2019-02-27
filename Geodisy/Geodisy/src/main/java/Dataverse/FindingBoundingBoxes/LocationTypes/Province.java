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
        if(latSouth != 361)
            return latSouth;
        else
            return country.getLatSouth();
    }
    @Override
    public double getLatNorth() {
        if(latNorth != 361)
            return latNorth;
        else
            return country.getLatNorth();
    }
    @Override
    public double getLongWest() {
        if(longWest != 361)
            return longWest;
        else
            return country.getLongWest();
    }

    @Override
    public double getLongEast() {
        if(longEast != 361)
            return longEast;
        else
            return country.getLongEast();
    }
}
