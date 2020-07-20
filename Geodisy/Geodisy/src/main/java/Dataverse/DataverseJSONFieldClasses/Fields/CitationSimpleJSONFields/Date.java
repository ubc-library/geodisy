package Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields;


import Dataverse.DataverseJSONFieldClasses.JSONField;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

/**
 * This class makes sure that the date entered as a string is actually formatted correctly and if it is not it returns
 * a date with the year 9999. When getting a date, if the date is year 9999, it returns an empty string.
 */
public class Date extends JSONField implements DateField {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu[-MM[-dd['T'HH:mm[:ss]['Z']]]]");
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

    public int getYear(){
        Year y = Year.from(date);
        //value -111111 is to indicate there was not a parsable date to get a year value from this TemporalAccessor
        if(y==null ||y.toString()=="9999")
            return -111111;
        return y.getValue();

    }


}
