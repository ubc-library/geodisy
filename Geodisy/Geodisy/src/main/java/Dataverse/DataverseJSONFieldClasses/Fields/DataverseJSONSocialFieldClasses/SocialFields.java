package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses;

import Dataverse.DataverseJSONFieldClasses.MetadataType;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONObject;

import java.util.List;

public class SocialFields extends MetadataType {
    private SimpleSocialFields simpleSocialFields;
    private TargetSampleSize targetSampleSize;
    private SocialScienceNotes socialScienceNotes;
    protected String doi;

    public SocialFields(String doi) {
        this.simpleSocialFields = new SimpleSocialFields();
        this.targetSampleSize = new TargetSampleSize();
        this.socialScienceNotes = new SocialScienceNotes();
        this.doi = doi;
    }

    //TODO implement class methods

    @Override
    public void setFields(JSONObject jo) {

    }

    @Override
    public List getListField(String fieldName) {
        return null;
    }

    @Override
    public String getDoi() {
        return null;
    }

    @Override
    public void setDoi(String doi) {

    }

    @Override
    public boolean hasBB() {
        return false;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return null;
    }
}
