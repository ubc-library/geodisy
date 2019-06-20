package Crosswalking;

import Dataverse.SourceJavaObject;

import java.util.LinkedList;

public class Crosswalk implements Crosswalking {
    //TODO write the crosswalk class
    @Override
    public void convertDJO(LinkedList<SourceJavaObject> records) {
        MetadataSchema metadata = new ISO_19115();
        for(SourceJavaObject sJO: records){
            metadata.crosswalk(sJO);

        }
    }
}
