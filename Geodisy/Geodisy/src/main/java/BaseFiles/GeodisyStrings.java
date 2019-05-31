package BaseFiles;

public class GeodisyStrings {

    //File paths
    public final static String EXISTING_RECORDS = "savedFiles/ExisitingRecords.txt";
    public final static String RECORDS_TO_CHECK = "./logs/recordsToCheck.log";
    public final static String ERROR_LOG = "./logs/error.log";

    //Unused file type extensions
    public final static String[] FILE_TYPES_TO_IGNORE = {".txt",".doc",".csv",".pdf",".jpg", ".docx"};

    //TODO Change GDAL location to where it is when on Cloud instance
    //GDAL
    public final static String GDAL_LOCATION = "C:\\Program Files\\GDAL\\ogrinfo -rl ";
}