package Dataverse.DataverseJSONFieldClasses.Fields;

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
                this.contributorType = value;
                break;
            case("contributorName"):
                this.contributorName = value;
                break;
            default:
                logger.error("Something wrong parsing Contributor. Title is %s", title);
                System.out.println("Something wrong parsing Contributor");
        }
    }
}
