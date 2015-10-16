package ch.alv.batches.legacy.to.master;

import ch.alv.batches.commons.test.SimpleTestApplication;
import ch.alv.batches.commons.test.springbatch.SpringBatchTestHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SimpleTestApplication.class)
@IntegrationTest
public class LegacyToMasterIntegrationTest {

    @Resource
    private Job importX28Jobs;

    @Resource
    private SpringBatchTestHelper springBatchHelper;

    @Test
    public void runTest() throws Exception {
        Assert.assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(importX28Jobs));

        // FIXME Add content expectations !!! totals should be :
        // - job: 24433
        // - job_location: 13591
    }

}
