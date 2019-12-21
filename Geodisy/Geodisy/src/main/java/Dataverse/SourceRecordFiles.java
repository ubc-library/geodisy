package Dataverse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class SourceRecordFiles {
    private static Map<String, DataverseRecordFile> files;
    private static SourceRecordFiles single_instance = null;

    public static SourceRecordFiles getSourceRecords(){
        if(single_instance == null)
            single_instance =  new SourceRecordFiles();
        return single_instance;
    }

    public SourceRecordFiles() {
        files = new HashMap<>();
    }

    public DataverseRecordFile getSingleRecord(String doi, String fileName) throws NullPointerException{
        return files.get(doi+fileName);
    }

    public void putRecord(String doi, String fileName, DataverseRecordFile drv){
        files.put(doi+fileName,drv);
    }

    public Set<String> getKeys(){
        return files.keySet();
    }
}
