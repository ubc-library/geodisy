package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.*;

public class Author extends CompoundJSONField {
    private String authorName, authorAffiliation, authorIdentifierScheme, authorIdentifier;


    public Author() {
        this.authorName = "";
        this.authorAffiliation = "";
        this.authorIdentifierScheme = "";
        this.authorIdentifier = "";
    }
    @Override
    protected void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch(title){
            case AUTHOR_ID:
                setAuthorIdentifier(value);
                break;
            case AUTHOR_AFFIL:
                setAuthorAffiliation(value);
                break;
            case AUTHOR_ID_SCHEME:
                setAuthorIdentifierScheme(value);
                break;
            case AUTHOR_NAME:
                setAuthorName(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch(title){
            case AUTHOR_ID:
                return getAuthorIdentifier();
            case AUTHOR_AFFIL:
                 return getAuthorAffiliation();
            case AUTHOR_ID_SCHEME:
                return getAuthorIdentifierScheme();
            case AUTHOR_NAME:
                return getAuthorName();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAffiliation() {
        return authorAffiliation;
    }

    public void setAuthorAffiliation(String authorAffiliation) {
        this.authorAffiliation = authorAffiliation;
    }

    public String getAuthorIdentifierScheme() {
        return authorIdentifierScheme;
    }

    public void setAuthorIdentifierScheme(String authorIdentifierScheme) {
        this.authorIdentifierScheme = authorIdentifierScheme;
    }

    public String getAuthorIdentifier() {
        return authorIdentifier;
    }

    public void setAuthorIdentifier(String authorIdentifier) {
        this.authorIdentifier = authorIdentifier;
    }


}
