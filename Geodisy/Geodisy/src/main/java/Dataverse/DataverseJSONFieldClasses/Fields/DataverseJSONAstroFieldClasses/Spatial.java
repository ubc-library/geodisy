package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static _Strings.DVFieldNameStrings.*;

public class Spatial extends CompoundJSONField {
    private String resolution, doi;
    private List<String> coverage;

    public Spatial(String doi) {
        this.resolution = "";
        this.coverage = new LinkedList<>();
        this.doi = doi;
    }



    public void setField(String label, String value) {
        switch(label) {
            case SPATIAL_RESOLUTION:
                this.resolution = value;
                break;
            case SPATIAL_COVERAGE:
                coverage.add(value);
                break;
            default:
                    logger.error("Tried to set a non-existent field in the Spatial fields of the Astrophysics metadata.");
        }

    }

    @Override
    public void setField(JSONObject field) {
        String fieldName = field.getString(TYPE_NAME);
        String val = field.getString(VAL);
        setField(fieldName,val);
    }

    @Override
    public String getField(String fieldName) {
        switch (fieldName){
            case SPATIAL_RESOLUTION:
               return resolution;
            case SPATIAL_COVERAGE:
            default:
                logger.error("Tried to get a non-existent field in the Spatial fields of the Astrophysics metadata or tried to return Spatial Coverage as a string rather than a list. Field requested was " + fieldName + ".");
                return "ERROR in Spatial get";
        }
    }

    public List getListField(String fieldName){
        if(fieldName.equals(SPATIAL_COVERAGE))
            return coverage;
        errorGettingValue(this.getClass().toString(),fieldName);
        return new LinkedList<>();
    }
}
