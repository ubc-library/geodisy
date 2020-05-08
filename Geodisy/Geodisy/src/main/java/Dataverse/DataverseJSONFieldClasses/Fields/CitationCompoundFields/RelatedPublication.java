package Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Strings.DVFieldNameStrings.*;

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
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch (title) {
            case PUB_CITE:
                setPublicationCitation(value);
                break;
            case PUB_ID_TYPE:
                setPublicationIDType(value);
                break;
            case PUB_ID_NUM:
                setPublicationIDNumber(value);
                break;
            case PUB_URL:
                setPublicationURL(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String title) {
        switch (title) {
            case PUB_CITE:
                return getPublicationCitation();
            case PUB_ID_TYPE:
                return getPublicationIDType();
            case PUB_ID_NUM:
                return getPublicationIDNumber();
            case PUB_URL:
                return getPublicationURL();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
