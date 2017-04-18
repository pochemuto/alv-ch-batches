package ch.alv.batches.company.to.master.config;


import ch.alv.batches.commons.spring.integration.AdvancedFtpSessionFactory;
import ch.alv.batches.commons.spring.integration.AdvancedFtpSessionFactoryConfig;
import ch.alv.batches.commons.sql.JooqBatchWriter;
import ch.alv.batches.commons.sql.SqlDataTypesHelper;
import ch.alv.batches.company.to.master.jooq.tables.records.AvgFirmenImportRecord;
import ch.alv.batches.company.to.master.reader.FtpAvgFirmenStaxEventItemReader;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;

import static ch.alv.batches.company.to.master.jooq.Tables.AVG_FIRMEN;
import static ch.alv.batches.company.to.master.jooq.Tables.AVG_FIRMEN_IMPORT;

/**
 * Configuration and Implementation of the Company Import Batch job.
 *
 * As the provided AVG XML data only contains the 'active' companies, this job run in
 * two phases in order to identify and mark the 'removed/inactive' copmanies which are
 * kept in the master database.
 */
@Configuration
@ComponentScan("ch.alv.batches.commons")
public class CompanyToMasterConfiguration {

    public final static String IMPORT_COMPANIES_JOB = "importCompaniesJob";

    @Value("${ch.alv.company.download_dir:/tmp}")
    private String downloadDirectory;

    @Value("${ch.alv.batches.chunkSizes.company:1000}")
    private Integer chunkSize;

    @Autowired
    private CompanyAvgFtpConfig ftpProperties;

    @Resource
    private DSLContext jooq;

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;

    @Bean(name = IMPORT_COMPANIES_JOB)
    public Job buildImportCompaniesJob() throws Exception {
        return jobs.get(IMPORT_COMPANIES_JOB)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(downloadXmlFileViaFtpStep())
                .next(truncateStagingTableStep())
                .next(importXmlToStagingTableStep())
                .next(addOrReplaceActiveCompaniesStep())
                .next(markToDeleteCompaniesStep())
                .end()
                .build();
    }

    private Step downloadXmlFileViaFtpStep() throws Exception {
        AdvancedFtpSessionFactory ftpSessionFactory = new AdvancedFtpSessionFactory();
        AdvancedFtpSessionFactoryConfig.applyConfiguration(ftpSessionFactory, ftpProperties);
        FtpRemoteFileTemplate template = new FtpRemoteFileTemplate(ftpSessionFactory);

        Tasklet t = (contribution, context) -> {
            File file = new File(downloadDirectory + "/" + ftpProperties.getFilename());
            FileOutputStream outputStream = new FileOutputStream(file);
            template.get(ftpProperties.getFilename(),
                    inputStream -> {
                        FileCopyUtils.copy(inputStream, outputStream);
                    });
            outputStream.flush();
            outputStream.close();

            return RepeatStatus.FINISHED;
        };

        return steps.get("downloadXmlFileViaFtpStep")
                .tasklet(t)
                .build();
    }

    private Step truncateStagingTableStep() {
        Tasklet t = (contribution, context) -> {
            jooq.truncate(AVG_FIRMEN_IMPORT).execute();

            return RepeatStatus.FINISHED;
        };

        return steps.get("truncateStagingTableStep")
                .tasklet(t)
                .build();
    }

    private Step importXmlToStagingTableStep() throws MalformedURLException, SQLException {

        final String SCAN_PACKAGES = "ch.alv.batches.company.to.master.jaxb";
        final String[] JAXB_FRAGMENT_ROOT_NAMES = new String[] { "Betrieb" };

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan(SCAN_PACKAGES);

        FtpAvgFirmenStaxEventItemReader staxEventItemReader = new FtpAvgFirmenStaxEventItemReader();
        staxEventItemReader.setFragmentRootElementNames(JAXB_FRAGMENT_ROOT_NAMES);
        staxEventItemReader.setResource(new FileSystemResource(downloadDirectory + "/" + ftpProperties.getFilename()));
        staxEventItemReader.setUnmarshaller(jaxb2Marshaller);

        return steps.get("importXmlToStagingTableStep")
                .<AvgFirmenImportRecord, UpdatableRecord<?>>chunk(chunkSize)
                .reader(staxEventItemReader)
                .writer(new JooqBatchWriter(jooq))
                .build();
    }

    private Step addOrReplaceActiveCompaniesStep() {
        Tasklet t = (contribution, context) -> {
            //
            // Remove "known" active companies to import the potential updates
            //
            jooq.delete(AVG_FIRMEN)
                    .where(AVG_FIRMEN.ID.in(
                            jooq.select(AVG_FIRMEN_IMPORT.ID)
                                    .from(AVG_FIRMEN_IMPORT)))
                    .andNot(AVG_FIRMEN.TODELETE)
                    .execute();

            //
            // For consistency/safety, don't take the risk to re-import any "resurrected" company
            // Note that without this filter, the unique constraint on the primary key would prevent
            // unwanted data overwrite.
            //
            jooq.delete(AVG_FIRMEN_IMPORT)
                    .where(AVG_FIRMEN_IMPORT.ID.in(
                            jooq.select(AVG_FIRMEN.ID)
                                    .from(AVG_FIRMEN)
                                    .where(AVG_FIRMEN.TODELETE)))
                    .execute();

            //
            // Copy the active companies to the final table
            //
            jooq.insertInto(AVG_FIRMEN,
                    AVG_FIRMEN.ID,
                    AVG_FIRMEN.BETID,
                    AVG_FIRMEN.EMAIL,
                    AVG_FIRMEN.KANTON,
                    AVG_FIRMEN.NAME,
                    AVG_FIRMEN.NAME2,
                    AVG_FIRMEN.ORT,
                    AVG_FIRMEN.PLZ,
                    AVG_FIRMEN.STRASSE,
                    AVG_FIRMEN.TELEFONNUMMER,
                    AVG_FIRMEN.TODELETE
            ).select(jooq.select(
                    AVG_FIRMEN_IMPORT.ID,
                    AVG_FIRMEN_IMPORT.BETID,
                    AVG_FIRMEN_IMPORT.EMAIL,
                    AVG_FIRMEN_IMPORT.KANTON,
                    AVG_FIRMEN_IMPORT.NAME,
                    AVG_FIRMEN_IMPORT.NAME2,
                    AVG_FIRMEN_IMPORT.ORT,
                    AVG_FIRMEN_IMPORT.PLZ,
                    AVG_FIRMEN_IMPORT.STRASSE,
                    AVG_FIRMEN_IMPORT.TELEFONNUMMER,
                    DSL.val(false).as(AVG_FIRMEN.TODELETE)
            ).from(AVG_FIRMEN_IMPORT))
                    .execute();

            return RepeatStatus.FINISHED;
        };

        return steps.get("insertActiveCompaniesStep")
                .tasklet(t)
                .build();
    }

    private Step markToDeleteCompaniesStep() {
        Tasklet t = (contribution, context) -> {
            jooq.update(AVG_FIRMEN)
                    .set(AVG_FIRMEN.TODELETE, true)
                    .set(AVG_FIRMEN.TODELETE_SINCE, SqlDataTypesHelper.now())

                    .where(AVG_FIRMEN.ID.notIn(
                            jooq.select(AVG_FIRMEN_IMPORT.ID)
                                    .from(AVG_FIRMEN_IMPORT)))
                    .and(AVG_FIRMEN.TODELETE.isFalse().or(AVG_FIRMEN.TODELETE.isNull()))

                    .execute();

            return RepeatStatus.FINISHED;
        };

        return steps.get("markToDeleteCompaniesStep")
                .tasklet(t)
                .build();
    }

}
