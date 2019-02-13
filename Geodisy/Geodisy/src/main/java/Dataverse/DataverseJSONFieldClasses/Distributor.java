package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONObject;

public class Distributor extends JSONField{
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
        this.distributorURL = distributorURL;
    }

    public String getDistributorLogoURL() {
        return distributorLogoURL;
    }

    public void setDistributorLogoURL(String distributorLogoURL) {
        this.distributorLogoURL = distributorLogoURL;
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch(title){
            case("distributorName"):
                this.distributorName = value;
                break;
            case("distributorAffiliation"):
                this.distributorAffiliation = value;
                break;
            case("distributorAbbreviation"):
                this.distributorAbbreviation = value;
                break;
            case("distributorURL"):
                this.distributorURL = value;
                break;
            case("distributorLogoURL"):
                this.distributorLogoURL = value;
                break;
            default:
                System.out.println("Something went wrong with Distributor parsing");
        }
    }
}