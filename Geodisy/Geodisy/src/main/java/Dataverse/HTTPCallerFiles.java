package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.HTTPCaller;
import com.sun.mail.iap.ConnectionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HTTPCallerFiles extends HTTPCaller {

    public HTTPCallerFiles() {
        logger = new GeoLogger(this.getClass());
    }

    @Override
    protected void ioError(IOException e) {
        logger.error("Something went wrong trying to access HEADER call for file; IOError " + e);
    }

    @Override
    protected HttpURLConnection getHttpURLConnection(String searchUrl) {
        try {

            URL url = new URL(searchUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            con.setConnectTimeout(60000);
            con.setReadTimeout(20000);
            con.setInstanceFollowRedirects(false);
            return con;
        } catch (ProtocolException e) {
            logger.error("Something went wrong making the HTTP URL connection, Protocol exception");
        } catch (MalformedURLException e) {
            logger.error("Something went wrong making the HTTP URL connection, URL was malformed");
        } catch (IOException e) {
            logger.error("Something went wrong making the HTTP URL connection, IOException");
        }
        return null;
    }

    @Override
    protected String readResponse(HttpURLConnection con) {
        con.setDoOutput(true);
        int responseCode = 0;
        String answer = "";
        try {
            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                con.getInputStream().close();
                Map<String, List<String>> map = con.getHeaderFields();
                List words = map.get("Content-Length");
                if (words==null)
                    return "BAD_RESPONSE";
                answer = (String) words.get(0);
            } else {
                logger.error("HEADER request didn't work: Code = " + responseCode);
                return "BAD_RESPONSE";
            }
        } catch (SocketTimeoutException s) {
            logger.warn("Socket Timed out :" + s);
            return "BAD_RESPONSE";
        }catch (IOException e) {
            e.printStackTrace();
            ioError(e);
            return "BAD_RESPONSE";
        }
        return answer;
    }

    @Override
    public String callHTTP(String searchUrl) {
        String fixed = searchUrl.replaceAll(" ", "%20");
        int counter = 0;
        String answer = "0";
        boolean run = true;
        while (run && counter<5) {
            HttpURLConnection h = null;
            h = getHttpURLConnection(fixed);
            if (h == null)
                return "0";
            answer = readResponse(h);
            if(!answer.contains("Please add a username"))
                run = false;
            counter++;
        }
        return (answer.equals("BAD_RESPONSE")? "0":answer);
    }
}
