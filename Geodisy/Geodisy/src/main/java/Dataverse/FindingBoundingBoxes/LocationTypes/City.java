package Dataverse.FindingBoundingBoxes.LocationTypes;

import BaseFiles.Geonames;
import Dataverse.ExistingLocations;
import Dataverse.FindingBoundingBoxes.GeonamesJSON;
import Dataverse.FindingBoundingBoxes.Location;

public class City extends Location {
    private Province province;
    //TODO deal with higher levels of the location hierarchy if the lower level does or does not find a bounding box
    public City(String name, String provinceName, String countryName) {
        super(countryName,provinceName,name);
        this.province = new Province(provinceName, countryName);
    }
    //When no province is given, but city and country are;
    public City(String name, String countryName){
        super(countryName,name);
        province = new Province("", countryName);

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

    public String getProvinceName(){
        return province.getCommonName();
    }
}
