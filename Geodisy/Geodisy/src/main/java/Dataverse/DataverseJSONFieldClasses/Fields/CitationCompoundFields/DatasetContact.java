package Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static _Strings.DVFieldNameStrings.*;

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
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch(title){
            case DS_CONTACT_NAME:
                setDatasetContactName(value);
                break;
            case DS_CONTACT_AFFIL:
                setDatasetContactAffiliation(value);
                break;
            case DS_CONTACT_EMAIL:
                setDatasetContactEmail(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String title) {
        switch(title){
            case DS_CONTACT_NAME:
                return getDatasetContactName();
            case DS_CONTACT_AFFIL:
                return getDatasetContactAffiliation();
            case DS_CONTACT_EMAIL:
                return getDatasetContactEmail();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }

}
