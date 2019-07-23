package Crosswalking.XML;

import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.*;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;


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
        if(relatedPublications.size()>0)
            levelJ = getRelatedPubs(levelJ);
        if(!noteText.isEmpty()) {
            Element levelK = doc.createGMDElement("supplementalInformation");
            levelK.appendChild(doc.addGCOVal(noteText,CHARACTER));
            levelJ.appendChild(levelK);
        }
        if(!language.isEmpty()){
            levelJ = getLanguage(levelJ);
        }
        if(datasetContacts.size()>0)
            levelJ.appendChild(getPointOfContact());

        




        //TODO finish fleshing out
        levelI.appendChild(levelJ);
        root.appendChild(levelI);
        return root;
    }

    private Element getLanguage(Element levelJ) {
        XMLStack stack = new XMLStack();
        stack.push(levelJ);
        stack.push(doc.createGMDElement("defaultLocale"));
        stack.push(doc.createGMDElement("PT_Locale"));
        Element localN = doc.createGMDElement("defaultLocale");
        localN.appendChild(doc.addGCOVal(language,"LanguageCode"));
        levelJ = stack.zip(localN);
        return levelJ;
    }

    private Element getRelatedPubs(Element levelJ) {
        Element levelK = doc.createGMDElement("additionalDocumentation");
        for(RelatedPublication rp: relatedPublications){
            Element levelL = doc.createGMDElement("CI_Citation");
            Element levelM = doc.createGMDElement("otherCitationDetails");
            levelM.appendChild(doc.addGCOVal(rp.getPublicationCitation(),CHARACTER));
            levelL.appendChild(levelM);
            levelK.appendChild(levelL);
            if(!rp.getPublicationIDType().isEmpty()||!rp.getPublicationIDNumber().isEmpty()){
                levelL = doc.createGMDElement("CI_Citation");
                levelM = doc.createGMDElement("identifier");
                Element levelN;
                Element levelO;
                if(!rp.getPublicationIDType().isEmpty()){
                    levelN = doc.createGMDElement("MD_Identifier");
                    levelO = doc.createGMDElement("codeSpace");
                    levelO.appendChild(doc.addGCOVal(rp.getPublicationIDType(),CHARACTER));
                    levelN.appendChild(levelO);
                    levelM.appendChild(levelN);
                }
                if(!rp.getPublicationIDNumber().isEmpty()){
                    levelN = doc.createGMDElement("MD_Identifier");
                    levelO = doc.createGMDElement("code");
                    levelO.appendChild(doc.addGCOVal(rp.getPublicationIDNumber(),CHARACTER));
                    levelN.appendChild(levelO);
                    levelM.appendChild(levelN);
                }
                levelL.appendChild(levelM);
                levelK.appendChild(levelL);
            }
            if(!rp.getPublicationURL().isEmpty()){
                XMLStack stack = new XMLStack();
                stack.push(levelK);
                stack.push(doc.createGMDElement("CI_Citation")); //L
                stack.push(doc.createGMDElement("onlineResource")); //M
                stack.push(doc.createGMDElement("CI_OnlineResource")); //N
                Element levelO = doc.createGMDElement("linkage");
                levelO.appendChild(doc.addGCOVal(rp.getPublicationURL(),CHARACTER));
                levelK = stack.zip(levelO);
            }
        }
        return levelK;
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
        if(topicClassifications.size()>0)
            levelK = getTopicClassifications(levelK);
        levelJ.appendChild(levelK);
        return levelJ;
    }

    private Element getTopicClassifications(Element levelK) {
        Element levelL;
        Element levelM;
        Element levelN;
        Element levelO;
        Element levelQ;
        for(TopicClassification tc: topicClassifications){
            levelL = doc.createGMDElement("MD_Keywords");
            levelM = doc.createGMDElement("keyword");
            levelM.appendChild(doc.addGCOVal(tc.getTopicClassValue(),CHARACTER));
            levelL.appendChild(levelM);
            levelK.appendChild(levelL);
            XMLStack stack = new XMLStack();
            if(!tc.getTopicClassVocab().isEmpty()){
                levelL = doc.createGMDElement("MD_Keywords");
                levelM = doc.createGMDElement("thesaurusName");
                levelN = doc.createGMDElement("CI_Citation");
                levelO = doc.createGMDElement("title");
                levelO.appendChild(doc.addGCOVal(tc.getTopicClassVocab(),CHARACTER));
                levelN.appendChild(levelO);
                levelM.appendChild(levelN);
                if(!tc.getTopicClassVocabURL().isEmpty()){
                    stack.push(doc.createGMDElement("CI_Citation")); //N
                    stack.push(doc.createGMDElement("onlineResource")); //O
                    stack.push(doc.createGMDElement("CI_OnlineResource")); //P
                    levelQ = doc.createGMDElement("linkage");
                    levelQ.appendChild(doc.addGCOVal(tc.getTopicClassVocabURL(),CHARACTER));
                    levelN = stack.zip(levelQ);
                    levelM.appendChild(levelN);
                }
            }
            levelL.appendChild(levelM);
            levelK.appendChild(levelL);
        }
        levelL = doc.createGMDElement("type");
        levelL.appendChild(doc.addGMDVal("theme","MD_KeywordTypeCode"));
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
        XMLStack stack = new XMLStack();
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
                    stack.push(levelN);
                    stack = doc.createGMDElement("contactInfo", stack); //O
                    stack = doc.createGMDElement("CI_Contact", stack); //P
                    stack = doc.createGMDElement("address", stack); //Q
                    stack = doc.createGMDElement("CI_Address", stack); //R
                    stack = doc.createGMDElement("electronicMailAddress", stack); //S
                    levelN = stack.zip(doc.addGCOVal(dsContactEmail,CHARACTER));

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
            XMLStack stack = new XMLStack();
            if(!p.getProducerName().isEmpty()) {
                stack.push(levelM);
                stack = doc.createGMDElement("CI_Individual", stack); //N
                stack = doc.createGMDElement("name", stack); //O
                levelM = stack.zip(doc.addGCOVal(p.getProducerName(), CHARACTER));
            }
            if(!p.getProducerAffiliation().isEmpty()||!p.getProducerURL().isEmpty()||!p.getProducerLogoURL().isEmpty()) {
                levelN = doc.createGMDElement("CI_Organisation");
                if (!p.getProducerAffiliation().isEmpty()) {
                    stack.push(levelN);
                    stack = doc.createGMDElement("name", stack); //O
                    levelN = stack.zip(doc.addGCOVal(p.getProducerAffiliation(), CHARACTER));
                }
                if (!p.getProducerURL().isEmpty()) {
                    stack.push(levelN);
                    stack = doc.createGMDElement("contactInfo", stack); //O
                    stack = doc.createGMDElement("CI_Contact", stack); //P
                    stack = doc.createGMDElement("onlineResource", stack); //Q
                    stack = doc.createGMDElement("CI_OnlineResource", stack); //R
                    stack = doc.createGMDElement("linkage", stack); //S
                    levelN = stack.zip(doc.addGCOVal(p.getProducerAbbreviation(), CHARACTER));
                }
                if (!p.getProducerLogoURL().isEmpty()) {
                    stack.push(levelN);
                    stack = doc.createGMDElement("logo",stack); //O
                    stack = doc.createGMDElement("MD_BrowseGraphic",stack); //P
                    stack = doc.createGMDElement("linkage", stack); //Q
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
        XMLStack outerStack = new XMLStack();
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
            XMLStack stack = new XMLStack();
            Element levelS = doc.createGMDElement("party");
            Element levelT = doc.createGMDElement("CI_Organisation");
            stack.push(levelT);
            if (!otherID.getOtherIdAgency().isEmpty()) {
                stack.push(doc.createGMDElement("name")); //U
                levelT = stack.zip(doc.addGCOVal(otherID.getOtherIdAgency(), CHARACTER));
                if (!otherID.getOtherIdValue().isEmpty()) {
                    stack.push(levelT);
                    stack.push(doc.createGMDElement("partyIdentifier")); //U
                    stack.push(doc.createGMDElement("MD_Identifier")); //V
                    stack.push(doc.createGMDElement("code"));//W
                    levelT = stack.zip(doc.addGCOVal(otherID.getOtherIdValue(), CHARACTER));
                }
            } else if (!otherID.getOtherIdValue().isEmpty()) {

                stack.push(doc.createGMDElement("partyIdentifier")); //U
                stack.push(doc.createGMDElement("MD_Identifier")); //V
                stack.push(doc.createGMDElement("code")); //W
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
