package Dataverse.DataverseJSONFieldClasses;

import Crosswalking.JSONParsing.DataverseParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public abstract class JSONField {
    protected Logger logger = LogManager.getLogger(DataverseParser.class);

    /**
     * @param value String that hopefully is a properly formated URL
     * @return Either a validly formated URL or an empty String
     */
    protected String filterURL(String value) {
        String URLFILTER = "^((?i)((http|https):\\/\\/(www))|(www))?.(?i)([\\w]+\\.)+(\\/[\\w\\/]*)*\\??([\\&a-z1-9=]*)?";
        if(value.matches(URLFILTER))
            return value;
        logger.error("Malformed URL error (%s), returning blank String", value);
        return "";
    }

    /**
     *
     * @param current Current JSONObject to extract a date from
     * @param fieldName The name of the field that has the date
     * @return An empty String if the object doesn't have that field or the String result from filterForDate(the String at location fieldname)
     */
    protected String getValueDate(JSONObject current, String fieldName) {
        if(!current.has(fieldName))
            return "";
        return filterForDate(current.getString(fieldName));
    }

    /**
     *
     * @param value String value of a possible Date
     * @return Either the properly formatted date String or an empty String)
     */
    protected String filterForDate(String value) {
        String justYear = "-?\\d{4}";
        String yearMonthDay = "-?\\d{4}-[01]\\d-[123]\\d";
        String yearMonth = "-?\\d{4}-[01]\\d";
        String dateTime = "-?\\d{4}-[01]\\d-[123]\\dT[(0\\d)(1[012])]:[0-5]\\d:[0-5]\\dZ";
        String formatedString = value;
        if(formatedString.matches(yearMonthDay)||value.matches(justYear)||value.matches(yearMonth)||value.matches(dateTime))
            return formatedString;
        logger.error("Malformed date value (%s), returning blank String instead.", value);
        return "";
    }

    /**
     *
     * @param current Current JSONObject to extract a simple String value from (fieldname as key to String value)
     * @param fieldName The name of the field to get the String value from
     * @return String value if field exists, otherwise an empty string
     */
    protected String parseSimpleValue(JSONObject current, String fieldName) {
        if(current.has(fieldName))
            return current.getString(fieldName);
        return "";
    }

    public abstract String getField(String fieldName);

    public void errorParsing(String className, String fieldName){
        logger.error("Something wrong parsing %s. Title is %s", className, fieldName);
        System.out.println("Something wrong with " + className + " parsing.");
    }

    public void errorGettingValue(String className, String fieldName){
        logger.error("Something wrong getting field from %s. Field name is %s", className, fieldName);
        System.out.println("Something wrong with " + className + " getting value.");
    }
}
