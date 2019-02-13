package Crosswalking.JSONParsing;

import Dataverse.DataverseJSONFieldClasses.*;
import Dataverse.DataverseJavaObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
     * Simple non-compound fields are parsed in this method, but for compound fields the appropriate JSONField class
     * is created and its parseCompoundData() method is called to parse the rest.
     * @throws JSONException
     */
    private void parse() throws JSONException {
        JSONObject current = (JSONObject) dataverseJSON.get("data");
        dJO.setAlternativeURL(getValue(current,"persistentUrl"));
        dJO.setProductionDate(getValueDate(current,"productionDate"));
        current = current.getJSONObject("latestVersion");
        dJO.setDataOfDeposit(getValueDate(current,"createTime"));
        dJO.setDistributionDate(getValueDate(current,"releaseTime"));
        dJO.setLicense(getValue(current,"license"));
        JSONArray currentArray = current.getJSONObject("metadataBlocks").getJSONObject("citation").getJSONArray("fields");
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
                    default:
                        System.out.println("Something wrong parsing a compound field");
                }
            }
            else {
                String value = current.getString("value");
                switch(label) {
                    case("title"):
                        dJO.setTitle(value);
                        break;
                    case("subtitle"):
                        dJO.setSubtitle(value);
                        break;
                    case("alternativeTitle"):
                        dJO.setAlternativeTitle(value);
                        break;
                    case("alternativeURL"):
                        dJO.setAlternativeURL(filterURL(value));
                        break;
                    case("license"):
                        dJO.setLicense(value);
                        break;
                    case("notesText"):
                        dJO.setNotesText(value);
                        break;
                    case("productionDate"):
                        dJO.setProductionDate(filterForDate(value));
                        break;
                    case("productionPlace"):
                        dJO.setProductionPlace(value);
                        break;
                    case("distributionDate"):
                        dJO.setDistributionDate(filterForDate(value));
                        break;
                    case("depositor"):
                        dJO.setDepositor(value);
                        break;
                    case("dateOfDeposit"):
                        dJO.setDataOfDeposit(filterForDate(value));
                        break;
                    case("originOfSources"):
                        dJO.setOriginOfSources(value);
                        break;
                    case("characteristicOfSources"):
                        dJO.setCharacteristicOfSources(value);
                        break;
                    case("accessToSources"):
                        dJO.setAccessToSources(value);
                        break;
                    default:
                        logger.error("Something has gone wrong with parsing. Label found is : %s", label);
                        System.out.println("Something has gone wrong with parsing. Label found is : " + label);
                }
            }
        }

    }

    static public String filterURL(String value) {
        String URLFILTER = "^((?i)((http|https):\\/\\/(www))|(www))?.(?i)([\\w]+\\.)+(\\/[\\w\\/]*)*\\??([\\&a-z1-9=]*)?";
        if(value.matches(URLFILTER))
            return value;
        logger.error("Malformed URL error (%s), returning blank String", value);
        return "";
    }

    private String getValueDate(JSONObject current, String fieldName) {
        if(!current.has(fieldName))
            return "";
        return filterForDate(current.getString(fieldName));
    }

    static public String filterForDate(String value) {
        String justYear = "\\d{4}";
        String yearMonthDay = "\\d{4}-[01]\\d-[123]\\d";
        String formatedString = value;
        if (value.length() > 10)
            formatedString = value.substring(0, 10);
        if(formatedString.matches(yearMonthDay)||value.matches(justYear))
            return formatedString;

        logger.error("Malformed data value (%s), returning blank String instead.", value);
        return "";
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
