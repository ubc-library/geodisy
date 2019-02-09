package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class JSONField {
    public JSONField parseCompoundData(JSONArray compoundField){
        for(Object o: compoundField){
            JSONObject field = (JSONObject) o;
            setField(field);
        }
        return this;
    }
    protected abstract void setField(JSONObject field);
}
