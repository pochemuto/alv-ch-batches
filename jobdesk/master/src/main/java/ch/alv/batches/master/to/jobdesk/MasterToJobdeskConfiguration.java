package ch.alv.batches.master.to.jobdesk;


import org.jooq.DSLContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.sql.SQLException;

import static ch.alv.batches.master.to.jobdesk.jooq.Tables.JOB;

/**
 * Main configuration of the master-to-jobdesk module
 *
 * @since 1.0.0
 */
@Configuration
@ComponentScan("ch.alv.batches")
@EnableBatchProcessing
public class MasterToJobdeskConfiguration {

    public final static String JOB_NAME = "loadJobdeskIndex";

    public static final String STEP_NAME = "loadJobdeskIndexStep";

    @Value("${ch.alv.batches.master.jobdesk.es.index:jobdesk}")
    private String indexName;

    @Value("${ch.alv.batches.master.jobdesk.es.type.job:job}")
    private String typeName;

    @Resource
    private DSLContext jooq;

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;

    @Bean(name = JOB_NAME)
    public Job loadJobdeskIndexJob() throws MalformedURLException, SQLException {
        return jobs.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(loadJobdeskIndexStep())
                .end()
                .build();
    }

    private Step loadJobdeskIndexStep() {
        Tasklet t = (contribution, context) -> {
            jooq.truncate(JOB).execute();
            return RepeatStatus.FINISHED;
        };

        return steps.get(STEP_NAME)
                .tasklet(t)
                .build();
    }



}
