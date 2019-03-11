package Dataverse;



import Crosswalking.JSONParsing.DataverseParser;
import Dataverse.DataverseJSONFieldClasses.Fields.CompoundField.*;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.*;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.Date;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.SimpleFields;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

/**
 * Java object structure to parse Dataverse Json into
 * May need to change field types for dates, URLs, and/or email addresses.
 * Also need to test to see how textboxes work with this.
 *
 * Dataverse Alias: Java Class: JavaObject Variable
 * Title : String: title
 * Subtitle : String: subtitle
 * Alternative Title : String : alternativeTitle
 * Alternative URL: String : alternativeURL
 * License : String : license
 * Publisher : String : publisher
 * Publication Date : String : Publication Date
 * OtherLocation ID : OtherID : otherID
 *
 *
 */

public class DataverseJavaObject {
    private SimpleFields simpleFields;
    private List<OtherID> otherID;
    private List<Author> author;
    private List<DatasetContact> datasetContact;
    private List<Description> dsDescription;
    private List<Keyword> keyword;
    private List<TopicClassification> topicClassification;
    private List<RelatedPublication> publication;
    private List<Producer> producer;
    private List<Contributor> contributor;
    private List<GrantNumber> grantNumber;
    private List<Distributor> distributor;
    private List<TimePeriodCovered> timePeriodCovered;
    private List<DateOfCollection> datesOfCollection;
    private List<Series> series;
    private List<Software> software;
    private List<String> relatedMaterial, relatedDatasets, otherReferences, dataSources, kindOfData, language, subject;
    private List<GeographicCoverage> geographicCoverage;
    private List<GeographicBoundingBox> geographicBoundingBoxes;
    protected Logger logger = LogManager.getLogger(DataverseParser.class);

    public DataverseJavaObject() {
        this.simpleFields = new SimpleFields();
        this.otherID = new LinkedList<>();
        this.author = new LinkedList<>();
        this.datasetContact = new LinkedList<>();
        this.dsDescription = new LinkedList<>();
        this.subject = new LinkedList<>();
        this.keyword = new LinkedList<>();
        this.topicClassification = new LinkedList<>();
        this.publication = new LinkedList<>();
        this.language = new LinkedList<>();
        this.producer = new LinkedList<>();
        this.contributor = new LinkedList<>();
        this.grantNumber = new LinkedList<>();
        this.distributor  = new LinkedList<>();
        this.timePeriodCovered = new LinkedList<>();
        this.datesOfCollection = new LinkedList<>();
        this.kindOfData = new LinkedList<>();
        this.series  = new LinkedList<>();
        this.software = new LinkedList<>();
        this.relatedMaterial = new LinkedList<>();
        this.relatedDatasets = new LinkedList<>();
        this.otherReferences = new LinkedList<>();
        this.dataSources = new LinkedList<>();
        this.geographicCoverage = new LinkedList<>();
        this.geographicBoundingBoxes = new LinkedList<>();
    }

    public void setBaseFields(JSONObject current){

        simpleFields.setField(ALT_URL,parseSimpleValue( current,"persistentUrl"));
        simpleFields.setField(PUB_DATE, getValueDate(current,PUB_DATE));
        simpleFields.setField(PUBLISHER, parseSimpleValue(current,PUBLISHER));
        current = current.getJSONObject("latestVersion");
        simpleFields.setField(PROD_DATE,getValueDate(current,PROD_DATE));
        simpleFields.setField(DEPOS_DATE,getValueDate(current,"createTime"));
        simpleFields.setField(DIST_DATE,getValueDate(current,"releaseTime"));
        simpleFields.setField(LICENSE,parseSimpleValue(current,LICENSE));

    }

    /**
     *
     * @param current Current JSONObject to extract a simple String value from (fieldname as key to String value)
     * @param fieldName The name of the field to get the String value from
     * @return String value if field exists, otherwise an empty string
     */
    protected String parseSimpleValue(JSONObject current, String fieldName) {
        if(current.has(fieldName))
            return current.getString(fieldName);
        return "";
    }
    /**
     *
     * @param current Current JSONObject to extract a date from
     * @param fieldName The name of the field that has the date
     * @return An empty String if the object doesn't have that field or the String result from filterForDate(the String at location fieldname)
     */
    protected String getValueDate(JSONObject current, String fieldName) {
        if(!current.has(fieldName))
            return "9999";
        return Date.checkDateString(current.getString(fieldName));
    }

    public String getTitle() {
        return simpleFields.getField(TITLE);
    }

    public void setTitle(String title) {
        simpleFields.setField(TITLE,title);
    }

    public String getSubtitle() { return simpleFields.getField(SUBJECT); }

    public void setSubtitle(String subtitle) { simpleFields.setField(SUBJECT,subtitle);
    }

    public String getAlternativeTitle() {
        return simpleFields.getField(ALT_TITLE);
    }

    public void setAlternativeTitle(String alternativeTitle) {
        simpleFields.setField(ALT_TITLE,alternativeTitle);
    }

    public String getAlternativeURL() {
        return simpleFields.getField(ALT_URL);
    }

    public void setAlternativeURL(String alternativeURL) {
        simpleFields.setField(ALT_URL,alternativeURL);
    }

    public String getLicense() {
        return simpleFields.getField(LICENSE);
    }

    public void setLicense(String license) {
        simpleFields.setField(LICENSE,license);
    }

    public String getNotesText() {
        return simpleFields.getField(NOTES_TEXT);
    }

    public void setNotesText(String notesText) {
        simpleFields.setField(NOTES_TEXT,notesText);
    }

    public String getProductionPlace() {
        return simpleFields.getField(PROD_PLACE);
    }

    public void setProductionPlace(String productionPlace) {
        simpleFields.setField(PROD_PLACE,productionPlace);
    }

    public String getDepositor() {
        return simpleFields.getField(DEPOSITOR);
    }

    public void setDepositor(String depositor) {
        simpleFields.setField(DEPOSITOR,depositor);
    }

    public String getOriginOfSources() {
        return simpleFields.getField(ORIG_OF_SOURCES);
    }

    public void setOriginOfSources(String originOfSources) {
        simpleFields.setField(ORIG_OF_SOURCES, originOfSources);
    }

    public String getCharacteristicOfSources() {
        return simpleFields.getField(CHAR_OF_SOURCES);
    }

    public void setCharacteristicOfSources(String characteristicOfSources) {
        simpleFields.setField(CHAR_OF_SOURCES,characteristicOfSources);
    }

    public String getAccessToSources() {
        return simpleFields.getField(ACCESS_TO_SOURCES);
    }

    public void setAccessToSources(String accessToSources) {
        simpleFields.setField(ACCESS_TO_SOURCES, accessToSources);
    }

    public String getProductionDate() { return simpleFields.getField(PROD_DATE);
    }

    public void setProductionDate(String productionDate) {
        simpleFields.setField(PROD_DATE, productionDate);
    }

    public String getDistributionDate() {
        return simpleFields.getField(DIST_DATE);
    }

    public void setDistributionDate(String distributionDate) {
        simpleFields.setField(DIST_DATE, distributionDate);
    }

    public String getDateOfDeposit() {
        return simpleFields.getField(DEPOS_DATE);
    }

    public void setDateOfDeposit(String dateOfDeposit) {
        simpleFields.setField(DEPOS_DATE,dateOfDeposit);
    }

    public List<OtherID> getOtherID() {
        return otherID;
    }

    public void setOtherID(List<OtherID> otherID) {
        this.otherID = otherID;
    }

    public void addOtherID(OtherID oID){this.otherID.add(oID);}

    public List<Author> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    public void addAuthor(Author author){this.author.add(author);}

    public List<DatasetContact> getDatasetContact() {
        return datasetContact;
    }

    public void setDatasetContact(List<DatasetContact> datasetContact) {
        this.datasetContact = datasetContact;
    }

    public void addDatasetContact(DatasetContact dc) {this.datasetContact.add(dc);}

    public List<Description> getDsDescription() {
        return dsDescription;
    }

    public void setDsDescription(List<Description> dsDescription) {
        this.dsDescription = dsDescription;
    }

    public void addDsDescription(Description d){this.dsDescription.add(d);}

    public List<String> getSubject() {
        return subject;
    }

    public void setSubject(List<String> subject) {
        this.subject = subject;
    }

    public void addSubject(String s){this.subject.add(s);}

    public List<Keyword> getKeyword() {
        return keyword;
    }

    public void setKeyword(List<Keyword> keyword) {
        this.keyword = keyword;
    }

    public void addKeyword(Keyword k){this.keyword.add(k);}

    public List<TopicClassification> getTopicClassification() {
        return topicClassification;
    }

    public void setTopicClassification(List<TopicClassification> topicClassification) {
        this.topicClassification = topicClassification;
    }

    public void addTopicClassification(TopicClassification tc){this.topicClassification.add(tc);}

    public List<RelatedPublication> getPublication() {
        return publication;
    }

    public void setPublication(List<RelatedPublication> publication) {
        this.publication = publication;
    }

    public void addPublication(RelatedPublication rp){this.publication.add(rp);}


    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public void addLanguage(String s){this.language.add(s);}

    public List<Producer> getProducer() {
        return producer;
    }

    public void setProducer(List<Producer> producer) {
        this.producer = producer;
    }

    public void addProducer(Producer p){this.producer.add(p);}


    public List<Contributor> getContributor() {
        return contributor;
    }

    public void setContributor(List<Contributor> contributor) {
        this.contributor = contributor;
    }

    public void addContributor(Contributor c){this.contributor.add(c);}

    public List<GrantNumber> getGrantNumber() {
        return grantNumber;
    }

    public void setGrantNumber(List<GrantNumber> grantNumber) {
        this.grantNumber = grantNumber;
    }

    public void addGrantNumber(GrantNumber gn){this.grantNumber.add(gn);}

    public List<Distributor> getDistributor() {
        return distributor;
    }

    public void setDistributor(List<Distributor> distributor) {
        this.distributor = distributor;
    }

    public void addDistributor(Distributor d){this.distributor.add(d);}

    public List<TimePeriodCovered> getTimePeriodCovered() {
        return timePeriodCovered;
    }

    public void setTimePeriodCovered(List<TimePeriodCovered> timePeriodCovered) {
        this.timePeriodCovered = timePeriodCovered;
    }

    public void addTimePeriodCovered(TimePeriodCovered tpc){this.timePeriodCovered.add(tpc);}

    public List<DateOfCollection> getDatesOfCollection() {
        return datesOfCollection;
    }

    public void setDatesOfCollection(List<DateOfCollection> datesOfCollection) {
        this.datesOfCollection = datesOfCollection;
    }

    public void addDateOfCollection(DateOfCollection dc){this.datesOfCollection.add(dc);}

    public List<String> getKindOfData() {
        return kindOfData;
    }

    public void setKindOfData(List<String> kindOfData) {
        this.kindOfData = kindOfData;
    }

    public void addKindOfData(String s){this.kindOfData.add(s);}

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public void addSeries(Series s){this.series.add(s);}

    public List<Software> getSoftware() {
        return software;
    }

    public void setSoftware(List<Software> software) {
        this.software = software;
    }

    public void addSoftware(Software s){this.software.add(s);}

    public List<String> getRelatedMaterial() {
        return relatedMaterial;
    }

    public void setRelatedMaterial(List<String> relatedMaterial) {
        this.relatedMaterial = relatedMaterial;
    }

    public void addRelatedMaterial(String s){this.relatedMaterial.add(s);}

    public List<String> getRelatedDatasets() {
        return relatedDatasets;
    }

    public void setRelatedDatasets(List<String> relatedDatasets) {
        this.relatedDatasets = relatedDatasets;
    }

    public void addRelatedDatasets(String s){this.relatedDatasets.add(s);}

    public List<String> getOtherReferences() {
        return otherReferences;
    }

    public void setOtherReferences(List<String> otherReferences) {
        this.otherReferences = otherReferences;
    }

    public void addOtherReference(String s){this.otherReferences.add(s);}

    public List<String> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<String> dataSources) {
        this.dataSources = dataSources;
    }

    public void addDataSource(String s){this.dataSources.add(s);}

    public List<GeographicCoverage> getGeographicCoverage() {
        return geographicCoverage;
    }

    public void setGeographicCoverage(List<GeographicCoverage> geographicCoverage) {
        this.geographicCoverage = geographicCoverage;
    }

    public void addGeographicCoverage(GeographicCoverage gc){this.geographicCoverage.add(gc);}

    public List<GeographicBoundingBox> getGeographicBoundingBoxes() {
        return geographicBoundingBoxes;
    }

    public void setGeographicBoundingBoxes(List<GeographicBoundingBox> geographicBoundingBoxes) {
        this.geographicBoundingBoxes = geographicBoundingBoxes;
    }

    public void addGeographicBoundingBox(GeographicBoundingBox gbb){this.geographicBoundingBoxes.add(gbb);}

    public BoundingBox getBoundingBox(){
        double north = -360;
        double south = 360;
        double east = -360;
        double west = 360;
        double temp;
        for(GeographicBoundingBox b: getGeographicBoundingBoxes()){
            temp = b.getNorthLatDub();
            north = (temp>north) ? temp : north;

            temp = b.getSouthLatDub();
            south = (temp<south) ? temp : south;

            temp = b.getEastLongDub();
            east = (temp>east) ? temp : east;

            temp = b.getWestLongDub();
            west = (temp<west) ? temp : west;
        }
        BoundingBox box = new BoundingBox();
        if(west == 360 || east == -360 || north == -360 || south == 360)
            logger.error("Something went wrong with the bounding box");
        box.setLongWest(west);
        box.setLongEast(east);
        box.setLatNorth(north);
        box.setLatSouth(south);
        return box;}
    public SimpleFields getSimpleFields() {
        return simpleFields;
    }

    public void setSimpleFields(SimpleFields simpleFields) {
        this.simpleFields = simpleFields;
    }
}
