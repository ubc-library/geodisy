package GeoServer;

import BaseFiles.GeoLogger;
import BaseFiles.HTTPCaller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HTTPCallerGeosever extends HTTPCaller {

    public HTTPCallerGeosever() {
        logger = new GeoLogger(this.getClass());
    }

    @Override
    protected void ioError(IOException e) {
        logger.error("Something went wrong trying to access Geoserver; IOError " + e);
    }

    public String createWorkSpace(String url) {
        return callHTTP(url);
    }

    public String deleteWorkSpace(String url) {
        return callHTTP(url);
    }

    @Override
    protected HttpURLConnection getHttpURLConnection(String searchUrl) {
        try {
            URL url = new URL(searchUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(60000);
            con.setReadTimeout(20000);
            con.setRequestProperty("Cookie", "troute=t1");
            con.setFollowRedirects(true);

            return con;
        } catch (ProtocolException e) {
            logger.error("Something went wrong protocol-wise calling " + searchUrl);
        } catch (MalformedURLException e) {
            logger.error("Something went wrong URL-wise calling " + searchUrl);
        } catch (IOException e) {
            logger.error("Something went wrong IO-wise calling " + searchUrl);
        }
        return null;
    }
}
