package BaseFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.TimeUnit;

public abstract class HTTPCaller {
    protected GeoLogger logger;

    public String callHTTP(String searchUrl) {
        String fixed = searchUrl.replaceAll(" ", "%20");
        int counter = 0;
        String answer = "";
        boolean run = true;
        while (run && counter<5) {
            HttpURLConnection h = null;
                h = getHttpURLConnection(fixed);
            if (h == null)
                return "HTTP Fail";
            answer = readResponse(h,fixed);
            if(!answer.contains("Please add a username"))
                run = false;
            counter++;
        }
        return (answer.equals("BAD_RESPONSE")? "HTTP Fail":answer);
    }

    protected HttpURLConnection getHttpURLConnection(String searchUrl) {
        try {

            URL url = new URL(searchUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(60000);
            con.setReadTimeout(20000);
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

    protected String readResponse(HttpURLConnection con, String request) {
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
                logger.error("GET request didn't work: Code = " + responseCode + " Call = " + request);
                return "BAD_RESPONSE";
            }
        } catch (SocketTimeoutException s) {
            logger.warn("Socket Timed out :" + s + " Request was " + request);
        }catch (IOException e) {
            e.printStackTrace();
            ioError(e);
        }
        return answer;
    }
    protected String retryReading(HttpURLConnection con) {
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

                continue;
            }
            if(!answer.isEmpty())
                return answer;
        }
        if(answer.isEmpty())
            logger.warn("Http call " + con.toString() + " failed repeatedly.");
        return answer;
    }

    protected abstract void ioError(IOException e);
}
