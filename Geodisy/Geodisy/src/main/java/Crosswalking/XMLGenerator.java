package Crosswalking;

import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static BaseFiles.GeodisyStrings.XMLNS;
import static Dataverse.DVFieldNameStrings.TITLE;

public class XMLGenerator {
    DataverseJavaObject djo;
    Document doc;

    //TODO write catch block
    public XMLGenerator(DataverseJavaObject djo) {
        this.djo = djo;
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
        Element md_DataIdend = doc.createElement("MD_DataIdentification");
        SimpleCitationFields simple = djo.getSimpleFields();
        if(!djo.getSimpleFields().getField(TITLE).isEmpty())
        {
            Element citation = doc.createElement("citation");
            Element ci_Citation = doc.createElement("CI_Citation");
            citation.appendChild(getCI_Citation(ci_Citation));
            md_DataIdend.appendChild(citation);
        }

        return md_DataIdend;
    }
    //TODO complete
    private Element getCI_Citation(Element ci_Citation) {


        return ci_Citation;
    }
}
