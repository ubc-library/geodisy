package Crosswalking.XML.XMLTools;


import Crosswalking.XML.XMLGroups.DataQualityInfo;
import Crosswalking.XML.XMLGroups.DistributionInfo;
import Crosswalking.XML.XMLGroups.IdentificationInfo;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.*;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;

import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import static BaseFiles.GeodisyStrings.CHARACTER;
import static BaseFiles.GeodisyStrings.XML_NS;
import static Crosswalking.XML.XMLTools.XMLStrings.*;
import static Dataverse.DVFieldNameStrings.*;

public class ISOXMLGen extends DjoXMLGenerator {
    private CitationFields citationFields;
    private SimpleCitationFields simple;

    XMLDocObject doc;

    public ISOXMLGen(DataverseJavaObject djo) {
        this.djo = djo;
        this.citationFields = djo.getCitationFields();
        this.simple = citationFields.getSimpleCitationFields();
        doc = new XMLDocObject();
        doc.setDoi(simple.getField(PERSISTENT_ID));
        doc.setPURL(simple.getField(RECORD_URL));
    }

    @Override
    public XMLDocObject generateXMLFile(){
        Element rootElement = getRoot();

        //DataIdentification Fields (most of the fields)
        SubElement ident = new IdentificationInfo(djo, doc, rootElement);
        rootElement = ident.getFields();

        //DataQuality Fields
        if(simple.hasField(ACCESS_TO_SOURCES)||simple.hasField(ORIG_OF_SOURCES)||simple.hasField(CHAR_OF_SOURCES)||citationFields.getListField(DATA_SOURCES).size()>0) {
            SubElement dqi = new DataQualityInfo(djo, doc, rootElement);
            rootElement.appendChild(dqi.getFields());
        }

        //Distributor Fields
        List distributors = citationFields.getListField(DISTRIB);
        if(distributors.size()>0||simple.hasField(DEPOSITOR)) {
            SubElement distribution = new DistributionInfo(djo,doc,rootElement,simple, distributors);
            rootElement.appendChild(distribution.getFields());
        }


        //TODO check the following line. I think dealing with other IDs happens elsewhere.
        //if(otherIds.size()>0)
        doc.addRoot(rootElement);
        return doc;
    }
    @Override
    protected Element getRoot() {
        // root element
        Element rootElement = doc.createGMDElement("MD_Metadata");
        rootElement.setAttribute(xmlNSElement(),XML_NS + "gmx");
        rootElement.setAttribute(xmlNSElement("gco"), XML_NS + "gco/1.0");
        rootElement.setAttribute(xmlNSElement("mdb"),XML_NS + "mdb/2.0");
        rootElement.setAttribute(xmlNSElement("srv"), XML_NS + "srv/2.0");
        rootElement.setAttribute(xmlNSElement("xlink"),"http://www.w3.org/1999/xlink");
        rootElement.setAttribute(xmlNSElement("xsi"), "http://www.w3.org/2001/XMLSchema-instance");
        return getMDMetadata(rootElement);

    }

    private Element getMDMetadata(Element rootElement) {
        Element levelI = doc.createGMDElement("metadataIdentifier");
        Element levelJ = doc.createGMDElement("MD_Identifier");
        Element levelK = doc.createGMDElement("codeSpace");
        UUID uuid = getUUID(djo.getDOI());
        Element levelL = doc.addGCOVal(uuid.toString(),CHARACTER);
        levelK.appendChild(levelL);
        levelJ.appendChild(levelK);
        levelI.appendChild(levelJ);
        rootElement.appendChild(levelI);
        rootElement = getParentMetadata(rootElement);
        return rootElement;
    }

    //getUUID also in DataverseRecordFile, so change there if changed here
    private UUID getUUID(String name) {
        byte[] bytes = name.getBytes(Charset.forName("UTF-8"));
        return UUID.nameUUIDFromBytes(bytes);

    }

    //TODO create the second part of the Geodisy metadata for ISO
    private Element getParentMetadata(Element mdMetadata) {
        Element levelI = doc.createGMDElement("parentMetadata");
        Element levelJ = doc.createGMDElement(CI_CITE);
        XMLStack stack = new XMLStack();
        stack.push(levelJ);
        stack.push(doc.createGMDElement(TITLE)); //Level K
        levelJ = stack.zip(doc.addGCOVal(djo.getSimpleFieldVal(TITLE) + ":" + djo.getSimpleFieldVal(SUBTITLE),CHARACTER)); //Level L
        stack.push(levelJ);
        stack.push(doc.createGMDElement(IDENTIFIER)); //Level K
        Element levelL = doc.createGMDElement(MD_IDENT);
        XMLStack innerStack = new XMLStack();
        innerStack.push(levelL);
        innerStack.push(doc.createGMDElement(CODE));
        levelL = innerStack.zip(doc.addGCOVal(djo.getDOI(),CHARACTER));
        innerStack.push(levelL);
        innerStack.push(doc.createGMDElement(CODE));
        levelL = innerStack.zip(doc.addGCOVal(djo.getDOIProtocal(),CHARACTER));
        levelJ = stack.zip(levelL);
        stack.push(levelJ);
        stack.push(doc.createGMDElement(ONLINE_RES)); //Level K
        stack.push(doc.createGMDElement(CI_ONLINE_RES)); //Level L
        stack.push(doc.createGMDElement(LINKAGE)); //Level M
        levelJ = stack.zip(doc.addGCOVal(djo.getSimpleFieldVal(RECORD_URL),CHARACTER)); //Level N
        stack.push(levelJ);
        stack.push(doc.createGMDElement("citedResponsibilityParty"));

        levelL = doc.createGMDElement(CI_RESPONSIBILITY);
        innerStack.push(levelL);
        innerStack.push(doc.createGMDElement(PARTY));
        innerStack.push(doc.createGMDElement(CI_ORG));
        innerStack.push(doc.createGMDElement(NAME));
        levelL = innerStack.zip(doc.addGCOVal(djo.getSimpleFieldVal(PUBLISHER),CHARACTER));
        stack.push(levelL);
        stack.push(doc.createGMDElement("role"));
        levelJ = stack.zip(doc.addRoleCode(PUBLISHER));
        levelI.appendChild(levelJ);
        mdMetadata.appendChild(levelI);
        return mdMetadata;
    }

    private String xmlNSElement() {
        return "xmlns";
    }

    private String xmlNSElement(String s) {
        return "xmlns:" + s;
    }
}
