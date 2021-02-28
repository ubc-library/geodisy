package BaseFiles;

import _Strings.GeodisyStrings;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import static _Strings.GeodisyStrings.DATA_DIR_LOC;

public class HTTPGetCall {
    GeoLogger logger = new GeoLogger(this.getClass().toString());

    public void getFile(String fileURL, String fileName, String path){
        CloseableHttpClient client = HttpClients.createDefault();
        URI uri = null;
        uri = URI.create(fileURL);
        HttpGet request = new HttpGet(uri);

        // 50 seconds timeout
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(50000)
                .setSocketTimeout(50000)
                .build();

        request.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(request);
            if(fileName.equals("unknown")) {
                if(response.getFirstHeader("Content-Disposition")!=null)
                    fileName = response.getFirstHeader("Content-Disposition").getValue().replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1");
            }
            try {

                // Get HttpResponse Status
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    long len = entity.getContentLength();
                    if(len<5000000000L) {
                        // return it as a String
                        InputStream is = entity.getContent();
                        File loc = new File(path);
                        if(!loc.exists())
                            loc.mkdirs();
                        String filePath = path + fileName;
                        File file = new File(filePath);
                        FileOutputStream fos = new FileOutputStream(file);
                        int inByte;
                        while ((inByte = is.read()) != -1)
                            fos.write(inByte);
                        is.close();
                        fos.close();
                    }
                }
            } finally {
                response.close();
            }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("Something went wrong trying to close the client for getting " + fileName + " from " + fileURL);
            }
        }
    }
}
