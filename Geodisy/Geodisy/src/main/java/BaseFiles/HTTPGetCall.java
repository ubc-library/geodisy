package BaseFiles;

import Dataverse.DataverseRecordFile;
import java.io.FileInputStream;

import _Strings.GeodisyStrings;
import io.netty.handler.timeout.TimeoutException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static _Strings.GeodisyStrings.*;



public class HTTPGetCall {
    GeoLogger logger;
    CloseableHttpClient client;

    public HTTPGetCall() {
        logger = new GeoLogger(this.getClass().toString());
        client = HttpClients.createDefault();
    }

    public void getFile(String fileURL, String fileName, String path) throws IOException {

        if (fileURL.startsWith("ftp://") || fileURL.startsWith("http://ftp") || fileURL.startsWith("https://ftp")) {
            getFTPFile(fileURL, fileName, path);
            return;
        }
        makeCurlCall(fileURL, fileName, path);
    }

    private void makeCurlCall(String fileURL, String fileName, String path) throws IOException {
        BufferedInputStream in = null;
        FileOutputStream fos = null;
        BufferedOutputStream bout = null;
        File loc = new File(path);
        if(!loc.exists())
            loc.mkdirs();
        try{
            URL url=new URL(fileURL);
            URLConnection connection = url.openConnection();
            if(fileURL.contains("data.globus.org"))
                connection.setRequestProperty("Referer", url.toExternalForm());
            in = new java.io.BufferedInputStream(connection.getInputStream());
            fos = new java.io.FileOutputStream(path+fileName);
            bout = new BufferedOutputStream(fos,2048);
            byte[] data = new byte[2048];
            int i=0;
            int counter = 1;
            long tots = 0;
            while((i=in.read(data,0,2048))>=0) {
                bout.write(data,0,i);
                if(counter%10000 == 0)
                    System.out.print(".");
                counter +=1;
                tots += 2048;
                if(tots >= 20000000000L){
                    deleteFile(path+fileName);
                    logger.info(path+fileName, fileURL);
                    break;
                }
            }
        } finally {
            try {
                if(bout!=null)
                    bout.close();
            } catch (IOException e) {
                logger.error("Something went wrong trying to close bout when downloading from " + fileURL);
            }
            try {
                if(in!=null)
                    in.close();
            } catch (IOException e) {
                logger.error("Something went wrong trying to close in when downloading from " + fileURL);
            }
            try {
                if(fos!=null)
                    fos.close();
            } catch (IOException e) {
                logger.error("Something went wrong trying to close fos when downloading from " + fileURL);
            }
        }


    }

    private void deleteFile(String s) {
        try {
            Files.deleteIfExists(Paths.get(s));
        } catch (IOException e) {
            logger.error("Something went wrong trying to delete " + s);
        }
    }

    private void getFTPFile(String fileURL, String fileName, String path) {
        System.out.println("File is on an FTP");
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
            if(url.startsWith("ftp://")||url.startsWith("http://ftp")||url.startsWith("https://ftp")|| GeodisyStrings.checkIfOpenDataSoftRepo(url)) {
                list.add(drf);
                continue;
            }
            try {
                uri = URI.create(drf.getFileURL());
                HttpGet request = new HttpGet(uri);
                if(drf.getFileURL().contains("data.globus.org"))
                    request.setHeader(HttpHeaders.REFERER,drf.getFileURL());
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
                        logger.error("When checking headers, " + "Something weird with the file length of " + drf.getTranslatedTitle() + ": " + current + "at " + drf.getRecordURL());
                        list.add(drf);
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
                    logger.error("When checking headers, " + drf.getRecordURL() + " had a ClientProtocolError (pID: " + pID + ")");
                    list.add(drf);
                } catch (IOException e) {
                    logger.error("When checking headers, " + drf.getRecordURL() + " had a IOException (pID: " + pID + ")");
                    list.add(drf);
                } finally {
                        request.abort();
                }

            } catch (NullPointerException e) {
                logger.error("When checking headers, " + "FileURL was null for pID = " + pID);
            } catch (IllegalArgumentException e){
                logger.error("When checking headers, " + "Something was wrong with the fileURL (" + drf.getFileURL() + ") from pID: " + pID);
            }
        }
        return list;
    }
}
