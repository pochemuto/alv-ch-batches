package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.commons.sql.JdbcCursorItemReaderFactory;
import ch.alv.batches.master.to.jobdesk.jooq.tables.records.LocationRecord;
import ch.alv.batches.master.to.jobdesk.model.JobdeskLocation;
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

import java.sql.SQLException;

import static ch.alv.batches.master.to.jobdesk.jooq.Tables.*;

@Configuration
public class LocationsToJobdeskConfiguration extends AnyMasterEntityToJobdeskConfiguration {

    public final static String ELASTICSEARCH_TYPE = "location";
    public final static String BATCH_JOB_LOAD_LOCATIONS = "loadAllLocationsIntoJobdeskJob";
    public final static String BATCH_STEP_LOAD_LOCATIONS = "loadAllLocationsIntoJobdeskStep";

    private static final Logger log = LoggerFactory.getLogger(LocationsToJobdeskConfiguration.class);

    @Value("${ch.alv.batches.chunkSizes.location:250}")
    private int chunkSize;

    @Bean(name = BATCH_JOB_LOAD_LOCATIONS)
    public Job createFullLocationIndexJob() throws SQLException {
        return jobs.get(BATCH_JOB_LOAD_LOCATIONS)
                .incrementer(new RunIdIncrementer())
                //.preventRestart()
                .start(createFullLocationIndexStep())
                .build();
    }

    private Step createFullLocationIndexStep() {
        return steps.get(BATCH_STEP_LOAD_LOCATIONS)
                .<LocationRecord, JobdeskLocation>chunk(chunkSize)
                .reader(locationRecordJdbcItemReader())
                .processor(locationRecordToJobdeskLocationConverter())
                .writer(jobdeskLocationElasticSearchItemWriter())
                .build();
    }

    private ItemReader<LocationRecord> locationRecordJdbcItemReader() {
        return JdbcCursorItemReaderFactory.buildJdbcCursorItemReader(
                LocationRecord.class,
                jooq.selectFrom(LOCATION).getSQL(),
                chunkSize, // FIXME see chunk setting in Step definition
                alvchMasterDataSource);
    }

    private ItemProcessor<LocationRecord, JobdeskLocation> locationRecordToJobdeskLocationConverter() {
        return new LocationRecordToJobdeskLocationConverter();
    }

    private ItemWriter<JobdeskLocation> jobdeskLocationElasticSearchItemWriter() {
        return list -> {
            BulkRequestBuilder bulkRequest = elasticsearchClient.prepareBulk();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            list.forEach(location -> {
                try {
                    bulkRequest.add(
                            elasticsearchClient
                                    .prepareIndex(getTargetIndex(),
                                            ELASTICSEARCH_TYPE,
                                            location.getCoords().toString())
                                    .setSource(ow.writeValueAsString(location)));
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
                            log.error("Failed to add a location to the es index. index: {}, type: {}, id: {}, failure: {}",
                                    response.getIndex(),
                                    response.getType(),
                                    response.getId(),
                                    response.getFailureMessage());
                        } else {
                            log.trace("Successfully added a location to the es index. index: {}, type: {}, id: {}",
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
