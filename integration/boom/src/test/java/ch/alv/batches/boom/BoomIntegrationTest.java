package ch.alv.batches.boom;

import ch.alv.batches.company.to.master.CompanyToMasterConfiguration;
import ch.alv.batches.partnerjob.to.master.PartnerJobToMasterConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertTrue;


//@IntegrationTest("server.port=9090")
//@IntegrationTest

//@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoomApplication.class)
public class BoomIntegrationTest {

    private RestTemplate restTemplate = new TestRestTemplate();

    //@Value("${server.port}")
    int port = 8999;

    @BeforeClass
    public static void startTestServer() {
        BoomApplication.main(new String[0]);
    }

    @Test
    public void testGetJobNames(){
        List jobNames = restTemplate.getForObject("http://localhost:" + port + "/batch/monitoring/jobs", List.class);
        assertTrue(jobNames.contains(CompanyToMasterConfiguration.IMPORT_COMPANIES_JOB));
        assertTrue(jobNames.contains(PartnerJobToMasterConfiguration.IMPORT_PARTNERJOB_JOB));
    }

    @Test
    public void test2(){
        List jobNames = restTemplate.getForObject("http://localhost:" + port + "/batch/monitoring/jobs", List.class);
        assertTrue(jobNames.contains(CompanyToMasterConfiguration.IMPORT_COMPANIES_JOB));
        assertTrue(jobNames.contains(PartnerJobToMasterConfiguration.IMPORT_PARTNERJOB_JOB));
    }
//    @Test
//    public void runTest() {
//        RestTemplate restTemplate = new TestRestTemplate();
//
//    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }
}