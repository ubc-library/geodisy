package GeoServer;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJavaObject;
import Dataverse.GDAL;


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

    public boolean addFile2PostGIS(DataverseJavaObject djo, String fileName, String geoserverLabel, boolean test) {


        String call = SHP_2_PGSQL + folderized(djo.getSimpleFieldVal(PERSISTENT_ID)) + "/" + fileName + " public." + geoserverLabel + PSQL_CALL + VECTOR_DB + POSTGIS_USER_CALL;
        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.command("bash", "-c", call);
        try {
            processBuilder.start();
            GeoServerAPI geo = new GeoServerAPI(djo);
            geo.addPostGISLayer(geoserverLabel,fileName);
            return true;
        } catch (IOException e) {
            logger.error("Something went wrong trying to get " + djo.getDOI()+fileName + " into postGIS");
            return false;
        }
    }

    private String folderized(String simpleFieldVal) {
        return simpleFieldVal.replace(".","/").replace("_","/");
    }
}

