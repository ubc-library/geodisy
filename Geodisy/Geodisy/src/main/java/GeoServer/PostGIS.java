package GeoServer;

import BaseFiles.GeoLogger;
import BaseFiles.ProcessCall;
import Dataverse.DataverseJavaObject;
import _Strings.GeodisyStrings;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


import static _Strings.DVFieldNameStrings.PERSISTENT_ID;
import static _Strings.GeodisyStrings.DATA_DIR_LOC;
import static _Strings.GeoserverStrings.*;

public class PostGIS {
    GeoLogger logger;


    public PostGIS() {
        logger = new GeoLogger(this.getClass());
    }

    public boolean addFile2PostGIS(DataverseJavaObject djo, String fileName, String geoserverLabel) {


        String call = GeodisyStrings.replaceSlashes(SHP_2_PGSQL + folderized(djo.getSimpleFieldVal(PERSISTENT_ID)) + "/" + fileName + " " + POSTGRES_SCHEMA + geoserverLabel + PSQL_CALL + VECTOR_DB + POSTGIS_USER_CALL);
        System.out.println("Adding file to postgres with:" + call);
        ProcessCall pc = new ProcessCall();
        String results = "";
        try {
            results = pc.runProcess(call,10,TimeUnit.SECONDS,logger);

            if(!results.contains("Unable to convert data value to UTF-8")) {
                return true;
            } else {
                String[] encodings = new String[]{"LATIN1", "LATIN2", "LATIN3", "LATIN4", "LATIN5", "LATIN6", "LATIN7", "LATIN8", "LATIN9", "LATIN10", "BIG5", "WIN866", "WIN874", "WIN1250", "WIN1251", "WIN1252", "WIN1256", "WIN1258", "EUC_CN", "EUC_JP", "EUC_KR", "EUC_TW", "GB18030", "GBK", "ISO_8859_5", "ISO_8859_6", "ISO_8859_7", "ISO_8859_8", "JOHAB", "KOI", "MULE_INTERNAL", "SJIS", "SQL_ASCII", "UHC"};

                for(String en: encodings){
                    call = GeodisyStrings.replaceSlashes(SHP_2_PGSQL_ALT(en) + folderized(djo.getSimpleFieldVal(PERSISTENT_ID)) + "/" + fileName + " " + POSTGRES_SCHEMA + geoserverLabel + PSQL_CALL + VECTOR_DB + POSTGIS_USER_CALL);
                    results = pc.runProcess(call, 10, TimeUnit.SECONDS, logger);
                    if (!results.contains("Unable to convert data value to UTF-8")) {
                        return true;
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
                logger.warn("Something went wrong trying to get " + djo.getPID() + fileName + " into postGIS");
            } catch (TimeoutException e) {
                logger.warn("Timed out trying to run SHP_2_PGSQL with encoding: UTF-8, see: " + e);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        logger.error("Something went wrong trying to get " + djo.getPID() + fileName + " into postGIS, couldn't find a working encoding");
        return false;
    }

    private String folderized(String simpleFieldVal) {
        return  GeodisyStrings.replaceSlashes(GeodisyStrings.removeHTTPSAndReplaceAuthority(simpleFieldVal).replace(".","/").replace("_","/"));
    }

    class SHP2PGSQL implements Callable<String>{
        String call;

        public SHP2PGSQL(String call) {
            this.call = call;
        }

        @Override
        public String call() throws Exception {
            List<String> args = new LinkedList<>();
            args.add("/usr/bin/bash");
            args.add("-c");
            args.add(call);
            ProcessBuilder processBuilder = new ProcessBuilder();
            Process p = processBuilder.start();
            StringBuilder result = new StringBuilder();
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                p.waitFor();
                p.destroy();
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException();
            } catch (InterruptedException e) {
                throw new IOException();
            }
            return result.toString();
        }
    }
}

