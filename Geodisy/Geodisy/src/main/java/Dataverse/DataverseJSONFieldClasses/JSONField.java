package Dataverse.DataverseJSONFieldClasses;

import Crosswalking.JSONParsing.DataverseParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public abstract class JSONField {
    protected Logger logger = LogManager.getLogger(DataverseParser.class);


    protected String filterURL(String value) {
        String URLFILTER = "^((?i)((http|https):\\/\\/(www))|(www))?.(?i)([\\w]+\\.)+(\\/[\\w\\/]*)*\\??([\\&a-z1-9=]*)?";
        if(value.matches(URLFILTER))
            return value;
        logger.error("Malformed URL error (%s), returning blank String", value);
        return "";
    }

    protected String getValueDate(JSONObject current, String fieldName) {
        if(!current.has(fieldName))
            return "";
        return filterForDate(current.getString(fieldName));
    }

    protected String filterForDate(String value) {
        String justYear = "\\d{4}";
        String yearMonthDay = "\\d{4}-[01]\\d-[123]\\d";
        String formatedString = value;
        if (value.length() > 10)
            formatedString = value.substring(0, 10);
        if(formatedString.matches(yearMonthDay)||value.matches(justYear))
            return formatedString;

        logger.error("Malformed data value (%s), returning blank String instead.", value);
        return "";
    }


    protected String getValue(JSONObject current, String fieldName) {
        if(current.has(fieldName))
            return current.getString(fieldName);
        return "";
    }
}
