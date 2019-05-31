package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import java.util.List;

public class Temporal extends CompoundJSONField {
    private String resolution;
    private List<TemporalExtents> times;


    @Override
    public void setField(JSONObject field) {

    }

    @Override
    public String getField(String fieldName) {
        return null;
    }
}
