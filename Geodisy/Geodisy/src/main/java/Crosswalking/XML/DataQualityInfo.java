package Crosswalking.XML;

import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;

import java.util.LinkedList;

import static BaseFiles.GeodisyStrings.CHARACTER;
import static Dataverse.DVFieldNameStrings.*;
import static Dataverse.DVFieldNameStrings.ACCESS_TO_SOURCES;

public class DataQualityInfo extends SubElement {
    public DataQualityInfo(DataverseJavaObject djo, XMLDocument doc, Element root) {
        super(djo, doc, root);
    }

    public Element getFields(){
        SimpleCitationFields simple = djo.getSimpleFields();
        CitationFields cf = djo.getCitationFields();
        Element temp;
        Element levelI = doc.createGMDElement("dataQualityInfo");
        Element levelJ = doc.createGMDElement("DQ_DataQuality");
        Element levelK = doc.createGMDElement("scope");
        Element levelL = doc.createGMDElement("resourceLineage");
        Element levelM = doc.createGMDElement("LI_Lineage");
        LinkedList<String> dataSources = (LinkedList) cf.getListField(DATA_SOURCE);
        if(dataSources.size()>0) {
            for(String s: dataSources) {
                Element levelN =  doc.createGMDElement("statement");
                levelN.appendChild(doc.addGCOVal(s, CHARACTER));
                levelM.appendChild(levelN);
            }
        }
        if(!simple.getField(ORIG_OF_SOURCES).isEmpty()) {
            doc.stackElement(doc.createGMDElement("processStep"));
            doc.stackElement(doc.createGMDElement("LI_ProcessStep"));
            doc.stackElement(doc.createGMDElement("description"));
            doc.stackElement(doc.addGCOVal(simple.getField(ORIG_OF_SOURCES), CHARACTER));
            temp = doc.stack.zip();
            if (!temp.getTagName().equals("__no elements__"))
                levelM.appendChild(temp);
        }
        if(!simple.getField(CHAR_OF_SOURCES).isEmpty()) {
            doc.stackElement(doc.createGMDElement("source"));
            doc.stackElement(doc.createGMDElement("LI_Source"));
            doc.stackElement(doc.createGMDElement("description"));
            doc.stackElement(doc.addGCOVal(simple.getField(CHAR_OF_SOURCES), CHARACTER));
            temp = doc.stack.zip();
            if (!temp.getTagName().equals("__no elements__"))
                levelM.appendChild(temp);
        }
        if(!simple.getField(ACCESS_TO_SOURCES).isEmpty()){
            doc.stackElement(doc.createGMDElement("additionalDocumentation"));
            doc.stackElement(doc.createGMDElement("CI_Citation"));
            doc.stackElement(doc.createGMDElement("otherCitationDetails"));
            doc.stackElement(doc.addGCOVal(simple.getField(ACCESS_TO_SOURCES), CHARACTER));
            temp = doc.stack.zip();
            if (!temp.getTagName().equals("__no elements__"))
                levelM.appendChild(temp);
        }
        levelL.appendChild(levelM);
        levelK.appendChild(levelL);
        levelJ.appendChild(levelK);
        levelI.appendChild(levelJ);
        return levelI;
    }

}
