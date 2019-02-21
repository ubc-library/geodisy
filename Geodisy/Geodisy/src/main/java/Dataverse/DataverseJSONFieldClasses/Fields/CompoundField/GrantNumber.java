package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

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
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case ("grantNumberAgency"):
                setGrantNumberAgency(value);
                break;
            case ("grantNumberValue"):
                setGrantNumberValue(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch (title) {
            case ("grantNumberAgency"):
                return getGrantNumberAgency();
            case ("grantNumberValue"):
                return getGrantNumberValue();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
