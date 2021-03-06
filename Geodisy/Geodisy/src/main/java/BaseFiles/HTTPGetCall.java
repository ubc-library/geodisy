package BaseFiles;

import Dataverse.DataverseRecordFile;
import _Strings.GeodisyStrings;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;


import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class HTTPGetCall {
    GeoLogger logger;
    CloseableHttpClient client;

    public HTTPGetCall() {
        logger = new GeoLogger(this.getClass().toString());
        client = HttpClients.createDefault();
    }

    public void getFile(String fileURL, String fileName, String path) {
        if (fileURL.startsWith("ftp://") || fileURL.startsWith("http://ftp") || fileURL.startsWith("http://ftp")) {
            getFTPFile(fileURL, fileName, path);
            return;
        }

        // 5 minute timeout
        int millisecond = 1000;
        int minutes = 60;
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5 * millisecond)
                .setConnectTimeout(10 * millisecond)
                .setSocketTimeout(5 * minutes * millisecond)
                .build();

        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        try {
            URI uri = URI.create(fileURL);
            HttpGet request = new HttpGet(uri);


            //Setting a hard stop for the download
            int hardTimeout = 10; //minutes
            String finalFileName = fileName;
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (request != null) {
                        request.abort();
                        logger.warn(finalFileName + " from " + fileURL + "timed our during donwload. Do we need this file?");
                        try {
                            Files.deleteIfExists(Paths.get(path+ finalFileName));
                        } catch (IOException ioException) {
                            logger.error("Something went wrong trying delete incomplete download " + finalFileName + " from " + fileURL);
                        }
                    }
                }
            };
            new Timer(true).schedule(task, hardTimeout * 60 * 1000);
            
            CloseableHttpResponse response = client.execute(request);
            if (fileName.equals("unknown")) {
                if (response.getFirstHeader("Content-Disposition") != null)
                    fileName = response.getFirstHeader("Content-Disposition").getValue().replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1");
            }
            try {

                // Get HttpResponse Status
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    long len = entity.getContentLength();
                    if (len < 5000000000L) {
                        // return it as a String
                        InputStream is = entity.getContent();
                        File loc = new File(path);
                        if (!loc.exists())
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
            }finally{
                    try {
                        response.close();
                        client.close();
                    } catch (IOException e) {
                        logger.error("Something went wrong trying close the response and client from  " + fileName + " at " + fileURL);
                    }
            }
        }catch (SocketTimeoutException e){
            logger.warn(fileName + " from " + fileURL + "timed our during donwload. Do we need this file?");
            try {
                Files.deleteIfExists(Paths.get(path+fileName));
            } catch (IOException ioException) {
                logger.error("Something went wrong trying delete incomplete download " + fileName + " from " + fileURL);
            }
        }catch(IOException e){
            logger.error("Something went wrong trying download " + fileName + " from " + fileURL);
            try {
                Files.deleteIfExists(Paths.get(path+fileName));
            } catch (IOException ioException) {
                logger.error("Something went wrong trying delete failed download " + fileName + " from " + fileURL);
            }
        }
    }

    private void getFTPFile(String fileURL, String fileName, String path) {
        //TODO Figure out how to pull files from an FTP
    }

    /**
     *  Checks to make sure no file is >5GB and no dataset is >100GB
     * @param dataset
     * @param pID
     * @return
     */

    public LinkedList<DataverseRecordFile> checkDataset(LinkedList<DataverseRecordFile> dataset, String pID){
        URI uri = null;
        LinkedList<DataverseRecordFile> list = new LinkedList<>();
        long total = 0L;
        for(DataverseRecordFile drf: dataset) {
            String url = drf.getFileURL();
            if(url.startsWith("ftp://")||url.startsWith("http://ftp")||url.startsWith("http://ftp")) {
                list.add(drf);
                continue;
            }
            try {
                uri = URI.create(drf.getFileURL());
                HttpGet request = new HttpGet(uri);
                // 50 seconds timeout
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectionRequestTimeout(5000)
                        .setConnectTimeout(10000)
                        .setSocketTimeout(1200000)
                        .build();
                request.setConfig(requestConfig);
                try {
                    CloseableHttpResponse response = client.execute(request);
                    long current = 0L;
                    try {
                        // Get HttpResponse Status
                        HttpEntity entity = response.getEntity();
                        if (entity == null | response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                            continue;
                        }
                        current = entity.getContentLength();
                        if (current > 5000000000L)
                            continue;
                        if (current == -1)
                            current = 0;
                        total += current;
                        list.add(drf);
                        if (total > 100000000000L) {
                            list = new LinkedList<>();
                            logger.warn("Dataset " + pID + " was too large to download.");
                            System.out.println("Dataset too large to download");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        logger.error("Something weird with the file length of " + drf.getTranslatedTitle() + ": " + current + "at " + drf.getRecordURL());
                    } finally {
                        try {
                            if (response != null) {
                                response.close();
                            }
                        } catch (IOException e) {
                            logger.error("Something went wrong closing the response from " + drf.getFileURL() + " when checking headers for " + pID);
                        }

                    }
                } catch (ClientProtocolException e) {
                    logger.error(drf.getRecordURL() + " had a ClientProtocolError (pID: " + pID + ")");
                } catch (IOException e) {
                    logger.error(drf.getRecordURL() + " had a IOException (pID: " + pID + ")");
                } finally {
                        request.abort();
                }

            } catch (NullPointerException e) {
                logger.error("FileURL was null for pID = " + pID);
            } catch (IllegalArgumentException e){
                logger.error("Something was wrong with the fileURL (" + drf.getFileURL() + ") from pID: " + pID);
            }
        }
        return list;
    }
}
