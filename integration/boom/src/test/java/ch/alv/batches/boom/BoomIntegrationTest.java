package ch.alv.batches.boom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static ch.alv.batches.cadastre.to.master.CadastreToMasterConfiguration.IMPORT_LOCATIONS;
import static ch.alv.batches.company.to.master.CompanyToMasterConfiguration.IMPORT_COMPANIES_JOB;
import static ch.alv.batches.legacy.to.master.LegacyToMasterConfiguration.IMPORT_X28_JOBS;
import static ch.alv.batches.master.to.jobdesk.LocationsToJobdeskConfiguration.BATCH_JOB_LOAD_LOCATIONS;
import static ch.alv.batches.master.to.jobdesk.VacanciesToJobdeskConfiguration.BATCH_JOB_LOAD_VACANCIES;
import static ch.alv.batches.partnerjob.to.master.PartnerJobToMasterConfiguration.IMPORT_PARTNERJOB_JOB;
import static ch.alv.batches.master.to.jobdesk.FullMasterToJobdeskConfiguration.BATCH_JOB_JOBDESK_FULLRELOAD;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoomApplication.class)
@WebIntegrationTest
public class BoomIntegrationTest {

    private RestTemplate restTemplate = new TestRestTemplate();

    @Value("${server.port}")
    private Integer port;

    @Test
    public void testFindAndExecuteJobs() throws InterruptedException {
        List<String> jobNames = restTemplate.getForObject("http://localhost:" + port + "/batch/monitoring/jobs", List.class);
        assertTrue(jobNames.contains(IMPORT_COMPANIES_JOB));
        assertTrue(jobNames.contains(IMPORT_PARTNERJOB_JOB));
        assertTrue(jobNames.contains(BATCH_JOB_LOAD_VACANCIES));
        assertTrue(jobNames.contains(BATCH_JOB_LOAD_LOCATIONS));
        assertTrue(jobNames.contains(IMPORT_X28_JOBS));
        assertTrue(jobNames.contains(IMPORT_LOCATIONS));
        assertTrue(jobNames.contains(BATCH_JOB_JOBDESK_FULLRELOAD));


        Thread.sleep(5000);

        for (String jobName : jobNames) {
            Long executionId = restTemplate
                    .postForObject("http://localhost:" + port + "/batch/operations/jobs/" + jobName, "", Long.class);

            int limit = 10;
            do {
                limit--;
                Thread.sleep(1000);
            } while (limit > 0 && !restTemplate.getForObject(
                    "http://localhost:" + port + "/batch/operations/jobs/executions/{executionId}", String.class, executionId)
                    .equals("FAILED"));

            // FIXME: is there a bug in codecentric's spring-boot-starter-batch-web ?
            String logPath = restTemplate.getForObject("http://localhost:" + port + "/batch/operations/jobs/executions/{executionId}/log", String.class, executionId);
            assertTrue(logPath.length() > 20);
        }

    }

}