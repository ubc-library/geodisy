package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses;

import Dataverse.DataverseJSONFieldClasses.MetadataType;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class JournalFields extends MetadataType {
    private String journalArticleType;
    private List<JournalVolIssue> journalVolIssues;
    protected String doi;

    public JournalFields(String doi) {
        this.journalArticleType = "";
        this.journalVolIssues = new LinkedList<>();
        this.doi = doi;
    }

    //TODO implement methods
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
