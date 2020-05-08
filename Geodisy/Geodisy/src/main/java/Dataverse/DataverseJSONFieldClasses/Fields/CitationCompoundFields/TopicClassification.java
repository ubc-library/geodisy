package Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;

import org.json.JSONObject;

import static Strings.DVFieldNameStrings.*;

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
            case TOPIC_CLASS_VAL:
                setTopicClassValue(value);
                break;
            case TOPIC_CLASS_VOCAB:
                setTopicClassVocab(value);
                break;
            case TOPIC_CLASS_VOCAB_URI:
                setTopicClassVocabURL(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String fieldName) {
        switch (fieldName) {
            case TOPIC_CLASS_VAL:
                return getTopicClassValue();
            case TOPIC_CLASS_VOCAB:
                return getTopicClassVocab();
            case TOPIC_CLASS_VOCAB_URI:
               return getTopicClassVocabURL();
            default:
                errorParsing(this.getClass().getName(),fieldName);
                return "Bad field name";
        }
    }
}
