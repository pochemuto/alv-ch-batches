package ch.alv.batches.jobroom.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static ch.alv.batches.company.to.master.config.CompanyToMasterConfiguration.IMPORT_COMPANIES_JOB;
import static ch.alv.batches.partnerjob.to.master.config.PartnerJobToMasterConfiguration.IMPORT_PROSPECTIVEJOBS_JOB;
import static ch.alv.batches.partnerjob.to.master.config.PartnerJobToMasterConfiguration.IMPORT_UBSJOBS_JOB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JobRoomDataApplication.class)
@WebIntegrationTest
public class JobRoomDataIntegrationTest {

    private RestTemplate restTemplate = new TestRestTemplate();

    @Value("${server.port}")
    private Integer port;

    @Test
    public void testFindAndExecuteJobs() throws InterruptedException {
        List jobNames = restTemplate.getForObject("http://localhost:" + port + "/batch/monitoring/jobs", List.class);
        assertEquals(3, jobNames.size());
        assertTrue(jobNames.contains(IMPORT_COMPANIES_JOB));
        assertTrue(jobNames.contains(IMPORT_PROSPECTIVEJOBS_JOB));
        assertTrue(jobNames.contains(IMPORT_UBSJOBS_JOB));


//        Thread.sleep(5000);
//
//        for (String jobName : jobNames) {
//            Long executionId = restTemplate
//                    .postForObject("http://localhost:" + port + "/batch/operations/jobs/" + jobName, "", Long.class);
//
//            int limit = 10;
//            do {
//                limit--;
//                Thread.sleep(1000);
//            } while (limit > 0 && !restTemplate.getForObject(
//                    "http://localhost:" + port + "/batch/operations/jobs/executions/{executionId}", String.class, executionId)
//                    .equals("FAILED"));
//
//            // FIXME: is there a bug in codecentric's spring-boot-starter-batch-web ?
//            String logPath = restTemplate.getForObject("http://localhost:" + port + "/batch/operations/jobs/executions/{executionId}/log", String.class, executionId);
//            assertTrue(logPath.length() > 20);
//        }

    }

}