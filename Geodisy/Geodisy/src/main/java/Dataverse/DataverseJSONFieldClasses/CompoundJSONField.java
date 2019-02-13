package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONArray;
import org.json.JSONObject;

abstract public class CompoundJSONField extends JSONField {

    /**
     * Iterates through the JSONArray calling setField on each object
     *
     * @param compoundField
     * @return
     */
    public JSONField parseCompoundData(JSONArray compoundField){
        for(Object o: compoundField){
            JSONObject field = (JSONObject) o;
            setField(field);
        }
        return this;
    }

    /**
     *
     * Each class overrides this with a version using a switch statement to
     * try to fill all the fields of its class
     *
     * @param field
     */
    protected abstract void setField(JSONObject field);
}
