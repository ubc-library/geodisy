package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
import BaseFiles.HTTPCaller;

import java.io.IOException;

public class HTTPCombineCaller extends HTTPCaller {

    public HTTPCombineCaller() {
        logger = new GeoLogger(this.getClass());
    }

    @Override
    protected void ioError(IOException e) {
        logger.error("Something went wrong trying to access GeoCombine; IOError " + e);

    }
}
