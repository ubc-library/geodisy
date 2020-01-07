package GeoServer;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJavaObject;


import java.io.IOException;


import static BaseFiles.GeodisyStrings.GEODISY_PATH_ROOT;
import static BaseFiles.GeodisyStrings.RASTER;
import static Dataverse.DVFieldNameStrings.PERSISTENT_ID;
import static GeoServer.GeoserverStrings.*;

public class PostGIS {
    GeoLogger logger;


    public PostGIS() {
        logger = new GeoLogger(this.getClass());
    }

    public void addFile2PostGIS(DataverseJavaObject djo, String fileName, String geoserverLabel, boolean test) {

        String vectorDB = test? TEST_VECTOR_DB : VECTOR_DB;
        String call = SHP_2_PGSQL + folderized(djo.getSimpleFieldVal(PERSISTENT_ID)) + "/" + fileName + " public." + geoserverLabel + PSQL_CALL + vectorDB + POSTGIS_USER_CALL;
        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.command("bash", "-c", call);
        try {
            processBuilder.start();
        } catch (IOException e) {
            logger.error("Something went wrong trying to get " + djo.getDOI()+fileName + " into postGID");
        }
    }

    private String folderized(String simpleFieldVal) {
        return simpleFieldVal.replace(".","/").replace("_","/");
    }
}

