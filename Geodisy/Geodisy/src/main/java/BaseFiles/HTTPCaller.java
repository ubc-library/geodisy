package BaseFiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Class for making HTTP calls and getting JSON string responses
 */
public class HTTPCaller {
    String searchUrl;
    Logger logger = LogManager.getLogger(this.getClass());

    public HTTPCaller(String searchUrl) {
        this.searchUrl = searchUrl;

    }

    private HttpURLConnection getHttpURLConnection() {
        try {

            URL url = new URL(searchUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            return con;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readResponse(HttpURLConnection con){
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
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Something went wrong getting a bounding box from an external source");
        }
        return answer;
    }

    public String getJSONString(){
        HttpURLConnection h = getHttpURLConnection();
        return readResponse(h);
    }
}
