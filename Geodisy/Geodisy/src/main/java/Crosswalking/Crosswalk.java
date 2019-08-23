package Crosswalking;


import Crosswalking.XML.XMLTools.GeoCombine;
import Crosswalking.XML.XMLTools.JGit;
import Crosswalking.XML.XMLTools.XMLDocObject;
import Dataverse.SourceJavaObject;

import java.util.List;

public class Crosswalk implements CrosswalkInterface {
    //TODO write the crosswalk class
    @Override
    public void convertSJOs(List<SourceJavaObject> records) {
        MetadataSchema metadata = new ISO_19115();
        sendXMLToGit(metadata.generateXML(records));
    }

    @Override
    public void sendXMLToGit(List<XMLDocObject> docs) {

        //Make sure local files are up to date
        GeoCombine geoCombine =  new GeoCombine();
        geoCombine.call();

        JGit jgit = new JGit();
        jgit.updateXML(docs);

        for(XMLDocObject doc:docs){
            geoCombine.generateGeoBlacklightXML(doc.getDoi(), jgit);
        }

        jgit.pushToGit();


    }
}
