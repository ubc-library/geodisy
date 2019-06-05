package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.*;

public class RedshiftExtents extends CompoundJSONField {
    private float redValue, min, max;

    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch(title){
            case REDSHIFT_MINIMUM_VALUE_COVERAGE:
                setMin(value);
                break;
            case REDSHIFT_MAXIMUM_VALUE_COVERAGE:
                setMax(value);
                break;
            case REDSHIFT_VALUE_COVERAGE:
                setRedValue(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }


    @Override
    public String getField(String fieldName) {
        switch(fieldName){
            case REDSHIFT_MINIMUM_VALUE_COVERAGE:
                return stringed(min);
            case REDSHIFT_MAXIMUM_VALUE_COVERAGE:
                return stringed(max);
            case REDSHIFT_VALUE_COVERAGE:
                return stringed(redValue);
            default:
                errorParsing(this.getClass().getName(),fieldName);
                return "";
        }
    }



    private void setMin(float min) {
        this.min = min;
    }

    private void setMin(String min){
        setMin(Float.parseFloat(min));
    }

    private void setMax(float max) {
        this.max = max;
    }

    private void setMax(String max){
        setMax(Float.parseFloat(max));
    }

    private void setRedValue(float redValue) {
        this.redValue = redValue;
    }

    private void setRedValue(String value){
        setRedValue(Float.parseFloat(value));
    }
}
