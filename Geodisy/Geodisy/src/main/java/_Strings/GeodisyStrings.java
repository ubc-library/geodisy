package _Strings;

import org.apache.commons.lang3.ArrayUtils;

import static _Strings.PrivateStrings.*;

public class GeodisyStrings {
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
    public final static String[] HUGE_RECORDS_TO_IGNORE_UNTIL_LATER = {"10.5683/SP2/FJWIL8","10.5683/SP/Y3HRN","10864/GSSJX","10.5683/SP2/JP4WDF","10864/9KJ1L","10864/11086","10864/9VNIK","10.5683/SP/Y3HMRN","10.5683/SP/OEIP77","10.5683/SP/IP9ERW","10.5683/SP/NTUOK9","10864/11369","10864/11175","10.5683/SP/BT7HN2","10.5683/SP/4RFHBJ","10.5683/SP/T7ZJAF","10.5683/SP/RZM9HE","10.5683/SP/RAJQ2P","10.5683/SP2/AAGZDG","10.5683/SP2/1XRF9U","10.5683/SP2/MICSLT"};
    public final static String[] PROCESS_THESE_DOIS = {};// {"10.5683/SP2/J8581N","10.5683/SP2/JISB1K","10.5683/SP2/YPVTYT","10.5682/SP2/PONAP6"}; //"10.5683/SP2/UEJGTV" "10864/11669" "10.5683/SP2/GKJPIQ""10.5683/SP2/KYHUNF""10.5683/SP/EGOYE3""10.5683/SP2/LAWLTI""10.5072/FK2/PLD5VK","10.5683/SP2/UEJGTV","10.5683/SP/SBTXLS","10.5683/SP/UYBROL","10864/XER6B","10864/10197""10.5683/SP/OZ0WB0","10.5683/SP/S0MQVP","10.5683/SP/5S5Y9T","10.5683/SP/30JPOR","10.5683/SP/ASR2XE","10.5683/SP2/1VWZNC","10.5683/SP/AB5A9O","10.5683/SP2/YNOJSD","10.5683/SP/AB5A9O","10.5683/SP/2ZARY2","10.5683/SP2/ZDAHQG","10.5683/SP2/JFQ1SZ"
    //Repositories (add new repository URLS to a appropriate repository URL array below)
        // New Repository Types need new URL Arrays [Geodisy 2]

        public final static String SANDBOX_DV_URL = "https://206-12-90-131.cloud.computecanada.ca/"; //currently our sandbox
        public final static String TEST_SCHOLARS_PORTAL = "https://demodv.scholarsportal.info/";
        public final static String SCHOLARS_PORTAL = "https://dataverse.scholarsportal.info/"; //Don't use this unless SP gives approval
        public final static String SCHOLARS_PORTAL_CLONE = "https://dvtest.scholarsportal.info/";
        public static String[] DATAVERSE_URLS(){return getServers();}

        private static String[] getServers() {
                /*if(TEST)
                    return new String[]{SCHOLARS_PORTAL_CLONE};
                else*/
                    return new String[]{SCHOLARS_PORTAL};
        }

    public static boolean windowsComputerType(){
            return  System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
        }

        public final static boolean IS_WINDOWS = windowsComputerType();
        public static String getRoot(){
            boolean isWindows = System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
            if(isWindows)
                return WINDOWS_ROOT;
            else
                return FRDR_VM_CENTOS_ROOT;
        }

    //File paths
        private final static String WINDOWS_ROOT = "D:\\Work\\Geodisy\\Geodisy\\";
        private final static String FRDR_VM_CENTOS_ROOT = "/home/centos/geodisy/Geodisy/Geodisy/";
        public final static String GEODISY_PATH_ROOT = getRoot();
        public final static String SAVED_FILES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles");
        public final static String LOGS = GEODISY_PATH_ROOT + replaceSlashes("logs");
        public final static String EXISTING_RECORDS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingRecords.txt");
        public final static String EXISTING_CHECKS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingChecks.txt");
        public final static String EXISTING_BBOXES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingBBoxes.txt");
        public final static String DOWNLOADED_FILES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/DownloadedFiles.csv");
        public final static String VECTOR_RECORDS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingVectorRecords.txt");
        public final static String TEST_EXISTING_RECORDS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/TestExistingRecords.txt");
        public final static String TEST_EXISTING_BBOXES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/TestExistingBBoxes.txt");
        public final static String RASTER_RECORDS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingRasterRecords.txt");
        public final static String RECORDS_TO_CHECK = GEODISY_PATH_ROOT + replaceSlashes("logs/recordsToCheck.log");
        public final static String EXISTING_CALL_TO_CHECK = GEODISY_PATH_ROOT + replaceSlashes("logs/existingCallToCheck.txt");
        public final static String ERROR_LOG = GEODISY_PATH_ROOT + replaceSlashes("logs/error.log");
        public final static String WARNING_LOG = GEODISY_PATH_ROOT + replaceSlashes("logs/warning.log");
        public final static String XML_NS = "http://www.isotc211.org/2005/";
        public final static String COUNTRY_VALS =  GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/Geoname_countries.xml");
        public final static String ALL_CITATION_METADATA = GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/AllCitationMetadata.json");
        public final static String TEST_GEO_COVERAGE = GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/geocoverage.json");
        public final static String XML_TEST_FILE = GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/XMLTestDJO.xml");
        public final static String DATASET_FILES_PATH = replaceSlashes("datasetFiles/");
        public final static String OPEN_GEO_METADATA_BASE = "https://github.com/OpenGeoMetadata/ca.frdr.geodisy/";
        public final static String ISO_19139_XML = "iso19139.xml";

    //Geonames
        public final static String GEONAMES_SEARCH_BASE = "http://api.geonames.org/search?q=";


    //GDAL
        private final static String GDALINFO_LOCAL = "gdalinfo -approx_stats ";
        private final static String OGRINFO_LOCAL = "ogrinfo -ro -al -so ";
        private final static String GDALINFO_CLOUD = "/usr/gdal30/bin/gdalinfo -approx_stats ";
        private final static String OGRINFO_CLOUD = "/usr/gdal30/bin/ogrinfo -ro -al -so ";
        public final static String GDALINFO = getGdalInfo();
        public final static String OGRINFO = getOgrInfo();
        private final static String[] GDALINFO_PROCESSABLE_EXTENSIONS = { ".tif", ".tiff",".xyz", ".png"};
        private final static String[] NON_TIF_GEOTIFF_EXTENSIONS = {".aux.xml",".tab",".twf",".tifw", ".tiffw",".wld", ".tif.prj",".tfw"};
        public final static String[] GDALINFO_RASTER_FILE_EXTENSIONS = ArrayUtils.addAll(GDALINFO_PROCESSABLE_EXTENSIONS,NON_TIF_GEOTIFF_EXTENSIONS);
        private final static String[] NON_SHP_SHAPEFILE_EXTENSIONS = {".shx", ".dbf", ".sbn",".prj"};
        private final static String[] OGRINFO_PROCESSABLE_EXTENTIONS = {".geojson",".shp",".gpkg"}; //also .csv/.tab, but need to check if the csv is actually geospatial in nature
        private final static String[] CSV_EXTENTIONS = {".csv", "tab"};
        private final static String[] INTRUM_VECTOR = ArrayUtils.addAll(OGRINFO_PROCESSABLE_EXTENTIONS,CSV_EXTENTIONS);
        public final static String[] OGRINFO_VECTOR_FILE_EXTENSIONS = ArrayUtils.addAll(NON_SHP_SHAPEFILE_EXTENSIONS, INTRUM_VECTOR);
        public final static String FINAL_OGRINFO_VECTOR_FILE_EXTENSIONS = ".shp";
        public final static String[] PREVIEWABLE_FILE_EXTENSIONS = {".tif"};
        private final static String OGR2OGR_LOCAL = "ogr2ogr -f \"ESRI Shapefile\" -t_srs EPSG:4326 ";
        private final static String GDAL_TRANSLATE_LOCAL = "gdal_translate -of GTiff ";
        private final static String OGR2OGR_CLOUD = "/usr/gdal30/bin/ogr2ogr -t_srs EPSG:4326 -f \"ESRI Shapefile\" ";
        //GDAL for Raster conversion needs to be using GDAL version 2.x, so had to use a docker version of it for use with Centos
        public final static String GDAL_DOCKER = "sudo docker run --rm -v /home:/home osgeo/gdal:alpine-ultrasmall-v2.4.1 "; //base call for docker gdal, but need the program call added on
        private final static String GDAL_TRANSLATE_CLOUD = "/usr/gdal30/bin/gdal_translate -of GTiff ";
        public final static String OGR2OGR = getOgr2Ogr();
        public final static String GDAL_TRANSLATE = getGdalTranslate();
        public final static String RASTER_CRS = "EPSG:3857";
        public static String GDALWARP(String path,String fileName){ return getGdalWarp(path,fileName);}
        public static String GDAL_WARP_LOCAL(String path, String filename){ return "gdalwarp -overwrite -t_srs " + RASTER_CRS +" -r near -multi -of GTiff -co TILED=YES -co COMPRESS=LZW {} {}" + path + filename +" " + path + "1" + filename;}

        public static String GDAL_WARP_CLOUD(String path, String fileName){
        return "/usr/gdal30/bin/gdalwarp -overwrite -t_srs "+ RASTER_CRS +" -r near -multi -of GTiff -co TILED=YES -co COMPRESS=LZW " + path + fileName + " " + path + "1"+ fileName; }

        public static String GDALADDO(String source){ return getGdalAddo(source);}
        public static String GDAL_ADDO_LOCAL(String source){return "gdaladdo " + source + " -r nearest --config COMPRESS_OVERVIEW LZW 2 4 8 16 32 64 128";}
        public static String GDAL_ADDO_CLOUD(String source){return "/usr/gdal30/bin/gdaladdo " + source + " -r nearest --config COMPRESS_OVERVIEW LZW 2 4 8 16 32 64 128";}
        public final static String[] PROCESSABLE_EXTENSIONS = ArrayUtils.addAll(GDALINFO_PROCESSABLE_EXTENSIONS,OGRINFO_PROCESSABLE_EXTENTIONS);
        private static String getOgr2Ogr(){
            //if(IS_WINDOWS)
            if(true)
                return OGR2OGR_LOCAL;
            else
                return OGR2OGR_CLOUD;
        }

        private static String getGdalTranslate(){
           if(IS_WINDOWS)
                return GDAL_TRANSLATE_LOCAL;
            else
                return GDAL_TRANSLATE_CLOUD;
        }

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
    private static String getGdalInfo(){
        if(IS_WINDOWS)
            return GDALINFO_LOCAL;
        else
            return GDALINFO_CLOUD;
    }
    private static String getOgrInfo(){
        if(IS_WINDOWS)
            return OGRINFO_LOCAL;
        else
            return OGRINFO_CLOUD;
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
    public final static String BACKEND_DEV_ADDRESS = "geoservertest.frdr-dfdr.ca";
    public final static String BACKEND_PROD_ADDRESS = "geoserver.frdr.ca";
    public final static String FRONTEND_DEV_ADDRESS = "geotest.frdr-dfdr.ca";
    public final static String FRONTEND_PROD_ADDRESS = "geo.frdr.ca";
    public static String BACKEND_ADDRESS = beAddressToUse();
    public static String FRONTEND_ADDRESS = feAddressToUse();
    public final static String BASE_PATH = BACKEND_ADDRESS + "/";
    public final static String END_XML_JSON_FILE_PATH = "http://" + BASE_PATH + "geodisy/";
    public final static String PATH_TO_XML_JSON_FILES = END_XML_JSON_FILE_PATH;
    public final static String MOVE_METADATA = "sudo rsync -upgo " + getRoot() + "metadata/* /var/www/" + BACKEND_ADDRESS + "/html/geodisy/";
    public final static String DATA_DIR_LOC = "/geodata/geoserver/data/";
    public final static String MOVE_DATA = "sudo rsync -auhv " + getRoot() + "datasetFiles/* " + DATA_DIR_LOC;
    public final static String GEOCOMBINE = "sh geodisyFiles/combine.sh";
    public final static String BASE_LOCATION_TO_STORE_METADATA = "metadata/";

    public static String beAddressToUse(){
        if(TEST)
            return BACKEND_DEV_ADDRESS;
        else
            return BACKEND_PROD_ADDRESS;
    }

    public static String feAddressToUse(){
        if(TEST)
            return FRONTEND_DEV_ADDRESS;
        else
            return FRONTEND_PROD_ADDRESS;
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
}
