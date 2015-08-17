package ch.alv.batches.commons.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchWebTestApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBatchWebTestApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

}