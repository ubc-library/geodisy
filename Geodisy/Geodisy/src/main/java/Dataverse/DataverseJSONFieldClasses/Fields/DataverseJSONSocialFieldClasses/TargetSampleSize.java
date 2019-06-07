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
        String fieldName =  field.getString(TYPE_NAME);
        String val = field.getString(VAL);

        if(fieldName.equals(TARGET_SAMPLE_ACTUAL_SIZE))
            actualSize = Integer.getInteger(val);
        else if(fieldName.equals(TARGET_SAMPLE_SIZE_FORMULA))
            forumla = val;
        else
            errorParsing(this.getClass().toString(),fieldName);
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
