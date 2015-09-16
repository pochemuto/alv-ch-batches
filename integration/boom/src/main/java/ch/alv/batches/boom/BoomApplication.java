package ch.alv.batches.boom;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@SpringBootApplication
@ComponentScan(
        "ch.alv.batches.company.to.master, " +
        "ch.alv.batches.partnerjob.to.master, " +
        "ch.alv.batches.master.to.jobdesk, " +
        "ch.alv.batches.commons.config" +
        "ch.alv.batches.commons.web"
)
public class BoomApplication {

    // FIXME configuration attributes managed in module?
    @Value(("${ch.alv.jobdesk.elasticsearch.host}"))
    private String jobdeskElasticsearchHost;
    @Value(("${ch.alv.jobdesk.elasticsearch.port:9300}"))
    private int jobdeskElasticsearchPort;

    @Bean
    Client jobdeskElasticSearch() {
        final ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
        TransportClient transportClient = new TransportClient(settings);
        transportClient = transportClient.addTransportAddress(
                new InetSocketTransportAddress(jobdeskElasticsearchHost, jobdeskElasticsearchPort));
        return transportClient;
    }

    // Mmmmh, this could also just "link" to master datasource bean... as primary
    @Primary // FIXME put this stuff in a common ? (and deal a) embedded, b) persisted)
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource defaultSpringDataSource() {
        return DataSourceBuilder.create().build();
    }


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BoomApplication.class);
        app.run(args);
    }

}

