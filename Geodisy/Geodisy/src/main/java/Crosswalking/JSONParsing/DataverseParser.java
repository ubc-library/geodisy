package Crosswalking.JSONParsing;

import Dataverse.DataverseJSONFieldClasses.*;
import Dataverse.DataverseJavaObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DataverseParser {

    private JSONObject dataverseJSON;
    private DataverseJavaObject dJO;

    public DataverseParser(String dataverseJSONString) {
        this.dataverseJSON = new JSONObject(dataverseJSONString);
        this.dJO = new DataverseJavaObject();
        parse();
    }

    /**
     * Simple non-compound fields are parsed in this method, but for compound fields the appropriate JSONField class
     * is created and its parseCompoundData() method is called to parse the rest.
     * @throws JSONException
     */
    private void parse() throws JSONException {
        JSONObject current = (JSONObject) dataverseJSON.get("data");
        dJO.setAlternativeURL(getValue(current,"persistentUrl"));
        dJO.setDataOfDeposit(getValue(current,"createTime"));
        dJO.setDistributionDate(getValue(current,"releaseTime"));
        dJO.setProductionDate(getValue(current,"productionDate"));
        dJO.setLicense(getValue(current,"license"));
        JSONArray currentArray = current.getJSONObject("metadataBlocks").getJSONObject("citation").getJSONArray("fields");
        for(Object o: currentArray){
            current = (JSONObject) o;
            Object value = current.get("value");
            String label = current.getString("typename");
            if(value instanceof JSONArray){
                JSONArray ja = (JSONArray) value;
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
                    default:
                        System.out.println("Something wrong parsing a compound field");
                }
            }
            else
                System.out.println("Something has gone wrong with parsing");
        }

    }



    private String getValue(JSONObject current, String fieldName) {
        if(current.has(fieldName))
            return current.getString(fieldName);
        return "";
    }

    public DataverseJavaObject getdJO() {
        return dJO;
    }

    public void setdJO(DataverseJavaObject dJO) {
        this.dJO = dJO;
    }
}
