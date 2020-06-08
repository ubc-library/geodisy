package Dataverse.DataverseJSONFieldClasses;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJavaObject;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONObject;

import java.util.List;

public abstract class MetadataType{
    protected String doi;
    protected DataverseJavaObject djo;

    public abstract MetadataType setFields(JSONObject jo);
    public abstract List getListField(String fieldName);
    public abstract String getDOI();
    public abstract void setDoi(String doi);

}
