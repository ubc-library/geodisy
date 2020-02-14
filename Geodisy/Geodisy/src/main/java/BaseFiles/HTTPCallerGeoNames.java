package BaseFiles;

import com.sun.jndi.toolkit.url.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Class for making HTTP calls and getting JSON string responses
 */
public class HTTPCallerGeoNames extends HTTPCaller {

    public HTTPCallerGeoNames() {
        logger = new GeoLogger(this.getClass());
    }

    @Override
    protected void ioError(IOException e){
        logger.error("Something went wrong getting a bounding box from Geonames " + e);
    }
}
