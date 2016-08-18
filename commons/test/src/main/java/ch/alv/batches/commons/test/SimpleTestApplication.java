package ch.alv.batches.commons.test;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;


/**
 * This simple Spring Boot application can be used to test any
 * Spring JavaConfig based on "ch.alv.batches" subpackages.
 *
 * SimpleTestApplication initialize an in-memory embedded database
 * as Primary Bean of the DataSource type. This way, SpringBatch metadata
 * are always stored in memory, even if additional DataSource beans are used
 * as input or output data sources.
 */
@SpringBootApplication
@ComponentScan("ch.alv.batches")
@EnableBatchProcessing(modular = false)
public class SimpleTestApplication {

    @Primary
    @Bean
    public DataSource defaultSpringDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setName("alv-in-memory").build();
        return db;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SimpleTestApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

}