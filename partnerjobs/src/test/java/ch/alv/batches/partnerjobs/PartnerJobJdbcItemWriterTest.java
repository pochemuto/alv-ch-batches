package ch.alv.batches.partnerjobs;

import org.junit.Assert;
import org.junit.Test;

/**
 * TODO: put some useful comment.
 *
 * @since: 1.0.0
 */
public class PartnerJobJdbcItemWriterTest {

    private static final String SQL_STATEMENT = "INSERT INTO people (ID, " +
            "BEZEICHNUNG, " +
            "BESCHREIBUNG, " +
            "BERUFSGRUPPE, " +
            "UNT_NAME, " +
            "ARBEITSORT_PLZ, " +
            "PENSUM_VON, " +
            "PENSUM_BIS, " +
            "URL_DETAIL, " +
            "ANMELDE_DATUM) " +
            "VALUES (" +
            ":id, " +
            ":title, " +
            ":description, " +
            ":jobGroup, " +
            ":companyName, " +
            ":jobLocation, " +
            ":quotaFrom, " +
            ":quotaTo, " +
            ":urlDetail, " +
            ":onlineSince)";

    @Test
    public void staticalQueryTest() {
        Assert.assertEquals(SQL_STATEMENT, PartnerJobJdbcItemWriterTest.SQL_STATEMENT);
    }

}
