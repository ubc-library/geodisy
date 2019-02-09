package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONArray;
import org.json.JSONObject;

public class GrantNumber extends JSONField{
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
                this.grantNumberAgency = value;
                break;
            case ("grantNumberValue"):
                this.grantNumberValue = value;
                break;
            default:
                System.out.println("Something went wrong with Grant Number parsing");
        }
    }
}
