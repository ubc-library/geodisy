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
    SimpleCitationFields simpleCitationFields;
    XMLDocument doc;

    //TODO write catch block
    public XMLGenerator(DataverseJavaObject djo) {
        this.djo = djo;
        this.citationFields = djo.getCitationFields();
        this.geographicFields=djo.getGeoFields();
        this.simpleCitationFields = citationFields.getSimpleCitationFields();
    }

    //TODO keep working on this
    public Document generateXMLFile(){
        Element rootElement = getRoot();
        rootElement.appendChild(createIdentificationInfo());
        SimpleCitationFields simple = djo.getSimpleFields();
        if(simple.hasField(ACCESS_TO_SOURCES)||simple.hasField(ORIG_OF_SOURCES)||simple.hasField(CHAR_OF_SOURCES)||simple.hasField(DATA_SOURCE))
            rootElement.appendChild(createDataQualityInfo(simple));
        CitationFields cf = djo.getCitationFields();
        LinkedList<Distributor> distributors = (LinkedList) cf.getListField(DISTRIB);
        if(distributors.size()>0||simpleCitationFields.hasField(DEPOSITOR))
            rootElement.appendChild(getDistribInfo(distributors));
        IdentificationInfo ii =  new IdentificationInfo(djo, doc, rootElement);

        rootElement = ii.generateIdentInfo();



        if(otherIds.size()>0)
        return doc.getDoc();
    }

    private Element getDistribInfo(List<Distributor> distributors) {
        Element temp;
        String tempString;
        Element root = doc.createGMDElement("distributionInfo");
        Element levelA = doc.createGMDElement("MD_Distribution");
        for (Distributor d : distributors) {
            Element levelB = doc.createGMDElement("distributor");
            Element levelC = doc.createGMDElement("MD_Distributor");
            Element levelD = doc.createGMDElement("distributorContact");
            Element levelE = doc.createGMDElement("CI_Responsibility");
            Element levelF = doc.createGMDElement("party");
            Element levelG = doc.createGMDElement("CI_Organization");
            tempString = d.getDistributorName();
            if(!d.getDistributorName().isEmpty()) {
                temp = doc.createGMDElement("name");
                temp.appendChild(doc.addGCOVal(tempString,CHARACTER));
                levelG.appendChild(temp);
            }
            tempString = d.getDistributorAffiliation();
            if(!d.getDistributorName().isEmpty()) {
                temp = doc.createGMDElement("name");
                temp.appendChild(doc.addGCOVal(tempString,CHARACTER));
                levelG.appendChild(temp);
            }
            if(!d.getDistributorLogoURL().isEmpty()||!d.getDistributorURL().isEmpty()){
                if(!d.getDistributorURL().isEmpty()) {
                    doc.stackElement(doc.createGMDElement("contactInfo"));
                    doc.stackElement(doc.createGMDElement("CI_Contact"));
                    doc.stackElement(doc.createGMDElement("onlineResource"));
                    doc.stackElement(doc.createGMDElement("CI_OnlineResource"));
                    doc.stackElement(doc.createGMDElement("linkage"));
                    doc.stackElement(doc.addGCOVal(d.getDistributorURL(),CHARACTER));
                    levelG.appendChild(doc.zip());
                }
                if(!d.getDistributorLogoURL().isEmpty()){
                    doc.stackElement(doc.createGMDElement("logo"));
                    doc.stackElement(doc.createGMDElement("MD_BrowseGraphic"));
                    doc.stackElement(doc.createGMDElement("linkage"));
                    doc.stackElement(doc.addGCOVal(d.getDistributorLogoURL(),CHARACTER));
                    levelG.appendChild(doc.zip());
                }
            }
            levelF.appendChild(levelG);
            levelE.appendChild(levelF);
            Element levelRoleCode = doc.create_Element("role");
            levelRoleCode.appendChild(doc.addRoleCode("distributor"));
            levelE.appendChild(levelRoleCode);

            levelD.appendChild(levelE);
            levelC.appendChild(levelD);
            levelB.appendChild(levelC);
            levelA.appendChild(levelB);
        }
        if(simpleCitationFields.hasField(DEPOSITOR))
            levelA.appendChild(getDepositor(simpleCitationFields.getField(DEPOSITOR)));
        root.appendChild(levelA);
        return root;
    }

    private Element getDepositor(String depositorName) {
        Element levelB = doc.createGMDElement("distributor");
        Element levelC = doc.createGMDElement("MD_Distributor");
        Element levelD = doc.createGMDElement("distributorContact");
        Element levelE = doc.createGMDElement("CI_Responsibility");
        Element levelF = doc.createGMDElement("party");
        Element levelG = doc.createGMDElement("CI_Organization");
        Element levelH = doc.createGMDElement("name");
        levelH.appendChild(doc.addGCOVal(depositorName,CHARACTER));
        levelG.appendChild(levelH);
        levelF.appendChild(levelG);
        levelE.appendChild(levelF);
        Element levelRoleCode = doc.create_Element("role");
        levelRoleCode.appendChild(doc.addRoleCode("distributor"));
        levelE.appendChild(levelRoleCode);
        levelD.appendChild(levelE);
        levelC.appendChild(levelD);
        levelB.appendChild(levelC);
        return levelB;
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

    private Element createDataQualityInfo(SimpleCitationFields simple){
        Element temp;
        Element root = doc.createGMDElement("dataQualityInfo");
        Element levelA = doc.createGMDElement("DQ_DataQuality");
        Element levelB = doc.createGMDElement("scope");
        Element levelC = doc.createGMDElement("resourceLineage");
        Element levelD = doc.createGMDElement("LI_Lineage");
        if(simple.hasField(DATA_SOURCE))
            levelD.appendChild(doc.addGCOVal("statement",CHARACTER));
        if(simple.hasField(ORIG_OF_SOURCES)) {
            doc.stackElement(doc.createGMDElement("processStep"));
            doc.stackElement(doc.createGMDElement("LI_ProcessStep"));
            doc.stackElement(doc.createGMDElement("description"));
            doc.stackElement(doc.addGCOVal(simple.getField(ORIG_OF_SOURCES), CHARACTER));
            temp = doc.stack.zip();
            if (!temp.getTagName().equals("__no elements__"))
                levelD.appendChild(temp);
        }
        if(simple.hasField(CHAR_OF_SOURCES)) {
            doc.stackElement(doc.createGMDElement("source"));
            doc.stackElement(doc.createGMDElement("LI_Source"));
            doc.stackElement(doc.createGMDElement("description"));
            doc.stackElement(doc.addGCOVal(simple.getField(CHAR_OF_SOURCES), CHARACTER));
            temp = doc.stack.zip();
            if (!temp.getTagName().equals("__no elements__"))
                levelD.appendChild(temp);
        }
        if(simple.hasField(ACCESS_TO_SOURCES)){
            doc.stackElement(doc.createGMDElement("additionalDocumentation"));
            doc.stackElement(doc.createGMDElement("processStep"));
            doc.stackElement(doc.createGMDElement("otherCitationDetails"));
            doc.stackElement(doc.addGCOVal(simple.getField(ACCESS_TO_SOURCES), CHARACTER));
            temp = doc.stack.zip();
            if (!temp.getTagName().equals("__no elements__"))
                levelD.appendChild(temp);
        }
        levelC.appendChild(levelD);
        levelB.appendChild(levelC);
        levelA.appendChild(levelB);
        root.appendChild(levelA);
        return root;
    }

    private Element createIdentificationInfo(){
        Element identInfo = doc.createGMDElement("identificationInfo");
        identInfo.appendChild(getMDIdent());
        return identInfo;
    }
    //TODO add sections for other sublevels of MD_DataIdentification
    private Element getMDIdent() {
        Element md_DataIdent = doc.createGMDElement("MD_DataIdentification");
        if(!simpleCitationFields.getField(TITLE).isEmpty())
        {
            Element citation = doc.createGMDElement("citation");
            Element ci_Citation = doc.createGMDElement("CI_Citation");
            citation.appendChild(getCI_Citation(ci_Citation));
            md_DataIdent.appendChild(citation);
        }

        return md_DataIdent;
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
