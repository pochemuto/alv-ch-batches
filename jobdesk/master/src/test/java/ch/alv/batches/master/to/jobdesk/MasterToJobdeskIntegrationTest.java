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
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
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

import static ch.alv.batches.master.to.jobdesk.jooq.Tables.JOB;
import static ch.alv.batches.master.to.jobdesk.jooq.Tables.LOCATION;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.junit.Assert.assertEquals;

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

        //
        // Location Tests
        //

        //
        // Ensure that the Testing ES Index is initially empty
        //
        GetResponse response = elasticsearchClient.prepareGet(
                elasticsearchIndexName, LocationsToJobdeskConfiguration.ELASTICSEARCH_TYPE, "3110"
        ).execute().actionGet();
        Assert.assertNull(response.getSourceAsString());

        //
        // Load the Locations
        //
        assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(loadAllLocationsIntoJobdeskJob));
        waitUntilElasticSearchBulkIsFinished(LocationsToJobdeskConfiguration.ELASTICSEARCH_TYPE, 4168);

        response = elasticsearchClient
                .prepareGet(elasticsearchIndexName, LocationsToJobdeskConfiguration.ELASTICSEARCH_TYPE, "N:46.876,E:7.555")
                .execute()
                .actionGet();
        ObjectReader ow = new ObjectMapper().readerFor(JobdeskLocation.class);
        JobdeskLocation location = ow.readValue(response.getSourceAsString());

        assertEquals("3110", location.getZip());
        assertEquals(7.555, location.getCoords().getLon(), 0.0);
        assertEquals(46.876, location.getCoords().getLat(), 0.0);

        SearchResponse search = elasticsearchClient
                .prepareSearch(elasticsearchIndexName)
                .setTypes(LocationsToJobdeskConfiguration.ELASTICSEARCH_TYPE)
                .setQuery(queryStringQuery("zip:1000"))
                .setExplain(true)
                .execute()
                .actionGet();

        for (SearchHit s : search.getHits()) {
            s.getId();
            JobdeskLocation lx = ow.readValue(s.getSourceAsString());
            assertEquals("Lausanne", lx.getMunicipalityName());
        }

        //
        // Job Vacancy Tests
        //

        //
        // Ensure that the Testing ES Index is initially empty
        //
        response = elasticsearchClient.prepareGet(
                elasticsearchIndexName,
                VacanciesToJobdeskConfiguration.ELASTICSEARCH_TYPE,
                "f37783852f2df0a8a4cad18b3c471fade0035788cad7b109765f7978d0f91a6bdd0f7e1c04065795"
        ).execute().actionGet();
        Assert.assertNull(response.getSourceAsString());

        assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(loadAllVacanciesIntoJobdeskJob));
        waitUntilElasticSearchBulkIsFinished(VacanciesToJobdeskConfiguration.ELASTICSEARCH_TYPE, 13591);

        response = elasticsearchClient.prepareGet(
                elasticsearchIndexName,
                VacanciesToJobdeskConfiguration.ELASTICSEARCH_TYPE,
                "f37783852f2df0a8a4cad18b3c471fade0035788cad7b109765f7978d0f91a6bdd0f7e1c04065795"
        ).execute().actionGet();
        String sourceAsString = response.getSourceAsString();
        ObjectReader ow2 = new ObjectMapper().readerFor(JobdeskJob.class);
        JobdeskJob job = ow2.readValue(sourceAsString);


        // FIXME id or not id?
        assertEquals("f37783852f2df0a8a4cad18b3c471fade0035788cad7b109765f7978d0f91a6bdd0f7e1c04065795", job.getJobId());
        assertEquals("00000589019", job.getJobIdAvam());
        assertEquals("230141180", job.getJobIdEgov());
        assertEquals("thailändische Köchin", job.getTitle().getDe());

        assertEquals(1, job.getLocation().getLocations().size());
        assertEquals(8897, job.getLocation().getLocations().get(0).getZip().longValue());
        assertEquals(47.086, job.getLocation().getLocations().get(0).getGeoLocation().getLat(), 0);
        assertEquals(9.305, job.getLocation().getLocations().get(0).getGeoLocation().getLon(), 0);
        
        //Assert.assertEquals(2, job.getLanguages().size());
        //Assert.assertEquals(20, job.getLanguages().get(0).getLanguageCode());
        //Assert.assertEquals(2, job.getLanguages().get(0).getSpokenCode());
        //Assert.assertEquals(3, job.getLanguages().get(0).getWrittenCode());

        //Assert.assertEquals(22, job.getLanguages().get(1).getLanguageCode());
        //Assert.assertEquals(4, job.getLanguages().get(1).getSpokenCode());
        //Assert.assertEquals(5, job.getLanguages().get(1).getWrittenCode());

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
