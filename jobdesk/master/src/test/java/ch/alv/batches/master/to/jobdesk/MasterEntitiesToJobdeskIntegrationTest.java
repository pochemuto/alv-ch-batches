package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.master.to.jobdesk.model.JobdeskJob;
import ch.alv.batches.master.to.jobdesk.model.JobdeskLocation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MasterEntitiesToJobdeskIntegrationTest extends MasterToJobdeskIntegrationTest {


    @Resource
    private Job loadAllLocationsIntoJobdeskJob;

    @Resource
    private Job loadAllVacanciesIntoJobdeskJob;

    @Override
    protected void initJobdeskElasticsearch() throws IOException {

        // KISS: Don't create any alias, but name the target index with the import alias
        this.elasticsearchIndexName = masterToJobdeskSettings.getElasticSearchImportAlias();

        String indexDefinition = FileUtils.readFileToString(
                new File(classLoader.getResource("elasticsearch-jobdesk.json").getFile()));

        elasticsearchClient.admin().indices().create(
                new CreateIndexRequest(elasticsearchIndexName)
                        .source(indexDefinition))
                .actionGet();

    }

    // FIXME address @BeforeClass static / Spring DI conflicts, to be able to split this monolithic Test Suite
    //@Ignore // Skipped because of an "SQL integration conflict" with the other FullMaster test
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
        assertEquals(0, location.getZipAdditionalNumber().intValue());
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

        //
        // Internal Job Offer
        //

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
        assertEquals("Für die Wintersaison, vom 01. Dezember 2015 bis Ende März 2016, suchen wir zur Ergänzung unseres Teams eine thailändische Köchin. Sie bringen bereits Erfahrung mit im Kochen von thailändischen Gerichten.\n" +
                " \n" +
                "Wir freuen uns auf Ihre Bewerbung.", job.getDescription().getDe());

// FIXME        assertEquals(new LocalDate(2015, 12, 1).toDate(), job.getStartDate());
// FIXME        assertEquals(new LocalDate(2016, 4, 31).toDate(), job.getEndDate());

        assertEquals("Hinnästall", job.getCompany().getName());
        assertEquals("Gafriedastrasse 21", job.getCompany().getAddress().getStreet());
        assertEquals("Max", job.getContact().getFirstName());
        // TODO full company/contact details (with better anonymous data), quotaTo/From, ...

        assertEquals(1, job.getLocation().getLocations().size());
        assertEquals(8897, job.getLocation().getLocations().get(0).getZip().longValue());
        assertEquals(47.086, job.getLocation().getLocations().get(0).getGeoLocation().getLat(), 0);
        assertEquals(9.305, job.getLocation().getLocations().get(0).getGeoLocation().getLon(), 0);

        assertEquals("5", job.getIsco().getGroupLevel1());
        assertEquals("51", job.getIsco().getGroupLevel2());
        assertEquals("512", job.getIsco().getGroupLevel3());
        assertEquals("5120", job.getIsco().getGroupLevel4());
        assertEquals(job.getIsco().getMajorGroup(), job.getIsco().getGroupLevel1());


        //Assert.assertEquals(2, job.getLanguages().size());
        //Assert.assertEquals(20, job.getLanguages().get(0).getLanguageCode());
        //Assert.assertEquals(2, job.getLanguages().get(0).getSpokenCode());
        //Assert.assertEquals(3, job.getLanguages().get(0).getWrittenCode());

        //Assert.assertEquals(22, job.getLanguages().get(1).getLanguageCode());
        //Assert.assertEquals(4, job.getLanguages().get(1).getSpokenCode());
        //Assert.assertEquals(5, job.getLanguages().get(1).getWrittenCode());

        //
        // External Job Offer
        //
        response = elasticsearchClient.prepareGet(
                elasticsearchIndexName,
                VacanciesToJobdeskConfiguration.ELASTICSEARCH_TYPE,
                "6965898ec075064b1104ac94b406c22acfcff844b6f35cc7a56a761f28a77b2f406726a85c4b82d8"
        ).execute().actionGet();
        sourceAsString = response.getSourceAsString();
        job = ow2.readValue(sourceAsString);


        // FIXME id or not id?
        assertEquals("6965898ec075064b1104ac94b406c22acfcff844b6f35cc7a56a761f28a77b2f406726a85c4b82d8", job.getJobId());
        assertEquals(null, job.getJobIdAvam());
        assertEquals(null, job.getJobIdEgov());
        assertTrue(job.isExternal());
        assertTrue(job.isFulltime());
        assertEquals("Entwicklungsingenieur", job.getTitle().getDe());
        assertEquals("http://www.jobs.ch/de/job_content.php?iid=6595213", job.getUrl());
// FIXME assertEquals(new LocalDate(2015, 9, 23).toDate(), job.getPublicationDate());

        assertEquals("Sauter Building Control Schweiz AG", job.getCompany().getName());
        assertEquals(null, job.getCompany().getAddress());

        assertEquals(1, job.getLocation().getLocations().size());
        assertEquals(4001, job.getLocation().getLocations().get(0).getZip().longValue());
        assertEquals(47.556, job.getLocation().getLocations().get(0).getGeoLocation().getLat(), 0);
        assertEquals(7.588, job.getLocation().getLocations().get(0).getGeoLocation().getLon(), 0);

        assertEquals("2", job.getIsco().getGroupLevel1());
        assertEquals("21", job.getIsco().getGroupLevel2());
        assertEquals("214", job.getIsco().getGroupLevel3());
        assertEquals("2140", job.getIsco().getGroupLevel4());
        assertEquals(job.getIsco().getMajorGroup(), job.getIsco().getGroupLevel1());

    }

}
