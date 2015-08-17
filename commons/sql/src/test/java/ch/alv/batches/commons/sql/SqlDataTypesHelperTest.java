package ch.alv.batches.commons.sql;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SqlDataTypesHelperTest {

    @Test
    public void testFromJavaUtilDate() {

    }

    @Test
    public void testFromGregorianCalendar() {

    }

    @Test
    public void testFromJodaLocalDate() {

    }

    @Test
    public void testNow() {
        java.sql.Date sqlNow = SqlDataTypesHelper.now();
        java.util.Date utilDate = new java.util.Date();
        assertTrue(Math.abs(sqlNow.getTime() - utilDate.getTime()) < 10);
    }
}