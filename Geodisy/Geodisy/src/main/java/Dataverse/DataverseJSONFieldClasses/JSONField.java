package Dataverse.DataverseJSONFieldClasses;

import Crosswalking.JSONParsing.DataverseParser;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class JSONField {
    protected Logger logger = LogManager.getLogger(DataverseParser.class);

    /**
     * @param value String that hopefully is a properly formated URL
     * @return Either a validly formated URL or an empty String
     */
    protected String filterURL(String value) {
        //String URLFILTER = "^((?i)((http|https):\\/\\/(www))|(www))?.(?i)([\\w]+\\.)+(\\/[\\w\\/]*)*\\??([\\&a-z1-9=]*)?";
        UrlValidator urlValidator = new UrlValidator();

        if(urlValidator.isValid(value))
            return value;
        logger.error("Malformed URL error (%s), returning blank String", value);
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
