package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.MetadataType;
import Dataverse.DataverseJavaObject;
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
    private DataverseJavaObject djo;
    GeoLogger logger = new GeoLogger(this.getClass());

    public GeographicFields(DataverseJavaObject djo) {
        this.djo = djo;
        this.geoCovers = new LinkedList<>();
        this.geoBBoxes = new LinkedList<>();
        this.geoUnits = new LinkedList<>();
        fullBB = new BoundingBox();
        this.doi = djo.getDOI();
    }

    //Placeholder GeographicFields constructor
    public GeographicFields(){
        this.geoCovers = new LinkedList<>();
        this.geoBBoxes = new LinkedList<>();
        this.geoUnits = new LinkedList<>();
        fullBB = new BoundingBox();
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
        CitationFields cf = djo.getCitationFields();
        cf.setDoi(doi);
        djo.setCitationFields(cf);
    }

    /**
     * Creates 2 bounding boxes, one for 180 down to -180 the other for -180 up to 180. I need to create 2 so
     * I don't have any bounding boxes crossing the 180/-180 meridian. Once I have calculated the E/W extents of these
     * two bounding boxes, I can then calculate if I should merge them across the meridian or the other direction when
     * creating a final single bounding box to encompass all the smaller bounding boxes.
     */
    public void setBoundingBox(){
        double north = -181;
        double south = 181;
        double east = -181;
        double west = 181;
        double altWest = 181;
        double altEast = -181;
        double temp;
        double temp2;
        for(GeographicBoundingBox b: geoBBoxes){
            temp = b.getNorthLatDub();
            north = (temp>north) ? temp : north;

            temp = b.getSouthLatDub();
            south = (temp<south) ? temp : south;

            temp = b.getEastLongDub();
            temp2 = b.getWestLongDub();
//Ugly nested if/else statements, but couldn't think of another easier way. Would be happy for this to get cleaned up.
            if(temp>east){
                if(temp<altWest){
                    if(temp2-east<=altWest-temp){
                        east = temp;
                        if(temp2<west)
                            west = temp2;
                    }else{
                        altWest = temp2;
                        if(altEast<temp)
                            altEast=temp;
                    }
                }else{
                    if(temp>altEast) {
                        altEast = temp;
                    }
                    if(temp2<altWest) {
                            altWest = temp2;
                    }
                }
            }else{
                if(temp2<west)
                    west=temp2;
            }

        }
        BoundingBox box = new BoundingBox();
        west = (west==181) ? altWest : west;
        east = (east==-181)? altEast : east;
        if(west == 181 || east == -181 || north == -181 || south == 181) {
            logger.info("Something went wrong with the bounding box for record " + doi, djo, logger.getName());
            west = 361;
            east = 361;
            north = 361;
            south = 361;
        }
        if(altWest==181) {
            box.setLongWest(west);
            box.setLongEast(east);
        }else if(altWest-east>west-altEast){
            box.setLongWest(altWest);
            box.setLongEast(east);
        }else{
            box.setLongEast(altEast);
            box.setLongWest(west);
        }
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

    public void addBB(List<GeographicBoundingBox> bboxes, GeographicBoundingBox gBB) {
        double east = gBB.getEastLongDub();
        double west = gBB.getWestLongDub();
        double north = gBB.getNorthLatDub();
        double south = gBB.getSouthLatDub();

        if(east<west){
            GeographicBoundingBox second = new GeographicBoundingBox(gBB.doi);
            second.setNorthLatitude(String.valueOf(north));
            second.setSouthLatitude(String.valueOf(south));
            second.setWestLongitude(String.valueOf(west));
            second.setEastLongitude("180");
            bboxes.add(second);
            gBB.setWestLongitude("-180");
            bboxes.add(gBB);
        }else
            bboxes.add(gBB);
        setGeoBBoxes(bboxes);

    }
}
