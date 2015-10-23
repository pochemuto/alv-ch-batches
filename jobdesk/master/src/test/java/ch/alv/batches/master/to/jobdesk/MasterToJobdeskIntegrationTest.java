package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.commons.config.MasterDatabaseSettings;
import ch.alv.batches.commons.test.TestApplicationWithEmbeddedElasticsearchNode;
import ch.alv.batches.commons.test.elasticsearch.EmbeddedElasticsearchNode;
import ch.alv.batches.commons.test.springbatch.SpringBatchTestHelper;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static ch.alv.batches.master.to.jobdesk.jooq.Tables.JOB;
import static ch.alv.batches.master.to.jobdesk.jooq.Tables.LOCATION;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplicationWithEmbeddedElasticsearchNode.class)
@IntegrationTest
public abstract class MasterToJobdeskIntegrationTest {

    @Resource
    protected MasterDatabaseSettings masterDbSettings;

    @Resource
    protected MasterToJobdeskSettings masterToJobdeskSettings;

    @Resource
    protected SpringBatchTestHelper springBatchHelper;

    @Resource
    protected DSLContext jooq;

    @Resource
    protected Client elasticsearchClient;

    @Resource
    protected EmbeddedElasticsearchNode elasticsearchNode;

    protected String elasticsearchIndexName = "undefined";

    protected final ClassLoader classLoader = getClass().getClassLoader();

    protected abstract void initJobdeskElasticsearch() throws IOException;

    protected void waitUntilElasticSearchBulkIsFinished(String elasticsearchType, long documentTotal)
            throws InterruptedException {
        long count;
        int waited = 0;
        int waitTime = 10_000;
        do {
            CountResponse countResponse = elasticsearchClient.prepareCount()
                    .setIndices(elasticsearchIndexName)
                    .setTypes(elasticsearchType)
                    .execute()
                    .actionGet();
            count = countResponse.getCount();
            Thread.sleep(waitTime);
            waited += waitTime;
            System.out.println(count + " items stored (running for " + waited/1000 + "s)");
        } while (count < documentTotal && waited < 60_000);

        // FIXME: a  misconfigured elasticsearch queue capacity problem with bulk insert can lead to lose entries...
        assertEquals(documentTotal, count);
    }

    @Before
    public void setup() throws InterruptedException, IOException {
        initJobdeskElasticsearch();
        initMasterDatabase();
    }

    @After
    public void cleanup() {
        elasticsearchNode.shutdown();
    }

    private void initMasterDatabase() throws IOException, InterruptedException {

        jooq.truncate(JOB).cascade().execute();
        jooq.truncate(LOCATION).cascade().execute();

        File file = new File(classLoader.getResource("jobs.dump").getFile());
        ProcessBuilder processBuilder = new ProcessBuilder(
                "pg_restore",
                "-h", masterDbSettings.getHost(),
                "-p", Integer.toString(masterDbSettings.getPort()),
                "-U", masterDbSettings.getUsername(),
                "-d", masterDbSettings.getName(),
                "-F", "c",
                "-a",
                file.getAbsolutePath());

        //processBuilder.directory(file.getParentFile());
        processBuilder.environment().put("PGPASSWORD", masterDbSettings.getPassword());
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectInput(file);

        Process psqlProcess = processBuilder.start();
        boolean executed = psqlProcess.waitFor(30, TimeUnit.SECONDS);
        assertEquals(true, executed);
        int result = psqlProcess.exitValue();
        assertEquals(0, result);
        // TODO assert totals in DB: location=4168, job=24443, job_loc=13591
    }

}
