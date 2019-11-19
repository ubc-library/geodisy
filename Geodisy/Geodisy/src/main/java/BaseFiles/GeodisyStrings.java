package BaseFiles;

public class GeodisyStrings {


    //Repositories (add new repository URLS to a appropriate repository URL array below)
        // New Repository Types need new URL Arrays [Geodisy 2]
        public final static String SANDBOX_DV_URL = "https://206-12-90-131.cloud.computecanada.ca/"; //currently our sandbox

        public final static String[] DATAVERSE_URLS = new String[]{SANDBOX_DV_URL};



    //File paths
        public final static String GEODISY_PATH_ROOT = "Geodisy/src/main/";
        public final static String EXISTING_RECORDS = GEODISY_PATH_ROOT + "savedFiles/ExisitingRecords.txt";
        public final static String EXISTING_BBOXES = GEODISY_PATH_ROOT + "savedFiles/ExistingBBoxes.txt";
        public final static String TEST_EXISTING_RECORDS = GEODISY_PATH_ROOT + "savedFiles/TestExistingRecords.txt";
        public final static String TEST_EXISTING_BBOXES = GEODISY_PATH_ROOT + "savedFiles/TestExistingBBoxes.txt";
        public final static String RECORDS_TO_CHECK = GEODISY_PATH_ROOT + "logs/recordsToCheck.log";
        public final static String EXISTING_CALL_TO_CHECK = GEODISY_PATH_ROOT + "logs/existingCallToCheck.txt";
        public final static String ERROR_LOG = GEODISY_PATH_ROOT + "logs/error.log";
        public final static String XML_NS = "http://www.isotc211.org/2005/";
        public final static String COUNTRY_VALS =  GEODISY_PATH_ROOT + "geodisyFiles/Geoname_countries.xml";
        public final static String ALL_CITATION_METADATA = GEODISY_PATH_ROOT + "geodisyFiles/AllCitationMetadata.json";
        public final static String XML_TEST_FILE = GEODISY_PATH_ROOT + "geodisyFiles/XMLTestDJO.xml";


    //Unused file type extensions
        public final static String[] FILE_TYPES_TO_IGNORE = {".txt",".doc",".csv",".pdf",".jpg", ".docx",".las",".xml"};

    //TODO Change GDAL location to where it is when on Cloud instance
    //GDAL
        public final static String GDALINFO_LOCAL = "C:\\Program Files\\GDAL\\gdalinfo -approx_stats ";
        public final static String OGRINFO_LOCAL = "C:\\Program Files\\GDAL\\ogrinfo -ro -al -so ";
        public final static String GDALINFO_CLOUD = "gdalinfo -approx_stats ";
        public final static String OGRINFO_CLOUD = "ogrinfo -ro -al -so ";
        public final static String[] GDALINFO_RASTER_FILE_EXTENSIONS = { ".tif", ".nc", ".png"};
        public final static String[] OGRINFO_VECTOR_FILE_EXTENSIONS = {".geojason",".shp", "kmz"}; //also .csv, but need to check if the csv is actually geospatial in nature
        public final static String[] PREVIEWABLE_FILE_EXTENSIONS = {".tif", ".kmz"};
        public final static String OGR2OGR_LOCAL = "C:\\Program Files\\GDAL\\ogr2ogr -t_srs EPSG:4326 ";
        public final static String GDAL_TRANSLATE_LOCAL = "C:\\Program Files\\GDAL\\gdal_translate -t_srs EPSG:4326 ";
        public final static String OGR2OGR_CLOUD = "ogr2ogr -t_srs EPSG:4326 ";
        public final static String GDAL_TRANSLATE_CLOUD = "gdal_translate -t_srs EPSG:4326 ";

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
    private final static String SOLR_PATH_PROD = "SOLR_URL=http://www.example.com:1234/solr/collection ";
    private final static String SOLR_PATH_TEST = "";
    private final static String SOLR_PATH = SOLR_PATH_TEST;
    public final static String GEOCOMBINE = SOLR_PATH + "bundle exec rake geocombine:index";
    public static boolean fileToIgnore(String title){
        for (String s : GeodisyStrings.FILE_TYPES_TO_IGNORE) {
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
