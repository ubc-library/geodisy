package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONObject;

import static BaseFiles.GeodisyStrings.OPEN_GEO_METADATA_BASE;
import static Dataverse.DVFieldNameStrings.*;

public class GeographicBoundingBox extends CompoundJSONField {
    private BoundingBox bb;
    String doi;
    private String projection = "";
    private String fileName = "";
    private String geometryType = "Non-Geospatial";
    private String geoserverLabel = "";
    private boolean generated = false;
    private int fileNumber = 0;


    public GeographicBoundingBox(String doi) {
        this.doi = doi;
        this.bb = new BoundingBox();
    }

    public GeographicBoundingBox(String doi, BoundingBox bb){
        this.doi = doi;
        this.bb = bb;
    }

    public String getFileNumber(){
        if(fileNumber==0)
            return "";
        return String.valueOf(fileNumber);
    }

    public void setFileNumber(int i){
        fileNumber = i;
    }
    public boolean isWMS(){
        return bb.isWMS();
    }

    public boolean isWFS() {
        return bb.isWFS();
    }
    //TODO make sure we create a Geoserver Location for each file
    public String getGeoserverLocation() {
        return geoserverLabel;
    }
    public String getWestLongitude() {
        checkCoords(bb);
        return String.valueOf(bb.getLongWest());
    }
    public double getWestLongDub(){
        checkCoords(bb);
        return bb.getLongWest();
    }

    public void setWestLongitude(String westLongitude) {
         bb.setLongWest(westLongitude);
    }
    public void setWestLongitude(double westLongitude) {
        bb.setLongWest(westLongitude);
    }

    public String getEastLongitude() {

        checkCoords(bb);
        return String.valueOf(bb.getLongEast());
    }
    public double getEastLongDub(){
        checkCoords(bb);
        return bb.getLongEast();
    }

    public void setEastLongitude(String eastLongitude) {
        this.bb.setLongEast(eastLongitude);
    }
    public void setEastLongitude(double eastLongitude) {
        this.bb.setLongEast(eastLongitude);
    }

    public String getNorthLatitude() {
        checkCoords(bb);
        return String.valueOf(bb.getLatNorth());
    }
    public double getNorthLatDub(){
        checkCoords(bb);
        return bb.getLatNorth();
    }

    public void setNorthLatitude(String northLatitude) {
        this.bb.setLatNorth(northLatitude);
    }
    public void setNorthLatitude(double northLatitude) {
        this.bb.setLatNorth(northLatitude);
    }

    public String getSouthLatitude() {
        checkCoords(bb);
        return String.valueOf(bb.getLatSouth());
    }
    public double getSouthLatDub(){
        checkCoords(bb);
        return bb.getLatSouth();
    }

    public void setSouthLatitude(String southLatitude) {
        bb.setLatSouth(southLatitude);
    }
    public void setSouthLatitude(double southLatitude) {
        bb.setLatSouth(southLatitude);
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch (title) {
            case WEST_LONG:
                setWestLongitude(value);
                break;
            case EAST_LONG:
                setEastLongitude(value);
                break;
            case NORTH_LAT:
            case NORTH_LAT_LONG:
                setNorthLatitude(value);
                break;
            case SOUTH_LAT:
            case SOUTH_LAT_LONG:
                setSouthLatitude(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    public void setField(String title, String value){
        switch (title) {
            case WEST_LONG:
                setWestLongitude(value);
                break;
            case EAST_LONG:
                setEastLongitude(value);
                break;
            case NORTH_LAT:
            case NORTH_LAT_LONG:
                setNorthLatitude(value);
                break;
            case SOUTH_LAT:
            case SOUTH_LAT_LONG:
                setSouthLatitude(value);
                break;
            case PROJECTION:
                setProjection(value);
                break;
            case GEOSERVER_LABEL:
                setGeoserverLabel(value);
                break;
            case GEOMETRY:
                setGeometryType(value);
                break;
            case FILE_NAME:
                fileName = value;
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    private void setGeometryType(String value) {
       geometryType = value;
    }

    @Override
    public String getField(String fieldName) {
        checkCoords(bb);
        switch (fieldName) {
            case WEST_LONG:
                return getWestLongitude();
            case EAST_LONG:
                return getEastLongitude();
            case NORTH_LAT:
            case NORTH_LAT_LONG:
                return getNorthLatitude();
            case SOUTH_LAT:
            case SOUTH_LAT_LONG:
                return getSouthLatitude();
            case FILE_NAME:
                return getFileName();
            case PROJECTION:
                return getProjection();
            case GEOSERVER_LABEL:
                return getGeoserverLocation();
            case GEOMETRY:
                return geometryType;
            default:
                errorGettingValue(this.getClass().getName(),fieldName);
                return "Bad fieldName";
        }
    }


    private void setGeoserverLabel(String value){
        geoserverLabel = value;
    }

    private String getProjection() {
        return projection;
    }
    private void setProjection(String s){
        projection = s;
    }

    private String getFileName() {
        return fileName;
    }

    private void setFileName(String name){
       while(name.startsWith("/")) {
           name = name.substring(1);
       }
        fileName = name;
    }

    /**
     *  If any of the coordinates are invalid, then invalidate all the coordinates for this bounding box
     * @param bb
     */
    private void checkCoords(BoundingBox bb) {
        if(bb.getLatNorth()==361|bb.getLatSouth()==361|bb.getLongEast()==361|bb.getLongWest()==361){
            bb.setLongEast(361);
            bb.setLatSouth(361);
            bb.setLatNorth(361);
            bb.setLongWest(361);
        }
    }

    public BoundingBox getBB(){
        return bb;
    }
    public void setBB(BoundingBox b){
        bb = b;
    }
    public boolean isGeneratedFromGeoFile(){
        return generated;
    }
    public void setIsGeneratedFromGeoFile(boolean generated){this.generated=generated;}

    public String getOpenGeoMetaLocation() {
        return OPEN_GEO_METADATA_BASE+folderized(doi)+"iso19115.xml";
    }

    private String folderized(String doi) {
        String answer = doi;
        answer = answer.replace(".","/");
        int loc = answer.lastIndexOf("/");
        if(loc==-1) {
            logger.error("Something went wrong with creating a folder structure for the doi using doi: " + doi);
            return "";
        }
        answer = answer.substring(0,loc+1);
        return answer;
    }
    public boolean hasBB(){
        return bb.hasBoundingBox();
    }
}
