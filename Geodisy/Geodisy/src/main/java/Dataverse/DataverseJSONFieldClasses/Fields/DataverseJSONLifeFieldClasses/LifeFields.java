package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONLifeFieldClasses;

import Dataverse.DataverseJSONFieldClasses.MetadataType;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class LifeFields extends MetadataType {
    private List<String> studyDesignType, studyFactorType, studyAssayOrganism, studyAssayOtherOrganism,
            studyAssayMeasurementType, studyAssayOtherMeasurmentType, studyAssayTechnologyType,
            studyAssayPlatform, studyAssayCellType;
    protected String doi;

    public LifeFields(String doi) {
        this.studyDesignType = new LinkedList<>();
        this.studyFactorType = new LinkedList<>();
        this.studyAssayOrganism = new LinkedList<>();
        this.studyAssayOtherOrganism = new LinkedList<>();
        this.studyAssayMeasurementType = new LinkedList<>();
        this.studyAssayOtherMeasurmentType = new LinkedList<>();
        this.studyAssayTechnologyType = new LinkedList<>();
        this.studyAssayPlatform = new LinkedList<>();
        this.studyAssayCellType = new LinkedList<>();
        this.doi = doi;

        //TODO implement Class methods
    }

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
