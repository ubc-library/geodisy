package _Strings;

import org.apache.commons.lang3.ArrayUtils;

import static _Strings.PrivateStrings.*;


public class GeodisyStrings {
    public static void load() {
        DATA_DIR_LOC = dataDir();
        BACKEND_ADDRESS = (TEST)? BACKEND_DEV_ADDRESS: BACKEND_PROD_ADDRESS;
        FRONTEND_ADDRESS = (TEST)? FRONTEND_DEV_ADDRESS: FRONTEND_PROD_ADDRESS;
        END_XML_JSON_FILE_PATH = FRONTEND_ADDRESS + "/metadata/geodisy/";
        PATH_TO_XML_JSON_FILES = BACKEND_ADDRESS + "/geodisy/";
        GEODISY_PATH_ROOT = (IS_WINDOWS)? WINDOWS_ROOT: FRDR_VM_CENTOS_ROOT;
        MOVE_METADATA = "sudo rsync -au --delete " + GEODISY_PATH_ROOT + "metadata/* /var/www/" + BACKEND_ADDRESS + "/html/geodisy/";
        MOVE_DATA = "sudo rsync -au --delete " + GEODISY_PATH_ROOT + "datasetFiles/* " + DATA_DIR_LOC;
        GEOCOMBINE = "sh " + GEODISY_PATH_ROOT + "geodisyFiles/combine.sh";
        GITCALL = "sh " + GEODISY_PATH_ROOT + "geodisyFiles/git.sh";
        DELETE_XML = "sudo find " + GEODISY_PATH_ROOT + "metadata/ -name \"iso19139.xml\" -delete";
        SAVED_FILES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles");
        LOGS = GEODISY_PATH_ROOT + replaceSlashes("logs");
        EXISTING_CHECKS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingChecks.txt");
        EXISTING_DATASET_BBOXES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingDatasetBBoxes.txt");
        EXISTING_LOCATION_BBOXES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingLocationBBoxes.txt");
        EXISTING_LOCATION_NAMES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingLocationNames.txt");
        EXISTING_GEO_LABELS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingGeoLabels.txt");
        EXISTING_GEO_LABELS_VALS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingGeoLabelsVals.txt");
        DOWNLOADED_FILES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/DownloadedFiles.csv");
        VECTOR_RECORDS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingVectorRecords.txt");
        TEST_EXISTING_RECORDS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/TestExistingRecords.txt");
        TEST_EXISTING_BBOXES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/TestExistingBBoxes.txt");
        RASTER_RECORDS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingRasterRecords.txt");
        RECORDS_TO_CHECK = GEODISY_PATH_ROOT + replaceSlashes("logs/recordsToCheck.log");
        EXISTING_CALL_TO_CHECK = GEODISY_PATH_ROOT + replaceSlashes("logs/existingCallToCheck.txt");
        ERROR_LOG = GEODISY_PATH_ROOT + replaceSlashes("logs/error.log");
        WARNING_LOG = GEODISY_PATH_ROOT + replaceSlashes("logs/warning.log");
        COUNTRY_VALS =  GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/Geoname_countries.xml");
        ALL_CITATION_METADATA = GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/AllCitationMetadata.json");
        TEST_GEO_COVERAGE = GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/geocoverage.json");
        XML_TEST_FILE = GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/XMLTestDJO.xml");
        DATASET_FILES_PATH = replaceSlashes("datasetFiles/");
        ERROR_LOG = GEODISY_PATH_ROOT + replaceSlashes("logs/error.log");
        WARNING_LOG = GEODISY_PATH_ROOT + replaceSlashes("logs/warning.log");
        HARVESTER_BASE = (TEST)? DEV_HARVESTER_BASE: PROD_HARVESTER_BASE;
        EXPORTER = HARVESTER_BASE + "exporter";
        MARK_AS_PROCESSED = HARVESTER_BASE + "records/";
        GeoBlacklightStrings.load();
        XMLStrings.load();
        GeoserverStrings.load();
        GDALINFO = (IS_WINDOWS)? GDALINFO_LOCAL: GDALINFO_CLOUD;
        OGRINFO = (IS_WINDOWS)? OGRINFO_LOCAL: OGRINFO_CLOUD;
        OGR2OGR = (IS_WINDOWS)? OGR2OGR_LOCAL: OGR2OGR_CLOUD;
        GDAL_TRANSLATE = (IS_WINDOWS)? GDAL_TRANSLATE_LOCAL: GDAL_TRANSLATE_CLOUD;
    }

    public static boolean TEST; //This will be false if there are no arguments when calling the jar
    public final static boolean GEOSPATIAL_ONLY = false;
    public final static String GIT_PASSWORD = PRIVATE_GIT_PASSWORD;
    public final static String GEOSERVER_PASSWORD = PRIVATE_GEOSERVER_PASSWORD;
    public final static String GEOSERVER_USERNAME = PRIVATE_GEOSERVER_USERNAME;
    public final static String GEONAMES_USERNAME = PRIVATE_GEONAMES_USERNAME;
    public final static String POSTGIS_USER_PASSWORD = PRIVATE_POSTGIS_USER_PASSWORD;
    public final static String OPENGEOMETADATA_USERNAME = PRIVATE_OPENGEOMETADATA_USERNAME;
    public final static String OPENGEOMETADATA_PASSWORD = PRIVATE_OPENGEOMETADATA_PASSWORD;
    public final static int NUMBER_OF_RECS_TO_HARVEST = 0;
    //These records will not have files downloaded for now
    public final static String[] HUGE_RECORDS_TO_IGNORE_UNTIL_LATER = {"10.5683/SP2/FJWIL8","10.5683/SP/Y3HRN","10864/GSSJX","10.5683/SP2/JP4WDF","10864/9KJ1L","10864/11086","10864/9VNIK","10.5683/SP/Y3HMRN","10.5683/SP/OEIP77","10.5683/SP/IP9ERW","10.5683/SP/NTUOK9","10864/11369","10864/11175","10.5683/SP/BT7HN2","10.5683/SP/4RFHBJ","10.5683/SP/T7ZJAF","10.5683/SP/RZM9HE","10.5683/SP/RAJQ2P","10.5683/SP2/AAGZDG","10.5683/SP2/1XRF9U","10.5683/SP2/MICSLT", "10.5683/SP2/5T2RD9", "10.5683/SP2/FVQSQG","11272.1/AB2/TSDGES","11272.1/AB2/7HKU08","10.5683/SP2/1XRF9U","11272.1/AB2/GMEFD3","10.5683/SP2/TTJNIU","10.5683/SP2/UCCFVQ"};
    public final static String[] PROCESS_THESE_DOIS = {};// { "10.5683/SP2/M0Q0JB","10.5683/SP2/J8581N","10.5683/SP2/JISB1K","10.5683/SP2/YPVTYT","10.5682/SP2/PONAP6"}; //"10.5683/SP2/UEJGTV" "10864/11669" "10.5683/SP2/GKJPIQ""10.5683/SP2/KYHUNF""10.5683/SP/EGOYE3""10.5683/SP2/LAWLTI""10.5072/FK2/PLD5VK","10.5683/SP2/UEJGTV","10.5683/SP/SBTXLS","10.5683/SP/UYBROL","10864/XER6B","10864/10197""10.5683/SP/OZ0WB0","10.5683/SP/S0MQVP","10.5683/SP/5S5Y9T","10.5683/SP/30JPOR","10.5683/SP/ASR2XE","10.5683/SP2/1VWZNC","10.5683/SP/AB5A9O","10.5683/SP2/YNOJSD","10.5683/SP/AB5A9O","10.5683/SP/2ZARY2","10.5683/SP2/ZDAHQG","10.5683/SP2/JFQ1SZ"
    //Repositories (add new repository URLS to a appropriate repository URL array below)
        // New Repository Types need new URL Arrays [Geodisy 2]

        public final static String TEST_SCHOLARS_PORTAL = "https://demodv.scholarsportal.info/";
        public final static String SCHOLARS_PORTAL = "https://dataverse.scholarsportal.info/"; //Don't use this unless SP gives approval
        public final static String SCHOLARS_PORTAL_CLONE = "https://dvtest.scholarsportal.info/";
        public static String[] DATAVERSE_URLS(){return getServers();}

        private static String[] getServers() {
                if(TEST)
                    return new String[]{TEST_SCHOLARS_PORTAL};
                else
                    return new String[]{SCHOLARS_PORTAL};
        }

    public static boolean windowsComputerType(){
            return  System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
        }

        public final static boolean IS_WINDOWS = windowsComputerType();

    //Flask values
        public static String DEV_HARVESTER_BASE = ENDPOINT_STRINGS.DEV_HARVESTER_BASE;
        public static String PROD_HARVESTER_BASE = ENDPOINT_STRINGS.PROD_HARVESTER_BASE;
        public static String HARVESTER_BASE;
        public static String EXPORTER;
        public static String MARK_AS_PROCESSED;

        public static String harvesterBase(){
            if (TEST) {
                return DEV_HARVESTER_BASE;
            } else
                return PROD_HARVESTER_BASE;
        }

    //File paths
        private final static String WINDOWS_ROOT = ENDPOINT_STRINGS.WINDOWS_ROOT;
        private final static String FRDR_VM_CENTOS_ROOT = ENDPOINT_STRINGS.FRDR_VM_CENTOS_ROOT;
        public static String GEODISY_PATH_ROOT;
        public static String SAVED_FILES;
        public static String LOGS;
        public static String EXISTING_CHECKS;
        public static String EXISTING_DATASET_BBOXES;
        public static String EXISTING_LOCATION_BBOXES;
        public static String EXISTING_LOCATION_NAMES;
        public static String EXISTING_GEO_LABELS;
        public static String EXISTING_GEO_LABELS_VALS;
        public static String DOWNLOADED_FILES;
        public static String VECTOR_RECORDS;
        public static String TEST_EXISTING_RECORDS;
        public static String TEST_EXISTING_BBOXES;
        public static String RASTER_RECORDS;
        public static String RECORDS_TO_CHECK;
        public static String EXISTING_CALL_TO_CHECK;
        public static String ERROR_LOG;
        public static String WARNING_LOG;
        public final static String XML_NS = "http://www.isotc211.org/2005/";
        public static String COUNTRY_VALS;
        public static String ALL_CITATION_METADATA;
        public static String TEST_GEO_COVERAGE;
        public static String XML_TEST_FILE;
        public static String DATASET_FILES_PATH = replaceSlashes("datasetFiles/");
        public final static String OPEN_GEO_METADATA_BASE = "https://github.com/OpenGeoMetadata/ca.frdr.geodisy/";
        public final static String ISO_19139_XML = "iso19139.xml";

    //Geonames
        public final static String GEONAMES_SEARCH_BASE = "https://secure.geonames.net/search?q=";


    //GDAL
        private final static String LOCAL_GDAL_PATH = "C:\\\"Program Files\"\\python39\\Lib\\site-packages\\osgeo\\";
        private final static String GDALINFO_LOCAL = LOCAL_GDAL_PATH + "gdalinfo -approx_stats ";
        private final static String OGRINFO_LOCAL = LOCAL_GDAL_PATH + "ogrinfo -ro -al -so ";
        private final static String GDALINFO_CLOUD = "/usr/gdal30/bin/gdalinfo -approx_stats ";
        private final static String OGRINFO_CLOUD = "/usr/gdal30/bin/ogrinfo -ro -al -so ";
        public static String GDALINFO;
        public static String OGRINFO;
        private final static String[] GDALINFO_PROCESSABLE_EXTENSIONS = { ".tif", ".tiff",".xyz", ".png"};
        private final static String[] NON_TIF_GEOTIFF_EXTENSIONS = {".aux.xml",".tab",".twf",".tifw", ".tiffw",".wld", ".tif.prj",".tfw"};
        public final static String[] GDALINFO_RASTER_FILE_EXTENSIONS = ArrayUtils.addAll(GDALINFO_PROCESSABLE_EXTENSIONS,NON_TIF_GEOTIFF_EXTENSIONS);
        public final static String[] NON_SHP_SHAPEFILE_EXTENSIONS = {".shx", ".dbf", ".sbn",".prj"};
        private final static String[] OGRINFO_PROCESSABLE_EXTENTIONS = {".geojson",".shp",".gpkg"}; //also .csv/.tab, but need to check if the csv is actually geospatial in nature
        private final static String[] CSV_EXTENTIONS = {".csv", ".tab"};
        private final static String[] LIDAR_EXTENSION = {".las",".laz",".zlas",".e57"};
        public static boolean isLidar(String title){
            for(String s: LIDAR_EXTENSION){
                if(title.endsWith(s))
                    return true;
            }
            return false;
        }
        private final static String[] INTERIM_VECTOR = ArrayUtils.addAll(OGRINFO_PROCESSABLE_EXTENTIONS,CSV_EXTENTIONS);
        public final static String[] OGRINFO_VECTOR_FILE_EXTENSIONS = ArrayUtils.addAll(NON_SHP_SHAPEFILE_EXTENSIONS, INTERIM_VECTOR);
        public final static String FINAL_OGRINFO_VECTOR_FILE_EXTENSIONS = ".shp";
        public final static String[] PREVIEWABLE_FILE_EXTENSIONS = {".tif"};
        private final static String OGR2OGR_LOCAL = LOCAL_GDAL_PATH + "ogr2ogr -f \"ESRI Shapefile\" -t_srs EPSG:4326  -lco ENCODING=UTF-8 ";
        private final static String GDAL_TRANSLATE_LOCAL = LOCAL_GDAL_PATH + "gdal_translate -of GTiff ";
        private final static String OGR2OGR_CLOUD = "/usr/gdal30/bin/ogr2ogr -t_srs EPSG:4326 -f \"ESRI Shapefile\" -lco ENCODING=UTF-8 ";
        //GDAL for Raster conversion needs to be using GDAL version 2.x, so had to use a docker version of it for use with Centos
        //public final static String GDAL_DOCKER = "sudo docker run --rm -v /home:/home osgeo/gdal:alpine-ultrasmall-v2.4.1 "; //base call for docker gdal, but need the program call added on
        private final static String GDAL_TRANSLATE_CLOUD = "/usr/gdal30/bin/gdal_translate -of GTiff ";
        public static String OGR2OGR;
        public static String GDAL_TRANSLATE;
        public final static String RASTER_CRS = "EPSG:3857";
        public static String GDALWARP(String path,String fileName){ return replaceSlashes(getGdalWarp(path,fileName));}
        public static String GDAL_WARP_LOCAL(String path, String filename){ return LOCAL_GDAL_PATH + "gdalwarp -overwrite -t_srs " + RASTER_CRS +" -r near -multi -co TILED=YES -co COMPRESS=LZW " + path + filename +" " + path + "1" + filename;}

        public static String GDAL_WARP_CLOUD(String path, String fileName){
        return "sudo /usr/gdal30/bin/gdalwarp -overwrite -t_srs "+ RASTER_CRS +" -r near -multi -co TILED=YES -co COMPRESS=LZW " + path + fileName + " " + path + "1"+ fileName; }

        public static String GDALADDO(String source){ return getGdalAddo(source);}
        public static String GDAL_ADDO_LOCAL(String source){return LOCAL_GDAL_PATH + "gdaladdo " + source + " -r near --config COMPRESS_OVERVIEW LZW 2 4 8 16 32 64 128";}
        public static String GDAL_ADDO_CLOUD(String source){return "sudo /usr/gdal30/bin/gdaladdo " + source + " -r near --config COMPRESS_OVERVIEW LZW 2 4 8 16 32 64 128";}
        public final static String[] PROCESSABLE_EXTENSIONS = ArrayUtils.addAll(ArrayUtils.addAll(GDALINFO_PROCESSABLE_EXTENSIONS,OGRINFO_PROCESSABLE_EXTENTIONS),CSV_EXTENTIONS);

        private static String getGdalWarp(String path, String fileName){
            if(IS_WINDOWS)
                return GDAL_WARP_LOCAL(path, fileName);
            else
                return GDAL_WARP_CLOUD(path, fileName);
        }

        private static String getGdalAddo(String source){
            if(IS_WINDOWS)
                return GDAL_ADDO_LOCAL(source);
            else
                return GDAL_ADDO_CLOUD(source);
        }
    
    public static String replaceSlashes(String s){
        if(IS_WINDOWS)
            return s.replace("/","\\");
        else
            return s.replace("\\","/");
    }

    public static String urlSlashes(String s){
            return s.replace("\\","/");
    }


    //Unused file type extensions
    public final static String[] FILE_TYPES_TO_IGNORE = {".txt",".doc",".pdf",".jpg", ".docx",".las",".xml", ".nc","bil", "xtc"};
    public final static String[] FILE_TYPES_TO_ALLOW = ArrayUtils.addAll(GDALINFO_PROCESSABLE_EXTENSIONS, OGRINFO_VECTOR_FILE_EXTENSIONS);



        public final static String RASTER = "Raster";
        public final static String VECTOR = "Vector";
        public final static String UNDETERMINED = "Undetermined";


    //XML value types
        public final static String CHARACTER = "CharacterString";
        public final static String DATE = "Date";
        public final static String DATE_TIME = "DateTime";
        public final static String INTEGER = "Integer";
        public final static String BOOLEAN = "Boolean";
        public final static String DECIMAL = "Decimal";
        public final static String MEASURE = "Measure";
        public final static String LOCAL_NAME = "LocalName";


    //Geocombine
    //Add value (including space at end) to SOLR_PATH if you want to index to somewhere other than what is in the rake file
    public final static String SOLR_PATH_PROD = ""; //"SOLR_URL=http://www.example.com:1234/solr/collection ";
    //Add value (including space at end) to OGM_PATH if you are harvesting from somewhere other than what's in the rake file
    public final static String OGM_PATH = ""; //"OGM_PATH=/var/www/geoserver.frdr.ca/html/geodisy/ ";
    public final static String BACKEND_DEV_ADDRESS = ENDPOINT_STRINGS.BACKEND_DEV_ADDRESS;
    public final static String BACKEND_PROD_ADDRESS = ENDPOINT_STRINGS.BACKEND_PROD_ADDRESS;
    public final static String FRONTEND_DEV_ADDRESS = ENDPOINT_STRINGS.FRONTEND_DEV_ADDRESS;
    public final static String FRONTEND_PROD_ADDRESS = ENDPOINT_STRINGS.FRONTEND_PROD_ADDRESS;
    public static String DATA_DIR_LOC;
    public final static String DATA_DIR_LOC_CLOUD = ENDPOINT_STRINGS.DATA_DIR_LOC_CLOUD;
    public final static String DATA_DIR_LOC_LOCAL = ENDPOINT_STRINGS.DATA_DIR_LOC_LOCAL;
    public final static String BASE_LOCATION_TO_STORE_METADATA = "metadata/";

    //Values are added by the load() method at the top of the class
    public static String BACKEND_ADDRESS;
    public static String FRONTEND_ADDRESS;
    public static String END_XML_JSON_FILE_PATH;
    public static String PATH_TO_XML_JSON_FILES;
    public static String MOVE_METADATA;
    public static String MOVE_DATA;
    public static String GEOCOMBINE;
    public static String GITCALL;
    public static String DELETE_XML;

    public static String dataDir(){
        if(IS_WINDOWS)
            return DATA_DIR_LOC_LOCAL;
        else
            return DATA_DIR_LOC_CLOUD;
    }


    public static boolean fileTypesToIgnore(String title){
        String[] temp = ArrayUtils.addAll(OGRINFO_VECTOR_FILE_EXTENSIONS,GDALINFO_RASTER_FILE_EXTENSIONS);
        for(String s: temp){
            if(title.toLowerCase().endsWith(s)||title.toLowerCase().endsWith("zip"))
                return false;
        }
        return true;
    }

    public static boolean fileToAllow(String title){
        for (String s : GeodisyStrings.FILE_TYPES_TO_ALLOW) {
            if (title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean gdalinfoRasterExtention(String title){
        for(String s : GDALINFO_RASTER_FILE_EXTENSIONS) {
            if (title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean ogrinfoVectorExtension(String title){
        for(String s : OGRINFO_VECTOR_FILE_EXTENSIONS) {
            if (title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean otherShapeFilesExtensions(String title){
        for(String s : NON_SHP_SHAPEFILE_EXTENSIONS){
            if(title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean otherTiffFilesExtensions(String title){
        for(String s: NON_TIF_GEOTIFF_EXTENSIONS){
            if(title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean isProcessable(String title){
        for(String s: PROCESSABLE_EXTENSIONS){
            if(title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean hasGeospatialFile(String title){
        return gdalinfoRasterExtention(title.toLowerCase())||ogrinfoVectorExtension(title.toLowerCase());
    }

    public static boolean isPreviewable(String title){
        for(String s : PREVIEWABLE_FILE_EXTENSIONS){
            if(title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }
    public static String removeHTTPS(String path){
        path = replaceSlashes(path);
        if(path.contains(replaceSlashes("https:\\\\")))
            path = path.replace(replaceSlashes("https:\\\\"),"");
        if(path.contains(replaceSlashes("http:\\\\")))
            path = path.replace(replaceSlashes("http:\\\\"),"");
        if(path.contains(replaceSlashes("http:\\")))
            path = path.replace(replaceSlashes("http:\\"),"");
        if(path.contains(replaceSlashes("https:\\")))
            path = path.replace(replaceSlashes("https:\\"),"");
        return path;
    }
    public static String removeHTTPSAndReplaceAuthority(String path) {
        path = removeHTTPS(path);

        path = nonUniqueFromPid(path);

        String slash = GeodisyStrings.replaceSlashes("/");

        path = path.replace("=","_");
        if(path.contains(slash)) {
            path = path.replace(".", slash);
            path = path.replace("?", slash);
        }
        else {
            path = path.replace(".", "_");
            path = path.replace("?", "_");
        }
        //replace any colon beyond the Windows drive colon
        if(path.startsWith("D:"))
            path = path.replace("D:","D***");
        if(path.startsWith("C:"))
            path = path.replace("C:","C***");
        path = path.replace(":","_");
        path = path.replace("D***","D:");
        path = path.replace("C***","C:");

        path = replaceSlashes(path);


        return path;
    }

    private static String nonUniqueFromPid(String path) {
        String[][] nonUnique = {
                {"catalogue.data.gov.bc.ca/dataset/","bc"}                                 //2
                ,{"catalogue.cioos.ca/dataset/", "cioos"}                                       //4
                ,{"search2.odesi.ca/#/details?uri=","odesi"}                                    //5
                ,{"lwbin-datahub.ad.umanitoba.ca/dataset/","umb"}                               //7
                ,{"data.calgary.ca/d/","calgary"}                                               //9
                ,{"data.edmonton.ca/d/","edmonton"}                                             //10
                ,{"data.surrey.ca/dataset/","surrey"}                                           //11
                ,{"spectrum.library.concordia.ca/","spectrum"}                                  //12
                ,{"data.ontario.ca/dataset/", "on"}                                             //15
                ,{"doi.org/","doi"}                                                             //16,20,28,44,46,67,129,131-142,146,147,150-158,160-166,168,169,174,184,186
                ,{"www.frdr-dfdr.ca/repo/dataset/", "frdr"}                                     //19
                ,{"www.donneesquebec.ca/recherche/fr/dataset/","qb"}                            //17
                ,{"hecate.hakai.org/geonetwork/srv/eng/catalog.search#/metadata/","geonet"}     //21
                ,{"open.canada.ca/data/en/dataset/","open"}                                     //34
                ,{"www.polardata.ca/pdcsearch/PDCSearchDOI.jsp?","pdc"}                         //35
                ,{"open.alberta.ca/opendata/","ab"}                                             //37
                ,{"data.novascotia.ca/d/","ns"}                                                 //38
                ,{"data.princeedwardisland.ca/d/","pei"}                                        //39
                ,{"researchdata.sfu.ca/islandora/object/islandora:", "sfu"}                     //43
                ,{"researchdata.sfu.ca/islandora/object/sfu:", "sfu"}
                ,{"hdl.handle.net/","hnd"}                                                      //47
                ,{"dx.doi.org/","doi"}                                                          //58
                ,{"data.upei.ca/islandora/object/data:","upei"}                                 //66
                ,{"digital.library.yorku.ca/", "yorku"}                                         //72
                ,{"open.toronto.ca/dataset/","toronto"}                                         //85
                ,{"opendata.vancouver.ca/explore/dataset/","vancouver"}                         //86
                ,{"data.winnipeg.ca/d/","winnipeg"}                                             //87
                ,{"opendatakingston.cityofkingston.ca/explore/dataset/","kingston"}             //172
                ,{"donnees.montreal.ca/dataset/", "montreal"}                                   //173
                ,{"data.montreal.ca/dataset/", "montreal"}                                   //173







        };
        String slash = GeodisyStrings.replaceSlashes("\\");
        for(String[] u: nonUnique){
            String uPath = GeodisyStrings.replaceSlashes(u[0]);
            String endPathVal =  u[1];

            if(path.contains(uPath))
                return path.replace(uPath,endPathVal+slash).replace("%2F",slash);
            String underUPath = uPath.replace(slash,"_").replace(".","_");
            if(path.contains(underUPath)) {
                path = path.replace(underUPath, endPathVal.replace(slash,"_").replace(".","_"));
                if(path.contains("%2F"))
                    path.replace("%2F","_");
                return path;
            }
            if(path.contains(uPath.substring(0, uPath.length()-1)))
                return path.replace(endPathVal.substring(endPathVal.length() - 1), "");
            if(path.contains((uPath.replace(".",slash))))
              return  path.replace(endPathVal.replace(".",slash),"").replace("%2F",slash);;
            path = path.replace("%2F",slash);
        }
        path = path.replace("%2F",slash);
        return path;
    }

    public static boolean checkIfOpenDataSoftRepo(String url){
        String[] repos = {
                "https://opendata.vancouver.ca/"
                ,"https://opendatakingston.cityofkingston.ca"
        };
        for(String s: repos){
            if(url.startsWith(s))
                return true;
        }
        return false;
    }
}
