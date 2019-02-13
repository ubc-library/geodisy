package Dataverse.DataverseJSONFieldClasses.Fields;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

public class RelatedPublication extends CompoundJSONField {
    private String publicationCitation, publicationIDType, publicationIDNumber, publicationURL;

    public RelatedPublication() {
        this.publicationCitation = "";
        this.publicationIDType = "";
        this.publicationIDNumber = "";
        this.publicationURL = "";
    }

    public String getPublicationCitation() {
        return publicationCitation;
    }

    public void setPublicationCitation(String publicationCitation) {
        this.publicationCitation = publicationCitation;
    }

    public String getPublicationIDType() {
        return publicationIDType;
    }

    public void setPublicationIDType(String publicationIDType) {
        this.publicationIDType = publicationIDType;
    }

    public String getPublicationIDNumber() {
        return publicationIDNumber;
    }

    public void setPublicationIDNumber(String publicationIDNumber) {
        this.publicationIDNumber = publicationIDNumber;
    }

    public String getPublicationURL() {
        return publicationURL;
    }

    public void setPublicationURL(String publicationURL) {
        this.publicationURL = publicationURL;
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("publicationCitation"):
                this.publicationCitation = value;
                break;
            case("publicationIDType"):
                this.publicationIDType = value;
                break;
            case("publicationIDNumber"):
                this.publicationIDNumber = value;
                break;
            case("publicationURL"):
                this.publicationURL = filterURL(value);
                break;
            default:
                logger.error("Something wrong parsing Related Publication. Title is %s", title);
                System.out.println("Something went wrong with Related Publication parsing");
        }
    }
}
