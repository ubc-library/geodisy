package Dataverse;

import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;

import java.io.Serializable;
import java.util.HashMap;


public class ExistingSearches implements Serializable {
    HashMap<String, BoundingBox> bBoxes;
    HashMap<String, DataverseRecordInfo> recordVersions;
    private static ExistingSearches single_instance = null;

    public static ExistingSearches getExistingSearches() {
        if (single_instance == null)
            single_instance = new ExistingSearches();
        return single_instance;
    }

    private ExistingSearches(){
        bBoxes = new HashMap<>();
        recordVersions = new HashMap<>();
    }
    public boolean isEmpty(){
        return bBoxes.isEmpty()&&recordVersions.isEmpty();
    }
    public void addBBox(String name, BoundingBox boundingBox){
        bBoxes.put(name,boundingBox);
    }

    private boolean hasBBox(String doi){
        if(bBoxes.containsKey(doi))
            return true;
        return false;
    }

    public BoundingBox getBBox(String doi){
        if(hasBBox(doi))
            return bBoxes.get(doi);
        return new BoundingBox();
    }

    public void addOrReplaceRecord(DataverseRecordInfo dataverseRecordInfo){
        recordVersions.put(dataverseRecordInfo.getDoi(), dataverseRecordInfo);
    }

    public boolean isNewerRecord(DataverseRecordInfo dataverseRecordInfo){
        return dataverseRecordInfo.younger(recordVersions.get(dataverseRecordInfo.getDoi()));
    }
}
