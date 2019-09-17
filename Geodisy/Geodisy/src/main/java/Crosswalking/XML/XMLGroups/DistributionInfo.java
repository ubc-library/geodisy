package Crosswalking.XML.XMLGroups;

import Crosswalking.XML.XMLTools.SubElement;
import Crosswalking.XML.XMLTools.XMLDocObject;
import Crosswalking.XML.XMLTools.XMLStack;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Distributor;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;

import java.util.List;


import static BaseFiles.GeodisyStrings.CHARACTER;
import static Crosswalking.XML.XMLTools.XMLStrings.*;
import static Dataverse.DVFieldNameStrings.DEPOSITOR;

public class DistributionInfo extends SubElement {
    SimpleCitationFields simple;
    List<Distributor> distributors;
    public DistributionInfo(DataverseJavaObject djo, XMLDocObject doc, Element root, SimpleCitationFields simple, List<Distributor> distributors) {
        super(djo, doc, root);
        this.simple = simple;
        this.distributors = distributors;
    }
    @Override
    public Element getFields() {
        Element temp;
        String tempString;
        XMLStack outterStack = new XMLStack();
        Element levelI = doc.createMDBElement("distributionInfo");
        Element levelJ = doc.createMDBElement(MD_DIST);
        Element levelK = doc.createMDBElement("distributor");
        Element levelL = doc.createMDBElement(MD_DIST);
        outterStack.push(levelI);
        outterStack.push(levelJ);
        outterStack.push(levelK);
        outterStack.push(levelL);
        Element levelM = doc.createMDBElement("distributorContact");
        Element levelN = doc.createMDBElement(CI_RESPONSIBILITY);
        Element levelO = doc.createMDBElement(PARTY);
        for(Distributor d:distributors) {
            Element levelP = doc.createMDBElement(CI_ORG);
            tempString = d.getDistributorName();
            if (!d.getDistributorName().isEmpty()) {
                temp = doc.createMDBElement(NAME); //Q
                temp.appendChild(doc.addGCOVal(tempString, CHARACTER));
                levelP.appendChild(temp);
            }
            tempString = d.getDistributorAffiliation();
            if (!d.getDistributorName().isEmpty()) {
                temp = doc.createMDBElement(NAME); //Q
                temp.appendChild(doc.addGCOVal(tempString, CHARACTER));
                levelP.appendChild(temp);
            }
            if (!d.getDistributorLogoURL().isEmpty() || !d.getDistributorURL().isEmpty()) {
                stack = new XMLStack();
                if (!d.getDistributorURL().isEmpty()) {
                    stack.push(levelP);
                    stack.push(doc.createMDBElement("contactInfo")); //Q
                    stack.push(doc.createMDBElement(CI_CITE)); //R
                    stack.push(doc.createMDBElement(ONLINE_RES)); //S
                    stack.push(doc.createMDBElement(CI_ONLINE_RES)); //T
                    stack.push(doc.createMDBElement(LINKAGE)); //U
                    levelP = stack.zip(doc.addGCOVal(d.getDistributorURL(), CHARACTER));
                }
                if (!d.getDistributorLogoURL().isEmpty()) {
                    stack.push(levelP);
                    stack.push(doc.createMDBElement("logo")); //Q
                    stack.push(doc.createMDBElement("MD_BrowseGraphic")); //R
                    stack.push(doc.createMDBElement(LINKAGE)); //S
                    stack.push(doc.createMDBElement(CI_ONLINE_RES)); //T
                    stack.push(doc.createMDBElement(LINKAGE)); // U
                    levelP = stack.zip(doc.addGCOVal(d.getDistributorLogoURL(), CHARACTER));
                }
            }
            levelO.appendChild(levelP);
        }
        levelN.appendChild(levelO);
        levelN.appendChild(levelRoleCode("distributor"));
        levelM.appendChild(levelN);
        if(simple.hasField(DEPOSITOR))
            levelM.appendChild(getDepositor(simple.getField(DEPOSITOR)));
        levelI = outterStack.zip(levelM);
        return levelI;
    }

    private Element getDepositor(String depositorName) {
        stack = new XMLStack();
        stack.push(doc.createMDBElement(CI_RESPONSIBILITY));
        stack.push(doc.createMDBElement(PARTY));
        stack.push(doc.createMDBElement(CI_ORG));
        stack.push(doc.createMDBElement(NAME));
        Element levelN = stack.zip(doc.addGCOVal(depositorName,CHARACTER));
        levelN.appendChild(levelRoleCode("originator"));
        return levelN;
    }
}
