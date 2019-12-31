package BaseFiles;

import org.apache.commons.lang3.ArrayUtils;


public class GeodisyStrings {

    public final static boolean TEST = true; //change this to false when in production

    //Repositories (add new repository URLS to a appropriate repository URL array below)
        // New Repository Types need new URL Arrays [Geodisy 2]
        public final static String SANDBOX_DV_URL = "https://206-12-90-131.cloud.computecanada.ca/"; //currently our sandbox

        public final static String[] DATAVERSE_URLS = new String[]{SANDBOX_DV_URL};



    //File paths
        public final static String GEODISY_PATH_ROOT = "";
        public final static String EXISTING_RECORDS = GEODISY_PATH_ROOT + "savedFiles/ExisitingRecords.txt";
        public final static String EXISTING_CHECKS = GEODISY_PATH_ROOT + "savedFiles/ExisitingChecks.txt";
        public final static String EXISTING_BBOXES = GEODISY_PATH_ROOT + "savedFiles/ExistingBBoxes.txt";
        public final static String TEST_EXISTING_RECORDS = GEODISY_PATH_ROOT + "savedFiles/TestExistingRecords.txt";
        public final static String TEST_EXISTING_BBOXES = GEODISY_PATH_ROOT + "savedFiles/TestExistingBBoxes.txt";
        public final static String RECORDS_TO_CHECK = GEODISY_PATH_ROOT + "logs/recordsToCheck.log";
        public final static String EXISTING_CALL_TO_CHECK = GEODISY_PATH_ROOT + "logs/existingCallToCheck.txt";
        public final static String ERROR_LOG = GEODISY_PATH_ROOT + "logs/error.log";
        public final static String WARNING_LOG = GEODISY_PATH_ROOT + "logs/warning.log";
        public final static String XML_NS = "http://www.isotc211.org/2005/";
        public final static String COUNTRY_VALS =  GEODISY_PATH_ROOT + "geodisyFiles/Geoname_countries.xml";
        public final static String ALL_CITATION_METADATA = GEODISY_PATH_ROOT + "geodisyFiles/AllCitationMetadata.json";
        public final static String XML_TEST_FILE = GEODISY_PATH_ROOT + "geodisyFiles/XMLTestDJO.xml";
        public final static String DATASET_FILES_PATH = "datasetFiles/";
        public final static String OPEN_GEO_METADATA_BASE = "https://github.com/OpenGeoMetadata/ca.ubc/";




    //TODO Change GDAL location to where it is when on Cloud instance
    //GDAL
        public final static String GDALINFO_LOCAL = "C:\\Program Files\\GDAL\\gdalinfo -approx_stats ";
        public final static String OGRINFO_LOCAL = "C:\\Program Files\\GDAL\\ogrinfo -ro -al -so ";
        public final static String GDALINFO_CLOUD = "sudo /usr/gdal30/bin/gdalinfo -approx_stats ";
        public final static String OGRINFO_CLOUD = "sudo /usr/gdal30/bin/ogrinfo -ro -al -so ";
        public final static String[] GDALINFO_RASTER_FILE_EXTENSIONS = { ".tif", ".tiff", ".nc", ".png",".xyz"};
        public final static String[] OGRINFO_VECTOR_FILE_EXTENSIONS = {".geojson",".shp", ".shx", ".dbf", ".sbn",".kmz",".csv",".tab",".gpkg"}; //also .csv, but need to check if the csv is actually geospatial in nature
        public final static String[] PREVIEWABLE_FILE_EXTENSIONS = {".tif", ".kmz"};
        public final static String OGR2OGR_LOCAL = "C:\\Program Files\\GDAL\\ogr2ogr -t_srs EPSG:4326 ";
        public final static String GDAL_TRANSLATE_LOCAL = "C:\\Program Files\\GDAL\\gdal_translate -t_srs EPSG:4326 ";
        public final static String OGR2OGR_CLOUD = "sudo /usr/gdal30/bin/ogr2ogr -t_srs EPSG:4326 ";
        public final static String GDAL_TRANSLATE_CLOUD = "sudo /usr/gdal30/bin/gdal_translate -t_srs EPSG:4326 ";

    //Unused file type extensions
    public final static String[] FILE_TYPES_TO_IGNORE = {".txt",".doc",".pdf",".jpg", ".docx",".las",".xml"};
    public final static String[] FILE_TYPES_TO_ALLOW = ArrayUtils.addAll(GDALINFO_RASTER_FILE_EXTENSIONS, OGRINFO_VECTOR_FILE_EXTENSIONS);

        public final static String RASTER = "Raster";
        public final static String VECTOR = "Vector";

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

    public final static String SOLR_PATH_PROD = "SOLR_URL=http://www.example.com:1234/solr/collection ";
    public final static String SOLR_PATH_TEST = "";
    public final static String SOLR_PATH = SOLR_PATH_TEST;
    //TODO set custom path for OGM location (where the GeoBlacklightJson are stored)
    public final static String DEV_ADDRESS = "206-12-92-97.cloud.computecanada.ca/";
    public final static String PROD_ADDRESS = "tbd";
    public final static String ADDRESS = addressToUse(TEST);
    public final static String VM_BASE_PATH_DEV = "https://" + DEV_ADDRESS;
    public final static String VM_BASE_PATH_PROD = "tbd";
    public final static String BASE_PATH = vmToUse(TEST);
    public final static String END_XML_JSON_FILE_PATH = BASE_PATH + "geodisy/";
    public final static String PATH_TO_XML_JSON_FILES = BASE_PATH + END_XML_JSON_FILE_PATH;
    public final static String OGM_PATH = "OGM_PATH=" + BASE_PATH;
    public final static String GEOCOMBINE = SOLR_PATH + OGM_PATH + "bundle exec rake geocombine:index";
    public final static String BASE_LOCATION_TO_STORE_METADATA = BASE_PATH + "/var/www/" + ADDRESS  + "/html/";

    public static String vmToUse(boolean test){
        if(test)
            return VM_BASE_PATH_DEV;
        else
            return VM_BASE_PATH_PROD;
    }

    public static String addressToUse(boolean test){
        if(test)
            return DEV_ADDRESS;
        else
            return PROD_ADDRESS;
    }
    public static boolean fileToIgnore(String title){
        for (String s : GeodisyStrings.FILE_TYPES_TO_IGNORE) {
            if (title.endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean fileToAllow(String title){
        for (String s : GeodisyStrings.FILE_TYPES_TO_ALLOW) {
            if (title.endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean gdalinfoRasterExtention(String title){
        for(String s : GDALINFO_RASTER_FILE_EXTENSIONS) {
            if (title.endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean ogrinfoVectorExtension(String title){
        for(String s : OGRINFO_VECTOR_FILE_EXTENSIONS) {
            if (title.endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean hasGeospatialFile(String title){
        return gdalinfoRasterExtention(title)||ogrinfoVectorExtension(title);
    }

    public static boolean isPreviewable(String title){
        for(String s : PREVIEWABLE_FILE_EXTENSIONS){
            if(title.endsWith(s))
                return true;
        }
        return false;
    }
}
