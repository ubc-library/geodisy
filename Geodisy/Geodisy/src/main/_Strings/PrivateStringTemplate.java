package _Strings;

public class PrivateStringTemplate {
    //This is an example of what Constants you will need to save in a class 'PrivateStrings' in the BaseFiles Package
    //Do not save the actual 'PrivateStrings' class these with publicly accessible code, it needs to be kept private

    // Github is used if you are saving metadata to Open Geo Metadata
    public final static String PRIVATE_GIT_PASSWORD = "[Your GIT Password]";

    // Geoserver provides geospatial file preview
    public final static String PRIVATE_GEOSERVER_PASSWORD = "[Your Geoserver Password]";
    public final static String PRIVATE_GEOSERVER_USERNAME = "[Your Geoserver Username]";

    // Geonames is an external service that provides bounding box information for placenames
    public final static String PRIVATE_GEONAMES_USERNAME = "[Your Geonames Username]";

    // Postgis is used to store vector data and provides that data to Geoserver
    public final static String PRIVATE_POSTGIS_USER_PASSWORD = "[Your POSTGIS USER PASSWORD]";
    public final static String PRIVATE_OPENGEOMETADATA_USERNAME = "[Your OpenGeoMetadata Username]";
    public final static String PRIVATE_OPENGEOMETADATA_PASSWORD = "[Your OpenGeoMetadata Password]";
}
