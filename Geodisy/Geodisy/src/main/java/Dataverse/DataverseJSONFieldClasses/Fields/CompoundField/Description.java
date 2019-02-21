package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.Date;
import org.json.JSONObject;

public class Description extends CompoundJSONField {
    private String dsDescriptionValue;
    private Date dsDescriptionDate;

    public Description() {
        this.dsDescriptionValue = "";
    }

    public String getDsDescriptionValue() {
        return dsDescriptionValue;
    }

    public void setDsDescriptionValue(String dsDescriptionValue) {
        this.dsDescriptionValue = dsDescriptionValue;
    }

    public String getDsDescriptionDate() {
        return dsDescriptionDate.getDateAsString();
    }

    public void setDsDescriptionDate(String dsDescriptionDate) {
        this.dsDescriptionDate = new Date(dsDescriptionDate);
    }


    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch(title){
            case("dsDescriptionValue"):
                setDsDescriptionValue(value);
                break;
            case("dsDescriptionDate"):
                setDsDescriptionDate(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch(title){
            case("dsDescriptionValue"):
                return getDsDescriptionValue();
            case("dsDescriptionDate"):
                return getDsDescriptionDate();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
