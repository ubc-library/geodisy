package GeoServer;

import static BaseFiles.GeodisyStrings.getRoot;

public class GeoserverStrings {

    static String WORKSPACE_NAME = "geodisy";
    public static String POSTGIS_BD = "";
    public static String VECTOR_DB = "vectordata";
    static String LOCATION = "tbd";
    static String SHP_2_PGSQL = "/usr/pgsql-12/bin/shp2pgsql -d " + getRoot() + "datasetFiles/";
    static String PSQL_CALL = "| /usr/pgsql-12/bin/psql -d ";
    private static String POSTGIS_USER = "geodisy_user";
    static String POSTGIS_USER_CALL = " -U " + POSTGIS_USER;
    public static String POSTGRES_SCHEMA = " public.";

}
