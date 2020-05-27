package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static _Strings.DVFieldNameStrings.*;

public class RedshiftExtents extends CompoundJSONField {
    private float  min, max;
    private List<Float> redValues;

    public RedshiftExtents(){
        redValues = new LinkedList<>();
    }
    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch(title){
            case REDSHIFT_MINIMUM_VALUE_COVERAGE: setMin(value); break;
            case REDSHIFT_MAXIMUM_VALUE_COVERAGE: setMax(value); break;
            case REDSHIFT_VALUE_COVERAGE: addRedValue(value); break;
            default: errorParsing(this.getClass().getName(),title);
        }
    }


    @Override
    public String getField(String fieldName) {
        switch(fieldName){
            case REDSHIFT_MINIMUM_VALUE_COVERAGE: return stringed(min);
            case REDSHIFT_MAXIMUM_VALUE_COVERAGE: return stringed(max);
            case REDSHIFT_VALUE_COVERAGE: logger.error("Tried to get a String from the Redshift Value Coverage List, should use the getListField method instead");
            default: errorParsing(this.getClass().getName(),fieldName); return "";
        }
    }

    public List getListField(String fieldName){
        if(fieldName.equals(REDSHIFT_VALUE_COVERAGE))
            return redValues;
        errorGettingValue(this.getClass().toString(),fieldName);
        return new LinkedList();
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

    private void addRedValue(float redValue) {
        this.redValues.add(redValue);
    }

    private void addRedValue(String value){
        addRedValue(Float.parseFloat(value));
    }
}
