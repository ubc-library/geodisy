package Crosswalking.XML.XMLGroups;

import Crosswalking.XML.XMLTools.SubElement;
import Crosswalking.XML.XMLTools.XMLDocObject;
import Crosswalking.XML.XMLTools.XMLStack;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.*;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses.JournalFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;


import java.util.LinkedList;
import java.util.List;

import static BaseFiles.GeodisyStrings.*;
import static Crosswalking.XML.XMLTools.XMLStrings.*;
import static Dataverse.DVFieldNameStrings.*;
import static Dataverse.DVFieldNameStrings.SOFTWARE;

public class IdentificationInfo extends SubElement {
    List<OtherID> otherIds;
    List<Author> authors;
    List<DatasetContact> datasetContacts;
    List<Description> descriptions;
    List<Keyword> keywords;
    List<TopicClassification> topicClassifications;
    List<RelatedPublication> relatedPublications;
    //NOTESTEXT
    //LANGUAGE
    List<Producer> producers;
    List<Contributor> contributors;
    List<GrantNumber> grantNumbers;
    //DistributionDate
    List<TimePeriodCovered> timePeriodCovereds;
    List<DateOfCollection> datesOfCollection;
    Series series;
    List<Software> software;
    List<String> subjects;
    List<String> relatedMaterials;
    List<String> relatedDatasets;
    List<String> otherReferences;
    List<String> languages;
    CitationFields cf;
    SimpleCitationFields simpleCF;
    GeographicFields geoCF;
    SocialFields socialCF;
    JournalFields journalCF;
    String noteText,  distDate;
    XMLStack stack;

    public IdentificationInfo(DataverseJavaObject djo, XMLDocObject doc, Element root) {
        super(djo,doc,root);
        cf = djo.getCitationFields();
        simpleCF = djo.getSimpleFields();


        otherIds= (LinkedList) cf.getListField(OTHER_ID);
        authors = (LinkedList) cf.getListField(AUTHOR);
        datasetContacts = (LinkedList) cf.getListField(DS_CONTACT);
        descriptions = (LinkedList) cf.getListField(DS_DESCRIPT);
        keywords = (LinkedList) cf.getListField(KEYWORD);
        topicClassifications = (LinkedList) cf.getListField(TOPIC_CLASS);
        relatedPublications = (LinkedList) cf.getListField(PUBLICATION);
        noteText = simpleCF.getField(NOTES_TEXT);
        languages = cf.getListField(LANGUAGE);
        producers = (LinkedList) cf.getListField(PRODUCER);
        contributors = (LinkedList) cf.getListField(CONTRIB);
        grantNumbers = (LinkedList) cf.getListField(GRANT_NUM);
        distDate = simpleCF.getField(DIST_DATE);
        timePeriodCovereds = (LinkedList) cf.getListField(TIME_PER_COV);
        datesOfCollection = (LinkedList) cf.getListField(DATE_OF_COLLECT);
        series = cf.getSeries();
        software = (LinkedList) cf.getListField(SOFTWARE);
        subjects = (LinkedList) cf.getListField(SUBJECT);
        relatedMaterials = (LinkedList) cf.getListField(RELATED_MATERIAL);
        relatedDatasets = (LinkedList) cf.getListField(RELATED_DATASETS);
        otherReferences = (LinkedList) cf.getListField(OTHER_REFERENCES);
        stack = new XMLStack();
        
    }
    @Override
    public Element getFields() {
        Element levelI = doc.createMDBElement(IDENT_INFO);
        Element levelJ = doc.createMDBElement(MD_DATA_IDENT);

        levelJ.appendChild(getCitation());
        if(descriptions.size()>0){
            levelJ = getDescriptions(levelJ);
        }
        if(subjects.size()>0 || keywords.size()>0)
            levelJ = getKeywords(levelJ);
        if(relatedPublications.size()>0)
            levelJ = getRelatedPubs(levelJ);
        if(!noteText.isEmpty()) {
            Element levelK = doc.createMDBElement("supplementalInformation");
            levelK.appendChild(doc.addGCOVal(noteText,CHARACTER));
            levelJ.appendChild(levelK);
        }
        if(!languages.isEmpty()){
            levelJ = getLanguage(levelJ);
        }
        if(datasetContacts.size()>0)
            levelJ.appendChild(getPointOfContact());

        if(software.size()>0){
            levelJ = getSoftware(levelJ);
        }

        if(relatedMaterials.size()>0||relatedDatasets.size()>0||otherReferences.size()>0||!series.getSeriesName().isEmpty()){
            levelJ = getAdditionalDocs(levelJ);
        }
        if(contributors.size()>0){
            levelJ = getContributors(levelJ);
        }
        if(grantNumbers.size()>0){
            levelJ = getGrantNumbers(levelJ);
        }
        levelJ = getTimePeriods(levelJ);
        GeographicInfo gi = new GeographicInfo(djo, doc, levelJ);
        levelJ = gi.getFields();

        SocialFieldInfo sfi = new SocialFieldInfo(djo, doc, levelJ);
        levelJ =  sfi.getFields();

        //Journal metadata is for Journal Dataverses
/*        JournalInfo ji = new JournalInfo(djo,doc,levelJ);
        levelJ = ji.getFields();*/
        




        //TODO finish fleshing out
        levelI.appendChild(levelJ);
        root.appendChild(levelI);
        return root;
    }
    //TODO Mark needs to figure out how to differentiate TimePeriodCovered and DateOfCollection
    private Element getTimePeriods(Element levelJ) {
        stack = new XMLStack();
        stack.push(levelJ);
        stack.push(doc.createMDBElement(EXTENT)); //K
        stack.push(doc.createMDBElement(EX_EXTENT)); //L
        stack.push(doc.createMDBElement("temporalElement")); //M
        Element levelN = doc.createMDBElement("EX_TemporalExtent");
        for(TimePeriodCovered tpc: timePeriodCovereds){
            if(!tpc.getTimePeriodCoveredStart().isEmpty()) {
                Element levelO = doc.createMDBElement(EXTENT);
                Element levelP = doc.createMDBElement(TIME_PERIOD);
                Element levelQ = doc.createMDBElement(BEGIN_POSITION);
                levelQ.setTextContent(tpc.getTimePeriodCoveredStart());
                levelP.appendChild(levelQ);
                if(!tpc.getTimePeriodCoveredEnd().isEmpty()){
                    levelQ = doc.createMDBElement(END_POSITION);
                    levelQ.setTextContent(tpc.getTimePeriodCoveredEnd());
                    levelP.appendChild(levelQ);
                }
                levelO.appendChild(levelP);
                levelN.appendChild(levelO);
            }else{
                Element levelO = doc.createMDBElement(EXTENT);
                Element levelP = doc.createMDBElement(TIME_PERIOD);
                Element levelQ = doc.createMDBElement(END_POSITION);
                levelQ.setTextContent(tpc.getTimePeriodCoveredEnd());
                levelP.appendChild(levelQ);
                levelO.appendChild(levelP);
                levelN.appendChild(levelO);
            }
        }
        for(DateOfCollection dc: datesOfCollection){
            if(!dc.getDateOfCollectionStart().isEmpty()) {
                Element levelO = doc.createMDBElement(EXTENT);
                Element levelP = doc.createMDBElement(TIME_PERIOD);
                Element levelQ = doc.createMDBElement(BEGIN_POSITION);
                levelQ.setTextContent(dc.getDateOfCollectionStart());
                levelP.appendChild(levelQ);
                if(!dc.getDateOfCollectionEnd().isEmpty()){
                    levelQ = doc.createMDBElement(END_POSITION);
                    levelQ.setTextContent(dc.getDateOfCollectionEnd());
                    levelP.appendChild(levelQ);
                }
                levelO.appendChild(levelP);
                levelN.appendChild(levelO);
            }else{
                Element levelO = doc.createMDBElement(EXTENT);
                Element levelP = doc.createMDBElement(TIME_PERIOD);
                Element levelQ = doc.createMDBElement(END_POSITION);
                levelQ.setTextContent(dc.getDateOfCollectionEnd());
                levelP.appendChild(levelQ);
                levelO.appendChild(levelP);
                levelN.appendChild(levelO);
            }
        }
        return stack.zip(levelN);
    }

    private Element getGrantNumbers(Element levelJ) {
        for(GrantNumber n: grantNumbers){
            String agency =  n.getGrantNumberAgency();
            String number = n.getGrantNumberValue();
            if(!number.isEmpty())
                agency= agency +": "+number;
            Element levelK = doc.createMDBElement("credit");
            levelK.appendChild(doc.addGCOVal(agency,CHARACTER));
            levelJ.appendChild(levelK);
        }
        return levelJ;
    }

    private Element getContributors(Element levelJ) {
        for(Contributor c: contributors){
            String name =  c.getContributorName();
            String type = c.getContributorType();
            if(!type.isEmpty())
                name= name +": "+type;
            Element levelK = doc.createMDBElement("credit");
            levelK.appendChild(doc.addGCOVal(name,CHARACTER));
            levelJ.appendChild(levelK);
        }
        return levelJ;
    }

    private Element getAdditionalDocs(Element levelJ) {
        stack = new XMLStack();
        stack.push(levelJ);
        stack.push(doc.createMDBElement(ADDITIONAL_DOCS)); //K
        Element levelL = doc.createMDBElement(CI_CITE);
        if(series != null) {
            Element levelM = doc.createMDBElement("series");
            Element levelN = doc.createMDBElement("CI_Series");
            Element levelO = doc.createMDBElement(NAME);
            levelO.appendChild(doc.addGCOVal(series.getSeriesName(),CHARACTER));
            levelN.appendChild(levelO);
            levelM.appendChild(levelN);
            levelL.appendChild(levelM);
        }
        for(String s: relatedMaterials) {
            Element levelM = doc.createMDBElement(OTHER_CITE_DEETS);
            levelM.appendChild(doc.addGCOVal(s,CHARACTER));
            levelL.appendChild(levelM);
        }
        for(String s: relatedDatasets) {
            Element levelM = doc.createMDBElement(OTHER_CITE_DEETS);
            levelM.appendChild(doc.addGCOVal(s,CHARACTER));
            levelL.appendChild(levelM);
        }
        for(String s: otherReferences) {
            Element levelM = doc.createMDBElement(OTHER_CITE_DEETS);
            levelM.appendChild(doc.addGCOVal(s,CHARACTER));
            levelL.appendChild(levelM);
        }
        return stack.zip(levelL);
    }

    private Element getSoftware(Element levelJ) {
        stack = new XMLStack();
        for(Software s: software){
            stack.push(levelJ);
            stack.push(doc.createMDBElement("environmentDescription"));
            levelJ = stack.zip(doc.addGCOVal(s.getSoftwareName(),CHARACTER));
            if(!s.getSoftwareVersion().isEmpty()) {
                stack.push(levelJ);
                stack.push(doc.createMDBElement("environmentDescription"));
                levelJ = stack.zip(doc.addGCOVal(s.getSoftwareVersion(),CHARACTER));
            }
        }
        return levelJ;
    }

    private Element getLanguage(Element levelJ) {
        stack = new XMLStack();
        stack.push(levelJ);
        stack.push(doc.createMDBElement("defaultLocale")); //K
        Element levelL = doc.createMDBElement("PT_Locale");
        Element levelM = null;
        for(String s:languages) {
            levelM = doc.createMDBElement("defaultLocale");
            levelM.appendChild(doc.addGCOVal(s, "LanguageCode"));
            levelL.appendChild(levelM);
        }
        levelJ = stack.zip(levelL);
        return levelJ;
    }

    private Element getRelatedPubs(Element levelJ) {
        Element levelK = doc.createMDBElement(ADDITIONAL_DOCS);
        Element levelL = doc.createMDBElement(CI_CITE);
        for(RelatedPublication rp: relatedPublications){
            Element levelM = doc.createMDBElement(OTHER_CITE_DEETS);
            levelM.appendChild(doc.addGCOVal(rp.getPublicationCitation(),CHARACTER));
            levelL.appendChild(levelM);
            if(!rp.getPublicationIDType().isEmpty()||!rp.getPublicationIDNumber().isEmpty()){
                levelM = doc.createMDBElement(IDENT);
                Element levelN;
                Element levelO;
                if(!rp.getPublicationIDType().isEmpty()){
                    levelN = doc.createMDBElement(MD_IDENT);
                    levelO = doc.createMDBElement("codeSpace");
                    levelO.appendChild(doc.addGCOVal(rp.getPublicationIDType(),CHARACTER));
                    levelN.appendChild(levelO);
                    levelM.appendChild(levelN);
                }
                if(!rp.getPublicationIDNumber().isEmpty()){
                    levelN = doc.createMDBElement(MD_IDENT);
                    levelO = doc.createMDBElement(CODE);
                    levelO.appendChild(doc.addGCOVal(rp.getPublicationIDNumber(),CHARACTER));
                    levelN.appendChild(levelO);
                    levelM.appendChild(levelN);
                }
                levelL.appendChild(levelM);
            }
            levelK.appendChild(levelL);
            if(!rp.getPublicationURL().isEmpty()){
                stack = new XMLStack();
                stack.push(levelK);
                stack.push(doc.createMDBElement(CI_CITE)); //L
                stack.push(doc.createMDBElement(ONLINE_RES)); //M
                stack.push(doc.createMDBElement(CI_ONLINE_RES)); //N
                Element levelO = doc.createMDBElement(LINKAGE);
                levelO.appendChild(doc.addGCOVal(rp.getPublicationURL(),CHARACTER));
                levelK = stack.zip(levelO);
            }
        }
        levelJ.appendChild(levelK);
        return levelJ;
    }

    private Element getKeywords(Element levelJ) {
        Element levelK = doc.createMDBElement("descriptiveKeywords");
        Element levelL = doc.createMDBElement("MD_Keywords");
        Element levelM;
        Element levelN;
        Element levelO;
        for(String s:subjects){
            levelM = doc.createMDBElement("keyword");
            levelM.appendChild(doc.addGCOVal(s,CHARACTER));
            levelL.appendChild(levelM);
        }
        for(Keyword k: keywords){
            if(!k.getKeywordValue().isEmpty()){
                levelM = doc.createMDBElement("keyword");
                levelM.appendChild(doc.addGCOVal(k.getKeywordValue(),CHARACTER));
                levelL.appendChild(levelM);
            }
            if(!k.getKeywordVocabulary().isEmpty()){
                levelM = doc.createMDBElement("thesaurusName");
                levelN = doc.createMDBElement(CI_CITE);
                levelO = doc.createMDBElement("title");
                levelO.appendChild(doc.addGCOVal(k.getKeywordVocabulary(),CHARACTER));
                levelN.appendChild(levelO);
                if(!k.getKeywordVocabularyURL().isEmpty()){
                    levelO = doc.createMDBElement("onlineResource");
                    levelO.appendChild(doc.addGCOVal(k.getKeywordVocabularyURL(),CHARACTER));
                    levelN.appendChild(levelO);
                }
                levelM.appendChild(levelN);
                levelL.appendChild(levelM);
            }
        }
        levelM = doc.createMDBElement("MD_KeywordTypeCode");
        levelN = doc.createMDBElement("type");
        levelN.appendChild(doc.addMDBVal("theme", "MD_KeywordTypeCode"));
        levelM.appendChild(levelN);
        levelL.appendChild(levelM);
        levelK.appendChild(levelL);
        if(topicClassifications.size()>0)
            levelK = getTopicClassifications(levelK);
        levelJ.appendChild(levelK);
        return levelJ;
    }

    private Element getTopicClassifications(Element levelK) {
        Element levelL = doc.createMDBElement("MD_Keywords");
        Element levelM;
        Element levelN;
        Element levelO;
        Element levelQ;
        for(TopicClassification tc: topicClassifications){
            levelM = doc.createMDBElement("keyword");
            levelM.appendChild(doc.addGCOVal(tc.getTopicClassValue(),CHARACTER));
            levelL.appendChild(levelM);
            levelK.appendChild(levelL);
            stack = new XMLStack();
            if(!tc.getTopicClassVocab().isEmpty()){
                levelM = doc.createMDBElement("thesaurusName");
                levelN = doc.createMDBElement(CI_CITE);
                levelO = doc.createMDBElement("title");
                levelO.appendChild(doc.addGCOVal(tc.getTopicClassVocab(),CHARACTER));
                levelN.appendChild(levelO);
                if(!tc.getTopicClassVocabURL().isEmpty()){
                    stack.push(levelN); //N
                    stack.push(doc.createMDBElement(ONLINE_RES)); //O
                    stack.push(doc.createMDBElement(CI_ONLINE_RES)); //P
                    levelQ = doc.createMDBElement(LINKAGE);
                    levelQ.appendChild(doc.addGCOVal(tc.getTopicClassVocabURL(),CHARACTER));
                    levelN = stack.zip(levelQ);

                }
                levelM.appendChild(levelN);
            }
            levelL.appendChild(levelM);
            levelK.appendChild(levelL);
        }
        levelL = doc.createMDBElement("type");
        levelL.appendChild(doc.addMDBVal("theme","MD_KeywordTypeCode"));
        levelK.appendChild(levelL);
        return levelK;
    }

    private Element getDescriptions(Element levelJ) {
        for(Description d: descriptions) {
            Element levelK = doc.createMDBElement("abstract");
            levelK.appendChild(doc.addGCOVal(d.getDsDescriptionValue(),CHARACTER));
            levelJ.appendChild(levelK);
        }
        return levelJ;
    }

    private Element getPointOfContact() {
        List<DatasetContact> datasetContacts = (LinkedList) cf.getListField(DS_CONTACT);
        Element levelK = doc.createMDBElement(P_OF_CONTACT);
        Element levelL = doc.create_Element(CI_RESPONSIBILITY);
        Element levelM;
        stack = new XMLStack();
        for(DatasetContact dc: datasetContacts) {
            levelM = doc.createMDBElement(PARTY);
            String dsContactName = dc.getDatasetContactName();
            String dsContactEmail = dc.getDatasetContactEmail();
            if(!dsContactName.isEmpty()||!dsContactEmail.isEmpty()) {
                Element levelN = doc.createMDBElement(CI_INDIV);
                if(!dsContactName.isEmpty()){
                    Element levelO = doc.createMDBElement(NAME);
                    levelO.appendChild(doc.addGCOVal(dsContactName,CHARACTER));
                    levelN.appendChild(levelO);
                }
                if(!dsContactEmail.isEmpty()){
                    stack.push(levelN);
                    stack.push(doc.createMDBElement("contactInfo")); //O
                    stack.push(doc.createMDBElement(CI_CITE)); //P
                    stack.push(doc.createMDBElement("address")); //Q
                    stack.push(doc.createMDBElement("CI_Address")); //R
                    stack.push(doc.createMDBElement("electronicMailAddress")); //S
                    levelN = stack.zip(doc.addGCOVal(dsContactEmail,CHARACTER));

                }
                levelM.appendChild(levelN);
            }
            String dsContactAffil = dc.getDatasetContactAffiliation();
            String dsPublisher = djo.getSimpleFieldVal(PUBLISHER);
            if(!dsContactAffil.isEmpty()||!dsPublisher.isEmpty()){
                Element levelN = doc.createMDBElement(CI_ORG);
                Element levelO;
                if(!dsContactAffil.isEmpty()){
                    levelO = doc.createMDBElement(NAME);
                    levelO.appendChild(doc.addGCOVal(dsContactAffil,CHARACTER));
                    levelN.appendChild(levelO);
                }
                if(!dsPublisher.isEmpty()){
                    levelO = doc.createMDBElement(NAME);
                    levelO.appendChild(doc.addGCOVal(dsPublisher,CHARACTER));
                    levelN.appendChild(levelO);
                }
                levelM.appendChild(levelN);
            }
            levelL.appendChild(levelM);
        }
        levelL.appendChild(levelRoleCode(P_OF_CONTACT));
        levelL = getProducer(levelL);
        levelK.appendChild(levelL);
        return levelK;
    }

    private Element getProducer(Element levelL) {
        Element levelM = null;
        for(Producer p: producers){
            levelM = doc.createMDBElement(PARTY);
            Element levelN;
            stack = new XMLStack();
            if(!p.getProducerName().isEmpty()) {
                stack.push(levelM);
                stack.push(doc.createMDBElement(CI_INDIV)); //N
                stack.push(doc.createMDBElement(NAME)); //O
                levelM = stack.zip(doc.addGCOVal(p.getProducerName(), CHARACTER));
            }
            if(!p.getProducerAffiliation().isEmpty()||!p.getProducerURL().isEmpty()||!p.getProducerLogoURL().isEmpty()) {
                levelN = doc.createMDBElement(CI_ORG);
                if (!p.getProducerAffiliation().isEmpty()) {
                    stack.push(levelN);
                    stack.push(doc.createMDBElement(NAME)); //O
                    levelN = stack.zip(doc.addGCOVal(p.getProducerAffiliation(), CHARACTER));
                }
                if (!p.getProducerURL().isEmpty()) {
                    stack.push(levelN);
                    stack.push(doc.createMDBElement("contactInfo")); //O
                    stack.push(doc.createMDBElement(CI_CITE)); //P
                    stack.push(doc.createMDBElement(ONLINE_RES)); //Q
                    stack.push(doc.createMDBElement(CI_ONLINE_RES)); //R
                    stack.push(doc.createMDBElement(LINKAGE)); //S
                    levelN = stack.zip(doc.addGCOVal(p.getProducerURL(), CHARACTER));
                }
                if (!p.getProducerLogoURL().isEmpty()) {
                    stack.push(levelN);
                    stack.push(doc.createMDBElement("logo")); //O
                    stack.push(doc.createMDBElement("MD_BrowseGraphic")); //P
                    stack.push(doc.createMDBElement(LINKAGE)); //Q
                    stack.push(doc.createMDBElement(CI_ONLINE_RES)); //R
                    stack.push(doc.createMDBElement(LINKAGE)); //S
                    levelN = stack.zip(doc.addGCOVal(p.getProducerLogoURL(), CHARACTER));
                }
                levelM.appendChild(levelN);
            }
        }
        if(levelM!=null) {
            levelL.appendChild(levelM);
            levelM.appendChild(levelRoleCode("custodian"));
            levelL.appendChild(levelM);
        }
        return levelL;
    }

    private Element getCitation() {
        Element levelK = doc.createMDBElement("citation");
        Element levelL = doc.createMDBElement(CI_CITE);
        levelL.appendChild(getDOI());
        //System generated Dates/Info
        levelL = getSystemVals(levelL);
        String subtitleVal = simpleCF.getField(SUBTITLE);
        String title = simpleCF.getField(TITLE);
        //Title
        if(!subtitleVal.isEmpty())
            title += ": " + subtitleVal;
        levelL = setValChild(levelL,"title",title,CHARACTER);
        String altTitleVal = simpleCF.getField(ALT_TITLE);
        //Alt Title
        if(!altTitleVal.isEmpty())
            levelL = setValChild(levelL,"alternateTitle", altTitleVal, CHARACTER);
        String altUrlVal = simpleCF.getField(ALT_URL);
        //Alt URL
        if(!altUrlVal.isEmpty()) {
            Element altURL = doc.createMDBElement("onlineResource");
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

    private Element getSystemVals(Element levelL) {
        stack = new XMLStack();
        Element levelM = doc.createMDBElement(XMLDATE);
        stack.push(levelM);
        stack.push(doc.createMDBElement(CI_DATE)); //N
        stack.push(doc.createMDBElement(XMLDATE)); //O
        levelM = stack.zip(doc.addGCOVal(simpleCF.getField(PUB_DATE),DATE_TIME));
        stack.push(levelM);
        stack.push(doc.createMDBElement(CI_DATE)); //N
        levelM = stack.zip(doc.addMDBVal("publication","CI_DateTypeCode" ));
        levelL.appendChild(levelM);

        //Version
        levelL.appendChild(levelM);
        levelM = doc.createMDBElement("edition");
        levelM.appendChild(doc.addGCOVal(Integer.toString(simpleCF.getVersion()),CHARACTER));
        levelL.appendChild(levelM);
        levelM = doc.createMDBElement("editionDate");
        //version date
        stack.push(levelM);
        stack.push(doc.createMDBElement(CI_DATE)); //N
        stack.push(doc.createMDBElement(XMLDATE)); //O
        levelM = stack.zip(doc.addGCOVal(simpleCF.getField(DIST_DATE),CHARACTER));
        levelL.appendChild(levelM);
        levelM = doc.createMDBElement("editionDate");
        stack.push(levelM);
        stack.push(doc.createMDBElement(CI_DATE));
        levelM = stack.zip(doc.addMDBVal("lastUpdate","CI_DateTypeCode" ));
        levelL.appendChild(levelM);
        return levelL;
    }

    private Element getDOI() {
        stack = new XMLStack();
        stack.push(doc.createMDBElement(IDENT)); //M
        stack.push(doc.createMDBElement(MD_IDENT)); //N
        stack.push(doc.createMDBElement(CODE)); //O
        return stack.zip(doc.addGCOVal(djo.getDOI(),CHARACTER));
    }

    private Element getAuthor() {
        Element levelM = doc.createMDBElement("citeResponsibleParty");
        Element levelN = null;
        for(Author a: authors){
            levelN = doc.createMDBElement(CI_RESPONSIBILITY);
            Element levelO = doc.createMDBElement(PARTY);
            Element levelP =  doc.createMDBElement(CI_INDIV);
            Element levelQ;
            Element levelR;
            Element levelS;
            if(!a.getField(AUTHOR_NAME).isEmpty()) {
                levelQ = doc.createMDBElement(NAME);
                levelQ.appendChild(doc.addGCOVal(a.getField(AUTHOR_NAME), CHARACTER));
                levelP.appendChild(levelQ);
            }
            String authorIDScheme = a.getField(AUTHOR_ID_SCHEME);
            String authorID = a.getField(AUTHOR_ID);
            if(!authorIDScheme.isEmpty()||!authorID.isEmpty()) {
                levelQ = doc.createMDBElement(PARTY_IDENT);
                levelR = doc.createMDBElement(MD_IDENT);
                if(!authorIDScheme.isEmpty()) {
                    levelS = doc.createMDBElement("codeSpace");
                    levelS.appendChild(doc.addGCOVal(authorIDScheme, CHARACTER));
                    levelR.appendChild(levelS);
                }
                if(!authorID.isEmpty()){
                    levelS = doc.createMDBElement(CODE);
                    levelS.appendChild(doc.addGCOVal(authorID,CHARACTER));
                    levelR.appendChild(levelS);
                }
                levelQ.appendChild(levelR);
                levelP.appendChild(levelQ);
            }
            levelO.appendChild(levelP);
            levelN.appendChild(levelO);
            if(!a.getField(AUTHOR_AFFIL).isEmpty()) {
                levelO = doc.createMDBElement(PARTY);
                levelP = doc.createMDBElement(CI_ORG);
                levelQ = doc.createMDBElement(NAME);
                levelQ.appendChild(doc.addGCOVal(a.getField(AUTHOR_AFFIL), CHARACTER));
                levelP.appendChild(levelQ);
                levelO.appendChild(levelP);
                levelN.appendChild(levelO);
            }
            levelM.appendChild(levelN);
        }
        levelN = doc.createMDBElement(CI_RESPONSIBILITY);
        levelN.appendChild(levelRoleCode("author"));
        levelM.appendChild(levelN);
        return levelM;
    }


    private Element getOnlineResource(Element onlineResouce, String altUrlVal) {
        Element ciOnline = doc.createMDBElement(CI_ONLINE_RES);
        Element linkage = doc.createMDBElement(LINKAGE);
        Element url = doc.createMDBElement("URL");
        url.setTextContent(altUrlVal);
        linkage.appendChild(url);
        ciOnline.appendChild(linkage);
        onlineResouce.appendChild(ciOnline);
        return onlineResouce;
    }

    private Element getOtherIds() {
        XMLStack outerStack = new XMLStack();
        Element levelM = doc.createMDBElement(IDENT);
        Element levelN =  doc.createMDBElement(MD_IDENT);
        Element levelO = doc.createMDBElement("authority");
        Element levelP = doc.createMDBElement(CI_CITE);
        Element levelQ = doc.createMDBElement("citedResponsibility");
        outerStack.push(levelM);
        outerStack.push(levelN);
        outerStack.push(levelO);
        outerStack.push(levelP);
        outerStack.push(levelQ);
        Element levelR = doc.createMDBElement(CI_RESPONSIBILITY);
        List<OtherID> otherIDS = djo.getCitationFields().getOtherIDs();
        for(OtherID otherID: otherIDS) {
            stack = new XMLStack();
            Element levelS = doc.createMDBElement(PARTY);
            Element levelT = doc.createMDBElement(CI_ORG);
            stack.push(levelT);
            if (!otherID.getOtherIdAgency().isEmpty()) {
                stack.push(doc.createMDBElement(NAME)); //U
                levelT = stack.zip(doc.addGCOVal(otherID.getOtherIdAgency(), CHARACTER));
                if (!otherID.getOtherIdValue().isEmpty()) {
                    stack.push(levelT);
                    stack.push(doc.createMDBElement(PARTY_IDENT)); //U
                    stack.push(doc.createMDBElement(MD_IDENT)); //V
                    stack.push(doc.createMDBElement(CODE));//W
                    levelT = stack.zip(doc.addGCOVal(otherID.getOtherIdValue(), CHARACTER));
                }
            } else if (!otherID.getOtherIdValue().isEmpty()) {

                stack.push(doc.createMDBElement(PARTY_IDENT)); //U
                stack.push(doc.createMDBElement(MD_IDENT)); //V
                stack.push(doc.createMDBElement(CODE)); //W
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
        boolean simple = noteText.isEmpty() && languages.isEmpty() && distDate.isEmpty();
        boolean complex = otherIds.isEmpty() && authors.isEmpty() && datasetContacts.isEmpty() && descriptions.isEmpty() && keywords.isEmpty() && topicClassifications.isEmpty() && relatedPublications.isEmpty() && producers.isEmpty() && contributors.isEmpty() && grantNumbers.isEmpty() && timePeriodCovereds.isEmpty() && datesOfCollection.isEmpty() && series.getSeriesName().isEmpty() && software.isEmpty();
        return simple && complex;
    }
}
