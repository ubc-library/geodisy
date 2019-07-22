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
        if(empty())
            return root;
        Element levelK = doc.createGMDElement("identificationInfo");
        Element levelL = doc.createGMDElement("DataIdentification");
        Element levelM = doc.createGMDElement("citation");
        levelM.appendChild(getCitation());
        levelL.appendChild(levelM);
        levelM = doc.createGMDElement("pointOfContact");
        levelM.appendChild(getPointOfContact());
        levelM1 = getOtherIds(levelM1);

        

        Element levelN = doc.createGMDElement("CI_Citation");


        //TODO finish fleshing out
        root.appendChild(levelK);
        return root;
    }

    private Element getCitation() {
        Element ci_Citation = doc.createGMDElement("CI_Citation");
        String subtitleVal = sf.getField(SUBTITLE);
        String title = sf.getField(TITLE);
        if(!subtitleVal.isEmpty())
            title += ":" + subtitleVal;
        ci_Citation = setValChild(ci_Citation,"title",title,CHARACTER);
        String altTitleVal = sf.getField(ALT_TITLE);
        if(!altTitleVal.isEmpty())
            ci_Citation = setValChild(ci_Citation,"alternateTitle", subtitleVal, CHARACTER);
        String altUrlVal = sf.getField(ALT_URL);
        if(!altUrlVal.isEmpty()) {
            Element altURL = doc.createGMDElement("onlineResource");
            altURL = getOnlineResource(altURL,altUrlVal);
            ci_Citation.appendChild(altURL);
            ci_Citation = getOtherIds(ci_Citation);
            ci_Citation = getAuthor(ci_Citation);
        }
        return ci_Citation;
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
    private Element getOtherIds(Element ci_Citation) {
        Element id = doc.createGMDElement("identifier");
        Element md_Identifier =  doc.createGMDElement("MD_Identifier");

        
    }

    private boolean empty() {
        boolean simple = noteText.isEmpty() && language.isEmpty() && distDate.isEmpty();
        boolean complex = otherIds.isEmpty() && authors.isEmpty() && datasetContacts.isEmpty() && descriptions.isEmpty() && keywords.isEmpty() && topicClassifications.isEmpty() && relatedPublications.isEmpty() && producers.isEmpty() && contributors.isEmpty() && grantNumbers.isEmpty() && timePeriodCovereds.isEmpty() && datesOfCollection.isEmpty() && series.isEmpty() && software.isEmpty();
        return simple && complex;
    }
}
