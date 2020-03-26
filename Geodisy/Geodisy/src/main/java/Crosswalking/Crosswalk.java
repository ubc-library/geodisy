package Crosswalking;


import Dataverse.SourceJavaObject;

public class Crosswalk implements CrosswalkInterface {
    @Override
    public void convertSJO(SourceJavaObject record) {
        XMLSchema metadata = new ISO_19115();
        metadata.generateXML(record);
        System.out.println("Finished Creating XML files");
    }
}
