package Crosswalking.XML.XMLGroups;

import Crosswalking.XML.XMLTools.SubElement;
import Crosswalking.XML.XMLTools.XMLDocObject;
import Crosswalking.XML.XMLTools.XMLStack;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;

import java.util.LinkedList;

import static BaseFiles.GeodisyStrings.CHARACTER;
import static Crosswalking.XML.XMLTools.XMLStrings.*;
import static Dataverse.DVFieldNameStrings.*;
import static Dataverse.DVFieldNameStrings.ACCESS_TO_SOURCES;

public class DataQualityInfo extends SubElement {
    public DataQualityInfo(DataverseJavaObject djo, XMLDocObject doc, Element root) {
        super(djo, doc, root);
    }

    public Element getFields(){
        SimpleCitationFields simple = djo.getSimpleFields();
        CitationFields cf = djo.getCitationFields();
        Element levelI = doc.createMDBElement("dataQualityInfo");
        Element levelJ = doc.createMDBElement("DQ_DataQuality");
        Element levelK = doc.createMDBElement("scope");
        Element levelL = doc.createMDBElement("resourceLineage");
        Element levelM = doc.createMDBElement("LI_Lineage");
        LinkedList<String> dataSources = (LinkedList) cf.getListField(DATA_SOURCES);
        if(dataSources.size()>0) {
            for(String s: dataSources) {
                Element levelN =  doc.createMDBElement("statement");
                levelN.appendChild(doc.addGCOVal(s, CHARACTER));
                levelM.appendChild(levelN);
            }
            levelL.appendChild(levelM);
        }
        if(!simple.getField(ORIG_OF_SOURCES).isEmpty()) {
            stack = new XMLStack();
            stack.push(levelM);
            stack.push(doc.createMDBElement("processStep")); //N
            stack.push(doc.createMDBElement("LI_ProcessStep")); //O
            stack.push(doc.createMDBElement(DESCRIP)); //P
            levelM = stack.zip(doc.addGCOVal(simple.getField(ORIG_OF_SOURCES), CHARACTER));
            levelL.appendChild(levelM);
        }
        if(!simple.getField(CHAR_OF_SOURCES).isEmpty()) {
            stack = new XMLStack();
            stack.push(levelM);
            stack.push(doc.createMDBElement("source")); //N
            stack.push(doc.createMDBElement("LI_Source")); //O
            stack.push(doc.createMDBElement(DESCRIP)); //P
            levelM = stack.zip(doc.addGCOVal(simple.getField(CHAR_OF_SOURCES), CHARACTER));
            levelL.appendChild(levelM);
        }
        if(!simple.getField(ACCESS_TO_SOURCES).isEmpty()){
            stack = new XMLStack();
            stack.push(levelM);
            stack.push(doc.createMDBElement(ADDITIONAL_DOCS));
            stack.push(doc.createMDBElement(CI_CITE));
            stack.push(doc.createMDBElement(OTHER_CITE_DEETS));
            levelM = stack.zip(doc.addGCOVal(simple.getField(ACCESS_TO_SOURCES), CHARACTER));
            levelL.appendChild(levelM);
        }
        levelK.appendChild(levelL);
        levelJ.appendChild(levelK);
        levelI.appendChild(levelJ);
        return levelI;
    }

}
