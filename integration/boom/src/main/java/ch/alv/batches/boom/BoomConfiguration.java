package ch.alv.batches.boom;

import ch.alv.batches.partnerjob.to.master.config.PartnerJobToMasterConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

import static ch.alv.batches.partnerjob.to.master.config.PartnerJobToMasterConfiguration.*;

/**
 *
 */
@Configuration
public class BoomConfiguration {

    public final static String BOOM_PREFIX = "boom";
    public final static String BOOM_PARTNERJOB_FULL = BOOM_PREFIX + "-full-" + IMPORT_PARTNERJOB_JOB;

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;


    @Resource(name = IMPORT_PARTNERJOB_JOB)
    private Job partnerjobsImportJob;

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
