package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.commons.config.MasterDatabaseSettings;
import ch.alv.batches.commons.test.TestApplicationWithEmbeddedElasticsearchNode;
import ch.alv.batches.commons.test.elasticsearch.EmbeddedElasticsearchNode;
import ch.alv.batches.commons.test.springbatch.SpringBatchTestHelper;
import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
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
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplicationWithEmbeddedElasticsearchNode.class)
@IntegrationTest
public class FullMasterToJobdeskIntegrationTest {

    @Resource
    private MasterDatabaseSettings masterDbSettings;

    @Resource
    private MasterToJobdeskSettings masterToJobdeskSettings;

    @Resource
    private Job jobdeskFullReloadJob;

    @Resource
    private SpringBatchTestHelper springBatchHelper;

    @Resource
    private DSLContext jooq;

    @Resource
    private Client elasticsearchClient;

    @Resource
    private EmbeddedElasticsearchNode elasticsearchNode;

    private final ClassLoader classLoader = getClass().getClassLoader();

    private String elasticsearchIndexName;

    @Before
    public void setup() throws InterruptedException, IOException {
        elasticsearchIndexName = masterToJobdeskSettings.getElasticSearchIndexName();
        initMasterDatabase();
    }

    // FIXME address @BeforeClass static / Spring DI conflicts, to be able to split this monolithic Test Suite
    @Test
    public void doTests() throws
    //public void importAllLocations() throws
            IOException,
            InterruptedException,
            NoSuchJobException,
            JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException,
            JobParametersNotFoundException {

//        //
//        // Ensure that the Testing ES Index is initially empty
//        //
//        GetResponse response = elasticsearchClient.prepareGet(
//                elasticsearchIndexName, LocationsToJobdeskConfiguration.ELASTICSEARCH_TYPE, "3110"
//        ).execute().actionGet();
//        Assert.assertNull(response.getSourceAsString());

        assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(jobdeskFullReloadJob));
        waitUntilElasticSearchBulkIsFinished(VacanciesToJobdeskConfiguration.ELASTICSEARCH_TYPE, 13591);
        waitUntilElasticSearchBulkIsFinished(LocationsToJobdeskConfiguration.ELASTICSEARCH_TYPE, 4168);

        final AliasesExistResponse checkExistingAlias = elasticsearchClient.admin().indices()
                .aliasesExist(new GetAliasesRequest(masterToJobdeskSettings.getElasticSearchIndexName()))
                .actionGet();
        assertTrue(checkExistingAlias.exists());
        // TODO wait until alias is created... + timeout safety
        // TODO assert about aliases, etc

        // TODO repeat to check existing will be replaced (and old one removed)
    }

    private void waitUntilElasticSearchBulkIsFinished(String elasticsearchType, long documentTotal)
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
            System.out.println("running for " + waited + " sec: " + count + " items stored in ES");
        } while (count < documentTotal && waited < 90_000);

        // FIXME: a  misconfigured elasticsearch queue capacity problem with bulk insert can lead to lose entries...
        assertEquals(documentTotal, count);
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
