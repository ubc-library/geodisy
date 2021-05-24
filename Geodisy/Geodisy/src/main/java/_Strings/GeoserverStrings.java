package _Strings;

import static _Strings.GeodisyStrings.*;

public class GeoserverStrings {

    public static String WORKSPACE_NAME = "geodisy";
    public static String VECTOR_DB = "vectordata";
    public static String GEOSERVER_VECTOR_STORE = "vectordata";
    public static String LOCATION = "tbd";
    public static String SHP_2_PGSQL = "sudo /usr/pgsql-12/bin/shp2pgsql -d " + DATA_DIR_LOC;
    public static String PSQL_CALL = "| PGPASSWORD=" + POSTGIS_USER_PASSWORD +" /usr/pgsql-12/bin/psql -d ";
    public static String POSTGIS_USER = "geodisy_user";
    public static String POSTGIS_USER_CALL = " -U " + POSTGIS_USER;
    public static String POSTGRES_SCHEMA = "public.";
    public static String SHP_2_PGSQL_ALT(String encoding){
        return "sudo /usr/pgsql-12/bin/shp2pgsql -W \"" + encoding + "\" -d " + DATA_DIR_LOC;
    };

    public static void load(){
        SHP_2_PGSQL = "sudo /usr/pgsql-12/bin/shp2pgsql -d " + DATA_DIR_LOC;
    }

}
