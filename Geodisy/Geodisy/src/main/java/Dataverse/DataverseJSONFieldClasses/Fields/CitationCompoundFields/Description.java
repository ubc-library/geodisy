package Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.Date;
import org.json.JSONObject;

import static Strings.DVFieldNameStrings.*;

public class Description extends CompoundJSONField {
    private String dsDescriptionValue;
    private Date dsDescriptionDate;

    public Description() {
        this.dsDescriptionValue = "";
    }

    public Description(String value){
        this.dsDescriptionValue=value;
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
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch(title){
            case DS_DESCRIPT_VAL:
                setDsDescriptionValue(value);
                break;
            case DS_DESCRIPT_DATE:
                setDsDescriptionDate(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String title) {
        switch(title){
            case DS_DESCRIPT_VAL:
                return getDsDescriptionValue();
            case DS_DESCRIPT_DATE:
                return getDsDescriptionDate();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
