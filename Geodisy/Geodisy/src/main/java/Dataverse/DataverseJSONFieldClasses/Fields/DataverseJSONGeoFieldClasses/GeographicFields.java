package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.MetadataType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

public class GeographicFields extends MetadataType {
    List<GeographicCoverage> geoCovers;
    List<GeographicBoundingBox> geoBBoxes;
    List<GeographicUnit> geoUnits;
    protected String doi;

    public GeographicFields() {
        this.geoCovers = new LinkedList<>();
        this.geoBBoxes = new LinkedList<>();
        this.geoUnits = new LinkedList<>();
    }
    @Override
    public void setFields(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        switch (title) {
            case GEOGRAPHIC_BBOX:
                for(Object o: (JSONArray) field.get("value")) {
                    JSONObject jo = (JSONObject) o;
                    geoBBoxes.add(addGeoBBox(jo));
                }
                break;
            case GEOGRAPHIC_COVERAGE:
                for(Object o: (JSONArray) field.get("value")) {
                    JSONObject jo = (JSONObject) o;
                    geoCovers.add(addGeoCover(jo));
                }
                break;
            case GEOGRAPHIC_UNIT:
                for(Object o: (JSONArray) field.get("value")) {
                    JSONObject jo = (JSONObject) o;
                    GeographicUnit geographicUnit = new GeographicUnit(jo.getString("value"));
                    geoUnits.add(geographicUnit);
                }
                break;
            default:
                errorParsing(this.getClass().getName(), title);
        }
    }

    /**
     * This method takes a Geographic Coverage JSON array and returns a
     * Geographic Coverage java object
     * @param jObject
     * @return A Geographic Coverage
     */
    public GeographicCoverage addGeoCover(JSONObject jObject) {
        GeographicCoverage gC = new GeographicCoverage(doi);
        JSONArray ja = (JSONArray) jObject.get("value");
        for (Object o: ja){
            JSONObject jO = (JSONObject) o;
            gC.setField(jO);
        }
        return gC;
    }

    public void setGeoCovers(List<GeographicCoverage> geoCovers) {
        this.geoCovers = geoCovers;
    }

    /**
     * This method takes a Geographic Bounding box JSON array and returns a
     * Geographic bounding box java object
     * @param jObject
     * @return A Geographic Bounding Box
     */
    public GeographicBoundingBox addGeoBBox(JSONObject jObject) {
            GeographicBoundingBox gBB = new GeographicBoundingBox(doi);
            JSONArray ja = (JSONArray) jObject.get("value");
        for (Object o: ja){
            JSONObject jO = (JSONObject) o;
            gBB.setField(jO);
        }
        return gBB;
    }

    public void setGeoBBoxes(List<GeographicBoundingBox> geoBBoxes) {
        this.geoBBoxes = geoBBoxes;
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
    public String getField(String fieldName) {
        return null;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }
}
