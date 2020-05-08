package Strings;

import static Strings.GeodisyStrings.getRoot;

public class GeoserverStrings {

    public static String WORKSPACE_NAME = "geodisy";
    public static String POSTGIS_BD = "";
    public static String VECTOR_DB = "vectordata";
    public static String LOCATION = "tbd";
    public static String SHP_2_PGSQL = "/usr/pgsql-12/bin/shp2pgsql -d " + getRoot() + "datasetFiles/";
    public static String PSQL_CALL = "| /usr/pgsql-12/bin/psql -d ";
    public static String POSTGIS_USER = "geodisy_user";
    public static String POSTGIS_USER_CALL = " -U " + POSTGIS_USER;
    public static String POSTGRES_SCHEMA = " public.";

}
