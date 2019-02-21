package Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields;

import Crosswalking.JSONParsing.DataverseParser;
import Dataverse.DataverseJSONFieldClasses.JSONField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

public class Date extends JSONField implements DateField {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu[-MM[-dd['T'HH:mm:ss['Z']]]]");
    TemporalAccessor date;

    public Date(String date) {
       this.date = formatter.parseBest(checkDateString(date), ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
    }

    public TemporalAccessor getDate() {
        return date;
    }

    public String getDateAsString(){
        if(date==null ||date.toString()=="9999")
            return "";
        return date.toString();
    }

    @Override
    public String getField(String fieldName) {
        return getDateAsString();
    }

    public static String checkDateString(String value){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[uuuu[-MM[-dd['T'HH:mm:ss[X]]]]");
        try {
            TemporalAccessor date = formatter.parseBest(value, ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
            return date.toString();
        }catch (DateTimeParseException e){
            return "9999";
        }

    }


}
