package Dataverse.FindingBoundingBoxes.LocationTypes;

import Dataverse.FindingBoundingBoxes.Location;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;

public class Province extends Location {
    private Country country;

    public Province(String name, String countryName) {
        super(name);
        country = new Country(countryName);
    }
    @Override
    public double getLatMin() {
        if(latMin != 361)
            return latMin;
        else
            return country.getLatMin();
    }
    @Override
    public double getLatMax() {
        if(latMax != 361)
            return latMax;
        else
            return country.getLatMax();
    }
    @Override
    public double getLongMin() {
        if(longMin != 361)
            return longMin;
        else
            return country.getLongMin();
    }

    @Override
    public double getLongMax() {
        if(longMax != 361)
            return longMax;
        else
            return country.getLongMax();
    }
}
