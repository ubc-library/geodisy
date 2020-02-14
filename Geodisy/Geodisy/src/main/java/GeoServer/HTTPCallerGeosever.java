package GeoServer;

import BaseFiles.GeoLogger;
import BaseFiles.HTTPCaller;

import java.io.IOException;

public class HTTPCallerGeosever extends HTTPCaller {

    public HTTPCallerGeosever() {
        logger = new GeoLogger(this.getClass());
    }

    @Override
    protected void ioError(IOException e) {
        logger.error("Something went wrong trying to access Geoserver; IOError " + e);
    }

    public String createWorkSpace(String url){
        return callHTTP(url);
    }

    public String deleteWorkSpace(String url){
        return callHTTP(url);
    }

}
