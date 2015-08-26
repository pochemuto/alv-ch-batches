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
import org.elasticsearch.client.Client;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.sql.SQLException;

import static ch.alv.batches.master.to.jobdesk.jooq.Tables.JOB;

/**
 * Created by stibe on 26.08.15.
 */
@Configuration
public class CreateFullJobIndexJobConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CreateFullJobIndexJobConfiguration.class);

    @Value("${ch.alv.batches.master.jobdesk.esIndexName:jobdesk}")
    private String indexName;

    @Value("${ch.alv.batches.master.jobdesk.job.esType:job}")
    private String typeName;

    @Value("${ch.alv.batches.master.jobdesk.job.chunkSize:250}")
    private int chunkSize;

    @Resource
    private DSLContext jooq;

    @Resource
    private DataSource dataSource;

    @Resource
    private Client elasticsearchClient;

    @Resource
    private StepBuilderFactory stepsBuilder;

    @Resource
    private JobBuilderFactory jobsBuilder;

    @Resource
    private JobRepository repo;

    @Bean(name = MasterToJobdeskConfiguration.JOB_NAME_JOBS_CREATE_FULL_INDEX)
    public Job createFullJobIndexJob() throws MalformedURLException, SQLException {
        return jobsBuilder.get(MasterToJobdeskConfiguration.JOB_NAME_JOBS_CREATE_FULL_INDEX)
                .repository(repo)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .start(createFullJobdeskJobIndexStep())
                .build();
    }

    @Bean(name = MasterToJobdeskConfiguration.STEP_NAME_JOBS_CREATE_FULL_INDEX)
    public Step createFullJobdeskJobIndexStep() {
        return stepsBuilder.get(MasterToJobdeskConfiguration.STEP_NAME_JOBS_CREATE_FULL_INDEX)
                .<JobRecord, JobdeskJob>chunk(chunkSize)
                .reader(jobRecordJdbcItemReader())
                .processor(jobRecordToJobdeskJobConverter())
                .writer(jobdeskJobElasticSearchItemWriter())
                .build();
    }

    @Bean(name = MasterToJobdeskConfiguration.JOB_RECORD_JDBC_ITEM_READER)
    public ItemReader<JobRecord> jobRecordJdbcItemReader() {
        JdbcCursorItemReader<JobRecord> reader = new JdbcCursorItemReader<>();
        reader.setSql(jooq.selectFrom(JOB).getSQL());
        reader.setRowMapper(new BeanPropertyRowMapper<>(JobRecord.class));
        reader.setDataSource(dataSource);
        return reader;
    }

    @Bean(name = MasterToJobdeskConfiguration.JOB_RECORD_TO_JOBDESK_JOB_CONVERTER)
    public ItemProcessor<JobRecord, JobdeskJob> jobRecordToJobdeskJobConverter() {
        return new JobRecordToJobdeskJobConverter();
    }

    @Bean(name = MasterToJobdeskConfiguration.JOBDESK_JOB_ELASTICSEARCH_ITEM_WRITER)
    public ItemWriter<JobdeskJob> jobdeskJobElasticSearchItemWriter() {
        return list -> {
            BulkRequestBuilder bulkRequest = elasticsearchClient.prepareBulk();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            list.forEach(job -> {
                try {
                    bulkRequest.add(elasticsearchClient.prepareIndex(indexName, typeName, job.getJobId()).setSource(ow.writeValueAsString(job)));
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
