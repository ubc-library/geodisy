package Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static _Strings.DVFieldNameStrings.*;

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
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch(title){
            case DISTRIB_NAME:
                setDistributorName(value);
                break;
            case DISTRIB_AFFIL:
                setDistributorAffiliation(value);
                break;
            case DISTRIB_ABRIV:
                setDistributorAbbreviation(value);
                break;
            case DISTRIB_URL:
                setDistributorURL(value);
                break;
            case DISTRIB_LOGO_URL:
                setDistributorLogoURL(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String title) {
        switch(title){
            case DISTRIB_NAME:
                return getDistributorName();
            case DISTRIB_AFFIL:
                return getDistributorAffiliation();
            case DISTRIB_ABRIV:
                return getDistributorAbbreviation();
            case DISTRIB_URL:
                return getDistributorURL();
            case DISTRIB_LOGO_URL:
                return getDistributorLogoURL();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
