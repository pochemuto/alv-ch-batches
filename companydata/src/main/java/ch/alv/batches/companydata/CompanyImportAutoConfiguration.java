package ch.alv.batches.companydata;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * AutoConfig of the Company import job.
 *
 * Currently it contains logic to support the listed data providers:
 * - avg firmen (AVAMPSTS.xml / source is a remote xml file grabbed via ftp, written to table 'AVG_FIRMEN')
 *
 * @since 1.0.0
 */
@ComponentScan
@EnableBatchProcessing
@Configuration
public class CompanyImportAutoConfiguration {

    @Resource(name = "importXmlToStagingTableStep")
    private Step importXmlToStagingTableStep;

    @Resource(name = "updateExistingCompaniesStep")
    private Step updateExistingCompaniesStep;

    @Resource(name = "createNewCompaniesStep")
    private Step createNewCompaniesStep;

    @Resource(name = "markToDeleteCompaniesStep")
    private Step markToDeleteCompaniesStep;

    @Bean(name = "importCompaniesJob")
    public Job importCompaniesJob(JobBuilderFactory jobs) throws IOException, URISyntaxException {
        return jobs.get("importCompaniesJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(importXmlToStagingTableStep)
                .next(updateExistingCompaniesStep)
                .next(createNewCompaniesStep)
                .next(markToDeleteCompaniesStep)
                .end()
                .build();
    }







}
