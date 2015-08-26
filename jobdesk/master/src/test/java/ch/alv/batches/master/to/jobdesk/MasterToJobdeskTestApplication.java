package ch.alv.batches.master.to.jobdesk;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ch.alv.batches")
@EnableBatchProcessing(modular = true)
public class MasterToJobdeskTestApplication {

    /**
     * Main method, used to run the application.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MasterToJobdeskTestApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

}

