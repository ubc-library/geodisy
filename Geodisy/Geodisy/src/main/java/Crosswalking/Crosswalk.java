package Crosswalking;


import Crosswalking.XML.XMLTools.GeoCombine;
import Crosswalking.XML.XMLTools.JGit;
import Crosswalking.XML.XMLTools.XMLDocObject;
import Dataverse.SourceJavaObject;

import java.util.List;

public class Crosswalk implements CrosswalkInterface {
    @Override
    public void convertSJOs(List<SourceJavaObject> records) {
        MetadataSchema metadata = new ISO_19115();
        sendXMLToGit(metadata.generateXML(records));
        System.out.println("Finished Creating XML files");
    }

    @Override
    public void sendXMLToGit(List<XMLDocObject> docs) {

        //Make sure local files are up to date
        GeoCombine geoCombine =  new GeoCombine();
        //geoCombine.call();

        //TODO uncomment once I've got JGit working
        /*JGit jgit = new JGit();
        jgit.updateXML(docs);

        for(XMLDocObject doc:docs){
            geoCombine.generateGeoBlacklightXML(doc.getDoi(), jgit);
        }

        jgit.pushToGit();
        */

    }
}
