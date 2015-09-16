package ch.alv.batches.commons.test.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
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

    public Job buildDummyJob(String jobName, String jobActionText, boolean fail) {
        Step s = buildDummyTasklet("dummyStepFor-" + jobName, jobActionText, fail);
        return buildJob(jobName, s);
    }

    public Job buildDummyJob(String jobName, int chunkSize, ItemReader reader, ItemWriter writer) {
        Step s = buildDummyReadWriteStep(jobName, chunkSize, reader, writer);
        return buildJob(jobName, s);
    }

    // TODO improve to support a Set<Step> stepList parameter, instead of a single step job.
    private Job buildJob(String jobName, Step jobStep) {
        return jobs.get(jobName)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .start(jobStep)
                .build();
    }

    private Step buildDummyTasklet(String stepName, String stepText, boolean fail) {
        Tasklet t = (contribution, context) -> {
            logger.info(stepText);
            if (fail) {
                throw new Exception("This Dummy Tasklet Step must fail, and so it did!");
            }
            return RepeatStatus.FINISHED;
        };

        return steps.get(stepName)
                .tasklet(t)
                .build();
    }


    private Step buildDummyReadWriteStep(String stepName, int chunkSize, ItemReader reader, ItemWriter writer) {
        return steps.get(stepName)
                .chunk(chunkSize)
                .reader(reader)
                .writer(writer)
                .build();
    }
}
