package tests;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTimeParseTest {
    String dateOnly = "2015-07-13";
    String dateTime = "2014-07-13T11:02:21Z";
    String dateBC = "-0002-01-02";
    String dateTimeBC = "-1000-11-20T03:41:11Z";
    String yearOnly = "1020";
    String yearMonth = "3021-12";
    String yearOnlyBC = "-0509";

    @Test
    public void testDateTimeParsing(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu[-MM[-dd['T'HH:mm:ss['Z']]]]");
        TemporalAccessor date =  formatter.parseBest(dateOnly,ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
        System.out.println(date.toString());
        date =  formatter.parseBest(dateTime,ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
        System.out.println(date.toString());
        date =  formatter.parseBest(yearOnly,ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
        System.out.println(date.toString());
        date =  formatter.parseBest(yearMonth,ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
        System.out.println(date.toString());
        date =  formatter.parseBest(dateBC,ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
        System.out.println(date.toString());
        date =  formatter.parseBest(yearOnlyBC,ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
        System.out.println(date.toString());
        date =  formatter.parseBest(dateTimeBC,ZonedDateTime::from, LocalDateTime::from, LocalDate::from, YearMonth::from, Year::from);
        System.out.println(date.toString());

        //assertEquals(((TemporalAccessor) date).getYear(),"-1000");
    }
}
