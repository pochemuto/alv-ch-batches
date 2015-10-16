package ch.alv.batches.cadastre.to.master;


import org.jooq.DSLContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.File;

import static ch.alv.batches.cadastre.to.master.jooq.Tables.*;

/**
 *
 */
@Configuration
@ComponentScan("ch.alv.batches.commons")
public class CadastreToMasterConfiguration {

    public final static String CADASTRE_PREFIX = "importCadastre";
    public final static String IMPORT_LOCATIONS = CADASTRE_PREFIX + "-Locations";

    @Resource
    private DSLContext jooq;

    @Resource
    private JobBuilderFactory jobs;

    @Resource
    private StepBuilderFactory steps;

    // FIXME download from opendata web site
//    @Value("${ch.alv.batch.master.company.avgfirmen.url}")
//    private String locationCsvUrl;

    @Value("${ch.alv.cadastre.csv_path:PLZO_CSV_WGS84.csv}")
    private String plzCsvFile;

    @Bean(name = IMPORT_LOCATIONS)
    public Job buildImportLocationsJob() throws ClassNotFoundException {
        return jobs.get(IMPORT_LOCATIONS)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(truncateLocationTableStep())
                .next(loadLocationCsvFile())
                .end()
                .build();
    }

    private Step truncateLocationTableStep() {
        Tasklet t = (contribution, context) -> {
            jooq.truncate(LOCATION).cascade().execute();

            return RepeatStatus.FINISHED;
        };

        return steps.get(CADASTRE_PREFIX + "-truncateLocationTableStep")
                .tasklet(t)
                .build();
    }

    private Step loadLocationCsvFile() {
        Tasklet t = (contribution, context) -> {
            jooq.loadInto(LOCATION)
                    .bulkAll()
                    //.bulkAfter(10)
                    .onDuplicateKeyError()
                    .onErrorAbort()
                    .loadCSV(new File(plzCsvFile), "ISO-8859-1")
                    .fields(LOCATION.NAME,
                            LOCATION.ZIP,
                            LOCATION.ZIP_ADDITIONAL_NUMBER,
                            LOCATION.MUNICIPALITY_NAME,
                            LOCATION.CANTON,
                            LOCATION.LON,
                            LOCATION.LAT)
                    .ignoreRows(1)
                    .separator(';')
                    .execute();

            return RepeatStatus.FINISHED;
        };

        return steps.get(CADASTRE_PREFIX + "-loadLocationCsvFileStep")
                .tasklet(t)
                .build();
    }
}
