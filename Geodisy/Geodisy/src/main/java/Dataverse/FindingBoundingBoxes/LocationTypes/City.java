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
        if(latSouth != 361)
            return latSouth;
        else
            return province.getLatSouth();
    }
    @Override
    public double getLatNorth() {
        if(latNorth != 361)
            return latNorth;
        else
            return province.getLatNorth();
    }
    @Override
    public double getLongWest() {
        if(longWest != 361)
            return longWest;
        else
            return province.getLongWest();
    }

    @Override
    public double getLongEast() {
        if(longEast != 361)
            return longEast;
        else
            return province.getLongEast();
    }
}
