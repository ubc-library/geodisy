package GeoServer;

import BaseFiles.GeoLogger;
import BaseFiles.ProcessCall;
import Dataverse.DataverseJavaObject;
import _Strings.GeodisyStrings;



import java.io.FileNotFoundException;
import java.util.concurrent.*;


import static _Strings.DVFieldNameStrings.PERSISTENT_ID;
import static _Strings.GeoserverStrings.*;

public class PostGIS {
    GeoLogger logger;


    public PostGIS() {
        logger = new GeoLogger(this.getClass());
    }

    public boolean addFile2PostGIS(DataverseJavaObject djo, String fileName, String geoserverLabel) {


        String call = GeodisyStrings.replaceSlashes(SHP_2_PGSQL + folderized(djo.getSimpleFieldVal(PERSISTENT_ID)) + "/" + fileName + " " + POSTGRES_SCHEMA + geoserverLabel + PSQL_CALL + VECTOR_DB + POSTGIS_USER_CALL);
        ProcessCall pc = new ProcessCall();
        String[] results;
        String error;
        try {
            results = pc.runProcess(call,10,TimeUnit.SECONDS,logger);
            error = results[1];
            if(!error.contains("Unable to convert data value to UTF-8")) {
                return true;
            } else {
                String[] encodings = new String[]{"LATIN1", "LATIN2", "LATIN3", "LATIN4", "LATIN5", "LATIN6", "LATIN7", "LATIN8", "LATIN9", "LATIN10", "BIG5", "WIN866", "WIN874", "WIN1250", "WIN1251", "WIN1252", "WIN1256", "WIN1258", "EUC_CN", "EUC_JP", "EUC_KR", "EUC_TW", "GB18030", "GBK", "ISO_8859_5", "ISO_8859_6", "ISO_8859_7", "ISO_8859_8", "JOHAB", "KOI", "MULE_INTERNAL", "SJIS", "SQL_ASCII", "UHC"};

                for(String en: encodings){
                    call = GeodisyStrings.replaceSlashes(SHP_2_PGSQL_ALT(en) + folderized(djo.getSimpleFieldVal(PERSISTENT_ID)) + "/" + fileName + " " + POSTGRES_SCHEMA + geoserverLabel + PSQL_CALL + VECTOR_DB + POSTGIS_USER_CALL);
                    pc = new ProcessCall();
                    results = pc.runProcess(call, 10, TimeUnit.SECONDS, logger);
                    error = results[1];
                    if (!error.contains("Unable to convert data value to UTF-8")) {
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
}

