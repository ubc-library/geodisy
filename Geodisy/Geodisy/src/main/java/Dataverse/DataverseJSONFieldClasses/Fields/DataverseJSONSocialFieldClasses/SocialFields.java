package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.MetadataType;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static Dataverse.DVFieldNameStrings.*;

public class SocialFields extends MetadataType {
    private SimpleSocialFields simpleSocialFields;
    private TargetSampleSize targetSampleSize;
    private SocialScienceNotes socialScienceNotes;
    protected String doi;
    private GeoLogger logger = new GeoLogger(this.getClass());

    public SocialFields(String doi) {
        this.simpleSocialFields = new SimpleSocialFields();
        this.targetSampleSize = new TargetSampleSize();
        this.socialScienceNotes = new SocialScienceNotes();
        this.doi = doi;
    }

    @Override
    public void setFields(JSONObject jo) {
        String fieldName = jo.getString(TYPE_NAME);
        JSONArray a = (JSONArray) jo.get(VAL);
        if(fieldName.contains(TARGET_SAMPLE_SIZE_FIELDS))
            targetSampleSize.setFields(a);
        else if(fieldName.contains(SOCIAL_SCIENCE_NOTES_FIELDS))
            socialScienceNotes.setFields(a);
        else
            simpleSocialFields.setFields(a);
    }
    public String getField(String fieldName){
        if(fieldName.contains(TARGET_SAMPLE_SIZE_FIELDS))
            return targetSampleSize.getField(fieldName);
        else if(fieldName.contains(SOCIAL_SCIENCE_NOTES_FIELDS))
            return socialScienceNotes.getField(fieldName);
        else
            return simpleSocialFields.getField(fieldName);
    }
    @Override
    public List getListField(String fieldName) {
        return simpleSocialFields.getListField(fieldName);
    }

    @Override
    public String getDoi() {
        return doi;
    }

    @Override
    public void setDoi(String doi) {
        this.doi=doi;
    }

    @Override
    public boolean hasBB() {
        logger.error("Tried to access the bounding box in the social metadata fields"); return false;
    }

    @Override
    public BoundingBox getBoundingBox() {
        logger.error("Tried to access the bounding box in the social metadata fields"); return null;
    }
}
