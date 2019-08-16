package Crosswalking;

import Crosswalking.XML.GeoCombine;
import Crosswalking.XML.JGit;
import Crosswalking.XML.XMLDocument;
import Dataverse.DataverseJavaObject;
import Dataverse.SourceJavaObject;

import java.util.LinkedList;
import java.util.List;

public class Crosswalk implements CrosswalkInterface {
    //TODO write the crosswalk class
    @Override
    public void convertSJOs(List<SourceJavaObject> records) {
        MetadataSchema metadata = new ISO_19115();
        sendXMLToGit(metadata.generateXML(records));
    }

    @Override
    public void sendXMLToGit(List<XMLDocument> docs) {
        JGit jgit = new JGit();
        jgit.updateXML(docs);
        GeoCombine geoCombine =  new GeoCombine();
        for(XMLDocument doc:docs){
            geoCombine.generateGeoBlacklightXML(doc.getDoi());
        }
        geoCombine.call();
    }
}
