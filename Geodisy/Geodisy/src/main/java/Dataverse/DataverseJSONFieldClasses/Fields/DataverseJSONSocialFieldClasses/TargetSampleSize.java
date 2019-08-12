package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.*;

public class TargetSampleSize extends CompoundJSONField {
    private String forumla;
    private int actualSize;

    public TargetSampleSize(){
        forumla = "";
        actualSize = 0;
    }
    @Override
    public void setField(JSONObject field) {
        if(field.has(TARGET_SAMPLE_ACTUAL_SIZE)){
            String val = field.getJSONObject(TARGET_SAMPLE_ACTUAL_SIZE).getString(VAL);
            actualSize = Integer.parseInt(val);
        }
        if(field.has(TARGET_SAMPLE_SIZE_FORMULA)){
            String val = field.getJSONObject(TARGET_SAMPLE_SIZE_FORMULA).getString(VAL);
            forumla = val;
        }
        if(actualSize==0 && forumla.equals(""))
            errorParsing(this.getClass().toString(),"Tried to set a field that doesn't exist.");
    }

    @Override
    public String getField(String fieldName) {
        if(fieldName.equals(TARGET_SAMPLE_ACTUAL_SIZE))
            return String.valueOf(actualSize);
        else if(fieldName.equals(TARGET_SAMPLE_SIZE_FORMULA))
            return forumla;
        else {
            errorGettingValue(this.getClass().toString(), fieldName);
            return "Error trying to get " + fieldName + " from Target Sample Size";
        }
    }
}
