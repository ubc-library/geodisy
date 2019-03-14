package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONObject;

public abstract class MetadataType extends JSONField{

    public abstract void setFields(JSONObject jo);
}
