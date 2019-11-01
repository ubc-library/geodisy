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
public class HTTPCaller {
    String searchUrl;
    GeoLogger logger = new GeoLogger(this.getClass());
    String responseCode;

    public HTTPCaller(String searchUrl) {
        this.searchUrl = searchUrl;

    }

    private HttpURLConnection getHttpURLConnection() {
        try {

            URL url = new URL(searchUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(20000);
            con.setReadTimeout(10000);
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
    //TODO don't think I need this
    private URL getURLSafeString() throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
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

    private String readResponse(HttpURLConnection con) {
        con.setDoOutput(true);
        int responseCode = 0;
        String answer = "";
        try {
            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                answer = response.toString();

            } else {
                logger.error("GET request didn't work");
                return "BAD_RESPONSE";
            }
        } catch (SocketTimeoutException s) {
            answer = retryReading(con);
        }catch (IOException e) {
            e.printStackTrace();
            logger.error("Something went wrong getting a bounding box from an external source");
        }
        return answer;
    }

    private String retryReading(HttpURLConnection con) {
        con.setDoOutput(true);
        int responseCode = 0;
        String answer = "";
        for(int i = 0; i<10; i++) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                logger.warn("retryReading failed when trying to sleep for 5 seconds between retries");
            }
            try {
                responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    answer = response.toString();

                } else {
                    logger.error("GET request didn't work");
                    return "BAD_RESPONSE";
                }
            } catch (SocketTimeoutException s) {
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Something went wrong getting a bounding box from an external source");
                continue;
            }
            if(!answer.isEmpty())
                return answer;
        }
        if(answer.isEmpty())
            logger.warn("Http call " + con.toString() + " failed repeatedly.");
        return answer;
    }

    public String getJSONString() {
        HttpURLConnection h = getHttpURLConnection();
        if(h==null)
            return "HTTP Fail";
        String answer = readResponse(h);
        return (answer.equals("BAD_RESPONSE")? "HTTP Fail":answer);
    }
}
