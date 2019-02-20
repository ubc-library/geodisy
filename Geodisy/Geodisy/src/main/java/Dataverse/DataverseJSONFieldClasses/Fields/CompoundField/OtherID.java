package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

public class OtherID extends CompoundJSONField {
    private String otherIdAgency, otherIdValue;

    public OtherID() {
        this.otherIdAgency = "";
        this.otherIdValue = "";
    }

    public String getOtherIdAgency() {
        return otherIdAgency;
    }

    public void setOtherIdAgency(String otherIdAgency) {
        this.otherIdAgency = otherIdAgency;
    }

    public String getOtherIdValue() {
        return otherIdValue;
    }

    public void setOtherIdValue(String otherIdValue) {
        this.otherIdValue = otherIdValue;
    }


    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("otherIdAgency"):
                setOtherIdAgency(value);
                break;
            case("otherIdValue"):
                setOtherIdValue(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch (title) {
            case("otherIdAgency"):
                return getOtherIdAgency();
            case("otherIdValue"):
                return getOtherIdValue();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad Field name";
        }
    }
}
