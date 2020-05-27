package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONLifeFieldClasses;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.MetadataType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static _Strings.DVFieldNameStrings.*;

public class LifeFields extends MetadataType {
    private List<String> studyDesignType, studyFactorType, studyAssayOrganism, studyAssayOtherOrganism,
            studyAssayMeasurementType, studyAssayOtherMeasurmentType, studyAssayTechnologyType,
            studyAssayPlatform, studyAssayCellType;
    protected String doi;
    private GeoLogger logger = new GeoLogger(this.getClass());

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

    }

    @Override
    public LifeFields setFields(JSONObject jo) {
        JSONArray a;
        JSONObject jsonObject;
        String fieldName, value;
        for(Object o: (JSONArray) jo.get(VAL)){
            a = (JSONArray)((JSONObject) o).get(VAL);
            for(Object object: a){
                jsonObject = (JSONObject) object;
                fieldName = jsonObject.getString(TYPE_NAME);
                value = jsonObject.getString(VAL);
                switch(fieldName){
                    case STUDY_DESIGN_TYPE: studyDesignType.add(value); break;
                    case STUDY_FACTOR_TYPE: studyFactorType.add(value);break;
                    case STUDY_ASSAY_ORGANISM: studyAssayOrganism.add(value); break;
                    case STUDY_ASSAY_OTHER_ORGANISM: studyAssayOtherOrganism.add(value); break;
                    case STUDY_ASSAY_MEASUREMENT_TYPE: studyAssayMeasurementType.add(value); break;
                    case STUDY_ASSAY_OTHER_MEASUREMENT_TYPE: studyAssayOtherMeasurmentType.add(value); break;
                    case STUDY_ASSAY_TECHNOLOGY_TYPE: studyAssayTechnologyType.add(value); break;
                    case STUDY_ASSAY_PLATFORM: studyAssayPlatform.add(value); break;
                    case STUDY_ASSAY_CELL_TYPE: studyAssayCellType.add(value); break;
                    default: logger.error("Tried to add a weird field to the Life Fields: "+ fieldName);
                }

            }
        }
        return this;
    }

    @Override
    public List getListField(String fieldName) {
        switch(fieldName){
            case STUDY_DESIGN_TYPE: return studyDesignType;
            case STUDY_FACTOR_TYPE: return studyFactorType;
            case STUDY_ASSAY_ORGANISM: return studyAssayOrganism;
            case STUDY_ASSAY_OTHER_ORGANISM: return studyAssayOtherOrganism;
            case STUDY_ASSAY_MEASUREMENT_TYPE: return studyAssayMeasurementType;
            case STUDY_ASSAY_OTHER_MEASUREMENT_TYPE: return studyAssayOtherMeasurmentType;
            case STUDY_ASSAY_TECHNOLOGY_TYPE: return studyAssayTechnologyType;
            case STUDY_ASSAY_PLATFORM: return studyAssayPlatform;
            case STUDY_ASSAY_CELL_TYPE: return studyAssayCellType;
            default: logger.error("Tried to get a weird field from Life Science: " + fieldName);
            return new LinkedList();
        }
    }

    @Override
    public String getDoi() {
        return doi;
    }

    @Override
    public void setDoi(String doi) {
        this.doi = doi;
    }

}
