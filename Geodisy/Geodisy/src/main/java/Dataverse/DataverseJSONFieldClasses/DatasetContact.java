package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONObject;

public class DatasetContact extends JSONField{
    private String datasetContactName, datasetContactAffiliation, datasetContactEmail;

    public DatasetContact() {
        this.datasetContactName = "";
        this.datasetContactAffiliation = "";
        this.datasetContactEmail = "";
    }

    public String getDatasetContactName() {
        return datasetContactName;
    }

    public void setDatasetContactName(String datasetContactName) {
        this.datasetContactName = datasetContactName;
    }

    public String getDatasetContactAffiliation() {
        return datasetContactAffiliation;
    }

    public void setDatasetContactAffiliation(String datasetContactAffiliation) {
        this.datasetContactAffiliation = datasetContactAffiliation;
    }

    public String getDatasetContactEmail() {
        return datasetContactEmail;
    }

    public void setDatasetContactEmail(String datasetContactEmail) {
        this.datasetContactEmail = datasetContactEmail;
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch(title){
            case("datasetContactName"):
                this.datasetContactName = value;
                break;
            case("datasetContactAffiliation"):
                this.datasetContactAffiliation = value;
                break;
            case("datasetContactEmail"):
                this.datasetContactEmail = value;
            default:
                System.out.println("Something wrong with DatasetContact parsing");
        }
    }
}
