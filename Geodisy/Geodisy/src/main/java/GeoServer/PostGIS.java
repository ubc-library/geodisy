package GeoServer;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJavaObject;
import _Strings.GeodisyStrings;


import java.io.IOException;


import static _Strings.DVFieldNameStrings.PERSISTENT_ID;
import static _Strings.GeoserverStrings.*;

public class PostGIS {
    GeoLogger logger;


    public PostGIS() {
        logger = new GeoLogger(this.getClass());
    }

    public boolean addFile2PostGIS(DataverseJavaObject djo, String fileName, String geoserverLabel) {


        String call = SHP_2_PGSQL + folderized(djo.getSimpleFieldVal(PERSISTENT_ID)) + "/" + fileName + " " + POSTGRES_SCHEMA + geoserverLabel + PSQL_CALL + VECTOR_DB + POSTGIS_USER_CALL;
        System.out.println("Call to add to postgres: " + call);
        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.command("/usr/bin/bash", "-c", call);
        Process p;
        try {
            p = processBuilder.start();
            try {
                p.waitFor();
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
        return  GeodisyStrings.replaceSlashes(GeodisyStrings.removeHTTPS(simpleFieldVal).replace(".","/").replace("_","/"));
    }
}

