package Dataverse.DataverseJSONFieldClasses;


import org.json.JSONObject;

public class Keyword extends JSONField{
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
        this.keywordVocabularyURL = keywordVocabularyURL;
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("keywordValue"):
                this.keywordValue = value;
                break;
            case("keywordVocabulary"):
                this.keywordVocabulary = value;
                break;
            case("keywordVocabularyURL"):
                this.keywordVocabularyURL = value;
                break;
            default:
                System.out.println("Something wrong with Keyword parsing. Could be URL vs URl vs URI issue.");
        }
    }
}