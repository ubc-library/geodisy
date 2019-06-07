package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.*;

public class SocialScienceNotes extends CompoundJSONField {
    private String type, subject, text;

    public SocialScienceNotes(){
        type = "";
        subject = "";
        text = "";
    }

    @Override
    public void setField(JSONObject jo) {
        String fieldName =  jo.getString(TYPE_NAME);
        String val = jo.getString(VAL);
        switch(fieldName){
            case SOCIAL_SCIENCE_NOTES_TYPE: this.type = val; break;
            case SOCIAL_SCIENCE_NOTES_SUBJECT: this.subject = val; break;
            case SOCIAL_SCIENCE_NOTES_TEXT: this.text = val; break;
            default: errorParsing(this.getClass().toString(),fieldName);
        }
    }

    @Override
    public String getField(String fieldName) {
        switch(fieldName){
            case SOCIAL_SCIENCE_NOTES_TYPE: return type;
            case SOCIAL_SCIENCE_NOTES_SUBJECT: return subject;
            case SOCIAL_SCIENCE_NOTES_TEXT: return text;
            default:
                errorGettingValue(this.getClass().toString(),fieldName);
                return "Error trying to get " + fieldName + " from Social Science Notes";
        }
    }
}
