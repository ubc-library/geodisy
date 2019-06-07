package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Dataverse.DVFieldNameStrings.*;

public class SimpleSocialFields extends CompoundJSONField {
    private String timeMethod, dataCollector, frequencyOfDataCollection, samplingProcedure,
            deviationsFromSampleDesign, collectionMode, researchInstrument, dataCollectionSituation,
            actionsToMinimizeLoss, controlOperations, weighting, cleaningOperations, datasetLevelErrorNotes,
            responseRate, samplingErrorEstimates, otherDataAppraisals;
    private List<String> unitOfAnalysis, universe;

    public SimpleSocialFields(){
        timeMethod = "";
        dataCollector = "";
        frequencyOfDataCollection = "";
        samplingProcedure = "";
        deviationsFromSampleDesign = "";
        collectionMode = "";
        researchInstrument = "";
        dataCollectionSituation = "";
        actionsToMinimizeLoss = "";
        controlOperations = "";
        weighting = "";
        cleaningOperations = "";
        datasetLevelErrorNotes = "";
        responseRate = "";
        samplingErrorEstimates = "";
        otherDataAppraisals = "";
        unitOfAnalysis = new LinkedList<>();
        universe = new LinkedList<>();
    }

    @Override
    public void setField(JSONObject jo) {
        String fieldName =  jo.getString(TYPE_NAME);
        String val = jo.getString(VAL);
        switch(fieldName){
            case TIME_METHOD: this.timeMethod = val; break;
            case DATA_COLLECTOR: this.dataCollector = val; break;
            case FREQUENCY_OF_DATA_COLLECTION: this.frequencyOfDataCollection = val; break;
            case SAMPLING_PROCEDURE: this.samplingProcedure = val; break;
            case DEVIATIONS_FROM_SAMPLE_DESIGN: this.deviationsFromSampleDesign = val; break;
            case COLLECTION_MODE: this.collectionMode = val; break;
            case RESEARCH_INSTRUMENT: this.researchInstrument = val; break;
            case DATA_COLLECTION_SITUATION: this.dataCollectionSituation = val; break;
            case ACTIONS_TO_MINIMIZE_LOSS: this.actionsToMinimizeLoss = val; break;
            case CONTROL_OPERATIONS: this.controlOperations = val; break;
            case WEIGHTING: this.weighting = val; break;
            case CLEANING_OPERATIONS: this.cleaningOperations = val; break;
            case DATASET_LEVEL_ERROR_NOTES: this.datasetLevelErrorNotes = val; break;
            case RESPONSE_RATE: this.responseRate = val; break;
            case SAMPLING_ERROR_ESTIMATES: this.samplingErrorEstimates = val; break;
            case OTHER_DATA_APPRAISAL: this.otherDataAppraisals = val; break;
            case UNIT_OF_ANALYSIS: this.unitOfAnalysis.add(val); break;
            case UNIVERSE: this.universe.add(val); break;
            default: errorParsing(this.getClass().toString(), fieldName);

        }
    }

    @Override
    public String getField(String fieldName) {
        switch (fieldName) {
            case TIME_METHOD: return timeMethod;
            case DATA_COLLECTOR: return dataCollector;
            case FREQUENCY_OF_DATA_COLLECTION: return frequencyOfDataCollection;
            case SAMPLING_PROCEDURE: return samplingProcedure;
            case DEVIATIONS_FROM_SAMPLE_DESIGN: return deviationsFromSampleDesign;
            case COLLECTION_MODE: return collectionMode;
            case RESEARCH_INSTRUMENT: return researchInstrument;
            case DATA_COLLECTION_SITUATION: return dataCollectionSituation;
            case ACTIONS_TO_MINIMIZE_LOSS: return actionsToMinimizeLoss;
            case CONTROL_OPERATIONS: return controlOperations;
            case WEIGHTING: return weighting;
            case CLEANING_OPERATIONS: return cleaningOperations;
            case DATASET_LEVEL_ERROR_NOTES: return datasetLevelErrorNotes;
            case RESPONSE_RATE: return responseRate;
            case SAMPLING_ERROR_ESTIMATES: return samplingErrorEstimates;
            case OTHER_DATA_APPRAISAL: return otherDataAppraisals;
            case UNIT_OF_ANALYSIS:
            case UNIVERSE: logger.error("Tried to get a List<String> from the get String method, should have used getListField method in SimpleSocialFields class");
            default:
                errorGettingValue(this.getClass().toString(), fieldName);
                return "Error in the get method of SimpleSocialFields with a call for " + fieldName;
        }
    }

    public List<String> getListField(String fieldName){
        if(fieldName.equals(UNIT_OF_ANALYSIS))
            return unitOfAnalysis;
        if(fieldName.equals(UNIVERSE))
            return universe;
        errorGettingValue(this.getClass().toString(),fieldName);
        return new LinkedList<>();
    }
}
