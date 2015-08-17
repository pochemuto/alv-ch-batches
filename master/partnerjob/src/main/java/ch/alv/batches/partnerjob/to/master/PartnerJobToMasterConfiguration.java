package ch.alv.batches.partnerjob.to.master;

import ch.alv.batches.commons.sql.JooqBatchWriter;
import ch.alv.batches.partnerjob.to.master.converter.ProspectiveJobToAdminJobConverter;
import ch.alv.batches.partnerjob.to.master.jaxb.ProspectiveJob;
import ch.alv.batches.partnerjob.to.master.jooq.tables.records.OsteAdminRecord;
import org.jooq.DSLContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static ch.alv.batches.partnerjob.to.master.jooq.tables.OsteAdmin.OSTE_ADMIN;

/**
 * AutoConfig of the OsteAdminRecord import job.
 *
 * Currently it contains logic to support the listed partners:
 * - federal administration (source is a remote xml file grabbed via http, written to table 'OSTE_ADMIN')
 *
 * @since 1.0.0
 */
@Configuration
@ComponentScan("ch.alv.batches.commons")
@EnableBatchProcessing
public class PartnerJobToMasterConfiguration {

    public final static String IMPORT_PARTNERJOB_JOB = "partnerJobsImportJob";

    @Value("${ch.alv.batch.master.partnerjob.prospective.url}")
    private String prospectiveHttpUrl;

    @Value("${ch.alv.batch.master.partnerjob.prospective.chunkSize:100}")
    private Integer prospectiveChunkSize;

    @Resource
    private DSLContext jooq;

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;

    @Bean(name = IMPORT_PARTNERJOB_JOB)
    public Job importAdminJobsJob() throws IOException, URISyntaxException, SQLException {
        return jobs.get(IMPORT_PARTNERJOB_JOB)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(truncateDbTableStep())
                .next(prospectiveXmlToDbImportStep())
                .end()
                .build();
    }

    private Step truncateDbTableStep() {
        Tasklet t = (contribution, context) -> {
            jooq.truncate(OSTE_ADMIN).execute();
            return RepeatStatus.FINISHED;
        };

        return steps.get("truncateStagingTableStep")
                .tasklet(t)
                .build();
    }

    private Step prospectiveXmlToDbImportStep() throws MalformedURLException, URISyntaxException, SQLException {
        Jaxb2Marshaller prospectiveXmlJobUnmarshaller = new Jaxb2Marshaller();
        prospectiveXmlJobUnmarshaller.setPackagesToScan("ch.alv.batches.partnerjob.to.master.jaxb");

        StaxEventItemReader<ProspectiveJob> prospectiveXmlReader = new StaxEventItemReader<>();
        prospectiveXmlReader.setFragmentRootElementName("inserat");
        prospectiveXmlReader.setResource(new UrlResource(prospectiveHttpUrl));
        prospectiveXmlReader.setUnmarshaller(prospectiveXmlJobUnmarshaller);

        return steps.get("prospectiveXmlToDbImportStep")
                .<ProspectiveJob, OsteAdminRecord>chunk(prospectiveChunkSize)
                .reader(prospectiveXmlReader)
                .processor(new ProspectiveJobToAdminJobConverter())
                .writer(new JooqBatchWriter(jooq))
                .build();
    }

}
