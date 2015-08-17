package ch.alv.batches.commons.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SpringBatchTestDummyJobFactory {

    final static Logger logger = LoggerFactory.getLogger(SpringBatchTestDummyJobFactory.class);

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;

    public Job buildDummyJob(String jobName, String jobActionText) {
        return buildDummyJob(jobName, jobActionText, false);
    }

    public Job buildDummyJob(String jobName, String jobActionText, boolean fail) {
        Tasklet t = (contribution, context) -> {
            logger.info(jobActionText);
            if (fail) {
                throw new Exception("This Test Dummy Job must fail, and so it did!");
            }
            return RepeatStatus.FINISHED;
        };

        Step s = steps.get("dummyStepFor-" + jobName)
                .tasklet(t)
                .build();

        return jobs.get(jobName)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(s)
                .end()
                .build();
    }
}
