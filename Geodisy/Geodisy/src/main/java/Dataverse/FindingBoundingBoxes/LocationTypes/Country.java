package Dataverse.FindingBoundingBoxes.LocationTypes;

import Crosswalking.JSONParsing.DataverseParser;
import Dataverse.FindingBoundingBoxes.CountryCodes;
import Dataverse.FindingBoundingBoxes.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Country extends Location {
    private String countryCode;
    Logger logger = LogManager.getLogger(DataverseParser.class);

    public Country(String country) {
        super(country);
        this.countryCode = CountryCodes.getCountryCode(country);

    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public double getLatSouth() {
        if(latSouth == 361)
            logger.error("No latSouth for %s, returning 361", name);
        return latSouth;
    }

    @Override
    public double getLatNorth() {
        if(latNorth == 361)
            logger.error("No latNorth for %s, returning 361", name);

        return latNorth;
    }

    @Override
    public double getLongWest() {
        if(longWest == 361)
            logger.error("No longWest for %s, returning 361", name);
        return longWest;
    }

    @Override
    public double getLongEast() {
        if(longWest == 361)
            logger.error("No logMax for %s, returning 361", name);
        return longEast;
    }
}
