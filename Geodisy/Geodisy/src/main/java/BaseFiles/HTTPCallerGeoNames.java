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
    String responseCode;

    public HTTPCallerGeoNames() {
        logger = new GeoLogger(this.getClass());

    }


    //TODO don't think I need this
    private URL getURLSafeString(String searchUrl) throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        int location = searchUrl.indexOf("?q=")!=-1? searchUrl.indexOf("?q=")+ 3 : searchUrl.indexOf("?exporter=") + 10; //call list of dois or call for metadata records
        String search = URLEncoder.encode(searchUrl.substring(location), StandardCharsets.UTF_8.toString());
        String urlString = searchUrl.substring(0, location);
        urlString = urlString + search;
        urlString = urlString.replaceAll("%26", "&");
        urlString = urlString.replaceAll("%3D", "=");
        urlString = urlString.replaceAll("%252C%2520","%2C%20");
        urlString = urlString.replaceAll(" ", "%20");
        if(urlString.contains("doi%3"))
            urlString = urlString.replaceAll("%2F","/");
        URL url = new URL(searchUrl);
        return url;
    }


    @Override
    protected void ioError(){
        logger.error("Something went wrong getting a bounding box from an external source");
    }
}
