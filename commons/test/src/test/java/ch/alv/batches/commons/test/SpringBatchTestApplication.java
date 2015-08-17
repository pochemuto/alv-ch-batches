package ch.alv.batches.commons.test;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchTestApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBatchTestApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

}