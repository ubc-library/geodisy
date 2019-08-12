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
        for(String s: SOCIAL_SIENCE_NOTES_ALL_FIELDS){
            if(!jo.isNull(s)){
                if(s.contains("Text"))
                    text = jo.getJSONObject(s).getString(VAL);
                else if(s.contains("Subject"))
                    subject = jo.getJSONObject(s).getString(VAL);
                else if(s.contains("Type"))
                    type = jo.getJSONObject(s).getString(VAL);
                else
                    errorParsing(this.getClass().toString(),s);
            }
            else
                errorParsing(this.getClass().toString(),jo.toString());
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
