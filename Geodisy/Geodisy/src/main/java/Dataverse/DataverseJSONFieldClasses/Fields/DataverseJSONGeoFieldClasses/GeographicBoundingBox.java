package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.*;

public class GeographicBoundingBox extends CompoundJSONField {
    private BoundingBox bb;
    String doi;

    public GeographicBoundingBox(String doi) {
        this.doi = doi;
        this.bb = new BoundingBox();
    }

    public GeographicBoundingBox(String doi, BoundingBox bb){
        this.doi = doi;
        this.bb = bb;
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
            default:
                errorParsing(this.getClass().getName(),title);
        }
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
            case GEOMETRY:
                return getGeometry();
            default:
                errorGettingValue(this.getClass().getName(),fieldName);
                return "Bad fieldName";
        }
    }

    private String getGeometry() {
        return bb.getGeometryType();
    }

    private String getFileName() {
        return bb.getFileName();
    }

    public void setFileName(String name){
        bb.setFileName(name);
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
        return bb.isGenerated();
    }
}
