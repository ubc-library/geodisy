package Crosswalking.JSONParsing;

import Dataverse.DataverseJSONFieldClasses.Fields.CompoundField.*;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicCoverage;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.SimpleFields;
import Dataverse.DataverseJavaObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;


public class DataverseParser {

    private JSONObject dataverseJSON;
    private DataverseJavaObject dJO;
    static Logger logger = LogManager.getLogger(DataverseParser.class);
    //TODO compartimentalize Citation metadata, Geographic Metadata, etc
    public DataverseParser(String dataverseJSONString) {
        this.dataverseJSON = new JSONObject(dataverseJSONString);
        this.dJO = new DataverseJavaObject();
        parse();
    }

    /**
     * The appropriate JSONField class is created and its parseCompoundData() method or setField() method is called
     * to parse compound or simple field data, respectively.
     * @throws JSONException
     */
    private void parse() throws JSONException {
        JSONObject current = (JSONObject) dataverseJSON.get("data");
        dJO.setBaseFields(current);
        JSONArray currentArray = current.getJSONObject("latestVersion").getJSONObject("metadataBlocks").getJSONObject("citation").getJSONArray("fields");
        for(Object o: currentArray){
            current = (JSONObject) o;
            Object valueObject = current.get(VAL);
            String label = current.getString(TYPE_NAME);
            if(valueObject instanceof JSONArray){
                JSONArray ja = (JSONArray) valueObject;
                switch(label){
                    case AUTHOR:
                        Author a = new Author();
                        dJO.addAuthor((Author) a.parseCompoundData(ja));
                        break;
                    case OTHER_ID:
                        OtherID otherID = new OtherID();
                        dJO.addOtherID((OtherID) otherID.parseCompoundData(ja));
                        break;
                    case DS_CONTACT:
                        DatasetContact dc = new DatasetContact();
                        dJO.addDatasetContact((DatasetContact) dc.parseCompoundData(ja));
                        break;
                    case DS_DESCRIPT:
                        Description d =  new Description();
                        dJO.addDsDescription((Description) d.parseCompoundData(ja));
                        break;
                    case KEYWORD:
                        Keyword k = new Keyword();
                        dJO.addKeyword((Keyword) k.parseCompoundData(ja));
                        break;
                    case TOPIC_CLASS:
                        TopicClassification tc = new TopicClassification();
                        dJO.addTopicClassification((TopicClassification) tc.parseCompoundData(ja));
                        break;
                    case PUBLICATION:
                        RelatedPublication rp = new RelatedPublication();
                        dJO.addPublication((RelatedPublication) rp.parseCompoundData(ja));
                        break;
                    case PRODUCER:
                        Producer p = new Producer();
                        dJO.addProducer((Producer) p.parseCompoundData(ja));
                        break;
                    case CONTRIB:
                        Contributor c = new Contributor();
                        dJO.addContributor((Contributor) c.parseCompoundData(ja));
                        break;
                    case GRANT_NUM:
                        GrantNumber gn = new GrantNumber();
                        dJO.addGrantNumber((GrantNumber) gn.parseCompoundData(ja));
                        break;
                    case DISTRIB:
                        Distributor dt = new Distributor();
                        dJO.addDistributor((Distributor) dt.parseCompoundData(ja));
                        break;
                    case TIME_PER_COV:
                        TimePeriodCovered tpc = new TimePeriodCovered();
                        dJO.addTimePeriodCovered((TimePeriodCovered) tpc.parseCompoundData(ja));
                        break;
                    case SERIES:
                        Series s = new Series();
                        dJO.addSeries((Series) s.parseCompoundData(ja));
                        break;
                    case SOFTWARE:
                        Software sw = new Software();
                        dJO.addSoftware((Software) sw.parseCompoundData(ja));
                        break;
                        case DATA_SOURCE:
                        dJO.setDataSources(getList(ja));
                        break;
                    case KIND_OF_DATA:
                        dJO.setKindOfData(getList(ja));
                        break;
                    case LANGUAGE:
                        dJO.setLanguage(getList(ja));
                        break;
                    case OTHER_REFERENCES:
                        dJO.setOtherReferences(getList(ja));
                        break;
                    case RELATED_DATASETS:
                        dJO.setRelatedDatasets(getList(ja));
                        break;
                    case RELATED_MATERIAL:
                        dJO.setRelatedMaterial(getList(ja));
                        break;
                    case SUBJECT:
                        dJO.setSubject(getList(ja));
                        break;
                    default:
                        logger.error("Something went wrong parsing a compound field. Label is %s", label);
                        System.out.println("Something wrong parsing a compound field");
                }
            }
            else {
                String value = valueObject.toString();
                dJO.getSimpleFields().setField(label, value);
            }
        }
        String prodPlace = dJO.getProductionPlace();
        //TODO not sure about this logic or what this is even trying to do
        if(!prodPlace.matches("")){
            List<GeographicCoverage> gc = dJO.getGeographicCoverage();
        }

    }

    public List<String> getList(JSONArray ja){
        List<String> answer = new LinkedList<>();
        String s;
        for(Object o: ja){
            s = (String) o;
            answer.add(s);
        }
        return answer;
    }

    public DataverseJavaObject getdJO() {
        return dJO;
    }

    public void setdJO(DataverseJavaObject dJO) {
        this.dJO = dJO;
    }
}
