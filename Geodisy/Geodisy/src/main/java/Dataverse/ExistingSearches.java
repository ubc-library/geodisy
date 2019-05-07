package Dataverse;

import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class that holds bounding boxes that have already been found and what versions of records have already been downloaded.
 */
public class ExistingSearches implements Serializable {
    private static final long serialVersionUID = 8947943825774008362L;
    HashMap<String, BoundingBox> bBoxes;
    HashMap<String, DataverseRecordInfo> recordVersions;
    private static ExistingSearches single_instance = null;


    public static ExistingSearches getExistingSearches() {
        if (single_instance == null) {
            single_instance = new ExistingSearches();
            single_instance.addBBox("_filler_", new BoundingBox());
        }
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
        return bBoxes.containsKey(doi);
    }

    public BoundingBox getBBox(String doi){
        if(hasBBox(doi))
            return bBoxes.get(doi);
        return new BoundingBox();
    }

    public int numberOfBBoxes(){
        return bBoxes.size();
    }

    public int numberOfRecords(){
        return recordVersions.size();
    }

    public void addOrReplaceRecord(DataverseRecordInfo dataverseRecordInfo){
        recordVersions.put(dataverseRecordInfo.getDoi(), dataverseRecordInfo);
    }

    public boolean isNewerRecord(DataverseRecordInfo dataverseRecordInfo){
        return dataverseRecordInfo.younger(recordVersions.get(dataverseRecordInfo.getDoi()));
    }


    public DataverseRecordInfo getRecordInfo(String doi){
        if(recordVersions.containsKey(doi))
            return recordVersions.get(doi);
        return new DataverseRecordInfo();
    }

    public void deleteBBox(String location){
        bBoxes.remove(location);
    }

    public boolean hasRecord(String doi){
        return recordVersions.containsKey(doi);
    }


}
