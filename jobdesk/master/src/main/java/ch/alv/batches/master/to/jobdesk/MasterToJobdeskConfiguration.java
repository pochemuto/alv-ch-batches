package ch.alv.batches.master.to.jobdesk;


import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration of the master-to-jobdesk module
 *
 * @since 1.0.0
 */
@Configuration
public class MasterToJobdeskConfiguration {

    public final static String JOB_NAME_JOBS_CREATE_FULL_INDEX = "createFullVacanciesIndexJob";
    public final static String STEP_NAME_JOBS_CREATE_FULL_INDEX = "createFullVacanciesIndexStep";
    public static final String JOB_RECORD_JDBC_ITEM_READER = "jobRecordJdbcItemReader";
    public static final String JOB_RECORD_TO_JOBDESK_JOB_CONVERTER = "jobRecordToJobdeskJobConverter";
    public static final String JOBDESK_JOB_ELASTICSEARCH_ITEM_WRITER = "jobdeskJobElasticSearchItemWriter";

    public final static String JOB_NAME_LOCATIONS_CREATE_FULL_INDEX = "createFullLocationIndexJob";
    public final static String STEP_NAME_LOCATIONS_CREATE_FULL_INDEX = "createFullLocationIndexStep";
    public static final String LOCATION_RECORD_JDBC_ITEM_READER = "locationRecordJdbcItemReader";
    public static final String LOCATION_RECORD_TO_JOBDESK_LOCATION_CONVERTER = "locationRecordToJobdeskLocationConverter";
    public static final String JOBDESK_LOCATION_ELASTICSEARCH_ITEM_WRITER = "jobdeskLocationElasticSearchItemWriter";

    @Bean
    public ApplicationContextFactory createFullJobIndexJob() {
        return new GenericApplicationContextFactory(CreateFullJobIndexJobConfiguration.class);
    }

    @Bean
    public ApplicationContextFactory createFullLocationIndexJob() {
        return new GenericApplicationContextFactory(CreateFullLocationIndexJobConfiguration.class);
    }

    @Bean
    public Client elasticsearchClient() {
        final ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
        TransportClient transportClient = new TransportClient(settings);
        transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
        return transportClient;
    }


}
