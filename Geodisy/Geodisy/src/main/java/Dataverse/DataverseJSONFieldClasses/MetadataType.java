package Dataverse.DataverseJSONFieldClasses;

import Dataverse.DataverseJavaObject;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONObject;

import java.util.List;

public abstract class MetadataType{
    protected String doi;
    protected DataverseJavaObject djo;

    public abstract void setFields(JSONObject jo);
    public abstract String getField(String fieldName);
    public abstract List getListField(String fieldName);
    public abstract String getDoi();
    public abstract void setDoi(String doi);

}
