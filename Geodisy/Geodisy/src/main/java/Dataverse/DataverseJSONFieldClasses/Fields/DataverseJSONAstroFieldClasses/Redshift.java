package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Strings.DVFieldNameStrings.*;

public class Redshift extends CompoundJSONField {
    private String type;
    private float resolution;
    private List<RedshiftExtents> redshifts;
    private String doi;

    public Redshift(String doi) {
        this.type = "";
        this.resolution = 0;
        this.redshifts = new LinkedList<>();
        this.doi = doi;
    }

    @Override
    public String getField(String fieldName) {
        switch(fieldName){
            case REDSHIFT_TYPE: return type;
            case REDSHIFT_RESOLUTION: return String.valueOf(resolution);
            default: logger.error("Tried to return field " + fieldName + " from Redshift in doi: " + doi); return "";
        }
    }

    public List getListField(String fieldName){
        if(fieldName.equals(REDSHIFT_VALUE_COVERAGE))
            return redshifts;
        logger.error("Tried to return a list of name " + fieldName + " from Redshift with doi: " + doi);
        return new LinkedList();
    }
    @Override
    public void setField(JSONObject jo){
        String fieldName =  jo.getString(TYPE_NAME);
        Object val = jo.get(VAL);
        switch(fieldName){
            case REDSHIFT_TYPE: setType((String) val); break;
            case REDSHIFT_RESOLUTION: setResolution((String) val); break;
            case REDSHIFT_VALUE_COVERAGE:
                RedshiftExtents redshiftExtents = new RedshiftExtents();
                JSONArray ja = (JSONArray) val;
                for(Object o: ja) {
                    redshiftExtents.setField((JSONObject) o);
                }
                addRedshiftExtent(redshiftExtents);
                break;
            default: logger.error("Weird fieldName, " + fieldName + ", in Redshift, with doi: " + doi + " and val = " + val); break;
        }
    }

    public void addRedshiftExtent(RedshiftExtents red){
        this.redshifts.add(red);
    }

    public void addFullRedshiftExtent(List<RedshiftExtents> reds){
        this.redshifts = reds;
    }

    private void setType(String type) {
        this.type = type;
    }

    private void setResolution(float resolution) {
        this.resolution = resolution;
    }

    private void setResolution(String resolution){
        setResolution(Float.parseFloat(resolution));
    }

    private void setDoi(String doi) {
        this.doi = doi;
    }
}
