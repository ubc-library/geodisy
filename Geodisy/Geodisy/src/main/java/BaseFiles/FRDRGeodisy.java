package BaseFiles;

import Dataverse.FRDRAPI;
import Dataverse.SourceJavaObject;

import java.util.LinkedList;
import java.util.List;

public class FRDRGeodisy extends Geodisy{
    LinkedList<SourceJavaObject> records;
    public List<SourceJavaObject> harvestFRDRMetadata(){
        records = new LinkedList<>();
        FRDRAPI frdrAPI = new FRDRAPI();
        records = frdrAPI.callFRDRHarvester();

        return records;
    }
}
