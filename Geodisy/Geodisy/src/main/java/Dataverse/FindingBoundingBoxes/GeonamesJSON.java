package Dataverse.FindingBoundingBoxes;

import BaseFiles.GeoLogger;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class GeonamesJSON {
    JSONObject jo;
    GeoLogger logger = new GeoLogger(this.getClass());

    public GeonamesJSON(JSONObject jo) {
        this.jo = jo;
    }

    public GeonamesJSON(String geonameString){
        if(geonameString.contains("<totalResultsCount>")) {
            if (geonameString.contains("<totalResultsCount>0</totalResultsCount>"))
                jo = new JSONObject();
            else {
                JSONArray geonameJSON = new JSONArray(geonameString);
                this.jo = (JSONObject) geonameJSON.get(0);
            }
        }else{
            jo = XML.toJSONObject(geonameString);
            if(jo.has("country"))
                jo = jo.getJSONObject("country");
            else
                logger.error("Something was wrong with the country XML" + geonameString);
        }
    }

    public JSONObject getRecordByName(String name){
            if(jo.get("countryName").toString().toLowerCase().equalsIgnoreCase(name) || jo.get("altName").toString().toLowerCase().contains(name.toLowerCase()))
                return jo;
            return null;
    }

    public boolean hasGeoRecord(){
        return !jo.isEmpty();
    }

    public BoundingBox getBoundingBox(){
        BoundingBox bb =  new BoundingBox();
        if(!jo.has("south"))
            return bb;

        bb.setLongWest(getDoubleVal("west"));
        bb.setLatNorth(getDoubleVal("north"));
        bb.setLongEast(getDoubleVal("east"));
        bb.setLatSouth(getDoubleVal("south"));
        return bb;
    }

    public String getAltNames(){
        return getVal("altName");
    }

    public String getCommonName(){
        return getVal("countryName");
    }

    public String getCountryCode(){
        return getVal("countryCode");
    }

    private String getVal(String label) {
        return jo.has(label)? jo.getString(label):"";
    }

    private double getDoubleVal(String label){
        return jo.has(label)? jo.getDouble(label):-361;
    }
}
