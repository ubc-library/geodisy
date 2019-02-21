package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

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
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("producerName"):
                setProducerName(value);
                break;
            case("producerAffiliation"):
                setProducerAffiliation(value);
                break;
            case("producerAbbreviation"):
                setProducerAbbreviation(value);
                break;
            case("producerURL"):
                setProducerURL(value);
                break;
            case("producerLogoURL"):
                setProducerLogoURL(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch (title) {
            case("producerName"):
                return getProducerName();
            case("producerAffiliation"):
                return getProducerAffiliation();
            case("producerAbbreviation"):
                return getProducerAbbreviation();
            case("producerURL"):
                return getProducerURL();
            case("producerLogoURL"):
                return getProducerLogoURL();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
