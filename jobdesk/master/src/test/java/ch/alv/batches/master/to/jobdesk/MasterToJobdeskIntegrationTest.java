package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.commons.config.MasterDatabaseSettings;
import ch.alv.batches.commons.test.TestApplicationWithEmbeddedElasticsearchNode;
import ch.alv.batches.commons.test.elasticsearch.EmbeddedElasticsearchNode;
import ch.alv.batches.commons.test.springbatch.SpringBatchTestHelper;
import ch.alv.batches.master.to.jobdesk.model.JobdeskJob;
import ch.alv.batches.master.to.jobdesk.model.JobdeskLocation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Assert;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static ch.alv.batches.master.to.jobdesk.jooq.tables.Job.JOB;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplicationWithEmbeddedElasticsearchNode.class)
@IntegrationTest
public class MasterToJobdeskIntegrationTest {

    @Resource
    private MasterDatabaseSettings masterDbSettings;

    @Value("${ch.alv.jobdesk.elasticsearch.index}")
    private String elasticsearchIndexName;

    @Resource
    private Job loadAllLocationsIntoJobdeskJob;

    @Resource
    private Job loadAllVacanciesIntoJobdeskJob;

    @Resource
    private SpringBatchTestHelper springBatchHelper;

    @Resource
    private DSLContext jooq;

    @Resource
    private Client elasticsearchClient;

    @Resource
    private EmbeddedElasticsearchNode elasticsearchNode;

    private final ClassLoader classLoader = getClass().getClassLoader();

    @Before
    public void setup() throws InterruptedException, IOException {
        initJobdeskElasticsearch(); // FIXME wait till ES is ready (yellow check)...
        initMasterDatabase();
        //springBatchHelper.initializeSpringBatchPostgresqlSchema();
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

        GetResponse response = elasticsearchClient.prepareGet(elasticsearchIndexName, LocationsToJobdeskConfiguration.ELASTICSEARCH_TYPE, "3110")
                .execute()
                .actionGet();
        Assert.assertNull(response.getSourceAsString());

        Assert.assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(loadAllLocationsIntoJobdeskJob));

//        response = client.prepareGet(elasticsearchIndexName, LocationsToJobdeskConfiguration.ELASTICSEARCH_TYPE, "3110")
//                .execute()
//                .actionGet();

        ObjectReader ow = new ObjectMapper().readerFor(JobdeskLocation.class);
        //JobdeskLocation location = ow.readValue(response.getSourceAsString());

        // todo useful checks, but discuss data handling before

//        Assert.assertEquals("3110", location.getZip());
//        Assert.assertEquals(7.555, location.getCoords().getLat(), 0.0);
//        Assert.assertEquals(46.876, location.getCoords().getLon(), 0.0);
//
//        response = client.prepareGet("jobdesk", "locations", "3158")
//                .execute()
//                .actionGet();
//
//        location = ow.readValue(response.getSourceAsString());
//        Assert.assertEquals("3158", location.getZip());
//        Assert.assertEquals(7.329, location.getCoords().getLat(), 0.0);
//        Assert.assertEquals(46.766, location.getCoords().getLon(), 0.0);

        Assert.assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(loadAllVacanciesIntoJobdeskJob));

        long total = 6136;
        long count = 0;
        int waited = 0;
        int waitTime = 10_000;
        do {
            CountResponse countResponse = elasticsearchClient.prepareCount()
                    .setIndices(elasticsearchIndexName)
                    .setTypes(VacanciesToJobdeskConfiguration.ELASTICSEARCH_TYPE)
                    .execute()
                    .actionGet();
            count = countResponse.getCount();
            Thread.sleep(waitTime);
            waited += waitTime;
        } while (count < total && waited < 60_000);
        // FIXME: a  misconfigured elasticsearch queue capacity problem with bulk insert can lead to lose entries...
        Assert.assertEquals(total, count);

        GetResponse response2 = elasticsearchClient
                .prepareGet(elasticsearchIndexName, VacanciesToJobdeskConfiguration.ELASTICSEARCH_TYPE, "6493b71705ef19b8cd3c67cd3ea6cda20432e6b0843baa525b2802abcb9b5cd454ffb4f95785bcf9")
                .execute()
                .actionGet();
        String sourceAsString = response2.getSourceAsString();
        ObjectReader ow2 = new ObjectMapper().readerFor(JobdeskJob.class);
        JobdeskJob job = ow2.readValue(sourceAsString);

        // todo useful checks, but discuss data handling before

        Assert.assertEquals("6493b71705ef19b8cd3c67cd3ea6cda20432e6b0843baa525b2802abcb9b5cd454ffb4f95785bcf9", job.getJobId());
        Assert.assertEquals("00000483601", job.getJobIdAvam());
        Assert.assertEquals("230088354", job.getJobIdEgov());
        Assert.assertEquals("Tiefbauer Q / Betoninstandhalter", job.getTitle().getDe());
//        Assert.assertEquals("Tiefbauer Q / Betoninstandhalter", job.getTitle().getFr());
//        Assert.assertEquals("Tiefbauer Q / Betoninstandhalter", job.getTitle().getIt());
//        Assert.assertEquals("Tiefbauer Q / Betoninstandhalter", job.getTitle().getEn());
//

        //Assert.assertEquals(1, job.getLocations().getLocation().size());
        //Assert.assertEquals("8355", job.getLocations().getLocation().get(0).getZip());

        //Assert.assertEquals(2, job.getLanguages().size());
        //Assert.assertEquals(20, job.getLanguages().get(0).getLanguageCode());
        //Assert.assertEquals(2, job.getLanguages().get(0).getSpokenCode());
        //Assert.assertEquals(3, job.getLanguages().get(0).getWrittenCode());

        //Assert.assertEquals(22, job.getLanguages().get(1).getLanguageCode());
        //Assert.assertEquals(4, job.getLanguages().get(1).getSpokenCode());
        //Assert.assertEquals(5, job.getLanguages().get(1).getWrittenCode());

    }

    @After
    public void cleanup() {
        elasticsearchNode.shutdown();
    }

    private void initJobdeskElasticsearch() throws IOException {

        // TODO: delete all previous indices...

//        elasticsearchClient.admin().indices().create(
//                new CreateIndexRequest(elasticsearchIndexName)
//                        .mapping(classLoader.getResource("elasticsearch-jobdesk.json").getFile()))
//                .actionGet();

        String indexDefinition = FileUtils.readFileToString(
                new File(classLoader.getResource("elasticsearch-jobdesk.json").getFile()));

        elasticsearchClient.admin().indices().create(
                new CreateIndexRequest(elasticsearchIndexName)
                        .source(indexDefinition))
                .actionGet();

//        GetIndexResponse check = elasticsearchClient.admin().indices().prepareGetIndex().setIndices(elasticsearchIndexName).execute().actionGet();

    }

    private void initMasterDatabase() throws IOException, InterruptedException {

        jooq.truncate(JOB).cascade().execute();

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
        Assert.assertEquals(true, executed);
        int result = psqlProcess.exitValue();
        Assert.assertEquals(0, result);
    }

}
