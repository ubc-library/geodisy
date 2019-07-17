package Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.Date;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.SimpleCitationFields;
import Dataverse.DataverseJSONFieldClasses.MetadataType;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedList;
import java.util.List;
import static Dataverse.DVFieldNameStrings.*;

public class CitationFields extends MetadataType {
    private SimpleCitationFields simpleCitationFields;
    private List<OtherID> otherIDs;
    private List<Author> authors;
    private List<DatasetContact> datasetContacts;
    private List<Description> dsDescriptions;
    private List<Keyword> keywords;
    private List<TopicClassification> topicClassifications;
    private List<RelatedPublication> publications;
    private List<Producer> producers;
    private List<Contributor> contributors;
    private List<GrantNumber> grantNumbers;
    private List<Distributor> distributors;
    private List<TimePeriodCovered> timePeriodsCovered;
    private List<DateOfCollection> datesOfCollection;
    private Series series;
    private List<Software> softwares;
    private List<String> relatedMaterials, relatedDatasets, otherReferences, dataSources, kindsOfData, languages, subjects;
    GeoLogger logger = new GeoLogger(CitationFields.class);

    public CitationFields() {
        this.simpleCitationFields = new SimpleCitationFields();
        this.otherIDs = new LinkedList<>();
        this.authors = new LinkedList<>();
        this.datasetContacts = new LinkedList<>();
        this.dsDescriptions = new LinkedList<>();
        this.subjects = new LinkedList<>();
        this.keywords = new LinkedList<>();
        this.topicClassifications = new LinkedList<>();
        this.publications = new LinkedList<>();
        this.languages = new LinkedList<>();
        this.producers = new LinkedList<>();
        this.contributors = new LinkedList<>();
        this.grantNumbers = new LinkedList<>();
        this.distributors = new LinkedList<>();
        this.timePeriodsCovered = new LinkedList<>();
        this.datesOfCollection = new LinkedList<>();
        this.kindsOfData = new LinkedList<>();
        this.series  = new Series();
        this.softwares = new LinkedList<>();
        this.relatedMaterials = new LinkedList<>();
        this.relatedDatasets = new LinkedList<>();
        this.otherReferences = new LinkedList<>();
        this.dataSources = new LinkedList<>();
    }

    @Override
    public void setFields(JSONObject jo) {
        Object valueObject = jo.get(VAL);
        String label = jo.getString(TYPE_NAME);
        if(!(valueObject instanceof String)){
            JSONObject jobj = new JSONObject();
            JSONArray ja = new JSONArray();
            if(label.equals(SERIES))
                jobj  = (JSONObject) valueObject;
            else
                ja = (JSONArray) valueObject;
            switch(label){
                case AUTHOR:
                    Author a;
                    for(int i = 0; i<ja.length(); i++) {
                        a = new Author();
                        a.parseCompoundData((JSONObject)ja.get(i));
                        addAuthor(a);
                    }
                    break;
                case OTHER_ID:
                    OtherID otherID;
                    for(int i = 0; i<ja.length(); i++) {
                        otherID = new OtherID();
                        otherID.parseCompoundData((JSONObject)ja.get(i));
                        addOtherID(otherID);
                    }
                    break;
                case DS_CONTACT:
                    DatasetContact dc;
                    for(int i = 0; i<ja.length(); i++) {
                        dc = new DatasetContact();
                        dc.parseCompoundData((JSONObject)ja.get(i));
                        addDatasetContact(dc);
                    }
                    break;
                case DS_DESCRIPT:
                    Description d;
                    for(int i = 0; i<ja.length(); i++) {
                        d =  new Description();
                        d.parseCompoundData((JSONObject)ja.get(i));
                        addDsDescription(d);
                    }
                    break;
                case KEYWORD:
                    Keyword k;
                    for(int i = 0; i<ja.length(); i++) {
                        k = new Keyword();
                        k.parseCompoundData((JSONObject)ja.get(i));
                        addKeyword(k);
                    }
                    break;
                case TOPIC_CLASS:
                    TopicClassification tc;
                    for(int i = 0; i<ja.length(); i++) {
                        tc = new TopicClassification();
                        tc.parseCompoundData((JSONObject)ja.get(i));
                        addTopicClassification(tc);
                    }
                    break;
                case PUBLICATION:
                    RelatedPublication rp;
                    for(int i = 0; i<ja.length(); i++) {
                        rp = new RelatedPublication();
                        rp.parseCompoundData((JSONObject)ja.get(i));
                        addPublication(rp);
                    }
                    break;
                case PRODUCER:
                    Producer p;
                    for(int i = 0; i<ja.length(); i++) {
                        p = new Producer();
                        p.parseCompoundData((JSONObject)ja.get(i));
                        addProducer(p);
                    }
                    break;
                case CONTRIB:
                    Contributor c;
                    for(int i = 0; i<ja.length(); i++) {
                        c = new Contributor();
                        c.parseCompoundData((JSONObject)ja.get(i));
                        addContributor(c);
                    }
                    break;
                case GRANT_NUM:
                    GrantNumber gn;
                    for(int i = 0; i<ja.length(); i++) {
                        gn = new GrantNumber();
                        gn.parseCompoundData((JSONObject)ja.get(i));
                        addGrantNumber(gn);
                    }
                    break;
                case DISTRIB:
                    Distributor dt;
                    for(int i = 0; i<ja.length(); i++) {
                        dt = new Distributor();
                        dt.parseCompoundData((JSONObject)ja.get(i));
                        addDistributor(dt);
                    }
                    break;
                case TIME_PER_COV:
                    TimePeriodCovered tpc;
                    for(int i = 0; i<ja.length(); i++) {
                        tpc = new TimePeriodCovered();
                        tpc.parseCompoundData((JSONObject)ja.get(i));
                        addTimePeriodCovered(tpc);
                    }
                    break;

                case SOFTWARE:
                    Software sw;
                    for(int i = 0; i<ja.length(); i++) {
                        sw = new Software();
                        sw.parseCompoundData((JSONObject)ja.get(i));
                        addSoftware(sw);
                    }
                    break;
                case DATE_OF_COLLECT:
                    DateOfCollection doc;
                    for(int i = 0; i<ja.length(); i++) {
                        doc = new DateOfCollection();
                        doc.parseCompoundData((JSONObject)ja.get(i));
                        addDateOfCollection(doc);
                    }
                    break;
                case SERIES:
                    series.parseCompoundData(jobj);
                    break;
                case DATA_SOURCE:
                    setDataSources(getList(ja));
                    break;
                case KIND_OF_DATA:
                    setKindsOfData(getList(ja));
                    break;
                case LANGUAGE:
                    setLanguages(getList(ja));
                    break;
                case OTHER_REFERENCES:
                    setOtherReferences(getList(ja));
                    break;
                case RELATED_DATASETS:
                    setRelatedDatasets(getList(ja));
                    break;
                case RELATED_MATERIAL:
                    setRelatedMaterials(getList(ja));
                    break;
                case SUBJECT:
                    setSubjects(getList(ja));
                    break;
                default:
                    logger.error("Something went wrong parsing a compound field. Label is " + label);
                    System.out.println("Something wrong parsing a compound field");
            }
        }
        else {
            String value = valueObject.toString();
            simpleCitationFields.setField(label, value);
        }
    }

    public void setBaseFields(JSONObject current){

        simpleCitationFields.setField(ALT_URL,parseSimpleValue( current,"persistentUrl"));
        simpleCitationFields.setField(PUB_DATE, getValueDate(current,PUB_DATE));
        simpleCitationFields.setField(PUBLISHER, parseSimpleValue(current,PUBLISHER));
        current = getVersionSection(current);
        simpleCitationFields.setField(PROD_DATE,getValueDate(current,PROD_DATE));
        simpleCitationFields.setField(DEPOS_DATE,getValueDate(current,"createTime"));
        simpleCitationFields.setField(DIST_DATE,getValueDate(current,"releaseTime"));
        simpleCitationFields.setField(LICENSE,parseSimpleValue(current,LICENSE));
        simpleCitationFields.setField(MAJOR_VERSION,parseSimpleValue(current,MAJOR_VERSION));
        simpleCitationFields.setField(MINOR_VERSION,parseSimpleValue(current,MINOR_VERSION));

    }

    /**
     *
     * @param current Current JSONObject to extract a simple String value from (fieldname as key to String value)
     * @param fieldName The name of the field to get the String value from
     * @return String value if field exists, otherwise an empty string
     */
    protected String parseSimpleValue(JSONObject current, String fieldName) {
        if(current.has(fieldName))
            return current.get(fieldName).toString();
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
    public List getListField(String fieldName) {
        switch(fieldName){
            case AUTHOR:
                return authors;
            case OTHER_ID:
                return otherIDs;
            case DS_CONTACT:
                return datasetContacts;
            case DS_DESCRIPT:
                return dsDescriptions;
            case KEYWORD:
                return keywords;
            case TOPIC_CLASS:
                return topicClassifications;
            case PUBLICATION:
                return publications;
            case PRODUCER:
                return producers;
            case CONTRIB:
                return contributors;
            case GRANT_NUM:
                return grantNumbers;
            case DISTRIB:
                return distributors;
            case TIME_PER_COV:
                return timePeriodsCovered;
            case SOFTWARE:
                return softwares;
            case DATA_SOURCE:
                return dataSources;
            case KIND_OF_DATA:
                return kindsOfData;
            case LANGUAGE:
                return languages;
            case OTHER_REFERENCES:
                return otherReferences;
            case RELATED_DATASETS:
                return relatedDatasets;
            case RELATED_MATERIAL:
                return relatedMaterials;
            case SUBJECT:
                return subjects;
            case DATE_OF_COLLECT:
                return datesOfCollection;
            default:
                logger.error("Something went wrong parsing a compound field. Label is " + fieldName);
                System.out.println("Something wrong parsing a compound field");
                return new LinkedList();
        }
    }

    public CompoundJSONField getSeries(){
        return series;
    }

    @Override
    public String getDoi() {
        return simpleCitationFields.getDOI();
    }

    @Override
    public void setDoi(String doi) {
        simpleCitationFields.setField(DOI,doi);

    }

    @Override
    public boolean hasBB() {
        logger.debug("Somehow trying to find a bounding box in the citation metadata");
        return false;
    }

    @Override
    public BoundingBox getBoundingBox() {
        logger.debug("Somehow trying to find a bounding box in the citation metadata");
        return new BoundingBox();
    }

    public List<OtherID> getOtherIDs() {
        return otherIDs;
    }

    public void setOtherIDs(List<OtherID> otherIDs) {
        this.otherIDs = otherIDs;
    }

    public void addOtherID(OtherID oID){this.otherIDs.add(oID);}

    public void setAuthor(List<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author){this.authors.add(author);}

    public void setDatasetContacts(List<DatasetContact> datasetContacts) {
        this.datasetContacts = datasetContacts;
    }

    public void addDatasetContact(DatasetContact dc) {this.datasetContacts.add(dc);}

    public void setDsDescriptions(List<Description> dsDescriptions) {
        this.dsDescriptions = dsDescriptions;
    }

    public void addDsDescription(Description d){this.dsDescriptions.add(d);}

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(String s){this.subjects.add(s);}

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public void addKeyword(Keyword k){this.keywords.add(k);}

    public void setTopicClassifications(List<TopicClassification> topicClassifications) {
        this.topicClassifications = topicClassifications;
    }

    public void addTopicClassification(TopicClassification tc){this.topicClassifications.add(tc);}

    public void setPublications(List<RelatedPublication> publications) {
        this.publications = publications;
    }

    public void addPublication(RelatedPublication rp){this.publications.add(rp);}

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void addLanguage(String s){this.languages.add(s);}

    public void setProducers(List<Producer> producers) {
        this.producers = producers;
    }

    public void addProducer(Producer p){this.producers.add(p);}

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    public void addContributor(Contributor c){this.contributors.add(c);}

    public void setGrantNumbers(List<GrantNumber> grantNumbers) {
        this.grantNumbers = grantNumbers;
    }

    public void addGrantNumber(GrantNumber gn){this.grantNumbers.add(gn);}

    public void setDistributors(List<Distributor> distributors) {
        this.distributors = distributors;
    }

    public void addDistributor(Distributor d){this.distributors.add(d);}

    public void setTimePeriodsCovered(List<TimePeriodCovered> timePeriodsCovered) {
        this.timePeriodsCovered = timePeriodsCovered;
    }

    public void addTimePeriodCovered(TimePeriodCovered tpc){this.timePeriodsCovered.add(tpc);}

    public void setDatesOfCollection(List<DateOfCollection> datesOfCollection) {
        this.datesOfCollection = datesOfCollection;
    }

    public void addDateOfCollection(DateOfCollection dc){this.datesOfCollection.add(dc);}

    public void setKindsOfData(List<String> kindsOfData) {
        this.kindsOfData = kindsOfData;
    }

    public void addKindOfData(String s){this.kindsOfData.add(s);}

    public void setSeries(Series series) {
        this.series = series;
    }

    public void setSoftwares(List<Software> softwares) {
        this.softwares = softwares;
    }

    public void addSoftware(Software s){this.softwares.add(s);}

    public void setRelatedMaterials(List<String> relatedMaterials) {
        this.relatedMaterials = relatedMaterials;
    }

    public void addRelatedMaterial(String s){this.relatedMaterials.add(s);}

    public void setRelatedDatasets(List<String> relatedDatasets) {
        this.relatedDatasets = relatedDatasets;
    }

    public void addRelatedDatasets(String s){this.relatedDatasets.add(s);}

    public void setOtherReferences(List<String> otherReferences) {
        this.otherReferences = otherReferences;
    }

    public void addOtherReference(String s){this.otherReferences.add(s);}

    public void setDataSources(List<String> dataSources) {
        this.dataSources = dataSources;
    }

    public void addDataSource(String s){this.dataSources.add(s);}

    public SimpleCitationFields getSimpleCitationFields() {
        return simpleCitationFields;
    }

    public void setSimpleCitationFields(SimpleCitationFields simpleCitationFields) {
        this.simpleCitationFields = simpleCitationFields;
    }

    public String getDOI(){
        return getSimpleCitationFields().getDOI();
    }

    //If you update this method, also update copy in DataverseJavaObject
    public JSONObject getVersionSection(JSONObject current) {
        if(current.has("latestVersion"))
            return current.getJSONObject("latestVersion");
        else if(current.has("datasetVersion"))
            return current.getJSONObject("datasetVersion");
        else{
            logger.error("missing a _____Version field in the dataverseJson in " + current.toString());
            return new JSONObject();
        }
    }
    public int getVersion(){
        return getSimpleCitationFields().getVersion();
    }

}
