package BaseFiles;

public class GeodisyStrings {

    //File paths
    public final static String EXISTING_RECORDS = "./savedFiles/ExisitingRecords.txt";
    public final static String RECORDS_TO_CHECK = "./logs/recordsToCheck.log";
    public final static String EXISTING_CALL_TO_CHECK = "./logs/existingCallToCheck.txt";
    public final static String ERROR_LOG = "./logs/error.log";
    public final static String XMLNS = "http://www.isotc211.org/2005/";

    //Unused file type extensions
    public final static String[] FILE_TYPES_TO_IGNORE = {".txt",".doc",".csv",".pdf",".jpg", ".docx",".las",".xml"};

    //TODO Change GDAL location to where it is when on Cloud instance
    //GDAL
    public final static String GDALINFO_LOCAL = "C:\\Program Files\\GDAL\\gdalinfo -approx_stats ";
    public final static String OGRINFO_LOCAL = "C:\\Program Files\\GDAL\\ogrinfo -ro -al -so ";
    public final static String GDALINFO_CLOUD = "gdalinfo -approx_stats ";
    public final static String OGRINFO_CLOUD = "ogrinfo -ro -al -so ";
    public final static String[] GDALINFO_RASTER_FILE_EXTENSIONS = { ".tif", ".nc", ".png"};
    public final static String[] OGRINFO_VECTOR_FILE_EXTENSIONS = {".geojason",".shp", ".csv"};
    public final static String OGR2OGR_LOCAL = "C:\\Program Files\\GDAL\\ogr2ogr -t_srs EPSG:4326 ";
    public final static String GDAL_TRANSLATE_LOCAL = "C:\\Program Files\\GDAL\\gdal_translate -t_srs EPSG:4326 ";
    public final static String OGR2OGR_CLOUD = "ogr2ogr -t_srs EPSG:4326 ";
    public final static String GDAL_TRANSLATE_CLOUD = "gdal_translate -t_srs EPSG:4326 ";

    //XML value types
    public final static String CHARACTER = "CharacterString";
    public final static String DATE = "Date";
    public final static String INTEGER = "Integer";
    public final static String BOOLEAN = "Boolean";
    public final static String DECIMAL = "Decimal";
    public final static String MEASURE = "Measure";
    public final static String LOCAL_NAME = "LocalName";


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
}
