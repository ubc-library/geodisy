package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

public class DatasetContact extends CompoundJSONField {
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
                setDatasetContactName(value);
                break;
            case("datasetContactAffiliation"):
                setDatasetContactAffiliation(value);
                break;
            case("datasetContactEmail"):
                setDatasetContactEmail(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch(title){
            case("datasetContactName"):
                return getDatasetContactName();
            case("datasetContactAffiliation"):
                return getDatasetContactAffiliation();
            case("datasetContactEmail"):
                return getDatasetContactEmail();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
