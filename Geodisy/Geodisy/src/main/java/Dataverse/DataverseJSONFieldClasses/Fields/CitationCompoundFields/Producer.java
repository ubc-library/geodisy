package Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.*;

public class Producer extends CompoundJSONField {
    private String producerName, producerAffiliation, producerAbbreviation, producerURL, producerLogoURL;

    public Producer() {
        this.producerName = "";
        this.producerAffiliation = "";
        this.producerAbbreviation = "";
        this.producerURL = "";
        this.producerLogoURL = "";
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getProducerAffiliation() {
        return producerAffiliation;
    }

    public void setProducerAffiliation(String producerAffiliation) {
        this.producerAffiliation = producerAffiliation;
    }

    public String getProducerAbbreviation() {
        return producerAbbreviation;
    }

    public void setProducerAbbreviation(String producerAbbreviation) {
        this.producerAbbreviation = producerAbbreviation;
    }

    public String getProducerURL() {
        return producerURL;
    }

    public void setProducerURL(String producerURL) {
        this.producerURL = filterURL(producerURL);
    }

    public String getProducerLogoURL() {
        return producerLogoURL;
    }

    public void setProducerLogoURL(String producerLogoURL) {
        this.producerLogoURL = filterURL(producerLogoURL);
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch (title) {
            case PROD_NAME:
                setProducerName(value);
                break;
            case PROD_AFFIL:
                setProducerAffiliation(value);
                break;
            case PROD_ABBREV:
                setProducerAbbreviation(value);
                break;
            case PROD_URL:
                setProducerURL(value);
                break;
            case PROD_LOGO_URL:
                setProducerLogoURL(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String title) {
        switch (title) {
            case PROD_NAME:
                return getProducerName();
            case PROD_AFFIL:
                return getProducerAffiliation();
            case PROD_ABBREV:
                return getProducerAbbreviation();
            case PROD_URL:
                return getProducerURL();
            case PROD_LOGO_URL:
                return getProducerLogoURL();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
