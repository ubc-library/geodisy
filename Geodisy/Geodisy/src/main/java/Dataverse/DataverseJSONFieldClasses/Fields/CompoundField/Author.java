package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

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
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch(title){
            case "authorIdentifier":
                setAuthorIdentifier(value);
                break;
            case "authorAffiliation":
                setAuthorAffiliation(value);
                break;
            case "authorIdentifierScheme":
                setAuthorIdentifierScheme(value);
                break;
            case("authorName"):
                setAuthorName(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch(title){
            case "authorIdentifier":
                return getAuthorIdentifier();
            case "authorAffiliation":
                 return getAuthorAffiliation();
            case "authorIdentifierScheme":
                return getAuthorIdentifierScheme();
            case("authorName"):
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
