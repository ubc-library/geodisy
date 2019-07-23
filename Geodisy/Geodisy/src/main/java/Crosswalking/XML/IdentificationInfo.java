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
    LinkedList<String> subjects;
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
        subjects = (LinkedList) cf.getListField(SUBJECT);
    }
    @Override
    public Element getFields() {
        Element levelI = doc.createGMDElement("identificationInfo");
        Element levelJ = doc.createGMDElement("MD_DataIdentification");

        levelJ.appendChild(getCitation());
        if(descriptions.size()>0){
            levelJ = getDescriptions(levelJ);
        }
        if(subjects.size()>0 || keywords.size()>0)
            levelJ = getKeywords(levelJ);
        if(datasetContacts.size()>0)
            levelJ.appendChild(getPointOfContact());

        




        //TODO finish fleshing out
        levelI.appendChild(levelJ);
        root.appendChild(levelI);
        return root;
    }

    private Element getKeywords(Element levelJ) {
        Element levelK = doc.createGMDElement("descriptiveKeywords");
        Element levelL = doc.createGMDElement("MD_Keywords");
        Element levelM;
        Element levelN;
        Element levelO;
        for(String s:subjects){
            levelM = doc.createGMDElement("keyword");
            levelM.appendChild(doc.addGCOVal(s,CHARACTER));
            levelL.appendChild(levelM);
        }
        for(Keyword k: keywords){
            if(!k.getKeywordValue().isEmpty()){
                levelM = doc.createGMDElement("keyword");
                levelM.appendChild(doc.addGCOVal(k.getKeywordValue(),CHARACTER));
                levelL.appendChild(levelM);
            }
            if(!k.getKeywordVocabulary().isEmpty()){
                levelM = doc.createGMDElement("thesaurusName");
                levelN = doc.createGMDElement("CI_Citation");
                levelO = doc.createGMDElement("title");
                levelO.appendChild(doc.addGCOVal(k.getKeywordVocabulary(),CHARACTER));
                levelN.appendChild(levelO);
                if(!k.getKeywordVocabularyURL().isEmpty()){
                    levelO = doc.createGMDElement("onlineResource");
                    levelO.appendChild(doc.addGCOVal(k.getKeywordVocabularyURL(),CHARACTER));
                    levelN.appendChild(levelO);
                }
                levelM.appendChild(levelN);
                levelL.appendChild(levelM);
            }
        }
        levelM = doc.createGMDElement("MD_KeywordTypeCode");
        levelN = doc.createGMDElement("type");
        levelN.appendChild(doc.addGMDVal("theme", "MD_KeywordTypeCode"));
        levelM.appendChild(levelN);
        levelL.appendChild(levelM);
        levelK.appendChild(levelL);
        return levelK;
    }

    private Element getDescriptions(Element levelJ) {
        for(Description d: descriptions) {
            Element levelK = doc.createGMDElement("abstract");
            levelK.appendChild(doc.addGCOVal(d.getDsDescriptionValue(),CHARACTER));
            levelJ.appendChild(levelK);
        }
        return levelJ;
    }

    private Element getPointOfContact() {
        LinkedList<DatasetContact> datasetContacts = (LinkedList) cf.getListField(DS_CONTACT);
        Element levelK = doc.createGMDElement("pointOfContact");
        Element levelL = doc.create_Element("CI_Responsibility");
        Element levelM = doc.createGMDElement("party");
        XMLStack innerStack = new XMLStack(doc);
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
                    innerStack.push(levelN);
                    innerStack = doc.createGMDElement("contactInfo", innerStack);
                    innerStack = doc.createGMDElement("CI_Contact", innerStack);
                    innerStack = doc.createGMDElement("address", innerStack);
                    innerStack = doc.createGMDElement("CI_Address", innerStack);
                    innerStack = doc.createGMDElement("electronicMailAddress", innerStack);
                    levelN = innerStack.zip(doc.addGCOVal(dsContactEmail,CHARACTER));

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
        levelL.appendChild(getProducer());
        levelK.appendChild(levelL);
        return levelK;
    }

    private Element getProducer() {
        Element levelM = doc.createGMDElement("party");
        for(Producer p: producers){
            Element levelN;
            XMLStack stack = new XMLStack(doc);
            if(!p.getProducerName().isEmpty()) {
                stack.push(levelM);
                stack = doc.createGMDElement("CI_Individual", stack);
                stack = doc.createGMDElement("name", stack);
                levelM = stack.zip(doc.addGCOVal(p.getProducerName(), CHARACTER));
            }
            if(!p.getProducerAffiliation().isEmpty()||!p.getProducerURL().isEmpty()||!p.getProducerLogoURL().isEmpty()) {
                levelN = doc.createGMDElement("CI_Organisation");
                if (!p.getProducerAffiliation().isEmpty()) {
                    stack.push(levelN);
                    stack = doc.createGMDElement("name", stack);
                    levelN = stack.zip(doc.addGCOVal(p.getProducerAffiliation(), CHARACTER));
                }
                if (!p.getProducerURL().isEmpty()) {
                    stack.push(levelN);
                    stack = doc.createGMDElement("contactInfo", stack);
                    stack = doc.createGMDElement("CI_Contact", stack);
                    stack = doc.createGMDElement("onlineResource", stack);
                    stack = doc.createGMDElement("CI_OnlineResource", stack);
                    stack = doc.createGMDElement("linkage", stack);
                    levelN = stack.zip(doc.addGCOVal(p.getProducerAbbreviation(), CHARACTER));
                }
                if (!p.getProducerLogoURL().isEmpty()) {
                    stack.push(levelN);
                    stack = doc.createGMDElement("logo",stack);
                    stack = doc.createGMDElement("MD_BrowseGraphic",stack);
                    stack = doc.createGMDElement("linkage", stack);
                    levelN = stack.zip(doc.addGCOVal(p.getProducerAbbreviation(), CHARACTER));
                }
                levelM.appendChild(levelN);
            }
        }
        levelM.appendChild(levelRoleCode("custodian"));
        return levelM;
    }

    private Element getCitation() {
        Element levelK = doc.createGMDElement("citation");
        Element levelL = doc.createGMDElement("CI_Citation");
        String subtitleVal = sf.getField(SUBTITLE);
        levelL.appendChild(getSystemGeneratedFields());
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
        if(otherIds.size()>0)
            levelL.appendChild(getOtherIds());
        //AUTHORs
        if(authors.size()>0)
            levelL.appendChild(getAuthor());
        levelK.appendChild(levelL);
        return levelK;
    }

    private Element getSystemGeneratedFields() {
        //TODO get Dataset Publisher, Publication Date, Version, Version Date

        return null;
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
        XMLStack outerStack = new XMLStack(doc);
        Element levelM = doc.createGMDElement("identifier");
        Element levelN =  doc.createGMDElement("MD_Identifier");
        Element levelO = doc.createGMDElement("authority");
        Element levelP = doc.createGMDElement("CI_Citation");
        Element levelQ = doc.createGMDElement("citedResponsibility");
        outerStack.push(levelM);
        outerStack.push(levelN);
        outerStack.push(levelO);
        outerStack.push(levelP);
        outerStack.push(levelQ);
        Element levelR = doc.createGMDElement("CI_Responsibility");
        LinkedList<OtherID> otherIDS = (LinkedList) djo.getCitationFields().getOtherIDs();
        for(OtherID otherID: otherIDS) {
            XMLStack stack = new XMLStack(doc);
            Element levelS = doc.createGMDElement("party");
            Element levelT = doc.createGMDElement("CI_Organisation");
            stack.push(levelT);
            if (!otherID.getOtherIdAgency().isEmpty()) {
                stack.push(doc.createGMDElement("name"));
                levelT = stack.zip(doc.addGCOVal(otherID.getOtherIdAgency(), CHARACTER));
                if (!otherID.getOtherIdValue().isEmpty()) {
                    stack.push(levelT);
                    stack.push(doc.createGMDElement("partyIdentifier"));
                    stack.push(doc.createGMDElement("MD_Identifier"));
                    stack.push(doc.createGMDElement("code"));
                    levelT = stack.zip(doc.addGCOVal(otherID.getOtherIdValue(), CHARACTER));
                }
            } else if (!otherID.getOtherIdValue().isEmpty()) {

                stack.push(doc.createGMDElement("partyIdentifier"));
                stack.push(doc.createGMDElement("MD_Identifier"));
                stack.push(doc.createGMDElement("code"));
                levelT = stack.zip(doc.addGCOVal(otherID.getOtherIdValue(), CHARACTER));
            }
            levelS.appendChild(levelT);
            levelR.appendChild(levelS);
        }
        levelR.appendChild(levelRoleCode("resourceProvider"));
        levelM = outerStack.zip(levelR);
        return levelM;
    }

    private boolean empty() {
        boolean simple = noteText.isEmpty() && language.isEmpty() && distDate.isEmpty();
        boolean complex = otherIds.isEmpty() && authors.isEmpty() && datasetContacts.isEmpty() && descriptions.isEmpty() && keywords.isEmpty() && topicClassifications.isEmpty() && relatedPublications.isEmpty() && producers.isEmpty() && contributors.isEmpty() && grantNumbers.isEmpty() && timePeriodCovereds.isEmpty() && datesOfCollection.isEmpty() && series.isEmpty() && software.isEmpty();
        return simple && complex;
    }
}
