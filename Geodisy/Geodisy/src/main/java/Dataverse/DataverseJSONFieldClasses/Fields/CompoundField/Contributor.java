package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

public class Contributor extends CompoundJSONField {
    private String contributorType, contributorName;

    public Contributor() {
        this.contributorType = "";
        this.contributorName = "";
    }

    public String getContributorType() {
        return contributorType;
    }

    public void setContributorType(String contributorType) {
        this.contributorType = contributorType;
    }

    public String getContributorName() {
        return contributorName;
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch(title){
            case("contributorType"):
                setContributorType(value);
                break;
            case("contributorName"):
                setContributorName(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch(title){
            case("contributorType"):
                return getContributorType();
            case("contributorName"):
                return getContributorName();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field Name";
        }
    }
}
