package ch.alv.batches.commons.sql;

import ch.alv.batches.commons.test.SimpleTestApplication;
import ch.alv.batches.commons.test.springbatch.SpringBatchTestDummyJobFactory;
import ch.alv.batches.commons.test.springbatch.SpringBatchTestHelper;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static ch.alv.batches.commons.sql.jooq.Tables.TEST_JOOQ;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SimpleTestApplication.class)
@IntegrationTest
public class SpringBatchSqlIntegrationTest {

    @Resource
    private SpringBatchTestDummyJobFactory dummyJobs;

    @Resource
    private SpringBatchTestHelper springBatchHelper;

    @Resource(name = "alvchMasterDataSource")
    private DataSource database;

    @Resource(name = "alvchMasterJooq")
    private DSLContext jooq;

    @Before
    public void setup() {
        String createTable = "CREATE TABLE TEST_JOOQ (ID INTEGER PRIMARY KEY, VAL1 VARCHAR2(255 CHAR))";

        try {
            Connection c = database.getConnection();
            c.createStatement().execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void cleanup() throws SQLException {
        jooq.dropTableIfExists(TEST_JOOQ).execute();
    }

    @Test
    public void testRunJobWithoutAutoCommit() throws Exception {

        JooqBatchWriter writer = new JooqBatchWriter(jooq);
        writer.setAutoCommit(false);
        Job j = dummyJobs.buildDummyJob("jooqBatchWriterTest", 20, new TestJooqRecordReader(100), writer);

        // Validate Insertions
        Assert.assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(j));
        assertEquals(100, jooq.fetchCount(TEST_JOOQ));

        // TODO Validate Updates ?
        // ...
    }

    @Test
    public void testRunJobWithAutoCommit() throws Exception {

        JooqBatchWriter writer = new JooqBatchWriter(jooq);
        Job j = dummyJobs.buildDummyJob("jooqBatchWriterTest", 50, new TestJooqRecordReader(100), writer);

        // Validate Insertions
        Assert.assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(j));
        assertEquals(100, jooq.fetchCount(TEST_JOOQ));

        // TODO Validate Updates ?
        // ...
    }
}