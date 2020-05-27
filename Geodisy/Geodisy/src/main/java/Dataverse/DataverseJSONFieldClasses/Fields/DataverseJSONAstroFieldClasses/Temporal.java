package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static _Strings.DVFieldNameStrings.*;

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
        String fieldName = field.getString(TYPE_NAME);
        Object o = field.get(VAL);
        switch (fieldName) {
            case TEMPORAL_RESOLUTION: resolution = (String) o; break;
            case TEMPORAL_COVERAGE:
                JSONArray timeFields = (JSONArray) o;
                TemporalExtents temporalExtents = new TemporalExtents();
                for (Object o2 : timeFields) {
                    temporalExtents.setField((JSONObject) o2);
                }
                times.add(temporalExtents);
        }
    }

    @Override
    public String getField(String fieldName) {
        if(fieldName.equals(TEMPORAL_RESOLUTION))
            return resolution;
        if(fieldName.equals(doi))
            return doi;
        logger.error("Asked for a strange field: " + fieldName + ", for Temporal section of Astrophysics with doi: " + doi);
        return "ERROR in Temporal get";
    }

    public List getListField(String fieldName){
        if(fieldName.equals("TemporalExtent"))
            return times;
        logger.error("Asked for weird temporal list: " + fieldName + ". See doi: " + doi);
        return new LinkedList<TemporalExtents>();
        }
}
