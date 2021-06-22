package _Strings;

public class ENDPOINT_STRINGS {
    public static final String GEOSERVER_BASE_DEV       = "{test backend VM url to access geoserver, e.g. https://206.12.95.20/}"; 
    public static final String GEOSERVER_BASE_PROD      = "{prod backend VM url to access geoserver, e.g. https://206.12.95.20/geoserver/}";

    public static final String FRONTEND_DEV_ADDRESS     = "{test frontend VM url to access geoserver, e.g. https://206.12.95.20}";
    public static final String FRONTEND_PROD_ADDRESS    = "{prod frontend VM url to access geoserver, e.g. https://206.12.95.20}";

    public static final String BACKEND_DEV_ADDRESS      = "{test backend url stub, e.g. geoservertest.frdr.ca}";
    public static final String BACKEND_PROD_ADDRESS     = "{prod backend url stub, e.g. prod-gs-g1.frdr.ca}";

    public static final String DATA_DIR_LOC_LOCAL       = "{test location to store data, e.g. on a Windows machine D:/geodata/geoserver/data/downloads/}";
    public static final String DATA_DIR_LOC_CLOUD       = "{prod location to store data, e.g. on a linux machine /geodata/geoserver/data/downloads/";

    public static final String WINDOWS_ROOT             = "{root location of java program, e.g. windows machine D:\\Work\\Geodisy\\Geodisy\\}";
    public static final String VM_LINUX_ROOT            = "{root location of java program for linux, ./}";

    public static String DEV_HARVESTER_BASE             = "{URl of test dataverse repo download api endpoint, https://dev3.frdr.ca/harvestapi/";
    public static String PROD_HARVESTER_BASE            = "{URL of prod dataverse repo download api endpoint, https://www.frdr-dfdr.ca/harvestapi/";

    public static String OPEN_GEO_METADATA_URL          = "{URL of your OGM git repo, e.g. https://github.com/OpenGeoMetadata/ca.frdr.geodisy/}";

    //FOR testing on TEST

    //public static String DEV_HARVESTER_BASE             = "https://test.frdr.ca/harvestapi/";
}
