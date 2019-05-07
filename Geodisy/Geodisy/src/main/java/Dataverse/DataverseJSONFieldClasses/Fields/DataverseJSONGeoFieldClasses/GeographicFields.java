package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DVFieldNameStrings;
import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.CompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.CompoundFields.Series;
import Dataverse.DataverseJSONFieldClasses.MetadataType;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Dataverse.DVFieldNameStrings.*;

public class GeographicFields extends MetadataType {
    List<GeographicCoverage> geoCovers;
    List<GeographicBoundingBox> geoBBoxes;
    List<GeographicUnit> geoUnits;
    BoundingBox fullBB; //The single bounding box that includes all listed bounding box extents
    protected String doi;
    Logger logger = LogManager.getLogger(GeographicFields.class);

    public GeographicFields(String doi) {
        this.geoCovers = new LinkedList<>();
        this.geoBBoxes = new LinkedList<>();
        this.geoUnits = new LinkedList<>();
        fullBB = new BoundingBox();
        this.doi = doi;
    }
    @Override
    public void setFields(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        switch (title) {
            case GEOGRAPHIC_BBOX:
                for(Object o: (JSONArray) field.get("value")) {
                    JSONObject jo = (JSONObject) o;
                    geoBBoxes.add(parseGeoBBox(jo));
                }
                setBoundingBox();
                break;
            case GEOGRAPHIC_COVERAGE:
                for(Object o: (JSONArray) field.get("value")) {
                    JSONObject jo = (JSONObject) o;
                    geoCovers.add(addGeoCover(jo));
                }
                break;
            case GEOGRAPHIC_UNIT:
                for(Object o: (JSONArray) field.get("value")) {
                    //JSONObject jo = (JSONObject) o;
                    GeographicUnit geographicUnit = new GeographicUnit((String)o);
                    geoUnits.add(geographicUnit);
                }
                break;
            default:
               logger.error("Something went wrong parsing the Geographic Fields. Field " + title + " does not exist");
        }
    }

    /**
     * This method takes a Geographic Coverage JSON object and returns a
     * Geographic Coverage java object
     * @param jObj
     * @return A Geographic Coverage
     */
    public GeographicCoverage addGeoCover(JSONObject jObj) {
        GeographicCoverage gC = new GeographicCoverage(doi);
        gC.setField(jObj);
        return gC;
    }

    public void setGeoCovers(List<GeographicCoverage> geoCovers) {
        this.geoCovers = geoCovers;
    }

    /**
     * This method takes a Geographic Bounding box JSON array and returns a
     * Geographic bounding box java object
     * @param jsonObject
     * @return A Geographic Bounding Box
     */
    private GeographicBoundingBox parseGeoBBox(JSONObject jsonObject) {
            GeographicBoundingBox gBB = new GeographicBoundingBox(doi);
            for(String k: jsonObject.keySet()) {
                JSONObject jO = jsonObject.getJSONObject(k);
                gBB.setField(jO);
            }
        return gBB;
    }

    public void setGeoBBoxes(List<GeographicBoundingBox> geoBBoxes) {
        this.geoBBoxes = geoBBoxes;
        setBoundingBox();
    }

    public void setGeoUnits(List<GeographicUnit> geoUnits) {
        this.geoUnits = geoUnits;
    }

    public List<GeographicBoundingBox> getGeoBBoxes(){
        return geoBBoxes;
    }

    public List<GeographicCoverage> getGeoCovers(){
        return geoCovers;
    }

    public List<GeographicUnit> getGeoUnits(){
        return geoUnits;
    }
/*TODO Figure out how getField will work when there are multiple copies of that section of the geographic
  metadata*/
    @Override
    public List getListField(String fieldName) {
        switch(fieldName){
            case GEOGRAPHIC_COVERAGE:
                return geoCovers;
            case GEOGRAPHIC_BBOX:
                return geoBBoxes;
            case GEOGRAPHIC_UNIT:
                return geoUnits;
            default:
                return new LinkedList<>();
        }
    }

    @Override
    public String getDoi() {
        return doi;
    }
    @Override
    public void setDoi(String doi) {
        this.doi = doi;
    }

    private void setBoundingBox(){
        double north = -360;
        double south = 360;
        double east = -360;
        double west = 360;
        double temp;
        for(GeographicBoundingBox b: geoBBoxes){
            temp = b.getNorthLatDub();
            north = (temp>north) ? temp : north;

            temp = b.getSouthLatDub();
            south = (temp<south) ? temp : south;

            temp = b.getEastLongDub();
            east = (temp>east) ? temp : east;

            temp = b.getWestLongDub();
            west = (temp<west) ? temp : west;
        }
        BoundingBox box = new BoundingBox();
        if(west == 360 || east == -360 || north == -360 || south == 360) {
            logger.info("Something went wrong with the bounding box for record " + doi);
            logger.error("Something went wrong with the bounding box for record " + doi);
            west = 361;
            east = 361;
            north = 361;
            south = 361;
        }
        box.setLongWest(west);
        box.setLongEast(east);
        box.setLatNorth(north);
        box.setLatSouth(south);
        fullBB = box;
    }
    @Override
    public boolean hasBB(){
        return fullBB.getLongWest() != 361;
    }
    @Override
    public BoundingBox getBoundingBox(){
        return fullBB;
    }

    public void setFullBB(BoundingBox b){
        GeographicBoundingBox g = new GeographicBoundingBox(doi);
        g.setEastLongitude(String.valueOf(b.getLongEast()));
        g.setWestLongitude(String.valueOf(b.getLongWest()));
        g.setNorthLatitude(String.valueOf(b.getLatNorth()));
        g.setSouthLatitude(String.valueOf(b.getLatSouth()));
        geoBBoxes.add(g);
        setBoundingBox();
    }
}
