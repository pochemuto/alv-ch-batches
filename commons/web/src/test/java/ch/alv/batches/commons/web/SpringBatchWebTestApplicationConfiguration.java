package ch.alv.batches.commons.web;

import ch.alv.batches.commons.test.springbatch.SpringBatchTestDummyJobFactory;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class SpringBatchWebTestApplicationConfiguration {

    public final static String TEST_DUMMY_JOB_NAME = "dummyJobForWebTest";
    public final static String TEST_DUMMY_JOB_ACTION = "NOOP :-)";

    @Resource
    private SpringBatchTestDummyJobFactory dummyJobs;

    @Bean
    public Job buildDummyJob() {
       return dummyJobs.buildDummyJob(TEST_DUMMY_JOB_NAME, TEST_DUMMY_JOB_ACTION, false);
    }
}
