package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses;

import Dataverse.DataverseJSONFieldClasses.JSONField;

import java.util.List;

public class SimpleSocialFields extends JSONField {
    private String timeMethod, dataCollector, collectorTraining, frequencyOfDataCollection, samplingProcedure,
            deviationsFromSampleDesign, collectionMode, researchInstrument, dataCollectionSituation,
            actionsToMinimizeLoss, controlOperations, weighting, cleaningOperations, datasetLevelErrorNotes,
            responseRate, samplingErrorEstimates, otherDataAppraisal;
    private List<String> unitOfAnalysis;
    private List<String> universe;



    @Override
    public String getField(String fieldName) {
        return null;
    }
}
