package Dataverse;

import BaseFiles.GeoLogger;

import java.io.IOException;
import java.net.*;


public class HTTPCallerFiles {
    protected GeoLogger logger;

    public HTTPCallerFiles() {
        logger = new GeoLogger(this.getClass());
    }

    protected void ioError(IOException e) {
        logger.error("Something went wrong trying to access HEADER call for file; IOError " + e);
    }

    public long getFileLength(String searchUrl) {
        try {
            URL url = new URL(searchUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            return con.getContentLengthLong();
        } catch (ProtocolException e) {
            logger.error("Something went wrong making the HTTP URL connection, Protocol exception");
        } catch (MalformedURLException e) {
            logger.error("Something went wrong making the HTTP URL connection, URL was malformed");
        } catch (IOException e) {
            logger.error("Something went wrong making the HTTP URL connection, IOException");
        }
        return -2;
    }
}
