package Crosswalking.GeoBlacklightJson;

import BaseFiles.HTTPCaller;

public class HTTPCombineCaller extends HTTPCaller {

    @Override
    protected void ioError() {
        logger.error("Something went wrong trying to access GeoCombine; IOError");

    }
}
