package ch.alv.batches.companydata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompanyImportTestApplication {

    /**
     * Main method, used to run the application.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CompanyImportTestApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

}

