package ch.alv.batches.partnerjob.to.master.config;

import ch.alv.batches.commons.sql.JooqBatchWriter;
import ch.alv.batches.partnerjob.to.master.batch.ProspectiveJobToPartnerJobConverter;
import ch.alv.batches.partnerjob.to.master.batch.ProspectiveJobXmlItemReader;
import ch.alv.batches.partnerjob.to.master.jaxb.prospective.Inserat;
import ch.alv.batches.partnerjob.to.master.jooq.tables.records.OstePartnerRecord;
import org.jooq.DSLContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;

import static ch.alv.batches.partnerjob.to.master.jooq.Tables.OSTE_PARTNER;

/**
 * Import partner's job offers into OSTE_PARTNER table from the following kind of sources:
 *  - Remote XML file grabbed via http
 *  - (more to come...)
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ch.alv.partnerjob")
@ComponentScan("ch.alv.batches.commons")
public class PartnerJobToMasterConfiguration implements InitializingBean {

    public final static String IMPORT_PARTNERJOB_JOB = "partnerJobsImportJob";

    public final static String BATCH_JOB_PARAMETER_PARTNER_CODE = "partnerCode";

    @Value("${ch.alv.batches.chunkSizes.partnerjob:100}") // FIXME define a common constant for the default fallback?
    private Integer prospectiveChunkSize;

    private HashMap<String, Partner> sources;

    @Resource
    private DSLContext jooq;

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;

    public HashMap<String, Partner> getSources() {
        return sources;
    }

    public void setSources(HashMap<String, Partner> sources) {
        this.sources = sources;
    }



    @Bean(name = IMPORT_PARTNERJOB_JOB)
    public Job importPartnerJobsJob() throws IOException, URISyntaxException, SQLException {
        return jobs.get(IMPORT_PARTNERJOB_JOB)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(deleteExistingPartnerJobsStep())
                .next(fetchAndImportNewPartnerJobsStep())
                .end()
                .build();
    }

    public void afterPropertiesSet() throws Exception {
        sources.keySet().forEach(k -> sources.get(k).setCode(k));
    }

    private Step deleteExistingPartnerJobsStep() {
        Tasklet t = (contribution, context) -> {
            String partnerCode = (String) context.getStepContext().getJobParameters().get(BATCH_JOB_PARAMETER_PARTNER_CODE);
            jooq.delete(OSTE_PARTNER).where(OSTE_PARTNER.QUELLE.equal(partnerCode)).execute();
            return RepeatStatus.FINISHED;
        };

        return steps.get("deleteExistingPartnerJobsStep")
                .tasklet(t)
                .build();
    }

    private Step fetchAndImportNewPartnerJobsStep() throws MalformedURLException, URISyntaxException, SQLException {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setPackagesToScan("ch.alv.batches.partnerjob.to.master.jaxb.prospective");

        ProspectiveJobXmlItemReader partnerjobXmlReader = new ProspectiveJobXmlItemReader(sources);
        partnerjobXmlReader.setFragmentRootElementName("inserat");
        partnerjobXmlReader.setUnmarshaller(unmarshaller);

        return steps.get("fetchAndImportNewPartnerJobsStep")
                .<Inserat, OstePartnerRecord>chunk(prospectiveChunkSize)
                .reader(partnerjobXmlReader)
                .processor(new ProspectiveJobToPartnerJobConverter())
                .writer(new JooqBatchWriter(jooq))
                .build();
    }

}
