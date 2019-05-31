package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Temporal extends CompoundJSONField {
    private String resolution, doi;
    private List<TemporalExtents> times;

    public Temporal(String doi) {
        this.resolution = "";
        this.doi = doi;
        this.times = new LinkedList<>();
    }

    @Override
    public void setField(JSONObject field) {

    }

    @Override
    public String getField(String fieldName) {
        return null;
    }
}
