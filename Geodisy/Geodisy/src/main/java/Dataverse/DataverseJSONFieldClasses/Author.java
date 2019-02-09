package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONObject;

public class Author extends JSONField{
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
                this.authorIdentifier = value;
                break;
            case "authorAffiliation":
                this.authorAffiliation = value;
                break;
            case "authorIdentifierScheme":
                this.authorIdentifierScheme = value;
                break;
            case("authorName"):
                this.authorName = value;
                break;
            default:
                System.out.println("Something wrong parsing Author");
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
