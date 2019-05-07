package Dataverse.DataverseJSONFieldClasses;

import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONObject;

import java.util.List;

public abstract class MetadataType{

    public abstract void setFields(JSONObject jo);
    public abstract List getListField(String fieldName);
    public abstract String getDoi();
    public abstract void setDoi(String doi);
    public abstract boolean hasBB();
    public abstract BoundingBox getBoundingBox();
}
