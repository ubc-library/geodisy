package Dataverse;


import java.io.Serializable;
import java.util.HashMap;


public class ExistingCallsToCheck implements Serializable{
    private static final long serialVersionUID = 5416853597895403102L;
    HashMap<String, DataverseRecordInfo> records;
    private static ExistingCallsToCheck single_instance = null;


    public static ExistingCallsToCheck getExistingCallsToCheck() {
        if (single_instance == null) {
            single_instance = new ExistingCallsToCheck();
        }
        return single_instance;
    }

    private ExistingCallsToCheck(){
        records = new HashMap<>();
    }
    public boolean isEmpty(){
        return records.isEmpty();
    }
    public void addRecord(String name, DataverseRecordInfo dRI){
        records.put(name,dRI);
    }

    private boolean hasRecord(String doi){
        return records.containsKey(doi);
    }


    public int numberOfRecords(){
        return records.size();
    }

    public void addOrReplaceRecord(DataverseRecordInfo dataverseRecordInfo){
        records.put(dataverseRecordInfo.getDoi(), dataverseRecordInfo);
    }

    public boolean isNewerRecord(DataverseRecordInfo dataverseRecordInfo){
        return dataverseRecordInfo.younger(records.get(dataverseRecordInfo.getDoi()));
    }


    public DataverseRecordInfo getRecordInfo(String doi){
        if(records.containsKey(doi))
            return records.get(doi);
        return new DataverseRecordInfo();
    }

    public void deleteRecords(String location){
        records.remove(location);
    }



}
