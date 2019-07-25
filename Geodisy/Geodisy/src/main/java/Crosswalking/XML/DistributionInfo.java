package Crosswalking.XML;

import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Distributor;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;
import java.util.LinkedList;


import static BaseFiles.GeodisyStrings.CHARACTER;
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
        Element levelJ = doc.createGMDElement("MD_Distribution");
        Element levelK = doc.createGMDElement("distributor");
        Element levelL = doc.createGMDElement("MD_Distributor");
        outterStack.push(levelI);
        outterStack.push(levelJ);
        outterStack.push(levelK);
        outterStack.push(levelL);
        Element levelM = doc.createGMDElement("distributorContact");
        Element levelN = doc.createGMDElement("CI_Responsibility");
        Element levelO = doc.createGMDElement("party");
        for(Distributor d:distributors) {
            Element levelP = doc.createGMDElement("CI_Organization");
            tempString = d.getDistributorName();
            if (!d.getDistributorName().isEmpty()) {
                temp = doc.createGMDElement("name"); //Q
                temp.appendChild(doc.addGCOVal(tempString, CHARACTER));
                levelP.appendChild(temp);
            }
            tempString = d.getDistributorAffiliation();
            if (!d.getDistributorName().isEmpty()) {
                temp = doc.createGMDElement("name"); //Q
                temp.appendChild(doc.addGCOVal(tempString, CHARACTER));
                levelP.appendChild(temp);
            }
            if (!d.getDistributorLogoURL().isEmpty() || !d.getDistributorURL().isEmpty()) {
                stack = new XMLStack();
                if (!d.getDistributorURL().isEmpty()) {
                    stack.push(levelP);
                    stack = doc.createGMDElement("contactInfo", stack);
                    stack = doc.createGMDElement("CI_Contact", stack);
                    stack = doc.createGMDElement("onlineResource", stack);
                    stack = doc.createGMDElement("CI_OnlineResource", stack);
                    stack = doc.createGMDElement("linkage", stack);
                    levelP = stack.zip(doc.addGCOVal(d.getDistributorURL(), CHARACTER));
                }
                if (!d.getDistributorLogoURL().isEmpty()) {
                    stack.push(levelP);
                    stack = doc.createGMDElement("logo", stack);
                    stack = doc.createGMDElement("MD_BrowseGraphic", stack);
                    stack = doc.createGMDElement("linkage", stack);
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
        stack = doc.createGMDElement("CI_Responsibility", stack);
        stack = doc.createGMDElement("party", stack);
        stack = doc.createGMDElement("CI_Organization", stack);
        stack = doc.createGMDElement("name", stack);
        Element levelN = stack.zip(doc.addGCOVal(depositorName,CHARACTER));
        levelN.appendChild(levelRoleCode("originator"));
        return levelN;
    }
}
