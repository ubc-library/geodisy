package Dataverse.FindingBoundingBoxes;

import BaseFiles.GeoLogger;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
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
                jo = (JSONObject) XML.toJSONObject(geonameString).get("geonames");
                jo = (JSONObject)jo.get("geoname");
            }
        }else{
            logger.warn("Not sure why there wasn't a totalResultsCount in the geonames XML string. See " + geonameString);
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

    public BoundingBox getBBFromGeonamesBBElementString(){
        BoundingBox bb =  new BoundingBox();
        if(!jo.has("south"))
            return bb;
        bb.setLongWest(getDoubleLatLongVal(jo,"west"));
        bb.setLatNorth(getDoubleLatLongVal(jo,"north"));
        bb.setLongEast(getDoubleLatLongVal(jo,"east"));
        bb.setLatSouth(getDoubleLatLongVal(jo,"south"));
        return bb;
    }

    public BoundingBox getBBFromGeonamesJSON(){
        BoundingBox bb =  new BoundingBox();
        if(!jo.has("bbox"))
            return bb;
        JSONObject bbox = (JSONObject) jo.get("bbox");
        bb.setLongWest(getDoubleLatLongVal(bbox,"west"));
        bb.setLatNorth(getDoubleLatLongVal(bbox,"north"));
        bb.setLongEast(getDoubleLatLongVal(bbox,"east"));
        bb.setLatSouth(getDoubleLatLongVal(bbox,"south"));
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

    private double getDoubleLatLongVal(JSONObject ob, String label){
        return ob.has(label)? ob.getDouble(label):361;
    }
}
