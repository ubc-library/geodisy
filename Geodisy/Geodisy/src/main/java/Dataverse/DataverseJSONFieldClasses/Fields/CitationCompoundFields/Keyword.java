package Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields;



import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Strings.DVFieldNameStrings.*;

public class Keyword extends CompoundJSONField {
    private String keywordValue, keywordVocabulary, keywordVocabularyURL;

    public Keyword() {
        this.keywordValue = "";
        this.keywordVocabulary = "";
        this.keywordVocabularyURL = "";
    }

    public String getKeywordValue() {
        return keywordValue;
    }

    public void setKeywordValue(String keywordValue) {
        this.keywordValue = keywordValue;
    }

    public String getKeywordVocabulary() {
        return keywordVocabulary;
    }

    public void setKeywordVocabulary(String keywordVocabulary) {
        this.keywordVocabulary = keywordVocabulary;
    }

    public String getKeywordVocabularyURL() {
        return keywordVocabularyURL;
    }

    public void setKeywordVocabularyURL(String keywordVocabularyURL) {
        this.keywordVocabularyURL = filterURL(keywordVocabularyURL);
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch (title) {
            case KEYWORD_VAL:
                setKeywordValue(value);
                break;
            case KEYWORD_VOCAB:
                setKeywordVocabulary(value);
                break;
            case KEYWORD_VOCAB_URI:
                setKeywordVocabularyURL(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String title) {
        switch (title) {
            case KEYWORD_VAL:
                return getKeywordValue();
            case KEYWORD_VOCAB:
                return getKeywordVocabulary();
            case KEYWORD_VOCAB_URI:
                return getKeywordVocabularyURL();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
