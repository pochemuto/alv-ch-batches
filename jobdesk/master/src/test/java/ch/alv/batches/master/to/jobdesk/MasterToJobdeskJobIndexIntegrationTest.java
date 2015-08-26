package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.commons.test.SpringBatchTestHelper;
import ch.alv.batches.master.to.jobdesk.model.JobdeskJob;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.junit.Assert;
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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by stibe on 17.08.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MasterToJobdeskTestApplication.class)
public class MasterToJobdeskJobIndexIntegrationTest {

    @Resource(name = MasterToJobdeskConfiguration.JOB_NAME_JOBS_CREATE_FULL_INDEX)
    private Job fullJobIndexJob;

    @Resource
    SpringBatchTestHelper springBatchHelper;

    @Resource
    private Client client;

    @Test
    public void doSomething() throws IOException, InterruptedException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobException, JobParametersNotFoundException {

        client.delete(new DeleteRequest("jobdesk", "jobs", "abc"));
        GetResponse response = client.prepareGet("jobdesk", "jobs", "abc")
                .execute()
                .actionGet();

        ObjectReader ow = new ObjectMapper().reader(JobdeskJob.class);
        Assert.assertNull(response.getSourceAsString());

        ExitStatus status = springBatchHelper.runJob(fullJobIndexJob);
        Assert.assertEquals("COMPLETED", status.getExitCode());

        response = client.prepareGet("jobdesk", "jobs", "abc")
                .execute()
                .actionGet();

        JobdeskJob job = ow.readValue(response.getSourceAsString());


        // todo useful checks, but discuss data handling before

        Assert.assertEquals("abc", job.getJobId());
        Assert.assertEquals("def", job.getJobIdAvam());
        Assert.assertEquals("ghi", job.getJobIdEgov());
        Assert.assertEquals("testJob", job.getTitle().getDe());
        Assert.assertEquals("testJobFr", job.getTitle().getFr());
        Assert.assertEquals("testJobIt", job.getTitle().getIt());
        Assert.assertEquals("testJobEn", job.getTitle().getEn());


        Assert.assertEquals(1, job.getLocations().getLocation().size());
        Assert.assertEquals("8355", job.getLocations().getLocation().get(0).getZip());

        Assert.assertEquals(2, job.getLanguages().size());
        Assert.assertEquals(20, job.getLanguages().get(0).getLanguageCode());
        Assert.assertEquals(2, job.getLanguages().get(0).getSpokenCode());
        Assert.assertEquals(3, job.getLanguages().get(0).getWrittenCode());

        Assert.assertEquals(22, job.getLanguages().get(1).getLanguageCode());
        Assert.assertEquals(4, job.getLanguages().get(1).getSpokenCode());
        Assert.assertEquals(5, job.getLanguages().get(1).getWrittenCode());

    }

}
