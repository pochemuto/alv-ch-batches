package ch.alv.batches.commons.sql;

import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.assertTrue;

public class SqlDataTypesHelperTest {

    @Test
    public void testFromJavaUtilDate() {
        java.sql.Date sqlNow = SqlDataTypesHelper.fromJavaUtilDate(new java.util.Date());
        java.util.Date utilDate = new java.util.Date();
        assertTrue(Math.abs(sqlNow.getTime() - utilDate.getTime()) < 10);
    }

    @Test
    public void testFromGregorianCalendar() {
        java.sql.Date sqlNow = SqlDataTypesHelper.fromGregorianCalendar(new GregorianCalendar());
        java.util.Date utilDate = new java.util.Date();
        assertTrue(Math.abs(sqlNow.getTime() - utilDate.getTime()) < 10);
    }

    @Test
    public void testFromJodaLocalDate() {
        java.sql.Date sqlToday = SqlDataTypesHelper.fromJodaLocalDate(new org.joda.time.LocalDate());
        java.util.Date utilDate = new java.util.Date();
        assertTrue(utilDate.getTime() - sqlToday.getTime() < 8_6400_000);
    }

    @Test
    public void testNow() {
        java.sql.Date sqlNow = SqlDataTypesHelper.now();
        java.util.Date utilDate = new java.util.Date();
        assertTrue(Math.abs(sqlNow.getTime() - utilDate.getTime()) < 10);
    }
}