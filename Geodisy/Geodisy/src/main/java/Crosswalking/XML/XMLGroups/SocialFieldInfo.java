package Crosswalking.XML.XMLGroups;


import Crosswalking.XML.XMLTools.SubElement;
import Crosswalking.XML.XMLTools.XMLDocObject;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;

import static Strings.GeodisyStrings.CHARACTER;
import static Strings.XMLStrings.*;
import static Strings.DVFieldNameStrings.DATA_COLLECTOR;

public class SocialFieldInfo extends SubElement {
    SocialFields sf;
    String dataCollector;
    String doi;

    public SocialFieldInfo(DataverseJavaObject djo, XMLDocObject doc, Element root) {
        super(djo, doc, root);
        this.djo = djo;
        sf = djo.getSocialFields();
        dataCollector = sf.getField(DATA_COLLECTOR);
        doi = djo.getDOI();
    }

    @Override
    public Element getFields() {
        stack.push(root); //J
        stack.push(doc.createGMDElement(P_OF_CONTACT)); //K
        stack.push((doc.createGMDElement(CI_RESPONSIBILITY))); //L
        stack.push(doc.createGMDElement(PARTY)); //M
        stack.push(doc.createGMDElement(CI_ORG)); //N
        stack.push(doc.createGMDElement(NAME)); //O
        root = stack.zip(doc.addGCOVal(dataCollector,CHARACTER)); //P

        stack.push(root); //J
        stack.push(doc.createGMDElement(P_OF_CONTACT)); //K
        stack.push((doc.createGMDElement(CI_RESPONSIBILITY))); //L
        root = stack.zip(doc.addRoleCode("contributor"));
        return root;
    }


}
