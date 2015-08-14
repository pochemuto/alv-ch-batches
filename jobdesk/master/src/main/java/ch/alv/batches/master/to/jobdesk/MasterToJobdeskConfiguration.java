package ch.alv.batches.master.to.jobdesk;


import org.jooq.DSLContext;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Main configuration of the master-to-jobdesk module
 *
 * @since 1.0.0
 */
@Configuration
@ComponentScan("ch.alv.batches.commons.sql")
@EnableBatchProcessing
public class MasterToJobdeskConfiguration {

    @Resource
    private DSLContext jooq;

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;



}
