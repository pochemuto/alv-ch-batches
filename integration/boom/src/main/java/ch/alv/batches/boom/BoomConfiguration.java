package ch.alv.batches.boom;

import ch.alv.batches.legacy.to.master.LegacyToMasterConfiguration;
import ch.alv.batches.master.to.jobdesk.FullMasterToJobdeskConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 *
 */
@Configuration
public class BoomConfiguration {

    public final static String BOOM_PREFIX = "boom";
    public final static String BOOM_JOBDESK_FULLRELOAD = BOOM_PREFIX + "ReloadJobdeskJob";

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;

    @Resource(name = LegacyToMasterConfiguration.IMPORT_X28_JOBS)
    private Job legacyDataImportJob;

    @Resource(name = FullMasterToJobdeskConfiguration.BATCH_JOB_JOBDESK_FULLRELOAD)
    private Job jobdeskFullReloadJob;

    @Bean(name = BOOM_JOBDESK_FULLRELOAD)
    public Job getYup() throws IOException {
        return jobs.get(BOOM_JOBDESK_FULLRELOAD)
                .incrementer(new RunIdIncrementer())
                //.preventRestart()
                .start(steps.get(BOOM_JOBDESK_FULLRELOAD + "-" + legacyDataImportJob.getName())
                        .job(legacyDataImportJob).build())
                .next(steps.get(BOOM_JOBDESK_FULLRELOAD + "-" + jobdeskFullReloadJob.getName())
                        .job(jobdeskFullReloadJob).build())
                .build();
    }
}
