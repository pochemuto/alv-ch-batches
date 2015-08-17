package ch.alv.batches.company.to.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompanyToMasterTestApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CompanyToMasterTestApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

}

