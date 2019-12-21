package GeoServer;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJavaObject;


import java.io.IOException;


import static BaseFiles.GeodisyStrings.RASTER;
import static Dataverse.DVFieldNameStrings.PERSISTENT_ID;
import static GeoServer.GeoserverStrings.*;

public class PostGIS {
    GeoLogger logger;


    public PostGIS() {
        logger = new GeoLogger(this.getClass());
    }

    /**
     * Use this only if it is decided that Raster data should go into postGIS
     * @param djo
     * @param fileName
     * @param rasterOrVector
     * @param test
     */
    public void addFile2PostGIS(DataverseJavaObject djo, String fileName, String rasterOrVector, boolean test) {
        String call;
        String rasterDB = test? TEST_RASTER_DB : RASTER_DB;
        String vectorDB = test? TEST_VECTOR_DB : VECTOR_DB;
        if(rasterOrVector.equals(RASTER))
            call = "/usr/pgsql-12/bin/raster2pgsql -d /geodisy/" + folderized(djo.getSimpleFieldVal(PERSISTENT_ID)) + fileName + "public." + djo.getSimpleFieldVal(PERSISTENT_ID)+fileName + "| /usr/pgsql-12/bin/psql -d " + rasterDB + " -U geodisy_user";
        else
            call = "/usr/pgsql-12/bin/vector2pgsql -d /geodisy/" + folderized(djo.getSimpleFieldVal(PERSISTENT_ID)) + fileName + "public." + djo.getSimpleFieldVal(PERSISTENT_ID)+fileName + "| /usr/pgsql-12/bin/psql -d " + vectorDB + " -U geodisy_user";
        try {
            Runtime.getRuntime().exec(call);
        } catch (IOException e) {
            logger.error("Something went wrong trying to get " + djo.getDOI() + " into " + rasterOrVector);
        }
    }

    public void addFile2PostGIS(DataverseJavaObject djo, String fileName, boolean test) {

        String vectorDB = test? TEST_VECTOR_DB : VECTOR_DB;
        String call = "/usr/pgsql-12/bin/vector2pgsql -d /geodisy/" + folderized(djo.getSimpleFieldVal(PERSISTENT_ID)) + fileName + "public." + djo.getSimpleFieldVal(PERSISTENT_ID)+fileName + "| /usr/pgsql-12/bin/psql -d " + vectorDB + " -U geodisy_user";
        try {
            Runtime.getRuntime().exec(call);
        } catch (IOException e) {
            logger.error("Something went wrong trying to get " + djo.getDOI()+fileName + " into postGID");
        }
    }

    private String folderized(String simpleFieldVal) {
        return simpleFieldVal.replace(".","/");
    }
}
