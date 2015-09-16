package ch.alv.batches.commons.test;

import ch.alv.batches.commons.test.springbatch.SpringBatchTestDummyJobFactory;
import ch.alv.batches.commons.test.springbatch.SpringBatchTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SimpleTestApplication.class)
@IntegrationTest
public class SpringBatchTestDummyJobFactoryTest {

    private static final String JOB_NAME = "testJob";

    @Resource
    private SpringBatchTestDummyJobFactory dummyJobs;

    @Resource
    private SpringBatchTestHelper batchTestHelper;

//    @Test
//    public void testSpringBootAppStartup() {
//        SpringBatchTestApplication.main(new String[0]);
//    }

    @Test
    public void testCompletingDummyJob() throws Exception {
        String jobName = JOB_NAME + "-1";
        Job job = dummyJobs.buildDummyJob(jobName, "foo", false);
        assertEquals(jobName, job.getName());
        assertEquals(ExitStatus.COMPLETED, batchTestHelper.runJob(job));

        // TODO should assert that "foo" is logged
    }

    @Test
    public void testFailingDummyJob() throws Exception {
        String jobName = JOB_NAME + "-2";
        Job job = dummyJobs.buildDummyJob(jobName, "bar", true);
        assertEquals(jobName, job.getName());
        assertEquals(0, ExitStatus.FAILED.compareTo(batchTestHelper.runJob(job)));

        // TODO should assert that "bar" is logged
    }

}