package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
import BaseFiles.HTTPCaller;

public class HTTPCombineCaller extends HTTPCaller {

    public HTTPCombineCaller() {
        logger = new GeoLogger(this.getClass());
    }

    @Override
    protected void ioError() {
        logger.error("Something went wrong trying to access GeoCombine; IOError");

    }
}
