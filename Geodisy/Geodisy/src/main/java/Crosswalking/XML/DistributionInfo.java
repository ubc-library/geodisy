package Crosswalking.XML;

import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Distributor;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;
import java.util.LinkedList;


import static BaseFiles.GeodisyStrings.CHARACTER;
import static Crosswalking.XML.XMLStrings.*;
import static Dataverse.DVFieldNameStrings.DEPOSITOR;

public class DistributionInfo extends SubElement {
    SimpleCitationFields simple;
    LinkedList<Distributor> distributors;
    public DistributionInfo(DataverseJavaObject djo, XMLDocument doc, Element root, SimpleCitationFields simple, LinkedList<Distributor> distributors) {
        super(djo, doc, root);
        this.simple = simple;
        this.distributors = distributors;
    }
    @Override
    public Element getFields() {
        Element temp;
        String tempString;
        XMLStack outterStack = new XMLStack();
        Element levelI = doc.createGMDElement("distributionInfo");
        Element levelJ = doc.createGMDElement(MD_DIST);
        Element levelK = doc.createGMDElement("distributor");
        Element levelL = doc.createGMDElement(MD_DIST);
        outterStack.push(levelI);
        outterStack.push(levelJ);
        outterStack.push(levelK);
        outterStack.push(levelL);
        Element levelM = doc.createGMDElement("distributorContact");
        Element levelN = doc.createGMDElement(CI_RESPONSIBILITY);
        Element levelO = doc.createGMDElement(PARTY);
        for(Distributor d:distributors) {
            Element levelP = doc.createGMDElement(CI_ORG);
            tempString = d.getDistributorName();
            if (!d.getDistributorName().isEmpty()) {
                temp = doc.createGMDElement(NAME); //Q
                temp.appendChild(doc.addGCOVal(tempString, CHARACTER));
                levelP.appendChild(temp);
            }
            tempString = d.getDistributorAffiliation();
            if (!d.getDistributorName().isEmpty()) {
                temp = doc.createGMDElement(NAME); //Q
                temp.appendChild(doc.addGCOVal(tempString, CHARACTER));
                levelP.appendChild(temp);
            }
            if (!d.getDistributorLogoURL().isEmpty() || !d.getDistributorURL().isEmpty()) {
                stack = new XMLStack();
                if (!d.getDistributorURL().isEmpty()) {
                    stack.push(levelP);
                    stack = doc.createGMDElement("contactInfo", stack);
                    stack = doc.createGMDElement(CI_CITE, stack);
                    stack = doc.createGMDElement(ONLINE_RES, stack);
                    stack = doc.createGMDElement(CI_ONLINE_RES, stack);
                    stack = doc.createGMDElement(LINKAGE, stack);
                    levelP = stack.zip(doc.addGCOVal(d.getDistributorURL(), CHARACTER));
                }
                if (!d.getDistributorLogoURL().isEmpty()) {
                    stack.push(levelP);
                    stack = doc.createGMDElement("logo", stack);
                    stack = doc.createGMDElement("MD_BrowseGraphic", stack);
                    stack = doc.createGMDElement(LINKAGE, stack);
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
        stack = doc.createGMDElement(CI_RESPONSIBILITY, stack);
        stack = doc.createGMDElement(PARTY, stack);
        stack = doc.createGMDElement(CI_ORG, stack);
        stack = doc.createGMDElement(NAME, stack);
        Element levelN = stack.zip(doc.addGCOVal(depositorName,CHARACTER));
        levelN.appendChild(levelRoleCode("originator"));
        return levelN;
    }
}
