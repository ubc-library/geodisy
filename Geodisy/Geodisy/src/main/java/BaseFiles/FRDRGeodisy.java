package BaseFiles;

import Dataverse.SourceJavaObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FRDRGeodisy extends Geodisy{
    LinkedList<SourceJavaObject> records;
    @Override
    public List<SourceJavaObject> harvestDataverseMetadata(){
        boolean moreRecords = true;
        records = new LinkedList<>();
        while(moreRecords){
            moreRecords = getFRDRRecord();
        }

        return records;
    }

    private boolean getFRDRRecord() {

    }


}
