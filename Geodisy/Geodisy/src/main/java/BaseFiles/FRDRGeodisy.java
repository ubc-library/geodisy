package BaseFiles;

import Dataverse.DataverseJavaObject;
import Dataverse.FRDRAPI;
import Dataverse.SourceJavaObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FRDRGeodisy extends Geodisy{
    LinkedList<DataverseJavaObject> records;
    public List<DataverseJavaObject> harvestFRDRMetadata(){
        records = new LinkedList<>();
        FRDRAPI frdrAPI = new FRDRAPI();
        records = frdrAPI.callFRDRHarvester();

        return records;
    }
    //TODO connect to frdr harvester for records
    private boolean getFRDRRecord() {

        return false;
    }


}
