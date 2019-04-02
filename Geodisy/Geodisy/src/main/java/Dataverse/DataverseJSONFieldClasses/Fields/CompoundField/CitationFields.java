package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.Date;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.SimpleFields;
import Dataverse.DataverseJSONFieldClasses.MetadataType;
import Dataverse.DataverseJavaObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

public class CitationFields extends MetadataType {
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


    public CitationFields() {
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
    }

    @Override
    public void setFields(JSONObject jo) {
        Object valueObject = jo.get(VAL);
        String label = jo.getString(TYPE_NAME);
        if(valueObject instanceof JSONArray){
            JSONArray ja = (JSONArray) valueObject;
            switch(label){
                case AUTHOR:
                    Author a = new Author();
                    addAuthor((Author) a.parseCompoundData(ja));
                    break;
                case OTHER_ID:
                    OtherID otherID = new OtherID();
                    addOtherID((OtherID) otherID.parseCompoundData(ja));
                    break;
                case DS_CONTACT:
                    DatasetContact dc = new DatasetContact();
                    addDatasetContact((DatasetContact) dc.parseCompoundData(ja));
                    break;
                case DS_DESCRIPT:
                    Description d =  new Description();
                    addDsDescription((Description) d.parseCompoundData(ja));
                    break;
                case KEYWORD:
                    Keyword k = new Keyword();
                    addKeyword((Keyword) k.parseCompoundData(ja));
                    break;
                case TOPIC_CLASS:
                    TopicClassification tc = new TopicClassification();
                    addTopicClassification((TopicClassification) tc.parseCompoundData(ja));
                    break;
                case PUBLICATION:
                    RelatedPublication rp = new RelatedPublication();
                    addPublication((RelatedPublication) rp.parseCompoundData(ja));
                    break;
                case PRODUCER:
                    Producer p = new Producer();
                    addProducer((Producer) p.parseCompoundData(ja));
                    break;
                case CONTRIB:
                    Contributor c = new Contributor();
                    addContributor((Contributor) c.parseCompoundData(ja));
                    break;
                case GRANT_NUM:
                    GrantNumber gn = new GrantNumber();
                    addGrantNumber((GrantNumber) gn.parseCompoundData(ja));
                    break;
                case DISTRIB:
                    Distributor dt = new Distributor();
                    addDistributor((Distributor) dt.parseCompoundData(ja));
                    break;
                case TIME_PER_COV:
                    TimePeriodCovered tpc = new TimePeriodCovered();
                    addTimePeriodCovered((TimePeriodCovered) tpc.parseCompoundData(ja));
                    break;
                case SERIES:
                    Series s = new Series();
                    addSeries((Series) s.parseCompoundData(ja));
                    break;
                case SOFTWARE:
                    Software sw = new Software();
                    addSoftware((Software) sw.parseCompoundData(ja));
                    break;
                case DATA_SOURCE:
                    setDataSources(getList(ja));
                    break;
                case KIND_OF_DATA:
                    setKindOfData(getList(ja));
                    break;
                case LANGUAGE:
                    setLanguage(getList(ja));
                    break;
                case OTHER_REFERENCES:
                    setOtherReferences(getList(ja));
                    break;
                case RELATED_DATASETS:
                    setRelatedDatasets(getList(ja));
                    break;
                case RELATED_MATERIAL:
                    setRelatedMaterial(getList(ja));
                    break;
                case SUBJECT:
                    setSubject(getList(ja));
                    break;
                default:
                    logger.error("Something went wrong parsing a compound field. Label is %s", label);
                    System.out.println("Something wrong parsing a compound field");
            }
        }
        else {
            String value = valueObject.toString();
            simpleFields.setField(label, value);
        }
    }

    public void setBaseFields(JSONObject current){

        simpleFields.setField(ALT_URL,parseSimpleValue( current,"persistentUrl"));
        simpleFields.setField(PUB_DATE, getValueDate(current,PUB_DATE));
        simpleFields.setField(PUBLISHER, parseSimpleValue(current,PUBLISHER));
        current = getVersionSection(current);
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

    public List<String> getList(JSONArray ja){
        List<String> answer = new LinkedList<>();
        String s;
        for (Object o: ja){
            s = (String) o;
            answer.add(s);
        }
        return answer;
    }
    @Override
    public String getField(String fieldName) {
        return null;
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

    public SimpleFields getSimpleFields() {
        return simpleFields;
    }

    public void setSimpleFields(SimpleFields simpleFields) {
        this.simpleFields = simpleFields;
    }

    public String getDOI(){
        return getSimpleFields().getDOI();
    }

    //If you update this method, also update copy in DataverseJavaObject
    public JSONObject getVersionSection(JSONObject current) {
        if(current.has("latestVersion"))
            return current.getJSONObject("latestVersion");
        else if(current.has("datasetVersion"))
            return current.getJSONObject("datasetVersion");
        else{
            logger.error("missing a _____Version field in the dataverseJson in $s", current.toString());
            return new JSONObject();
        }
    }

}
