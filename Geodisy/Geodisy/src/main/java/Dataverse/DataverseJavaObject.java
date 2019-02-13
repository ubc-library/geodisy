package Dataverse;



import Dataverse.DataverseJSONFieldClasses.Fields.*;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Java object structure to parse Dataverse Json into
 * May need to change field types for dates, URLs, and/or email addresses.
 * Also need to test to see how textboxes work with this.
 */

public class DataverseJavaObject {
    private String title, subtitle, alternativeTitle, alternativeURL, license;
    private List<OtherID> otherID;
    private List<Author> author;
    private List<DatasetContact> datasetContact;
    private List<Description> dsDescription;
    private List<String> subject;
    private List<Keyword> keyword;
    private List<TopicClassification> topicClassification;
    private List<RelatedPublication> publication;
    private String notesText;
    private List<String> language;
    private List<Producer> producer;
    private String productionDate, productionPlace;
    private List<Contributor> contributor;
    private List<GrantNumber> grantNumber;
    private List<Distributor> distributor;
    private String distributionDate, depositor, dataOfDeposit;
    private List<TimePeriodCovered> timePeriodCovered;
    private List<DateOfCollection> dateOfCollection;
    private List<String> kindOfData;
    private List<Series> series;
    private List<Software> software;
    private List<String> relatedMaterial, relatedDatasets, otherReferences, dataSources;
    private String originOfSources, characteristicOfSources, accessToSources;
    private List<GeographicCoverage> geographicCoverage;
    private List<GeographicBoundingBox> geographicBoundingBox;

    public DataverseJavaObject() {
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
        this.dateOfCollection = new LinkedList<>();
        this.kindOfData = new LinkedList<>();
        this.series  = new LinkedList<>();
        this.software = new LinkedList<>();
        this.relatedMaterial = new LinkedList<>();
        this.relatedDatasets = new LinkedList<>();
        this.otherReferences = new LinkedList<>();
        this.dataSources = new LinkedList<>();
        this.geographicCoverage = new LinkedList<>();
        this.geographicBoundingBox = new LinkedList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAlternativeTitle() {
        return alternativeTitle;
    }

    public void setAlternativeTitle(String alternativeTitle) {
        this.alternativeTitle = alternativeTitle;
    }

    public String getAlternativeURL() {
        return alternativeURL;
    }

    public void setAlternativeURL(String alternativeURL) {
        this.alternativeURL = alternativeURL;
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

    public String getNotesText() {
        return notesText;
    }

    public void setNotesText(String notesText) {
        this.notesText = notesText;
    }

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

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getProductionPlace() {
        return productionPlace;
    }

    public void setProductionPlace(String productionPlace) {
        this.productionPlace = productionPlace;
    }

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

    public String getDistributionDate() {
        return distributionDate;
    }

    public void setDistributionDate(String distributionDate) {
        this.distributionDate = distributionDate;
    }

    public String getDepositor() {
        return depositor;
    }

    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }

    public String getDataOfDeposit() {
        return dataOfDeposit;
    }

    public void setDataOfDeposit(String dataOfDeposit) {
        this.dataOfDeposit = dataOfDeposit;
    }

    public List<TimePeriodCovered> getTimePeriodCovered() {
        return timePeriodCovered;
    }

    public void setTimePeriodCovered(List<TimePeriodCovered> timePeriodCovered) {
        this.timePeriodCovered = timePeriodCovered;
    }

    public void addTimePeriodCovered(TimePeriodCovered tpc){this.timePeriodCovered.add(tpc);}

    public List<DateOfCollection> getDateOfCollection() {
        return dateOfCollection;
    }

    public void setDateOfCollection(List<DateOfCollection> dateOfCollection) {
        this.dateOfCollection = dateOfCollection;
    }

    public void addDateOfCollection(DateOfCollection dc){this.dateOfCollection.add(dc);}

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

    public String getOriginOfSources() {
        return originOfSources;
    }

    public void setOriginOfSources(String originOfSources) {
        this.originOfSources = originOfSources;
    }

    public String getCharacteristicOfSources() {
        return characteristicOfSources;
    }

    public void setCharacteristicOfSources(String characteristicOfSources) {
        this.characteristicOfSources = characteristicOfSources;
    }

    public String getAccessToSources() {
        return accessToSources;
    }

    public void setAccessToSources(String accessToSources) {
        this.accessToSources = accessToSources;
    }

    public List<GeographicCoverage> getGeographicCoverage() {
        return geographicCoverage;
    }

    public void setGeographicCoverage(List<GeographicCoverage> geographicCoverage) {
        this.geographicCoverage = geographicCoverage;
    }

    public void addGeographicCoverage(GeographicCoverage gc){this.geographicCoverage.add(gc);}

    public List<GeographicBoundingBox> getGeographicBoundingBox() {
        return geographicBoundingBox;
    }

    public void setGeographicBoundingBox(List<GeographicBoundingBox> geographicBoundingBox) {
        this.geographicBoundingBox = geographicBoundingBox;
    }

    public void addGeographicBoundingBox(GeographicBoundingBox gbb){this.geographicBoundingBox.add(gbb);}

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
