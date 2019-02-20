package Crosswalking.JSONParsing;

import Dataverse.DataverseJSONFieldClasses.Fields.CompoundField.*;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.SimpleFields;
import Dataverse.DataverseJavaObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class DataverseParser {

    private JSONObject dataverseJSON;
    private DataverseJavaObject dJO;
    static Logger logger = LogManager.getLogger(DataverseParser.class);

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
            Object valueObject = current.get("value");
            String label = current.getString("typeName");
            if(valueObject instanceof JSONArray){
                JSONArray ja = (JSONArray) valueObject;
                switch(label){
                    case("author"):
                        Author a = new Author();
                        dJO.addAuthor((Author) a.parseCompoundData(ja));
                        break;
                    case("otherId"):
                        OtherID otherID = new OtherID();
                        dJO.addOtherID((OtherID) otherID.parseCompoundData(ja));
                        break;
                    case("datasetContact"):
                        DatasetContact dc = new DatasetContact();
                        dJO.addDatasetContact((DatasetContact) dc.parseCompoundData(ja));
                        break;
                    case("dsDescription"):
                        Description d =  new Description();
                        dJO.addDsDescription((Description) d.parseCompoundData(ja));
                        break;
                    case("keyword"):
                        Keyword k = new Keyword();
                        dJO.addKeyword((Keyword) k.parseCompoundData(ja));
                        break;
                    case("topicClassification"):
                        TopicClassification tc = new TopicClassification();
                        dJO.addTopicClassification((TopicClassification) tc.parseCompoundData(ja));
                        break;
                    case("publication"):
                        RelatedPublication rp = new RelatedPublication();
                        dJO.addPublication((RelatedPublication) rp.parseCompoundData(ja));
                        break;
                    case("producer"):
                        Producer p = new Producer();
                        dJO.addProducer((Producer) p.parseCompoundData(ja));
                        break;
                    case("contributor"):
                        Contributor c = new Contributor();
                        dJO.addContributor((Contributor) c.parseCompoundData(ja));
                        break;
                    case("grantNumber"):
                        GrantNumber gn = new GrantNumber();
                        dJO.addGrantNumber((GrantNumber) gn.parseCompoundData(ja));
                        break;
                    case("distributor"):
                        Distributor dt = new Distributor();
                        dJO.addDistributor((Distributor) dt.parseCompoundData(ja));
                        break;
                    case("timePeriodCovered"):
                        TimePeriodCovered tpc = new TimePeriodCovered();
                        dJO.addTimePeriodCovered((TimePeriodCovered) tpc.parseCompoundData(ja));
                        break;
                    case("series"):
                        Series s = new Series();
                        dJO.addSeries((Series) s.parseCompoundData(ja));
                        break;
                    case("software"):
                        Software sw = new Software();
                        dJO.addSoftware((Software) sw.parseCompoundData(ja));
                        break;
                        case("dataSource"):
                        dJO.setDataSources(getList(ja));
                        break;
                    case("kindOfData"):
                        dJO.setKindOfData(getList(ja));
                        break;
                    case("language"):
                        dJO.setLanguage(getList(ja));
                        break;
                    case("otherReference"):
                        dJO.setOtherReferences(getList(ja));
                        break;
                    case("relatedDataset"):
                        dJO.setRelatedDatasets(getList(ja));
                        break;
                    case("relatedMaterial"):
                        dJO.setRelatedMaterial(getList(ja));
                        break;
                    case("subject"):
                        dJO.setSubject(getList(ja));
                        break;
                    default:
                        logger.error("Something went wrong parsing a compound field. Label is %s", label);
                        System.out.println("Something wrong parsing a compound field");
                }
            }
            else {
                String value = valueObject.toString();
                SimpleFields sf = dJO.getSimpleFields();
                sf.setField(value, label);
                dJO.setSimpleFields(sf);
            }
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
