package Crosswalking.XML;

import Crosswalking.Crosswalking;
import Crosswalking.XML.XMLDocument;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.LinkedList;
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

            // root element
            Element rootElement = doc.createGMDElement("MD_Metadata");
            rootElement.setAttribute("xmlns",XMLNS + "gmd");
            rootElement.setAttribute("xmlns:gco",XMLNS + "gco");
            rootElement.setAttribute("xmlns:gts",XMLNS + "gts");
            rootElement.setAttribute("xmlns:srv",XMLNS + "srv");
            rootElement.setAttribute("xmlns:gml",XMLNS + "gml");
            rootElement.setAttribute("xmlns:xlink","http://www.w3.org/1999/xlink");
            rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.appendChild(createIdentificationInfo());
        SimpleCitationFields simple = djo.getSimpleFields();
        if(simple.hasField(ACCESS_TO_SOURCES)||simple.hasField(ORIG_OF_SOURCES)||simple.hasField(CHAR_OF_SOURCES)||simple.hasField(DATA_SOURCE))
            rootElement.appendChild(createDataQualityInfo(simple));
        
        
        return doc.getDoc();
    }
    private Element createDataQualityInfo(SimpleCitationFields simple){
        Element root = doc.createGMDElement("dataQualityInfo");
        Element levelA = doc.createGMDElement("DQ_DataQuality");
        Element levelB = doc.createGMDElement("scope");
        Element levelC = doc.createGMDElement("resourceLineage");
        Element levelD = doc.createGMDElement("LI_Lineage");
        if(simple.hasField(DATA_SOURCE))
            levelD.appendChild(doc.addVal("statement",CHARACTER));
        if(simple.hasField(ORIG_OF_SOURCES)) {
            doc.stackElement(doc.createGMDElement("processStep"));
            doc.stackElement(doc.createGMDElement("LI_ProcessStep"));
            doc.stackElement(doc.createGMDElement("description"));
            doc.stackElement(doc.addVal(simple.getField(ORIG_OF_SOURCES),CHARACTER));
            //TODO need to unzip and then put the lower stack onto Stack plus still need to stack the lower values listed above
        }
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
    //TODO complete
    private Element getCI_Citation(Element ci_Citation) {
        String subtitleVal = simpleCitationFields.getField(SUBTITLE);
        String title = simpleCitationFields.getField(TITLE);
        if(!subtitleVal.isEmpty())
            title += ":" + subtitleVal;
        ci_Citation = setCIChild(ci_Citation,"title", title, CHARACTER);
        String altTitleVal = simpleCitationFields.getField(ALT_TITLE);
        if(!altTitleVal.isEmpty())
            ci_Citation = setCIChild(ci_Citation,"alternateTitle", subtitleVal, CHARACTER);

        String altUrlVal = simpleCitationFields.getField(ALT_URL);
        if(!altUrlVal.isEmpty()) {
            Element altURL = doc.createGMDElement("onlineResource");
            altURL = getOnlineResource(altURL,altUrlVal);
            ci_Citation.appendChild(altURL);
        }
        ci_Citation = getIdentifier(ci_Citation);
        return ci_Citation;
    }

    private Element setCIChild(Element ci_Citation, String title, String subtitleVal, String valType) {
        Element subTitle = doc.createGMDElement(title);
        subTitle.appendChild(doc.addVal(subtitleVal, valType));
        ci_Citation.appendChild(subTitle);

        return ci_Citation;
    }



    //TODO need more input from Mark
    private Element getIdentifier(Element ci_citation) {
        LinkedList otherIDs = (LinkedList) citationFields.getOtherIDs();


        return ci_citation;
    }

    private Element getOnlineResource(Element onlineResouce, String altUrlVal) {
        Element ciOnline = doc.createGMDElement("CI_OnlineResource");
        Element linkage = doc.createGMDElement("linkage");
        Element url = doc.createGMDElement("URL");
        url.setNodeValue(altUrlVal);
        linkage.appendChild(url);
        ciOnline.appendChild(linkage);
        onlineResouce.appendChild(ciOnline);
        return onlineResouce;
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
            Element charString = doc.addVal(s,CHARACTER);
            keyword.appendChild(charString);
            descriptiveK.appendChild(keyword);
        }
        Element typeEl = doc.createGMDElement("type");
        return descriptiveK;
    }


    
}
