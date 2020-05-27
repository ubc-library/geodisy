package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;


import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.Date;
import org.json.JSONObject;

import static _Strings.DVFieldNameStrings.*;


public class TemporalExtents extends CompoundJSONField {
    Date start, end;


    @Override
    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch(title){
            case TEMPORAL_COVERAGE_START_TIME: setStart(value); break;
            case TEMPORAL_COVERAGE_STOP_TIME: setEnd(value); break;
            default: errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    public String getField(String fieldName) {
        if(fieldName.equals(TEMPORAL_COVERAGE_START_TIME))
            return start.getDateAsString();
        if(fieldName.equals((TEMPORAL_COVERAGE_STOP_TIME)))
            return end.getDateAsString();
        logger.error("Asked for weird temporal extent: " + fieldName + ".");
        return "ERROR in TemporalExtents get";
    }

    private void setStart(Date start) {
        this.start = start;
    }

    private void setStart(String start){
        setStart(new Date(start));
    }

    private void setEnd(Date end) {
        this.end = end;
    }

    private void setEnd(String end){
        setEnd(new Date(end));
    }
}
