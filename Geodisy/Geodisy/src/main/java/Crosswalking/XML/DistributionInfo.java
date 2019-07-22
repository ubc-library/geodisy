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

    public Element getDistribInfo() {
        Element temp;
        String tempString;
        Element root = doc.createGMDElement("distributionInfo");
        Element levelJ = doc.createGMDElement("MD_Distribution");
        Element levelK = doc.createGMDElement("distributor");
        Element levelL = doc.createGMDElement("MD_Distributor");
        Element levelM = doc.createGMDElement("distributorContact");
        Element levelN = doc.createGMDElement("CI_Responsibility");
        Element levelO = doc.createGMDElement("party");
        for(Distributor d:distributors) {
            Element levelP = doc.createGMDElement("CI_Organization");
            tempString = d.getDistributorName();
            if (!d.getDistributorName().isEmpty()) {
                temp = doc.createGMDElement("name");
                temp.appendChild(doc.addGCOVal(tempString, CHARACTER));
                levelP.appendChild(temp);
            }
            tempString = d.getDistributorAffiliation();
            if (!d.getDistributorName().isEmpty()) {
                temp = doc.createGMDElement("name");
                temp.appendChild(doc.addGCOVal(tempString, CHARACTER));
                levelP.appendChild(temp);
            }
            if (!d.getDistributorLogoURL().isEmpty() || !d.getDistributorURL().isEmpty()) {
                if (!d.getDistributorURL().isEmpty()) {
                    doc.stackElement(doc.createGMDElement("contactInfo"));
                    doc.stackElement(doc.createGMDElement("CI_Contact"));
                    doc.stackElement(doc.createGMDElement("onlineResource"));
                    doc.stackElement(doc.createGMDElement("CI_OnlineResource"));
                    doc.stackElement(doc.createGMDElement("linkage"));
                    doc.stackElement(doc.addGCOVal(d.getDistributorURL(), CHARACTER));
                    levelP.appendChild(doc.zip());
                }
                if (!d.getDistributorLogoURL().isEmpty()) {
                    doc.stackElement(doc.createGMDElement("logo"));
                    doc.stackElement(doc.createGMDElement("MD_BrowseGraphic"));
                    doc.stackElement(doc.createGMDElement("linkage"));
                    doc.stackElement(doc.addGCOVal(d.getDistributorLogoURL(), CHARACTER));
                    levelP.appendChild(doc.zip());
                }
            }
            levelO.appendChild(levelP);
        }
        levelN.appendChild(levelO);
        levelN.appendChild(levelRoleCode("distributor"));
        levelM.appendChild(levelN);
        if(simple.hasField(DEPOSITOR))
            levelM.appendChild(getDepositor(simple.getField(DEPOSITOR)));
        levelL.appendChild(levelM);
        levelK.appendChild(levelL);
        levelJ.appendChild(levelK);
        root.appendChild(levelJ);
        return root;
    }

    private Element getDepositor(String depositorName) {

        Element levelN = doc.createGMDElement("CI_Responsibility");
        Element levelO = doc.createGMDElement("party");
        Element levelP = doc.createGMDElement("CI_Organization");
        Element levelQ = doc.createGMDElement("name");
        levelQ.appendChild(doc.addGCOVal(depositorName,CHARACTER));
        levelP.appendChild(levelQ);
        levelO.appendChild(levelP);
        levelN.appendChild(levelO);
        levelN.appendChild(levelRoleCode("originator"));
        return levelN;
    }
}
