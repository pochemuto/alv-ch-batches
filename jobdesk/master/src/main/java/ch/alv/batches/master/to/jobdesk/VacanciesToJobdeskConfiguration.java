package ch.alv.batches.master.to.jobdesk;

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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.net.MalformedURLException;
import java.sql.SQLException;

import static ch.alv.batches.master.to.jobdesk.jooq.tables.Job.JOB;

@Configuration
public class VacanciesToJobdeskConfiguration extends MasterToJobdeskConfiguration {

    public final static String ELASTICSEARCH_TYPE = "job";
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
        JdbcCursorItemReader<JobRecord> reader = new JdbcCursorItemReader<>();
        reader.setSql(jooq.selectFrom(JOB).getSQL());
        reader.setRowMapper(new BeanPropertyRowMapper<>(JobRecord.class));
        reader.setDataSource(alvchMasterDataSource);
        return reader;
    }

    private ItemProcessor<JobRecord, JobdeskJob> jobRecordToJobdeskJobConverter() {
        return new JobRecordToJobdeskJobConverter();
    }

    private ItemWriter<JobdeskJob> jobdeskJobElasticSearchItemWriter() {
        return list -> {
            BulkRequestBuilder bulkRequest = elasticsearchClient.prepareBulk();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            list.forEach(job -> {
                try {
                    bulkRequest.add(elasticsearchClient.prepareIndex(elasticsearchIndexName, ELASTICSEARCH_TYPE,
                            job.getJobId()).setSource(ow.writeValueAsString(job)));
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
