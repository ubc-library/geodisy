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
    public double getLatMin() {
        if(latMin == 361)
            logger.error("No latMin for %s, returning 361", name);
        return latMin;
    }

    @Override
    public double getLatMax() {
        if(latMax == 361)
            logger.error("No latMax for %s, returning 361", name);

        return latMax;
    }

    @Override
    public double getLongMin() {
        if(longMin == 361)
            logger.error("No longMin for %s, returning 361", name);
        return longMin;
    }

    @Override
    public double getLongMax() {
        if(longMin == 361)
            logger.error("No logMax for %s, returning 361", name);
        return longMax;
    }
}
