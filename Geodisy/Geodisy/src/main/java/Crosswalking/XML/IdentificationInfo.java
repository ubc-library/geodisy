package Crosswalking.XML;

import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.*;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


import java.util.LinkedList;

import static BaseFiles.GeodisyStrings.CHARACTER;
import static Dataverse.DVFieldNameStrings.*;
import static Dataverse.DVFieldNameStrings.SOFTWARE;

public class IdentificationInfo extends SubElement{
    LinkedList<OtherID> otherIds;
    LinkedList<Author> authors;
    LinkedList<DatasetContact> datasetContacts;
    LinkedList<Description> descriptions;
    LinkedList<Keyword> keywords;
    LinkedList<TopicClassification> topicClassifications;
    LinkedList<RelatedPublication> relatedPublications;
    //NOTESTEXT
    //LANGUAGE
    LinkedList<Producer> producers;
    LinkedList<Contributor> contributors;
    LinkedList<GrantNumber> grantNumbers;
    //DistributionDate
    LinkedList<TimePeriodCovered> timePeriodCovereds;
    LinkedList<DateOfCollection> datesOfCollection;
    LinkedList<Series> series;
    LinkedList<Software> software;
    Element root;
    DataverseJavaObject djo;
    CitationFields cf;
    XMLDocument doc;
    SimpleCitationFields scf;
    GeographicFields gf;
    SocialFields sf;
    String noteText, language, distDate;

    public IdentificationInfo(DataverseJavaObject djo, XMLDocument doc, Element root) {
        super(djo,doc,root);
        cf = djo.getCitationFields();
        scf = djo.getSimpleFields();
        gf = djo.getGeoFields();
        sf = djo.getSocialFields();

        otherIds= (LinkedList) cf.getListField(OTHER_ID);
        authors = (LinkedList) cf.getListField(AUTHOR);
        datasetContacts = (LinkedList) cf.getListField(DS_CONTACT);
        descriptions = (LinkedList) cf.getListField(DS_DESCRIPT);
        keywords = (LinkedList) cf.getListField(KEYWORD);
        topicClassifications = (LinkedList) cf.getListField(TOPIC_CLASS);
        relatedPublications = (LinkedList) cf.getListField(PUBLICATION);
        noteText = scf.getField(NOTES_TEXT);
        language = scf.getField(LANGUAGE);
        producers = (LinkedList) cf.getListField(PRODUCER);
        contributors = (LinkedList) cf.getListField(CONTRIB);
        grantNumbers = (LinkedList) cf.getListField(GRANT_NUM);
        distDate = scf.getField(DIST_DATE);
        timePeriodCovereds = (LinkedList) cf.getListField(TIME_PER_COV);
        datesOfCollection = (LinkedList) cf.getListField(DATE_OF_COLLECT);
        series = (LinkedList) cf.getListField(SERIES);
        software = (LinkedList) cf.getListField(SOFTWARE);
    }

    public Element generateIdentInfo() {
        Element levelK = doc.createGMDElement("identificationInfo");
        Element levelL = doc.createGMDElement("DataIdentification");
        Element levelM = doc.createGMDElement("citation");
        levelM.appendChild(getCitation());
        levelL.appendChild(levelM);
        if(djo.getCitationFields().getOtherIDs().size()>0)
            levelL.appendChild(getOtherIds());
        




        //TODO finish fleshing out
        levelK.appendChild(levelL);
        root.appendChild(levelK);
        return root;
    }

    private Element getPointOfContact() {
        LinkedList<DatasetContact> datasetContacts = (LinkedList) cf.getListField(DS_CONTACT);
        Element levelK = doc.createGMDElement("pointOfContact");
        Element levelL = doc.create_Element("CI_Responsibility");
        Element levelM = doc.createGMDElement("party");
        for(DatasetContact dc: datasetContacts) {
            String dsContactName = dc.getDatasetContactName();
            String dsContactEmail = dc.getDatasetContactEmail();
            if(!dsContactName.isEmpty()||!dsContactEmail.isEmpty()) {
                Element levelN = doc.createGMDElement("CI_Individual");
                if(!dsContactName.isEmpty()){
                    Element levelO = doc.createGMDElement("name");
                    levelO.appendChild(doc.addGCOVal(dsContactName,CHARACTER));
                    levelN.appendChild(levelO);
                }
                if(!dsContactEmail.isEmpty()){
                    Element levelO = doc.createGMDElement("contactInfo");
                    Element levelP = doc.createGMDElement("CI_Contact");
                    Element levelQ = doc.createGMDElement("address");
                    Element levelR = doc.createGMDElement("CI_Address");
                    Element levelS = doc.createGMDElement("electronicMailAddress");
                    levelS.appendChild(doc.addGCOVal(dsContactEmail,CHARACTER));
                    levelR.appendChild(levelS);
                    levelQ.appendChild(levelR);
                    levelP.appendChild(levelQ);
                    levelO.appendChild(levelP);
                    levelN.appendChild(levelO);
                }
                levelM.appendChild(levelN);
            }
            String dsContactAffil = dc.getDatasetContactAffiliation();
            String dsPublisher = djo.getSimpleFieldVal(PUBLISHER);
            if(!dsContactAffil.isEmpty()||!dsPublisher.isEmpty()){
                Element levelN = doc.createGMDElement("CI_Organisation");
                Element levelO;
                if(!dsContactAffil.isEmpty()){
                    levelO = doc.createGMDElement("name");
                    levelO.appendChild(doc.addGCOVal(dsContactAffil,CHARACTER));
                    levelN.appendChild(levelO);
                }
                if(!dsPublisher.isEmpty()){
                    levelO = doc.createGMDElement("name");
                    levelO.appendChild(doc.addGCOVal(dsPublisher,CHARACTER));
                    levelN.appendChild(levelO);
                }
                levelM.appendChild(levelN);
            }

        }
        levelM.appendChild(levelRoleCode("pointOfContact"));
        levelL.appendChild(levelM);
        levelK.appendChild(levelL);
        return levelK;
    }

    private Element getCitation() {
        Element levelL = doc.createGMDElement("CI_Citation");
        String subtitleVal = sf.getField(SUBTITLE);
        String title = sf.getField(TITLE);
        //Title
        if(!subtitleVal.isEmpty())
            title += ":" + subtitleVal;
        levelL = setValChild(levelL,"title",title,CHARACTER);
        String altTitleVal = sf.getField(ALT_TITLE);
        //Alt Title
        if(!altTitleVal.isEmpty())
            levelL = setValChild(levelL,"alternateTitle", subtitleVal, CHARACTER);
        String altUrlVal = sf.getField(ALT_URL);
        //Alt URL
        if(!altUrlVal.isEmpty()) {
            Element altURL = doc.createGMDElement("onlineResource");
            altURL = getOnlineResource(altURL, altUrlVal);
            levelL.appendChild(altURL);
        }
        //OTHERIDs
        if(!otherIds.isEmpty())
            levelL.appendChild(getOtherIds());
        //AUTHORs
        if(!authors.isEmpty())
            levelL.appendChild(getAuthor());

        return levelL;
    }

    private Element getAuthor() {
        Element levelM = doc.createGMDElement("citeResponsibleParty");
        Element levelN = doc.createGMDElement("CI_Responsibility");
        for(Author a: authors){
            Element levelO = doc.createGMDElement("party");
            Element levelP =  doc.createGMDElement("CI_Individual");
            Element levelQ;
            Element levelR;
            Element levelS;
            if(!a.getField(AUTHOR_NAME).isEmpty()) {
                levelQ = doc.createGMDElement("name");
                levelQ.appendChild(doc.addGCOVal(a.getField(AUTHOR_NAME), CHARACTER));
                levelP.appendChild(levelQ);
            }
            String authorIDScheme = a.getField(AUTHOR_ID_SCHEME);
            String authorID = a.getField(AUTHOR_ID);
            if(!authorIDScheme.isEmpty()||!authorID.isEmpty()) {
                levelQ = doc.createGMDElement("partyIdentifier");
                levelR = doc.createGMDElement("MD_Identifier");
                if(!authorIDScheme.isEmpty()) {
                    levelS = doc.createGMDElement("codeSpace");
                    levelS.appendChild(doc.addGCOVal(authorIDScheme, CHARACTER));
                    levelR.appendChild(levelS);
                }
                if(!authorID.isEmpty()){
                    levelS = doc.createGMDElement("code");
                    levelS.appendChild(doc.addGCOVal(authorID,CHARACTER));
                    levelR.appendChild(levelS);
                }
                levelQ.appendChild(levelR);
                levelP.appendChild(levelQ);
            }
            levelO.appendChild(levelP);
            if(!a.getField(AUTHOR_AFFIL).isEmpty()) {
                levelP = doc.createGMDElement("CI_Organisation");
                levelQ = doc.createGMDElement("name");
                levelQ.appendChild(doc.addGCOVal(a.getField(AUTHOR_AFFIL), CHARACTER));
                levelP.appendChild(levelQ);
                levelO.appendChild(levelP);
            }
        }
        levelN.appendChild(levelRoleCode("author"));
        levelM.appendChild(levelN);
        return levelM;
    }


    private Element getOnlineResource(Element onlineResouce, String altUrlVal) {
        Element ciOnline = doc.createGMDElement("CI_OnlineResource");
        Element linkage = doc.createGMDElement("linkage");
        Element url = doc.createGMDElement("URL");
        url.setNodeValue(altUrlVal);
        linkage.appendChild(url);
        ciOnline.appendChild(linkage);
        onlineResouce.appendChild(ciOnline);
        return onlineResouce;
    }

    //TODO
    private Element getOtherIds() {
        Element levelM = doc.createGMDElement("identifier");
        Element levelN =  doc.createGMDElement("MD_Identifier");
        Element levelO = doc.createGMDElement("authority");
        Element levelP = doc.createGMDElement("CI_Citation");
        Element levelQ = doc.createGMDElement("citedResponsibility");
        Element levelR = doc.createGMDElement("CI_Responsibility");
        LinkedList<OtherID> otherIDS = (LinkedList) djo.getCitationFields().getOtherIDs();
        for(OtherID otherID: otherIDS) {
            Element levelS = doc.createGMDElement("party");
            Element levelT = doc.createGMDElement("CI_Organisation");
            if (!otherID.getOtherIdAgency().isEmpty()) {
                levelR = doc.createGMDElement("CI_Responsibility");
                levelS = doc.createGMDElement("party");
                levelT = doc.createGMDElement("CI_Organisation");
                Element levelU = doc.createGMDElement("name");
                levelU.appendChild(doc.addGCOVal(otherID.getOtherIdAgency(), CHARACTER));
                levelT.appendChild(levelU);
                if (!otherID.getOtherIdValue().isEmpty()) {
                    levelU = doc.createGMDElement("partyIdentifier");
                    Element levelV = doc.createGMDElement("MD_Identifier");
                    Element levelW = doc.createGMDElement("code");
                    levelW.appendChild(doc.addGCOVal(otherID.getOtherIdValue(), CHARACTER));
                    levelV.appendChild(levelW);
                    levelU.appendChild(levelV);
                    levelT.appendChild(levelU);
                }
            } else if (!otherID.getOtherIdValue().isEmpty()) {
                Element levelU = doc.createGMDElement("partyIdentifier");
                Element levelV = doc.createGMDElement("MD_Identifier");
                Element levelW = doc.createGMDElement("code");
                levelW.appendChild(doc.addGCOVal(otherID.getOtherIdValue(), CHARACTER));
                levelV.appendChild(levelW);
                levelU.appendChild(levelV);
                levelT.appendChild(levelU);
            }
            levelS.appendChild(levelT);
            levelR.appendChild(levelS);
        }
        levelR.appendChild(levelRoleCode("resourceProvider"));
        levelQ.appendChild(levelR);
        levelP.appendChild(levelQ);
        levelO.appendChild(levelP);
        levelN.appendChild(levelO);
        levelM.appendChild(levelN);
        return levelM;
    }

    private boolean empty() {
        boolean simple = noteText.isEmpty() && language.isEmpty() && distDate.isEmpty();
        boolean complex = otherIds.isEmpty() && authors.isEmpty() && datasetContacts.isEmpty() && descriptions.isEmpty() && keywords.isEmpty() && topicClassifications.isEmpty() && relatedPublications.isEmpty() && producers.isEmpty() && contributors.isEmpty() && grantNumbers.isEmpty() && timePeriodCovereds.isEmpty() && datesOfCollection.isEmpty() && series.isEmpty() && software.isEmpty();
        return simple && complex;
    }
}
