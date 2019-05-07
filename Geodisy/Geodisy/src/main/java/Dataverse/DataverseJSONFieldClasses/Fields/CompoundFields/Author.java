package Dataverse.DataverseJSONFieldClasses.Fields.CompoundFields;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import java.util.List;

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
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch(title){
            case AUTHOR_ID:
                authorIdentifier = value;
                break;
            case AUTHOR_AFFIL:
                authorAffiliation = value;
                break;
            case AUTHOR_ID_SCHEME:
                authorIdentifierScheme = value;
                break;
            case AUTHOR_NAME:
                authorName = value;
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String title) {
        switch(title){
            case AUTHOR_ID:
                return authorIdentifier;
            case AUTHOR_AFFIL:
                return authorAffiliation;
            case AUTHOR_ID_SCHEME:
                return authorIdentifierScheme;
            case AUTHOR_NAME:
                return authorName;
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }

    /*
    The Following setters are only for use in testing.

     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setAuthorAffiliation(String authorAffiliation) {
        this.authorAffiliation = authorAffiliation;
    }

    public void setAuthorIdentifierScheme(String authorIdentifierScheme) {
        this.authorIdentifierScheme = authorIdentifierScheme;
    }

    public void setAuthorIdentifier(String authorIdentifier) {
        this.authorIdentifier = authorIdentifier;
    }




}
