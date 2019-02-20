package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;


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
        this.publicationURL = filterURL(publicationURL);
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("publicationCitation"):
                setPublicationCitation(value);
                break;
            case("publicationIDType"):
                setPublicationIDType(value);
                break;
            case("publicationIDNumber"):
                setPublicationIDNumber(value);
                break;
            case("publicationURL"):
                setPublicationURL(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch (title) {
            case("publicationCitation"):
                return getPublicationCitation();
            case("publicationIDType"):
                return getPublicationIDType();
            case("publicationIDNumber"):
                return getPublicationIDNumber();
            case("publicationURL"):
                return getPublicationURL();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
