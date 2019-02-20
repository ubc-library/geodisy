package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;



import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

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
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("keywordValue"):
                setKeywordValue(value);
                break;
            case("keywordVocabulary"):
                setKeywordVocabulary(value);
                break;
            case("keywordVocabularyURL"):
                setKeywordVocabularyURL(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch (title) {
            case("keywordValue"):
                return getKeywordValue();
            case("keywordVocabulary"):
                return getKeywordVocabulary();
            case("keywordVocabularyURL"):
                return getKeywordVocabularyURL();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
