package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.commons.sql.JdbcCursorItemReaderFactory;
import ch.alv.batches.master.to.jobdesk.jooq.tables.records.JobRecord;
import ch.alv.batches.master.to.jobdesk.model.JobdeskJob;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.sql.SQLException;

import static ch.alv.batches.master.to.jobdesk.jooq.Tables.*;

@Configuration
public class VacanciesToJobdeskConfiguration extends MasterToJobdeskConfiguration {

    public final static String ELASTICSEARCH_TYPE = "jobs";
    public final static String BATCH_JOB_LOAD_VACANCIES = "loadAllVacanciesIntoJobdeskJob";
    public final static String BATCH_STEP_LOAD_VACANCIES = "loadAllVacanciesIntoJobdeskStep";

    private static final Logger log = LoggerFactory.getLogger(VacanciesToJobdeskConfiguration.class);

    @Value("${ch.alv.batches.chunkSizes.job:250}")
    private int chunkSize;

    @Bean(name = BATCH_JOB_LOAD_VACANCIES)
    public Job createFullJobIndexJob() throws MalformedURLException, SQLException {
        return jobs.get(BATCH_JOB_LOAD_VACANCIES)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .start(createFullJobdeskJobIndexStep())
                .build();
    }

    private Step createFullJobdeskJobIndexStep() {
        return steps.get(BATCH_STEP_LOAD_VACANCIES)
                .<JobRecord, JobdeskJob>chunk(chunkSize)
                .reader(jobRecordJdbcItemReader())
                .processor(jobRecordToJobdeskJobConverter())
                .writer(jobdeskJobElasticSearchItemWriter())
                .build();
    }

    private ItemReader<JobRecord> jobRecordJdbcItemReader() {
        return JdbcCursorItemReaderFactory.buildJdbcCursorItemReader(
                JobRecord.class,
                jooq.selectFrom(JOB)
                        .where(JOB.ID.in(jooq.select(JOB_LOCATION.JOB_ID).from(JOB_LOCATION)))
                        .getSQL(),
                chunkSize, // FIXME see chunk setting in Step definition
                alvchMasterDataSource);
    }

    private ItemProcessor<JobRecord, JobdeskJob> jobRecordToJobdeskJobConverter() {
        return new JobRecordToJobdeskJobConverter(jooq);
    }

    private ItemWriter<JobdeskJob> jobdeskJobElasticSearchItemWriter() {
        return list -> {
            BulkRequestBuilder bulkRequest = elasticsearchClient.prepareBulk();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            list.forEach(job -> {
                try {
                    bulkRequest.add(elasticsearchClient.prepareIndex(elasticsearchIndexName, ELASTICSEARCH_TYPE,
                            job.getFingerprint()).setSource(ow.writeValueAsString(job)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            bulkRequest.execute().addListener(new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkItemResponses) {

                    if (bulkItemResponses.hasFailures()) {
                        log.warn("One ore more errors have been thrown while bulk-indexing jobs. Please check the detailed item logs.");
                    }

                    for (BulkItemResponse response : bulkItemResponses.getItems()) {
                        if (response.isFailed()) {
                            log.error("Failed to add a job to the es index. indexName: {}, typeName: {}, jobId: {}, message: {}",
                                    response.getIndex(),
                                    response.getType(),
                                    response.getId(),
                                    response.getFailureMessage());
                        } else {
                            log.trace("Successfully added a job to the es index. indexName: {}, typeName: {}, jobId: {}",
                                    response.getIndex(),
                                    response.getType(),
                                    response.getId());
                        }
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    log.error("An error raised while adding jobs to the jobdesk index.", e);
                }
            });
        };
    }

}
