package GeoServer;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJavaObject;
import _Strings.GeodisyStrings;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;


import static _Strings.DVFieldNameStrings.PERSISTENT_ID;
import static _Strings.GeodisyStrings.DATA_DIR_LOC;
import static _Strings.GeoserverStrings.*;

public class PostGIS {
    GeoLogger logger;


    public PostGIS() {
        logger = new GeoLogger(this.getClass());
    }

    public boolean addFile2PostGIS(DataverseJavaObject djo, String fileName, String geoserverLabel) {


        String call = SHP_2_PGSQL + folderized(djo.getSimpleFieldVal(PERSISTENT_ID)) + "/" + fileName + " " + POSTGRES_SCHEMA + geoserverLabel + PSQL_CALL + VECTOR_DB + POSTGIS_USER_CALL;
        call = GeodisyStrings.replaceSlashes(call);
        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.command("/usr/bin/bash", "-c", call);
        processBuilder.redirectErrorStream(true);
        Process p;
        try {
            p = processBuilder.start();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null)
                    continue;
                p.waitFor(5, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                logger.error("Something went wrong trying to upload " + geoserverLabel + " to postgis");
            }finally{
                p.destroy();
            }
            p.destroy();
            return true;
        } catch (IOException e) {
            logger.error("Something went wrong trying to get " + djo.getPID() + fileName + " into postGIS");
        }
            return false;
    }

    private String folderized(String simpleFieldVal) {
        return  GeodisyStrings.replaceSlashes(GeodisyStrings.removeHTTPSAndReplaceAuthority(simpleFieldVal).replace(".","/").replace("_","/"));
    }
}

