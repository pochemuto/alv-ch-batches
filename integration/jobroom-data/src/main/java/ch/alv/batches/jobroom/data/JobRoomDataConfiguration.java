package ch.alv.batches.jobroom.data;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 *
 */
@Configuration
public class JobRoomDataConfiguration {

//    public final static String BOOM_PREFIX = "jobroom_data";
//    public final static String BOOM_PARTNERJOB_FULL = BOOM_PREFIX + "-full-" + IMPORT_PROSPECTIVEJOBS_JOB;

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;

//    @Resource(name = IMPORT_PROSPECTIVEJOBS_JOB)
//    private Job partnerjobsImportJob;

    // TODO a meta-job that import all partners in sequence ...?
//    @Bean(name = BOOM_PARTNERJOB_FULL)
//    public Job getYup() throws IOException {
//        return jobs.get(BOOM_PARTNERJOB_FULL)
//                .incrementer(new RunIdIncrementer())
//                //.preventRestart()
//                ...........
//                .build();
//    }
}
