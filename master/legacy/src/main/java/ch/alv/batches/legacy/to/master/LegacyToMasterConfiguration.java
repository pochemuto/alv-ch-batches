package ch.alv.batches.legacy.to.master;

import ch.alv.batches.commons.sql.JdbcReaderJooqWriterStepFactory;
import ch.alv.batches.legacy.to.master.jooq.tables.records.JobLocationRecord;
import ch.alv.batches.legacy.to.master.jooq.tables.records.JobRecord;
import ch.alv.batches.legacy.to.master.jooq.tables.records.LocationRecord;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import static ch.alv.batches.legacy.to.master.jooq.Tables.*;
/**
 *
 */
@Configuration
@ComponentScan("ch.alv.batches.commons")
@EnableConfigurationProperties
public class LegacyToMasterConfiguration {

    public final static String LEGACY_PREFIX = "importLegacy";
    public final static String IMPORT_X28_JOBS = LEGACY_PREFIX + "-X28Jobs";

    protected final static String JOOQ_PACKAGES_PATH = "ch.alv.batches.legacy.to.master.jooq.tables.records";
    // LegacyToMasterConfiguration.class.getName()

    @Resource
    private DSLContext jooq;

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;

    private JdbcReaderJooqWriterStepFactory jrjwFactory = null;

    @Bean(name = "alvchLegacyDataSource")
    @ConfigurationProperties(prefix="ch.alv.legacy.database")
    public DataSource legacyDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "alvchMasterDataSource")
    @ConfigurationProperties(prefix="ch.alv.master.database")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }


    @PostConstruct()
    public void init() {
        jrjwFactory = new JdbcReaderJooqWriterStepFactory(legacyDataSource(), jooq, steps);
    }

    @Bean(name = IMPORT_X28_JOBS)
    public Job buildImportX28Job() throws ClassNotFoundException {
        return jobs.get(IMPORT_X28_JOBS)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(truncateTableStep())
                .next(importLegacyX28JobsStep())
                // FIXME: should elegantly fail when location table is empty !!!
                .next(mapJobLocationsStep())     // TODO this step could/should be part of a distinct batch
                .end()
                .build();
    }

    private Step truncateTableStep() {
        Tasklet t = (contribution, context) -> {
            jooq.truncate(JOB).cascade().execute();

            return RepeatStatus.FINISHED;
        };

        return steps.get(IMPORT_X28_JOBS + "-truncateJobTableStep")
                .tasklet(t)
                .build();
    }

    private Step importLegacyX28JobsStep() throws ClassNotFoundException {

        String className =  JOOQ_PACKAGES_PATH + ".JobRecord";

        Integer chunkSize = 1000;
        String selectQuery = "select\n" +
                "    ID as JOB_ID,\n" +
                "    STELLENNUMMER_AVAM as JOB_ID_AVAM,\n" +
                "    STELLENNUMMER_EGOV as JOB_ID_EGOV,\n" +
                "    URL,\n" +
                "    BEZEICHNUNG as TITLE_DE,\n" +
                "    BEZEICHNUNG as TITLE_FR,\n" +
                "    BEZEICHNUNG as TITLE_IT,\n" +
                "    BEZEICHNUNG as TITLE_EN,\n" +
                "    BESCHREIBUNG as DESCRIPTION_DE,\n" +
                "    BESCHREIBUNG as DESCRIPTION_FR,\n" +
                "    BESCHREIBUNG as DESCRIPTION_IT,\n" +
                "    BESCHREIBUNG as DESCRIPTION_EN,\n" +
                "    to_date(STELLENANTRITT, 'YYYY-MM-DD-HH24:MI:SS.\"000000\"') as START_DATE,\n" +
                "    to_date(VERTRAGSDAUER, 'YYYY-MM-DD-HH24:MI:SS.\"000000\"') as END_DATE,\n" +
                "    PENSUM_VON as QUOTA_FROM,\n" +
                "    PENSUM_BIS as QUOTA_TO,\n" +
                "    ARBEITSORT_TEXT as LOCATION_REMARKS_DE,\n" +
                "    ARBEITSORT_TEXT as LOCATION_REMARKS_FR,\n" +
                "    ARBEITSORT_TEXT as LOCATION_REMARKS_IT,\n" +
                "    ARBEITSORT_TEXT as LOCATION_REMARKS_EN,\n" +
                "    ARBEITSORT_PLZ as WORKPLACE_ZIP,\n" +
                "    BEWER_SCHRIFTLICH_B as APPLICATION_WRITTEN,\n" +
                "    BEWER_ELEKTRONISCH_B as APPLICATION_ELECTRONICAL,\n" +
                "    BEWER_TELEFONISCH_B as APPLICATION_PHONE,\n" +
                "    BEWER_PERSOENLICH_B as APPLICATION_PERSONAL,\n" +
                "    UNT_NAME as COMPANY_NAME,\n" +
                "    TRIM(CONCAT(UNT_STRASSE, CONCAT(' ', UNT_HAUS_NR))) as COMPANY_ADDRESS,\n" +
                "    UNT_LAND as COMPANY_COUNTRY,\n" +
                "    UNT_PLZ as COMPANY_ZIP,\n" +
                "    UNT_ORT as COMPANY_CITY,\n" +
                "    UNT_POSTFACH as COMPANY_POSTBOX,\n" +
                "    UNT_POSTFACH_PLZ as COMPANY_POSTBOX_ZIP,\n" +
                "    UNT_POSTFACH_ORT as COMPANY_POSTBOX_CITY,\n" +
                "    UNT_TELEFON as COMPANY_PHONE,\n" +
                "    UNT_EMAIL as COMPANY_EMAIL,\n" +
                "    UNT_URL as  COMPANY_URL,\n" +
                "    KP_ANREDE_CODE as CONTACT_GENDER,\n" +
                "    KP_VORNAME as CONTACT_FIRST_NAME,\n" +
                "    KP_NAME as CONTACT_LAST_NAME,\n" +
                "    KP_TELEFON_NR as CONTACT_PHONE,\n" +
                "    KP_EMAIL as CONTACT_EMAIL,\n" +
                "    SOURCE as SOURCE,\n" +
                "    to_date(ANMELDE_DATUM, 'YYYY-MM-DD-HH24:MI:SS.\"000000\"') as PUBLICATION_DATE,\n" +
                "    ISCO_08_CODE as ISCO08_ID\n" +
                "from Z_JOBDESK2\n" +
                // FIXME remove the where condition when view is up to date!
                "where ARBEITSORT_TEXT != 'Ausland'" +
                "";

        return jrjwFactory.buildStep(IMPORT_X28_JOBS + "-fillJobTableStep", chunkSize, selectQuery, className);
    }


    private Step mapJobLocationsStep() {

        Tasklet t = (contribution, context) -> {

            // FIXME not all PLZ have an additional number of 0, e.g. 8106;2
            // FIXME there are some PLZ+ZusatzNumber duplicates !!!

            // Obtain a Cursor reference:
            Cursor<JobRecord> cursor = null;

            try {
                cursor = jooq.selectFrom(JOB)
                        .where(JOB.ID.notIn(
                                jooq.select(JOB_LOCATION.JOB_ID)
                                        .from(JOB_LOCATION)))
                        // and PLZ is not null
                        .fetchLazy();

                // Cursor has similar methods as Iterator<R>
                while (cursor.hasNext()) {
                    JobRecord job = cursor.fetchOne();
                    JobLocationRecord jl = null;

                    // mmmh, 3000 --> 3004; 1211 --> 120x; 4000 -> 4001 vs 3999
                    // TODO keep each mapped PLZ/location into a memory map
                    if (job.getWorkplaceZip() != null) {
                        Integer zipGap = 0;
                        Integer originalZip = Integer.parseInt(job.getWorkplaceZip());
                        Integer direction = -1;
                        // TODO zigzag search, starting in the good direction...
                        if (originalZip % 100 <= 3) { // FIXME <= 1,2,3 can be safer...
                            direction = 1;
                        }
                        do {
                            String zip = String.valueOf(originalZip + zipGap);
                            LocationRecord location = jooq.selectFrom(LOCATION)
                                    .where(LOCATION.ZIP.equal(zip))
                                    .orderBy(LOCATION.ZIP_ADDITIONAL_NUMBER.asc().nullsLast())
                                    .limit(1)
                                    .fetchOne();

                            if (location != null) {
                                jl = jooq.newRecord(JOB_LOCATION);
                                jl.setJobId(job.getId());
                                jl.setLocationId(location.getId());
                                break;
                            }

                            zipGap += direction;
                        } while (zipGap < 30);
                    }
                    else {
                        // Search location by Municipality Name
                        LocationRecord location = jooq.selectFrom(LOCATION)
                                .where(LOCATION.MUNICIPALITY_NAME.equal(job.getLocationRemarksDe()))
                                .orderBy(LOCATION.ZIP.asc().nullsLast(), LOCATION.ZIP_ADDITIONAL_NUMBER.asc().nullsLast())
                                .limit(1)
                                .fetchOne();

                        if (location != null) {
                            jl = jooq.newRecord(JOB_LOCATION);
                            jl.setJobId(job.getId());
                            jl.setLocationId(location.getId());
                        }
                    }
                    // TODO fallback to Canton Main City ?

                    if (jl != null) {
                        jl.store();
                        // TODO update counter
                    } else {
                        // TODO update counter and log error: no location found!
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            // Close the cursor and the cursor's underlying JDBC ResultSet
            finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            return RepeatStatus.FINISHED;
        };

        return steps.get(IMPORT_X28_JOBS + "-mapJobLocationsStep")
                .tasklet(t)
                .build();
    }
}
