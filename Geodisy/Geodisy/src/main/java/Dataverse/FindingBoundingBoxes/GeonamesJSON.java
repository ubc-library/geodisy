package Dataverse.FindingBoundingBoxes;

import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;

public class GeonamesJSON {
    JSONObject jo;

    public GeonamesJSON(JSONObject jo) {
        this.jo = jo;
    }

    public GeonamesJSON(String geonameString){
        if(geonameString.contains("<totalResultsCount>0</totalResultsCount>"))
            jo = new JSONObject();
        else {
            JSONArray geonameJSON = new JSONArray(geonameString);
            this.jo = (JSONObject) geonameJSON.get(0);
        }
    }

    public JSONObject getRecordByName(String name){
            if(jo.get("countryName").toString().toLowerCase().equalsIgnoreCase(name) || jo.get("altName").toString().toLowerCase().contains(name.toLowerCase()))
                return jo;
            return null;
    }

    public String getCommonName(){
        return jo.getString("name");
    }

    public boolean hasGeoRecord(){
        return !jo.isEmpty();
    }

    public BoundingBox getBoundingBox(){
        BoundingBox bb =  new BoundingBox();
        if(!jo.has("bbox"))
            return bb;

        JSONObject bbox = (JSONObject) jo.get("bbox");
        bb.setLongWest(bbox.getString("west"));
        bb.setLatNorth(bbox.getString("north"));
        bb.setLongEast(bbox.getString("east"));
        bb.setLatSouth(bbox.getString("south"));
        return bb;
    }
}
