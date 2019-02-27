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
    public double getLatMin() {
        if(latMin != 361)
            return latMin;
        else
            return province.getLatMin();
    }
    @Override
    public double getLatMax() {
        if(latMax != 361)
            return latMax;
        else
            return province.getLatMax();
    }
    @Override
    public double getLongMin() {
        if(longMin != 361)
            return longMin;
        else
            return province.getLongMin();
    }

    @Override
    public double getLongMax() {
        if(longMax != 361)
            return longMax;
        else
            return province.getLongMax();
    }
}
