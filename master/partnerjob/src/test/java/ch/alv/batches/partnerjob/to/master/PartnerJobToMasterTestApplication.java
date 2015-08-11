package ch.alv.batches.partnerjob.to.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PartnerJobToMasterTestApplication {

    /**
     * Main method, used to run the application.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PartnerJobToMasterTestApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

}

