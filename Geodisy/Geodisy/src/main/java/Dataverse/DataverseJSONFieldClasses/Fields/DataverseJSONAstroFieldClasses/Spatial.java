package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.JSONField;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.*;

public class Spatial extends CompoundJSONField {
    private String resolution, coverage, doi;

    public Spatial(String doi) {
        this.resolution = "";
        this.coverage = "";
        this.doi = doi;
    }



    public void setField(String label, String value) {
        switch(label) {
            case SPATIAL_RESOLUTION:
                this.resolution = value;
                break;
            case SPATIAL_COVERAGE:
                this.coverage = value;
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
                return coverage;
            default:
                logger.error("Tried to get a non-existent field in the Spatial fields of the Astrophysics metadata.");
                return "ERROR in Spatial get";
        }
    }
}
