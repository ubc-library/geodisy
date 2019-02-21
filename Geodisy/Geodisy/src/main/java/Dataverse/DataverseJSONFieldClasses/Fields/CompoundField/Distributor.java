package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

public class Distributor extends CompoundJSONField {
    private String distributorName, distributorAffiliation, distributorAbbreviation, distributorURL, distributorLogoURL;

    public Distributor() {
        this.distributorName = "";
        this.distributorAffiliation = "";
        this.distributorAbbreviation = "";
        this.distributorURL = "";
        this.distributorLogoURL = "";
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorAffiliation() {
        return distributorAffiliation;
    }

    public void setDistributorAffiliation(String distributorAffiliation) {
        this.distributorAffiliation = distributorAffiliation;
    }

    public String getDistributorAbbreviation() {
        return distributorAbbreviation;
    }

    public void setDistributorAbbreviation(String distributorAbbreviation) {
        this.distributorAbbreviation = distributorAbbreviation;
    }

    public String getDistributorURL() {
        return distributorURL;
    }

    public void setDistributorURL(String distributorURL) {
        this.distributorURL = filterURL(distributorURL);
    }

    public String getDistributorLogoURL() {
        return distributorLogoURL;
    }

    public void setDistributorLogoURL(String distributorLogoURL) {
        this.distributorLogoURL = filterURL(distributorLogoURL);
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch(title){
            case("distributorName"):
                setDistributorName(value);
                break;
            case("distributorAffiliation"):
                setDistributorAffiliation(value);
                break;
            case("distributorAbbreviation"):
                setDistributorAbbreviation(value);
                break;
            case("distributorURL"):
                setDistributorURL(value);
                break;
            case("distributorLogoURL"):
                setDistributorLogoURL(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch(title){
            case("distributorName"):
                return getDistributorName();
            case("distributorAffiliation"):
                return getDistributorAffiliation();
            case("distributorAbbreviation"):
                return getDistributorAbbreviation();
            case("distributorURL"):
                return getDistributorURL();
            case("distributorLogoURL"):
                return getDistributorLogoURL();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
