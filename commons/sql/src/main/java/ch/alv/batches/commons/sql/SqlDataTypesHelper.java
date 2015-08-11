package ch.alv.batches.commons.sql;

import org.joda.time.LocalDate;

import java.util.Date;
import java.util.GregorianCalendar;

public class SqlDataTypesHelper {

    public static java.sql.Date fromJavaUtilDate(Date d) {
        return new java.sql.Date(d.getTime());
    }

    public static java.sql.Date fromGregorianCalendar(GregorianCalendar d) {
        return fromJavaUtilDate(d.getTime());
    }

    public static java.sql.Date fromJodaLocalDate(LocalDate d) {
        return fromJavaUtilDate(d.toDate());
    }

    public static java.sql.Date now() {
        return fromJavaUtilDate(new java.util.Date());
    }

}
