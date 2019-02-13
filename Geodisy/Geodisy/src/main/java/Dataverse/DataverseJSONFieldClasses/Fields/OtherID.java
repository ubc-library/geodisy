package Dataverse.DataverseJSONFieldClasses.Fields;

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
                this.otherIdAgency = value;
                break;
            case("otherIdValue"):
                this.otherIdValue = value;
                break;
            default:
                logger.error("Something wrong parsing Other ID. Title is %s", title);
                System.out.println("Something wrong with OtherId parsing");
        }
    }
}
