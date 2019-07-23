package Crosswalking.XML;


import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.*;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.List;

import static BaseFiles.GeodisyStrings.CHARACTER;
import static BaseFiles.GeodisyStrings.XMLNS;
import static Dataverse.DVFieldNameStrings.*;

public class XMLGenerator {
    DataverseJavaObject djo;
    CitationFields citationFields;
    GeographicFields geographicFields;
    SimpleCitationFields simple;
    XMLDocument doc;

    //TODO write catch block
    public XMLGenerator(DataverseJavaObject djo) {
        this.djo = djo;
        this.citationFields = djo.getCitationFields();
        this.geographicFields=djo.getGeoFields();
        this.simple = citationFields.getSimpleCitationFields();
    }

    //TODO keep working on this
    public Document generateXMLFile(){
        Element rootElement = getRoot();
        rootElement.appendChild(createIdentificationInfo(rootElement));
        if(simple.hasField(ACCESS_TO_SOURCES)||simple.hasField(ORIG_OF_SOURCES)||simple.hasField(CHAR_OF_SOURCES)||citationFields.getListField(DATA_SOURCE).size()>0) {
            DataQualityInfo dqi = new DataQualityInfo(djo, doc, rootElement);
            rootElement.appendChild(dqi.getDataQualityInfo(simple, citationFields));
        }
        CitationFields cf = djo.getCitationFields();
        LinkedList<Distributor> distributors = (LinkedList) cf.getListField(DISTRIB);
        if(distributors.size()>0||simple.hasField(DEPOSITOR)) {
            DistributionInfo distribInfo = new DistributionInfo(djo,doc,rootElement,simple, distributors);
            rootElement.appendChild(distribInfo.getDistribInfo());
        }



        //TODO check the following line. I think dealing with other IDs happens elsewhere.
        //if(otherIds.size()>0)
        return doc.getDoc();
    }



    private Element getRoot() {
        // root element
        Element rootElement = doc.createGMDElement("MD_Metadata");
        rootElement.setAttribute("xmlns",XMLNS + "gmd");
        rootElement.setAttribute("xmlns:gco",XMLNS + "gco");
        rootElement.setAttribute("xmlns:gts",XMLNS + "gts");
        rootElement.setAttribute("xmlns:srv",XMLNS + "srv");
        rootElement.setAttribute("xmlns:gml",XMLNS + "gml");
        rootElement.setAttribute("xmlns:xlink","http://www.w3.org/1999/xlink");
        rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        return rootElement;
    }


    private Element createIdentificationInfo(Element root){
        IdentificationInfo ident = new IdentificationInfo(djo, doc, root);
        root = ident.generateIdentInfo();
        return root;
    }




    //TODO need more input from Mark
    private Element getIdentifier(Element ci_citation) {
        LinkedList otherIDs = (LinkedList) citationFields.getOtherIDs();


        return ci_citation;
    }



    private Element roleCode(String val){
        Element role = doc.createGMDElement("role");
        Element ci = doc.createGMDElement("CI_RoleCode");
        ci.setNodeValue(val);
        role.appendChild(ci);
        return role;
    }

    //TODO add whatever other fields are possibly needed for descriptive keywords
    private Element descriptiveKeywards(LinkedList<String> keywords, String type){
        return descriptiveKeywards(keywords, type, "nilReason=unknown");
    }

    private Element descriptiveKeywards(LinkedList<String> keywords, String type, String thesaurus){
        Element descriptiveK = doc.createGMDElement("descriptiveKeywords");
        for(String s: keywords){
            Element keyword = doc.createGMDElement("keyword");
            Element charString = doc.addGCOVal(s,CHARACTER);
            keyword.appendChild(charString);
            descriptiveK.appendChild(keyword);
        }
        Element typeEl = doc.createGMDElement("type");
        return descriptiveK;
    }


    
}
