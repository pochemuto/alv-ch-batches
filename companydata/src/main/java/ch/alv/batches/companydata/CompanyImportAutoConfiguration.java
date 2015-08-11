package ch.alv.batches.companydata;

import ch.alv.batches.commons.sql.JooqBatchWriter;
import ch.alv.batches.commons.sql.SqlDataTypesHelper;
import ch.alv.batches.companydata.reader.FtpAvgFirmenStaxEventItemReader;
import ch.alv.batches.jooq.tables.AvgFirmen;
import ch.alv.batches.jooq.tables.AvgFirmenBatchStaging;
import ch.alv.batches.jooq.tables.records.AvgFirmenRecord;
import ch.alv.batches.jooq.tables.records.AvgFirmenBatchStagingRecord;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
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
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AutoConfig of the Company import job.
 *
 * Currently it contains logic to support the listed data providers:
 * - avg firmen (AVAMPSTS.xml / source is a remote xml file grabbed via ftp, written to table 'AVG_FIRMEN')
 *
 * @since 1.0.0
 */
@Configuration
@ComponentScan("ch.alv.batches.commons")
@EnableBatchProcessing
public class CompanyImportAutoConfiguration {

    public final static String IMPORT_COMPANIES_JOB = "importCompaniesJob";

    @Resource
    private DSLContext jooq = null;

    @Value("${ch.alv.batch.companydata.avgfirmen.url}")
    private String avgFirmenUrl;

    @Value("${ch.alv.batch.companydata.avgfirmen.chunkSize:100}")
    private Integer avgFirmenChunkSize;

    @Resource
    private DataSource dataSource;

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;

    @Bean(name = IMPORT_COMPANIES_JOB)
    public Job buildImportCompaniesJob() throws MalformedURLException, SQLException {
        return jobs.get(IMPORT_COMPANIES_JOB)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(truncateStagingTableStep())
                .next(importXmlToStagingTableStep())
                .next(updateExistingCompaniesStep())
                .next(createNewCompaniesStep())
                .next(markToDeleteCompaniesStep())
                .end()
                .build();
    }

    private Step truncateStagingTableStep() {
        Tasklet t = (contribution, context) -> {
            jooq.truncate(AvgFirmenBatchStaging.AVG_FIRMEN_BATCH_STAGING).execute();

            return RepeatStatus.FINISHED;
        };

        return steps.get("truncateStagingTableStep")
                .tasklet(t)
                .build();
    }

    private Step importXmlToStagingTableStep() throws MalformedURLException, SQLException {

        final String SCAN_PACKAGES = "ch.alv.batches.companydata.jaxb";
        final String[] JAXB_FRAGMENT_ROOT_NAMES = new String[] { "Betrieb" };

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan(SCAN_PACKAGES);

        FtpAvgFirmenStaxEventItemReader staxEventItemReader = new FtpAvgFirmenStaxEventItemReader();
        staxEventItemReader.setFragmentRootElementNames(JAXB_FRAGMENT_ROOT_NAMES);
        staxEventItemReader.setResource(new EncodedResource(new UrlResource(avgFirmenUrl), Charset.forName("UTF8")).getResource());
        staxEventItemReader.setUnmarshaller(jaxb2Marshaller);

        // TODO Why not getting rid of chunk reading here (small dataset, xml file input) ?
        return steps.get("importXmlToStagingTableStep")
                .<AvgFirmenBatchStagingRecord, UpdatableRecord<?>>chunk(avgFirmenChunkSize)
                .reader(staxEventItemReader)
                .writer(new JooqBatchWriter(dataSource.getConnection()))
                .build();
    }

    private Step updateExistingCompaniesStep() {
        Tasklet t = (contribution, context) -> {

            Map<Integer, AvgFirmenRecord> existingValidCompanies = jooq.selectFrom(AvgFirmen.AVG_FIRMEN)
                    .where(AvgFirmen.AVG_FIRMEN.TODELETE.isNull()).fetch().intoMap(AvgFirmen.AVG_FIRMEN.ID);

            // Only update companies that effectively differ
            Map<Integer, AvgFirmenRecord> companiesToUpdate = jooq.selectFrom(AvgFirmenBatchStaging.AVG_FIRMEN_BATCH_STAGING)
                    .where(AvgFirmenBatchStaging.AVG_FIRMEN_BATCH_STAGING.ID.in(
                            jooq.select(AvgFirmen.AVG_FIRMEN.ID)
                                    .from(AvgFirmen.AVG_FIRMEN)
                                    .where(AvgFirmen.AVG_FIRMEN.TODELETE.isNull())))
                    .fetchInto(AvgFirmenRecord.class).stream().filter(c -> c.compareTo(existingValidCompanies.get(c.getId())) != 0)
                    .collect(Collectors.toMap(AvgFirmenRecord::getId,
                            c -> { AvgFirmenRecord x = c.copy(); x.setId(c.getId()); return x; }));

            jooq.deleteFrom(AvgFirmen.AVG_FIRMEN)
                    .where(AvgFirmen.AVG_FIRMEN.ID.in(companiesToUpdate.keySet()))
                    .execute();

            // TODO should we verify the returned sql statuses,
            // or simply rely on Exception handling to deal with potential errors ?
            // Note that the same question applies to all other SQL query steps...
            jooq.batchInsert(companiesToUpdate.values()).execute();

            return RepeatStatus.FINISHED;
        };

        return steps.get("updateExistingCompaniesStep")
                .tasklet(t)
                .build();
    }

    private Step createNewCompaniesStep() {
        Tasklet t = (contribution, context) -> {

            List<AvgFirmenRecord> companiesToAdd = jooq.selectFrom(AvgFirmenBatchStaging.AVG_FIRMEN_BATCH_STAGING)
                    .where(AvgFirmenBatchStaging.AVG_FIRMEN_BATCH_STAGING.ID.notIn(
                            jooq.select(AvgFirmen.AVG_FIRMEN.ID)
                                    .from(AvgFirmen.AVG_FIRMEN)))
                    .fetchInto(AvgFirmenRecord.class);

            // TODO: check with jOOQ community if there is a way to get rid of this step...
            jooq.batchInsert(companiesToAdd.stream().map(c -> {
                AvgFirmenRecord x = c.copy();
                x.setId(c.getId());
                return x;
            }).collect(Collectors.toList())).execute();

            return RepeatStatus.FINISHED;
        };

        return steps.get("createNewCompaniesStep")
                .tasklet(t)
                .build();
    }

    private Step markToDeleteCompaniesStep() {
        Tasklet t = (contribution, context) -> {

            java.sql.Date now = SqlDataTypesHelper.now();

            jooq.update(AvgFirmen.AVG_FIRMEN)
                    .set(AvgFirmen.AVG_FIRMEN.TODELETE, now)
                    .where(AvgFirmen.AVG_FIRMEN.ID.notIn(
                            jooq.select(AvgFirmenBatchStaging.AVG_FIRMEN_BATCH_STAGING.ID)
                                    .from(AvgFirmenBatchStaging.AVG_FIRMEN_BATCH_STAGING)))
                    .and(AvgFirmen.AVG_FIRMEN.TODELETE.isNull())
                    .execute();

            return RepeatStatus.FINISHED;
        };

        return steps.get("markToDeleteCompaniesStep")
                .tasklet(t)
                .build();
    }

}
