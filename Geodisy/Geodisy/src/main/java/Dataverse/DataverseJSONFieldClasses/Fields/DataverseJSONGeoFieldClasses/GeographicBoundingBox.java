package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONObject;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

public class GeographicBoundingBox extends CompoundJSONField {
    private BoundingBox bb;

    public GeographicBoundingBox() {
        this.bb = new BoundingBox();
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
        this.bb.setLongWest(westLongitude);
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

    public String getSouthLatitude() {
        checkCoords(bb);
        return String.valueOf(bb.getLatSouth());
    }

    public double getSouthLatDub(){
        checkCoords(bb);
        return bb.getLatSouth();
    }

    public void setSouthLatitude(String southLatitude) {
        this.bb.setLatSouth(southLatitude);
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
                setNorthLatitude(value);
                break;
            case SOUTH_LAT:
                setSouthLatitude(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String fieldName) {
        checkCoords(bb);
        switch (fieldName) {
            case WEST_LONG:
                return getWestLongitude();
            case EAST_LONG:
                return getEastLongitude();
            case NORTH_LAT:
                return getNorthLatitude();
            case SOUTH_LAT:
                return getSouthLatitude();
            default:
                errorGettingValue(this.getClass().getName(),fieldName);
                return "Bad fieldName";
        }
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
}
