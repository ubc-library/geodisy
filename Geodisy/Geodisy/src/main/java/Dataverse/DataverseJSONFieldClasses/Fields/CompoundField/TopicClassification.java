package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

public class TopicClassification extends CompoundJSONField {
    private String topicClassValue, topicClassVocab, topicClassVocabURL;

    public TopicClassification() {
        this.topicClassValue = "";
        this.topicClassVocab = "";
        this.topicClassVocabURL = "";
    }

    public String getTopicClassValue() {
        return topicClassValue;
    }

    public void setTopicClassValue(String topicClassValue) {
        this.topicClassValue = topicClassValue;
    }

    public String getTopicClassVocab() {
        return topicClassVocab;
    }

    public void setTopicClassVocab(String topicClassVocab) {
        this.topicClassVocab = topicClassVocab;
    }

    public String getTopicClassVocabURL() {
        return topicClassVocabURL;
    }

    public void setTopicClassVocabURL(String topicClassVocabURL) {
        this.topicClassVocabURL = filterURL(topicClassVocabURL);
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("topicClassValue"):
                setTopicClassValue(value);
                break;
            case("topicClassVocab"):
                setTopicClassVocab(value);
                break;
            case("topicClassVocabURL"):
                setTopicClassVocabURL(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String fieldName) {
        switch (fieldName) {
            case("topicClassValue"):
                return getTopicClassValue();
            case("topicClassVocab"):
                return getTopicClassVocab();
            case("topicClassVocabURL"):
               return getTopicClassVocabURL();
            default:
                errorParsing(this.getClass().getName(),fieldName);
                return "Bad field name";
        }
    }
}
