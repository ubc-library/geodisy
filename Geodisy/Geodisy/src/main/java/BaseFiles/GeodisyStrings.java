package BaseFiles;

public class GeodisyStrings {

    //File paths
    public final static String EXISTING_RECORDS = "savedFiles/ExisitingRecords.txt";
    public final static String RECORDS_TO_CHECK = "./logs/recordsToCheck.log";
    public final static String ERROR_LOG = "./logs/error.log";

    //Unused file type extensions
    public final static String[] FILE_TYPES_TO_IGNORE = {".txt",".doc",".csv",".pdf",".jpg", ".docx",".las",".xml"};

    //TODO Change GDAL location to where it is when on Cloud instance
    //GDAL
    public final static String GDALINFO_LOCAL = "C:\\Program Files\\GDAL\\gdalinfo -json ";
    public final static String OGRINFO_LOCAL = "C:\\Program Files\\GDAL\\ogrinfo -ro -al -so ";
    public final static String GDALINFO_CLOUD = "gdalinfo -listmdd ";
    public final static String OGRINFO_CLOUD = "ogrinfo -ro -al -so ";
    public final static String[] GDAL_RASTER_FILE_EXTENSIONS = {".geojason", ".tif", ".nc"};
    public final static String[] GDAL_VECTOR_FILE_EXTENSIONS = {".shp", ".csv"};
    public final static String OGR2OGR_LOCAL = "C:\\Program Files\\GDAL\\ogr2ogr -t_srs ";
    public final static String GDAL_TRANSLATE_LOCAL = "C:\\Program Files\\GDAL\\gdal_translate -t_srs ";
    public final static String OGR2OGR_CLOUD = "ogr2ogr -t_srs ";
    public final static String GDAL_TRANSLATE_CLOUD = "gdal_translate -t_srs ";

    public static boolean fileToIgnore(String title){
        for (String s : GeodisyStrings.FILE_TYPES_TO_IGNORE) {
            if (title.endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean gdalRasterExtention(String title){
        for(String s : GDAL_RASTER_FILE_EXTENSIONS) {
            if (title.endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean gdalVectorExtension(String title){
        for(String s : GDAL_VECTOR_FILE_EXTENSIONS) {
            if (title.endsWith(s))
                return true;
        }
        return false;
    }
}
