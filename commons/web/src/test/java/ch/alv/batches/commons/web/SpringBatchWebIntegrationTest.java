package ch.alv.batches.commons.web;


import ch.alv.batches.commons.test.SpringBatchTestApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBatchTestApplication.class)
@WebIntegrationTest
public class SpringBatchWebIntegrationTest {

    private RestTemplate restTemplate = new TestRestTemplate();

    @Value("${server.port}")
    private Integer port;

    // FIXME: must be explicitly set in application.yml !
    @Value("${spring.batch.web.operations.base}")
    private String batchOperationBasePath;

    // FIXME: must be explicitly set in application.yml !
    @Value("${spring.batch.web.monitoring.base}")
    private String batchMonitoringBasePath;

    private String batchOperationsBaseUrl;
    private String batchMonitoringBaseUrl;

    @Before
    public void init() {
        batchOperationsBaseUrl = "http://localhost:" + port + batchOperationBasePath + "/jobs";
        batchMonitoringBaseUrl = "http://localhost:" + port + batchMonitoringBasePath;
    }

    @Test
    public void testGetJobNames(){
        List jobNames = restTemplate.getForObject(batchMonitoringBaseUrl + "/jobs", List.class);
        assertTrue(jobNames.contains(SpringBatchWebTestApplicationConfiguration.TEST_DUMMY_JOB_NAME));
    }

    @Test
    public void testRunJob() throws InterruptedException, IOException {
        Long executionId = restTemplate.postForObject(
                batchOperationsBaseUrl + "/" + SpringBatchWebTestApplicationConfiguration.TEST_DUMMY_JOB_NAME, "", Long.class);
        while (!restTemplate.getForObject(batchOperationsBaseUrl + "/executions/{executionId}", String.class, executionId).equals("COMPLETED")) {
            Thread.sleep(1000);
        }
        String logPath = restTemplate.getForObject(batchOperationsBaseUrl + "/executions/{executionId}/log", String.class, executionId);
        assertTrue(logPath.length() > 20);

        // FIXME: log files are not stored (neither in expected log directory, or effective one!!!)
        //String logFile = FileUtils.readFileToString(new File(logPath));
        //assertTrue(logFile.contains(SpringBatchWebTestApplicationConfiguration.TEST_DUMMY_JOB_ACTION));
    }
}