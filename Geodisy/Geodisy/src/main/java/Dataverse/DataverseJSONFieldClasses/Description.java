package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONObject;

public class Description extends JSONField{
    private String dsDescriptionValue, dsDescriptionDate;

    public Description() {
        this.dsDescriptionValue = "";
        this.dsDescriptionDate = "";
    }

    public String getDsDescriptionValue() {
        return dsDescriptionValue;
    }

    public void setDsDescriptionValue(String dsDescriptionValue) {
        this.dsDescriptionValue = dsDescriptionValue;
    }

    public String getDsDescriptionDate() {
        return dsDescriptionDate;
    }

    public void setDsDescriptionDate(String dsDescriptionDate) {
        this.dsDescriptionDate = dsDescriptionDate;
    }


    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch(title){
            case("dsDescriptionValue"):
                this.dsDescriptionValue = value;
                break;
            case("dsDescriptionDate"):
                this.dsDescriptionDate = value;
                break;
            default:
                System.out.println("Something wrong with Dataset Description parsing");
        }
    }
}
