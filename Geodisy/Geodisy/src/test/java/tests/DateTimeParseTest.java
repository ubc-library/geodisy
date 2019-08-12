package tests;

import org.junit.Test;

import static Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.Date.checkDateString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTimeParseTest{
    String dateOnly = "2015-07-13";
    String dateTime = "2014-07-13T11:02:21Z";
    String dateBC = "-0002-01-02";
    String dateTimeBC = "-1000-11-20T03:41:11Z";
    String yearOnly = "1020";
    String yearMonth = "3021-12";
    String yearOnlyBC = "-0509";
    String badDate = "Product Date";


    @Test
    public void testDateTimeParsing(){

        assertEquals(checkDateString(dateOnly), "2015-07-13");
        assertEquals(checkDateString(dateTime), "2014-07-13T11:02:21Z");
        assertEquals(checkDateString(dateBC), "-0002-01-02");
        assertEquals(checkDateString(dateTimeBC), "-1000-11-20T03:41:11Z");
        assertEquals(checkDateString(yearOnly), "1020");
        assertEquals(checkDateString(yearOnlyBC), "-509");
        assertEquals(checkDateString(yearMonth), "3021-12");
        assertEquals(checkDateString(badDate), "9999");

    }
}
