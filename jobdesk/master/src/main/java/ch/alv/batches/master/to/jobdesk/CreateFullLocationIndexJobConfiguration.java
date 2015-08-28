package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.master.to.jobdesk.jooq.tables.records.LocationRecord;
import ch.alv.batches.master.to.jobdesk.model.JobdeskLocation;
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

import static ch.alv.batches.master.to.jobdesk.jooq.tables.Location.LOCATION;

/**
 * Created by stibe on 26.08.15.
 */
@Configuration
public class CreateFullLocationIndexJobConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MasterToJobdeskConfiguration.class);

    @Value("${ch.alv.batches.master.jobdesk.esIndexName:jobdesk}")
    private String indexName;

    @Value("${ch.alv.batches.master.jobdesk.location.esType:locations}")
    private String typeName;

    @Value("${ch.alv.batches.master.jobdesk.location.chunkSize:250}")
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

    @Bean(name = MasterToJobdeskConfiguration.JOB_NAME_LOCATIONS_CREATE_FULL_INDEX)
    public Job createFullLocationIndexJob() throws MalformedURLException, SQLException {
        return jobsBuilder.get(MasterToJobdeskConfiguration.JOB_NAME_LOCATIONS_CREATE_FULL_INDEX)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .start(createFullLocationIndexStep())
                .build();
    }

    @Bean(name = MasterToJobdeskConfiguration.STEP_NAME_LOCATIONS_CREATE_FULL_INDEX)
    public Step createFullLocationIndexStep() {
        return stepsBuilder.get(MasterToJobdeskConfiguration.STEP_NAME_LOCATIONS_CREATE_FULL_INDEX)
                .<LocationRecord, JobdeskLocation>chunk(chunkSize)
                .reader(locationRecordJdbcItemReader())
                .processor(locationRecordToJobdeskLocationConverter())
                .writer(jobdeskLocationElasticSearchItemWriter())
                .build();
    }

    @Bean(name = MasterToJobdeskConfiguration.LOCATION_RECORD_JDBC_ITEM_READER)
    public ItemReader<LocationRecord> locationRecordJdbcItemReader() {
        JdbcCursorItemReader<LocationRecord> reader = new JdbcCursorItemReader<>();
        reader.setSql(jooq.selectFrom(LOCATION).getSQL());
        reader.setRowMapper(new BeanPropertyRowMapper<>(LocationRecord.class));
        reader.setDataSource(dataSource);
        return reader;
    }

    @Bean(name = MasterToJobdeskConfiguration.LOCATION_RECORD_TO_JOBDESK_LOCATION_CONVERTER)
    public ItemProcessor<LocationRecord, JobdeskLocation> locationRecordToJobdeskLocationConverter() {
        return new LocationRecordToJobdeskLocationConverter();
    }

    @Bean(name = MasterToJobdeskConfiguration.JOBDESK_LOCATION_ELASTICSEARCH_ITEM_WRITER)
    public ItemWriter<JobdeskLocation> jobdeskLocationElasticSearchItemWriter() {
        return list -> {
            BulkRequestBuilder bulkRequest = elasticsearchClient.prepareBulk();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            list.forEach(location -> {
                try {
                    bulkRequest.add(elasticsearchClient.prepareIndex(indexName, typeName, location.getZip()).setSource(ow.writeValueAsString(location)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            bulkRequest.execute().addListener(new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkItemResponses) {

                    if (bulkItemResponses.hasFailures()) {
                        log.warn("One ore more errors have been thrown while bulk-indexing location data. Please check the detailed item logs.");
                    }

                    for (BulkItemResponse response : bulkItemResponses.getItems()) {
                        if (response.isFailed()) {
                            log.error("Failed to add a location to the es index. indexName: {}, typeName: {}, zip: {}, failure: {}",
                                    response.getIndex(),
                                    response.getType(),
                                    response.getId(),
                                    response.getFailureMessage());
                        } else {
                            log.trace("Successfully added a location to the es index. indexName: {}, typeName: {}, jobId: {}",
                                    response.getIndex(),
                                    response.getType(),
                                    response.getId());
                        }
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    log.error("An error raised while adding locations to the jobdesk index.", e);
                }
            });
        };
    }

}
