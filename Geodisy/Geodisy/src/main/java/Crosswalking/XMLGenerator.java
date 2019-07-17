package Crosswalking;

import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.LinkedList;

import static BaseFiles.GeodisyStrings.CHARACTER;
import static BaseFiles.GeodisyStrings.XMLNS;
import static Dataverse.DVFieldNameStrings.*;

public class XMLGenerator {
    DataverseJavaObject djo;
    CitationFields citationFields;
    GeographicFields geographicFields;
    SimpleCitationFields simpleCitationFields;
    Document doc;

    //TODO write catch block
    public XMLGenerator(DataverseJavaObject djo) {
        this.djo = djo;
        this.citationFields = djo.getCitationFields();
        this.geographicFields=djo.getGeoFields();
        this.simpleCitationFields = citationFields.getSimpleCitationFields();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            docBuilder = docFactory.newDocumentBuilder();

        doc = docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    //TODO keep working on this
    public Document generateXMLFile(){


            // root element
            Element rootElement = doc.createElement("gmd:MD_Metadata");
            rootElement.setAttribute("xmlns",XMLNS + "gmd");
            rootElement.setAttribute("xmlns:gco",XMLNS + "gco");
            rootElement.setAttribute("xmlns:gts",XMLNS + "gts");
            rootElement.setAttribute("xmlns:srv",XMLNS + "srv");
            rootElement.setAttribute("xmlns:gml",XMLNS + "gml");
            rootElement.setAttribute("xmlns:xlink","http://www.w3.org/1999/xlink");
            rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.appendChild(createIdentificationInfo());
        return doc;
    }

    private Element createIdentificationInfo(){
        Element identInfo = doc.createElement("identificationInfo");
        identInfo.appendChild(getMDIdent());
        return identInfo;
    }
    //TODO add sections for other sublevels of MD_DataIdentification
    private Element getMDIdent() {
        Element md_DataIdent = doc.createElement("MD_DataIdentification");
        if(!simpleCitationFields.getField(TITLE).isEmpty())
        {
            Element citation = doc.createElement("citation");
            Element ci_Citation = doc.createElement("CI_Citation");
            citation.appendChild(getCI_Citation(ci_Citation));
            md_DataIdent.appendChild(citation);
        }

        return md_DataIdent;
    }
    //TODO complete
    private Element getCI_Citation(Element ci_Citation) {
        ci_Citation = setCIChild(ci_Citation,"title", simpleCitationFields.getField(TITLE), CHARACTER);
        String subtitleVal = simpleCitationFields.getField(SUBTITLE);
        if(!subtitleVal.isEmpty())
            ci_Citation = setCIChild(ci_Citation,"title", subtitleVal, CHARACTER);

        String altTitleVal = simpleCitationFields.getField(ALT_TITLE);
        if(!altTitleVal.isEmpty())
            ci_Citation = setCIChild(ci_Citation,"alternateTitle", subtitleVal, CHARACTER);

        String altUrlVal = simpleCitationFields.getField(ALT_URL);
        if(!altUrlVal.isEmpty()) {
            Element altURL = doc.createElement(addGMD("onlineResource"));
            altURL = getOnlineResource(altURL,altUrlVal);
            ci_Citation.appendChild(altURL);
        }
        ci_Citation = getIdentifier(ci_Citation);
        return ci_Citation;
    }

    private Element setCIChild(Element ci_Citation, String title, String subtitleVal, String valType) {
        Element subTitle = doc.createElement(addGMD(title));
        subTitle.appendChild(addVal(subtitleVal, valType));
        ci_Citation.appendChild(subTitle);

        return ci_Citation;
    }

    //Adds the element at the lowest level of the hierarchy that holds the value
    private Node addVal(String altTitleVal, String label) {
        Element val = doc.createElement(addGCO(label));
        val.setNodeValue(altTitleVal);
        return val;
    }

    //TODO need more input from Mark
    private Element getIdentifier(Element ci_citation) {
        LinkedList otherIDs = (LinkedList) citationFields.getOtherIDs();



        return ci_citation;
    }

    private Element getOnlineResource(Element onlineResouce, String altUrlVal) {
        Element ciOnline = doc.createElement(addGMD("CI_OnlineResource"));
        Element linkage = doc.createElement(addGMD("linkage"));
        Element url = doc.createElement(addGMD("URL"));
        url.setNodeValue(altUrlVal);
        linkage.appendChild(url);
        ciOnline.appendChild(linkage);
        onlineResouce.appendChild(ciOnline);
        return onlineResouce;
    }

    private Element roleCode(String val){
        Element role = doc.createElement(addGMD("role"));
        Element ci = doc.createElement(addGMD("CI_RoleCode"));
        ci.setNodeValue(val);
        role.appendChild(ci);
        return role;
    }

    //TODO add whatever other fields are possibly needed for descriptive keywords
    private Element descriptiveKeywards(LinkedList<String> keywords, String type){
        return descriptiveKeywards(keywords, type, "nilReason=unknown");
    }

    private Element descriptiveKeywards(LinkedList<String> keywords, String type, String thesaurus){
        Element descriptiveK = doc.createElement(addGMD("descriptiveKeywords"));
        for(String s: keywords){
            Element keyword = doc.createElement(addGMD("keyword"));
            Element charString = doc.createElement(addGCO("CharacterString"));
            charString.setNodeValue(s);
            keyword.appendChild(charString);
            descriptiveK.appendChild(keyword);
        }
        Element typeEl = doc.createElement("type");
        return descriptiveK;
    }

    // GCO indicates generally a value rather than a description
    private String addGCO(String s) {
        return "gco:" + s;
    }
    // GMD is either a description or parent element w/o a value
    private String addGMD(String s){
        return "gmd:" + s;
    }
}
