package ch.alv.batches.commons.test;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * This simple Spring Boot application can be used to test any Spring JavaConfig based on "ch.alv.batches" subpackages
 */
@SpringBootApplication
@ComponentScan("ch.alv.batches")
@EnableBatchProcessing
public class SpringBatchTestApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBatchTestApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

}