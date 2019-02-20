package Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields;

import Dataverse.DataverseJSONFieldClasses.JSONField;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class Date extends JSONField implements DateField {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu[-MM[-dd['T'HH:mm:ss['Z']]]]");
    TemporalAccessor date;

    public Date(String date) {
       this.date = formatter.parseBest(date, ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
    }

    public TemporalAccessor getDate() {
        return date;
    }

    public String getDateAsString(){
        if(date==null)
            return "";
        return date.toString();
    }

    @Override
    public String getField(String fieldName) {
        return getDateAsString();
    }
}
