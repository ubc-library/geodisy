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
        Element levelI = doc.createGMDElement("distributionInfo");
        Element levelJ = doc.createGMDElement(MD_DIST);
        Element levelK = doc.createGMDElement("distributor");
        Element levelL = doc.createGMDElement(MD_DISTRIBUTOR);
        outterStack.push(levelI);
        outterStack.push(levelJ);
        outterStack.push(levelK);
        outterStack.push(levelL);
        Element levelM = doc.createGMDElement("distributorContact");
        if(distributors.size()>0){
            Element levelN = doc.createGMDElement(CI_RESPONSIBILITY);
            Element levelO = doc.createGMDElement(PARTY);
            for (Distributor d : distributors) {
                Element levelP = doc.createGMDElement(CI_ORG);
                tempString = d.getDistributorName();
                if (!d.getDistributorName().isEmpty()) {
                    temp = doc.createGMDElement(NAME); //Q
                    if(d.getDistributorAffiliation().isEmpty())
                        tempString+= ", " + d.getDistributorAffiliation();
                    temp.appendChild(doc.addGCOVal(tempString, CHARACTER));
                    levelP.appendChild(temp);
                }else {
                    tempString = d.getDistributorAffiliation();
                    if (!tempString.isEmpty()) {
                        temp = doc.createGMDElement(NAME); //Q
                        temp.appendChild(doc.addGCOVal(tempString, CHARACTER));
                        levelP.appendChild(temp);
                    }
                }
                if (!d.getDistributorLogoURL().isEmpty() || !d.getDistributorURL().isEmpty()) {
                    stack = new XMLStack();
                    if (!d.getDistributorURL().isEmpty()) {
                        stack.push(levelP);
                        stack.push(doc.createGMDElement("contactInfo")); //Q
                        stack.push(doc.createGMDElement(CI_CITE)); //R
                        stack.push(doc.createGMDElement(ONLINE_RES)); //S
                        stack.push(doc.createGMDElement(CI_ONLINE_RES)); //T
                        stack.push(doc.createGMDElement(LINKAGE)); //U
                        levelP = stack.zip(doc.addGCOVal(d.getDistributorURL(), CHARACTER));
                    }
                    if (!d.getDistributorLogoURL().isEmpty()) {
                        stack.push(levelP);
                        stack.push(doc.createGMDElement("logo")); //Q
                        stack.push(doc.createGMDElement("MD_BrowseGraphic")); //R
                        stack.push(doc.createGMDElement(LINKAGE)); //S
                        stack.push(doc.createGMDElement(CI_ONLINE_RES)); //T
                        stack.push(doc.createGMDElement(LINKAGE)); // U
                        levelP = stack.zip(doc.addGCOVal(d.getDistributorLogoURL(), CHARACTER));
                    }
                }
                levelO.appendChild(levelP);
            }
            levelN.appendChild(levelO);

            levelN.appendChild(levelRoleCode("distributor"));
            levelM.appendChild(levelN);
        }
        if(simple.hasField(DEPOSITOR))
            levelM.appendChild(getDepositor(simple.getField(DEPOSITOR)));
        levelI = outterStack.zip(levelM);
        return levelI;
    }

    private Element getDepositor(String depositorName) {
        stack = new XMLStack();
        stack.push(doc.createGMDElement(CI_RESPONSIBILITY));
        stack.push(doc.createGMDElement(PARTY));
        stack.push(doc.createGMDElement(CI_ORG));
        stack.push(doc.createGMDElement(NAME));
        Element levelN = stack.zip(doc.addGCOVal(depositorName,CHARACTER));
        levelN.appendChild(levelRoleCode("originator"));
        return levelN;
    }
}
