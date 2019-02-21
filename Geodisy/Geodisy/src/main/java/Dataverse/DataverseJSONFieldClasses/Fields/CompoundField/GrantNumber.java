package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

public class GrantNumber extends CompoundJSONField {
    private String grantNumberAgency, grantNumberValue;

    public GrantNumber() {
        this.grantNumberAgency = "";
        this.grantNumberValue = "";
    }

    public String getGrantNumberAgency() {
        return grantNumberAgency;
    }

    public void setGrantNumberAgency(String grantNumberAgency) {
        this.grantNumberAgency = grantNumberAgency;
    }

    public String getGrantNumberValue() {
        return grantNumberValue;
    }

    public void setGrantNumberValue(String grantNumberValue) {
        this.grantNumberValue = grantNumberValue;
    }

    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch (title) {
            case GRANT_NUM_AGENCY:
                setGrantNumberAgency(value);
                break;
            case GRANT_NUM_VAL:
                setGrantNumberValue(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch (title) {
            case GRANT_NUM_AGENCY:
                return getGrantNumberAgency();
            case GRANT_NUM_VAL:
                return getGrantNumberValue();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
