package Dataverse;


import BaseFiles.GeoLogger;

import java.io.Serializable;
import java.util.HashMap;

import static _Strings.GeodisyStrings.EXISTING_CHECKS;


public class ExistingCallsToCheck extends ExistingSearches implements Serializable{
    private static final long serialVersionUID = 5416853597895403102L;
    private HashMap<String, DataverseRecordInfo> records;
    private HashMap<String, String> newRecords;
    private static ExistingCallsToCheck single_instance = null;

    public static ExistingCallsToCheck getExistingCallsToCheck() {
        if (single_instance == null) {
            single_instance = new ExistingCallsToCheck();
        }
        return single_instance;
    }

    private ExistingCallsToCheck(){
        records = readExistingRecords(EXISTING_CHECKS);
        newRecords = new HashMap<>();

    }
    public boolean isEmpty(){
        return records.isEmpty();
    }
    public void addRecord(String name, DataverseRecordInfo dRI){
        records.put(name,dRI);
    }

    public boolean hasRecord(String doi){
        return records.containsKey(doi);
    }


    public int numberOfRecords(){
        return records.size();
    }

    public void addOrReplaceRecord(DataverseRecordInfo dataverseRecordInfo, String message){
        records.put(dataverseRecordInfo.getDoi(), dataverseRecordInfo);
        newRecords.put(dataverseRecordInfo.getDoi(), message);
    }

    public boolean isNewerRecord(DataverseRecordInfo dataverseRecordInfo){
        return dataverseRecordInfo.newer(records.get(dataverseRecordInfo.getDoi()));
    }

    public boolean isNewerRecord(DataverseRecordInfo dataverseRecordInfo, String loggerName){
        DataverseRecordInfo driSaved = records.get(dataverseRecordInfo.getDoi());
        return dataverseRecordInfo.newer(driSaved) && loggerName.equals(driSaved.getLoggerName());
    }

    public HashMap<String, String> getNewRecords(){
        return newRecords;
    }
    public HashMap<String, DataverseRecordInfo> getRecords(){
        return records;
    }

    public DataverseRecordInfo getRecordInfo(String doi){
        if(records.containsKey(doi))
            return records.get(doi);
        return new DataverseRecordInfo();
    }

    public void deleteRecords(String location){
        records.remove(location);
    }

    @Override
    protected GeoLogger getLogger() {
        if (logger == null) {
            logger = new GeoLogger(this.getClass());
        }
        return logger;
    }

}
